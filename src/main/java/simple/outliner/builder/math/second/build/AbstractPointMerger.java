package simple.outliner.builder.math.second.build;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import com.kadme.test.Line;
import com.kadme.test.Point;
import simple.outliner.builder.math.second.geom.Polygon2D;
import simple.outliner.builder.math.second.geom.Segment;
import simple.outliner.builder.math.second.geom.SegmentType;

/** Common functionalities for merging point to the polygon. */
public abstract class AbstractPointMerger extends AbstractMergerParent
{

    /**
     * Merge single point to polygon.
     * @param point the point to merge.
     * @param polygon the polygon.
     * @param line the line the points belongs to.
     * @return true if point was merged.
     */
    protected boolean mergeOutsidePoint(final Point point, final Polygon2D polygon, final Line line)
    {
        List<Segment> reachableFromP1 =  filterReachableSegmentsSortedByDistance(polygon.getSegments(), point);
        final Segment fromLine = new Segment(line);
        final Optional<Segment> crossingSegmentOptional = reachableFromP1.stream().filter(fromLine::intersects).findFirst();
        // At least one should exists.
        if (!crossingSegmentOptional.isPresent())
        {
            return false;
        }
        final Segment crossing = crossingSegmentOptional.get();
        final Segment connection1 = new Segment(crossing.getX1(), crossing.getY1(), point.getX(), point.getY(), SegmentType.SOFT);
        final Segment connection2 = new Segment(point.getX(), point.getY(), crossing.getX2(), crossing.getY2(), SegmentType.SOFT);
        final int indexOfCrossingSegment = polygon.getSegments().indexOf(crossing);
        polygon.getSegments().remove(crossing);
        polygon.getSegments().add(indexOfCrossingSegment, connection1);
        polygon.getSegments().add(indexOfCrossingSegment+1, connection2);

        //connection from first to p1 of segment
        //Segment connection1 = null;
        //Segment connection2 = null;
        //
        //int newFirstPosition = 0;
        //if (toRemoveFirst.size() > toRemoveLast.size())
        //{
        //    polygon.getSegments().removeAll(toRemoveLast);
        //    newFirstPosition = polygon.getSegments().indexOf(last);
        //    //connection from first to p1 of segment
        //    connection1 = new Segment(last.getX2(), last.getY2(), point.getX(), point.getY(), SegmentType.SOFT);
        //    connection2 = new Segment(point.getX(), point.getY(), first.getX2(), first.getY2(), SegmentType.SOFT);
        //}
        //else
        //{
        //    polygon.getSegments().removeAll(toRemoveFirst);
        //    newFirstPosition = polygon.getSegments().indexOf(first);
        //    //connection from first to p1 of segment
        //    connection1 = new Segment(first.getX2(), first.getY2(), point.getX(), point.getY(), SegmentType.SOFT);
        //    connection2 = new Segment(point.getX(), point.getY(), last.getX2(), last.getY2(), SegmentType.SOFT);
        //}
        //
        ////Add new segments
        //segments.add(newFirstPosition+1, connection1);
        //segments.add(newFirstPosition+2, connection2);
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
