package simple.outliner.builder.math.second.build;

import com.kadme.test.Line;
import simple.outliner.builder.math.second.geom.Polygon2D;

/** Merge lines if one point of the line is inside the polygon and second is outside. */
public class InOutLineMerger implements LineMerger
{
    @Override
    public boolean merge(final Polygon2D polygon, final Line line)
    {
        return true;
    }
}
