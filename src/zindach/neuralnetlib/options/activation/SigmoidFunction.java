/*
* NeuralNetLib by ChriZ98 is licensed under a
* Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License
* https://creativecommons.org/licenses/by-nc-sa/4.0/
 */
package zindach.neuralnetlib.options.activation;

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
    @Override
    protected double calculate(double x) {
        return 1 / (1 + Math.exp(-x));
    }

    /**
     * Calculates derived activation for single value.
     *
     * @param x value
     * @return calculated activation
     */
    @Override
    protected double calculateDeriv(double x) {
        double exp = Math.exp(x);
        return exp / (x * (x + 2) + 1);
    }
}