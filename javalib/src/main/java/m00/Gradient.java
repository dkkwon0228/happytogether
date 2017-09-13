/*
 * Gradient.java
 *
 * Created on 3 de Janeiro de 2005, 11:04
 */

/**
 *
 * @author  Consularo
 */

package m00;

import java.awt.image.*;

public class Gradient {
    private BufferedImage inpImg;
    private BufferedImage oupImg;
    private double[][] mtGray;
    private double[][] mtX;
    private double[][] mtY;
    private double[][] mtEo;
    private double[][] mtEs;
    private int w, h;
    private Consts cts = new Consts();
    
    /** Creates a new instance of Gradient */
    public Gradient(BufferedImage inp) {
        
        int i, j, k, ik, jk;
        double pixCx, pixCy;
        
        inpImg = inp;
        w = inpImg.getWidth();
        h = inpImg.getHeight();
        
        WritableRaster input = inpImg.copyData(null);
        mtGray = new double[w][h];
        for (i=0; i < w; i++)
            for (j=0; j < h; j++) {
                   mtGray[i][j] = (double)input.getSample(i, j, 0);
            }
        mtX = new double[w][h];
        mtY = new double[w][h];
        for (i=0; i < w; i++)
            for (j=0; j < h; j++) {
                pixCx = 0; pixCy = 0;
                for (k=0; k<8; k++) {
                    ik = i + cts.viz8x[k];
                    jk = j + cts.viz8y[k];
                    if ((ik >= 0) && (jk >= 0) && (ik < w) && (jk < h)) {
                        pixCx += cts.mskX[k] * mtGray[ik][jk]; 
                        pixCy += cts.mskY[k] * mtGray[ik][jk];
                    }
                }
                mtX[i][j] = pixCx;
                mtY[i][j] = pixCy;
            }
        
        mtEs = new double[w][h];
        mtEo = new double[w][h];
        
        for (i=0; i < w; i++)
            for (j=0; j < h; j++) {
                mtEs[i][j] = Math.sqrt(Math.pow(mtX[i][j], 2.0) + Math.pow(mtY[i][j], 2.0));
                mtEo[i][j] = Math.atan2(mtY[i][j], mtX[i][j]);
            }
    }    

    public Gradient(double[][] mtGray, int w, int h) {
        
        int i, j, k, ik, jk;
        double pixCx, pixCy;
        
        this.w = w;
        this.h = h;
        inpImg = new BufferedImage(w, h, BufferedImage.TYPE_BYTE_GRAY);
        
        this.mtGray = mtGray;
        WritableRaster input = inpImg.getRaster();
        for (i=0; i < w; i++)
            for (j=0; j < h; j++) {
                   input.setSample(i, j, 0, (int)(Math.floor(mtGray[i][j])));
            }
        mtX = new double[w][h];
        mtY = new double[w][h];
        for (i=0; i < w; i++)
            for (j=0; j < h; j++) {
                pixCx = 0; pixCy = 0;
                for (k=0; k<8; k++) {
                    ik = i + cts.viz8x[k];
                    jk = j - cts.viz8y[k];
                    if ((ik >= 0) && (jk >= 0) && (ik < w) && (jk < h)) {
                        pixCx += cts.mskX[k] * mtGray[ik][jk]; 
                        pixCy += cts.mskY[k] * mtGray[ik][jk];
                    }
                }
                mtX[i][j] = pixCx;
                mtY[i][j] = pixCy;
            }
        
        mtEs = new double[w][h];
        mtEo = new double[w][h];
        
        for (i=0; i < w; i++)
            for (j=0; j < h; j++) {
                mtEs[i][j] = Math.sqrt(Math.pow(mtX[i][j], 2.0) + Math.pow(mtY[i][j], 2.0));
                mtEo[i][j] = Math.atan2(mtY[i][j], mtX[i][j]);
            }
    }    
    
    public BufferedImage showModule() {

        oupImg = new BufferedImage(w, h, inpImg.getType());
        WritableRaster output = oupImg.getRaster();
        for (int i=0; i < w; i++)
            for (int j=0; j < h; j++) 
                output.setSample(i, j, 0, (int)Math.floor(mtEs[i][j]));
        return(oupImg);
    }

    public BufferedImage showPhasis() {

        oupImg = new BufferedImage(w, h, inpImg.getType());
        WritableRaster output = oupImg.getRaster();
        for (int i=0; i < w; i++)
            for (int j=0; j < h; j++) 
                output.setSample(i, j, 0, (int)Math.floor(mtEo[i][j]));
        return(oupImg);
    }
    
    public BufferedImage showModuleNormalized() {

        double mn = mtEs[0][0];
        double mx = mn;
        for (int i=0; i < w; i++)
            for (int j=0; j < h; j++) {
                mn = Math.min(mn, mtEs[i][j]);
                mx = Math.max(mx, mtEs[i][j]);
            }

        oupImg = new BufferedImage(w, h, inpImg.getType());
        WritableRaster output = oupImg.getRaster();
        for (int i=0; i < w; i++)
            for (int j=0; j < h; j++) 
                output.setSample(i, j, 0, (int)Math.floor(255*(mtEs[i][j]-mn)/(mx-mn)));
        return(oupImg);
    }

    public BufferedImage showPhasisNormalized() {

        double mn = mtEo[0][0];
        double mx = mn;
        for (int i=0; i < w; i++)
            for (int j=0; j < h; j++) {
                mn = Math.min(mn, mtEo[i][j]);
                mx = Math.max(mx, mtEo[i][j]);
            }

        oupImg = new BufferedImage(w, h, inpImg.getType());
        WritableRaster output = oupImg.getRaster();
        for (int i=0; i < w; i++)
            for (int j=0; j < h; j++) 
                output.setSample(i, j, 0, (int)Math.floor(255*(mtEo[i][j]-mn)/(mx-mn)));
        return(oupImg);
    }

    public double[][] getModuleNormalized() {

        double mn = mtEs[0][0];
        double mx = mn;
        for (int i=0; i < w; i++)
            for (int j=0; j < h; j++) {
                mn = Math.min(mn, mtEs[i][j]);
                mx = Math.max(mx, mtEs[i][j]);
            }
        for (int i=0; i < w; i++)
            for (int j=0; j < h; j++) 
                mtEs[i][j] = Math.floor(255*(mtEs[i][j]-mn)/(mx-mn));
        return(mtEs);
    }

    public double[][] getModuleNormalizedAndInverted() {

        double mn = mtEs[0][0];
        double mx = mn;
        for (int i=0; i < w; i++)
            for (int j=0; j < h; j++) {
                mn = Math.min(mn, mtEs[i][j]);
                mx = Math.max(mx, mtEs[i][j]);
            }
        for (int i=0; i < w; i++)
            for (int j=0; j < h; j++) 
                mtEs[i][j] = 255 - Math.floor(255*(mtEs[i][j]-mn)/(mx-mn));
        return(mtEs);
    }
    
    public double[][] getModule() {
        return(mtEs);
    }

    public double[][] getPhasis() {
        return(mtEo);
    }
}
 