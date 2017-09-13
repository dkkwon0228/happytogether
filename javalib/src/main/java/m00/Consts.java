/*

2008mar18

*/

package m00;

import java.awt.*;

public class Consts {
    final public int[] mskX = {-1,  0,  1,  2,  1,  0, -1, -2};
    final public int[] mskY = { 1,  2,  1,  0, -1, -2, -1,  0};
    final public int[] viz8x   = {-1,  0,  1,  1,  1,  0, -1, -1};
    final public int[] viz8y   = {-1, -1, -1,  0, +1, +1, +1,  0};
    final public int[] viz4x   = {0,  1,  0,  -1};
    final public int[] viz4y   = {-1,  0,  1,  0};
    final public double[] mskGss = {0.0585, 0.0965, 0.0585, 0.0965, 0.0585, 0.0965, 0.0585, 0.0965};
    final public double[] dst8  = {1.0, 1.414213562, 1.0, 1.414213562, 1.0, 1.414213562, 1.0, 1.414213562};
    final public Color corTransp = Color.white;
    final public float INF = 10000000000.0f;
}
