package gutenberg.itext;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class PageInfos {
    private final int rawPageNumber;
    private final String formattedPageNumber;
    private final boolean extra;

    public PageInfos(int rawPageNumber, String formattedPageNumber, boolean isExtra) {
        this.rawPageNumber = rawPageNumber;
        this.formattedPageNumber = formattedPageNumber;
        this.extra = isExtra;
    }

    public String getFormattedPageNumber() {
        return formattedPageNumber;
    }

    public int getRawPageNumber() {
        return rawPageNumber;
    }

    public boolean isExtra() {
        return extra;
    }

    @Override
    public String toString() {
        return "PageInfos{" +
                "raw: " + rawPageNumber +
                ", formatted: '" + formattedPageNumber + '\'' +
                ", xtra=" + extra +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof PageInfos))
            return false;

        PageInfos pageInfos = (PageInfos) o;
        return extra == pageInfos.extra
                && rawPageNumber == pageInfos.rawPageNumber
                && formattedPageNumber.equals(pageInfos.formattedPageNumber);
    }

    @Override
    public int hashCode() {
        int result = rawPageNumber;
        result = 31 * result + (formattedPageNumber != null ? formattedPageNumber.hashCode() : 0);
        result = 31 * result + (extra ? 1 : 0);
        return result;
    }
}
