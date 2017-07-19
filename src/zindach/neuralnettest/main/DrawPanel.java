/*
* NeuralNetTest by ChriZ98 is licensed under a
* Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License
* https://creativecommons.org/licenses/by-nc-sa/4.0/
 */
package zindach.neuralnettest.main;

import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JPanel;

/**
 * The panel where drawing is performed.
 *
 * @author ChriZ98
 */
public class DrawPanel extends JPanel {

    private final Frame frame;

    /**
     * Initializes drawing space.
     *
     * @param frame parent frame
     */
    public DrawPanel(Frame frame) {
        super();
        this.frame = frame;
        setPreferredSize(new Dimension(Frame.DRAW_SIZE, Frame.DRAW_SIZE));

        addMouseListener(frame.getMouse());
        addMouseMotionListener(frame.getMouse());
    }

    /**
     * Paints the panel on screen.
     *
     * @param grphcs graphics to paint at
     */
    @Override
    protected void paintComponent(Graphics grphcs) {
        super.paintComponent(grphcs);
        grphcs.drawImage(frame.getImage(), 0, 0, Frame.DRAW_SIZE, Frame.DRAW_SIZE, this);
    }
}
