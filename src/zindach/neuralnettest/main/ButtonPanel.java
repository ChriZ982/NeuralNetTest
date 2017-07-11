package zindach.neuralnettest.main;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import zindach.mathlib.algebra.Vector;
import zindach.neuralnetlib.io.NetworkIO;

public class ButtonPanel extends JPanel {

    private final JLabel[] labels;
    private final Frame frame;
    private final Graphics2D graphics;
    private Vector[] inputData;
    private Vector[] outputData;

    public ButtonPanel(Frame frame, Graphics2D graphics) {
        super(new GridLayout(12, 3));
        this.frame = frame;
        this.graphics = graphics;
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
        JButton loadTrainingButton = new JButton("Load Training");

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
        loadTrainingButton.addActionListener((ActionEvent ae) -> {
            loadTrainingButtonActionPerformed();
        });

        add(loadButton);
        add(saveButton);
        add(resetButton);
        add(loadTrainingButton);
        add(trainButton);

        resetButtonActionPerformed();
    }

    private void trainButtonActionPerformed() {
        frame.getNN().stochasticGradientDescent(inputData, outputData, 0.833334, 30, 3.0, 10);
    }

    private void saveButtonActionPerformed() {
        NetworkIO.saveNetwork("network.dat", frame.getNN());
    }

    private void loadButtonActionPerformed() {
        frame.setNN(NetworkIO.loadNetwork("network.dat"));
    }

    private void resetButtonActionPerformed() {
        graphics.setColor(Color.BLACK);
        graphics.fillRect(0, 0, 28, 28);
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
                labels[i].setForeground(new Color(0f, 0.6f, 0f));
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

    private void loadTrainingButtonActionPerformed() {
        System.out.println("---Loading Training Data---");
        ArrayList<Vector> inputs = new ArrayList<>();
        ArrayList<Vector> outputs = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            System.out.println(i);
            File[] dir = new File("training\\" + i + "\\").listFiles((File file, String string) -> string.endsWith(Frame.FILE_ENDING));
            for (File file : dir) {
                try {
                    BufferedImage readImage = ImageIO.read(file);
                    inputs.add(new Vector(calcInput(readImage)));
                    double[] out = new double[10];
                    out[i] = 1;
                    outputs.add(new Vector(out));
                } catch (IOException ex) {
                }
            }
        }
        inputData = new Vector[inputs.size()];
        outputData = new Vector[outputs.size()];
        for (int i = 0; i < inputs.size(); i++) {
            inputData[i] = inputs.get(i);
            outputData[i] = outputs.get(i);
        }
    }
}
