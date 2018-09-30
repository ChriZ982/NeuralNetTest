/*
* NeuralNetLib by ChriZ98 is licensed under a
* Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License
* https://creativecommons.org/licenses/by-nc-sa/4.0/
 */
package zindach.neuralnetlib.options.activation;

/**
 * Represents the rectified linear activation function.
 *
 * @author ChriZ98
 */
public class RectifyingFunction extends ActivationFunction {

    /**
     * Calculates activation for single value.
     *
     * @param x value
     * @return calculated activation
     */
    @Override
    protected double calculate(double x) {
        return Math.max(0, x);
    }

    /**
     * Calculates derived activation for single value.
     *
     * @param x value
     * @return calculated activation
     */
    @Override
    protected double calculateDeriv(double x) {
        return x > 0.0 ? 1.0 : 0.0;
    }
}
