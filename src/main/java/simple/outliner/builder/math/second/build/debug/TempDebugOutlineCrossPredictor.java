package simple.outliner.builder.math.second.build.debug;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import com.kadme.test.Line;
import com.kadme.test.Point;
import simple.outliner.builder.math.second.geom.Polygon2D;
import simple.outliner.builder.math.second.geom.Segment;
import simple.outliner.builder.math.second.geom.SegmentType;

public class TempDebugOutlineCrossPredictor
{
    public List<Segment> predictConnection(final Polygon2D polygon, final Line line)
    {
        if (isMyJob(polygon, line))
        {
            final List<Segment> result = new ArrayList<>();
            List<Segment> reachableFromP1 =  filterReachableSegmentsSortedByDistance(polygon.getSegments(), line.getP1().getX(), line.getP1().getY());
            final Segment p11 = reachableFromP1.get(0);
            final Segment p12 = reachableFromP1.get(1);
            final int index1 = polygon.getSegments().indexOf(p11);
            final int index2 = polygon.getSegments().indexOf(p12);
            result.addAll(mergePoint(index1, index2, line.getP1(), polygon));


            List<Segment> reachableFromP2 =  filterReachableSegmentsSortedByDistance(polygon.getSegments(), line.getP2().getX(), line.getP2().getY());
            final Segment p21 = reachableFromP2.get(0);
            final Segment p22 = reachableFromP2.get(1);
            final int index3 = polygon.getSegments().indexOf(p21);
            final int index4 = polygon.getSegments().indexOf(p22);
            result.addAll(mergePoint(index3, index4, line.getP2(), polygon));
            return result;
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
               && isTheLineIntersectingThePolygon(polygon, line);
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

    private List<Segment> mergePoint(int start, int end, final Point point, final Polygon2D polygon)
    {
        if (start == end)
        {
            throw new IllegalStateException("Start and end index in outline crossing merger can not be the same.");
        }

        final LinkedList<Segment> segments = polygon.getSegments();
        final Segment first = polygon.getSegments().get(start);
        final Segment last = polygon.getSegments().get(end);
        //connection from first to p1 of segment
        final Segment connection1 = new Segment(first.getX2(), first.getY2(), point.getX(), point.getY(), SegmentType.SOFT);
        final Segment connection2 = new Segment(point.getX(), point.getY(), last.getX2(), last.getY2(), SegmentType.SOFT);

        final List<Segment> connections = new ArrayList<>(2);
        connections.add(connection1);
        connections.add(connection2);
        return connections;
    }

    /**
     *  Check if the line intersects the polygon.
     * @param polygon the polygon.
     * @param line the line.
     * @return true if the line intersects the polgon.
     */
    protected static boolean isTheLineIntersectingThePolygon(final Polygon2D polygon, final Line line)
    {
        final Segment segment = new Segment(line);
        return polygon.getSegments().stream().filter(segment::intersects).findFirst().isPresent();
    }
}
