package leavenslab;

/**
 * Created by jls on 2/26/16.
 */

import beaver.Symbol;
import org.extendj.ExtendJVersion;
import org.extendj.ast.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Collection;


import org.extendj.ast.Frontend;
import org.extendj.ast.CompilationUnit;
import org.extendj.ast.JavaParser;
import org.extendj.ast.BytecodeParser;
import org.extendj.ast.BytecodeReader;
import org.extendj.ast.Problem;
import org.extendj.ast.Program;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Collection;
import java.util.Iterator;

/**
 * Pretty-print some Java source files.
 */
class AddPathLabels extends Frontend {

    /**
     * Entry point for the compiler frontend.
     * @param args command-line arguments
     */
    public static void main(String args[]) {
        int exitCode = new AddPathLabels().run(args);
        if (exitCode != 0) {
            System.exit(exitCode);
        }
    }

    private final JavaParser parser;
    private final BytecodeReader bytecodeParser;

    /**
     * Initialize the compiler.
     */
    public AddPathLabels() {
        super("AddPathLabels", ExtendJVersion.getVersion());

//        final Symbol _symbol_m = _symbols[offset + 1];
//        final List m = (List) _symbol_m.value;
//        final Symbol _symbol_t = _symbols[offset + 2];
//        final Access t = (Access) _symbol_t.value;
//        final Symbol _symbol_l = _symbols[offset + 3];
//        final List l = (List) _symbol_l.value;'
        //Symbol s = new Symbol()
        //VariableDeclarator(IDENTIFIER, d, new Opt());
//

        parser = new JavaParser() {
            @Override
            public CompilationUnit parse(InputStream is, String fileName) throws IOException,
                    beaver.Parser.Exception {
                return new org.extendj.parser.JavaParser().parse(is, fileName);
            }
        };
        bytecodeParser = new BytecodeReader() {
            @Override
            public CompilationUnit read(InputStream is, String fullName, Program p)
                    throws FileNotFoundException, IOException {
                return new BytecodeParser(is, fullName).parse(null, null, p);
            }
        };
    }

    /**
     * @param args command-line arguments
     * @return {@code true} on success, {@code false} on error
     * @deprecated Use run instead!
     */
    @Deprecated
    public static boolean compile(String args[]) {
        return 0 == new AddPathLabels().run(args);
    }

    /**
     * Pretty print the source files.
     * @param args command-line arguments
     * @return 0 on success, 1 on error, 2 on configuration error, 3 on system
     */
    public int run(String args[]) {
        return run(args, bytecodeParser, parser);
    }

    @SuppressWarnings("rawtypes")
    @Override
    protected void processErrors(Collection<Problem> errors, CompilationUnit unit) {

        /*
         * We want to remove all of the JPF-related errors (it's totally fine that the types can't
         * be resolved at this stage in the game.
         */
        Iterator<Problem> it = errors.iterator();

        while(it.hasNext()){
            Problem p = it.next();
            if(p.message().contains("nasa")){
                it.remove();
            }
        }




        super.processErrors(errors, unit);
        try {
            unit.prettyPrint(new PrintStream(System.out, false, "UTF-8"));
        } catch (IOException e) {
            e.printStackTrace(System.err);
        }
    }

    @Override
    protected void processNoErrors(CompilationUnit unit) {
        try {
            unit.prettyPrint(new PrintStream(System.out, false, "UTF-8"));
        } catch (IOException e) {
            e.printStackTrace(System.err);
        }
    }
}
