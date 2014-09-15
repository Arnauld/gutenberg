package gutenberg.itext.support;

import com.google.common.base.Supplier;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import gutenberg.itext.FontCopier;
import gutenberg.itext.ITextContext;
import gutenberg.itext.SimpleEmitter;
import gutenberg.itext.Styles;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class FirstPageRenderer implements SimpleEmitter {

    public static final String FIRST_PAGE_TITLE_FONT = "first-page-title-font";
    public static final String FIRST_PAGE_SUBJECT_FONT = "first-page-subject-font";

    private Logger log = LoggerFactory.getLogger(FirstPageRenderer.class);

    private final String title;
    private final String subject;

    public FirstPageRenderer(String title, String subject) {
        this.title = title;
        this.subject = subject;
    }

    @Override
    public void emit(ITextContext context) {
        try {
            Document document = context.getDocument();
            appendMainPage(context, document);
            document.newPage();
        } catch (DocumentException e) {
            log.warn("Failed to render cover page", e);
        }
    }

    private void appendMainPage(ITextContext context, Document document) throws DocumentException {
        Paragraph mainPage = new Paragraph();
        appendTitle(context, mainPage);
        appendSubject(context, mainPage);
        document.add(mainPage);
    }

    private void appendSubject(ITextContext context, Paragraph preface) {
        if (StringUtils.isEmpty(subject)) {
            return;
        }

        Styles styles = context.styles();
        Font font = styles.getFont(FIRST_PAGE_SUBJECT_FONT).or(subjectFont(styles));
        for (String titlePart : subject.split("[\n\r]+")) {
            Paragraph paragraph = new Paragraph(titlePart, font);
            paragraph.setAlignment(Element.ALIGN_RIGHT);
            paragraph.setSpacingAfter(15.0f);
            preface.add(paragraph);
        }
    }

    private Supplier<? extends Font> subjectFont(final Styles styles) {
        return new Supplier<Font>() {
            @Override
            public Font get() {
                return FontCopier
                            .copyFont(styles.defaultFont())
                            .color(BaseColor.DARK_GRAY)
                            .size(18f)
                            .get();
            }
        };
    }

    private void appendTitle(ITextContext context, Paragraph preface) {
        if (StringUtils.isEmpty(title)) {
            return;
        }

        Styles styles = context.styles();
        Font font = styles.getFont(FIRST_PAGE_TITLE_FONT).or(titleFont(styles));
        Paragraph paragraph = null;
        for (String titlePart : title.split("[\n\r]+")) {
            paragraph = new Paragraph(titlePart, font);
            paragraph.setAlignment(Element.ALIGN_RIGHT);
            paragraph.setSpacingAfter(15.0f);
            preface.add(paragraph);
        }

        if (paragraph != null) {
            paragraph.setSpacingAfter(30.0f);
        }
    }

    private Supplier<? extends Font> titleFont(final Styles styles) {
        return new Supplier<Font>() {
            @Override
            public Font get() {
                return FontCopier
                        .copyFont(styles.defaultFont())
                        .color(styles.defaultColor())
                        .size(32f)
                        .get();
            }
        };
    }


}
