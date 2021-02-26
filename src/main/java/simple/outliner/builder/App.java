package simple.outliner.builder;

import java.awt.EventQueue;

import simple.outliner.builder.render.OutlineBuilderFrame;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        EventQueue.invokeLater( () -> {
            OutlineBuilderFrame outlineBuilderFrame = new OutlineBuilderFrame();
            outlineBuilderFrame.setVisible(true);
        });
    }
}
