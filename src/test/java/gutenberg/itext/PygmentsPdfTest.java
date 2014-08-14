package gutenberg.itext;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Paragraph;
import gutenberg.TestSettings;
import gutenberg.util.Attributes;
import gutenberg.pygments.Pygments;
import gutenberg.pygments.StyleSheet;
import gutenberg.pygments.styles.DefaultStyle;
import gutenberg.pygments.styles.FriendlyStyle;
import gutenberg.pygments.styles.MonokaiStyle;
import gutenberg.pygments.styles.SolarizedDarkStyle;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class PygmentsPdfTest {

    private String workingDir;

    @Before
    public void setUp() throws IOException, DocumentException {
        workingDir = new TestSettings().workingDir();
    }

    @Test
    public void simpleGenerate() throws Exception {
        ITextContext iTextContext = openDocument("simpleGenerate");
        Document document = iTextContext.getDocument();

        for (StyleSheet styleSheet : Arrays.asList(
                new DefaultStyle(),
                new FriendlyStyle(),
                new MonokaiStyle(),
                new SolarizedDarkStyle())) {

            PygmentsAdapter pygmentsAdapter = new PygmentsAdapter(
                    new Pygments(),
                    styleSheet, ITextUtils.inconsolata(),
                    12.0f);

            for (String[] lang : Arrays.asList(
                    s("clojure", clojureCode()),
                    s("java", javaCode()),
                    s("erlang", erlangCode()),
                    s("unknownlang", yukCode()))) {

                Paragraph stylePara = new Paragraph(
                        "Style: " + styleSheet.getClass().getSimpleName() + ", lang: " + lang[0]);
                document.add(stylePara);
                for (Element element : pygmentsAdapter.process(lang[0], lang[1], new Attributes())) {
                    document.add(element);
                }
            }
        }
        iTextContext.close();
    }

    private static String[] s(String... s) {
        return s;
    }

    private String clojureCode() {
        return "" +
                "(defn year-end-evaluation\n" +
                "  []\n" +
                "  (if (> (rand) 0.5)\n" +
                "    \"You get a raise!\"\n" +
                "    \"Better luck next year!\"))";
    }

    private String yukCode() {
        return "sub print_info {\n" +
                "      my $self   = shift;\n" +
                "      my $prefix = shift // \"This file is at \";\n" +
                "      print $prefix, \", \", $self->path, \"\\n\";\n" +
                "  }\n" +
                "  $file->print_info(\"The file is located at \");\n";
    }

    private String javaCode() {
        return "/* Block comment */\n" +
                "import java.util.Date;\n" +
                "/**\n" +
                " * Doc comment here for <code>SomeClass</code>\n" +
                " * @see Math#sin(double)\n" +
                " */\n" +
                "@Annotation (name=value)\n" +
                "public class SomeClass<T extends Runnable> { // some comment\n" +
                "  private T field = null;\n" +
                "  private double unusedField = 12345.67890;\n" +
                "  private UnknownType anotherString = \"Another\\nStrin\\g\";\n" +
                "  public static int staticField = 0;\n" +
                "\n" +
                "  public SomeClass(AnInterface param, int[] reassignedParam) {\n" +
                "    int localVar = \"IntelliJ\"; // Error, incompatible types\n" +
                "    System.out.println(anotherString + toString() + localVar);\n" +
                "    long time = Date.parse(\"1.2.3\"); // Method is deprecated\n" +
                "    int reassignedValue = this.staticField; \n" +
                "    reassignedValue ++; \n" +
                "    field.run(); \n" +
                "    new SomeClass() {\n" +
                "      {\n" +
                "        int a = localVar;\n" +
                "      }\n" +
                "    };\n" +
                "    reassignedParam = new ArrayList<String>().toArray(new int[0]);\n" +
                "  }\n" +
                "}\n" +
                "enum AnEnum { CONST1, CONST2 }\n" +
                "interface AnInterface {\n" +
                "  int CONSTANT = 2;\n" +
                "  void method();\n" +
                "}\n" +
                "abstract class SomeAbstractClass {\n" +
                "}";
    }

    private String erlangCode() {
        return "%%% Module fact documentation\n" +
                "-module(fact).\n" +
                "-export([fac/1]).\n" +
                "\n" +
                "-record(state, {id, name}).\n" +
                "\n" +
                "-define(MACRO, macro_value).\n" +
                "\n" +
                "-type in() :: ok | hello .\n" +
                "-type out() :: ok | {error, term()}.\n" +
                "\n" +
                "%% Factorial implementation\n" +
                "%% @doc Documentation\n" +
                "fac(0) -> 1;\n" +
                "fac(N) when N > 0, is_integer(N) -> N * fac(N-1).\n" +
                "\n" +
                "string_sample(A) -> \"string\n" +
                "  second line\".\n" +
                "\n" +
                "update_state(State) -> State#state{id=10}.\n" +
                "\n" +
                "-spec simple(in())-> out(). \n" +
                "simple(ok) -> ok.\n" +
                "\n" +
                "use_macro() -> io:format(?MACRO).\n" +
                "\n" +
                "-callback start_service() -> {ok, pid()}.";
    }

    private ITextContext openDocument(String method) throws FileNotFoundException, DocumentException {
        File file = new File(workingDir, getClass().getSimpleName() + "_" + method + ".pdf");
        return new ITextContext().open(file);
    }

}
