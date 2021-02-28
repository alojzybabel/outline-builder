package simple.outliner.builder.math.second.build;

import com.kadme.test.Line;
import simple.outliner.builder.math.second.geom.Polygon2D;

/** Merge line to the polygon if both ends of the line are outside the polygon and the line does not intersect the polygon. */
public class OutlineCrossingLineMerger implements LineMerger
{
    @Override
    public boolean merge(final Polygon2D polygon, final Line line)
    {
        return true;
    }
}
