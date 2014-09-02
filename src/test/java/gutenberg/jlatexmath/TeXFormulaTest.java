package gutenberg.jlatexmath;

import gutenberg.TestSettings;
import org.junit.Before;
import org.junit.Test;
import org.scilab.forge.jlatexmath.TeXConstants;
import org.scilab.forge.jlatexmath.TeXFormula;
import org.scilab.forge.jlatexmath.TeXIcon;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class TeXFormulaTest {

    private TestSettings settings;

    @Before
    public void setUp() {
        settings = new TestSettings();
    }

    @Test
    public void generate_image() throws IOException {
        String formula = "" +
                "\\Re{z} =\\frac{n\\pi \\dfrac{\\theta +\\psi}{2}}{\n" +
                "            \\left(\\dfrac{\\theta +\\psi}{2}\\right)^2 + \\left( \\dfrac{1}{2}\n" +
                "            \\log \\left\\vert\\dfrac{B}{A}\\right\\vert\\right)^2}.";

        TeXFormula.setDPITarget(600);

        BufferedImage image = (BufferedImage)TeXFormula.createBufferedImage(formula, TeXConstants.STYLE_DISPLAY, 14f, Color.BLUE, Color.WHITE);
        File output = new File(settings.workingDir(), "TeXFormulaTest__generate_image.png");
        ImageIO.write(image, "png", output);
        System.out.println("TeXFormulaTest.generate_image::" + output.getAbsolutePath());
    }

    @Test
    public void generate_image_from_icon() throws IOException {
        String formula = "" +
                "\\Re{z} =\\frac{n\\pi \\dfrac{\\theta +\\psi}{2}}{\n" +
                "            \\left(\\dfrac{\\theta +\\psi}{2}\\right)^2 + \\left( \\dfrac{1}{2}\n" +
                "            \\log \\left\\vert\\dfrac{B}{A}\\right\\vert\\right)^2}.";

        TeXFormula teXFormula = new TeXFormula(formula);
        TeXIcon teXIcon = teXFormula.createTeXIcon(TeXConstants.STYLE_DISPLAY, 14f);

        BufferedImage image = new BufferedImage(teXIcon.getIconWidth(), teXIcon.getIconHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = (Graphics2D)image.getGraphics();
        teXIcon.paintIcon(null, g2, 0, 0);
        g2.dispose();

        File output = new File(settings.workingDir(), "TeXFormulaTest__generate_image_from_icon.png");
        ImageIO.write(image, "png", output);
        System.out.println("TeXFormulaTest.generate_image_from_icon::" + output.getAbsolutePath());
    }
}
