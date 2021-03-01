package simple.outliner.builder.math.second.build;

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
            return mergeLine(polygon, line);
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


    private boolean mergeLine(final Polygon2D polygon, final Line line)
    {
        List<Segment> reachableFromP1 =  filterReachableSegmentsSortedByDistance(polygon.getSegments(), line.getP1());
        List<Segment> reachableFromP2 =  filterReachableSegmentsSortedByDistance(polygon.getSegments(), line.getP2());
        //Check if at least one point of the polygon is reachable from line
        if (reachableFromP1.isEmpty() || reachableFromP2.isEmpty())
        {
            return false;
        }

        final Segment first = reachableFromP1.get(0);
        final int indexFirst = polygon.getSegments().indexOf(first);
        final int size = polygon.getSegments().size();
        final Segment right = polygon.getSegments().get((indexFirst + 1) % size);

        boolean merged = false;
        if (reachableFromP2.contains(right))
        {
            final Segment connection = new Segment(first.getX2(), first.getY2(), line.getP1().getX(), line.getP1().getY(), SegmentType.SOFT);
            final Segment rightConnection = new Segment(line.getP2().getX(), line.getP2().getY(), right.getX2(), right.getY2(), SegmentType.SOFT);
            if(!connection.intersects(rightConnection))
            {
                polygon.getSegments().remove(right);
                addSegmentToPolygon(polygon, indexFirst+1, connection);
                addSegmentToPolygon(polygon, indexFirst+2, new Segment(line));
                addSegmentToPolygon(polygon, indexFirst+3, rightConnection);
                merged = true;
            }
        }

        if (!merged)
        {
            final Segment left = polygon.getSegments().get(indexFirst - 1 < 0 ? size - 1 : indexFirst - 1);
            if(reachableFromP2.contains(left))
            {
                final Segment leftConnection = new Segment(left.getX2(), left.getY2(), line.getP2().getX(), line.getP2().getY(), SegmentType.SOFT);
                final Segment connection = new Segment(line.getP1().getX(), line.getP1().getY(), first.getX2(), first.getY2(), SegmentType.SOFT);
                if(!connection.intersects(leftConnection))
                {
                    polygon.getSegments().remove(first);
                    addSegmentToPolygon(polygon, indexFirst, leftConnection);
                    addSegmentToPolygon(polygon, indexFirst + 1, new Segment(line.getP2(), line.getP1(), SegmentType.HARD));
                    addSegmentToPolygon(polygon, indexFirst + 2, connection);
                    merged = true;
                }
            }
        }
        return merged;
    }

    private void addSegmentToPolygon(final Polygon2D polygon, final int indexFirst, final Segment segment)
    {
        final int size = polygon.getSegments().size();
        if (size > indexFirst)
        {
            polygon.getSegments().add(indexFirst , segment);
        }
        else
        {
            polygon.add(segment);
        }
    }
}
