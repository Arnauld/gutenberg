package gutenberg.itext.pegdown;

import com.itextpdf.awt.FontMapper;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.pdf.BaseFont;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.Font;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import static org.apache.commons.io.IOUtils.toByteArray;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class JLaTeXmathFontMapper implements FontMapper {

    private Logger log = LoggerFactory.getLogger(JLaTeXmathFontMapper.class);

    @Override
    public BaseFont awtToPdf(Font font) {
        String fontName = font.getName();
        try {

            if ("sanserif".equalsIgnoreCase(fontName))
                return FontFactory.getFont(FontFactory.HELVETICA).getBaseFont();

            InputStream stream = openStream(fontName);
            if (stream == null) {
                log.error("Font resource not found for font {}", fontName);
                throw new RuntimeException("Font resource not found for font " + fontName);
            }
            return BaseFont.createFont(
                    font.getName() + ".ttf",
                    BaseFont.IDENTITY_H,
                    BaseFont.EMBEDDED,
                    true,
                    toByteArray(stream),
                    null
            );
        } catch (IOException e) {
            throw new RuntimeException("Failed to map font " + fontName, e);
        } catch (DocumentException e) {
            throw new RuntimeException("Failed to map font " + fontName, e);
        }
    }

    private InputStream openStream(String fontName) {
        for (String basePath : Arrays.asList(
                "/org/scilab/forge/jlatexmath/fonts/maths/",
                "/org/scilab/forge/jlatexmath/fonts/maths/optional",
                "/org/scilab/forge/jlatexmath/fonts/latin/",
                "/org/scilab/forge/jlatexmath/fonts/latin/optional",
                "/org/scilab/forge/jlatexmath/fonts/base/",
                "/org/scilab/forge/jlatexmath/fonts/euler/")) {
            String name = basePath + fontName + ".ttf";
            InputStream stream = getClass().getResourceAsStream(name);
            if (stream != null)
                return stream;
        }
        return null;
    }

    @Override
    public Font pdfToAwt(BaseFont font, int size) {
        return null;
    }
}
