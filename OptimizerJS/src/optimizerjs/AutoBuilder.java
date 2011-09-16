/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package optimizerjs;

import com.google.javascript.jscomp.CompilationLevel;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.JSError;
import com.google.javascript.jscomp.JSSourceFile;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author trieunt
 */
public class AutoBuilder {

    protected static String getCurrentDateTimeAsString() {
        DateFormat dateFormat = new SimpleDateFormat("MMM/dd/yyyy H.m.s");
        String formated_date = dateFormat.format(new Date());
        return formated_date;
    }

    public static void main(String[] args) {

        StringBuilder compiledSrc = new StringBuilder();

        String src = "Y:/plugins.banbe.net/public/static/js/v2/fo-connect-debug.js";
        String dist = "Y:/plugins.banbe.net/public/static/js/fosp-all.js";

        Compiler compiler = new Compiler();

        CompilerOptions options = new CompilerOptions();
        // Advanced mode is used here, but additional options could be set, too.
        CompilationLevel.SIMPLE_OPTIMIZATIONS.setOptionsForCompilationLevel(options);

        // To get the complete set of externs, the logic in
        // CompilerRunner.getDefaultExterns() should be used here.
        JSSourceFile extern = JSSourceFile.fromCode("externs.js", "");

        // The dummy input name "input.js" is used here so that any warnings or
        // errors will cite line numbers in terms of input.js.
        JSSourceFile input = JSSourceFile.fromFile(src);

        // compile() returns a Result, but it is not needed here.
        compiler.compile(extern, input, options);

        if (compiler.hasErrors()) {
            StringBuilder sb = new StringBuilder();

            sb.append("//BUILD FAILED!, date: ").append(getCurrentDateTimeAsString()).append("\n");
            JSError[] jsErrors = compiler.getErrors();
            for (JSError jsError : jsErrors) {
                System.out.println(jsError.toString());
                sb.append(jsError.toString()).append("\n");
            }
        } else {
            compiledSrc.append("//FOSP Connect v2, BUILD DATE: ").append(getCurrentDateTimeAsString()).append("\n");
            compiledSrc.append(compiler.toSource()).append("\n");
        }
        int size = IOHelper.writeStringToFile(compiledSrc.toString(), dist);
        System.out.println("Build "+ size);
    }
}
