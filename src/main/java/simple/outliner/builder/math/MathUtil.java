package simple.outliner.builder.math;

import com.kadme.test.Line;
import com.kadme.test.Point;
import simple.outliner.builder.math.second.geom.Polygon2D;
import simple.outliner.builder.math.second.geom.Segment;

public final class MathUtil
{
    private MathUtil()
    {
    }

    /**
     * Count angle of the line defined by points
     * @param p1 the first point of the line
     * @param p2 the second point of the line.
     * @return the line angle.
     */
    public static double angle(final Point p1, final Point p2)
    {
        if (p1.getX() - p2.getX() == 0)
        {
            return 90;
        }
        final double gradient = gradient(p1, p2);
        return Math.toDegrees(Math.atan(gradient));
    }

    /**
     * Count angle of the line defined by points
     * @param line the line.
     * @return the line angle.
     */
    public static double angle(final Line line)
    {
        return angle(line.getP1(), line.getP2());
    }

    /**.
     * Count straight line gradient.
     * @param p1 the point one.
     * @param p2 the point two.
     * @return the result straight line gradient.
     */
    public static double gradient(final Point p1, final Point p2)
    {
        return (p2.getY() - p1.getY()) / (p2.getX() - p1.getX());
    }

    /**
     * Count point distance from the line.
     * @param line the line.
     * @param point the point.
     * @return the point distance from the line.
     */
    public static double distanceFromPoint(final Line line, final Point point)
    {
        //Ax + By + C = 0, (y-y1)(x2-x1) - (y2-y1)(x-x1)=0
        final double A = -1 * (line.getP2().getY() - line.getP1().getY());
        final double B =  (line.getP2().getX() - line.getP1().getX());
        final double C = (-1 * line.getP1().getY() * B) + (-1 * line.getP1().getX() * A);

        // |Ax0 + By0 + C| / sqrt(A*A + B*B)
        final double x0 = point.getX();
        final double y0 = point.getY();
        final double distance = Math.abs(A*x0 + B*y0 + C) / Math.hypot(A, B);
        return distance;
    }

    /**
     * Count the line length.
     * @param line the line.
     * @return the line length.
     */
    public static double length(final Line line)
    {
        final double deltaX = line.getP2().getX() - line.getP1().getX();
        final double deltaY = line.getP2().getY() - line.getP1().getY();
        return Math.hypot(deltaX, deltaY);
    }

    /**
     *  Check if the line intersects the polygon.
     * @param polygon the polygon.
     * @param line the line.
     * @return true if the line intersects the polgon.
     */
    public static boolean isTheLineIntersectingThePolygon(final Polygon2D polygon, final Line line)
    {
        final Segment segment = new Segment(line);
        return polygon.getSegments().stream().filter(segment::intersects).findFirst().isPresent();
    }
}
