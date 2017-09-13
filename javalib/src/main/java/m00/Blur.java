/*
 * Blur.java
 *
 * Created on 3 de Janeiro de 2005, 11:29
 */

/**
 *
 * @author  Consularo
 */

package m00;
 
import java.awt.image.*;

public class Blur {

    private BufferedImage inpImg;
    private BufferedImage oupImg;
    private double[][] mtGray;
    private double[][] mtBlur;
    private int w, h;
    private Consts cts = new Consts();
    
    public Blur(BufferedImage inp, int nB) {
        int i, j, k, ik, jk;
        double pixC;

        inpImg = inp;
        w = inpImg.getWidth();
        h = inpImg.getHeight();
        
        WritableRaster input = inpImg.copyData(null);
        mtGray = new double[w][h];
        for (i=0; i < w; i++)
            for (j=0; j < h; j++) {
                   mtGray[i][j] = (double)input.getSample(i, j, 0);
            }
        mtBlur = new double[w][h];
        for (int n=1; n<=nB; n++) {
            for (i=0; i < w; i++)
                for (j=0; j < h; j++) {
                    pixC = 0.1592 * mtGray[i][j];
                    for (k=0; k<8; k++) {
                        ik = i + cts.viz8x[k];
                        jk = j + cts.viz8y[k];
                        if ((ik >= 0) && (jk >= 0) && (ik < w) && (jk < h)) {
                            pixC += cts.mskGss[k] * mtGray[ik][jk];
                        }
                    }
                    mtBlur[i][j] = Math.floor(pixC);
                }
            
            for (i=0; i < w; i++)
                for (j=0; j < h; j++) {
                    mtGray[i][j] = mtBlur[i][j];
                }
        }
    }

    public Blur(double[][] mtGray, int w, int h, int nB) {
        int i, j, k, ik, jk;
        double pixC;

        this.w = w;
        this.h = h;
        inpImg = new BufferedImage(w, h, BufferedImage.TYPE_BYTE_GRAY);
        
        this.mtGray = mtGray;
        WritableRaster input = inpImg.getRaster();
        for (i=0; i < w; i++)
            for (j=0; j < h; j++) {
                   input.setSample(i, j, 0, (int)(Math.floor(mtGray[i][j])));
            }

        mtBlur = new double[w][h];
        for (int n=1; n<=nB; n++) {
            for (i=0; i < w; i++)
                for (j=0; j < h; j++) {
                    pixC = 0.1592 * mtGray[i][j];
                    for (k=0; k<8; k++) {
                        ik = i + cts.viz8x[k];
                        jk = j - cts.viz8y[k];
                        if ((ik >= 0) && (jk >= 0) && (ik < w) && (jk < h)) {
                            pixC += cts.mskGss[k] * mtGray[ik][jk];
                        }
                    }
                    mtBlur[i][j] = Math.floor(pixC);
                }
            
            for (i=0; i < w; i++)
                for (j=0; j < h; j++) {
                    mtGray[i][j] = mtBlur[i][j];
                }
        }
    }

    public BufferedImage show() {

        oupImg = new BufferedImage(w, h, inpImg.getType());
        WritableRaster output = oupImg.getRaster();
        for (int i=0; i < w; i++)
            for (int j=0; j < h; j++) 
                output.setSample(i, j, 0, (int)Math.floor(mtBlur[i][j]));
        return(oupImg);
    }
    
    public void Normalize() {
        double mn = mtBlur[0][0];
        double mx = mn;
        for (int i=0; i < w; i++)
            for (int j=0; j < h; j++) {
                mn = Math.min(mn, mtBlur[i][j]);
                mx = Math.max(mx, mtBlur[i][j]);
            }

        for (int i=0; i < w; i++)
            for (int j=0; j < h; j++) 
                mtBlur[i][j] = (int)Math.floor(255*(mtBlur[i][j]-mn)/(mx-mn));
    }
    
    public BufferedImage showNormalized() {

        double mn = mtBlur[0][0];
        double mx = mn;
        for (int i=0; i < w; i++)
            for (int j=0; j < h; j++) {
                mn = Math.min(mn, mtBlur[i][j]);
                mx = Math.max(mx, mtBlur[i][j]);
            }

        oupImg = new BufferedImage(w, h, inpImg.getType());
        WritableRaster output = oupImg.getRaster();
        for (int i=0; i < w; i++)
            for (int j=0; j < h; j++) 
                output.setSample(i, j, 0, (int)Math.floor(255*(mtBlur[i][j]-mn)/(mx-mn)));
        return(oupImg);
    }

    public double[][] getBlur() {
        return(mtBlur);
    }
}

