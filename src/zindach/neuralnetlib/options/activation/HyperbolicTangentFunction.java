/*
* NeuralNetLib by ChriZ98 is licensed under a
* Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License
* https://creativecommons.org/licenses/by-nc-sa/4.0/
 */
package zindach.neuralnetlib.options.activation;

/**
 * Represents the hyperbolic tangent activation function. Maps every x to a y
 * between -1 and 1.
 *
 * @author ChriZ98
 */
public class HyperbolicTangentFunction extends ActivationFunction {

    /**
     * Calculates activation for single value.
     *
     * @param x value
     * @return calculated activation
     */
    @Override
    protected double calculate(double x) {
        return Math.tanh(x);
    }

    /**
     * Calculates derived activation for single value.
     *
     * @param x value
     * @return calculated activation
     */
    @Override
    protected double calculateDeriv(double x) {
        return 2.0 / (Math.cosh(2.0 * x) + 1.0);
    }
}
