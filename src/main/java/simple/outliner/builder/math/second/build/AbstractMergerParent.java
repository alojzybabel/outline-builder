package simple.outliner.builder.math.second.build;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import com.kadme.test.Line;
import com.kadme.test.Point;
import simple.outliner.builder.math.second.geom.Polygon2D;
import simple.outliner.builder.math.second.geom.Segment;
import simple.outliner.builder.math.second.geom.SegmentType;

/** Storing functionalities common for line mergers. */
public abstract class AbstractMergerParent
{
    /**
     * Find all segments of the polygon which can be reach from given point without crossing any lines.
     * @param segments the segments.
     * @param point the point.
     * @return all reachable segments.
     */
    protected List<Segment> filterReachableSegmentsSortedByDistance(final List<Segment> segments , final Point point)
    {
        //Create connection with segment and check if not cross any polygon segments.
        final List<Segment> result = new LinkedList<>();
        for (final Segment segment : segments)
        {
            final Segment connection = new Segment(point.getX(), point.getY(), segment.getX2(), segment.getY2(), SegmentType.SOFT);
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
            if (s1.distanceFromPoint(point.getX(), point.getY()) < s2.distanceFromPoint(point.getX(), point.getY())) return -1;
            else if(s1.distanceFromPoint(point.getX(), point.getY()) > s2.distanceFromPoint(point.getX(), point.getY())) return 1;
            return 0;
        });

        return result;
    }

    /**
     * Check if segment should be ignored.
     * @param segments the all segments.
     * @param s1 the first segment.
     * @param s2 the second segment.
     * @return true if segments intersection check should be avoided.
     */
    protected boolean checkIfSegmentShouldBeIgnored(final List<Segment> segments, final Segment s1, final Segment s2)
    {

        if (s1 == s2)
        {
            return true;
        }

        final int index = segments.indexOf(s1);
        final Segment nextToTheSegmentToCheck = index == segments.size() - 1 ? segments.get(0) : segments.get(index + 1);
        if (nextToTheSegmentToCheck == s2)
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