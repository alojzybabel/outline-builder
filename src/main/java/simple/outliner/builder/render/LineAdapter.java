package simple.outliner.builder.render;

import com.kadme.test.Line;

public class LineAdapter
{
    final private Line line;

    public LineAdapter(final Line line)
    {
        this.line = line;
    }

    public PointAdapter getP1()
    {
        return new PointAdapter(line.getP1());
    }

    public PointAdapter getP2()
    {
        return new PointAdapter(line.getP2());
    }
}
