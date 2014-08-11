package gutenberg.itext;

import com.itextpdf.text.Chapter;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Section;

import java.util.Arrays;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class Sections {

    private int[] counters;
    private Section[] sections;
    private Chapter chapter;


    public Section newSection(Paragraph p, int hLevel) {
        if (sections == null) {
            sections = new Section[10];
        }
        Arrays.fill(sections, hLevel, sections.length, null);
        if (hLevel == 1) {
            chapter = new Chapter(p, nextChapterFor(hLevel));
            sections[hLevel] = chapter;
            return chapter;
        } else {
            Section parent = sections[hLevel - 1];
            Section section = parent.addSection(p);
            sections[hLevel] = section;
            return section;
        }
    }

    private int nextChapterFor(int lvl) {
        if (counters == null) {
            counters = new int[10];
        }

        int i = ++counters[lvl];
        Arrays.fill(counters, lvl, counters.length, 1);
        return i;
    }
}
