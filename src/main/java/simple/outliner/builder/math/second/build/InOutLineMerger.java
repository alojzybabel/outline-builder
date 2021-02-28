package simple.outliner.builder.math.second.build;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.kadme.test.Line;
import com.kadme.test.Point;
import simple.outliner.builder.math.second.geom.Polygon2D;
import simple.outliner.builder.math.second.geom.Segment;
import simple.outliner.builder.math.second.geom.SegmentType;

/** Merge lines if one point of the line is inside the polygon and second is outside. */
public class InOutLineMerger extends AbstractOutlineParent implements LineMerger
{
    @Override
    public boolean merge(final Polygon2D polygon, final Line line)
    {
        if (isMyJob(polygon, line))
        {
            final Point outlinePoint = polygon.contains(line.getP1()) ? line.getP2() : line.getP1();
            List<Segment> reachableFromP1 =  filterReachableSegmentsSortedByDistance(polygon.getSegments(), outlinePoint.getX(), outlinePoint.getY());
            final Segment p11 = reachableFromP1.get(0);
            final Segment p12 = reachableFromP1.get(1);
            final int index1 = polygon.getSegments().indexOf(p11);
            final int index2 = polygon.getSegments().indexOf(p12);
            mergePoint(index1, index2, line.getP1(), polygon);
            return true;
        }
        return false;
    }

    private void mergePoint(int start, int end, final Point point, final Polygon2D polygon)
    {
        if (start == end)
        {
            throw new IllegalStateException("Start and end index in outline crossing merger can not be the same.");
        }

        final LinkedList<Segment> segments = polygon.getSegments();
        final Segment first = polygon.getSegments().get(start);
        final Segment last = polygon.getSegments().get(end);

        //Remove old segments
        final List<Segment> toRemoveFirst = new ArrayList<>();
        final int size = polygon.getSegments().size();
        for (int i=start+1 ;; i++)
        {
            final Segment s = polygon.getSegments().get(i%size);
            toRemoveFirst.add(s);
            if(s == last)
            {
                break;
            }
        }

        //Remove old segments
        final List<Segment> toRemoveLast = new ArrayList<>();
        for (int i=end+1 ;; i++)
        {
            final Segment s = polygon.getSegments().get(i%size);
            toRemoveLast.add(s);
            if(s == first)
            {
                break;
            }
        }

        //connection from first to p1 of segment
        Segment connection1 = null;
        Segment connection2 = null;

        int newFirstPosition = 0;
        if (toRemoveFirst.size() > toRemoveLast.size())
        {
            polygon.getSegments().removeAll(toRemoveLast);
            newFirstPosition = polygon.getSegments().indexOf(last);
            //connection from first to p1 of segment
            connection1 = new Segment(last.getX2(), last.getY2(), point.getX(), point.getY(), SegmentType.SOFT);
            connection2 = new Segment(point.getX(), point.getY(), first.getX2(), first.getY2(), SegmentType.SOFT);
        }
        else
        {
            polygon.getSegments().removeAll(toRemoveFirst);
            newFirstPosition = polygon.getSegments().indexOf(first);
            //connection from first to p1 of segment
            connection1 = new Segment(first.getX2(), first.getY2(), point.getX(), point.getY(), SegmentType.SOFT);
            connection2 = new Segment(point.getX(), point.getY(), last.getX2(), last.getY2(), SegmentType.SOFT);
        }

        //Add new segments
        segments.add(newFirstPosition+1, connection1);
        segments.add(newFirstPosition+2, connection2);
    }

    /**
     * Check if the line should be merge by this merger.
     * It should happen if both points ends of the line are outside the polygon and the line cross some of the polygon segments.
     * @param polygon the polygon.
     * @param line the line.
     * @return true if the both of the line ends are outside the polygon and the line cross some of the polygon edges.
     */
    private boolean isMyJob(final Polygon2D polygon, final Line line)
    {
        return (!polygon.contains(line.getP1())
               && polygon.contains(line.getP2()))
            || (polygon.contains(line.getP1())
               && !polygon.contains(line.getP2()));
    }
}