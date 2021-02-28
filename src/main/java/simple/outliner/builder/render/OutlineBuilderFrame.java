package simple.outliner.builder.render;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;

public class OutlineBuilderFrame extends JFrame
{
    private final JButton generateBtn;
    private final OutlineBuilderPanel outlineBuilderPanel;
    private final SecondOutlineBuilderPanel secondOutlineBuilderPanel;

    public OutlineBuilderFrame() throws HeadlessException
    {
        super("Outline builder");
        setSize(3840, 2160);
        generateBtn = new JButton("generate");
        outlineBuilderPanel = new OutlineBuilderPanel();
        secondOutlineBuilderPanel = new SecondOutlineBuilderPanel();
        //getContentPane().add(outlineBuilderPanel);
        getContentPane().add(secondOutlineBuilderPanel);
        getContentPane().add(generateBtn, BorderLayout.PAGE_END);
        generateBtn.setPreferredSize(new Dimension(40, 100));
        generateBtn.addActionListener(secondOutlineBuilderPanel);
    }
}
