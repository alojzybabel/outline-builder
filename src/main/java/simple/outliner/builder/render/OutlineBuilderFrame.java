package simple.outliner.builder.render;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.HeadlessException;

import javax.swing.JButton;
import javax.swing.JFrame;

public class OutlineBuilderFrame extends JFrame
{
    private final JButton generateBtn;
    private final OutlineBuilderPanel outlineBuilderPanel;

    public OutlineBuilderFrame() throws HeadlessException
    {
        super("Outline builder");
        setSize(3840, 2160);
        generateBtn = new JButton("generate");
        outlineBuilderPanel = new OutlineBuilderPanel();
        getContentPane().add(outlineBuilderPanel);
        getContentPane().add(generateBtn, BorderLayout.PAGE_END);
        generateBtn.setPreferredSize(new Dimension(40, 100));
        generateBtn.addActionListener(e -> outlineBuilderPanel.updateUI());
    }
}
