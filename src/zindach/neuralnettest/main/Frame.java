package zindach.neuralnettest.main;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import zindach.neuralnetlib.functions.CrossEntropyCostFunction;
import zindach.neuralnetlib.functions.SigmoidFunction;
import zindach.neuralnetlib.net.NeuralNetwork;

public class Frame extends JFrame {

    public static final String FILE_ENDING = ".png";

    private BufferedImage image;
    private Graphics2D graphics;
    private NeuralNetwork nn;
    private final DrawPanel drawPanel;
    private final ButtonPanel buttonPanel;
    private final Mouse mouse;

    public Frame() {
        super("Neural Network Test");

        nn = new NeuralNetwork(new SigmoidFunction(), new CrossEntropyCostFunction(), 784, 30, 10);
        setImage(new BufferedImage(28, 28, BufferedImage.TYPE_INT_ARGB_PRE));

        setLayout(new BoxLayout(getContentPane(), BoxLayout.X_AXIS));

        mouse = new Mouse(this);

        buttonPanel = new ButtonPanel(this);
        drawPanel = new DrawPanel(this);

        add(drawPanel);
        add(buttonPanel);

        setSize(1000, 600);
        setResizable(false);
        setVisible(true);
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void setImage(BufferedImage image) {
        this.image = image;
        this.graphics = (Graphics2D) image.createGraphics();

        repaint();
    }

    public BufferedImage getImage() {
        return image;
    }

    public Graphics2D getGraphics2D() {
        return graphics;
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

    public static void main(String[] args) {
        new Frame();
    }
}
