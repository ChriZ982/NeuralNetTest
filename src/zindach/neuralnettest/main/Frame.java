package zindach.neuralnettest.main;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import zindach.neuralnetlib.functions.CrossEntropyCostFunction;
import zindach.neuralnetlib.functions.SigmoidFunction;
import zindach.neuralnetlib.net.NeuralNetwork;

public class Frame extends JFrame {

    public static final int DRAW_SIZE = 700;
    public static final String FILE_ENDING = ".png";

    private BufferedImage image;
    private Graphics2D graphics;
    private NeuralNetwork nn;
    private final DrawPanel drawPanel;
    private final ButtonPanel buttonPanel;
    private final Mouse mouse;

    public Frame() {
        super("Neural Network Test");

        nn = new NeuralNetwork(new SigmoidFunction(), new CrossEntropyCostFunction(), 784, 100, 10);
        setImage(new BufferedImage(DRAW_SIZE, DRAW_SIZE, BufferedImage.TYPE_INT_ARGB_PRE));

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

//        Vector[] test = MNISTLoader.importData("data/t10k-labels-idx1-ubyte.gz");
//        Vector[] test2 = MNISTLoader.importData("data/t10k-images-idx3-ubyte.gz");
//        for (int i = 0; i < 28; i++) {
//            for (int j = 0; j < 28; j++) {
////                System.out.println(test2[0].value(i * j));
//                int grey = 255 - (int) (test2[1].value(j * 28 + i) * 255);
//                graphics.setColor(new Color(grey, grey, grey));
//                graphics.fillRect(i * 25, j * 25, 25, 25);
//            }
//            repaint();
//        }
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
