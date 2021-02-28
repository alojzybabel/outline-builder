package simple.outliner.builder.math.second.build;

import com.kadme.test.Line;
import simple.outliner.builder.math.second.geom.Polygon2D;

/** Implement strategy of merging line into polygon. */
public interface LineMerger
{
    /**
     * Merge line to polygon.
     * @param polygon the polygon.
     * @param line the line.
     * @return true if merger do the job.
     */
    boolean merge(final Polygon2D polygon, final Line line);
}
