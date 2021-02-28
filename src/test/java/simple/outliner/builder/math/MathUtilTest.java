package simple.outliner.builder.math;

import static org.assertj.core.api.Assertions.assertThat;

import org.assertj.core.data.Offset;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.kadme.test.Line;
import com.kadme.test.Point;


public class MathUtilTest
{

    @Test( dataProvider = "getAngleData")
    public void testAngle(final Point p1, final Point p2, long result)
    {
        assertThat(MathUtil.angle(p1, p2)).isEqualTo(result);
    }

    @DataProvider
    public Object [][] getAngleData()
    {
        final Point p1 = new Point(2,2);
        final Point p2 = new Point(-2,-2);
        final Point p3 = new Point(1,1);
        final Point p4 = new Point(2, -1);
        final Point p5 = new Point(-2, 2);
        final Point p6 = new Point(-1, 1);

        return new Object[][]
           {
               {p1, p2, 45},
               {p1, p3, 45},
               {p1, p4, 90},
               {p5, p6, -45}
           };
    }

    @Test( dataProvider = "getDistanceData")
    public void testDistanceFromPoint(final Line line, final Point point, long result)
    {
        assertThat(MathUtil.distanceFromPoint(line, point)).isCloseTo(1.3, Offset.offset(5D));
    }

    @DataProvider
    public Object [][] getDistanceData()
    {
        final Point p1 = new Point(3,1);
        final Point p2 = new Point(4,3);
        final Line line = new Line(p1, p2);
        final Point p3 = new Point(3,4);

        return new Object[][]
       {
           {line, p3, 0}
       };
    }

    @Test( dataProvider = "getLengthData")
    public void testLength(final Line line, long result)
    {
        assertThat(MathUtil.length(line)).isEqualTo(result);
    }

    @DataProvider
    public Object [][] getLengthData()
    {
        final Point p1 = new Point(1,1);
        final Point p2 = new Point(5,4);
        final Line line = new Line(p1, p2);

        return new Object[][]
           {
               {line, 5}
          };
    }
}