package simple.outliner.builder.render;

import com.kadme.test.Point;

public class PointAdapter
{
    final private Point point;

    public PointAdapter(final Point point)
    {
        this.point = point;
    }

    public int getX()
    {
        return Math.toIntExact(Math.round(point.getX()));
    }

    public int getY()
    {
        return Math.toIntExact(Math.round(point.getY()));
    }
}
