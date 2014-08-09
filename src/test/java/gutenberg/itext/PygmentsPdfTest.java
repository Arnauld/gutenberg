package gutenberg.itext;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import gutenberg.TestSettings;
import gutenberg.pygments.Pygments;
import gutenberg.util.Style;
import gutenberg.pygments.StyleSheet;
import gutenberg.pygments.TokenWithValue;
import gutenberg.pygments.Tokens;
import gutenberg.pygments.styles.DefaultStyle;
import gutenberg.pygments.styles.FriendlyStyle;
import gutenberg.pygments.styles.MonokaiStyle;
import gutenberg.util.RGB;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Arrays;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class PygmentsPdfTest {

    private String workingDir;
    private Document document;

    @Before
    public void setUp() {
        workingDir = new TestSettings().workingDir();
    }

    @Test
    public void simpleGenerate() throws Exception {
        openDocument("simpleGenerate");
        BaseFont bf = createEmbeddedFont("font/Inconsolata.otf", BaseFont.WINANSI);

        Pygments pygments = new Pygments();

        for (StyleSheet styleSheet : Arrays.asList(new DefaultStyle(), new MonokaiStyle(), new FriendlyStyle())) {
            for (String[] lang : Arrays.asList(
                    s("clojure", clojureCode()),
                    s("java", javaCode()),
                    s("erlang", erlangCode()),
                    s("unknownlang", yukCode()))) {

                Tokens tokens = pygments.tokenize(lang[0], lang[1]);
                emit(bf, tokens, lang[0], styleSheet);
            }
        }
        closeDocument();
    }

    private String[] s(String... s) {
        return s;
    }

    private void emit(BaseFont bf, Tokens tokens, String lang, StyleSheet stylesheet) throws DocumentException {
        Paragraph stylePara = new Paragraph(
                "Style: " + stylesheet.getClass().getSimpleName() + ", lang: " + lang);
        document.add(stylePara);

        Paragraph p = new Paragraph();
        for (TokenWithValue token : tokens) {
            Style style = stylesheet.styleOf(token.token);

            BaseColor color = toColor(style.fg());
            int s = Font.NORMAL;
            if (style.isBold()) {
                s |= Font.BOLD;
            }
            if (style.isItalic()) {
                s |= Font.ITALIC;
            }
            if (style.isStrikethrough()) {
                s |= Font.STRIKETHRU;
            }
            if (style.isUnderline()) {
                s |= Font.UNDERLINE;
            }

            Font font = new Font(bf, 12, s, color);
            Chunk o = new Chunk(token.value, font);

            RGB bg = style.bg();
            if (bg != null)
                o.setBackground(toColor(bg));
            p.add(o);
        }

        PdfPCell cell = new PdfPCell(p);
        cell.setBackgroundColor(toColor(stylesheet.backgroundColor()));
        cell.setPaddingBottom(5.0f);

        PdfPTable table = new PdfPTable(1);
        table.addCell(cell);
        table.setSpacingBefore(5.0f);
        table.setSpacingAfter(5.0f);
        document.add(table);
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

    private BaseColor toColor(RGB fg) {
        if (fg != null) {
            return new BaseColor(fg.r(), fg.g(), fg.b());
        }
        return null;
    }

    private BaseFont createEmbeddedFont(String fontName, String encoding) throws Exception {
        return BaseFont.createFont(fontName, encoding, BaseFont.EMBEDDED);
    }

    private File openDocument(String method) throws FileNotFoundException, DocumentException {
        File file = new File(workingDir, getClass().getSimpleName() + "_" + method + ".pdf");

        document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(file));
        document.open();
        return file;
    }

    private void closeDocument() {
        document.close();
    }
}
