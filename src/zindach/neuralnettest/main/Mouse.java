package zindach.neuralnettest.main;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import zindach.mathlib.algebra.Vector;

public class Mouse extends MouseAdapter {

    private static final int BRUSH_RADIUS = 30;
    private final Frame frame;

    public Mouse(Frame frame) {
        this.frame = frame;
    }

    private void draw(MouseEvent me) {
        Graphics2D g = frame.getGraphics2D();
        g.setColor(Color.BLACK);
        g.fillOval(me.getX() - BRUSH_RADIUS, me.getY() - BRUSH_RADIUS, 2 * BRUSH_RADIUS, 2 * BRUSH_RADIUS);
        frame.repaint();
    }

    @Override
    public void mouseClicked(MouseEvent me) {
        draw(me);
    }

    @Override
    public void mouseDragged(MouseEvent me) {
        draw(me);
    }

    @Override
    public void mouseReleased(MouseEvent me) {
        frame.getButtonPanel().setLabels(frame.getNN().calculate(new Vector(frame.getButtonPanel().calcInput(frame.getImage()))).values());
    }
}
