package simple.outliner.builder.math.second.geom;

import java.awt.geom.Line2D;

import com.kadme.test.Line;

public class Segment
{
    private final Line2D line;
    private final SegmentType type;

    public Segment(final Line line)
    {
        this.line = new Line2D.Double(line.getP1().getX(),
                                      line.getP1().getY(),
                                      line.getP2().getX(),
                                      line.getP2().getY());
        this.type = SegmentType.HARD;
    }

    public Segment(final double x1, final double y1, final double x2, final double y2, final SegmentType type)
    {
        this.line = new Line2D.Double(x1,
                                      y1,
                                      x2,
                                      y2);
        this.type = SegmentType.HARD;
    }

    public Segment(final Line2D line, final SegmentType type)
    {
        this.line = line;
        this.type = type;
    }

    public Line2D getLine()
    {
        return line;
    }

    public SegmentType getType()
    {
        return type;
    }

    public boolean intersects(final Segment segment)
    {
        if (segment == null)
        {
            return false;
        }
        return line.intersectsLine(segment.getLine());
    }

    public boolean intersects(final double x1, final double y1, final double x2, final double y2)
    {
        return line.intersectsLine(x1, y1, x2, y2);
    }

    public double getX1()
    {
        return line.getX1();
    }

    public double getX2()
    {
        return line.getX2();
    }

    public double getY1()
    {
        return line.getY1();
    }

    public double getY2()
    {
        return line.getY2();
    }

    public double distanceFromPoint(final double x, final double y)
    {
        final double deltaX = x - getX2();
        final double deltaY = y - getY2();
        return Math.hypot(deltaX, deltaY);
    }
}