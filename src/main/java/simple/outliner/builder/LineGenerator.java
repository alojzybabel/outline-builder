package simple.outliner.builder;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.StreamSupport;

import com.kadme.test.Line;
import com.kadme.test.Point;

/** Generates set of lines */
public class LineGenerator
{
    /** Max x value for generator. */
    final int resolutionX;

    /** Max y value for generator. */
    final int resolutionY;

    /** Number of lines to generate */
    final int amount;

    public LineGenerator(final int resolutionX, final int resolutionY, final int amount)
    {
        this.resolutionX = resolutionX;
        this.resolutionY = resolutionY;
        this.amount = amount;
    }

    public List<Line> generate()
    {
        return IntStream.rangeClosed(1, amount).mapToObj(i -> randomLine()).collect(Collectors.toList());
    }

    private Line randomLine()
    {
        return new Line(new Point(random(resolutionX), random(resolutionY)),
                        new Point(random(resolutionX), random(resolutionY)));
    }

    private double random(final int max)
    {
        return max * Math.random();
    }
}