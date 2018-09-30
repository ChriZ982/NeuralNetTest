/*
* NeuralNetLib by ChriZ98 is licensed under a
* Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License
* https://creativecommons.org/licenses/by-nc-sa/4.0/
 */
package zindach.neuralnetlib.options.cost;

import zindach.mathlib.algebra.Matrix;
import zindach.mathlib.algebra.Vector;
import zindach.neuralnetlib.net.NeuralNetwork;
import zindach.neuralnetlib.options.activation.ActivationFunction;

/**
 * Represents the cross entropy cost function. C = - 1/n sum(y ln a + (1-y)
 * ln(1-a))
 *
 * @author ChriZ98
 */
public class CrossEntropyCostFunction extends CostFunction {

    /**
     * Calculates the total cost for some given test data.
     *
     * @param net network to test
     * @param dataIn input test data
     * @param dataOut output test data for evaluation
     * @return cost evaluated
     */
    @Override
    public double calculateTotal(NeuralNetwork net, Vector[] dataIn, Vector[] dataOut) {
        double sum = 0;
        double[][] MA = net.feedforward(new Matrix(dataIn)).getArray();
        for (int i = 0; i < dataIn.length; i++) {
            double[] y = dataOut[i].getArray();
            for (int j = 0; j < y.length; j++) {
                sum += y[j] * Math.log(MA[j][i]) + (1.0 - y[j]) * Math.log(1.0 - MA[j][i]);
            }
        }
        return -(1.0 / dataIn.length) * sum;
    }

    /**
     * Calculates the error for one vector of training data.
     *
     * @param calcOut calculated output vector
     * @param dataOut output vector for evaluation
     * @param values values of network without activation applied
     * @param activationFunction activation function used in neural network
     * @return calculated error
     */
    @Override
    public Matrix calculateError(Matrix calcOut, Matrix dataOut, Matrix values, ActivationFunction activationFunction) {
        return calcOut.subMat(dataOut);
    }

}
