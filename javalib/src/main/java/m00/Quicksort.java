
/*

2009nov04: ordena valores e indices ao mesmo tempo

*/

package m00;

public class Quicksort {

    private float[] val;
    private int[] ind;

    public Quicksort(int[] values) {
        int tot = values.length;
        float[] valuesf = new float[tot];
        for (int i = 0; i < tot; i++)
            valuesf[i] = (float) values[i];
        this.executa(valuesf);
        for (int i = 0; i < tot; i++)
            values[i] = (int) valuesf[i];
    }
    
    public Quicksort(float[] values) {
        this.executa(values);
    }
    
    public void executa(float[] values) {
        int tot = values.length;
        int[] index = new int[tot];
        for (int i = 0; i < tot; i++)
            index[i] = i;
        quickQuicksort(values, index);
        this.val = values;
        this.ind = index;
    }

    public float[] getValues() {
        return this.val;
    }

    public int[] getIndices() {
        return this.ind;
    }

public static void quickQuicksort(float[] main, int[] index) {
    quickQuicksort(main, index, 0, index.length - 1);
}

// quickQuicksort a[left] to a[right]
public static void quickQuicksort(float[] a, int[] index, int left, int right) {
    if (right <= left) return;
    int i = partition(a, index, left, right);
    quickQuicksort(a, index, left, i-1);
    quickQuicksort(a, index, i+1, right);
}

// partition a[left] to a[right], assumes left < right
private static int partition(float[] a, int[] index, 
int left, int right) {
    int i = left - 1;
    int j = right;
    while (true) {
        while (less(a[++i], a[right]))      // find item on left to swap
            ;                               // a[right] acts as sentinel
        while (less(a[right], a[--j]))      // find item on right to swap
            if (j == left) break;           // don't go out-of-bounds
        if (i >= j) break;                  // check if pointers cross
        exch(a, index, i, j);               // swap two elements into place
    }
    exch(a, index, i, right);               // swap with partition element
    return i;
}

// is x < y ?
private static boolean less(float x, float y) {
    return (x < y);
}

// exchange a[i] and a[j]
private static void exch(float[] a, int[] index, int i, int j) {
    float swap = a[i];
    a[i] = a[j];
    a[j] = swap;
    int b = index[i];
    index[i] = index[j];
    index[j] = b;
}

    public static void main( String args[] ) {
        float[] values = {5.5f, 1.1f, 2.2f, 4.4f, -9.9f, 3.3f, 6.6f, 8.8f, 7.7f};
        int tot = values.length;
        System.out.println();
        for (int i = 0; i < tot; i++)
            System.out.print(values[i]+", ");
        Quicksort s = new Quicksort(values);
        System.out.println();
        for (int i = 0; i < tot; i++)
            System.out.print(values[i]+", ");
        System.out.println();

        int[] ind = s.getIndices();
        for (int i = 0; i < tot; i++)
            System.out.print(ind[i]+", ");
        System.out.println();
    }
}
