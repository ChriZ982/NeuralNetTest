/*
* NeuralNetLib by ChriZ98 is licensed under a
* Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License
* https://creativecommons.org/licenses/by-nc-sa/4.0/
 */
package zindach.neuralnetlib.net;

import java.util.Random;
import zindach.mathlib.algebra.Matrix;
import zindach.mathlib.algebra.Vector;

/**
 * A helper class for dealing with training and test data of the neural network.
 *
 * @author ChriZ98
 */
public class DataUtils {

    /**
     * Divides the given array into parts of specified size. The last part could
     * be shorter if size of array is indivisible
     *
     * @param in array to be subdivided
     * @param size size of individual parts
     * @return parts of vector array represented as matrix array
     */
    public static Matrix[] subdivide(Vector[] in, int size) {
        Matrix[] result = new Matrix[(int) Math.ceil((double) in.length / size)];
        for (int i = 0; i < result.length; i++) {
            int pos = i * size;
            Vector[] part;
            if (pos + size > in.length) {
                part = new Vector[in.length - pos];
            } else {
                part = new Vector[size];
            }
            for (int j = 0; j < part.length; j++) {
                part[j] = in[pos + j];
            }
            result[i] = new Matrix(part);
        }
        return result;
    }

    /**
     * Swaps two elements in vector array.
     *
     * @param array array to operate on
     * @param i index of first item
     * @param j index of second item
     */
    private static void swap(Vector[] array, int i, int j) {
        Vector temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }

    /**
     * Shuffles the tow arrays randomly. Every swap is executed on both arrays
     * so that the order stays comparable.
     *
     * @param dataIn first array to shuffle
     * @param dataOut second array to shuffle
     */
    public static void shuffle(Vector[] dataIn, Vector[] dataOut) {
        Random rand = new Random();
        for (Vector data : dataIn) {
            int i1 = rand.nextInt(dataIn.length);
            int i2 = rand.nextInt(dataIn.length);
            swap(dataIn, i1, i2);
            swap(dataOut, i1, i2);
        }
    }
}
