package simple.outliner.builder.math.second;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.kadme.test.Line;
import com.kadme.test.OutlineBuilder;
import com.kadme.test.Point;
import com.kadme.test.Polygon;
import simple.outliner.builder.math.second.build.LineMerger;
import simple.outliner.builder.math.second.geom.Polygon2D;

public class OutlineBuilderImp extends AbstractOutlineBuilderParent implements OutlineBuilder
{
    /** The line merger. Create polygon from lines. */
    private LineMerger merger;

    public OutlineBuilderImp(final LineMerger merger)
    {
        this.merger = merger;
    }

    @Override
    public Polygon buildOutline(final Set<Line> lines)
    {
        if (lines == null || lines.size() < 2)
        {
            throw new IllegalArgumentException("At least two lines are needed");
        }

        //Copy the input.
        final LinkedList<Line> linkedLines = new LinkedList<>(lines);
        final Polygon2D polygon2D = createPolygon2D(linkedLines);

        for (final Line line : linkedLines)
        {
            merger.merge(polygon2D, line);
        }

        final List<Point> points = polygon2D.getSegments()
                                            .stream()
                                            .map(s -> new Point(s.getX1(), s.getY1()))
                                            .collect(Collectors.toList());
        return new Polygon(points);
    }
}