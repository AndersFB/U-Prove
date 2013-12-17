package uprove;

import com.sun.org.apache.xml.internal.security.exceptions.Base64DecodingException;
import com.sun.org.apache.xml.internal.security.utils.Base64;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

/**
 * Created with IntelliJ IDEA.
 * User: andersfogbunzel
 * Date: 10/12/13
 * Time: 22.46
 * To change this template use File | Settings | File Templates.
 */
public class Helper {
    /**
     * Read the object from Base64 string.
     */
    public static Object fromString(String s) throws IOException,
            ClassNotFoundException, Base64DecodingException {
        byte[] data = Base64.decode(s);
        ObjectInputStream ois = new ObjectInputStream(
                new ByteArrayInputStream(data));
        Object o = ois.readObject();
        ois.close();
        return o;
    }

    /**
     * Write the object to a Base64 string.
     */
    public static String toString(Serializable o) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(o);
        oos.close();
        return new String(Base64.encode(baos.toByteArray()));
    }

    /**
     * either return "" (empty string) if locally or "/u-prove" if online
     */
    public static String getServletPath() {
        return "";
    }

    public static String getPostURL(HttpServletRequest request, String requestPath) {
        return request.getRequestURL().substring(0, request.getRequestURL().length() - request.getServletPath().length())
                + requestPath;
    }

    public static JSONObject parseToJSON(String jsonString) {
        JSONObject json = null;
        try {
            JSONParser parser = new JSONParser();
            Object jsonObj = parser.parse(jsonString);
            json = (JSONObject) jsonObj;
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return json;
    }

    public static HttpURLConnection requestURL(String url, String urlParameters) {
        HttpURLConnection con = null;
        try {
            URL obj = new URL(url);
            con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("POST"); // add request header

            // send post request
            con.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.writeBytes(urlParameters);
            wr.flush();
            wr.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return con;
    }
}
