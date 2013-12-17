package uprove.wallets;

import com.microsoft.uprove.*;
import com.sun.org.apache.xml.internal.security.exceptions.Base64DecodingException;
import com.sun.org.apache.xml.internal.security.utils.Base64;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import uprove.Helper;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created with IntelliJ IDEA.
 * User: andersfogbunzel
 * Date: 14/12/13
 * Time: 23.48
 */
public class WalletsServlet extends HttpServlet {
    private static int numberOfTokens;
    private int numberOfAttributes;
    private byte[][] attributes;
    private IssuerParameters ip;
    private UProveKeyAndToken[] upkt;
    private boolean STATE_READY = false;
    private static String token;
    private byte[] message;
    private UProveKeyAndToken keyAndToken;
    private static String document;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("WalletsServlet received wallets message");

        // receive JSON object
        JSONObject message_tokens = Helper.parseToJSON(request.getParameter("json"));

        // receive token parameters and other parameters
        this.numberOfTokens = ((Long) message_tokens.get("numberOfTokens")).intValue();
        this.numberOfAttributes = ((Long) message_tokens.get("numberOfAttributes")).intValue();
        try {
            this.ip = (IssuerParameters) Helper.fromString(((String) message_tokens.get("ip")).replace(" ", "+"));
            this.upkt = (UProveKeyAndToken[]) Helper.fromString(((String) message_tokens.get("keyAndToken")).replace(" ", "+"));
            this.attributes = new byte[this.numberOfAttributes][];
            for (int i = 0; i < this.numberOfAttributes; i++) {
                String attributeParameter = "attribute_" + Integer.toString(i + 1);
                this.attributes[i] = Base64.decode((String) message_tokens.get(attributeParameter));
            }
        } catch (Base64DecodingException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        // prover and verifier should validate the issuer parameters upon reception
        this.ip.validate();

        //set state (used by token wallets)
        STATE_READY = true;
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // check if user still have wallets left
        if (this.numberOfTokens == 0) {
            System.out.println("No valid tokens left");
            response.sendRedirect(Helper.getServletPath()+"/token-wallets/?numberOfTokens=invalid");
        } else {
            if (request.getParameter("message").equals("present_token")) {
                // get service provider
                String serviceProvider = request.getParameter("service_provider");

                // get document to sign
                if (request.getParameter("document") != null)
                    document = request.getParameter("document");

                // get disclosedAttributes
                String disclosedAttributes = getAttributes(request.getParameter("attribute_1"),
                        request.getParameter("attribute_2"), request.getParameter("attribute_3"));

                // token presentation
                JSONObject message_token = createToken(disclosedAttributes, serviceProvider);

                // protocol parameters (shared by prover and verifier)
                int[] disclosed = getDisclosed(disclosedAttributes);

                // prover generates the presentation proof
                PresentationProof proof = PresentationProtocol.generatePresentationProof(this.ip, disclosed, this.message, null, this.keyAndToken, this.attributes);
                message_token.put("proof", Helper.toString(proof)); // added Serializable in PresentationProof because need to send UproveToken to VerifierServlet

                // send message by POST
                String url = request.getRequestURL().substring(0, request.getRequestURL().length() - request.getServletPath().length())
                        + "/VerifierServlet";
                String urlParameters = "json=" + message_token.toJSONString() + "&token=online";

                // send post request
                HttpURLConnection con = Helper.requestURL(url, urlParameters);

                System.out.println("WalletsServlet sent an U-Prove token to " + serviceProvider + " (response code: " + con.getResponseCode() + ")");

                System.out.println("WalletsServlet redirect user to " + serviceProvider + "'s web page");
                response.sendRedirect(con.getHeaderField("service_provider"));
            } else if (request.getParameter("message").equals("present_local_token")) {
                // clear this.token (because only allowed to use a token once)
                token = "";

                String serviceProvider = request.getParameter("service_provider");
                String token = request.getParameter("token"); // token presentation

                // get disclosedAttributes
                String disclosedAttributes = getAttributes(request.getParameter("attribute_1"),
                        request.getParameter("attribute_2"), request.getParameter("attribute_3"));

                // protocol parameters (shared by prover and verifier)
                int[] disclosed = getDisclosed(disclosedAttributes);

                // receive JSON object
                JSONObject message_token = null;
                try {
                    JSONParser parser = new JSONParser();
                    Object jsonObj = parser.parse(token);
                    message_token = (JSONObject) jsonObj;
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                // prover generates the presentation proof
                PresentationProof proofLocal = PresentationProtocol.generatePresentationProof(this.ip, disclosed, this.message, null, this.keyAndToken, this.attributes);
                message_token.put("disclosedAttributes", disclosedAttributes);
                message_token.put("proof", Helper.toString(proofLocal)); // added Serializable in PresentationProof because need to send UproveToken to VerifierServlet

                // send message by POST
                String url = request.getRequestURL().substring(0, request.getRequestURL().length() - request.getServletPath().length())
                        + "/VerifierServlet";
                URL obj = new URL(url);
                HttpURLConnection con = (HttpURLConnection) obj.openConnection();
                con.setRequestMethod("POST"); // add request header

                String urlParameters = "json=" + message_token.toJSONString() + "&token=local&serviceProvider=" + serviceProvider;

                // send post request
                con.setDoOutput(true);
                DataOutputStream wr = new DataOutputStream(con.getOutputStream());
                wr.writeBytes(urlParameters);
                wr.flush();
                wr.close();

                System.out.println("WalletsServlet sent an U-Prove token to " + serviceProvider + " (response code: " + con.getResponseCode() + ")");

                System.out.println("WalletsServlet redirected user to " + serviceProvider + "'s web page");
                response.sendRedirect(con.getHeaderField("service_provider"));
            } else if (request.getParameter("message").equals("get_token")) {
                if (!STATE_READY) {
                    System.out.println("Token wallets asked after a token but WalletsServlet have not received a token yet");

                    response.sendRedirect(Helper.getServletPath()+"/token-wallets/?failedAt=WalletsServlet&reason=stateNotReady");
                } else {
                    System.out.println("Token wallets asked after a token");

                    token = createToken("", "").toJSONString();
                    response.sendRedirect(Helper.getServletPath()+"/token-wallets");
                }
            }
        }
    }

    private String getAttributes(String reqAtt_1, String reqAtt_2, String reqAtt_3) {
        int attribute_1 = 0;
        if (reqAtt_1 != null)
            attribute_1 = Integer.parseInt(reqAtt_1);

        int attribute_2 = 0;
        if (reqAtt_2 != null)
            attribute_2 = Integer.parseInt(reqAtt_2);

        int attribute_3 = 0;
        if (reqAtt_3 != null)
            attribute_3 = Integer.parseInt(reqAtt_3);

        String disclosedAttributes = "";
        if (attribute_1 == 1)
            disclosedAttributes += "1";

        if (attribute_2 == 1)
            disclosedAttributes += "2";

        if (attribute_3 == 1)
            disclosedAttributes += "3";

        int disclosedLength = disclosedAttributes.length()-1;
        for (int i=disclosedLength; 0<i; i--) {
            disclosedAttributes = new StringBuffer(disclosedAttributes).insert(i, ",").toString();
        }

        return disclosedAttributes;
    }

    private int[] getDisclosed(String disclosedAttributes) {
        String[] disclosedArray = disclosedAttributes.split(",");
        int[] disclosed = new int[disclosedArray.length]; // what attributes are sent to the verifier, a list of integers identifying what attribute to sent, separated by a comma
        for (int i = 0; i < disclosedArray.length; i++) {
            disclosed[i] = Integer.parseInt(disclosedArray[i]);
        }

        return disclosed;
    }

    public JSONObject createToken(String disclosedAttributes, String serviceProvider) {
        JSONObject message_token = null;
        try {
            System.out.println("WalletsServlet picked an U-Prove token to " + serviceProvider);

            /*
             * token presentation
    		 */
            System.out.println("WalletsServlet picked an U-Prove token");

            // protocol parameters (shared by prover and verifier)
            this.message = "message".getBytes();

            // prover chooses a token to use
            this.keyAndToken = this.upkt[this.numberOfTokens - 1];

            //decrease users number of wallets
            decNumberOfTokens();

            // prover transmits the U-Prove token and presentation proof to the verifier
            UProveToken token = this.keyAndToken.getToken();

            // create JSON object
            message_token = new JSONObject();
            message_token.put("serviceProvider", serviceProvider);
            message_token.put("ip", Helper.toString(this.ip)); // added Serializable in IssuerParameters, PrimeOrderGroup and FieldZq because need to send IssuerParameters to ProverServlet and VerifierServlet
            message_token.put("message", Base64.encode(this.message));
            message_token.put("token", Helper.toString(token)); // added Serializable in UProveToken because need to send UproveToken to VerifierServlet
            message_token.put("disclosedAttributes", disclosedAttributes);

        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return message_token;
    }

    public static String getToken() {
        return token;
    }

    public static String getDocument() {
        return document;
    }

    public static int getNumberOfTokens() {
        return numberOfTokens;
    }

    private void decNumberOfTokens() {
        this.numberOfTokens--;
    }

}
