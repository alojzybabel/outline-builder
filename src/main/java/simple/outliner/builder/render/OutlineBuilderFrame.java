package simple.outliner.builder.render;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.HeadlessException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class OutlineBuilderFrame extends JFrame
{
    private final JButton generateBtn;
    private final JButton nextBtn;
    private final JButton resetBtn;
    private final OutlineBuilderPanel outlineBuilderPanel;
    private final SecondOutlineBuilderPanel secondOutlineBuilderPanel;
    private final JPanel buttonPanel;

    public OutlineBuilderFrame() throws HeadlessException
    {
        super("Outline builder");
        setSize(1280, 720);
        nextBtn = new JButton("Next step!");
        generateBtn = new JButton("Generate Polygon");
        generateBtn.setPreferredSize(new Dimension(400, 75));
        resetBtn = new JButton("Reset the Polygon");
        resetBtn.setPreferredSize(new Dimension(400, 75));
        buttonPanel = new JPanel(new BorderLayout());

        outlineBuilderPanel = new OutlineBuilderPanel();
        secondOutlineBuilderPanel = new SecondOutlineBuilderPanel();

        //getContentPane().add(outlineBuilderPanel);
        getContentPane().add(secondOutlineBuilderPanel);
        getContentPane().add(buttonPanel, BorderLayout.PAGE_END);
        buttonPanel.add(nextBtn, BorderLayout.CENTER);
        buttonPanel.add(generateBtn, BorderLayout.LINE_START);
        buttonPanel.add(resetBtn, BorderLayout.LINE_END);
        nextBtn.addActionListener(secondOutlineBuilderPanel);
        generateBtn.addActionListener(e -> secondOutlineBuilderPanel.drawAll());
        resetBtn.addActionListener(e -> secondOutlineBuilderPanel.reset());
    }
}
