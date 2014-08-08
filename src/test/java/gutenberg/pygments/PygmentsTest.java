package gutenberg.pygments;

import org.junit.Test;
import org.python.core.PyObject;
import org.python.core.PyString;
import org.python.util.PythonInterpreter;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class PygmentsTest {

    @Test
    public void tokenize_known_language() {
        Pygments pygments = new Pygments();
        Tokens tokens = pygments.tokenize("clojure", "" +
                "(defn year-end-evaluation\n" +
                "  []\n" +
                "  (if (> (rand) 0.5)\n" +
                "    \"You get a raise!\"\n" +
                "    \"Better luck next year!\"))");
        System.out.println("PygmentsTest.tokenize_known_language" + tokens);
    }

    @Test
    public void raw_usecase() {
        long startMs = System.currentTimeMillis();
        PythonInterpreter interpreter = new PythonInterpreter();

        long interpMs = System.currentTimeMillis();
        System.out.println("Python interpreter initialized in " + (interpMs - startMs) + "ms");


        // Set a variable with the content you want to work with
        interpreter.set("code", "" +
                "(defn year-end-evaluation\n" +
                "  []\n" +
                "  (if (> (rand) 0.5)\n" +
                "    \"You get a raise!\"\n" +
                "    \"Better luck next year!\"))");

        // Simple use Pygments as you would in Python
        interpreter.exec("from pygments import highlight\n"
                + "from pygments.lexers.jvm import ClojureLexer\n"
                + "from pygments.formatters import RawTokenFormatter\n"
                + "\n"
                + "result = highlight(code, ClojureLexer(), RawTokenFormatter())");

        // Get the result that has been set in a variable
        PyObject result = interpreter.get("result");
        PyString string = (PyString) result;
        assertThat(string.getString()).isEqualTo("" +
                "Token.Punctuation\tu'('\n" +
                "Token.Keyword.Declaration\tu'defn '\n" +
                "Token.Name.Variable\tu'year-end-evaluation'\n" +
                "Token.Text\tu'\\n  '\n" +
                "Token.Punctuation\tu'['\n" +
                "Token.Punctuation\tu']'\n" +
                "Token.Text\tu'\\n  '\n" +
                "Token.Punctuation\tu'('\n" +
                "Token.Keyword\tu'if '\n" +
                "Token.Punctuation\tu'('\n" +
                "Token.Name.Builtin\tu'> '\n" +
                "Token.Punctuation\tu'('\n" +
                "Token.Name.Function\tu'rand'\n" +
                "Token.Punctuation\tu')'\n" +
                "Token.Text\tu' '\n" +
                "Token.Literal.Number.Float\tu'0.5'\n" +
                "Token.Punctuation\tu')'\n" +
                "Token.Text\tu'\\n    '\n" +
                "Token.Literal.String\tu'\"You get a raise!\"'\n" +
                "Token.Text\tu'\\n    '\n" +
                "Token.Literal.String\tu'\"Better luck next year!\"'\n" +
                "Token.Punctuation\tu')'\n" +
                "Token.Punctuation\tu')'\n" +
                "Token.Text\tu'\\n'\n");
    }

    @Test
    public void raw_customFormatter() {
        long startMs = System.currentTimeMillis();
        PythonInterpreter interpreter = new PythonInterpreter();

        long interpMs = System.currentTimeMillis();
        System.out.println("Python interpreter initialized in " + (interpMs - startMs) + "ms");

        // Set a variable with the content you want to work with
        interpreter.set("code", "" +
                "(defn year-end-evaluation\n" +
                "  []\n" +
                "  (if (> (rand) 0.5)\n" +
                "    \"You get a raise!\"\n" +
                "    \"Better luck next year!\"))");
        interpreter.set("out", new RFormatter());

        // Simple use Pygments as you would in Python
        interpreter.exec(""
                + "from pygments import highlight\n"
                + "from pygments.lexers.jvm import ClojureLexer\n"
                + "from pygments.formatter import Formatter\n"
                + "\n"
                + "class ForwardFormatter(Formatter):\n"
                + "    def format(self, tokensource, outfile):\n"
                + "        for ttype, value in tokensource:\n"
                + "            out.write(ttype, value)\n"
                + "\n"
                + "result = highlight(code, ClojureLexer(), ForwardFormatter())");

        // Get the result that has been set in a variable
        PyObject result = interpreter.get("result");
        System.out.println("PygmentsTest.usecase ::> " + result.getClass());
        PyString string = (PyString) result;
        System.out.println(string.encode("utf8"));
    }

    public class RFormatter extends PyObject {
        public void write(PyObject ttype, PyString value) {
            System.out.println("ttype = [" + ttype.getClass() + ":" + ttype + "], value = [" + value.getString() + "]");
        }
    }
}
