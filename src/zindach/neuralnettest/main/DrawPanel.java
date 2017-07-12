package zindach.neuralnettest.main;

import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JPanel;

public class DrawPanel extends JPanel {

    private final Frame frame;

    public DrawPanel(Frame frame) {
        super();
        this.frame = frame;
        setPreferredSize(new Dimension(600, 600));

        addMouseListener(frame.getMouse());
        addMouseMotionListener(frame.getMouse());
    }

    @Override
    protected void paintComponent(Graphics grphcs) {
        super.paintComponent(grphcs);
        grphcs.drawImage(frame.getImage(), 0, 0, 600, 600, 0, 0, 28, 28, this);
    }
}
