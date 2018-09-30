/*
* NeuralNetLib by ChriZ98 is licensed under a
* Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License
* https://creativecommons.org/licenses/by-nc-sa/4.0/
 */
package zindach.neuralnetlib.trainer;

import zindach.mathlib.algebra.Matrix;
import zindach.mathlib.algebra.Vector;
import zindach.neuralnetlib.net.DataUtils;
import zindach.neuralnetlib.net.NeuralNetwork;
import zindach.neuralnetlib.options.cost.CostFunction;
import zindach.neuralnetlib.options.regularization.Regularization;

/**
 * This class is used to train a neural network using stochastic gradient
 * descent. The idea is to minimize the specified cost function. That means
 * adapting to the desired outputs of the network. This is achieved by
 * evaluation of the cost after each training batch and changing the weights
 * with respect to the "downhill" direction of the cost function.
 *
 * @author ChriZ98
 */
public class StochasticGradientDescentTrainer {

    private final NeuralNetwork net;
    private final CostFunction costFunction;
    private final Regularization regularization;
    private Vector[] trainingIn;
    private Vector[] trainingOut;
    private Vector[] testIn;
    private Vector[] testOut;

    /**
     * Initializes the Trainer.
     *
     * @param net network to be trained
     * @param costFunction cost function to minimize
     * @param regularization regularization applied on weights
     */
    public StochasticGradientDescentTrainer(NeuralNetwork net, CostFunction costFunction, Regularization regularization) {
        this.net = net;
        this.costFunction = costFunction;
        this.regularization = regularization;
    }

    /**
     * Trains the network using the training and test data. Training is repeated
     * for the number of epochs.
     *
     * @param epochs number of training iterations
     * @param learningRate learning rate determines how fast the weights in the
     * network are changed
     * @param lambda strength of regularization
     * @param batchSize size of training data to be trained at once. Lower
     * values decrease training speed but increase accuracy. Higher values
     * increase training speed but decrease accuracy.
     * @param evaluate if true, after every epoch the network is evaluated using
     * the whole test data. Slows down training but gives you important
     * insights.
     */
    public void train(int epochs, double learningRate, double lambda, int batchSize, boolean evaluate) {
        if (trainingIn == null || trainingOut == null || testIn == null || testOut == null) {
            System.out.println("\n--- Training was cancelled because of missing data - Please specify training and test data ---");
        }

        System.out.printf("%n---Training Network---%n"
                + "Training data: %d, Test data: %d, Batch size: %d%n"
                + "Epochs: %d, Learning rate: %.2f, Lambda: %.2f%n%n",
                trainingIn.length, testIn.length, batchSize, epochs, learningRate, lambda);
        for (int i = 0; i < epochs; i++) {
            trainEpoch(i, learningRate, lambda, batchSize, evaluate);
        }
        System.out.printf("Result:%n"
                + "Training accuracy: %.2f%n"
                + "            error: %f%n"
                + "   Test  accuracy: %.2f%n"
                + "            error: %f%n",
                test(trainingIn, trainingOut),
                costFunction.calculateTotal(net, trainingIn, trainingOut),
                test(testIn, testOut),
                costFunction.calculateTotal(net, testIn, testOut));
    }

    /**
     * Trains one epoch using the training data. Divides the whole data into
     * smaller batches and trains the network with them.
     *
     * @param epoch current epoch
     * @param learningRate learning rate determines how fast the weights in the
     * network are changed
     * @param lambda strength of regularization
     * @param batchSize size of training data to be trained at once. Lower
     * values decrease training speed but increase accuracy. Higher values
     * increase training speed but decrease accuracy.
     * @param evaluate if true, after every epoch the network is evaluated using
     * the whole test data. Slows down training but gives you important
     * insights.
     */
    private void trainEpoch(int epoch, double learningRate, double lambda, int batchSize, boolean evaluate) {
        long millis = System.currentTimeMillis();
        DataUtils.shuffle(trainingIn, trainingOut);
        Matrix[] dataIn = DataUtils.subdivide(trainingIn, batchSize);
        Matrix[] dataOut = DataUtils.subdivide(trainingOut, batchSize);
        long millisPrepare = System.currentTimeMillis();
        for (int j = 0; j < trainingIn.length / (double) batchSize; j++) {
            trainBatch(dataIn[j], dataOut[j], learningRate, lambda, trainingIn.length);
        }
        long millisTrain = System.currentTimeMillis();
        System.out.printf("Epoch: %d%n",
                epoch + 1);
        if (evaluate) {
            System.out.printf("Training accuracy: %.2f%n"
                    + "            error: %f%n"
                    + "   Test  accuracy: %.2f%n"
                    + "            error: %f%n"
                    + "Time elapsed: %.4f%n"
                    + "     prepare: %.4f%n"
                    + "    training: %.4f%n"
                    + "    evaluate: %.4f%n",
                    test(trainingIn, trainingOut),
                    costFunction.calculateTotal(net, trainingIn, trainingOut),
                    test(testIn, testOut),
                    costFunction.calculateTotal(net, testIn, testOut),
                    (System.currentTimeMillis() - millis) / 1000.0,
                    (millisPrepare - millis) / 1000.0,
                    (millisTrain - millisPrepare) / 1000.0,
                    (System.currentTimeMillis() - millisTrain) / 1000.0);
        }
    }

    /**
     * Trains one batch of training examples and changes weights of the neural
     * network.
     *
     * @param trainingIn part of whole input training data
     * @param trainingOut part of whole output training data
     * @param learningRate learning rate determines how fast the weights in the
     * network are changed
     * @param lambda strength of regularization
     * @param n total size of training data
     */
    private void trainBatch(Matrix trainingIn, Matrix trainingOut, double learningRate, double lambda, int n) {
        int size = net.getSize();
        Matrix[] weights = net.getWeights();
        Vector[] biases = net.getBiases();
        Matrix[] weightErrors = new Matrix[size - 1];
        Vector[] biasErrors = new Vector[size - 1];
        for (int i = 0; i < size - 1; i++) {
            weightErrors[i] = new Matrix(weights[i].getN(), weights[i].getM());
            biasErrors[i] = new Vector(biases[i].getN());
        }
        net.backpropagate(trainingIn, trainingOut, weightErrors, biasErrors, costFunction);
        double stochasticFactor = learningRate / trainingIn.getM();
        for (int i = 0; i < size - 1; i++) {
            if (regularization == null) {
                weights[i] = weights[i].subMat(weightErrors[i].mulSca(stochasticFactor));
            } else {
                weights[i] = regularization.calculate(weights[i], learningRate, lambda, n).subMat(weightErrors[i].mulSca(stochasticFactor));
            }
            biases[i] = biases[i].subVec(biasErrors[i].mulSca(stochasticFactor));
        }
    }

    /**
     * Testing the neural network. Evaluates the accuracy of the network by
     * comparing the calculated outputs with the desired outputs.
     *
     * @param testIn input test data
     * @param testOut test data of desired outputs
     * @return percentage of test exapmles calculated correctly
     */
    private double test(Vector[] testIn, Vector[] testOut) {
        int correct = 0;
        double[][] result = net.feedforward(new Matrix(testIn)).getArray();
        for (int j = 0; j < testIn.length; j++) {
            int max = 0;
            for (int i = 1; i < result.length; i++) {
                if (result[i][j] > result[max][j]) {
                    max = i;
                }
            }
            if (testOut[j].getArray()[max] == 1.0) {
                correct++;
            }
        }
        return (double) correct / testIn.length * 100.0;
    }

    /**
     * Sets the training data.
     *
     * @param trainingIn input data
     * @param trainingOut output data
     */
    public void setTrainingData(Vector[] trainingIn, Vector[] trainingOut) {
        this.trainingIn = trainingIn;
        this.trainingOut = trainingOut;
    }

    /**
     * Sets the test data.
     *
     * @param testIn input data
     * @param testOut output data
     */
    public void setTestData(Vector[] testIn, Vector[] testOut) {
        this.testIn = testIn;
        this.testOut = testOut;
    }
}
