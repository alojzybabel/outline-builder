package simple.outliner.builder.math.second.build;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.kadme.test.Line;
import simple.outliner.builder.math.second.geom.Polygon2D;
import simple.outliner.builder.math.second.geom.Segment;
import simple.outliner.builder.math.second.geom.SegmentType;

/** Merge line if both line points are placed outside the polygon. */
public class OutlineLineMerger extends AbstractMergerParent implements LineMerger
{
    @Override
    public boolean merge(final Polygon2D polygon, final Line line)
    {
        if (isMyJob(polygon, line))
        {
            List<Segment> reachableFromP1 =  filterReachableSegmentsSortedByDistance(polygon.getSegments(), line.getP1());
            List<Segment> reachableFromP2 =  filterReachableSegmentsSortedByDistance(polygon.getSegments(), line.getP2());
            final Segment p1 = reachableFromP1.get(0);
            final Segment p2 = reachableFromP2.stream().filter( s -> s != p1).findFirst().get();
            final int index1 = polygon.getSegments().indexOf(p1);
            final int index2 = polygon.getSegments().indexOf(p2);
            if (index1 < index2)
            {
                final Segment lineSegment = new Segment(line);
                mergeLine(index1, index2, lineSegment, polygon);
            }
            else if (index1 > index2)
            {
                final Segment lineSegment = new Segment(line.getP2().getX(), line.getP2().getY(),
                                                        line.getP1().getX(), line.getP1().getY(), SegmentType.HARD);
                mergeLine(index2, index1, lineSegment, polygon);
            }
            else
            {
                throw new IllegalStateException("The ends of new segment cannot connect to the same point");
            }
            return true;
        }
        return false;
    }

    /**
     * Check if the line should be merge by this merger.
     * It should happen if both points ends of the line are outside the polygon and the line does not cross any of the polygon edges.
     * @param polygon the polygon.
     * @param line the line.
     * @return true if the both of the line ends are outside the polygon and thr line does not cross any of polygon edges.
     */
    private boolean isMyJob(final Polygon2D polygon, final Line line)
    {
        return !polygon.contains(line.getP1().getX(), line.getP1().getY())
               && !polygon.contains(line.getP2().getX(), line.getP2().getY())
               && !isTheLineIntersectingThePolygon(polygon, line);
    }

    /**
     * Merge line to the polygon.
     * @param start
     * @param end
     * @param segment
     * @param polygon
     */
    private void mergeLine(final int start, final int end, final Segment segment, final Polygon2D polygon)
    {
        final LinkedList<Segment> segments = polygon.getSegments();
        final Segment first = polygon.getSegments().get(start);
        final Segment last = polygon.getSegments().get(end);
        //connection from first to p1 of segment
        Segment connection1 = new Segment(first.getX2(), first.getY2(), segment.getX1(), segment.getY1(), SegmentType.SOFT);
        Segment connection2 = new Segment(segment.getX2(), segment.getY2(), last.getX2(), last.getY2(), SegmentType.SOFT);

        // Replace segment ends if connections intersect
        Segment segmentToInsert = segment;
        if (connection1.intersects(connection2))
        {
            segmentToInsert = new Segment(segment.getX2(), segment.getY2(), segment.getX1(), segment.getY1(), SegmentType.HARD);
            connection1 = new Segment(first.getX2(), first.getY2(), segmentToInsert.getX1(), segmentToInsert.getY1(), SegmentType.SOFT);
            connection2 = new Segment(segmentToInsert.getX2(), segmentToInsert.getY2(), last.getX2(), last.getY2(), SegmentType.SOFT);
        }

        //Remove old segments
        final List<Segment> toRemove = new ArrayList<>(end-start);
        for (int i=end; i>start; i--)
        {
            toRemove.add(polygon.getSegments().get(i));
        }
        polygon.getSegments().removeAll(toRemove);

        //Add new segments
        segments.add(start+1, connection1);
        segments.add(start+2, segmentToInsert);
        segments.add(start+3, connection2);
    }
}
