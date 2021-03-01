package simple.outliner.builder.math.second.build;

import com.kadme.test.Line;
import com.kadme.test.Point;
import simple.outliner.builder.math.second.geom.Polygon2D;

/** Merge lines if one point of the line is inside the polygon and second is outside. */
public class InOutLineMerger extends AbstractPointMerger implements LineMerger
{
    @Override
    public boolean merge(final Polygon2D polygon, final Line line)
    {
        if (isMyJob(polygon, line))
        {
            final Point outlinePoint = polygon.contains(line.getP1()) ? line.getP2() : line.getP1();
            return mergeOutsidePoint(outlinePoint, polygon, line);
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
        return (!polygon.contains(line.getP1())
               && polygon.contains(line.getP2()))
            || (polygon.contains(line.getP1())
               && !polygon.contains(line.getP2()));
    }
}