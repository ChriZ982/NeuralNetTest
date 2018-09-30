/*
* NeuralNetLib by ChriZ98 is licensed under a
* Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License
* https://creativecommons.org/licenses/by-nc-sa/4.0/
 */
package zindach.neuralnetlib.options.regularization;

import zindach.mathlib.algebra.Matrix;

/**
 * Represents L2 regularization. This method adds or subtracts
 * (lambda*learningrate)/n from every weight. It adds the factor if the weight
 * is less than zero and it subtracts the factor if the weight is greater than
 * zero. This is useful for training because the weights do not saturate
 * quickly. This gives a boost in training speed.
 *
 * @author ChriZ98
 */
public class L1Regularization extends Regularization {

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
    @Override
    public Matrix calculate(Matrix weights, double learningRate, double lambda, int n) {
        double factor = (learningRate * lambda) / n;
        double[][] C = new double[weights.getN()][weights.getM()];
        double[][] A = weights.getArray();
        for (int i = 0; i < weights.getN(); i++) {
            for (int j = 0; j < weights.getM(); j++) {
                if (A[i][j] < 0) {
                    C[i][j] = A[i][j] + factor;
                } else {
                    C[i][j] = A[i][j] - factor;
                }
            }
        }
        return new Matrix(C);
    }

}
