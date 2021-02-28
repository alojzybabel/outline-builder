package simple.outliner.builder.math.second.build;

import java.util.Arrays;

import com.kadme.test.Line;
import simple.outliner.builder.math.second.geom.Polygon2D;

/** Line merger for combining other merger together, one to rule them all. */
public class ComposedLineMerger implements LineMerger
{
    final LineMerger [] mergers =
                    {
                        new OutlineLineMerger(),
                        new OutlineCrossingLineMerger(),
                        new InOutLineMerger(),
                        new InlineLineMerger()
                    };

    @Override
    public boolean merge(final Polygon2D polygon, final Line line)
    {
        return Arrays.stream(mergers).filter(m -> m.merge(polygon, line)).findFirst().isPresent();
    }
}
