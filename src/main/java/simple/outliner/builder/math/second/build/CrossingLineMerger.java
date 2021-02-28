package simple.outliner.builder.math.second.build;

import com.kadme.test.Line;
import simple.outliner.builder.math.second.geom.Polygon2D;

/** Merge line to the polygon if both ends of the line are outside the polygon and the line does not intersect the polygon. */
public class CrossingLineMerger extends AbstractPointMerger implements LineMerger
{
    @Override
    public boolean merge(final Polygon2D polygon, final Line line)
    {
        if (isMyJob(polygon, line))
        {
            return mergePoint(line.getP1(), polygon) && mergePoint(line.getP2(), polygon);
        }
        return false;
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
        return !polygon.contains(line.getP1().getX(), line.getP1().getY())
               && !polygon.contains(line.getP2().getX(), line.getP2().getY())
               && isTheLineIntersectingThePolygon(polygon, line);
    }
}
