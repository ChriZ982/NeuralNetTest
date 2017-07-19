/*
* NeuralNetTest by ChriZ98 is licensed under a
* Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License
* https://creativecommons.org/licenses/by-nc-sa/4.0/
 */
package zindach.neuralnettest.main;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Mouse extends MouseAdapter {

    private static final int BRUSH_RADIUS = 30;
    private final Frame frame;
    private Thread autoPredict;

    public Mouse(Frame frame) {
        this.frame = frame;
    }

    private void startAutoPredict() {
        if (autoPredict != null && !autoPredict.isInterrupted()) {
            autoPredict.interrupt();
        }
        autoPredict = new Thread(() -> {
            try {
                Thread.sleep(2000);
                if (!frame.isPredicted()) {
                    frame.getButtonPanel().predictButtonActionPerformed();
                }
            } catch (InterruptedException ex) {
            }
        });
        autoPredict.start();
    }

    private void draw(MouseEvent me) {
        if (frame.isPredicted()) {
            frame.getButtonPanel().resetButtonActionPerformed();
        }
        Graphics2D g = frame.getGraphics2D();
        g.setColor(Color.BLACK);
        g.fillOval(me.getX() - BRUSH_RADIUS, me.getY() - BRUSH_RADIUS, 2 * BRUSH_RADIUS, 2 * BRUSH_RADIUS);
        frame.repaint();
        startAutoPredict();
    }

    @Override
    public void mouseClicked(MouseEvent me) {
        draw(me);
    }

    @Override
    public void mouseDragged(MouseEvent me) {
        draw(me);
    }
}
