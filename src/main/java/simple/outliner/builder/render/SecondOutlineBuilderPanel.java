package simple.outliner.builder.render;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.JPanel;

import com.kadme.test.Line;
import com.kadme.test.OutlineBuilder;
import com.kadme.test.Polygon;
import simple.outliner.builder.LineGenerator;
import simple.outliner.builder.RandomLineGenerator;
import simple.outliner.builder.math.second.IterableOutlineBuilder;
import simple.outliner.builder.math.second.OutlineBuilderImp;
import simple.outliner.builder.math.second.build.ComposedLineMerger;

public class SecondOutlineBuilderPanel extends JPanel implements ActionListener
{

    private IterableOutlineBuilder builder;

    private final LineGenerator generator;

    private boolean isIterable = true;

    public SecondOutlineBuilderPanel()
    {
        super(new BorderLayout());
        generator = new RandomLineGenerator(1280, 600, 50);
        builder = new IterableOutlineBuilder(generator, new ComposedLineMerger());
    }

    private void doDrawing(Graphics g) {

        if (isIterable)
        {
            drawIterable((Graphics2D) g);
        }
        else
        {
            drawAll((Graphics2D) g);
        }
    }

    private void drawIterable(final Graphics2D g)
    {
        final BasicStroke thin =  new BasicStroke(1.0f);
        final BasicStroke thick =  new BasicStroke(2.0f);
        final Graphics2D g2d = g;
        g2d.setColor(Color.RED);
        g2d.setStroke(thick);
        g2d.draw(builder.getPolygon().generatePath());
        g2d.setStroke(thin);
        builder.getUsedLines().forEach( l -> {
            drawLine(l, Color.BLACK, g2d);
        });

        g2d.setStroke(thick);
        drawLine(builder.getUsedLines().get(builder.getUsedLines().size() - 1), Color.CYAN, g2d);
    }

    private void drawAll(final Graphics2D g)
    {
        final List<Line> lines = generator.generate();
        final OutlineBuilder builder = new OutlineBuilderImp(new ComposedLineMerger());
        final Polygon polygon = builder.buildOutline(new HashSet<>(lines));
        final List<PointAdapter> points = polygon.getPoints()
                                                 .stream()
                                                 .map(PointAdapter::new)
                                                 .collect(Collectors.toList());
        final int size = points.size();
        final int [] arrayX = new int[size];
        final int [] arrayY = new int[size];

        for(int i=0; i<points.size(); i++)
        {
            arrayX[i] = points.get(i).getX();
            arrayY[i] = points.get(i).getY();
        }

        final BasicStroke thin =  new BasicStroke(1.0f);
        final BasicStroke thick =  new BasicStroke(2.0f);
        final Graphics2D g2d = g;
        g2d.setStroke(thin);
        lines.stream().map(LineAdapter::new).forEach( l -> {
            drawLine(l, Color.BLACK, g2d);
        });
        g2d.setColor(Color.RED);
        g2d.setStroke(thick);
        g2d.drawPolygon(arrayX, arrayY, size);
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
        isIterable = true;
        updateUI();
    }

    public void reset()
    {
        isIterable = true;
        builder = new IterableOutlineBuilder(generator, new ComposedLineMerger());
        updateUI();
    }

    public void drawAll()
    {
        isIterable = false;
        updateUI();
    }
}