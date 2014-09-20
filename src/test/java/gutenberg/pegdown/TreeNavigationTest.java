package gutenberg.pegdown;

import com.google.common.base.Optional;
import gutenberg.pegdown.plugin.AttributesNode;
import gutenberg.pegdown.plugin.AttributesPlugin;
import org.junit.Test;
import org.pegdown.Extensions;
import org.pegdown.PegDownProcessor;
import org.pegdown.ast.ParaNode;
import org.pegdown.ast.RefImageNode;
import org.pegdown.ast.RootNode;
import org.pegdown.ast.SuperNode;
import org.pegdown.plugins.PegDownPlugins;

import static gutenberg.pegdown.TreeNavigation.*;
import static org.assertj.core.api.Assertions.assertThat;

public class TreeNavigationTest extends AbstractPegdownTest {

    @Test
    @SuppressWarnings("unchecked")
    public void usecase__ancestor_sibling_and_of_type() throws Exception {
        String mkd = loadResource("image-04-ref.md").trim();

        PegDownPlugins plugins = PegDownPlugins
                .builder()
                .withPlugin(AttributesPlugin.class)
                .build();
        PegDownProcessor processor = new PegDownProcessor(Extensions.ALL, plugins);
        RootNode rootNode = processor.parseMarkdown(mkd.toCharArray());

        TreeNavigation nav;
        nav = new TreeNavigation();
        nav.push(rootNode);
        nav.pushChild(3);
        nav.pushChild();
        nav.pushChild();
        assertThat(nav.peek()).isInstanceOf(RefImageNode.class);

        //
        //
        Optional<TreeNavigation> result1 =
                ancestor(RefImageNode.class, SuperNode.class, ParaNode.class)
                        .query(nav);
        assertThat(result1.isPresent()).isTrue();
        assertThat(result1.get().peek()).isInstanceOf(ParaNode.class);

        //
        //
        Optional<TreeNavigation> result2 =
                ancestor(RefImageNode.class, SuperNode.class, ParaNode.class)
                        .then(siblingBefore())
                        .query(nav);
        assertThat(result2.isPresent()).isTrue();
        assertThat(result2.get().peek()).isInstanceOf(AttributesNode.class);

        //
        //
        Optional<TreeNavigation> result3 =
                firstAncestorOfType(ParaNode.class)
                        .then(siblingBefore())
                        .query(nav);
        assertThat(result3.isPresent()).isTrue();
        assertThat(result3.get().peek()).isInstanceOf(AttributesNode.class);

        //System.out.println("TreeNavigationTest.usecase" + dumpAST(rootNode));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void usecase__sibling_then_of_type() throws Exception {
        String mkd = loadResource("image-04-ref.md").trim();

        PegDownPlugins plugins = PegDownPlugins
                .builder()
                .withPlugin(AttributesPlugin.class)
                .build();
        PegDownProcessor processor = new PegDownProcessor(Extensions.ALL, plugins);
        RootNode rootNode = processor.parseMarkdown(mkd.toCharArray());

        TreeNavigation nav;
        nav = new TreeNavigation();
        nav.push(rootNode);
        nav.pushChild(3);

        assertThat(nav.peek()).isInstanceOf(ParaNode.class);

        //
        //
        Optional<TreeNavigation> result =
                siblingBefore()
                        .then(ofType(AttributesNode.class))
                        .query(nav);
        assertThat(result.isPresent()).isTrue();
        assertThat(result.get().peek()).isInstanceOf(AttributesNode.class);
    }

}