package gutenberg.ditaa;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.stathissideris.ascii2image.core.RenderingOptions;
import org.stathissideris.ascii2image.core.Shape3DOrderingComparator;
import org.stathissideris.ascii2image.graphics.Diagram;
import org.stathissideris.ascii2image.graphics.DiagramShape;
import org.stathissideris.ascii2image.graphics.DiagramText;
import org.stathissideris.ascii2image.graphics.ShapePoint;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

/**
 * Almost completely copied from <a href="https://github.com/stathissideris/ditaa/blob/master/src/org/stathissideris/ascii2image/graphics/BitmapRenderer.java">org.stathissideris.ascii2image.graphics.BitmapRenderer</a>
 *
 * @author Efstathios Sideris
 * @author <a href="http://twitter.com/aloyer">@aloyer</a> (minor adjustements)
 */
public class GraphicsRenderer {

    private Logger log = LoggerFactory.getLogger(GraphicsRenderer.class);
    private Color backgroundColor = Color.WHITE;

    @SuppressWarnings("unchecked")
    public void render(Diagram diagram, Graphics2D g2, RenderingOptions options) {

        Object antialiasSetting = RenderingHints.VALUE_ANTIALIAS_OFF;
        if (options.performAntialias())
            antialiasSetting = RenderingHints.VALUE_ANTIALIAS_ON;

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, antialiasSetting);

        g2.setColor(backgroundColor);
        g2.fillRect(0, 0, diagram.getWidth() + 10, diagram.getHeight() + 10);
        g2.setStroke(new BasicStroke(1, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_ROUND));

        ArrayList<DiagramShape> shapes = diagram.getAllDiagramShapes();

        log.debug("Rendering " + shapes.size() + " shapes (groups flattened)");

        Iterator<DiagramShape> shapesIt;
        if (options.dropShadows()) {
            //render shadows
            shapesIt = shapes.iterator();
            while (shapesIt.hasNext()) {
                DiagramShape shape = shapesIt.next();

                if (shape.getPoints().isEmpty()) continue;

                //GeneralPath path = shape.makeIntoPath();
                GeneralPath path = shape.makeIntoRenderPath(diagram);

                float offset = diagram.getMinimumOfCellDimension() / 3.333f;

                if (path != null
                        && shape.dropsShadow()
                        && shape.getType() != DiagramShape.TYPE_CUSTOM) {
                    GeneralPath shadow = new GeneralPath(path);
                    AffineTransform translate = new AffineTransform();
                    translate.setToTranslation(offset, offset);
                    shadow.transform(translate);
                    g2.setColor(new Color(150, 150, 150));
                    g2.fill(shadow);

                }
            }


            //blur shadows
//
//            if(true) {
//                int blurRadius = 6;
//                int blurRadius2 = blurRadius * blurRadius;
//                float blurRadius2F = blurRadius2;
//                float weight = 1.0f / blurRadius2F;
//                float[] elements = new float[blurRadius2];
//                for (int k = 0; k < blurRadius2; k++)
//                    elements[k] = weight;
//                Kernel myKernel = new Kernel(blurRadius, blurRadius, elements);
//
//                //if EDGE_NO_OP is not selected, EDGE_ZERO_FILL is the default which creates a black border
//                ConvolveOp simpleBlur =
//                        new ConvolveOp(myKernel, ConvolveOp.EDGE_NO_OP, null);
//
//                BufferedImage destination =
//                        new BufferedImage(
//                                image.getWidth(),
//                                image.getHeight(),
//                                image.getType());
//
//                simpleBlur.filter(image, (BufferedImage) destination);
//
//                //destination = destination.getSubimage(blurRadius/2, blurRadius/2, image.getWidth(), image.getHeight());
//                g2 = (Graphics2D) destination.getGraphics();
//                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, antialiasSetting);
//                renderedImage = (RenderedImage) destination;
//            }
        }


        //fill and stroke

        float dashInterval = Math.min(diagram.getCellWidth(), diagram.getCellHeight()) / 2;
        //Stroke normalStroke = g2.getStroke();

        float strokeWeight = diagram.getMinimumOfCellDimension() / 10;

        Stroke normalStroke = new BasicStroke(
                strokeWeight,
                //10,
                BasicStroke.CAP_ROUND,
                BasicStroke.JOIN_ROUND
        );

        Stroke dashStroke = new BasicStroke(
                strokeWeight,
                BasicStroke.CAP_BUTT,
                BasicStroke.JOIN_ROUND,
                0,
                new float[]{dashInterval},
                0
        );

        //TODO: at this stage we should draw the open shapes first in order to make sure they are at the bottom (this is useful for the {mo} shape)


        //find storage shapes
        ArrayList<DiagramShape> storageShapes = new ArrayList<DiagramShape>();
        shapesIt = shapes.iterator();
        while (shapesIt.hasNext()) {
            DiagramShape shape = shapesIt.next();
            if (shape.getType() == DiagramShape.TYPE_STORAGE) {
                storageShapes.add(shape);
            }
        }

        //render storage shapes
        //special case since they are '3d' and should be
        //rendered bottom to top
        //TODO: known bug: if a storage object is within a bigger normal box, it will be overwritten in the main drawing loop
        //(BUT this is not possible since tags are applied to all shapes overlaping shapes)


        Collections.sort(storageShapes, new Shape3DOrderingComparator());

        g2.setStroke(normalStroke);
        shapesIt = storageShapes.iterator();
        while (shapesIt.hasNext()) {
            DiagramShape shape = shapesIt.next();

            GeneralPath path = shape.makeIntoRenderPath(diagram);

            if (!shape.isStrokeDashed()) {
                if (shape.getFillColor() != null)
                    g2.setColor(shape.getFillColor());
                else
                    g2.setColor(Color.white);
                g2.fill(path);
            }

            if (shape.isStrokeDashed())
                g2.setStroke(dashStroke);
            else
                g2.setStroke(normalStroke);
            g2.setColor(shape.getStrokeColor());
            g2.draw(path);
        }

        //sort so that the largest shapes are rendered first
        Collections.sort(shapes, new ShapeAreaComparator());

        //render the rest of the shapes
        ArrayList<DiagramShape> pointMarkers = new ArrayList<DiagramShape>();
        shapesIt = shapes.iterator();
        while (shapesIt.hasNext()) {
            DiagramShape shape = shapesIt.next();
            if (shape.getType() == DiagramShape.TYPE_POINT_MARKER) {
                pointMarkers.add(shape);
                continue;
            }
            if (shape.getType() == DiagramShape.TYPE_STORAGE) {
                continue;
            }
            if (shape.getType() == DiagramShape.TYPE_CUSTOM) {
                renderCustomShape(shape, g2);
                continue;
            }

            if (shape.getPoints().isEmpty()) continue;

            GeneralPath path = shape.makeIntoRenderPath(diagram);

            //fill
            if (path != null && shape.isClosed() && !shape.isStrokeDashed()) {
                if (shape.getFillColor() != null)
                    g2.setColor(shape.getFillColor());
                else
                    g2.setColor(Color.white);
                g2.fill(path);
            }

            //draw
            if (shape.getType() != DiagramShape.TYPE_ARROWHEAD) {
                g2.setColor(shape.getStrokeColor());
                if (shape.isStrokeDashed())
                    g2.setStroke(dashStroke);
                else
                    g2.setStroke(normalStroke);
                g2.draw(path);
            }
        }

        //render point markers

        g2.setStroke(normalStroke);
        shapesIt = pointMarkers.iterator();
        while (shapesIt.hasNext()) {
            DiagramShape shape = shapesIt.next();
            //if(shape.getType() != DiagramShape.TYPE_POINT_MARKER) continue;

            GeneralPath path;
            path = shape.makeIntoRenderPath(diagram);

            g2.setColor(Color.white);
            g2.fill(path);
            g2.setColor(shape.getStrokeColor());
            g2.draw(path);
        }

        //handle text
        //g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        //renderTextLayer(diagram.getTextObjects().iterator());

        for (DiagramText text : (Iterable<DiagramText>) diagram.getTextObjects()) {
            g2.setFont(text.getFont());
            if (text.hasOutline()) {
                g2.setColor(text.getOutlineColor());
                g2.drawString(text.getText(), text.getXPos() + 1, text.getYPos());
                g2.drawString(text.getText(), text.getXPos() - 1, text.getYPos());
                g2.drawString(text.getText(), text.getXPos(), text.getYPos() + 1);
                g2.drawString(text.getText(), text.getXPos(), text.getYPos() - 1);
            }
            g2.setColor(text.getColor());
            g2.drawString(text.getText(), text.getXPos(), text.getYPos());
        }

        if (options.renderDebugLines()) {
            Stroke debugStroke =
                    new BasicStroke(
                            1,
                            BasicStroke.CAP_ROUND,
                            BasicStroke.JOIN_ROUND
                    );
            g2.setStroke(debugStroke);
            g2.setColor(new Color(170, 170, 170));
            g2.setXORMode(Color.white);
            for (int x = 0; x < diagram.getWidth(); x += diagram.getCellWidth())
                g2.drawLine(x, 0, x, diagram.getHeight());
            for (int y = 0; y < diagram.getHeight(); y += diagram.getCellHeight())
                g2.drawLine(0, y, diagram.getWidth(), y);
        }
    }

    @SuppressWarnings("UnusedParameters")
    private void renderCustomShape(DiagramShape shape, Graphics2D g2) {
        log.warn("Unsupported custom shape rendering... {}", shape);
    }

    public class ShapeAreaComparator implements Comparator<DiagramShape> {

        /**
         * Puts diagram shapes in order or area starting from largest to smallest
         */
        public int compare(DiagramShape shape1, DiagramShape shape2) {
            double y1 = calculateArea(shape1);
            double y2 = calculateArea(shape2);

            if (y1 > y2) return -1;
            if (y1 < y2) return 1;

            return 0;
        }

        /**
         * https://github.com/stathissideris/ditaa/blob/master/src/org/stathissideris/ascii2image/graphics/DiagramShape.java#L959
         */
        private double calculateArea(DiagramShape shape) {
            ArrayList points = shape.getPoints();
            if (points.size() == 0) return 0;

            double area = 0;

            for (int i = 0; i < points.size() - 1; i++) {
                ShapePoint point1 = (ShapePoint) points.get(i);
                ShapePoint point2 = (ShapePoint) points.get(i + 1);
                area += point1.x * point2.y;
                area -= point2.x * point1.y;
            }
            ShapePoint point1 = (ShapePoint) points.get(points.size() - 1);
            ShapePoint point2 = (ShapePoint) points.get(0);
            area += point1.x * point2.y;
            area -= point2.x * point1.y;

            return Math.abs(area / 2);
        }

    }
}
