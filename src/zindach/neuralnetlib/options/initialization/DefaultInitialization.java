/*
* NeuralNetLib by ChriZ98 is licensed under a
* Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License
* https://creativecommons.org/licenses/by-nc-sa/4.0/
 */
package zindach.neuralnetlib.options.initialization;

import java.util.Random;
import zindach.mathlib.algebra.Matrix;
import zindach.mathlib.algebra.Vector;

/**
 * Initializes the weights and biases using gaussian normal distribution.
 * Performs no additional operations on the values.
 *
 * @author ChriZ98
 */
public class DefaultInitialization extends WeightInitialization {

    /**
     * Initializes the bias vectors.
     *
     * @param sizes layer sizes of neural network
     * @return vector array with initialized values
     */
    @Override
    public Vector[] initBiases(int[] sizes) {
        Random rand = new Random();
        Vector[] biases = new Vector[sizes.length - 1];

        for (int i = 0; i < sizes.length - 1; i++) {
            double[] bias = new double[sizes[i + 1]];
            for (int j = 0; j < sizes[i + 1]; j++) {
                bias[j] = rand.nextGaussian();
            }
            biases[i] = new Vector(bias);
        }

        return biases;
    }

    /**
     * Initializes the weight matrices.
     *
     * @param sizes layer sizes of neural network
     * @return matrix array with initialized values
     */
    @Override
    public Matrix[] initWeights(int[] sizes) {
        Random rand = new Random();
        Matrix[] weights = new Matrix[sizes.length - 1];

        for (int i = 0; i < sizes.length - 1; i++) {
            double[][] weight = new double[sizes[i + 1]][sizes[i]];
            for (int j = 0; j < sizes[i + 1]; j++) {
                for (int k = 0; k < sizes[i]; k++) {
                    weight[j][k] = rand.nextGaussian();
                }
            }
            weights[i] = new Matrix(weight);
        }

        return weights;
    }

}
