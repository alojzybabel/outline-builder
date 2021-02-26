package simple.outliner.builder.math;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.kadme.test.Line;

/** Class for managing the lines groups. */
public class LineGroupContainer
{
    /** Angle margin define difference in angle which can be ignore. */
    private double angleMargin;

    /** Angle margin define difference in distance which can be ignore. */
    private double distance;

    private final List<LineGroup> groups = new ArrayList<>();

    /**
     * Constructor.
     * @param angleMargin the difference in angle that will be ignored during angle comparison.
     * @param distance the maximum acceptable distance.
     */
    public LineGroupContainer(final double angleMargin, final double distance)
    {
        this.angleMargin = angleMargin;
        this.distance = distance;
    }

    /**
     * Add line to matching group or create new group for the line
     * @param line the line that should be added to line group.
     */
    public void add(final Line line)
    {
        final Optional<LineGroup> lineGroup = groups.stream().filter(g -> g.add(line)).findFirst();
        if(!lineGroup.isPresent())
        {
            //Create new group for a line which does not match to any.
            final LineGroup group = new LineGroup(angleMargin, distance, line);
            groups.add(group);
        }
    }

    public List<LineGroup> getGroups()
    {
        return groups;
    }
}
