/*

2009jul20: usa soma de gaussianas (Fast Gaussian Transform)

*/

package m15;

import m00.*;
import m01.*;

import java.util.*;
import java.awt.image.*;

///////////////////////////////////////////////////////////////////////////////
public class InputGraph15 extends Graph {
    protected ReducedWshed opWshed;
    protected BufferedImage im;
    protected float[][] rgb;
    protected int w, h;
    protected int totr; //total de regioes do watershed
    protected Consts cts = new Consts();
    protected Ferramentas1 fe = new Ferramentas1();
    protected double[] pfgt = null;
    
private Region[] reg = null;

//2010ago03: teste MSRM (Ning et al. PR2010)
    public BufferedImage getWshedPartition() {
        return this.opWshed.calcWshedImagePartition();
    }
    

    public double getPFGT(int id) {
        return this.pfgt[id];
    }

    public float[] getRGB(int id) {
        return this.rgb[id];
    }
    
    public BufferedImage getImRGB() {
        return this.im;
    }

    public BufferedImage getImWshed() {
        return this.opWshed.getImWshed();
    }

    public int getWidth() {
        return this.w;
    }

    public int getHeight() {
        return this.h;
    }

    public InputGraph15() {
        super();
    }
    
    public InputGraph15(String fn, int minsize) {
        super();
        BufferedImage im = Ferramentas.loadImage(fn);
        init(im, minsize);        
    }
    public InputGraph15(BufferedImage im, int minsize) {
        super();
        init(im, minsize);
    }
    public void init(BufferedImage im, int minsize) {
System.out.println("Pre-processing input image to reduce the number of regions. Please wait...");
        this.opWshed = new ReducedWshed(im, minsize);
        this.totr = this.opWshed.getTotalRegions(); //total de regioes
        commonInit(im);
    }
    public void commonInit(BufferedImage im) {
        //se imagem grayscale, replica para tres canais RGB
        Conversion opConv = new Conversion(im, true);
        BufferedImage imRGB = opConv.imgConverted();
        this.im = imRGB;
        this.w = im.getWidth();
        this.h = im.getHeight();
    }
    
/////////////////////////////////////////////////////
    public ReducedWshed getOpWshed() {
        return this.opWshed;
    }
    
    //constantes de normalizacao para custo em GraphMatch2.java
    public float getDmax() {
        float dmax = (float)Ferramentas.euclideanDistance(0.0,0.0,this.w,this.h);//sqrt(w^2 + h^2)
        return dmax;
    }

    //inclui todas as regioes no grafo
    public void buildVertices() {
        boolean[] incluir = new boolean[this.totr];
        for (int i = 0; i < this.totr; i++)
            incluir[i] = true;
        this.reg = this.buildVertices(incluir);
    }
    public Region[] buildVertices(boolean[] incluir) {
        int[] idr = new int[this.totr];
        int[][] mtLab = this.opWshed.getRefLabels();
        Region[] refr = new Region[this.totr];
        int tot = 0;
        for (int i = 0; i < this.totr; i++)
            if (incluir[i] == true) {
                Region r = new Region();
                refr[i] = r;
                idr[i] = tot;
                tot++;
            }
            else {
                refr[i] = null;
                idr[i] = -1;
            }
        this.rgb = new float[tot][3];
        for (int i = 0; i < tot; i++)
            for (int c = 0; c < 3; c++)
                this.rgb[i][c] = 0.0f;
        WritableRaster inp = this.im.copyData(null);
        for (int x=0; x<this.w; x++)
            for (int y=0; y<this.h; y++) {
                int i = mtLab[x][y];
                if (i >= 0 && incluir[i] == true) {
                    Pixel p = new Pixel(x, y, 0); //usarei nivel medio de cada canal RGB
                    Region r = refr[i];
                    r.addPixel(p);
                    for (int c = 0; c < 3; c++) {
                        float g = (float)inp.getSample(x,y,c);
                        rgb[idr[i]][c] += g;
                    }
                }
            }
        for (int i = 0; i < this.totr; i++)
            if (incluir[i] == true) {
                Region r = refr[i];
                this.addVertex(r); //atualiza centroides...
            }
        this.finalizaConstrucaoVertices(); // agora posso atualizar atributos de cores RGB...
        Vertex[] refv = this.getVertices();
        for (int i = 0; i < this.totr; i++)
            if (incluir[i] == true) {
                Vertex v = refv[idr[i]];
                Region r = refr[i];
                for (int c = 0; c < 3; c++) {
                    int totpixels = r.getArea();
                    this.rgb[idr[i]][c] /= totpixels;
                }
                //2008jul01: converte rgb para cielab
                //this.rgb[idr[i]] = fe.rgb2cielab(this.rgb[idr[i]]);
            }
        //System.out.println("Grafo com cielab.");
        return refr;
    }

    public void computeVerticesProbabilities(fgt kernelBg, fgt kernelFg) {
        // 1. compute target matrix: 3 x totpixels
        int totv = this.getNumVertices();
        double[][] t = new double[3][totv]; // pontos por coluna
        for (int i = 0; i < totv; i++) {
            for (int c = 0; c < 3; c++)
                t[c][i] = (double) this.rgb[i][c];
        }
        this.convertsLAB01(t);
        // 2. compute weights
        double[] vbg = kernelBg.computeNormalizedWeights(t);
        double[] vfg = kernelFg.computeNormalizedWeights(t);

        double[] p = new double[totv];
        double[] pf = vfg;
        double[] pb = vbg;
        for (int i = 0; i < totv; i++) {
            p[i] = 0.0;
            if (pf[i] + pb[i] > 0.0)
                p[i] = pf[i] / (pf[i] + pb[i]);
        }
        this.pfgt = p;
        
        //Debug:
        double[][] mp = new double[h][w];
        for (int y = 0; y < h; y++)
            for (int x = 0; x < w; x++)
                mp[y][x] = 0.0;
        for (int i = 0; i < totr; i++) {
            Region r = this.reg[i];
            LinkedList lp = r.getPixels();
            Iterator it = lp.iterator();
            while (it.hasNext()) {
                Pixel pix = (Pixel)it.next();
                int x = pix.getx();
                int y = pix.gety();
                mp[y][x] = this.pfgt[i];
            }
        }
        fe.saveImage(fe.imGray(mp),"zzz-pvi.png");
    }

//java -Xmx1024m segm3 pelo.bmp zres/pelo/zzz-stroke.png
    public void testProbabilities(fgt kernelBg, fgt kernelFg) {
        // 1. compute target matrix: 3 x totpixels
        double[][] t = new double[3][this.h*this.w]; // pontos por coluna
        WritableRaster inp = this.im.getRaster();
        // por linha
        for (int y=0; y<this.h; y++)
            for (int x=0; x<this.w; x++)
                for (int c = 0; c < 3; c++)
                    t[c][y*w+x] = (double) inp.getSample(x,y,c);
        this.convertsLAB01(t);
        // 2. compute weights
        double[] vbg = kernelBg.computeNormalizedWeights(t);
        double[][] mbg = fe.vector2matrixRow(vbg, this.h, this.w);
        double[] vfg = kernelFg.computeNormalizedWeights(t);
        double[][] mfg = fe.vector2matrixRow(vfg, this.h, this.w);

        double[][] p = new double[this.h][this.w];
        double[][] pf = mfg;
        double[][] pb = mbg;
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                p[y][x] = 0.0;
                if (pf[y][x] + pb[y][x] > 0.0)
                    p[y][x] = pf[y][x] / (pf[y][x] + pb[y][x]);
            }
        }
        fe.saveImage(fe.imGray(p),"zzz-p.png");
        fe.saveImage(fe.imGray(pf),"zzz-pf.png");
        fe.saveImage(fe.imGray(pb),"zzz-pb.png");
    }
    protected void convertsLAB01(double[][] s) {
        int totp = s[0].length;
        // converts RGB to CIELAB
        double[] minc = new double[3];
        double[] maxc = new double[3];
        for (int c = 0; c < 3; c++) {
            minc[c] = 9999.9;
            maxc[c] = 0.0;
        }
        for (int i = 0; i < totp; i++) {
            float[] lab = fe.rgb2cielab((int)s[0][i], (int)s[1][i], (int)s[2][i]);
            for (int c = 0; c < 3; c++) {
                s[c][i] = (double)lab[c];
                if (s[c][i] < minc[c])
                    minc[c] = s[c][i];
                if (s[c][i] > maxc[c])
                    maxc[c] = s[c][i];
            }
        }
        // normalizes between 0 and 1
        for (int i = 0; i < totp; i++)
            for (int c = 0; c < 3; c++)
                if ((maxc[c] - minc[c]) > 0.0)
                    s[c][i] = (s[c][i] - minc[c]) / (maxc[c] - minc[c]);
                else
                    s[c][i] = 0.0;
    }

}

