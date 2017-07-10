package zindach.neuralnettest.main;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import zindach.mathlib.algebra.Vector;

public class Mouse extends MouseAdapter {

    private final Frame frame;
    private final Graphics2D graphics;
    private final ButtonPanel buttonPanel;

    public Mouse(Frame frame, Graphics2D graphics, ButtonPanel buttonPanel) {
        this.frame = frame;
        this.graphics = graphics;
        this.buttonPanel = buttonPanel;
    }

    private void draw(MouseEvent me) {
        graphics.setColor(new Color(0f, 0f, 0f, 0.02f));
        graphics.drawRect((int) (me.getX() / 600.0 * 28.0) - 1, (int) (me.getY() / 600.0 * 28.0), 3, 0);
        graphics.drawRect((int) (me.getX() / 600.0 * 28.0), (int) (me.getY() / 600.0 * 28.0) - 1, 0, 2);
        graphics.setColor(Color.BLACK);
        graphics.drawRect((int) (me.getX() / 600.0 * 28.0), (int) (me.getY() / 600.0 * 28.0), 0, 0);
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
        buttonPanel.setLabels(frame.getNN().calculate(new Vector(buttonPanel.calcInput(frame.getImage()))).values());
    }
}
