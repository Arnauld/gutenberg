package gutenberg.pegdown;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import org.pegdown.ast.Node;

import java.util.List;
import java.util.Stack;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class TreeNavigation {
    private final Stack<Node> ancestorsStack;

    public TreeNavigation(List<Node> ancestorsStack) {
        this();
        this.ancestorsStack.addAll(ancestorsStack);
    }

    public TreeNavigation() {
        this.ancestorsStack = new Stack<Node>();
    }

    public TreeNavigation push(Node node) {
        ancestorsStack.push(node);
        return this;
    }

    public TreeNavigation pushChild(int nth) {
        return push(peek().getChildren().get(nth));
    }

    public TreeNavigation pushChild() {
        return pushChild(0);
    }

    public Node pop() {
        return ancestorsStack.pop();
    }

    public Node peek() {
        return ancestorsStack.peek();
    }

    public boolean ancestorTreeMatches(Class<? extends Node>... ancestorTypes) {
        int len = ancestorsStack.size();
        if (ancestorTypes.length > len)
            return false;

        for (int i = 0; i < ancestorTypes.length; i++) {
            Class<? extends Node> ancestor = ancestorTypes[i];
            Node node = ancestorsStack.get(len - 1 - i);
            if (!ancestor.isInstance(node))
                return false;
        }
        return true;
    }

    public List<Node> ancestorsStack() {
        return ancestorsStack;
    }

    private int numberOfAncestors() {
        return ancestorsStack.size();
    }

    public static Node lookupChild(Node node, Class<? extends Node>... childClasses) {
        Node child = node;
        for (Class<? extends Node> childClass : childClasses) {
            if(child==null)
                return null;

            List<Node> children = child.getChildren();
            if(children==null || children.isEmpty())
                return null;

            child = children.get(0);
            if (!childClass.isInstance(child))
                return null;
        }
        return child;
    }

    @SuppressWarnings({"unchecked", "UnusedParameters"})
    public <T extends Node> T peek(Class<T> nodeType) {
        return (T) peek();
    }

    public static abstract class Query {
        public abstract Optional<TreeNavigation> query(TreeNavigation nav);

        public Query then(final Query next) {
            final Query first = this;
            return new Query() {
                @Override
                public Optional<TreeNavigation> query(TreeNavigation nav) {
                    Optional<TreeNavigation> res = first.query(nav);
                    if (res.isPresent())
                        return next.query(res.get());
                    return res;
                }
            };
        }
    }


    public static Ancestor ancestor(Class<? extends Node>... ancestorTypes) {
        return new Ancestor(ancestorTypes);
    }

    public static SiblingBefore siblingBefore() {
        return new SiblingBefore();
    }

    public static FirstAncestorOfType firstAncestorOfType(Class<? extends Node> ancestorType) {
        return new FirstAncestorOfType(ancestorType);
    }

    public static OfType ofType(Class<? extends Node> nodeType) {
        return new OfType(nodeType);
    }

    public static class OfType extends Query {
        private final Class<? extends Node> nodeType;

        public OfType(Class<? extends Node> nodeType) {
            this.nodeType = nodeType;
        }

        public Optional<TreeNavigation> query(TreeNavigation nav) {
            Node node = nav.peek();
            if (nodeType.isInstance(node))
                return Optional.of(nav);
            return Optional.absent();
        }
    }

    public static class SiblingBefore extends Query {
        public SiblingBefore() {
        }

        public Optional<TreeNavigation> query(TreeNavigation nav) {
            int len = nav.numberOfAncestors();
            if (len <= 1)
                return Optional.absent();
            List<Node> ancestors = nav.ancestorsStack();
            Node n = ancestors.get(len - 1);
            Node p = ancestors.get(len - 2);
            Node s = null;
            for (Node sibling : p.getChildren()) {
                if (sibling == n) {
                    if (s == null) {
                        return Optional.absent();
                    }
                    List<Node> nodes = Lists.newArrayList(ancestors.subList(0, len - 2));
                    nodes.add(s);
                    return Optional.of(new TreeNavigation(nodes));
                }
                s = sibling;
            }
            return Optional.absent();
        }

    }

    public static class FirstAncestorOfType extends Query {
        private final Class<? extends Node> ancestorType;

        public FirstAncestorOfType(Class<? extends Node> ancestorType) {
            this.ancestorType = ancestorType;
        }

        public Optional<TreeNavigation> query(TreeNavigation nav) {
            List<Node> ancestors = nav.ancestorsStack();
            for (int i = ancestors.size() - 1; i >= 0; i--) {
                Node ancestor = ancestors.get(i);
                if (ancestorType.isInstance(ancestor))
                    return Optional.of(new TreeNavigation(ancestors.subList(0, i + 1)));
            }

            return Optional.absent();
        }
    }

    public static class Ancestor extends Query {
        private final Class<? extends Node>[] ancestorTypes;

        public Ancestor(Class<? extends Node>... ancestorTypes) {
            this.ancestorTypes = ancestorTypes;
        }

        public Optional<TreeNavigation> query(TreeNavigation nav) {
            int len = nav.numberOfAncestors();
            if (ancestorTypes.length > len)
                return Optional.absent();

            List<Node> ancestors = nav.ancestorsStack();
            for (int i = 0; i < ancestorTypes.length; i++) {
                Class<? extends Node> ancestor = ancestorTypes[i];
                Node node = ancestors.get(len - 1 - i);
                if (!ancestor.isInstance(node))
                    return Optional.absent();
            }

            return Optional.of(new TreeNavigation(ancestors.subList(0, len - ancestorTypes.length + 1)));
        }
    }

}
