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

    public static String[] fetchAndOptimizeRawSource(String htmlSrcPath, String optimizedHtmlSrcPath) {
        ArrayList<String> list = new ArrayList<String>();
        String regex = "<script(?:[^>]*src=['\"]([^'\"]*)['\"][^>]*>|[^>]*>([^<]*)</script>)";
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);

        String htmlSrc = IOHelper.loadStringFromFile(htmlSrcPath);
//        System.out.println(htmlSrc);

        final Matcher matcher = pattern.matcher(htmlSrc);
        while (matcher.find()) {
            list.add(matcher.group(1));
           
            String scriptTag = matcher.group()+"</script>";
            htmlSrc = htmlSrc.replaceAll(scriptTag, "");
        }
        String[] normalJSPaths = new String[list.size()];
        int i = 0;
        String baseDir = htmlSrcPath.substring(0, htmlSrcPath.lastIndexOf("/"));
        
        
        for (String src : list) {
            String path = baseDir + "/" + src;
            normalJSPaths[i++] = path;          
        }
        
        String scriptTag = "<script type='text/javascript' charset='utf-8' src='optimized-all.js'></script>\n </head>";
        htmlSrc = htmlSrc.replaceAll("</head>",scriptTag);//put at the bottom head       
        
        IOHelper.writeStringToFile(htmlSrc, optimizedHtmlSrcPath);        
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
        String[] args = new String[4];
        
//      args[0] = "F:/eclipse3.5.2/workspace/place-marker-project/placemarker_android/assets/www/index.html";
        args[0] = "/Users/trieunguyen/Documents/yopco-media/www/index.html";
               
//      args[1] = "production-js/all.js";
        args[1] = "/Users/trieunguyen/Documents/yopco-media/www/optimized-all.js";
        args[2] = "/Users/trieunguyen/Documents/yopco-media/www/optimized-index.html";
        args[3] = "false";
        
        new OptimizerJS().initTheApp(args);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        int argc = args.length; 
        if(argc == 0){
            runTest();
        } else if (argc == 4) {
            new OptimizerJS().initTheApp(args);
        } else {
            System.out.println("Exit, wrong params, nothing to compress!");
            System.exit(0);
        }
    }

    public void initTheApp(String[] args) {      
        
                    
        String parentDirPath = "";
        
        //FIXME remove hardcode for Phonegap Project
        if(args[0].startsWith("/yopco-media/www/")){
           parentDirPath = IOHelper.getParentDirPath();
        }
        
        //set params that are set from cmd 
        String htmlSrcPath = parentDirPath + args[0];  
        String destJSPath = parentDirPath + args[1];
        String destHTMLPath = parentDirPath + args[2];
        boolean autoCompiling = Boolean.parseBoolean(args[3]);   
        System.out.println("autoCompiling = " + autoCompiling);
        
        String[] normalJSPaths = fetchAndOptimizeRawSource(htmlSrcPath,destHTMLPath);
        CompressJS compressJS = new CompressJS();
        
        //set the important params
        compressJS.setNormalJSPaths(normalJSPaths);
        compressJS.setProductionJSPath(destJSPath);
        compressJS.setAutoCompile(autoCompiling);

        System.out.println("... Starting the OptimizerJS with number of params: " + args.length);
        MainGUI gUI = new MainGUI();
        gUI.setCompressJS(compressJS);
        JFrame f = new JFrame("The OptimizerJS for Web");
        f.setLocation(80, 80);
        f.setSize(new java.awt.Dimension(682, 422));
        f.setResizable(false);
        f.setContentPane(gUI);
        f.setVisible(true);
        gUI.setVisible(true); 
        
        gUI.getTxtIndexFilePath().setText(htmlSrcPath);
        gUI.getTxtOptimizedFilePath().setText(destHTMLPath);
        
    }
}
