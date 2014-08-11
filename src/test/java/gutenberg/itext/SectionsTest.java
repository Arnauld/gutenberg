package gutenberg.itext;

import com.itextpdf.text.Chapter;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Section;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class SectionsTest {

    @Test
    public void h1_should_be_chapter() {
        Sections sections = new Sections();
        Section section = sections.newSection(new Paragraph("Introduction"), 1);

        assertThat(section).isInstanceOf(Chapter.class);
        assertThat(sections.currentSection()).isSameAs(section);
        assertThat(sections.sections()).isEqualTo(new Section[]{null, section, null, null, null, null, null, null, null, null});
    }

    @Test(expected = IllegalStateException.class)
    public void h2_should_require_a_h1_defined_first() {
        Sections sections = new Sections();
        sections.newSection(new Paragraph("Introduction"), 2);
    }

    @Test
    public void h2_should_be_section() {
        Sections sections = new Sections();
        Section chapter = sections.newSection(new Paragraph("Getting Started"), 1);
        Section section = sections.newSection(new Paragraph("Introduction"), 2);

        assertThat(section).isInstanceOf(Section.class);
        assertThat(sections.currentSection()).isSameAs(section);
        assertThat(sections.sections()).isEqualTo(new Section[]{null, chapter, section, null, null, null, null, null, null, null});
    }

    @Test
    public void h3_should_be_section() {
        Sections sections = new Sections();
        Section chapter = sections.newSection(new Paragraph("Organizational Challenges"), 1);
        Section sectionChapter = sections.newSection(new Paragraph("Cultural Challenges"), 2);
        Section section1 = sections.newSection(new Paragraph("Organizational culture"), 3);

        assertThat(section1).isInstanceOf(Section.class);
        assertThat(sections.currentSection()).isSameAs(section1);
        assertThat(sections.sections()).isEqualTo(new Section[]{null, chapter, sectionChapter, section1, null, null, null, null, null, null});
    }

    @Test
    public void new_h3_should_replace_previous_h3() {
        Sections sections = new Sections();
        Section chapter = sections.newSection(new Paragraph("Organizational Challenges"), 1);
        Section sectionChapter = sections.newSection(new Paragraph("Cultural Challenges"), 2);
        Section section1 = sections.newSection(new Paragraph("Organizational culture"), 3);
        Section section2 = sections.newSection(new Paragraph("Barrier to success"), 3);

        assertThat(section2).isInstanceOf(Section.class);
        assertThat(sections.currentSection()).isSameAs(section2);
        assertThat(sections.sections()).isEqualTo(new Section[]{null, chapter, sectionChapter, section2, null, null, null, null, null, null});
    }

    @Test
    public void new_h2_should_discard_previous_h3() {
        Sections sections = new Sections();
        Section chapter = sections.newSection(new Paragraph("Organizational Challenges"), 1);
        Section sectionChapter1 = sections.newSection(new Paragraph("Cultural Challenges"), 2);
        Section section1 = sections.newSection(new Paragraph("Organizational culture"), 3);
        Section section2 = sections.newSection(new Paragraph("Barrier to success"), 3);
        Section sectionChapter2 = sections.newSection(new Paragraph("Team Logistics"), 2);

        assertThat(sections.currentSection()).isSameAs(sectionChapter2);
        assertThat(sections.sections()).isEqualTo(new Section[]{null, chapter, sectionChapter2, null, null, null, null, null, null, null});
    }

}