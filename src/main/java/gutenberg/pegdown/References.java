package gutenberg.pegdown;

import com.google.common.collect.Maps;
import org.pegdown.ast.Node;
import org.pegdown.ast.ReferenceNode;
import org.pegdown.ast.TextNode;

import java.util.Map;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class References {
    private final Map<String, Ref> references;

    public References() {
        this.references = Maps.newConcurrentMap();
    }

    public Ref lookup(String key) {
        return references.get(key);
    }

    public void traverse(Node node) {
        if (node instanceof ReferenceNode) {
            declareReference((ReferenceNode) node);
        }
        for (Node child : node.getChildren()) {
            traverse(child);
        }
    }

    private void declareReference(ReferenceNode node) {
        TextNode text = (TextNode) node.getChildren().get(0).getChildren().get(0);
        String title = node.getTitle();
        String url = node.getUrl();
        String key = text.getText();
        Ref ref = new Ref(key, url, title);
        references.put(key, ref);
    }

    public static class Ref {
        private final String key;
        private final String url;
        private final String title;

        public Ref(String key, String url, String title) {
            this.key = key;
            this.url = url;
            this.title = title;
        }

        public String key() {
            return key;
        }

        public String url() {
            return url;
        }

        public String title() {
            return title;
        }

        @Override
        public String toString() {
            return "Ref{" +
                    "key='" + key + '\'' +
                    ", url='" + url + '\'' +
                    ", title='" + title + '\'' +
                    '}';
        }
    }
}
