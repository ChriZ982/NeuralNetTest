package zindach.neuralnettest.main;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import zindach.mathlib.algebra.Vector;
import zindach.neuralnetlib.export.NetworkIO;

public class ButtonPanel extends JPanel {

    private JLabel[] labels;
    private final Frame frame;
    private final Graphics2D graphics;

    public ButtonPanel(Frame frame, Graphics2D graphics) {
        super(new GridLayout(11, 4));
        this.frame = frame;
        this.graphics = graphics;
        this.labels = new JLabel[10];

        for (int i = 0; i < 10; i++) {
            add(new JLabel(i + " "));
            labels[i] = new JLabel();
            add(labels[i]);
            add(new JLabel("%"));
            JButton storeButton = new JButton("Store " + i);
            add(storeButton);
            storeButton.addActionListener((ActionEvent ae) -> {
                storeButtonActionPerformed(ae);
            });
        }
        JButton resetButton = new JButton("Reset");
        JButton trainButton = new JButton("Train");
        JButton saveButton = new JButton("Save");
        JButton loadButton = new JButton("Load");

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

        add(resetButton);
        add(trainButton);
        add(saveButton);
        add(loadButton);

        resetButtonActionPerformed();
    }

    private void storeButtonActionPerformed(ActionEvent ae) {
        try {
            File f = new File("trainingData\\" + ((JButton) ae.getSource()).getText() + "-0" + Frame.FILE_ENDING);
            int i = 1;
            while (f.exists()) {
                f = new File("trainingData\\" + ((JButton) ae.getSource()).getText() + "-" + i + Frame.FILE_ENDING);
                i++;
            }
            ImageIO.write(frame.getImage(), Frame.FILE_ENDING.substring(1), f);
        } catch (IOException ex) {
        }
        graphics.setColor(Color.WHITE);
        graphics.fillRect(0, 0, 28, 28);
        graphics.setColor(Color.BLACK);
        repaint();
    }

    private void trainButtonActionPerformed() {
        File[] dir = new File("trainingData\\").listFiles((File file, String string) -> string.endsWith(Frame.FILE_ENDING));
        Vector[] inputs = new Vector[dir.length];
        Vector[] outputs = new Vector[dir.length];
        for (int i = 0; i < dir.length; i++) {
            try {
                BufferedImage readImage = ImageIO.read(dir[i]);
                inputs[i] = new Vector(calcInput(readImage));
                double[] out = new double[10];
                out[Integer.parseInt(dir[i].getName().split(" ")[1].charAt(0) + "")] = 1;
                outputs[i] = new Vector(out);

            } catch (IOException ex) {
            }
        }
        frame.getNN().stochasticGradientDescent(inputs, outputs, 0.8, 8000, 0.0000005);
    }

    private void saveButtonActionPerformed() {
        NetworkIO.saveNetwork("network.dat", frame.getNN());
    }

    private void loadButtonActionPerformed() {
        frame.setNN(NetworkIO.loadNetwork("network.dat"));
    }

    private void resetButtonActionPerformed() {
        graphics.setColor(Color.WHITE);
        graphics.fillRect(0, 0, 28, 28);
        graphics.setColor(Color.BLACK);
        setLabels(new double[10]);
        frame.repaint();
    }

    public void setLabels(double[] output) {
        for (int i = 0; i < 10; i++) {
            labels[i].setText(String.format("%.2f", output[i] * 100));
        }
    }

    public double[] calcInput(BufferedImage image) {
        double[] input = new double[784];
        for (int i = 0; i < 28; i++) {
            for (int j = 0; j < 28; j++) {
                input[i * 28 + j] = Math.abs((image.getRGB(j, i) + 1.0) / 16777215.0);
            }
        }
        return input;
    }
}
