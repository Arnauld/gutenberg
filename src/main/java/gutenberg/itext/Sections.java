package gutenberg.itext;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.Lists;
import com.itextpdf.text.Chapter;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Section;

import java.util.Arrays;
import java.util.List;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class Sections {

    private int chapterCount = 0;
    private Section[] sections;
    private List<Chapter> chapters = Lists.newArrayList();

    public Section newSection(Paragraph sectionTitle, int hLevel) {
        if (sections == null) {
            sections = new Section[10];
        }
        Arrays.fill(sections, hLevel, sections.length, null);
        if (hLevel == 1) {
            Chapter chapter = new Chapter(sectionTitle, ++chapterCount);
            sections[hLevel] = chapter;
            chapters.add(chapter);
            return chapter;
        } else {
            Section parent = sections[hLevel - 1];
            if (parent == null) {
                throw new IllegalStateException("No parent section (depth H" + (hLevel - 1) + ") found");
            }
            Section section = parent.addSection(10.0f, sectionTitle);
            sections[hLevel] = section;
            return section;
        }
    }

    @VisibleForTesting
    Section[] sections() {
        return sections;
    }

    @VisibleForTesting
    Section currentSection() {
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
}
