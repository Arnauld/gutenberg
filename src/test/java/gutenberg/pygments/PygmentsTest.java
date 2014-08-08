package gutenberg.pygments;

import org.junit.Test;
import org.python.core.PyObject;
import org.python.core.PyString;
import org.python.util.PythonInterpreter;

import java.util.List;

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
        assertThat(tokens).isNotNull();
        assertThat(tokens).contains(new TokenWithValue(Token.String, "\"You get a raise!\""));
    }

    @Test
    public void raw_usecase() {
        PythonInterpreter interpreter = new PyGateway().getInterpreter();

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
        PythonInterpreter interpreter = new PyGateway().getInterpreter();

        // Set a variable with the content you want to work with
        interpreter.set("code", "" +
                "(defn year-end-evaluation\n" +
                "  []\n" +
                "  (if (> (rand) 0.5)\n" +
                "    \"You get a raise!\"\n" +
                "    \"Better luck next year!\"))");
        RFormatter rFormatter = new RFormatter();
        interpreter.set("out", rFormatter);

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
                + "highlight(code, ClojureLexer(), ForwardFormatter())");

        assertThat(rFormatter.out.toString()).isEqualTo("" +
                "Token.Punctuation:[(']\n" +
                "Token.Keyword.Declaration:[defn ']\n" +
                "Token.Name.Variable:[year-end-evaluation']\n" +
                "Token.Text:[\\n  ']\n" +
                "Token.Punctuation:[[']\n" +
                "Token.Punctuation:[]']\n" +
                "Token.Text:[\\n  ']\n" +
                "Token.Punctuation:[(']\n" +
                "Token.Keyword:[if ']\n" +
                "Token.Punctuation:[(']\n" +
                "Token.Name.Builtin:[> ']\n" +
                "Token.Punctuation:[(']\n" +
                "Token.Name.Function:[rand']\n" +
                "Token.Punctuation:[)']\n" +
                "Token.Text:[ ']\n" +
                "Token.Literal.Number.Float:[0.5']\n" +
                "Token.Punctuation:[)']\n" +
                "Token.Text:[\\n    ']\n" +
                "Token.Literal.String:[\"You get a raise!\"']\n" +
                "Token.Text:[\\n    ']\n" +
                "Token.Literal.String:[\"Better luck next year!\"']\n" +
                "Token.Punctuation:[)']\n" +
                "Token.Punctuation:[)']\n" +
                "Token.Text:[\\n']\n");
    }

    public class RFormatter extends PyObject {
        public StringBuilder out = new StringBuilder();
        public void write(PyObject ttype, PyString value) {
            out.append(ttype).append(":[").append(value.getString().replace("\n", "\\n")).append("']").append('\n');
        }
    }
}
