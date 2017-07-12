package zindach.neuralnettest.main;

import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JPanel;

public class DrawPanel extends JPanel {

    private final Frame frame;

    public DrawPanel(Frame frame) {
        super();
        this.frame = frame;
        setPreferredSize(new Dimension(Frame.DRAW_SIZE, Frame.DRAW_SIZE));

        addMouseListener(frame.getMouse());
        addMouseMotionListener(frame.getMouse());
    }

    @Override
    protected void paintComponent(Graphics grphcs) {
        super.paintComponent(grphcs);
        grphcs.drawImage(frame.getImage(), 0, 0, Frame.DRAW_SIZE, Frame.DRAW_SIZE, 0, 0, Frame.DRAW_SIZE, Frame.DRAW_SIZE, this);
    }
}
