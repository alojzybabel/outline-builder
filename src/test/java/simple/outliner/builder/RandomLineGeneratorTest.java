package simple.outliner.builder;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.testng.annotations.Test;

import com.kadme.test.Line;

public class RandomLineGeneratorTest
{

    @Test
    public void testGenerate()
    {
        final RandomLineGenerator generator = new RandomLineGenerator(2048, 1536, 100);
        final List<Line> line =  generator.generate();

        assertThat(line.size()).isEqualTo(100);
        line.forEach(l -> {
               assertThat(l.getP1().getX()).isLessThan(2049);
               assertThat(l.getP2().getX()).isLessThan(2049);
               assertThat(l.getP1().getY()).isLessThan(1537);
               assertThat(l.getP2().getY()).isLessThan(1537);
        });
    }
}