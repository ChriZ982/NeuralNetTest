package zindach.neuralnettest.main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import zindach.mathlib.algebra.Vector;
import zindach.neuralnetlib.main.CostFunction;
import zindach.neuralnetlib.main.NeuralNetwork;
import zindach.neuralnetlib.main.NeuronFunction;

public class Frame extends JFrame {

    private BufferedImage image;
    private JLabel[] labels;
    private NeuralNetwork nn;

    public Frame() throws HeadlessException {
        super("Neural Network Test");
        setSize(1000, 600);

        labels = new JLabel[10];
        nn = new NeuralNetwork(NeuronFunction.SIGMOID, CostFunction.QUADRATIC, 784, 15, 10);

        image = new BufferedImage(28, 28, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = (Graphics2D) image.getGraphics();
        graphics.setColor(Color.WHITE);
        graphics.fillRect(0, 0, 28, 28);
        graphics.setColor(Color.BLACK);

        setLayout(new BoxLayout(getContentPane(), BoxLayout.X_AXIS));

        JPanel panel1 = new JPanel() {
            @Override
            protected void paintComponent(Graphics grphcs) {
                super.paintComponent(grphcs);
                grphcs.drawImage(image, 0, 0, 600, 600, 0, 0, 28, 28, this);
            }
        };
        panel1.setPreferredSize(new Dimension(600, 600));
        add(panel1);

        JPanel panel2 = new JPanel(new GridLayout(11, 3));

        for (int i = 0; i < 10; i++) {
            panel2.add(new JLabel(i + " "));
            labels[i] = new JLabel();
            panel2.add(labels[i]);
            JButton button = new JButton("train " + i);
            panel2.add(button);
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent ae) {
                    try {
                        File f = new File("trainingData\\" + ((JButton) ae.getSource()).getText() + "-0.png");
                        int i = 1;
                        while (f.exists()) {
                            f = new File("trainingData\\" + ((JButton) ae.getSource()).getText() + "-" + i + ".png");
                            i++;
                        }
                        ImageIO.write(image, "png", f);
                    } catch (IOException ex) {
                    }
                    graphics.setColor(Color.WHITE);
                    graphics.fillRect(0, 0, 28, 28);
                    graphics.setColor(Color.BLACK);
                    repaint();
                }
            });
        }
        JButton button2 = new JButton("Reset");
        panel2.add(button2);
        button2.addActionListener((ActionEvent ae) -> {
            graphics.setColor(Color.WHITE);
            graphics.fillRect(0, 0, 28, 28);
            graphics.setColor(Color.BLACK);
            repaint();
        });
        JButton button3 = new JButton("Train");
        panel2.add(button3);
        button3.addActionListener((ActionEvent ae) -> {
            ArrayList<Double[]> trainingData = new ArrayList<>();
            ArrayList<Double> correctOut = new ArrayList<>();
            File[] dir = new File("trainingData\\").listFiles(new FilenameFilter() {
                @Override
                public boolean accept(File file, String string) {
                    return string.endsWith(".png");
                }
            });
            Vector[] inputs = new Vector[dir.length];
            Vector[] outputs = new Vector[dir.length];
            for (int i = 0; i < dir.length; i++) {
                try {
                    BufferedImage readImage = ImageIO.read(dir[i]);
                    inputs[i] = new Vector(calcInput(image));
                    double[] out = new double[10];
//                    System.out.println(dir[i].getName());
                    out[Integer.parseInt(dir[i].getName().split(" ")[1].charAt(0) + "")] = 1;
                    outputs[i] = new Vector(out);

                } catch (IOException ex) {
                }
            }
            nn.stochasticGradientDescent(inputs, outputs);
        });

        add(panel2);

        setResizable(false);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        MouseAdapter mo = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent me) {
                graphics.drawRect((int) (me.getX() / 600.0 * 28.0), (int) (me.getY() / 600.0 * 28.0) - 1, 0, 0);
                repaint();
            }

            @Override
            public void mouseDragged(MouseEvent me) {
                graphics.drawRect((int) (me.getX() / 600.0 * 28.0), (int) (me.getY() / 600.0 * 28.0) - 1, 0, 0);
                repaint();
            }

            @Override
            public void mouseReleased(MouseEvent me) {
                setLabels(nn.calculate(new Vector(calcInput(image))).values());
            }
        };
        addMouseListener(mo);
        addMouseMotionListener(mo);
    }

    private double[] calcInput(BufferedImage image) {
        double[] input = new double[784];
        for (int i = 0; i < 28; i++) {
            for (int j = 0; j < 28; j++) {
                input[i * 28 + j] = image.getRGB(i, j) == -1 ? 0.0 : 1.0;
            }
        }
        return input;
    }

    private void setLabels(double[] output) {
        for (int i = 0; i < 10; i++) {
            labels[i].setText(String.format("%.2f", output[i] * 100));
        }
    }

    public static void main(String[] args) {
        Frame frame = new Frame();
    }
}
