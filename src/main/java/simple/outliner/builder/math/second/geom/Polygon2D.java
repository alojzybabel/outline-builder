package simple.outliner.builder.math.second.geom;

import java.awt.geom.Path2D;
import java.util.LinkedList;
import java.util.List;

/** Represents the polygon. */
public class Polygon2D
{
    private final LinkedList<Segment> segments;

    public Polygon2D(final LinkedList<Segment> segments)
    {
        //Initial polygon should be crated from two lines connected.
        if (segments.size() < 4)
        {
            throw new IllegalStateException();
        }
        this.segments = segments;
    }

    public boolean contains(final double x, final double y)
    {
        return generatePath().contains(x, y);
    }

    public Path2D generatePath()
    {
        Path2D path = new Path2D.Double();
        path.moveTo(segments.get(0).getX2(), segments.get(0).getY2());
        for(int i = 1; i < segments.size(); ++i) {
            path.lineTo(segments.get(i).getX2(), segments.get(i).getY2());
        }
        path.closePath();
        return path;
    }

    public void add(final Segment segment)
    {
        segments.add(segment);
    }

    public LinkedList<Segment> getSegments()
    {
        return segments;
    }
}
