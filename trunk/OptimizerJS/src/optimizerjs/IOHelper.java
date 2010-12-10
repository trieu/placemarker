package optimizerjs;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author tantrieuf31@gmail.com (Trieu Nguyen)
 */
public class IOHelper {

    public static String loadStringFromFile(String filePath) {
        try {
            Writer writer = new StringWriter();
            char[] buffer = new char[1024];
            Reader reader = new InputStreamReader(new FileInputStream(filePath), "UTF-8");
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
        try {
            File theFile = new File(toFilePath);
            if( ! theFile.exists() ){
                if( ! theFile.createNewFile() ) {
                    return 0;
                }
            }
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(theFile), "UTF-8"));
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
    
    public static String getParentDirPath(){
        String currentdir = System.getProperty("user.dir");        
        try {
            File dir2 = new File("..");            
            return dir2.getCanonicalPath();
        } catch (IOException ex) {
            Logger.getLogger(OptimizerJS.class.getName()).log(Level.SEVERE, null, ex);
        }
        //FIXME remove hardcode
        return currentdir.replace("/OptimizerJS", "");
    }

    /**
     * Recursively walk a directory tree and return a List of all
     * Files found; the List is sorted using File.compareTo().
     *
     * @param aStartingDir is a valid directory, which can be read.
     */
    public static List<File> getFileListing(File aStartingDir) throws FileNotFoundException {
        validateDirectory(aStartingDir);
        List<File> result = getFileListingNoSort(aStartingDir);
 //       Collections.sort(result);
        return result;
    }

    // PRIVATE //
    private static List<File> getFileListingNoSort(
            File aStartingDir) throws FileNotFoundException {
        List<File> result = new ArrayList<File>();
        File[] filesAndDirs = aStartingDir.listFiles();
        List<File> filesDirs = Arrays.asList(filesAndDirs);
        for (File file : filesDirs) {
            result.add(file); //always add, even if directory
            if (!file.isFile()) {
                //must be a directory
                //recursive call!
                List<File> deeperList = getFileListingNoSort(file);
                result.addAll(deeperList);
            }
        }
        return result;
    }

    /**
     * Directory is valid if it exists, does not represent a file, and can be read.
     */
    private static void validateDirectory(
            File aDirectory) throws FileNotFoundException {
        if (aDirectory == null) {
            throw new IllegalArgumentException("Directory should not be null.");
        }
        if (!aDirectory.exists()) {
            throw new FileNotFoundException("Directory does not exist: " + aDirectory);
        }
        if (!aDirectory.isDirectory()) {
            throw new IllegalArgumentException("Is not a directory: " + aDirectory);
        }
        if (!aDirectory.canRead()) {
            throw new IllegalArgumentException("Directory cannot be read: " + aDirectory);
        }
    }
}
