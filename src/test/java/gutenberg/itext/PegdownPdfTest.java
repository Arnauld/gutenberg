package gutenberg.itext;

import com.google.common.collect.Lists;
import com.itextpdf.text.*;
import gutenberg.TestSettings;
import gutenberg.pegdown.plugin.AttributesPlugin;
import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;
import org.pegdown.Extensions;
import org.pegdown.PegDownProcessor;
import org.pegdown.ast.HeaderNode;
import org.pegdown.ast.Node;
import org.pegdown.ast.ParaNode;
import org.pegdown.ast.RootNode;
import org.pegdown.ast.StrongEmphSuperNode;
import org.pegdown.ast.TextNode;
import org.pegdown.plugins.PegDownPlugins;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class PegdownPdfTest {
    private String workingDir;
    private ITextContext iTextContext;
    private Document document;
    private Stack<Element> stack = new Stack<Element>();

    @Before
    public void setUp() throws IOException, DocumentException {
        workingDir = new TestSettings().workingDir();
        pushFont(FontFactory.getFont(FontFactory.HELVETICA, 12.0f, Font.NORMAL));
    }

    @Test
    public void simpleGenerate() throws Exception {
        iTextContext = openDocument("simpleGenerate");
        document = iTextContext.getDocument();
        String mkd = loadResource("/gutenberg/pegdown/astText.md").trim();

        PegDownPlugins plugins = PegDownPlugins
                .builder()
                .withPlugin(AttributesPlugin.class)
                .build();
        PegDownProcessor processor = new PegDownProcessor(Extensions.ALL, plugins);
        RootNode rootNode = processor.parseMarkdown(mkd.toCharArray());

        for (Element element : lookupInterceptor(rootNode).process(0, rootNode)) {
            document.add(element);
        }
        iTextContext.close();
    }

    private static int[] counters;

    private static int nextChapterFor(int lvl) {
        if (counters == null) {
            counters = new int[10];
        }

        int i = ++counters[lvl];
        Arrays.fill(counters, lvl, counters.length, 1);
        return i;
    }


    private static List<Element> elements(Element... elements) {
        return Arrays.asList(elements);
    }

    private ITextContext openDocument(String method) throws FileNotFoundException, DocumentException {
        File file = new File(workingDir, getClass().getSimpleName() + "_" + method + ".pdf");
        return new ITextContext().open(file);
    }

    protected String loadResource(String resourceName) throws IOException {
        InputStream stream = getClass().getResourceAsStream(resourceName);
        try {
            return IOUtils.toString(stream, "utf8");
        } finally {
            IOUtils.closeQuietly(stream);
        }
    }

    private static String indent(int level) {
        StringBuilder b = new StringBuilder();
        for (int i = 0; i < level; i++)
            b.append("    ");
        return b.toString();
    }

    private static Interceptor lookupInterceptor(Node node) {
        if (node instanceof ParaNode) {
            return new Interceptor() {
                @Override
                public List<Element> process(int level, Node node) {
                    List<Element> subs = super.process(level, node);
                    Paragraph p = new Paragraph();
                    p.addAll(subs);
                    return elements(p);
                }
            };
        }

        if (node instanceof StrongEmphSuperNode) {
            final StrongEmphSuperNode emNode = (StrongEmphSuperNode) node;
            return new Interceptor() {
                @Override
                public List<Element> process(int level, Node node) {
                    Font font = peekFont();
                    int style = emNode.isStrong() ? Font.BOLD : Font.ITALIC;
                    pushFont(new Font(font.getBaseFont(), font.getSize(), font.getStyle() | style));
                    List<Element> subs = super.process(level, node);
                    popFont();
                    return subs;
                }
            };
        }

        if (node instanceof TextNode) {
            final TextNode tNode = (TextNode) node;
            return new Interceptor() {
                @Override
                public List<Element> process(int level, Node node) {
                    List<Element> subs = super.process(level, node);
                    return elements(new Chunk(tNode.getText(), peekFont()));
                }
            };
        }

        if (node instanceof HeaderNode) {
            final HeaderNode hNode = (HeaderNode) node;
            return new Interceptor() {
                @Override
                public List<Element> process(int level, Node node) {
                    List<Element> subs = super.process(level, node);
                    int hLevel = hNode.getLevel();

                    Paragraph p = new Paragraph();
                    p.addAll(subs);
                    Chapter chapter = new Chapter(p, nextChapterFor(hLevel));
                    return elements(chapter);
                }
            };
        }

        // default
        return new Interceptor();
    }

    private static Stack<Font> fontStack = new Stack<Font>();

    private static Font peekFont() {
        return fontStack.peek();
    }

    private static void pushFont(Font font) {
        fontStack.push(font);
    }

    private static Font popFont() {
        return fontStack.pop();
    }

    public static class Interceptor {
        public List<Element> process(int level, Node node) {
            String indent = indent(level);
            System.out.println(indent + ((getClass() == Interceptor.class) ? "" : "* ") + node);

            List<Element> subs = Lists.newArrayList();
            for (Node child : node.getChildren()) {
                Interceptor next = lookupInterceptor(child);
                subs.addAll(next.process(level + 1, child));
            }
            return subs;
        }

    }

}
