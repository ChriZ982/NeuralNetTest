package zindach.neuralnettest.main;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import zindach.neuralnetlib.net.NeuralNetwork;

public class Frame extends JFrame {

    public static final int DRAW_SIZE = 700;
    public static final String FILE_ENDING = ".png";

    private BufferedImage image;
    private NeuralNetwork nn;
    private final DrawPanel drawPanel;
    private final ButtonPanel buttonPanel;
    private final Mouse mouse;
    private boolean predicted = false;

    public Frame() {
        super("Neural Network Test");

        nn = new NeuralNetwork(784, 30, 10);
        image = new BufferedImage(DRAW_SIZE, DRAW_SIZE, BufferedImage.TYPE_INT_ARGB_PRE);

        setLayout(new BoxLayout(getContentPane(), BoxLayout.X_AXIS));

        mouse = new Mouse(this);

        buttonPanel = new ButtonPanel(this);
        drawPanel = new DrawPanel(this);

        add(drawPanel);
        add(buttonPanel);

        setSize(DRAW_SIZE + 300, DRAW_SIZE);
        setResizable(false);
        setVisible(true);
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void setImage(BufferedImage image) {
        this.image = image;
        repaint();
    }

    public BufferedImage getImage() {
        return image;
    }

    public Graphics2D getGraphics2D() {
        return (Graphics2D) image.getGraphics();
    }

    public ButtonPanel getButtonPanel() {
        return buttonPanel;
    }

    public void setNN(NeuralNetwork nn) {
        this.nn = nn;
    }

    public NeuralNetwork getNN() {
        return nn;
    }

    public Mouse getMouse() {
        return mouse;
    }

    public boolean isPredicted() {
        return predicted;
    }

    public void setPredicted(boolean predicted) {
        this.predicted = predicted;
        buttonPanel.getPredictButton().setEnabled(!predicted);
    }

    public static void main(String[] args) {
        new Frame();
    }
}
