package optimizerjs;

import com.google.javascript.jscomp.CompilationLevel;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.JSError;
import com.google.javascript.jscomp.JSSourceFile;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTextArea;

/**
 * call the Closure Compiler programmatically.
 *
 * @author tantrieuf31@gmail.com (Trieu Nguyen)
 */
public class CompressJS {

    //the view
    javax.swing.JTextArea txtareaLog;
    boolean autoCompile = false;
    String[] normalJSPaths = new String[0];
    String productionJSPath = "";
    StringBuilder compiledSrc = new StringBuilder();

    public CompressJS() {
        Logger.getLogger("com.google.javascript.jscomp").setLevel(Level.OFF);
    }

    public String[] getNormalJSPaths() {
        return normalJSPaths;
    }

    public void setNormalJSPaths(String[] normalJSPaths) {
        this.normalJSPaths = normalJSPaths;
    }

    public String getProductionJSPath() {
        return productionJSPath;
    }

    public void setProductionJSPath(String productionJSPath) {
        this.productionJSPath = productionJSPath;
    }

    public JTextArea getTxtareaLog() {
        return txtareaLog;
    }

    public void setTxtareaLog(JTextArea txtareaLog) {
        this.txtareaLog = txtareaLog;
        this.txtareaLog.setText(this.toString());
        if(autoCompile){
            startCompile();
        }
    }

    public boolean isAutoCompile() {
        return autoCompile;
    }

    public void setAutoCompile(boolean autoCompile) {
        this.autoCompile = autoCompile;
    }  

    public boolean compile(String normalJSPath, String productionJSPath) {
        System.out.println("#Compiling " + normalJSPath);
        Compiler compiler = new Compiler();

        CompilerOptions options = new CompilerOptions();
        // Advanced mode is used here, but additional options could be set, too.
        CompilationLevel.SIMPLE_OPTIMIZATIONS.setOptionsForCompilationLevel(options);

        // To get the complete set of externs, the logic in
        // CompilerRunner.getDefaultExterns() should be used here.
        JSSourceFile extern = JSSourceFile.fromCode("externs.js", "");

        // The dummy input name "input.js" is used here so that any warnings or
        // errors will cite line numbers in terms of input.js.
        JSSourceFile input = JSSourceFile.fromFile(normalJSPath);

        // compile() returns a Result, but it is not needed here.
        compiler.compile(extern, input, options);

        if (compiler.hasErrors()) {
            StringBuilder sb = new StringBuilder();
            compiledSrc = new StringBuilder();//clear all build js

            sb.append("//BUILD FAILED!, date: ").append(getCurrentDateTimeAsString()).append("\n");
            JSError[] jsErrors = compiler.getErrors();
            for (JSError jsError : jsErrors) {
                System.out.println(jsError.toString());
                sb.append(jsError.toString()).append("\n");
            }
            this.txtareaLog.setText(txtareaLog.getText() + sb.toString());
            return false;
        } else {
            compiledSrc.append(compiler.toSource()).append("\n");
            return true;
        }
    }

    public void startCompile() {
        if (this.normalJSPaths != null && productionJSPath != null) {
            updateViewBeginCompile();
            if (compiledSrc.toString().length() > 0) {
                compiledSrc = new StringBuilder();
            }

            compiledSrc.append("//BUILD BY OptimizerJS \n");
            for (String path : normalJSPaths) {
                if (!compile(path, productionJSPath)) {
                    break;
                }
            }

            String optimizedSrcStr = compiledSrc.toString();
            int size = IOHelper.writeStringToFile(optimizedSrcStr, productionJSPath);
            updateViewEndCompile(size);
        } else {
            throw new IllegalArgumentException("Please set normalJSPaths and productionJSPath!");
        }
    }

    protected void updateViewBeginCompile() {
        String s = "\n\n ##### Compiling javascript files ####### \n";
        this.txtareaLog.setText(txtareaLog.getText() + s);
    }

    protected void updateViewEndCompile(int size) {
        String s = "\n\n DONE compiling javascript files \n";
        s += (this.productionJSPath + " has the size = " + size + " KB. \n");
        this.txtareaLog.setText(txtareaLog.getText() + s);
        if (size > 0) {
            System.exit(0);
        }
    }

    @Override
    public String toString() {
        StringBuilder logStr = new StringBuilder();

        logStr.append("### CompressJS input files: \n");
        for (String path : normalJSPaths) {
            logStr.append(path).append("\n");
        }
        logStr.append("#####################").append("\n");

        logStr.append("### Output file: \n");
        logStr.append(this.productionJSPath).append("\n");
        logStr.append("#####################").append("\n");
        
        if(autoCompile){
            logStr.append("### AUTO COMPILE MODE, start compiling ...").append("\n");
        }

        return logStr.toString();
    }

    protected String getCurrentDateTimeAsString() {
        DateFormat dateFormat = new SimpleDateFormat("MMM/dd/yyyy H.m.s");
        String formated_date = dateFormat.format(new Date());
        return formated_date;
    }
}
