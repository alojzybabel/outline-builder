package simple.outliner.builder.math.second;

import java.util.LinkedList;

import com.kadme.test.Line;
import simple.outliner.builder.math.second.geom.Polygon2D;
import simple.outliner.builder.math.second.geom.Segment;
import simple.outliner.builder.math.second.geom.SegmentType;

public abstract class AbstractOutlineBuilderParent
{
    protected Polygon2D createPolygon2D(final LinkedList<Line> line)
    {
        final LinkedList<Segment> initialSegments = new LinkedList<>();
        final Segment first = new Segment(line.pollFirst());
        Segment second = new Segment(line.pollFirst());
        if (first.intersects(second))
        {
            Segment connection1 = new Segment(first.getX1(), first.getY1(),
                                              second.getX1(), second.getY1(), SegmentType.SOFT);
            Segment connection2 = new Segment(second.getX1(), second.getY1(),
                                              first.getX2(), first.getY2(), SegmentType.SOFT);
            Segment connection3 = new Segment(first.getX2(), first.getY2(),
                                              second.getX2(), second.getY2(), SegmentType.SOFT);
            Segment connection4 = new Segment(second.getX2(), second.getY2(),
                                              first.getX1(), first.getY1(), SegmentType.SOFT);

            initSegments(initialSegments, connection1, connection3, connection2, connection4);
        }
        else
        {
            Segment connection1 = new Segment(first.getX2(), first.getY2(),
                                              second.getX1(), second.getY1(), SegmentType.SOFT);
            Segment connection2 = new Segment(second.getX2(), second.getY2(),
                                              first.getX1(), first.getY1(), SegmentType.SOFT);

            if (connection1.intersects(connection2))
            {
                second = new Segment(second.getX2(), second.getY2(), second.getX1(), second.getY1(), SegmentType.HARD);
                connection1 = new Segment(first.getX2(), first.getY2(),
                                          second.getX1(), second.getY1(), SegmentType.SOFT);
                connection2 = new Segment(second.getX2(), second.getY2(),
                                          first.getX1(), first.getY1(), SegmentType.SOFT);

            }
            initSegments(initialSegments, first, second, connection1, connection2);
        }
        return new Polygon2D(initialSegments);
    }

    protected void initSegments(final LinkedList<Segment> initialSegments, final Segment first, final Segment second,
                              final Segment connection1, final Segment connection2)
    {
        initialSegments.add(first);
        initialSegments.add(connection1);
        initialSegments.add(second);
        initialSegments.add(connection2);
    }
}
