/*
* MathLib by ChriZ98 is licensed under a
* Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License
* https://creativecommons.org/licenses/by-nc-sa/4.0/
 */
package zindach.mathlib.algebra;

import java.util.Arrays;

/**
 * Representation of a real matrix with customizable widht and height. First
 * index is always the row number and second index is always the column number.
 *
 * @author ChriZ98
 */
public class Matrix {

    private final int n, m;
    private final double[][] A;

    /**
     * Initializes the matrix using predefined values.
     *
     * @param A values that the matrix should consist of
     */
    public Matrix(double[][] A) {
        this.A = A;
        this.n = A.length;
        this.m = A[0].length;
    }

    /**
     * Initializes an empty matrix of the given size
     *
     * @param n number of rows
     * @param m number of columns
     */
    public Matrix(int n, int m) {
        this(new double[n][m]);
    }

    /**
     * Initializes the matrix using column vectors.
     *
     * @param cols the columns that the matrix should consist ofs
     */
    public Matrix(Vector... cols) {
        this(cols[0].getN(), cols.length);
        for (int j = 0; j < m; j++) {
            double[] b = cols[j].getAllRef();
            for (int i = 0; i < n; i++) {
                A[i][j] = b[i];
            }
        }
    }

    /**
     * Transposes the Matrix. Imagine it being like an 90 degree rotation.
     * Columns become rows and rows become columns.
     *
     * @return transposed matrix
     */
    public Matrix transpose() {
        Matrix C = new Matrix(m, n);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                C.A[j][i] = A[i][j];
            }
        }
        return C;
    }

    /**
     * Adds a vector to every column in the matrix.
     *
     * @param b vector to be added to the columns
     * @return resulting matrix
     */
    public Matrix addVec(Vector b) {
        Matrix C = new Matrix(n, m);
        double[] ba = b.getAllRef();
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                C.A[i][j] = A[i][j] + ba[i];
            }
        }
        return C;
    }

    /**
     * Adds another matrix.
     *
     * @param B matrix with values to add
     * @return resulting matrix
     */
    public Matrix addMat(Matrix B) {
        checkEqualDimensions(B);
        Matrix C = new Matrix(n, m);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                C.A[i][j] = A[i][j] + B.A[i][j];
            }
        }
        return C;
    }

    /**
     * Subtracts another Matrix.
     *
     * @param B matrix with values to subtract
     * @return resulting matrix
     */
    public Matrix subMat(Matrix B) {
        checkEqualDimensions(B);
        Matrix C = new Matrix(n, m);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                C.A[i][j] = A[i][j] - B.A[i][j];
            }
        }
        return C;
    }

    /**
     * Multiplies with another Matrix. Uses the ikj-Algorithm.
     *
     * @param B matrix to multiply with
     * @return resulting matrix
     */
    public Matrix mulMat(Matrix B) {
        checkMulDimensions(B);
        Matrix C = new Matrix(n, B.m);
        for (int i = 0; i < n; i++) {
            for (int k = 0; k < B.n; k++) {
                for (int j = 0; j < B.m; j++) {
                    C.A[i][j] += A[i][k] * B.A[k][j];
                }
            }
        }
        return C;
    }

    /**
     * Takes the hadamard product with another matrix. That means multiplying
     * the values in place.
     *
     * @param B matrix with values to multiply with
     * @return resulting matrix
     */
    public Matrix hadamardMat(Matrix B) {
        checkEqualDimensions(B);
        Matrix C = new Matrix(n, m);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                C.A[i][j] = A[i][j] * B.A[i][j];
            }
        }
        return C;
    }

    /**
     * Multiplies with a column vector.
     *
     * @param b vector to multiply with
     * @return resulting vector
     */
    public Vector mulVec(Vector b) {
        checkVectorDimensions(b);
        double[] c = new double[n];
        double[] ba = b.getAllRef();
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                c[i] += A[i][j] * ba[j];
            }
        }
        return new Vector(c);
    }

    /**
     * Multiplies every single entry with a scalar.
     *
     * @param s scalar to multiply with
     * @return resulting matrix
     */
    public Matrix mulSca(double s) {
        Matrix C = new Matrix(n, m);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                C.A[i][j] = s * A[i][j];
            }
        }
        return C;
    }

    /**
     * Sums all columns to one vector with size n.
     *
     * @return vector with sum of all columns
     */
    public Vector sumCols() {
        double[] c = new double[n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                c[i] += A[i][j];
            }
        }
        return new Vector(c);
    }

    /**
     * Gets all columns as vectors out of the matrix.
     *
     * @return columns as vector array
     */
    public Vector[] getCols() {
        Vector[] result = new Vector[m];
        for (int j = 0; j < m; j++) {
            double[] v = new double[n];
            for (int i = 0; i < n; i++) {
                v[i] = A[i][j];
            }
            result[j] = new Vector(v);
        }
        return result;
    }

    /**
     * Throws an exception if dimension of other matrix is not valid.
     *
     * @param b other matrix
     */
    private void checkEqualDimensions(Matrix B) {
        if (n != B.n || m != B.m) {
            throw new RuntimeException("Dimensions of Matrices not matching: " + n + " and " + B.n + ", " + m + " and " + B.m);
        }
    }

    /**
     * Throws an exception if dimension of other vector is not valid.
     *
     * @param b other vector
     */
    private void checkVectorDimensions(Vector b) {
        if (m != b.getN()) {
            throw new RuntimeException("Dimensions of Matrix and Vector not matching: " + m + " and " + b.getN());
        }
    }

    /**
     * Throws an exception if dimension of other matrix is not valid.
     *
     * @param b other matrix
     */
    private void checkMulDimensions(Matrix B) {
        if (m != B.n) {
            throw new RuntimeException("Dimensions of Matrices not matching: " + n + " and " + B.m + ", " + m + " and " + B.n);
        }
    }

    /**
     * Gets row count.
     *
     * @return row count
     */
    public int getN() {
        return n;
    }

    /**
     * Gets column count
     *
     *
     * @return column count
     */
    public int getM() {
        return m;
    }

    /**
     * Gets values at specified position.
     *
     * @param i row
     * @param j column
     * @return value at position
     */
    public double get(int i, int j) {
        return A[i][j];
    }

    /**
     * Gets all values as array.
     *
     * @return all values as array
     */
    public double[][] getAllRef() {
        return A;
    }

    /**
     * Gets all values copied into an array.
     *
     * @return copied values as array
     */
    public double[][] getAllCopy() {
        double[][] C = new double[n][m];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                C[i][j] = A[i][j];
            }
        }
        return C;
    }

    /**
     * Compares every entry of the matrix with another matrix.
     *
     * @param o other matrix to compare with
     * @return true if matrices equal false if not
     */
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Matrix)) {
            return false;
        }
        Matrix B = ((Matrix) o);
        if (n != B.n || m != B.m) {
            return false;
        }
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                if (A[i][j] != B.A[i][j]) {
                    return false;
                }
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
        int hash = 5;
        hash = 67 * hash + Arrays.deepHashCode(this.A);
        return hash;
    }

    /**
     * Generates string with all values of the matrix row and column organized.
     *
     * @return string representation
     */
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        for (double[] mr : A) {
            for (double mi : mr) {
                result.append(mi).append(" ");
            }
            result.append("\n");
        }
        return result.toString();
    }
}
