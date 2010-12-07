package optimizerjs;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author tantrieuf31@gmail.com (Trieu Nguyen)
 */
public class IOHelper {

    public static String loadStringFromFile(String htmlFilePath) {
        try {
            Writer writer = new StringWriter();
            char[] buffer = new char[1024];
            Reader reader = new InputStreamReader(new FileInputStream(htmlFilePath), "UTF-8");
            int n;
            while ((n = reader.read(buffer)) != -1) {
                writer.write(buffer, 0, n);
            }
            reader.close();
            return writer.toString();
        } catch (Exception ex) {
            Logger.getLogger(OptimizerJS.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "";
    }

    public static int writeStringToFile(String content, String toFilePath) {
        File compiledFile = new File(toFilePath);
        try {
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(compiledFile), "UTF-8"));
            out.write(content);
            out.close();
        } catch (UnsupportedEncodingException ue) {
            System.out.println("Not supported : " + ue.getMessage());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        int size = (int) Math.ceil((content.getBytes().length / 1024));
        return size;
    }
}
