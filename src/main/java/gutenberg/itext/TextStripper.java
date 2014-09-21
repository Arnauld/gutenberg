package gutenberg.itext;

import com.google.common.collect.Lists;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.ContentByteUtils;
import com.itextpdf.text.pdf.parser.ImageRenderInfo;
import com.itextpdf.text.pdf.parser.LineSegment;
import com.itextpdf.text.pdf.parser.PdfContentStreamProcessor;
import com.itextpdf.text.pdf.parser.RenderListener;
import com.itextpdf.text.pdf.parser.TextRenderInfo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class TextStripper {

    private Logger log = LoggerFactory.getLogger(TextStripper.class);

    private Page currentPage;
    private float charWidth = 5f;

    public float getCharWidth() {
        return charWidth;
    }

    public TextStripper charWidth(float charWidth) {
        this.charWidth = charWidth;
        return this;
    }

    /**
     * Extracts text from a PDF document.
     *
     * @param src the original PDF document
     * @throws java.io.IOException
     */
    public List<Page> extractText(InputStream src) throws IOException {
        List<Page> pages = Lists.newArrayList();

        PdfReader reader = new PdfReader(src);
        RenderListener listener = new InternalListener();
        PdfContentStreamProcessor processor = new PdfContentStreamProcessor(listener);

        for (int i = 1; i <= reader.getNumberOfPages(); i++) {
            pages.add(currentPage = new Page());
            PdfDictionary pageDic = reader.getPageN(i);
            PdfDictionary resourcesDic = pageDic.getAsDict(PdfName.RESOURCES);
            processor.processContent(ContentByteUtils.getContentBytesForPage(reader, i), resourcesDic);
        }
        reader.close();

        return pages;
    }

    public class Page {
        private List<Row> rows = Lists.newArrayList();

        Row rowAt(float y) {
            for (Row r : rows) {
                if (Math.abs(r.y - y) < 1e-3)
                    return r;
            }
            Row row = new Row(y);
            rows.add(row);
            return row;
        }

        public String renderedText() {
            Collections.sort(rows);
            StringBuilder b = new StringBuilder();
            for (Row row : rows) {
                float prevX = 0;
                Collections.sort(row.chunks);
                for (Text chunk : row.chunks) {
                    String tab = StringUtils.repeat(" ", toNumberOfEM(chunk.x - prevX));

                    b.append(tab).append(chunk.text).append(" ");
                    prevX = chunk.x + chunk.width;
                }
                b.append("\n");
            }
            return b.toString();
        }

        private int toNumberOfEM(float delta) {
            return (int) (delta / charWidth);
        }
    }

    public static class Text implements Comparable<Text> {
        public final float x;
        public final String text;
        public float width;

        public Text(float x, String text, float width) {
            this.x = x;
            this.text = text;
            this.width = width;
        }

        @Override
        public int compareTo(Text o) {
            return Float.compare(x, o.x);
        }
    }

    public static class Row implements Comparable<Row> {
        public final float y;
        public final List<Text> chunks = Lists.newArrayList();

        public Row(float y) {
            this.y = y;
        }

        public Row text(float x, String text, float width) {
            chunks.add(new Text(x, text, width));
            return this;
        }

        @Override
        public int compareTo(Row o) {
            return -1 * Float.compare(y, o.y);
        }
    }

    public class InternalListener implements RenderListener {

        public InternalListener() {
        }

        /**
         * @see com.itextpdf.text.pdf.parser.RenderListener#beginTextBlock()
         */
        public void beginTextBlock() {
        }

        /**
         * @see com.itextpdf.text.pdf.parser.RenderListener#endTextBlock()
         */
        public void endTextBlock() {
        }

        /**
         * @see com.itextpdf.text.pdf.parser.RenderListener#renderImage(
         *com.itextpdf.text.pdf.parser.ImageRenderInfo)
         */
        public void renderImage(ImageRenderInfo renderInfo) {
        }

        /**
         * @see com.itextpdf.text.pdf.parser.RenderListener#renderText(
         *com.itextpdf.text.pdf.parser.TextRenderInfo)
         */
        public void renderText(TextRenderInfo renderInfo) {
            LineSegment baseline = renderInfo.getBaseline();
            float x = baseline.getStartPoint().get(0);
            float y = baseline.getStartPoint().get(1);
            float w = baseline.getLength();
            String text = renderInfo.getText();

            log.debug("Text: @({}, {}) width: {} '{}'", x, y, w, text);
            currentPage().rowAt(y).text(x, text, w);
        }
    }

    private Page currentPage() {
        return currentPage;
    }
}
