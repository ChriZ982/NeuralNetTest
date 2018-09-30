/*
* NeuralNetLib by ChriZ98 is licensed under a
* Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License
* https://creativecommons.org/licenses/by-nc-sa/4.0/
 */
package zindach.neuralnetlib.options.activation;

import zindach.mathlib.algebra.Matrix;
import zindach.mathlib.algebra.Vector;

/**
 * Abstract superclass for activation functions that are being applied to
 * neurons.
 *
 * @author ChriZ98
 */
public abstract class ActivationFunction {

    /**
     * Calculates activation for single value.
     *
     * @param x value
     * @return calculated activation
     */
    protected abstract double calculate(double x);

    /**
     * Calculates derived activation for single value.
     *
     * @param x value
     * @return calculated activation
     */
    protected abstract double calculateDeriv(double x);

    /**
     * Calculates activation for every entry of vector.
     *
     * @param v vector with data to calculate
     * @return calculated values as vector
     */
    public Vector calculateVec(Vector v) {
        double[] c = new double[v.getN()];
        double[] va = v.getAllRef();
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
    public Matrix calculateMat(Matrix M) {
        double[][] C = new double[M.getN()][M.getM()];
        double[][] MA = M.getAllRef();
        for (int i = 0; i < M.getN(); i++) {
            for (int j = 0; j < M.getM(); j++) {
                C[i][j] = calculate(MA[i][j]);
            }
        }
        return new Matrix(C);
    }

    /**
     * Calculates derived activation for every entry of vector.
     *
     * @param v vector with data to calculate
     * @return calculated values as vector
     */
    public Vector calculateDerivVec(Vector v) {
        double[] c = new double[v.getN()];
        double[] va = v.getAllRef();
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
    public Matrix calculateDerivMat(Matrix M) {
        double[][] C = new double[M.getN()][M.getM()];
        double[][] MA = M.getAllRef();
        for (int i = 0; i < M.getN(); i++) {
            for (int j = 0; j < M.getM(); j++) {
                C[i][j] = calculateDeriv(MA[i][j]);
            }
        }
        return new Matrix(C);
    }
}
