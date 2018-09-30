/*
* NeuralNetLib by ChriZ98 is licensed under a
* Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License
* https://creativecommons.org/licenses/by-nc-sa/4.0/
 */
package zindach.neuralnetlib.options.initialization;

import zindach.mathlib.algebra.Matrix;
import zindach.mathlib.algebra.Vector;

/**
 * An abstract superclass for different weight initializetion approaches.
 *
 * @author ChriZ98
 */
public abstract class WeightInitialization {

    /**
     * Initializes the bias vectors.
     *
     * @param sizes layer sizes of neural network
     * @return vector array with initialized values
     */
    public abstract Vector[] initBiases(int[] sizes);

    /**
     * Initializes the weight matrices.
     *
     * @param sizes layer sizes of neural network
     * @return matrix array with initialized values
     */
    public abstract Matrix[] initWeights(int[] sizes);
}
