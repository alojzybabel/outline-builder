package simple.outliner.builder.math.second.build;

import static simple.outliner.builder.math.MathUtil.isTheLineIntersectingThePolygon;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import com.kadme.test.Line;
import simple.outliner.builder.math.second.geom.Polygon2D;
import simple.outliner.builder.math.second.geom.Segment;
import simple.outliner.builder.math.second.geom.SegmentType;

/** For test purpose. */
public class TempDebugOutlineConnectionPredictor
{

    public List<Segment> predictConnection(final Polygon2D polygon, final Line line)
    {
        if (isMyJob(polygon, line))
        {
            List<Segment> reachableFromP1 =  filterReachableSegmentsSortedByDistance(polygon.getSegments(), line.getP1().getX(), line.getP1().getY());
            List<Segment> reachableFromP2 =  filterReachableSegmentsSortedByDistance(polygon.getSegments(), line.getP2().getX(), line.getP2().getY());
            final Segment p1 = reachableFromP1.get(0);
            final Segment p2 = reachableFromP2.stream().filter( s -> s != p1).findFirst().get();
            final int index1 = polygon.getSegments().indexOf(p1);
            final int index2 = polygon.getSegments().indexOf(p2);
            if (index1 < index2)
            {
                final Segment lineSegment = new Segment(line);
                return mergeSegment(index1, index2, lineSegment, polygon);
            }
            else if (index1 > index2)
            {
                final Segment lineSegment = new Segment(line.getP2().getX(), line.getP2().getY(),
                                                        line.getP1().getX(), line.getP1().getY(), SegmentType.HARD);
                return mergeSegment(index2, index1, lineSegment, polygon);
            }
            else
            {
                throw new IllegalStateException("The ends of new segment cannot connect to the same point");
            }

        }
        return new ArrayList<>();
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
     * Find all segments of polygon which end can be reach from given point without crossing any lines.
     * @param segments the segments.
     * @param x the x coordinate of the point.
     * @param y the y coordinate of the point.
     * @return all reachable segments.
     */
    private List<Segment> filterReachableSegmentsSortedByDistance(final List<Segment> segments , final double x, final double y)
    {
        //Create connection with segment and check if not cross any polygon segments.
        final List<Segment> result = new LinkedList<>();
        for (final Segment segment : segments)
        {
            final Segment connection = new Segment(x, y, segment.getX2(), segment.getY2(), SegmentType.SOFT);
            boolean isCrossing = false;
            for (final Segment s: segments)
            {
                //Ignore segments which contains the end of the connection.
                if (checkIfSegmentShouldBeIgnored(segments, segment, s))
                {
                    continue;
                }
                if(connection.intersects(s))
                {
                    isCrossing = true;
                    break;
                }
            }
            if (!isCrossing)
            {
                result.add(segment);
            }
        }

        Collections.sort(result, (Segment s1, Segment s2) ->
        {
            if (s1.distanceFromPoint(x, y) < s2.distanceFromPoint(x, y)) return -1;
            else if(s1.distanceFromPoint(x, y) > s2.distanceFromPoint(x, y)) return 1;
            return 0;
        });

        return result;
    }

    private boolean checkIfSegmentShouldBeIgnored(final List<Segment> segments, final Segment segmentToCheck, final Segment segmentWhichCanCross)
    {

        if (segmentToCheck == segmentWhichCanCross)
        {
            return true;
        }

        final int index = segments.indexOf(segmentToCheck);
        final Segment nextToTheSegmentToCheck = index == segments.size() - 1 ? segments.get(0) : segments.get(index + 1);
        if (nextToTheSegmentToCheck == segmentWhichCanCross)
        {
            return true;
        }
        return false;
    }


    private List<Segment> mergeSegment(final int start, final int end, final Segment segment, final Polygon2D polygon)
    {
        final LinkedList<Segment> segments = polygon.getSegments();
        final Segment first = polygon.getSegments().get(start);
        final Segment last = polygon.getSegments().get(end);
        //connection from first to p1 of segment
        final Segment connection1 = new Segment(first.getX2(), first.getY2(), segment.getX1(), segment.getY1(), SegmentType.SOFT);
        final Segment connection2 = new Segment(segment.getX2(), segment.getY2(), last.getX2(), last.getY2(), SegmentType.SOFT);

        final List<Segment> list = new ArrayList<>(2);
        list.add(connection1);
        list.add(connection2);
        return list;
    }

}