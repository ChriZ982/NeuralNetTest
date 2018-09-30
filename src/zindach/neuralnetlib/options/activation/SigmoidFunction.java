/*
* NeuralNetLib by ChriZ98 is licensed under a
* Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License
* https://creativecommons.org/licenses/by-nc-sa/4.0/
 */
package zindach.neuralnetlib.options.activation;

import zindach.mathlib.algebra.Matrix;
import zindach.mathlib.algebra.Vector;

/**
 * Represents the sigmoid activation function. Maps every x to a y between 0 and
 * 1.
 *
 * @author ChriZ98
 */
public class SigmoidFunction extends ActivationFunction {

    /**
     * Calculates activation for single value.
     *
     * @param x value
     * @return calculated activation
     */
    private double calculate(double x) {
        return 1 / (1 + Math.exp(-x));
    }

    /**
     * Calculates activation for every entry of vector.
     *
     * @param v vector with data to calculate
     * @return calculated values as vector
     */
    @Override
    public Vector calculateVec(Vector v) {
        double[] c = new double[v.getN()];
        double[] va = v.getArray();
        for (int i = 0; i < v.getN(); i++) {
            c[i] = calculate(va[i]);
        }
        return new Vector(c);
    }

    /**
     * Calculates result for every entry of matrix.
     *
     * @param M matrix with data to calculate
     * @return calculated values as matrix
     */
    @Override
    public Matrix calculateMat(Matrix M) {
        double[][] C = new double[M.getN()][M.getM()];
        double[][] MA = M.getArray();
        for (int i = 0; i < M.getN(); i++) {
            for (int j = 0; j < M.getM(); j++) {
                C[i][j] = calculate(MA[i][j]);
            }
        }
        return new Matrix(C);
    }

    /**
     * Calculates derived activation for single value.
     *
     * @param x value
     * @return calculated activation
     */
    private double calculateDeriv(double x) {
        return Math.exp(x) / Math.pow(Math.exp(x) + 1.0, 2.0);
    }

    /**
     * Calculates derived activation for every entry of vector.
     *
     * @param v vector with data to calculate
     * @return calculated values as vector
     */
    @Override
    public Vector calculateDerivVec(Vector v) {
        double[] c = new double[v.getN()];
        double[] va = v.getArray();
        for (int i = 0; i < v.getN(); i++) {
            c[i] = calculateDeriv(va[i]);
        }
        return new Vector(c);
    }

    /**
     * Calculates derived activation for every entry of matrix.
     *
     * @param M matrix with data to calculate
     * @return calculated values as matrix
     */
    @Override
    public Matrix calculateDerivMat(Matrix M) {
        double[][] C = new double[M.getN()][M.getM()];
        double[][] MA = M.getArray();
        for (int i = 0; i < M.getN(); i++) {
            for (int j = 0; j < M.getM(); j++) {
                C[i][j] = calculateDeriv(MA[i][j]);
            }
        }
        return new Matrix(C);
    }
}
