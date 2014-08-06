package gutenberg.pygments;

import com.google.common.base.Optional;
import com.google.common.collect.Maps;
import org.python.core.PyException;
import org.python.core.PyGenerator;
import org.python.core.PyTuple;
import org.python.util.PythonInterpreter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class Lexers {

    public static Lexers getInstance() {
        return new Lexers().loadAvailableLexers();
    }

    private static final Object NULL = new Object();

    private Logger log = LoggerFactory.getLogger(Lexers.class);
    private Map<String, Object> lexerCache = Maps.newConcurrentMap();

    /**
     * Lookup a Pygments lexer by an alias.
     *
     * @param alias language alias for which a lexer is queried
     */
    public Optional<Object> lookupLexer(String alias) {
        Object result = lexerCache.get(alias);

        if (result == null) {
            result = pythonLookupLexer(alias, NULL);
            lexerCache.put(alias, result);
        }

        if (result == NULL) {
            return Optional.absent();
        } else {
            return Optional.of(result);
        }
    }

    private Object pythonLookupLexer(String alias, Object notFoundFallback) {
        Object result;
        try {
            PythonInterpreter interpreter = new PythonInterpreter();
            interpreter.set("alias", alias);
            interpreter.exec(""
                    + "from pygments.lexers import get_lexer_by_name\n"
                    + "result = get_lexer_by_name(alias)");

            result = interpreter.get("result");
        } catch (PyException e) {
            log.warn("Unable to find Pygments lexer for alias '{}'", alias);
            result = notFoundFallback;
        }
        return result;
    }

    Lexers loadAvailableLexers() {
        PythonInterpreter interpreter = new PythonInterpreter();

        // Simple use Pygments as you would in Python
        interpreter.exec(""
                + "from pygments.lexers import get_all_lexers\n"
                + "result = get_all_lexers()");

        PyGenerator result = (PyGenerator) interpreter.get("result");
        for (Object o : result) {
            PyTuple tuple = (PyTuple) o;
            System.out.println(tuple + " ==> result = " + tuple.get(0) + "; " + tuple.get(1).getClass());
        }
        return this;
    }
}
