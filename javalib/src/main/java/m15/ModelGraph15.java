/*

2009jul20: binary segmentation

.calculates Kernel via Fast Gaussian Transform.

*/

package m15;

import m00.*;

import java.util.*;
import java.awt.image.*;
import java.awt.*;


///////////////////////////////////////////////////////////////////////////////
public class ModelGraph15 extends InputGraph15 {
    private BufferedImage strk;
    private int totlabels;
    private boolean[] incluir;
    private Color[] bgfg; //background:0, foreground:1
    private int[] cor;
    private int[] cor_todos;
    private Consts cts = new Consts();
    private fgt kernelFg = null;
    private fgt kernelBg = null;
    
    public int BgORFg(int vm_id) {
        return this.cor[vm_id];
    }

    public fgt getKernelFg() {
        return this.kernelFg;
    }
    
    public fgt getKernelBg() {
        return this.kernelBg;
    }
    
    public ModelGraph15() {
        super();
    }

    public BufferedImage getStrokeImage() {
        return this.strk;
    }
    
    public ModelGraph15(BufferedImage strk, ReducedWshed opWshed) {
        super();
        this.opWshed = opWshed;
        this.totr = this.opWshed.getTotalRegions(); //total de regioes
        this.im = this.opWshed.getImRGB();
        this.w = im.getWidth();
        this.h = im.getHeight();

        //inicializa cores: fg e bg
        this.bgfg = new Color[2];
        this.bgfg[0] = Color.blue; //background
        this.bgfg[1] = Color.red;  //foreground

        this.calcStrokes(strk);
    }
    
    public Color getColor(int vmid) {
        return bgfg[cor[vmid]];
    }
    
    private void calcStrokes(String fnstrk) {
        BufferedImage strk = Ferramentas.loadImage(fnstrk);
        this.calcStrokes(strk);
    }
    private void calcStrokes(BufferedImage strk) {
        this.strk = strk;
        WritableRaster inp2 = this.strk.copyData(null);
        this.incluir = new boolean[this.totr];
        Color[] cor_aux = new Color[this.totr];
        for (int i = 0; i < this.totr; i++) {
            this.incluir[i] = false;
            cor_aux[i] = null;
        }
        int[][] mtLab = this.opWshed.getRefLabels();
        int[][] mtLab2 = this.opWshed.getRefLabelsBeforeVanishWatershed();
        for (int x=0; x<this.w; x++)
            for (int y=0; y<this.h; y++) {
                if(inp2.getSample(x,y,0) != cts.corTransp.getRed() || 
                   inp2.getSample(x,y,1) != cts.corTransp.getGreen() || 
                   inp2.getSample(x,y,2) != cts.corTransp.getBlue()) {
                    int i = mtLab[x][y];
                    int j = mtLab2[x][y];//wshedlines
                    if (i >= 0 && !incluir[i] && j >= 0) {
                        incluir[i] = true;
                        cor_aux[i] = new Color(this.strk.getRGB(x, y));
                    }
                }
            }
        //suponho que a criacao dos vertices obedece a sequencia:
        int totvm = 0;
        for (int i = 0; i < this.totr; i++)
            if (incluir[i])
                totvm++;
        this.cor = new int[totvm];
        this.cor_todos = new int[totr];
        int ii = 0;
        for (int i = 0; i < this.totr; i++) {
            cor_todos[i] = -1;
            if (incluir[i]) {
                this.cor[ii] = 0;
                if (fe.sameRGB(cor_aux[i],bgfg[1]))
                    this.cor[ii] = 1;
                cor_todos[i] = cor[ii];
                ii++;
            }
        }
    }

    public void buildModelVerticesAndEdges() {
        Region[] refr = this.buildVertices(this.incluir);
        /*
        //calculates Kernel using FGT
        //double[][] sbg = this.calcFGTSourceMatrixStrokes(0); 
        double[][] sbg = this.calcFGTSourceMatrixRegions(refr, 0);
        this.kernelBg = this.calcFGTKernel(sbg);
        //double[][] sfg = this.calcFGTSourceMatrixStrokes(1); 
        double[][] sfg = this.calcFGTSourceMatrixRegions(refr, 1);
        this.kernelFg = this.calcFGTKernel(sfg);
        */
        this.buildEdgesDelaunayTriangulation();
    }

    private fgt calcFGTKernel(double[][] s) {
        int totlin = s.length;
        int totcol = s[0].length;
        double h = 0.3;//0.5;
        int K = 20;//10;
        double e = 10.0;
        int ds = totlin;
        int ns = totcol;
        //pesos: tudo 1
        double[] u = new double[ns];
        for (int i = 0; i < ns; i++)
            u[i] = 1.0;
        int p = 5;//10;
        fgt pFGT = new fgt(ds, u, s, ns, h, p, K, e);
        return pFGT;
    }

    // source matrix (LAB color, normalized 0..1)
    private double[][] calcFGTSourceMatrixStrokes(int type) {
        WritableRaster inpstrk = this.strk.getRaster();
        int w = this.strk.getWidth();
        int h = this.strk.getHeight();
        LinkedList lp = new LinkedList();
        for (int y = 0; y < h; y++)
            for (int x = 0; x < w; x++) {
                int r = inpstrk.getSample(x,y,0);
                int g = inpstrk.getSample(x,y,1);
                int b = inpstrk.getSample(x,y,2);
                Color c1 = new Color(r,g,b);
                if (fe.sameRGB(c1, bgfg[type]))
                    lp.addLast(new Pixel(x,y,0));
            }
        int totp = lp.size();
        // points per column: 3 x totalPoints
        double[][] s = new double[3][totp]; //source matrix
        Iterator it = lp.iterator();
        WritableRaster inp = this.im.getRaster();
        for (int i = 0; i < totp; i++) {
            Pixel p = (Pixel) it.next();
            int x = p.getx();
            int y = p.gety();
            for (int c = 0; c < 3; c++)
                s[c][i] = (double)inp.getSample(x,y,c);
        }
        this.convertsLAB01(s);
        return s;
    }
    //type: 0:background; 1:foreground
    private double[][] calcFGTSourceMatrixRegions(Region[] refr, int type) {
        LinkedList lp = new LinkedList();
        for (int i = 0; i < this.totr; i++)
            if (incluir[i] && cor_todos[i] == type) {
                Region r = refr[i];
                lp.addAll(r.getPixels());
            }
        int totp = lp.size();
        // points per column: 3 x totalPoints
        double[][] s = new double[3][totp]; //source matrix
        Iterator it = lp.iterator();
        WritableRaster inp = this.im.getRaster();
        for (int i = 0; i < totp; i++) {
            Pixel p = (Pixel) it.next();
            int x = p.getx();
            int y = p.gety();
            for (int c = 0; c < 3; c++)
                s[c][i] = (double)inp.getSample(x,y,c);
        }
        this.convertsLAB01(s);
        return s;
    }

}    
