package simple.outliner.builder.math.second;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import com.kadme.test.Line;
import simple.outliner.builder.LineGenerator;
import simple.outliner.builder.math.second.build.LineMerger;
import simple.outliner.builder.math.second.build.TempDebugOutlineConnectionPredictor;
import simple.outliner.builder.math.second.geom.Polygon2D;
import simple.outliner.builder.math.second.geom.Segment;
import simple.outliner.builder.math.second.geom.SegmentType;
import simple.outliner.builder.render.LineAdapter;

public class OutlinerBuilder
{
    /** Stores all generated lines. */
    private final List<Line> allLines;

    private final LinkedList<Line> notUsedLines;

    private final LinkedList<Line> usedLines;

    private LineGenerator generator;

    private LineMerger merger;

    private Polygon2D polygon = null;

    public OutlinerBuilder(final LineGenerator generator, final LineMerger merger)
    {
        this.generator = generator;
        this.merger = merger;
        allLines = generator.generate();
        notUsedLines = new LinkedList<>();
        usedLines = new LinkedList<>();
        notUsedLines.addAll(allLines);
        initializePolygon();
    }

    private void initializePolygon()
    {
        final LinkedList<Segment> initialSegments = new LinkedList<>();
        //find first two lines which not cross to initialise the polygon.
        final Line first = notUsedLines.pollFirst();
        usedLines.add(first);
        Line matching = null;
        final Segment firstSegment = new Segment(first);
        for (final Line line : notUsedLines)
        {
            final Segment segment = new Segment(line);
            if (!segment.intersects(firstSegment))
            {
                matching = line;
                break;
            }
        }

        if (matching == null)
        {
            //TODO: Not crossing lines was not found, this can happen if a small number of lines was generated, fix this later.
            throw new IllegalStateException();
        }
        notUsedLines.remove(matching);
        usedLines.add(matching);
        Segment matchingSegment = new Segment(matching);

        //Create segments which will connect first two lines but not cross;
        Segment connection1 = new Segment(firstSegment.getX2(), firstSegment.getY2(),
                                          matchingSegment.getX1(), matchingSegment.getY1(), SegmentType.SOFT);
        Segment connection2 = new Segment(matchingSegment.getX2(), matchingSegment.getY2(),
                                          firstSegment.getX1(), firstSegment.getY1(), SegmentType.SOFT);

        if (connection1.intersects(connection2))
        {
            //But the order of points in matching segment needs to be changed.
            matchingSegment = new Segment(matchingSegment.getX2(), matchingSegment.getY2(),
                                          matchingSegment.getX1(), matchingSegment.getY1(), SegmentType.HARD);

            //Create segments which will connect first two lines but not cross;
            connection1 = new Segment(firstSegment.getX2(), firstSegment.getY2(),
                                              matchingSegment.getX1(), matchingSegment.getY1(), SegmentType.SOFT);
            connection2 = new Segment(matchingSegment.getX2(), matchingSegment.getY2(),
                                              firstSegment.getX1(), firstSegment.getY1(), SegmentType.SOFT);
        }
        initialSegments.add(firstSegment);
        initialSegments.add(connection1);
        initialSegments.add(matchingSegment);
        initialSegments.add(connection2);
        polygon = new Polygon2D(initialSegments);
    }

    /**
     * Gets used lines wrapped by adapters used in rendering.
     * @return used lines wrapped by adapters used in rendering.
     */
    public List<LineAdapter> getUsedLines()
    {
        //return usedLines.stream().map(LineAdapter::new).collect(Collectors.toList());
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

    public List<Segment> getTempConnections()
    {
        //TODO Remove
        final TempDebugOutlineConnectionPredictor merger = new TempDebugOutlineConnectionPredictor();
        return merger.predictConnection(polygon, notUsedLines.get(0));
    }
}