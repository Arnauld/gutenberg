package gutenberg.pygments;

import com.google.common.base.Optional;
import org.python.core.PyObject;
import org.python.core.PyString;
import org.python.util.PythonInterpreter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class Pygments {
    private Logger log = LoggerFactory.getLogger(Pygments.class);

    private final PyGateway gateway;
    private final Lexers lexers;

    public Pygments() {
        this(PyGateway.getInstance(), Lexers.getInstance());
    }

    public Pygments(PyGateway gateway, Lexers lexers) {
        this.lexers = lexers;
        this.gateway = gateway;
    }

    public Tokens tokenize(String language, String code) {
        Optional<Object> lexer = lexers.lookupLexer(gateway, language);
        if (!lexer.isPresent())
            return new Tokens().append(Token.Text, code);

        return process(lexer.get(), code);
    }

    private Tokens process(Object lexer, String code) {
        PythonInterpreter interpreter = gateway.getInterpreter();

        Tokens tokens = new Tokens();

        interpreter.set("code", code);
        interpreter.set("lexer", lexer);
        interpreter.set("out", new RFormatter(tokens));

        // Simple use Pygments as you would in Python
        interpreter.exec(""
                + "from pygments import highlight\n"
                + "from pygments.formatter import Formatter\n"
                + "\n"
                + "class ForwardFormatter(Formatter):\n"
                + "    def format(self, tokensource, outfile):\n"
                + "        for ttype, value in tokensource:\n"
                + "            out.write(ttype, value)\n"
                + "\n"
                + "result = highlight(code, lexer, ForwardFormatter())");

        return tokens;
    }

    public class RFormatter extends PyObject {
        private final Tokens tokens;

        public RFormatter(Tokens tokens) {
            this.tokens = tokens;
        }

        public void write(PyObject ttype, PyString value) {
            // TODO find a better way to get the token name than relying on the 'toString'...
            String ttypeStr = ttype.__repr__().getString();
            String stringRepr = ttypeStr.replace(".", "");
            Optional<Token> tokenOpt = Token.findTokenByRepr(stringRepr);

            Token token = tokenOpt.or(Token.Text);
            if (!tokenOpt.isPresent()) {
                log.warn("Unrecognized Pygments token {}, fallbacking to Text one", stringRepr);
            }

            String valueString = value.getString();
            log.debug("Token {} ({}): '{}'", token, ttypeStr, valueString.replace("\n", "\\n"));
            tokens.append(token, valueString);
        }
    }
}
