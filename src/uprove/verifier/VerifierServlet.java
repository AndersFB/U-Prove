package uprove.verifier;

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
import java.io.IOException;
import java.util.Arrays;

/**
 * Created with IntelliJ IDEA.
 * User: andersfogbunzel
 * Date: 11/12/13
 * Time: 10.01
 */
public class VerifierServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("VerifierServlet received an U-Prove token");

        // receive JSON object
        JSONObject message_token = Helper.parseToJSON(request.getParameter("json"));

        // receive token parameters and other parameters
        String serviceProvider = (String) message_token.get("serviceProvider");
        IssuerParameters ip = null;
        byte[] message = new byte[0];
        PresentationProof proof = null;
        UProveToken token = null;
        String disclosedAttributes = (String) message_token.get("disclosedAttributes");
        try {
            ip = (IssuerParameters) Helper.fromString(((String) message_token.get("ip")).replace(" ", "+"));
            message = Base64.decode((String) message_token.get("message"));
            proof = (PresentationProof) Helper.fromString(((String) message_token.get("proof")).replace(" ", "+"));
            token = (UProveToken) Helper.fromString(((String) message_token.get("token")).replace(" ", "+"));
        } catch (Base64DecodingException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        // prover and verifier should validate the issuer parameters upon reception
        ip.validate();

        // if the token is locally stored the service provider is not in the JSON object
        if (request.getParameter("token").equals("local")) {
            serviceProvider = request.getParameter("serviceProvider");
        }

        // protocol parameters and get attributes
        String[] disclosedArray = disclosedAttributes.split(",");
        int[] disclosed = new int[disclosedArray.length]; // what attributes are sent to the verifier, a list of integers identifying what attribute to sent, separated by a comma
        String attributes = "";
        for (int i = 0; i < disclosedArray.length; i++) {
            disclosed[i] = Integer.parseInt(disclosedArray[i]);
            if (disclosedArray[i].equals("1"))
                attributes += "name="+new String(proof.getDisclosedAttributes()[i]);
            if (disclosedArray[i].equals("2"))
                attributes += "age="+new String(proof.getDisclosedAttributes()[i]);
            if (disclosedArray[i].equals("3"))
                attributes += "citizenship="+new String(proof.getDisclosedAttributes()[i]);
            if (i < disclosedArray.length - 1)
                attributes += "&";
        }

        System.out.println("VerifierServlet got the following attributes: " + attributes);

        // verifier verifies the presentation proof
        try {
            PresentationProtocol.verifyPresentationProof(ip, disclosed, message, null, token, proof);
        } catch (InvalidProofException e) {
            e.printStackTrace();
        }

        System.out.println("VerifierServlet verified the presentation proof and got the requested attributes");

        // send service providers redirect link to WalletsServlet
        response.addHeader("service_provider", Helper.getServletPath()+"/service-provider/" + serviceProvider + "/authenticated.jsp?" + attributes);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
