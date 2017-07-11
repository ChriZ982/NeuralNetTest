package zindach.neuralnettest.main;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import zindach.neuralnetlib.neuralnet.NeuralNetwork;
import zindach.neuralnetlib.neuralnet.NeuronFunction;

public class Frame extends JFrame {

    public static final String FILE_ENDING = ".png";

    private final BufferedImage image;
    private final Graphics2D graphics;
    private NeuralNetwork nn;

    public Frame() {
        super("Neural Network Test");

        nn = new NeuralNetwork(NeuronFunction.SIGMOID, 784, 15, 10);
        image = new BufferedImage(28, 28, BufferedImage.TYPE_INT_ARGB_PRE);
        graphics = (Graphics2D) image.getGraphics();

        setLayout(new BoxLayout(getContentPane(), BoxLayout.X_AXIS));

        ButtonPanel buttonPanel = new ButtonPanel(this, graphics);
        DrawPanel drawPanel = new DrawPanel(image, this, graphics, buttonPanel);

        add(drawPanel);
        add(buttonPanel);

        setSize(1000, 600);
        setResizable(false);
        setVisible(true);
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public BufferedImage getImage() {
        return image;
    }

    public NeuralNetwork getNN() {
        return nn;
    }

    public void setNN(NeuralNetwork nn) {
        this.nn = nn;
    }

    public static void main(String[] args) {
        new Frame();
    }
}
