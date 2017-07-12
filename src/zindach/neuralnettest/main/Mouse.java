package zindach.neuralnettest.main;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import zindach.mathlib.algebra.Vector;

public class Mouse extends MouseAdapter {

    private final Frame frame;

    public Mouse(Frame frame) {
        this.frame = frame;
    }

    private void draw(MouseEvent me) {
        Graphics2D g = frame.getGraphics2D();
        g.setColor(new Color(1f, 1f, 1f, 0.04f));
        g.drawRect((int) (me.getX() / 600.0 * 28.0) - 1, (int) (me.getY() / 600.0 * 28.0), 3, 0);
        g.drawRect((int) (me.getX() / 600.0 * 28.0), (int) (me.getY() / 600.0 * 28.0) - 1, 0, 2);
        g.setColor(Color.WHITE);
        g.drawRect((int) (me.getX() / 600.0 * 28.0), (int) (me.getY() / 600.0 * 28.0), 0, 0);
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
