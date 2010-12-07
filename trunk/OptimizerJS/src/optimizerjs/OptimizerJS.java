/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package optimizerjs;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JFrame;

/**
 *
 * @author tantrieuf31@gmail.com (Trieu Nguyen)
 */
public class OptimizerJS {

    public static String[] fetchAllScriptSource(String htmlFile) {
        ArrayList<String> list = new ArrayList<String>();
        String regex = "<script(?:[^>]*src=['\"]([^'\"]*)['\"][^>]*>|[^>]*>([^<]*)</script>)";
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);

        String htmlSrc = IOHelper.loadStringFromFile(htmlFile);
//        System.out.println(htmlSrc);

        final Matcher matcher = pattern.matcher(htmlSrc);
        while (matcher.find()) {
            list.add(matcher.group(1));            
        }
        String[] normalJSPaths = new String[list.size()];
        int i = 0;
        for (String src : list) {
            normalJSPaths[i++] = src;
        }
        return normalJSPaths;
    }

    public static void fetchAllScriptSource2() {
        final String srcOne = "<html>\r\n<head>\r\n<script src=\"http://test.com/some.js\"/>\r\n</head></html>";
        final String srcTwo = "<html>\r\n<head>\r\n<script src=\"http://test.com/some.js\"></script>\r\n</head></html>";
        final String tag = "<html>\r\n<head>\r\n<script>\r\nfunction() {\r\n\talert('hi');\r\n}\r\n</script>\r\n</head></html>";
        final String tagAndSrc = "<html>\r\n<head>\r\n<script src=\"http://test.com/some.js\">\r\nfunction() {\r\n\talert('hi');\r\n}\r\n</script>\r\n</head></html>";
        final String[] tests = new String[]{srcOne, srcTwo, tag, tagAndSrc, srcOne + srcTwo, tag + srcOne + tagAndSrc};

        final String regex = "<script(?:[^>]*src=['\"]([^'\"]*)['\"][^>]*>|[^>]*>([^<]*)</script>)";
        final Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
        for (int testNumber = 0; testNumber < tests.length; ++testNumber) {
            final String test = tests[testNumber];
            final Matcher matcher = pattern.matcher(test);
            System.out.println("--------------------------------");
            System.out.println("TEST " + testNumber + ": " + test);
            while (matcher.find()) {
                System.out.println("GROUP 1: " + matcher.group(1));
                System.out.println("GROUP 2: " + matcher.group(2));
            }
            System.out.println("--------------------------------");
            System.out.println();
        }
    }

    public static void runTest() {
        // fetchAllScriptSource2();
        System.out.println("----------RUNNING TEST MODE---------");
        String htmlSrcPath = "F:/eclipse3.5.2/workspace/place-marker-project/placemarker_android/assets/www/index.html";
        String baseDir = htmlSrcPath.substring(0, htmlSrcPath.lastIndexOf("/"));
        String[] srcs = fetchAllScriptSource(htmlSrcPath);
        String[] args = new String[srcs.length + 1];
        int i=0;
        for (String src : srcs) {
            String path = baseDir + "/" + src;
            System.out.println(path);
            args[i++] = path;
        }
        args[i] = baseDir + "/" + "production-js/all.js";
        new OptimizerJS().initTheApp(args);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        if(args.length == 0){
            runTest();
        } else {
            new OptimizerJS().initTheApp(args);
        }
    }

    public void initTheApp(String[] args) {
        int argc = args.length;
        int lastIndex = argc - 1;
        if (lastIndex <= 0) {
            System.out.println("Exit, nothing to compress!");
            System.exit(0);
        }
        CompressJS compressJS = new CompressJS();
        String destPath = args[lastIndex];
        String[] normalJSPaths = new String[lastIndex];
        for (int i = 0; i < lastIndex; i++) {
            normalJSPaths[i] = args[i];
        }
        compressJS.setNormalJSPaths(normalJSPaths);
        compressJS.setProductionJSPath(destPath);

        System.out.println("... Starting the OptimizerJS with number of params: " + argc);
        MainGUI gUI = new MainGUI();
        gUI.setCompressJS(compressJS);
        JFrame f = new JFrame("The OptimizerJS for Web");
        f.setLocation(60, 50);
        f.setSize(800, 550);
        f.setContentPane(gUI);
        f.setVisible(true);
        gUI.setVisible(true);
    }
}
