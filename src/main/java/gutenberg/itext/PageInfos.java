package gutenberg.itext;

import java.util.Arrays;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class PageInfos {

    public enum Matter {
        Front,
        Main,
        Back
    }

    private final int rawPageNumber;
    private final String formattedPageNumber;
    private final Matter matter;
    private final String[] sectionTitles;

    public PageInfos(int rawPageNumber,
                     String formattedPageNumber,
                     Matter matter,
                     String[] sectionTitles) {
        this.rawPageNumber = rawPageNumber;
        this.formattedPageNumber = formattedPageNumber;
        this.matter = matter;
        this.sectionTitles = sectionTitles;
    }

    public String getFormattedPageNumber() {
        return formattedPageNumber;
    }

    public int getRawPageNumber() {
        return rawPageNumber;
    }

    public boolean is(Matter matter) {
        return this.matter == matter;
    }

    public String[] sectionTitles() {
        return sectionTitles;
    }

    public String chapterTitle() {
        return sectionTitles[1];
    }

    public String sectionTitle() {
        String title = null;
        for (String sectionTitle : sectionTitles) {
            if (sectionTitle != null)
                title = sectionTitle;
        }
        return title;
    }

    @Override
    public String toString() {
        return "PageInfos{" +
                "raw: " + rawPageNumber +
                ", formatted: '" + formattedPageNumber + '\'' +
                ", matter=" + matter +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof PageInfos))
            return false;

        PageInfos pageInfos = (PageInfos) o;
        return matter == pageInfos.matter
                && rawPageNumber == pageInfos.rawPageNumber
                && formattedPageNumber.equals(pageInfos.formattedPageNumber)
                && Arrays.equals(sectionTitles, pageInfos.sectionTitles);
    }

    @Override
    public int hashCode() {
        int result = rawPageNumber;
        result = 31 * result + (formattedPageNumber != null ? formattedPageNumber.hashCode() : 0);
        result = 31 * result + matter.hashCode();
        return result;
    }
}
