/*
* MathLib by ChriZ98 is licensed under a
* Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License
* https://creativecommons.org/licenses/by-nc-sa/4.0/
 */
package zindach.mathlib.algebra;

import java.util.Arrays;

/**
 * Represents a real vector with customizable size.
 *
 * @author ChriZ98
 */
public class Vector {

    private final int n;
    private final double[] a;

    /**
     * Initializes a vector with fixed size and no values.
     *
     * @param size size of vector
     */
    public Vector(int size) {
        this.a = new double[size];
        this.n = size;
    }

    /**
     * Initializes vector with predefined values.
     *
     * @param v values to be represented by vector
     */
    public Vector(double... v) {
        this.a = v;
        this.n = v.length;
    }

    /**
     * Adds another vector.
     *
     * @param b other vector to add
     * @return resulting vector
     */
    public Vector addVec(Vector b) {
        checkEqualDimensions(b);
        Vector c = new Vector(n);
        for (int i = 0; i < n; i++) {
            c.a[i] = a[i] + b.a[i];
        }
        return c;
    }

    /**
     * Subtracts another vector.
     *
     * @param b vector to be subtracted
     * @return resulting vector
     */
    public Vector subVec(Vector b) {
        checkEqualDimensions(b);
        Vector c = new Vector(n);
        for (int i = 0; i < n; i++) {
            c.a[i] = a[i] - b.a[i];
        }
        return c;
    }

    /**
     * Takes the hadamard product with another vector. That means multiplying
     * all values in place.
     *
     * @param b other vector
     * @return resulting vector
     */
    public Vector hadamardVec(Vector b) {
        checkEqualDimensions(b);
        Vector c = new Vector(n);
        for (int i = 0; i < n; i++) {
            c.a[i] = a[i] * b.a[i];
        }
        return c;
    }

    /**
     * Multiplies with scalar.
     *
     * @param s scalar to multiply with
     * @return
     */
    public Vector mulSca(double s) {
        Vector c = new Vector(n);
        for (int i = 0; i < n; i++) {
            c.a[i] = a[i] * s;
        }
        return c;
    }

    /**
     * Applies dot product.
     *
     * @param b other vector
     * @return resulting scalar
     */
    public double dotVec(Vector b) {
        checkEqualDimensions(b);
        double s = 0;
        for (int i = 0; i < n; i++) {
            s += a[i] * b.a[i];
        }
        return s;
    }

    /**
     * Throws an exception if dimension of other vector is not valid.
     *
     * @param b other vector
     */
    private void checkEqualDimensions(Vector b) {
        if (n != b.n) {
            throw new RuntimeException("Dimensions of Vectors not matching: " + n + " and " + b.n);
        }
    }

    /**
     * Calculates length of the vector.
     *
     * @return length of vector
     */
    public double length() {
        return Math.sqrt(dotVec(this));
    }

    /**
     * Gets size of vector.
     *
     * @return size of vector
     */
    public int getN() {
        return n;
    }

    /**
     * Gets value at index.
     *
     * @param i value index
     * @return value
     */
    public double get(int i) {
        return a[i];
    }

    /**
     * Gets all values as an array
     *
     * @return values as array
     */
    public double[] getAllRef() {
        return a;
    }

    /**
     * Gets all values copied into another array.
     *
     * @return copy of all values
     */
    public double[] getAllCopy() {
        double[] c = new double[n];
        System.arraycopy(a, 0, c, 0, n);
        return c;
    }

    /**
     * Compares all values to other vector.
     *
     * @param o other vector
     * @return true if all values equal each other false if not
     */
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Vector)) {
            return false;
        }
        Vector b = (Vector) o;
        if (n != b.n) {
            return false;
        }
        for (int i = 0; i < n; i++) {
            if (a[i] != b.a[i]) {
                return false;
            }
        }
        return true;
    }

    /**
     * Generates hashcode.
     *
     * @return hashcode
     */
    @Override
    public int hashCode() {
        int hash = 3;
        hash = 97 * hash + Arrays.hashCode(this.a);
        return hash;
    }

    /**
     * Generates string out of all values.
     *
     * @return generated string
     */
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        for (double vi : a) {
            result.append(vi).append(" ");
        }
        return result.toString();
    }
}
