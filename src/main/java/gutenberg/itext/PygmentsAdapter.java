package gutenberg.itext;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPTableEvent;
import gutenberg.pegdown.plugin.Attributes;
import gutenberg.pygments.Pygments;
import gutenberg.pygments.StyleSheet;
import gutenberg.pygments.TokenWithValue;
import gutenberg.pygments.Tokens;
import gutenberg.util.RGB;
import gutenberg.util.Style;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static gutenberg.itext.ITextUtils.toColor;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class PygmentsAdapter {
    private final Pygments pygments;
    private final StyleSheet styleSheet;
    private final BaseFont codeFont;
    private final float fontSize;

    public PygmentsAdapter(Pygments pygments,
                           StyleSheet styleSheet,
                           BaseFont codeFont,
                           float fontSize) {
        this.pygments = pygments;
        this.styleSheet = styleSheet;
        this.codeFont = codeFont;
        this.fontSize = fontSize;
    }

    public List<Element> process(String lang, String content, Attributes attributes) {
        boolean linenos = attributes.isOn("linenos");

        Tokens tokens = pygments.tokenize(lang, content);
        Paragraph p = new Paragraph();
        for (TokenWithValue token : tokens) {
            Style style = styleSheet.styleOf(token.token);

            BaseColor color = toColor(style.fg());
            int s = calculateStyle(style);

            Font font = new Font(codeFont, fontSize, s, color);
            Chunk o = new Chunk(token.value, font);

            RGB bg = style.bg();
            if (bg != null)
                o.setBackground(toColor(bg));
            p.add(o);
        }

        PdfPCell cell = new PdfPCell(p);
        cell.setPaddingBottom(5.0f);
        cell.setBorder(Rectangle.NO_BORDER);

        PdfPTable table = new PdfPTable(1);
        table.addCell(cell);
        table.setSpacingBefore(5.0f);
        table.setSpacingAfter(5.0f);
        table.setTableEvent(new TableBackground(toColor(styleSheet.backgroundColor())));
        return Arrays.<Element>asList(table);
    }

    public static class TableBackground implements PdfPTableEvent {

        private final BaseColor backgroundColor;

        public TableBackground(BaseColor backgroundColor) {
            this.backgroundColor = backgroundColor;
        }

        public void tableLayout(PdfPTable table, float[][] width, float[] height,
                                int headerRows, int rowStart, PdfContentByte[] canvas) {
            // because table is only one cell length, one can focus on using the first width
            float xmin = width[0][0];
            float xmax = width[0][1];
            float ymin = height[height.length - 1];
            float ymax = height[0];
            float d  = 3;

            PdfContentByte background = canvas[PdfPTable.BASECANVAS];
            background.saveState();
            background.setColorFill(backgroundColor);
            background.roundRectangle(
                    xmin - d,
                    ymin - d,
                    (xmax + d) - (xmin - d),
                    (ymax + d) - (ymin - d),
                    d+d);
            background.fill();
            background.restoreState();
        }

    }

    private int calculateStyle(Style style) {
        int s = Font.NORMAL;
        if (style.isBold()) {
            s |= Font.BOLD;
        }
        if (style.isItalic()) {
            s |= Font.ITALIC;
        }
        if (style.isStrikethrough()) {
            s |= Font.STRIKETHRU;
        }
        if (style.isUnderline()) {
            s |= Font.UNDERLINE;
        }
        return s;
    }

}
