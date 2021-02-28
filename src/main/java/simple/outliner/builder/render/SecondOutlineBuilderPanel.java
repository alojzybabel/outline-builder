package simple.outliner.builder.render;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;

import simple.outliner.builder.LineGenerator;
import simple.outliner.builder.RandomLineGenerator;
import simple.outliner.builder.math.second.OutlineBuilderImp;
import simple.outliner.builder.math.second.build.ComposedLineMerger;

public class SecondOutlineBuilderPanel extends JPanel implements ActionListener
{

    private final OutlineBuilderImp builder;

    public SecondOutlineBuilderPanel()
    {
        super(new BorderLayout());
        //final LineGenerator generator =  new RandomLineGenerator(2560, 1440, 100);
        final LineGenerator generator =  new RandomLineGenerator(800, 600, 100);
        builder = new OutlineBuilderImp(generator, new ComposedLineMerger());
    }

    private void doDrawing(Graphics g) {

        final BasicStroke thin =  new BasicStroke(1.0f);
        final BasicStroke thick =  new BasicStroke(2.0f);
        final Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.RED);
        g2d.setStroke(thick);
        g2d.draw(builder.getPolygon().generatePath());
        g2d.setStroke(thin);
        builder.getUsedLines().forEach( l -> {
            drawLine(l, Color.BLACK, g2d);
        });

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

    @Override
    public void actionPerformed(final ActionEvent actionEvent)
    {
        builder.nextIteration();
        updateUI();
    }
}