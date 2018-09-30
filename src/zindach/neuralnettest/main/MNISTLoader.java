/*
* NeuralNetTest by ChriZ98 is licensed under a
* Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License
* https://creativecommons.org/licenses/by-nc-sa/4.0/
 */
package zindach.neuralnettest.main;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import zindach.mathlib.algebra.Vector;

/**
 * Loads MNIST data sets. Loading works according to specified standards at
 * http://yann.lecun.com/exdb/mnist/.
 *
 * @author ChriZ98
 */
public class MNISTLoader {

    /**
     * Tries to read MNIST data from some file.
     *
     * @param fileName filename e.g. "train-images-idx3-ubyte.gz"
     * @return imported data
     */
    public static Vector[] importData(String fileName) {
        try {
            System.out.println("\n---Importing MNIST data---\nfile: " + fileName);
            GZIPInputStream gzip = new GZIPInputStream(new FileInputStream(fileName));
            byte[] magic = new byte[4];
            gzip.read(magic);
            int magicNum = bytesToInt(magic);
            System.out.println("magic num: " + magicNum);
            switch (magicNum) {
                case 2049:
                    return importLabelFile(gzip);
                case 2051:
                    return importImageFile(gzip);
                default:
                    System.err.println("This is not a valid file. magic num: " + magicNum);
                    break;
            }
        } catch (IOException ex) {
            System.err.println("Error while reading file:\n" + ex);
        }
        return new Vector[0];
    }

    /**
     * Converts 4 bytes to 32 bit integer.
     *
     * @param bytes byte array
     * @return integer value representation
     */
    private static int bytesToInt(byte[] bytes) {
        return ((bytes[0] & 0xFF) << 24 | (bytes[1] & 0xFF) << 16 | (bytes[2] & 0xFF) << 8 | (bytes[3] & 0xFF));
    }

    /**
     * Imports a label file form MNIST database
     *
     * @param gzip file stream
     * @return imported labels
     * @throws IOException error while reading file
     */
    private static Vector[] importLabelFile(GZIPInputStream gzip) throws IOException {
        byte[] itemCountBytes = new byte[4];
        gzip.read(itemCountBytes);
        int itemCount = bytesToInt(itemCountBytes);
        System.out.println("item count: " + itemCount);
        Vector[] data = new Vector[itemCount];
        for (int i = 0; i < itemCount; i++) {
            double[] vec = new double[10];
            vec[gzip.read()] = 1.0;
            data[i] = new Vector(vec);
        }
        System.out.println("finished");
        return data;
    }

    /**
     * Imports an image file form MNIST database
     *
     * @param gzip file stream
     * @return imported images
     * @throws IOException error while reading file
     */
    private static Vector[] importImageFile(GZIPInputStream gzip) throws IOException {
        byte[] infoBytes = new byte[4];
        gzip.read(infoBytes);
        int itemCount = bytesToInt(infoBytes);
        gzip.read(infoBytes);
        int rowCount = bytesToInt(infoBytes);
        gzip.read(infoBytes);
        int colCount = bytesToInt(infoBytes);
        System.out.println("item count: " + itemCount);
        System.out.println("row count: " + rowCount);
        System.out.println("col count: " + colCount);
        Vector[] data = new Vector[itemCount];
        int pixelCount = rowCount * colCount;
        for (int i = 0; i < itemCount; i++) {
            double[] vec = new double[pixelCount];
            for (int j = 0; j < pixelCount; j++) {
                vec[j] = gzip.read() / 255.0;
            }
            data[i] = new Vector(vec);
        }
        System.out.println("finished");
        return data;
    }
}
