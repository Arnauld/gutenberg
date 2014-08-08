package gutenberg.pygments;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.python.core.PyException;
import org.python.core.PyGenerator;
import org.python.core.PyObject;
import org.python.core.PyTuple;
import org.python.util.PythonInterpreter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class Lexers {

    public static Lexers getInstance() {
        return new Lexers();
    }

    private static final Object NULL = new Object();

    private Logger log = LoggerFactory.getLogger(Lexers.class);
    private Map<String, Object> lexerCache = Maps.newConcurrentMap();

    /**
     * Lookup a Pygments lexer by an alias.
     *
     * @param gateway
     * @param alias   language alias for which a lexer is queried
     */
    public Optional<Object> lookupLexer(PyGateway gateway, String alias) {
        Object result = lexerCache.get(alias);

        if (result == null) {
            result = evalLookupLexer(gateway, alias, NULL);
            lexerCache.put(alias, result);
        }

        if (result == NULL) {
            return Optional.absent();
        } else {
            return Optional.of(result);
        }
    }

    private Object evalLookupLexer(PyGateway gateway, String alias, Object notFoundFallback) {
        Object result;
        try {
            PythonInterpreter interpreter = gateway.getInterpreter();
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

    public List<LexerInfo> loadAvailableLexers(PyGateway gateway) {
        PythonInterpreter interpreter = gateway.getInterpreter();

        // Simple use Pygments as you would in Python
        interpreter.exec(""
                + "from pygments.lexers import get_all_lexers\n"
                + "result = get_all_lexers()");

        PyGenerator result = (PyGenerator) interpreter.get("result");
        ArrayList<LexerInfo> infos = Lists.newArrayList();
        for (Object o : result) {
            PyTuple tuple = (PyTuple) o;
            String name = (String) tuple.get(0);
            List<String> aliases = Lists.newArrayListWithCapacity(3);
            for (Object alias : (PyTuple) tuple.get(1)) {
                String str = (String) alias;
                aliases.add(str);
            }
            LexerInfo info = new LexerInfo(name, aliases);
            infos.add(info);
        }
        return infos;
    }
}
