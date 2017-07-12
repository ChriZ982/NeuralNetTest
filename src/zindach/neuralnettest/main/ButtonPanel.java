package zindach.neuralnettest.main;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.util.Random;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import zindach.mathlib.algebra.Vector;
import zindach.neuralnetlib.io.NetworkIO;

public class ButtonPanel extends JPanel {

    private final JLabel[] labels;
    private final Frame frame;

    public ButtonPanel(Frame frame) {
        super(new GridLayout(12, 3));
        this.frame = frame;
        this.labels = new JLabel[10];

        for (int i = 0; i < 10; i++) {
            add(new JLabel(i + " ", JLabel.RIGHT));
            labels[i] = new JLabel("", JLabel.CENTER);
            add(labels[i]);
            add(new JLabel("%"));
        }
        JButton resetButton = new JButton("Reset");
        JButton trainButton = new JButton("Train");
        JButton saveButton = new JButton("Save");
        JButton loadButton = new JButton("Load");
        JButton testButton = new JButton("Test");
        JButton predictButton = new JButton("Predict");

        resetButton.addActionListener((ActionEvent ae) -> {
            resetButtonActionPerformed();
        });
        trainButton.addActionListener((ActionEvent ae) -> {
            trainButtonActionPerformed();
        });
        saveButton.addActionListener((ActionEvent ae) -> {
            saveButtonActionPerformed();
        });
        loadButton.addActionListener((ActionEvent ae) -> {
            loadButtonActionPerformed();
        });
        testButton.addActionListener((ActionEvent ae) -> {
            testButtonActionPerformed();
        });
        predictButton.addActionListener((ActionEvent ae) -> {
            predictButtonActionPerformed();
        });

        add(loadButton);
        add(saveButton);
        add(resetButton);
        add(trainButton);
        add(testButton);
        add(predictButton);

        resetButtonActionPerformed();

//        Vector[] test = MNISTLoader.importData("data/t10k-labels-idx1-ubyte.gz");
//        Vector[] test2 = MNISTLoader.importData("data/t10k-images-idx3-ubyte.gz");
//
//        drawImageFromVector(test2[1]);
    }

    private void trainButtonActionPerformed() {
//        VectorUtils.shuffle(inputData, outputData);
//        Vector[][] dataWithoutOverfitting = VectorUtils.split(inputData, outputData, 0.033333334);
//        frame.getNN().stochasticGradientDescent(dataWithoutOverfitting[0], dataWithoutOverfitting[1], 0.5, 400, 0.5, 0.1, 10, true);
        frame.getNN().stochasticGradientDescent(MNISTLoader.importData("data/train-images-idx3-ubyte.gz"),
                MNISTLoader.importData("data/train-labels-idx1-ubyte.gz"),
                MNISTLoader.importData("data/t10k-images-idx3-ubyte.gz"),
                MNISTLoader.importData("data/t10k-labels-idx1-ubyte.gz"),
                60, 0.1, 5.0, 10, true);
    }

    private void testButtonActionPerformed() {
        Random rand = new Random();
        Vector[] data = MNISTLoader.importData("data/t10k-images-idx3-ubyte.gz");
        new Thread(() -> {
            System.out.println("\n---Testing Network---");
            for (int j = 0; j < data.length; j += rand.nextInt(200) + 400) {
                try {
                    drawImageFromVector(data[j]);
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                }

            }
            System.out.println("finished");
        }).start();
    }

    private void saveButtonActionPerformed() {
        NetworkIO.saveNetwork("data/network.dat", frame.getNN());
    }

    private void loadButtonActionPerformed() {
        frame.setNN(NetworkIO.loadNetwork("data/network.dat"));
    }

    private void resetButtonActionPerformed() {
        Graphics2D g = frame.getGraphics2D();
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, Frame.DRAW_SIZE, Frame.DRAW_SIZE);
        setLabels(new double[10]);
        frame.repaint();
    }

    public void setLabels(double[] output) {
        double sum = 0;
        double max = Double.NEGATIVE_INFINITY;
        for (double d : output) {
            sum += d;
            if (d > max) {
                max = d;
            }
        }
        for (int i = 0; i < 10; i++) {
            if (sum != 0) {
                labels[i].setText(String.format("%.2f", output[i] / sum * 100));
            } else {
                labels[i].setText(String.format("%.2f", output[i] * 100));
            }
            if (output[i] == max && sum != 0) {
                labels[i].setForeground(new Color(0f, 0.8f, 0f));
            } else {
                labels[i].setForeground(Color.BLACK);
            }
        }
    }

    public double[] calcInput(BufferedImage image) {
        double[] input = new double[784];
        for (int i = 0; i < 28; i++) {
            for (int j = 0; j < 28; j++) {
                input[i * 28 + j] = 1.0 - Math.abs((image.getRGB(j, i) + 1.0) / 16777215.0);
            }
        }
        return input;
    }

    private void drawImageFromVector(Vector vector) {
        resetButtonActionPerformed();
        for (int i = 0; i < 28; i++) {
            for (int j = 0; j < 28; j++) {
                int grey = 255 - (int) (vector.value(i * 28 + j) * 255);
                frame.getGraphics2D().setColor(new Color(grey, grey, grey));
                frame.getGraphics2D().fillRect(j * 25, i * 25, 25, 25);
            }
        }
        repaint();
        setLabels(frame.getNN().calculate(vector).values());
    }

    private void predictButtonActionPerformed() {
        double[] vec = new double[784];
        for (int i = 0; i < 28; i++) {
            for (int j = 0; j < 28; j++) {
                int sum = 0;
                for (int k = 0; k < 25; k++) {
                    for (int l = 0; l < 25; l++) {
                        sum += new Color(frame.getImage().getRGB(j * 25 + k, i * 25 + l)).getRed();
                    }
                }
                vec[i * 28 + j] = 1 - sum / 625.0 / 255.0;
            }
        }
        drawImageFromVector(new Vector(vec));
    }
}
