package simple.outliner.builder.render;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;

import javax.swing.JPanel;

import com.kadme.test.Line;
import simple.outliner.builder.LineGenerator;
import simple.outliner.builder.math.LineGroup;
import simple.outliner.builder.math.LineGroupContainer;

public class OutlineBuilderPanel extends JPanel
{
    private static final Color[] COLORS =
                    {
                        Color.BLACK,
                        Color.BLUE,
                        Color.CYAN,
                        Color.GRAY,
                        Color.GREEN,
                        Color.MAGENTA,
                        Color.ORANGE,
                        Color.PINK,
                        Color.WHITE,
                        Color.YELLOW
                    };

    public OutlineBuilderPanel()
    {
        super(new BorderLayout());
    }

    private void doDrawing(Graphics g) {

        Graphics2D g2d = (Graphics2D) g;
        final LineGroupContainer lineGroupContainer = new LineGroupContainer(4D, 0.2 * 2560);
        new LineGenerator(2560, 1440, 100)
                        .generate()
                        .stream()
                        .forEach(lineGroupContainer::add);

        int counter = 0;

        for (final LineGroup group : lineGroupContainer.getGroups())
        {
            for(final Line line : group.getLines())
            {
                if (group.getLines().size() == 1)
                {
                    drawLine(new LineAdapter(line), Color.RED, g2d);
                }
                else
                {
                    drawLine(new LineAdapter(line), COLORS[counter%COLORS.length], g2d);
                }
            }
            counter++;
        }
    }

    @Override
    public void paintComponent(Graphics g) {

        super.paintComponent(g);
        doDrawing(g);
    }

    private void drawLine(final LineAdapter line, final Color color, final Graphics2D g2d)
    {
        g2d.setColor(color);
        g2d.drawLine(line.getP1().getX(),
                     line.getP1().getY(),
                     line.getP2().getX(),
                     line.getP2().getY());

    }
}
