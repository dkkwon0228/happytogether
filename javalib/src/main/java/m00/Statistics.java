/*

2008mar21

*/
package m00;

import java.util.*;

public class Statistics {

    public static float median(float[] v) {
        int tot = v.length;
        return median(v, tot);
    }
    public static float median(float[] v, int tot) {
        Arrays.sort(v);
        int i = tot / 2;
        if (tot > i * 2)
            i++;
        float m = v[i-1]; //indice comeca do zero
        return m;
    }
    
    public static float mean(float[] v) {
        int tot = v.length;
        return mean(v,tot);
    }
    public static float mean(float[] v, int tot) {
        float sum = 0.0f;
        for (int i = 0; i < tot; i++)
            sum += v[i];
        float m = sum / (float) tot;
        return m;
    }
    
    public static float variance(float[] v) {
        int tot = v.length;
        return variance(v,tot);
    }
    public static float variance(float[] v, int tot) {
        float m = mean(v, tot);
        return variance(v, tot, m);
    }

    public static float variance(float[] x, int n, float mean) {
        // 1/n * sum_{i=1}^n (x_i - mean)^2 = (1/n sum_{i=1}^n x_i^2) - mean^2
        float sum2 = 0.0f;
        for (int i = 0; i < n; i++)
            sum2 += x[i] * x[i];
        float s = sum2 / (float) n   - mean*mean;
        return s;
    }

    public static float stddev(float[] x, int n) {
        float mean = mean(x, n);
        return stddev(x, n, mean);
    }    
    public static float stddev(float[] x, int n, float mean) {
        float vari = variance(x, n, mean);
        return (float)Math.sqrt(vari);
    }    
    
    public static void main (String args[]) {
        int tot = 4;
        float[] v = new float[tot];
        v[0] = 1;
        v[1] = 3;
        v[2] = 5;
        v[3] = 11;
        for (int i = 0; i < tot; i++)
            System.out.println(v[i]+"; ");
        float mean = Statistics.mean(v,tot);
        System.out.println("mean = "+mean);
        float median = Statistics.median(v,tot);
        System.out.println("median = "+median);
        float std = Statistics.stddev(v,tot);
        System.out.println("standard deviation = "+std);
    }

}
