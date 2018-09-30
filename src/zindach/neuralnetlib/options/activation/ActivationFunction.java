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
     * Calculates activation for every entry of vector.
     *
     * @param v vector with data to calculate
     * @return calculated values as vector
     */
    public abstract Vector calculateVec(Vector v);

    /**
     * Calculates result for every entry of matrix.
     *
     * @param M matrix with data to calculate
     * @return calculated values as matrix
     */
    public abstract Matrix calculateMat(Matrix M);

    /**
     * Calculates derived activation for every entry of vector.
     *
     * @param v vector with data to calculate
     * @return calculated values as vector
     */
    public abstract Vector calculateDerivVec(Vector v);

    /**
     * Calculates derived activation for every entry of matrix.
     *
     * @param M matrix with data to calculate
     * @return calculated values as matrix
     */
    public abstract Matrix calculateDerivMat(Matrix M);
}
