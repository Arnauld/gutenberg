package gutenberg.itext;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.Lists;
import com.itextpdf.text.Chapter;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Section;
import gutenberg.util.KeyValues;

import java.util.Arrays;
import java.util.List;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class Sections {

    private final KeyValues kvs;
    private final Styles styles;
    //
    private int chapterCount = 0;
    private Section[] sections = new Section[10];
    private List<Chapter> chapters = Lists.newArrayList();

    public Sections(KeyValues kvs, Styles styles) {
        this.kvs = kvs;
        this.styles = styles;
    }

    public Font sectionTitlePrimaryFont(int hLevel) {
        return styles.sectionTitleFontForLevel(hLevel);
    }

    public Section newSection(String title, int hLevel) {
        return newSection(title, hLevel, true);
    }

    public Section newSection(String title, int hLevel, boolean numbered) {
        Font font = sectionTitlePrimaryFont(hLevel);
        Paragraph pTitle = new Paragraph(title, font);
        return newSection(pTitle, hLevel, numbered);
    }

    public Section newSection(Paragraph sectionTitle, int hLevel) {
        return newSection(sectionTitle, hLevel, true);
    }

    public Section newSection(Paragraph sectionTitle, int hLevel, boolean numbered) {
        if (hLevel < 1)
            throw new IllegalArgumentException("Section hLevel starts at 1 (H1, H2, H3...)");

        Arrays.fill(sections, hLevel, sections.length, null);

        Section section;
        if (hLevel == 1) {
            if (numbered) // only increase chapter number if the number is used
                chapterCount++;

            Chapter chapter = new Chapter(sectionTitle, chapterCount);
            sections[hLevel] = chapter;
            chapters.add(chapter);
            section = chapter;
        } else {
            Section parent = sections[hLevel - 1];
            if (parent == null) {
                throw new IllegalStateException("No parent section (depth H" + (hLevel - 1) + ") found");
            }

            sectionTitle.setSpacingBefore(20f);

            section = parent.addSection(10.0f, sectionTitle);
            sections[hLevel] = section;
        }

        if (!numbered)
            section.setNumberDepth(0);
        return section;
    }

    @VisibleForTesting
    Section[] sections() {
        return sections;
    }

    public void leaveSection(int hLevel) {
        Arrays.fill(sections, hLevel, sections.length, null);
    }

    public Section currentSection() {
        Section prev = null;
        for (int i = 1; i < sections.length; i++) {
            Section section = sections[i];
            if (section != null) {
                prev = section;
            } else {
                break;
            }
        }
        return prev;
    }

    public void restoreChapter(Chapter chapter) {
        sections[1] = chapter;
    }

    public Chapter currentChapter() {
        return (Chapter) sections[1];
    }
}
