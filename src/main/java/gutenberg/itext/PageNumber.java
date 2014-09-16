package gutenberg.itext;

import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;
import gutenberg.util.RomanNumeral;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class PageNumber extends PdfPageEventHelper {

    private Sequence pnMainMatter = new Sequence();
    private Sequence pnFrontMatter = new Sequence(0, true);

    private PageInfos.Matter currentMatter = PageInfos.Matter.Front;
    private Sequence pnCurrent = pnFrontMatter;
    private int pageNumber;
    private List<PageInfos> emittedPageInfos = new ArrayList<PageInfos>();
    private String[] sectionTitles = new String[10];

    public void notifyPageChange() {
        pageNumber++;
        pnCurrent = pnCurrent.next();
    }

    @Override
    public void onChapter(PdfWriter writer, Document document, float paragraphPosition, Paragraph title) {
        Arrays.fill(sectionTitles, 1, sectionTitles.length, null);
        sectionTitles[1] = title.getContent();
    }

    @Override
    public void onSection(PdfWriter writer, Document document, float paragraphPosition, int depth, Paragraph title) {
        Arrays.fill(sectionTitles, depth, sectionTitles.length, null);
        sectionTitles[depth] = title.getContent();
    }

    @Override
    public void onStartPage(PdfWriter writer, Document document) {
        notifyPageChange();
    }

    public PageInfos pageInfos() {
        PageInfos pageInfos = new PageInfos(
                pageNumber,
                pnCurrent.formatPageNumber(),
                currentMatter,
                Arrays.copyOf(sectionTitles, sectionTitles.length));

        if (!emittedPageInfos.contains(pageInfos))
            emittedPageInfos.add(pageInfos);

        return pageInfos;
    }

    private List<PageInfos> emittedPageInfos() {
        return emittedPageInfos;
    }

    public void continueFrontMatter() {
        currentMatter = PageInfos.Matter.Front;
        PageInfos pageInfos = lastFrontMatterInfos();
        if (pageInfos != null) {
            sectionTitles = Arrays.copyOf(pageInfos.sectionTitles(), sectionTitles.length);
        } else {
            resetText();
        }
        pnCurrent.next = pnFrontMatter;
    }

    private void resetText() {
        Arrays.fill(sectionTitles, 0, sectionTitles.length, null);
    }

    public void startFrontMatter() {
        currentMatter = PageInfos.Matter.Front;
        resetText();
        pnFrontMatter = new Sequence(0, true);
        pnCurrent.next = pnFrontMatter;
    }

    public void startMainMatter() {
        currentMatter = PageInfos.Matter.Main;
        resetText();
        pnMainMatter = new Sequence();
        pnCurrent.next = pnMainMatter;
    }

    private PageInfos lastFrontMatterInfos() {
        PageInfos found = null;
        for (PageInfos pageInfos : emittedPageInfos()) {
            if (!pageInfos.is(PageInfos.Matter.Front))
                break;
            found = pageInfos;
        }
        return found;
    }

    public int lookupFrontMatterLastPage() {
        PageInfos pageInfos = lastFrontMatterInfos();
        if (pageInfos == null)
            return -1;
        else
            return pageInfos.getRawPageNumber();
    }

    private static class Sequence {
        int count = 1;
        Sequence next;
        boolean isRoman;

        public Sequence(int start, boolean isRoman) {
            this.count = start;
            this.isRoman = isRoman;
        }

        public Sequence() {
            this(1, false);
        }

        public String formatPageNumber() {
            if (count == 0)
                count++;
            if (isRoman)
                return new RomanNumeral().format(count);
            else
                return String.valueOf(count);
        }

        public Sequence next() {
            count++;
            if (next != null) {
                Sequence tmp = next;
                next = null;
                return tmp;
            }
            return this;
        }
    }
}
