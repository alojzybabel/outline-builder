package simple.outliner.builder.math.second.build;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import com.kadme.test.Line;
import simple.outliner.builder.math.second.geom.Polygon2D;
import simple.outliner.builder.math.second.geom.Segment;
import simple.outliner.builder.math.second.geom.SegmentType;

public abstract class AbstractOutlineParent
{
    /**
     * Find all segments of polygon which end can be reach from given point without crossing any lines.
     * @param segments the segments.
     * @param x the x coordinate of the point.
     * @param y the y coordinate of the point.
     * @return all reachable segments.
     */
    protected List<Segment> filterReachableSegmentsSortedByDistance(final List<Segment> segments , final double x, final double y)
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

    protected boolean checkIfSegmentShouldBeIgnored(final List<Segment> segments, final Segment segmentToCheck, final Segment segmentWhichCanCross)
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

    /**
     *  Check if the line intersects the polygon.
     * @param polygon the polygon.
     * @param line the line.
     * @return true if the line intersects the polgon.
     */
    protected boolean isTheLineIntersectingThePolygon(final Polygon2D polygon, final Line line)
    {
        final Segment segment = new Segment(line);
        return polygon.getSegments().stream().filter(segment::intersects).findFirst().isPresent();
    }
}
