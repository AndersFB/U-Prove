package uprove.prover;

import com.microsoft.uprove.*;
import com.sun.org.apache.xml.internal.security.exceptions.Base64DecodingException;
import com.sun.org.apache.xml.internal.security.utils.Base64;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import uprove.Helper;

/**
 * Created with IntelliJ IDEA.
 * User: andersfogbunzel
 * Date: 11/12/13
 * Time: 10.01
 */
public class ProverServlet extends HttpServlet {
    private int numberOfAttributes;
    private byte[][] attributes;
    private static int numberOfTokens;
    private Prover prover;
    private IssuerParameters ip;
    private UProveKeyAndToken[] upkt;

    public ProverServlet() {
        com.sun.org.apache.xml.internal.security.Init.init(); // Base64DecodingException
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getParameter("message").equals("init")) {
            System.out.println("ProverServlet received init message");

            // protocol parameters
            this.numberOfTokens = Integer.parseInt(request.getParameter("numberOfTokens"));
            this.numberOfAttributes = Integer.parseInt(request.getParameter("numberOfAttributes"));
            this.attributes = new byte[this.numberOfAttributes][];
            for (int i = 0; i < this.numberOfAttributes; i++) {
                String attributeParameter = "attribute_" + Integer.toString(i + 1);
                this.attributes[i] = request.getParameter(attributeParameter).getBytes();
            }

            // create JSON object
            JSONObject message_init = new JSONObject();
            for (int i = 0; i < this.numberOfAttributes; i++) {
                String attributeParameter = "attribute_" + Integer.toString(i + 1);
                message_init.put(attributeParameter, Base64.encode(this.attributes[i]));
            }
            message_init.put("numberOfTokens", this.numberOfTokens);
            message_init.put("numberOfAttributes", this.numberOfAttributes);

            // send message by POST
            String url = Helper.getPostURL(request, "/IssuerServlet");
            String urlParameters = "message=init&json=" + message_init.toJSONString();

            // send post request
            HttpURLConnection con = Helper.requestURL(url, urlParameters);

            // get response code
            int responseCode = con.getResponseCode();

            System.out.println("ProverServlet sent init message (response code: " + responseCode + ")");

            // notify if error (nested notification about an server error, between servlets)
            if (responseCode == 200) {
                response.sendRedirect(Helper.getServletPath()+"/token-wallets/");
            } else {
                response.sendRedirect(Helper.getServletPath()+"/prover/?failedAt=ProverServlet&response_code=" + responseCode);
            }
        } else if (request.getParameter("message").equals("1")) {
            System.out.println("ProverServlet received first message");

            // receive JSON object
            JSONObject message_1 = Helper.parseToJSON(request.getParameter("json"));

            // receive issuer parameters and other parameters
            byte[] tokenInformation = new byte[0];
            byte[][] message1 = new byte[this.numberOfTokens * 2][];
            try {
                this.ip = (IssuerParameters) Helper.fromString(((String) message_1.get("ip")).replace(" ", "+"));

                tokenInformation = Base64.decode(message_1.get("tokenInformation").toString());

                // only do numberOfTokens * 2 times
                for (int i = 0; i < this.numberOfTokens * 2; i++) {
                    String messageParameter = "message_" + Integer.toString(i + 1);
                    message1[i] = Base64.decode(((String) message_1.get(messageParameter)).replace(" ", "+"));
                }
            } catch (Base64DecodingException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

            // prover and verifier should validate the issuer parameters upon reception
            this.ip.validate();

            // protocol parameters
            byte[] proverInformation = "prover information".getBytes();

            // prover generates second issuance message
            ProverProtocolParameters proverProtocolParams = new ProverProtocolParameters();
            proverProtocolParams.setIssuerParameters(this.ip);
            proverProtocolParams.setNumberOfTokens(this.numberOfTokens);
            proverProtocolParams.setTokenAttributes(this.attributes);
            proverProtocolParams.setTokenInformation(tokenInformation);
            proverProtocolParams.setProverInformation(proverInformation);
            this.prover = proverProtocolParams.generate();
            byte[][] message2 = this.prover.generateSecondMessage(message1);

            // create JSON object
            JSONObject message_2 = new JSONObject();
            for (int i = 0; i < this.numberOfTokens; i++) {
                String messageParameter = "message_" + Integer.toString(i + 1);
                message_2.put(messageParameter, Base64.encode(message2[i]));
            }

            // send message by POST
            String url = Helper.getPostURL(request, "/IssuerServlet");
            String urlParameters = "message=2&json=" + message_2.toJSONString();

            // send post request
            HttpURLConnection con = Helper.requestURL(url, urlParameters);

            // get response code
            int responseCode = con.getResponseCode();

            // notify if error (nested notification about an server error, between servlets)
            if (responseCode != 200)
                response.setStatus(500);

            System.out.println("ProverServlet sent second message (response code: " + responseCode + ")");
        } else if (request.getParameter("message").equals("3")) {
            System.out.println("ProverServlet received third message");

            // receive JSON object
            JSONObject message_3 = Helper.parseToJSON(request.getParameter("json"));

            // receive and decode message
            byte[][] message3 = new byte[this.numberOfTokens][];
            try {
                for (int i = 0; i < this.numberOfTokens; i++) {
                    String messageParameter = "message_" + Integer.toString(i + 1);
                    message3[i] = Base64.decode(((String) message_3.get(messageParameter)).replace(" ", "+"));
                }
            } catch (Base64DecodingException e) {
                e.printStackTrace();
            }

            // prover send information to the U-Prove wallets which keep track of the tokens
            System.out.println("ProverServlet generate token");
            this.upkt = this.prover.generateTokens(message3);

            // create JSON object
            JSONObject message_tokens = new JSONObject();
            message_tokens.put("numberOfTokens", this.numberOfTokens);
            message_tokens.put("ip", Helper.toString(this.ip)); // added Serializable in IssuerParameters, PrimeOrderGroup and FieldZq because need to send IssuerParameters to ProverServlet and VerifierServlet
            message_tokens.put("keyAndToken", Helper.toString(this.upkt)); // added Serializable in UProveKeyAndToken because need to send UProveKeyAndToken to WalletsServlet
            message_tokens.put("numberOfAttributes", this.numberOfAttributes);
            for (int i = 0; i < this.numberOfAttributes; i++) {
                String attributeParameter = "attribute_" + Integer.toString(i + 1);
                message_tokens.put(attributeParameter, Base64.encode(this.attributes[i]));
            }

            // send message by POST
            String url = Helper.getPostURL(request, "/WalletsServlet");
            String urlParameters = "&json=" + message_tokens.toJSONString();

            // send post request
            HttpURLConnection con = Helper.requestURL(url, urlParameters);

            // get response code
            int responseCode = con.getResponseCode();

            // notify if error (nested notification about an server error, between servlets)
            if (responseCode != 200)
                response.setStatus(500);

            System.out.println("ProverServlet sent wallets message (response code: " + responseCode + ")");
        } else {
            System.out.println("ProverServlet failed");
            response.sendRedirect(Helper.getServletPath()+"/prover/?failedAt=ProverServlet");
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}