/*
* NeuralNetTest by ChriZ98 is licensed under a
* Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License
* https://creativecommons.org/licenses/by-nc-sa/4.0/
 */
package zindach.neuralnettest.main;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import zindach.neuralnetlib.net.NeuralNetwork;

/**
 * Main and window class of the project. Responsible for initializing the gui
 * and performing actions on the neural network.
 *
 * @author ChriZ98
 */
public class Frame extends JFrame {

    /**
     * Size of the panel used for drawing.
     */
    public static final int DRAW_SIZE = 700;

    private BufferedImage image;
    private NeuralNetwork net;
    private final DrawPanel drawPanel;
    private final ButtonPanel buttonPanel;
    private final Mouse mouse;
    private boolean predicted = false;

    /**
     * Initializes all needed components for digit recognition.
     */
    public Frame() {
        super("Neural Network Test");

        net = new NeuralNetwork(784, 30, 10);
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

    /**
     * Sets image.
     *
     * @param image image to set
     */
    public void setImage(BufferedImage image) {
        this.image = image;
        repaint();
    }

    /**
     * Gets image.
     *
     * @return image
     */
    public BufferedImage getImage() {
        return image;
    }

    /**
     * Gets 2D Graphics to draw on.
     *
     * @return graphics2d object
     */
    public Graphics2D getGraphics2D() {
        return (Graphics2D) image.getGraphics();
    }

    /**
     * Gets button panel.
     *
     * @return button panel
     */
    public ButtonPanel getButtonPanel() {
        return buttonPanel;
    }

    /**
     * Sets neural net.
     *
     * @param nn neural net to set
     */
    public void setNet(NeuralNetwork nn) {
        this.net = nn;
    }

    /**
     * Gets neural net.
     *
     * @return neural net
     */
    public NeuralNetwork getNet() {
        return net;
    }

    /**
     * Gets mouse.
     *
     * @return mouse
     */
    public Mouse getMouse() {
        return mouse;
    }

    /**
     * Gets prediction status.
     *
     * @return prediction status
     */
    public boolean isPredicted() {
        return predicted;
    }

    /**
     * Sets prediction status.
     *
     * @param predicted prediction status
     */
    public void setPredicted(boolean predicted) {
        this.predicted = predicted;
        buttonPanel.getPredictButton().setEnabled(!predicted);
    }

    /**
     * Main method. Creates new Frame.
     *
     * @param args arguments passed
     */
    public static void main(String[] args) {
        new Frame();
    }
}
