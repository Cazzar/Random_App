package caydedixon;

import java.util.Arrays;

public class MathUtil {
    public static double dotProduct(double[] a, double[] b) {
        if (a.length != b.length) throw new IllegalArgumentException("a.length != b.length");
        double c = 0;

        for (int i = 0; i < a.length; i++) c += a[i] * b[i];

        return c;
    }

    public static double[][] multiplyMatrix(final double[][] a, double factor) {
        //to avoid mutating the input
        double[][] c = Arrays.copyOf(a, a.length);

        for (int x = 0; x < c.length; x++) {
            for (int y = 0; y < c[x].length; y++) {
                c[x][y] *= factor;
            }
        }
        return c;
    }
}
