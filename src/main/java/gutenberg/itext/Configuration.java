package gutenberg.itext;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.pdf.BaseFont;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class Configuration {
    private Logger log = LoggerFactory.getLogger(Configuration.class);
    //
    private Font codeFont = new Font(FontFactory.getFont(FontFactory.COURIER).getBaseFont(), 12.0f);

    public Configuration() {
    }

    public Font sourceCodeFont() {
        return codeFont;
    }

    public Configuration sourceCodeFont(Font codeFont) {
        this.codeFont = codeFont;
        return this;
    }




}
