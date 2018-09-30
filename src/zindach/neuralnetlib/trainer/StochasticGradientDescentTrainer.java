/*
* NeuralNetLib by ChriZ98 is licensed under a
* Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License
* https://creativecommons.org/licenses/by-nc-sa/4.0/
 */
package zindach.neuralnetlib.trainer;

import java.util.ArrayList;
import java.util.Random;
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
     * Automated training of the neural network. Starts wit the initial learning
     * rate. After some epochs of no change the learning rate is decreased. This
     * is repeated until the learning rate reaches a specified value.
     *
     * @param epochsWithoutChange number of epochs where no significant change
     * in test cost is happening
     * @param initLearning initial lerning rate
     * @param endLearning last learning rate
     * @param changeLearning the learning rate is multiplied by this factor.
     * Therefore it should be less than 1.
     * @param lambda strength of regularization
     * @param batchSize size of training data to be trained at once. Lower
     * values decrease training speed but increase accuracy. Higher values
     * increase training speed but decrease accuracy.
     * @param dropout if true, half of the neurons are dropped out of the
     * network for training. This is similar to training two networks and
     * averaging over their output. You need roughly twice as many training
     * epochs with dropout.
     */
    public void autoTrain(int epochsWithoutChange, double initLearning, double endLearning, double changeLearning, double lambda, int batchSize, boolean dropout) {
        if (trainingIn == null || trainingOut == null || testIn == null || testOut == null) {
            System.out.println("\n--- Training was cancelled because of missing data - Please specify training and test data ---");
        }

        System.out.printf("%n---Auto Training Network---%n"
                + "Training data: %d, Test data: %d, Batch size: %d%n"
                + "Lambda: %.2f, Dropout: %b, InitLR: %.5f%n"
                + "FactorLR: %.5f, EndLR: %.10f%n%n",
                trainingIn.length, testIn.length, batchSize, lambda, dropout, initLearning, changeLearning, endLearning);
        double learningRate = initLearning;
        double[] lastCosts = new double[epochsWithoutChange];
        for (int k = 0; k < epochsWithoutChange; k++) {
            lastCosts[k] = Double.POSITIVE_INFINITY;
        }
        for (int i = 0; learningRate > endLearning; i++) {
            double[] results = trainEpoch(i, learningRate, lambda, batchSize, true, dropout);
            for (int j = 1; j < epochsWithoutChange; j++) {
                lastCosts[j - 1] = lastCosts[j];
            }
            lastCosts[epochsWithoutChange - 1] = results[3];
            int epochsWithNoChange = 0;
            for (int j = 1; j < epochsWithoutChange; j++) {
                if (lastCosts[j] > lastCosts[j - 1]) {
                    epochsWithNoChange++;
                }
            }
            System.out.println("epochs with no change: " + epochsWithNoChange);
            if (epochsWithNoChange >= 0.5 * epochsWithoutChange) {
                for (int k = 0; k < epochsWithoutChange; k++) {
                    lastCosts[k] = Double.POSITIVE_INFINITY;
                }
                learningRate *= changeLearning;
                System.out.println("new learning rate: " + learningRate);
            }
        }
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
     * @param dropout if true, half of the neurons are dropped out of the
     * network for training. This is similar to training two networks and
     * averaging over their output. You need roughly twice as many training
     * epochs with dropout.
     */
    public void train(int epochs, double learningRate, double lambda, int batchSize, boolean evaluate, boolean dropout) {
        if (trainingIn == null || trainingOut == null || testIn == null || testOut == null) {
            System.out.println("\n--- Training was cancelled because of missing data - Please specify training and test data ---");
        }

        System.out.printf("%n---Training Network---%n"
                + "Training data: %d, Test data: %d, Batch size: %d%n"
                + "Epochs: %d, Learning rate: %.2f, Lambda: %.2f%n"
                + "Evaluate: %b, Dropout: %b%n%n",
                trainingIn.length, testIn.length, batchSize, epochs, learningRate, lambda, evaluate, dropout);
        for (int i = 0; i < epochs; i++) {
            trainEpoch(i, learningRate, lambda, batchSize, evaluate, dropout);
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
     * @param dropout if true, half of the neurons are dropped out of the
     * network for training. This is similar to training two networks and
     * averaging over their output. You need roughly twice as many training
     * epochs with dropout.
     */
    private double[] trainEpoch(int epoch, double learningRate, double lambda, int batchSize, boolean evaluate, boolean dropout) {
        long millis = System.currentTimeMillis();
        DataUtils.shuffle(trainingIn, trainingOut);
        Matrix[] dataIn = DataUtils.subdivide(trainingIn, batchSize);
        Matrix[] dataOut = DataUtils.subdivide(trainingOut, batchSize);
        long millisPrepare = System.currentTimeMillis();
        if (dropout) {
            initDropout(net.getWeights());
        }
        for (int j = 0; j < trainingIn.length / (double) batchSize; j++) {
            trainBatch(dataIn[j], dataOut[j], learningRate, lambda, trainingIn.length, dropout);
        }
        if (dropout) {
            finalizeDropout(net.getWeights());
        }
        long millisTrain = System.currentTimeMillis();
        System.out.printf("Epoch: %d%n",
                epoch + 1);
        if (evaluate) {
            double[] results = new double[4];
            results[0] = test(trainingIn, trainingOut);
            results[1] = costFunction.calculateTotal(net, trainingIn, trainingOut);
            results[2] = test(testIn, testOut);
            results[3] = costFunction.calculateTotal(net, testIn, testOut);
            System.out.printf("Training accuracy: %.2f%n"
                    + "            error: %f%n"
                    + "   Test  accuracy: %.2f%n"
                    + "            error: %f%n"
                    + "Time elapsed: %.4f%n"
                    + "     prepare: %.4f%n"
                    + "    training: %.4f%n"
                    + "    evaluate: %.4f%n",
                    results[0],
                    results[1],
                    results[2],
                    results[3],
                    (System.currentTimeMillis() - millis) / 1000.0,
                    (millisPrepare - millis) / 1000.0,
                    (millisTrain - millisPrepare) / 1000.0,
                    (System.currentTimeMillis() - millisTrain) / 1000.0);
            return results;
        }
        return null;
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
     * @param dropout if true, half of the neurons are dropped out of the
     * network for training. This is similar to training two networks and
     * averaging over their output. You need roughly twice as many training
     * epochs with dropout.
     */
    private void trainBatch(Matrix trainingIn, Matrix trainingOut, double learningRate, double lambda, int n, boolean dropout) {
        double[][][][] dropoutChanges = null;
        if (dropout) {
            dropoutChanges = dropout(net.getWeights());
        }
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
        if (dropout) {
            undoDropout(net.getWeights(), dropoutChanges);
        }
    }

    /**
     * Drops out half of the neurons randomly. This is achieved by changing the
     * value of incoming and outgoing weights of the individual neuron to 0.
     *
     * @param weights weights of network to be manipulated
     * @return changes made in dropout algorithm. Needs to be passed in
     * undoDropout later on
     */
    private double[][][][] dropout(Matrix[] weights) {
        Random rand = new Random();
        double[][][][] changes = new double[weights.length - 1][][][];
        double[][][] oldWeights = new double[weights.length][][];

        for (int i = 0; i < weights.length; i++) {
            oldWeights[i] = weights[i].getAllCopy();
        }

        for (int layer = 0; layer < weights.length - 1; layer++) {
            int layerSize = weights[layer].getN();
            ArrayList<Integer> dropInLayer = new ArrayList<>();
            double[][] previousWeights = weights[layer].getAllRef();
            double[][] nextWeights = weights[layer + 1].getAllRef();

            changes[layer] = new double[layerSize / 2][3][];
            for (int change = 0; change < layerSize / 2; change++) {
                int row = rand.nextInt(layerSize);
                while (dropInLayer.contains(row)) {
                    row++;
                    if (row >= layerSize) {
                        row = 0;
                    }
                }
                dropInLayer.add(row);

                changes[layer][change][0] = new double[]{row};
                changes[layer][change][1] = new double[previousWeights[0].length];
                changes[layer][change][2] = new double[nextWeights.length];

                for (int k = 0; k < previousWeights[0].length; k++) {
                    changes[layer][change][1][k] = oldWeights[layer][row][k];
                    previousWeights[row][k] = 0;
                }
                for (int k = 0; k < nextWeights.length; k++) {
                    changes[layer][change][2][k] = oldWeights[layer + 1][k][row];
                    nextWeights[k][row] = 0;
                }
            }
        }
        return changes;
    }

    /**
     * Restores the dropped out neurons.
     *
     * @param weights weights of network to be manipulated
     * @param changes changes made in dropout algorithm. Is generated by dropout
     * method.
     */
    private void undoDropout(Matrix[] weights, double[][][][] changes) {
        for (int layer = 0; layer < changes.length; layer++) {
            for (double[][] change : changes[layer]) {
                int row = (int) change[0][0];
                double[][] previousWeights = weights[layer].getAllRef();
                double[][] nextWeights = weights[layer + 1].getAllRef();
                for (int k = 0; k < change[1].length; k++) {
                    previousWeights[row][k] = change[1][k];
                }
                for (int k = 0; k < change[2].length; k++) {
                    nextWeights[k][row] = change[2][k];
                }
            }
        }
    }

    /**
     * Initializes dropout. Multiplies every weight coming out of a hidden
     * neuron by 2 so that multiple training runs can be performed.
     *
     * @param weights weights of network to be manipulated
     */
    private void initDropout(Matrix[] weights) {
        for (int i = 1; i < weights.length; i++) {
            weights[i] = weights[i].mulSca(2);
        }
    }

    /**
     * Ends the dropout run. Needs to be called before feedforwarding some input
     * through the network. Multiplies every weight coming out of a hidden
     * neuron by 0.5. This is needed because after dropout in feedforwarding
     * twice as many neurons are active.
     *
     * @param weights weights of network to be manipulated
     */
    private void finalizeDropout(Matrix[] weights) {
        for (int i = 1; i < weights.length; i++) {
            weights[i] = weights[i].mulSca(0.5);
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
        double[][] result = net.feedforward(new Matrix(testIn)).getAllRef();
        for (int j = 0; j < testIn.length; j++) {
            int max = 0;
            for (int i = 1; i < result.length; i++) {
                if (result[i][j] > result[max][j]) {
                    max = i;
                }
            }
            if (testOut[j].getAllRef()[max] == 1.0) {
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
