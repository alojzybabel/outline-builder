package simple.outliner.builder.math.second.build;

import com.kadme.test.Line;
import simple.outliner.builder.math.second.geom.Polygon2D;

/** Merge line with all points inside the polygon. */
public class InlineLineMerger implements LineMerger
{
    @Override
    public boolean merge(final Polygon2D polygon, final Line line)
    {
        return true;
    }
}
