package gutenberg.pygments;

import org.python.util.PythonInterpreter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class PyGateway {

    public static PyGateway getInstance() {
        return new PyGateway();
    }

    private Logger log = LoggerFactory.getLogger(PyGateway.class);

    public PythonInterpreter getInterpreter() {
        long startMs = System.currentTimeMillis();
        PythonInterpreter interpreter = new PythonInterpreter();

        long interpMs = System.currentTimeMillis();
        log.debug("Python interpreter initialized in " + (interpMs - startMs) + "ms");
        return interpreter;
    }
}
