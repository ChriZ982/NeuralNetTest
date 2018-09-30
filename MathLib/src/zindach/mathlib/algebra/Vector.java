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
        double s = 0;
        for (int i = 0; i < n; i++) {
            s += a[i] * b.a[i];
        }
        return s;
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
     * Gets all values as an array
     *
     * @return values as array
     */
    public double[] getArray() {
        return a;
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
