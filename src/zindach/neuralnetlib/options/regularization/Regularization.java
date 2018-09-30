/*
* NeuralNetLib by ChriZ98 is licensed under a
* Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License
* https://creativecommons.org/licenses/by-nc-sa/4.0/
 */
package zindach.neuralnetlib.options.regularization;

import zindach.mathlib.algebra.Matrix;

/**
 * An abstract superclass for different regularization approaches.
 *
 * @author ChriZ98
 */
public abstract class Regularization {

    /**
     * Applies the specified regularization on a matrix.
     *
     * @param weights current weights of neural network
     * @param learningRate the learning rate used in training the network
     * @param lambda the lambda that affects how intense the changes to the
     * weights are
     * @param n count of training data used for training
     * @return regularised weight array
     */
    public abstract Matrix calculate(Matrix weights, double learningRate, double lambda, int n);
}
