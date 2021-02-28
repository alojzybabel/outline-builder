package simple.outliner.builder.math.second;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import com.kadme.test.Line;
import simple.outliner.builder.LineGenerator;
import simple.outliner.builder.math.second.build.LineMerger;
import simple.outliner.builder.math.second.geom.Polygon2D;
import simple.outliner.builder.render.LineAdapter;

public class IterableOutlineBuilder extends AbstractOutlineBuilderParent
{
    /** Stores all generated lines. */
    private final List<Line> allLines;

    private final LinkedList<Line> notUsedLines;

    private final LinkedList<Line> usedLines;

    private LineGenerator generator;

    private LineMerger merger;

    private Polygon2D polygon = null;

    public IterableOutlineBuilder(final LineGenerator generator, final LineMerger merger)
    {
        this.generator = generator;
        this.merger = merger;
        allLines = generator.generate();
        notUsedLines = new LinkedList<>();
        usedLines = new LinkedList<>();
        notUsedLines.addAll(allLines);
        usedLines.add(allLines.get(0));
        usedLines.add(allLines.get(1));
        polygon = createPolygon2D(notUsedLines);
    }

    /**
     * Gets used lines wrapped by adapters used in rendering.
     * @return used lines wrapped by adapters used in rendering.
     */
    public List<LineAdapter> getUsedLines()
    {
        List<LineAdapter> lines = usedLines.stream().map(LineAdapter::new).collect(Collectors.toList());
        //TODO REMOVE!! add next line for debug purpose.
        lines.add(new LineAdapter(notUsedLines.get(0)));
        return lines;
    }

    public Polygon2D getPolygon()
    {
        return polygon;
    }

    /**
     * Merge next line to polygon.
     * @return true if line was marge or false if there is no more lines to add.
     */
    public boolean nextIteration()
    {
        final Line line = notUsedLines.pollFirst();
        if (line == null)
        {
            return false;
        }
        usedLines.add(line);
        //all line should be merged, return false if merger does not merge the line.
        return merger.merge(polygon, line);
    }
}