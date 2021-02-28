package simple.outliner.builder.math.second.build;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.kadme.test.Point;
import simple.outliner.builder.math.second.geom.Polygon2D;
import simple.outliner.builder.math.second.geom.Segment;
import simple.outliner.builder.math.second.geom.SegmentType;

/** Common functionalities for merging point to the polygon. */
public abstract class AbstractPointMerger extends AbstractMergerParent
{

    /**
     * Merge single point to polygon.
     * @param point the point.
     * @param polygon the polygon.
     * @return true if point was merged.
     */
    protected boolean mergePoint(final Point point, final Polygon2D polygon)
    {
        List<Segment> reachableFromP1 =  filterReachableSegmentsSortedByDistance(polygon.getSegments(), point);
        // At least two reachable segment should be found.
        if (reachableFromP1.size() < 2)
        {
            return false;
        }
        final Segment first = reachableFromP1.get(0);
        final Segment last = reachableFromP1.get(1);
        final LinkedList<Segment> segments = polygon.getSegments();

        //Remove old segments
        final List<Segment> toRemoveFirst = findSegmentsToRemove(polygon, first, last);

        //Remove old segments
        final List<Segment> toRemoveLast = findSegmentsToRemove(polygon, last, first);

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
        return true;
    }

    /**
     * Find segments which should  be removed after merging the point.
     * @param polygon the polygon.
     * @param first the segment use as starting point for searching segments to remove.
     * @param last the last segment to remove.
     * @return collection of segments to remove.
     */
    protected List<Segment> findSegmentsToRemove(final Polygon2D polygon, final Segment first, final Segment last)
    {
        final List<Segment> toRemove = new ArrayList<>();
        final int size = polygon.getSegments().size();
        final int start = polygon.getSegments().indexOf(first);

        for (int i = start + 1;; i++)
        {
            final Segment s = polygon.getSegments().get(i % size);
            toRemove.add(s);
            if(s == last)
            {
                break;
            }
        }
        return toRemove;
    }
}
