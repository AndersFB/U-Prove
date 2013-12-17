package uprove;

import com.sun.org.apache.xml.internal.security.exceptions.Base64DecodingException;
import com.sun.org.apache.xml.internal.security.utils.Base64;

import java.io.*;

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
        return "/u-prove";
    }
}
