package zindach.neuralnettest.main;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;

public class DrawPanel extends JPanel {

    private final BufferedImage image;

    public DrawPanel(BufferedImage image, Frame frame, Graphics2D graphics, ButtonPanel buttonPanel) {
        super();
        this.image = image;
        setPreferredSize(new Dimension(600, 600));

        Mouse mo = new Mouse(frame, graphics, buttonPanel);
        addMouseListener(mo);
        addMouseMotionListener(mo);
    }

    @Override
    protected void paintComponent(Graphics grphcs) {
        super.paintComponent(grphcs);
        grphcs.drawImage(image, 0, 0, 600, 600, 0, 0, 28, 28, this);
    }
}
