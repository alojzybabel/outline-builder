package simple.outliner.builder.math;

import static simple.outliner.builder.math.MathUtil.angle;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import com.kadme.test.Line;

/** The class will group lines by distance and angle margins */
public class LineGroup
{
    /** Angle margin define difference in angle which can be ignore. */
    private double angleMargin;

    /** Angle margin define difference in distance which can be ignore. */
    private double distance;

    private final List<Line> lines = new ArrayList<>();

    public LineGroup(final double angleMargin, final double distance, final Line line)
    {
        this.angleMargin = angleMargin;
        this.distance = distance;
        lines.add(line);
    }

    public List<Line> getLines()
    {
        return lines;
    }

    /**
     * Add lines only if it matches to the group and angle is almost
     * the same like other lines in the group and distance is not to big.
     * @param line the line.
     * @return true if line was add to the group or false otherwise.
     */
    public boolean add(final Line line)
    {
        boolean result = false;
        if (line != null && verify(line))
        {
            lines.add(line);
            result = true;
        }
        return result;
    }

    /**
     * Verify if line can be add to the group
     * @param line the line.
     *  */
    public boolean verify(final Line line)
    {
        if (!lines.isEmpty())
        {
            final double angle = angle(line);
            return lines.stream()
                        // Order of filtering is important, it is assumed that lines are parallel when distance is checked.
                        .filter(l -> angle(l) + angleMargin > angle && angle > angle(l)  - angleMargin)
                        .filter(l -> MathUtil.distanceFromPoint(l, line.getP1()) <= distance)
                        .findFirst()
                        .isPresent();
        }
        return true;
    }
}
