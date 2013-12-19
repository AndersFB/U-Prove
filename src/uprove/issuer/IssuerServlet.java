package uprove.issuer;

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
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

/**
 * Created with IntelliJ IDEA.
 * User: andersfogbunzel
 * Date: 11/12/13
 * Time: 10.01
 */
public class IssuerServlet extends HttpServlet {
    private IssuerKeyAndParameters ikap;
    private IssuerParameters ip;
    private Issuer issuer;

    private byte[] encodingBytes;
    private String hashAlgorithmUID = "SHA-256";
    private String parametersUID = "unique UID";
    private String specification = "specification";

    private int numberOfTokens;
    private int numberOfAttributes;

    public IssuerServlet() {
        com.sun.org.apache.xml.internal.security.Init.init(); // Base64DecodingException
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getParameter("message").equals("init")) {
            System.out.println("IssuerServlet received init message");

            // receive JSON object
            JSONObject message_init = Helper.parseToJSON(request.getParameter("json"));

            // token issuance
            System.out.println("Issuing U-Prove wallets");

            // protocol parameters
            byte[] tokenInformation = "token information".getBytes();
            this.numberOfTokens = ((Long) message_init.get("numberOfTokens")).intValue();
            this.numberOfAttributes = ((Long) message_init.get("numberOfAttributes")).intValue();
            byte[][] attributes = new byte[0][];
            try {
                attributes = new byte[this.numberOfAttributes][];
                for (int i = 0; i < this.numberOfAttributes; i++) {
                    String attributeParameter = "attribute_" + Integer.toString(i + 1);
                    attributes[i] = Base64.decode((String) message_init.get(attributeParameter));
                }
            } catch (Base64DecodingException e) {
                e.printStackTrace();
            }

            // issuer parameters setup
            // if the attributes length is greater than q-1, then we have to raise g to the hashed value
            // instead of the attribute itself because the exponent have af max size.
            encodingBytes = new byte[this.numberOfAttributes]; // define encoding bytes for each attributes (1=hash attribute, 0=don't)
            for (int i = 0; i < this.numberOfAttributes; i++) {
                encodingBytes[i] = 0;
            }
            setupIssuerParameters();

            // issuer generates first issuance message
            IssuerProtocolParameters issuerProtocolParams = new IssuerProtocolParameters();
            issuerProtocolParams.setIssuerKeyAndParameters(this.ikap);
            issuerProtocolParams.setNumberOfTokens(numberOfTokens);
            issuerProtocolParams.setTokenAttributes(attributes);
            issuerProtocolParams.setTokenInformation(tokenInformation);
            this.issuer = issuerProtocolParams.generate();
            byte[][] message1 = this.issuer.generateFirstMessage();

            // create JSON object
            JSONObject message_1 = new JSONObject();
            message_1.put("ip", Helper.toString(this.ip)); // added Serializable in IssuerParameters, PrimeOrderGroup and FieldZq because need to send IssuerParameters to ProverServlet and VerifierServlet
            message_1.put("tokenInformation", Base64.encode(tokenInformation));
            for (int i = 0; i < this.numberOfTokens * 2; i++) { //*2 because i=sigmaA and i+1=sigmaB for each token
                String messageParameter = "message_" + Integer.toString(i + 1);
                message_1.put(messageParameter, Base64.encode(message1[i]));
            }

            // send message by POST
            String url = Helper.getPostURL(request, "/ProverServlet");
            String urlParameters = "message=1&json=" + message_1.toJSONString();

            // send post request
            HttpURLConnection con = Helper.requestURL(url, urlParameters);

            // get response code
            int responseCode = con.getResponseCode();

            // notify if error (nested notification about an server error, between servlets)
            if (responseCode != 200)
                response.setStatus(500);

            System.out.println("IssuerServlet sent first message (response code: " + responseCode + ")");
        } else if (request.getParameter("message").equals("2")) {
            System.out.println("IssuerServlet received second message");

            // receive JSON object
            JSONObject message_2 = Helper.parseToJSON(request.getParameter("json"));

            // receive and decode message
            byte[][] message2 = new byte[numberOfTokens][];
            try {
                for (int i = 0; i < numberOfTokens; i++) {
                    String messageParameter = "message_" + Integer.toString(i + 1);
                    message2[i] = Base64.decode(((String) message_2.get(messageParameter)).replace(" ", "+"));
                }
            } catch (Base64DecodingException e) {
                e.printStackTrace();
            }

            byte[][] message3 = this.issuer.generateThirdMessage(message2);

            // encode message 3
            JSONObject message_3 = new JSONObject();
            for (int i = 0; i < this.numberOfTokens; i++) {
                String messageParameter = "message_" + Integer.toString(i + 1);
                message_3.put(messageParameter, Base64.encode(message3[i]));
            }

            // send message by POST
            String url = Helper.getPostURL(request, "/ProverServlet");
            String urlParameters = "message=3&json=" + message_3.toJSONString();

            // send post request
            HttpURLConnection con = Helper.requestURL(url, urlParameters);

            // get response code
            int responseCode = con.getResponseCode();

            // notify if error (nested notification about an server error, between servlets)
            if (responseCode != 200)
                response.setStatus(500);

            System.out.println("IssuerServlet sent third message (response code: " + responseCode + ")");
        } else {
            response.sendRedirect(Helper.getServletPath() + "/prover/?failedAt=IssuerServlet");
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    private void setupIssuerParameters() {
        try {
            /*
             * issuer parameters setup
             */

            IssuerSetupParameters isp = new IssuerSetupParameters();
            isp.setEncodingBytes(this.encodingBytes);
            isp.setHashAlgorithmUID(this.hashAlgorithmUID);
            isp.setParametersUID(this.parametersUID.getBytes());
            isp.setSpecification(this.specification.getBytes());
            this.ikap = isp.generate();
            this.ip = this.ikap.getIssuerParameters();

            // issuer distributes the issuer parameters

        } catch (NoSuchProviderException e) {
            System.out.println(e.toString());
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            System.out.println(e.toString());
            e.printStackTrace();
        }
    }
}