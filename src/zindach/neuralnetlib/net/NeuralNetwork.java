/*
* NeuralNetLib by ChriZ98 is licensed under a
* Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License
* https://creativecommons.org/licenses/by-nc-sa/4.0/
 */
package zindach.neuralnetlib.net;

import zindach.mathlib.algebra.Matrix;
import zindach.mathlib.algebra.Vector;
import zindach.neuralnetlib.options.activation.ActivationFunction;
import zindach.neuralnetlib.options.activation.SigmoidFunction;
import zindach.neuralnetlib.options.cost.CostFunction;
import zindach.neuralnetlib.options.initialization.NormalizedInitialization;
import zindach.neuralnetlib.options.initialization.WeightInitialization;

/**
 * Representing a neural network. Neural Networks are very small representations
 * of the human brain. Every network consists of different layers and performs
 * mathematical operations on them. The whole network can be seen as a black box
 * acting like a function. Networks can be trained so that a specivif input is
 * resulting in a desired output.
 *
 * @author ChriZ98
 */
public class NeuralNetwork {

    private final int size;
    private Vector[] biases;
    private Matrix[] weights;
    private final Matrix[] lastValues;
    private final Matrix[] lastActivations;
    private final ActivationFunction activationFunction;

    /**
     * Initializes the neural network based on the following options. This
     * constructor should only be used by the NetworkIO class
     *
     * @param weights weights of the network
     * @param biases biases of the network
     * @param size number of layers in the network
     * @param activationFunction activation function to apply to ever neuron
     */
    public NeuralNetwork(Matrix[] weights, Vector[] biases, int size, ActivationFunction activationFunction) {
        this.size = size;
        this.biases = biases;
        this.weights = weights;
        this.lastValues = new Matrix[size - 1];
        this.lastActivations = new Matrix[size];
        this.activationFunction = activationFunction;
    }

    /**
     * Initializes the neural network based on the following options.
     *
     * @param neuronFunc activation function to apply to ever neuron
     * @param weightInitialization defines a method to initialize the weight and
     * biases with
     * @param sizes an array of layer sizes
     */
    public NeuralNetwork(ActivationFunction neuronFunc, WeightInitialization weightInitialization, int... sizes) {
        this(weightInitialization.initWeights(sizes), weightInitialization.initBiases(sizes), sizes.length, neuronFunc);
    }

    /**
     * Initializes the neural network based on the sizes. Every layer is
     * initialized with the given size. You need to specify at least two sizes
     * representing the input and output layer size.
     *
     * @param sizes an array of layer sizes
     */
    public NeuralNetwork(int... sizes) {
        this(new SigmoidFunction(), new NormalizedInitialization(), sizes);
    }

    /**
     * Feedforwards the input values organized in a matrix through the network.
     * The neuron values and activations are saved for later use.
     *
     * @param in input values organized in a matrix out of input columns
     * @return result of the output layer. Every input column has its
     * corresponding output column in the returned matrix
     */
    public Matrix feedforward(Matrix in) {
        lastActivations[0] = in;
        for (int i = 0; i < size - 1; i++) {
            lastValues[i] = weights[i].mulMat(lastActivations[i]).addVec(biases[i]);
            lastActivations[i + 1] = activationFunction.calculateMat(lastValues[i]);
        }
        return lastActivations[size - 1];
    }

    /**
     * Backpropagates an error through the network and updates error matrices.
     *
     * @param trainingIn input data
     * @param trainingOut output data with correct values
     * @param weightErrors is going to containg error values of every weight in
     * every layer
     * @param biasErrors is going to containg error values of every bias in
     * every layer
     * @param costFunction cost function being used to calculate error
     */
    public void backpropagate(Matrix trainingIn, Matrix trainingOut, Matrix[] weightErrors, Vector[] biasErrors, CostFunction costFunction) {
        Matrix error = costFunction.calculateError(feedforward(trainingIn), trainingOut, lastValues[size - 2], activationFunction);
        for (int i = size - 2; i >= 0; i--) {
            weightErrors[i] = error.mulMat(lastActivations[i].transpose());
            biasErrors[i] = error.sumCols();
            if (i > 0) {
                error = weights[i].transpose().mulMat(error).hadamardMat(activationFunction.calculateDerivMat(lastValues[i - 1]));
            }
        }
    }

    /**
     * Gets number of layers.
     *
     * @return number of layers
     */
    public int getSize() {
        return size;
    }

    /**
     * Gets biases.
     *
     * @return biases
     */
    public Vector[] getBiases() {
        return biases;
    }

    /**
     * Gets weights.
     *
     * @return weights
     */
    public Matrix[] getWeights() {
        return weights;
    }

    /**
     * Gets activation function.
     *
     * @return activation function
     */
    public ActivationFunction getActivationFunction() {
        return activationFunction;
    }

    /**
     * Set weights.
     *
     * @param weights weights to be set
     */
    public void setWeights(Matrix[] weights) {
        this.weights = weights;
    }

    /**
     * Sets biases.
     *
     * @param biases biases to be set
     */
    public void setBiases(Vector[] biases) {
        this.biases = biases;
    }
}
