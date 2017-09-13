/*
 2008set22: inclui grade para background (japanese)...
 */


package m01.tmp;

import m00.*;

import java.awt.*;
import java.util.*;
import java.awt.image.*;

import m01.InputGraph1;
import m01.ModelGraph1;
import utils.*;

////////////////////////////////////////////////////////////////////////////
public class Ferramentas1 extends Ferramentas {

    public BufferedImage edgeAlphaBlur(BufferedImage iminp, BufferedImage immsk, int blur) {
        int w = iminp.getWidth();
        int h = iminp.getHeight();

        //salva mascara
        double[][] msk = new double[w][h];
        WritableRaster inp = immsk.getAlphaRaster();
        for (int x = 0; x < w; x++)
            for (int y = 0; y < h; y++) {
                int g = inp.getSample(x,y,0);
                msk[x][y] = 0;
                if (g > 0)
                    msk[x][y] = 255;
            }

        Blur opBlur = new Blur(msk, w, h, blur);
        opBlur.Normalize();
        double[][] msk2 = opBlur.getBlur();
        
        inp = iminp.getRaster();
        BufferedImage res = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        WritableRaster oup = res.getRaster();
        WritableRaster oupa = res.getAlphaRaster();
        for (int x = 0; x < w; x++)
            for (int y = 0; y < h; y++) {
                for (int c = 0; c < 3; c++)
                    oup.setSample(x,y,c, inp.getSample(x,y,c));
                int g = (int)msk2[x][y];
                oupa.setSample(x,y,0,g);
            }
        return res;
    }

    public void heuristicConexidadeStrk(int[][] msk2, BufferedImage imstrk) {
        // agrupa regioes de acordo com 'conectividade com tracos'
        int[] tmp = new int[1];
        int[][] comps = this.rotulaComponentes(msk2, tmp);
        int totcomps = tmp[0];
        boolean[] comTracos = new boolean[totcomps];
        int[] viz = new int[totcomps];
        WritableRaster inp = imstrk.getRaster();
        for (int i = 0; i < totcomps; i++) {
            comTracos[i] = false;
            viz[i] = -1; // armazena '1 regiao adjacente'
        }
        int w = imstrk.getWidth();
        int h = imstrk.getHeight();
        for (int x = 0; x < w; x++)
            for (int y = 0; y < h; y++) {
                int idcomp = comps[x][y];
                // sem traco eh cor branca
                if (!this.sameRGB(inp, x, y, Color.white))
                    comTracos[idcomp] = true;
                    if (viz[idcomp] < 0) // vazio
                        for (int k=0; k<4; k++) {
                            int x2 = x + cts.viz4x[k];
                            int y2 = y + cts.viz4y[k];
                            if (x2 >= 0 && x2 < w && y2 >= 0 && y2 < h) {
                                int idviz = comps[x2][y2];
                                if (idcomp != idviz && comTracos[idviz]) {
                                    viz[idcomp] = msk2[x2][y2];
                                    break;
                                }
                             }
                        }
            }
        boolean existeRegiaoDesconexa = true;
        while (existeRegiaoDesconexa) {
            for (int x = 0; x < w; x++)
                for (int y = 0; y < h; y++) {
                    int idcomp = comps[x][y];
                    if (viz[idcomp] < 0) // vazio
                        for (int k=0; k<4; k++) {
                            int x2 = x + cts.viz4x[k];
                            int y2 = y + cts.viz4y[k];
                            if (x2 >= 0 && x2 < w && y2 >= 0 && y2 < h) {
                                int idviz = comps[x2][y2];
                                if (idcomp != idviz && comTracos[idviz]) {
                                    viz[idcomp] = msk2[x2][y2];
                                    break;
                                }
                             }
                        }
                }
            // elimina regioes sem tracos, juntando com um de seus vizinhos
            for (int x = 0; x < w; x++)
                for (int y = 0; y < h; y++) {
                    int idcomp = comps[x][y];
                    if (!comTracos[idcomp] && viz[idcomp] >= 0)
                        msk2[x][y] = viz[idcomp];
                }
            for (int x = 0; x < w; x++)
                for (int y = 0; y < h; y++) {
                    int idcomp = comps[x][y];
                    if (!comTracos[idcomp] && viz[idcomp] >= 0)
                        comTracos[idcomp] = true;
                }
            existeRegiaoDesconexa = false;
            for (int i = 0; i < totcomps; i++)
                if (!comTracos[i])
                    existeRegiaoDesconexa = true;
        }
    }

    public void heuristicPixelLevel(int[][] msk, BufferedImage iminp, int iters, int raio) {
        int w = iminp.getWidth();
        int h = iminp.getHeight();
        for (int i = 0; i < iters; i++) {
            //calcula lista dos pixels na borda interna (obj) e externa (fundo)
            LinkedList borda = new LinkedList();
            for (int x = 0; x < w; x++)
                for (int y = 0; y < h; y++) {
                    for (int k=0; k<8; k++) {
                        int x2 = x + cts.viz8x[k];
                        int y2 = y + cts.viz8y[k];
                            if (x2 >= 0 && x2 < w && y2 >= 0 && y2 < h && msk[x][y] != msk[x2][y2]) {
                                borda.addLast(new Coords2d(x,y));
                                break;
                            }
                    }
                }
            
            // heuristica: visita pontos no circulo
            //int raio = 5;//20;//10;//5;
            double perc = 0.01;
            double maxr = (double) raio + perc * (double) raio;
            double minr = (double) raio - perc * (double) raio;
            LinkedList viz = new LinkedList();
            for (int x = -raio; x <= raio; x++)
                for (int y = -raio; y <= raio; y++) {
                    double d = this.euclideanDistance(0, 0, x, y);
                    if (d >= minr && d <= maxr)
                        viz.addLast(new Coords2d(x,y));
                }
            
            // atribui rotulo do vizinho mais proximo em termos de aparencia
            int totMudancas = 0;
            WritableRaster inp = iminp.getRaster();
            Iterator itb = borda.iterator();
            while (itb.hasNext()) {
                Coords2d c = (Coords2d)itb.next();
                int x = c.getx();
                int y = c.gety();
                float[] rgb = new float[3];
                for (int ii = 0; ii < 3; ii++)
                    rgb[ii] = (float)inp.getSample(x,y,ii);
                double mincost = 1000;
                Coords2d minc = new Coords2d(x,y);
                // visita vizinhos no circulo
                Iterator itv = viz.iterator();
                while (itv.hasNext()) {
                    Coords2d c2 = (Coords2d)itv.next();
                    int x2 = x+c2.getx();
                    int y2 = y+c2.gety();
                    if (x2 >= 0 && x2 < w && y2 >= 0 && y2 < h) {
                        float[] rgb2 = new float[3];
                        for (int ii = 0; ii < 3; ii++)
                            rgb2[ii] = (float)inp.getSample(x2,y2,ii);
                        double cc = this.euclideanDistance(this.rgb2cielab(rgb),this.rgb2cielab(rgb2),3);
                        if (cc < mincost) {
                            mincost = cc;
                            minc = new Coords2d(x2,y2);
                        }
                    }
                }
                int x2 = minc.getx();
                int y2 = minc.gety();
                if (msk[x][y] != msk[x2][y2])
                    totMudancas++;
                msk[x][y] = msk[x2][y2];
            }
            if (totMudancas <= borda.size()/20) { //deve mudar mais de 5%
                //System.out.println("Parou em "+(i+1)+" iters.");
                break;
            }
        }
    }

    public BufferedImage heuristicsOnlyConnectivity(BufferedImage iminp, BufferedImage imstrk, 
                                                BufferedImage immap, int iters, int raio) {
        this.forceStroke(imstrk, Color.white, immap);
        int w = iminp.getWidth();
        int h = iminp.getHeight();
        int[] tmp = new int[1];
        int[][] comps = this.rotulaComponentes(immap, tmp);
        int totcomps = tmp[0];
        Color[] cores = this.coresComponentes(immap, comps, totcomps);
        int[][] msk = comps;
        /*
        this.heuristicPixelLevel(msk, iminp, iters, raio);
        BufferedImage newmap = this.calcCompsColorMap(msk, cores);
        */
        BufferedImage newmap = immap;
        comps = this.rotulaComponentes(newmap, tmp);
        totcomps = tmp[0];
        cores = this.coresComponentes(newmap, comps, totcomps);
        // agrupa regioes de acordo com 'conectividade com tracos'
        msk = comps;
        this.heuristicConexidadeStrk(msk, imstrk);
        /*
        // alisa bordas
        //msk = this.filtroConexo(msk);
        float threshold = 0.3f;//0.2f; //0.3f; //0.2f; //0.1f;
//System.out.println("Filtro Conexo Condicional: "+threshold);
        msk = this.filtroConexo(msk, iminp.getRaster(), threshold);//0.1f);
        newmap = this.calcCompsColorMap(msk, cores);
        this.forceStroke(imstrk, Color.white, newmap);
        */
        newmap = this.calcCompsColorMap(msk, cores);
        return newmap;
        
        //return immap;
    }

    public BufferedImage heuristicsImproveSegm(BufferedImage iminp, BufferedImage imstrk, 
                                                BufferedImage immap, int iters, int raio) {
                                                
        this.forceStroke(imstrk, Color.white, immap);
        int w = iminp.getWidth();
        int h = iminp.getHeight();
        int[] tmp = new int[1];
        int[][] comps = this.rotulaComponentes(immap, tmp);
        int totcomps = tmp[0];
        Color[] cores = this.coresComponentes(immap, comps, totcomps);
        int[][] msk = comps;
        this.heuristicPixelLevel(msk, iminp, iters, raio);
        BufferedImage newmap = this.calcCompsColorMap(msk, cores);
        this.forceStroke(imstrk, Color.white, newmap);
        comps = this.rotulaComponentes(newmap, tmp);
        totcomps = tmp[0];
        cores = this.coresComponentes(newmap, comps, totcomps);
        // agrupa regioes de acordo com 'conectividade com tracos'
        msk = comps;
        this.heuristicConexidadeStrk(msk, imstrk);
        // alisa bordas
        //msk = this.filtroConexo(msk);
        float threshold = 0.3f;//0.2f; //0.3f; //0.2f; //0.1f;
//System.out.println("Filtro Conexo Condicional: "+threshold);
        msk = this.filtroConexo(msk, iminp.getRaster(), threshold);//0.1f);
        newmap = this.calcCompsColorMap(msk, cores);
        this.forceStroke(imstrk, Color.white, newmap);
        return newmap;
        
        //return immap;
    }
    public BufferedImage heuristicsImproveSegmMulti(BufferedImage iminp, BufferedImage imstrk, 
                                                BufferedImage immap, int iters, int raio) {
                                                
        this.forceStroke(imstrk, Color.white, immap);
        int w = iminp.getWidth();
        int h = iminp.getHeight();
        int[] tmp = new int[1];
        int[][] comps = this.rotulaComponentes(immap, tmp);
        int totcomps = tmp[0];
        Color[] cores = this.coresComponentes(immap, comps, totcomps);
        int[][] msk = comps;
            /*
        this.heuristicPixelLevel(msk, iminp, iters, raio);
        BufferedImage newmap = this.calcCompsColorMap(msk, cores);
        this.forceStroke(imstrk, Color.white, newmap);
        comps = this.rotulaComponentes(newmap, tmp);
        totcomps = tmp[0];
        cores = this.coresComponentes(newmap, comps, totcomps);
        // agrupa regioes de acordo com 'conectividade com tracos'
        msk = comps;
            */
        this.heuristicConexidadeStrk(msk, imstrk);
        float threshold = 0.3f;//0.2f; //0.3f; //0.2f; //0.1f;
        msk = this.filtroConexo(msk, iminp.getRaster(), threshold);//0.1f);
        BufferedImage newmap = this.calcCompsColorMap(msk, cores);
        this.forceStroke(imstrk, Color.white, newmap);
        return newmap;
        
        //return immap;
    }
    //2009jul24: heuristica antiga... nao usar!
    public BufferedImage heuristicsImproveSegmOld(BufferedImage iminp, BufferedImage imstrk, 
                                                BufferedImage immap, int iters, int raio) {
        this.forceStroke(imstrk, Color.white, immap);
        int w = iminp.getWidth();
        int h = iminp.getHeight();
        int[] tmp = new int[1];
        int[][] comps = this.rotulaComponentes(immap, tmp);
        int totcomps = tmp[0];
        Color[] cores = this.coresComponentes(immap, comps, totcomps);
        //System.out.println("TotComps = "+totcomps);

        int[][] msk = comps;

        this.heuristicPixelLevel(msk, iminp, iters, raio);
        // alisa bordas
        int[][] msk2 = this.filtroConexo(msk);
        // agrupa regioes de acordo com 'conectividade com tracos'
        this.heuristicConexidadeStrk(msk2, imstrk);
        BufferedImage newmap = this.calcCompsColorMap(msk2, cores);
        this.forceStroke(imstrk, Color.white, newmap);
        return newmap;
    }

    public Color[] coresComponentes(BufferedImage imRGB, int[][] comps, int totc) {
        int w = comps.length;
        int h = comps[0].length;
        Color[] cores = new Color[totc];
        for (int i = 0; i < totc; i++)
            cores[i] = null;
        WritableRaster inp = imRGB.copyData(null);
        for (int x=0; x<w; x++)
            for (int y=0; y<h; y++) {
                int r = inp.getSample(x,y,0);
                int g = inp.getSample(x,y,1);
                int b = inp.getSample(x,y,2);
                cores[comps[x][y]] = new Color(r,g,b);
            }
        for (int i = 0; i < totc; i++)
            if (cores[i] == null) {
                System.out.println("Error(fe1.coresComponentes()): cores[i] == null");
                System.exit(1);
            }
        return cores;
    }

    public BufferedImage calcCompsColorMap(int[][] comps, Color[] cores) {
        int w = comps.length;
        int h = comps[0].length;
        BufferedImage res = new BufferedImage(w, h, BufferedImage.TYPE_3BYTE_BGR);
        WritableRaster oup = res.getRaster();
        for (int x = 0; x < w; x++)
            for (int y = 0; y < h; y++) {
                int i = comps[x][y];
                oup.setSample(x, y, 0, cores[i].getRed());
                oup.setSample(x, y, 1, cores[i].getGreen());
                oup.setSample(x, y, 2, cores[i].getBlue());
            }
        return res;
    }

    public void saveSegmParts(BufferedImage imRGB, Color cor, String arqs, int[][] comps, int totc, boolean edgeblur) {
        boolean[][] contorno = this.calcContorno(comps);
        int w = comps.length;
        int h = comps[0].length;
        BufferedImage res = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);//TYPE_3BYTE_BGR);
        Graphics2D g2d = res.createGraphics();
        g2d.setColor(cor);
        WritableRaster inp = imRGB.copyData(null);
        WritableRaster oup = res.getRaster();
        WritableRaster oupA = res.getAlphaRaster();
        for (int i = 0; i < totc; i++) {
            String fout = String.format(arqs, i);
            g2d.fillRect(0,0,w,h);
            this.corTransparente(res, cor);
            for (int x=0; x<w; x++)
                for (int y=0; y<h; y++)
                    if (comps[x][y] == i && !contorno[x][y]) {
                        oup.setSample(x,y,0,inp.getSample(x,y,0));
                        oup.setSample(x,y,1,inp.getSample(x,y,1));
                        oup.setSample(x,y,2,inp.getSample(x,y,2));
                        oupA.setSample(x,y,0,255);
                    }
            if (edgeblur)
                this.saveImage(this.edgeAlphaBlur(imRGB, res, 2), fout);
            else
                this.saveImage(res, fout);
        }
    }

    public int[][] rotulaComponentes(BufferedImage imRGB, int[] totcomps) {
        WritableRaster inp = imRGB.copyData(null);
        int w = imRGB.getWidth();
        int h = imRGB.getHeight();
        int[][] mtLab = new int[w][h];
        for (int x=0; x<w; x++)
            for (int y=0; y<h; y++) {
                int r = inp.getSample(x,y,0);
                int g = inp.getSample(x,y,1);
                int b = inp.getSample(x,y,2);
                int cor = r + g*256 + b*256*256;
                mtLab[x][y] = cor;
            }
        return rotulaComponentes(mtLab, totcomps);
    }
    
    public int[][] rotulaComponentes(int[][] mtLab, int[] totcomps) {
        return this.regionGrowing(mtLab, totcomps);
    }

    public int[][] filtroConexo(int[][] m) {
        LinkedList se = new LinkedList();
        
        //linha vertical
        se.add(new Coords2d(0,2));
        se.add(new Coords2d(0,1));
        se.add(new Coords2d(0,-1));
        se.add(new Coords2d(0,-2));
        m = this.supinf(m, se);
        m = this.infsup(m, se);

        //linha horizontal
        se.clear();
        se.add(new Coords2d(2,0));
        se.add(new Coords2d(1,0));
        se.add(new Coords2d(-1,0));
        se.add(new Coords2d(-2,0));
        m = this.supinf(m, se);
        m = this.infsup(m, se);
        
        //linha diagonal
        se.clear();
        se.add(new Coords2d(2,2));
        se.add(new Coords2d(1,1));
        se.add(new Coords2d(-1,-1));
        se.add(new Coords2d(-2,-2));
        m = this.supinf(m, se);
        m = this.infsup(m, se);

        //linha diagonal2
        se.clear();
        se.add(new Coords2d(-2,2));
        se.add(new Coords2d(-1,1));
        se.add(new Coords2d(1,-1));
        se.add(new Coords2d(2,-2));
        m = this.supinf(m, se);
        m = this.infsup(m, se);

        return m;
    }
    public int[][] filtroConexo(int[][] m, WritableRaster inp, float limiar) {
        LinkedList se = new LinkedList();
        
        //linha vertical
        se.add(new Coords2d(0,2));
        se.add(new Coords2d(0,1));
        se.add(new Coords2d(0,-1));
        se.add(new Coords2d(0,-2));
        m = this.supinf(m, se, inp, limiar);
        m = this.infsup(m, se, inp, limiar);

        //linha horizontal
        se.clear();
        se.add(new Coords2d(2,0));
        se.add(new Coords2d(1,0));
        se.add(new Coords2d(-1,0));
        se.add(new Coords2d(-2,0));
        m = this.supinf(m, se, inp, limiar);
        m = this.infsup(m, se, inp, limiar);
        
        //linha diagonal
        se.clear();
        se.add(new Coords2d(2,2));
        se.add(new Coords2d(1,1));
        se.add(new Coords2d(-1,-1));
        se.add(new Coords2d(-2,-2));
        m = this.supinf(m, se, inp, limiar);
        m = this.infsup(m, se, inp, limiar);

        //linha diagonal2
        se.clear();
        se.add(new Coords2d(-2,2));
        se.add(new Coords2d(-1,1));
        se.add(new Coords2d(1,-1));
        se.add(new Coords2d(2,-2));
        m = this.supinf(m, se, inp, limiar);
        m = this.infsup(m, se, inp, limiar);

        return m;
    }
    //abertura
    public int[][] infsup(int[][] mtLab, LinkedList se, WritableRaster inp, float limiar) {
        mtLab = infimo(mtLab, se, inp, limiar);
        mtLab = supremo(mtLab, se, inp, limiar);
        return mtLab;
    }
   
    //fechamento
    public int[][] supinf(int[][] mtLab, LinkedList se, WritableRaster inp, float limiar) {
        mtLab = supremo(mtLab, se, inp, limiar);
        mtLab = infimo(mtLab, se, inp, limiar);
        return mtLab;
    }
    //dilatacao
    public int[][] supremo(int[][] mtLab, LinkedList se, WritableRaster inp, float limiar) {
        int w = mtLab.length;
        int h = mtLab[0].length;
        int[][] m = new int[w][h];
        for (int x = 0; x < w; x++)
            for (int y = 0; y < h; y++) {
                int max = mtLab[x][y];
                Iterator it = se.iterator();
                while(it.hasNext()) {
                    Coords2d c = (Coords2d)it.next();
                    int x2 = x+c.getx();
                    int y2 = y+c.gety();
                    if (x2 >= 0 && x2 < w && y2 >= 0 && y2 < h) {
                        int d = mtLab[x2][y2];
                        if (d > max && this.distLab01(inp,x,y,x2,y2) < limiar)
                            max = d;
                    }
                }
                m[x][y] = max;
            }
        return m;
    }
    //erosao
    public int[][] infimo(int[][] mtLab, LinkedList se, WritableRaster inp, float limiar) {
        int w = mtLab.length;
        int h = mtLab[0].length;
        int[][] m = new int[w][h];
        for (int x = 0; x < w; x++)
            for (int y = 0; y < h; y++) {
                int min = mtLab[x][y];
                Iterator it = se.iterator();
                while(it.hasNext()) {
                    Coords2d c = (Coords2d)it.next();
                    int x2 = x+c.getx();
                    int y2 = y+c.gety();
                    if (x2 >= 0 && x2 < w && y2 >= 0 && y2 < h) {
                        int d = mtLab[x2][y2];
                        if (d < min && this.distLab01(inp,x,y,x2,y2) < limiar)
                            min = d;
                    }
                }
                m[x][y] = min;
            }
        return m;
    }

    public float distLab01(WritableRaster inp, int x, int y, int x2, int y2) {
        int r = inp.getSample(x,y,0);
        int g = inp.getSample(x,y,1);
        int b = inp.getSample(x,y,2);
        float[] lab = this.rgb2cielab(r,g,b);
        int r2 = inp.getSample(x2,y2,0);
        int g2 = inp.getSample(x2,y2,1);
        int b2 = inp.getSample(x2,y2,2);
        float[] lab2 = this.rgb2cielab(r2,g2,b2);
        float dist = this.euclideanDistance(lab,lab2,3)/100.0f;
        return dist;
    }

    public BufferedImage forceStroke(BufferedImage strk, Color corTransp, BufferedImage map) {
        WritableRaster inp = strk.copyData(null);
        WritableRaster oup = map.getRaster();
        int w = map.getWidth();
        int h = map.getHeight();
        for (int x = 0; x < w; x++)
            for (int y = 0; y < h; y++) {
                Color cor = new Color(inp.getSample(x,y,0),inp.getSample(x,y,1),inp.getSample(x,y,2));
                if (!this.sameRGB(corTransp, cor))
                    for (int c=0; c<3; c++)
                        oup.setSample(x, y, c, inp.getSample(x,y,c));
            }
            
        return map;
    }

    public void saveResultsSegm(InputGraph1 GI, ModelGraph1 GM, BufferedImage map) {
        this.saveResultsSegm(GI, GM, map, Color.green);
    }
    //////// veja m15/Ferramentas15.java para segm3.java !!!!!!!!!!
    public void saveResultsSegm(InputGraph1 GI, ModelGraph1 GM, BufferedImage map, Color cor) {
        BufferedImage tmp = this.calcMosaic(GI);
        //this.saveImage(tmp, "zzz-mosaic.png");
        tmp = this.calcContornoRegioes(tmp, GI.getOpWshed().getWshedLines(), cor);//Color.red);
        //this.saveImage(tmp,"zzz-mosaic-wshed.png");
        
        Ferramentas.saveImage(GM.getStrokeImage(),"zzz-stroke.png");
        
        tmp = this.copyImage(GI.getImWshed());
        tmp = GM.showEdges(tmp, 1);
        //this.saveImage(tmp,"zzz-edges-gm.png");
        /*
        tmp = this.copyImage(GI.getImWshed());
        tmp = GI.showEdges(tmp, 1);
        this.saveImage(tmp,"zzz-edges-gi.png");
*/
        //this.saveImage(GM.calcBufferedImage(),"zzz-model.png");
        
        Ferramentas.saveImage(map, "zzz-map.png"); // map of labels

        int w = GI.getImRGB().getWidth();
        int h = GI.getImRGB().getHeight();
        boolean[][] contorno = this.calcContorno(map);
        Graphics2D g2d = tmp.createGraphics();
        g2d.setColor(Color.white);
        g2d.fillRect(0,0,w,h);
        tmp = this.calcContornoRegioes(tmp,contorno,Color.blue);
        //this.saveImage(tmp,"zzz-contour.png");

cor = Color.white;
//System.out.println("Contorno branco.");
        tmp = this.calcContornoRegioes(GI.getImRGB(),contorno,cor,1);//Color.red);//blue);
        //tmp = this.calcContornoRegioes(GI.getImRGB(),contorno,cor);//Color.red);//blue);
        //this.saveImage(tmp, "zzz-contouroverim.png");// contours over RGB image
	    //this.saveImage(this.sobrepoe(tmp,GM.getStrokeImage()), "zzz-strkovercontouroverim.png");
        tmp = this.calcContornoRegioes(GI.getImWshed(),contorno,Color.green);//blue);
        //this.saveImage(tmp, "zzz-contouroverwhed.png"); // contours over wshed regions
        
        //System.out.println("components, edge blur");
        //Components c = new Components(map);
        //c.saveSegmParts(GI.getImRGB(), Color.white, "zzz-parts-%03d.png");
        //c.saveSegmPartsEdgeBlur(GI.getImRGB(), Color.white, "zzz-partsb-%03d.png");

        //contorno grosso sobre imagem de entrada
        int len = 3;
        rescontorno a = new rescontorno(map, GI.getImRGB(),len);
        tmp = a.getResult();
        Ferramentas.saveImage(tmp, "zzz-outline.png");
BufferedImage strk = this.engrossaStrokes(GM.getStrokeImage(),len);
this.saveImage(this.sobrepoe(tmp,strk), "zzz-strkoutline.png");
this.saveImage(this.sobrepoe(GI.getImRGB(),strk), "zzz-strkim.png");

        //display result: translucent map over original image
        tmp = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);  
        g2d = tmp.createGraphics();
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC, 1.0f));
        g2d.drawImage(GI.getImRGB(), 0, 0, null);
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.4f));
        g2d.drawImage(map, 0, 0, null);
        //Ferramentas.saveImage(tmp, "zzz-mapover.png");

        tmp = this.calcContornoRegioes(tmp,contorno, Color.blue);
        //this.saveImage(tmp,"zzz-contourovermapover.png");

        //this.saveImage(this.sobrepoe(GI.getImRGB(),GM.getStrokeImage()), "zzz-strkoverim.png");

    }

    public BufferedImage engrossaStrokes(BufferedImage strk, int len) {
        BufferedImage res = this.copyImage(strk);
        Graphics2D g2d = res.createGraphics();
        WritableRaster inp = strk.getRaster();
        WritableRaster oup = res.getRaster();
        int w = strk.getWidth();
        int h = strk.getHeight();
        for (int x=0; x<w; x++)
            for (int y=0; y<h; y++) {
                int r = inp.getSample(x,y,0);
                int g = inp.getSample(x,y,1);
                int b = inp.getSample(x,y,2);
                if (!this.sameRGB(r,g,b, 255, 255, 255)) { // fundo branco
                    Color cor = new Color(r,g,b);
                    g2d.setColor(cor);
                    g2d.fillOval(x, y, len, len);
                }
            }
        return res;
    }

    public void imagensRegioesId(InputGraph1 G, String foutfmt) {
        int totv = G.getNumVertices();
        Vertex[] refv = G.getVertices();
        BufferedImage im = G.getImRGB();
        int w = im.getWidth();
        int h = im.getHeight();
        BufferedImage res = new BufferedImage(w, h, BufferedImage.TYPE_3BYTE_BGR);
        Graphics2D g2d = res.createGraphics();
        g2d.setColor(Color.black);
        WritableRaster inp = im.copyData(null);
        WritableRaster oup = res.getRaster();
        for (int i = 0; i < totv; i++) {
            String fout = String.format(foutfmt,i);
            g2d.fillRect(0,0,w,h);
            Vertex v = refv[i];
            Region reg = v.getRegion();
            LinkedList list = reg.getPixels();
            Iterator it = list.iterator();
            while (it.hasNext()) {
            Pixel p = (Pixel)it.next();
            int x = p.getx();
            int y = p.gety();
            for (int c = 0; c < 3; c++)
                //oup.setSample(x,y,c,inp.getSample(x,y,c));
                oup.setSample(x,y,c,255);
            }
            this.saveImage(res,fout);
        }
    }

    public int[][] rotulaPixels(int w, int h, int[] result, int totvi, Vertex[] refvi) {
        int[][] rpixels = new int[w][h];
        for (int x=0; x<w; x++)
            for (int y=0; y<h; y++)
                rpixels[x][y] = -1; //vazio
        for (int i = 0; i < totvi; i++) {
            Vertex v = refvi[i];
            Region r = v.getRegion();
            Iterator it = r.getPixels().iterator();
            while (it.hasNext()) {
                Pixel p = (Pixel)it.next();
                int x = p.getx();
                int y = p.gety();
                rpixels[x][y] = result[i];
            }
        }
        return rpixels;
    }
    
    public BufferedImage calcMapaRotulos(int[][] rpixels, int w, int h, ModelGraph1 model) {
        BufferedImage mapaRotulos = new BufferedImage(w, h, BufferedImage.TYPE_3BYTE_BGR);
        Graphics2D g2d = mapaRotulos.createGraphics();
        g2d.setColor(Color.white);
        g2d.fillRect(0,0,w,h);
        
        for (int x=0; x<w; x++)
            for (int y=0; y<h; y++)
                if (rpixels[x][y] > -1)
                    mapaRotulos.setRGB(x, y, model.getColor(rpixels[x][y]).getRGB());
        return mapaRotulos;
    }

    public BufferedImage calcMapaRotulos(InputGraph1 input, ModelGraph1 model, int[] result) {
        BufferedImage im = input.getImRGB();
        int w = im.getWidth();
        int h = im.getHeight();
        int totvi = input.getNumVertices();
        Vertex[] refvi = input.getVertices();
        int[][] rpixels = rotulaPixels(w, h, result, totvi, refvi);
        return calcMapaRotulos(rpixels, w, h, model);
    }


    public boolean[][] calcContorno(BufferedImage mapaRotulos) {
        int w = mapaRotulos.getWidth();
        int h = mapaRotulos.getHeight();
        WritableRaster inp = mapaRotulos.copyData(null);
        boolean[][] res = new boolean[w][h];
        for (int x=0; x<w; x++)
            for (int y=0; y<h; y++)
                res[x][y] = false;
        for (int x=0; x<w; x++)
            for (int y=0; y<h-1; y++) {
                int br = inp.getSample(x,y,0) - inp.getSample(x,y+1,0);
                int bg = inp.getSample(x,y,1) - inp.getSample(x,y+1,1);
                int bb = inp.getSample(x,y,2) - inp.getSample(x,y+1,2);
                if (!(br == 0 && bg == 0 && bb == 0))
                    res[x][y] = true;
            }
        for (int x=0; x<w-1; x++)
            for (int y=0; y<h; y++) {
                int br = inp.getSample(x,y,0) - inp.getSample(x+1,y,0);
                int bg = inp.getSample(x,y,1) - inp.getSample(x+1,y,1);
                int bb = inp.getSample(x,y,2) - inp.getSample(x+1,y,2);
                if (!(br == 0 && bg == 0 && bb == 0))
                    res[x][y] = true;
            }
        return res;
    }
    public boolean[][] calcContorno(int[][] comps) {
        int w = comps.length;
        int h = comps[0].length;
        boolean[][] res = new boolean[w][h];
        for (int x=0; x<w; x++)
            for (int y=0; y<h; y++)
                res[x][y] = false;
        for (int x=0; x<w; x++)
            for (int y=0; y<h-1; y++) {
                int d = comps[x][y] - comps[x][y+1];
                if (d != 0)
                    res[x][y] = true;
            }
        for (int x=0; x<w-1; x++)
            for (int y=0; y<h; y++) {
                int d = comps[x][y] - comps[x+1][y];
                if (d != 0)
                    res[x][y] = true;
            }
        return res;
    }
    
    public BufferedImage calcContornoRegioes(BufferedImage im, boolean[][] contorno, Color cor) {
        //BufferedImage res = this.copyImage(im);
        int w = im.getWidth();
        int h = im.getHeight();
        BufferedImage res = new BufferedImage(w, h, BufferedImage.TYPE_3BYTE_BGR);
        WritableRaster inp = im.copyData(null);
        WritableRaster oup = res.getRaster();
        int r = cor.getRed();
        int g = cor.getGreen();
        int b = cor.getBlue();
        for (int x=0; x<w; x++)
            for (int y=0; y<h-1; y++) {
                for (int c = 0; c < 3; c++)
                    oup.setSample(x,y,c,inp.getSample(x,y,c));
                if (contorno[x][y]) {
                    oup.setSample(x,y,0,r);
                    oup.setSample(x,y,1,g);
                    oup.setSample(x,y,2,b);
                }
	    }
        return res;
    }
    public BufferedImage calcContornoRegioes(BufferedImage im, boolean[][] contorno, Color cor, int totalDilatacoes) {
        int h = contorno.length;
        int w = contorno[0].length;
        int[][] border = new int[h][w];
        for (int y = 0; y < h; y++)
            for (int x = 0; x < w; x++) {
                border[y][x] = 0;
                if (contorno[y][x])
                    border[y][x] = 1;
            }
        for (int i = 0; i < totalDilatacoes; i++)
            border = this.dilatacaoCruz(border);
        for (int y = 0; y < h; y++)
            for (int x = 0; x < w; x++)
                 if (border[y][x] == 1)
                    contorno[y][x] = true;
        return this.calcContornoRegioes(im, contorno, cor);
    }
    
    public void calcContornoRegioesARGB(BufferedImage imARGB, boolean[][] contorno, Color cor) {
        int w = imARGB.getWidth();
        int h = imARGB.getHeight();
        WritableRaster oup = imARGB.getRaster();
        WritableRaster oupA = imARGB.getAlphaRaster();
        int r = cor.getRed();
        int g = cor.getGreen();
        int b = cor.getBlue();
        for (int x=0; x<w; x++)
            for (int y=0; y<h-1; y++) {
                for (int c = 0; c < 3; c++)
                    if (contorno[x][y]) {
                        oup.setSample(x,y,0,r);
                        oup.setSample(x,y,1,g);
                        oup.setSample(x,y,2,b);
                        oupA.setSample(x,y,0,255);
                    }
	    }
    }
/*
    // para exibir contornos das regioes no watershed
    public BufferedImage calcContornoRegioes(int[][] rpixels, BufferedImage im) {
        int w = im.getWidth();
        int h = im.getHeight();
        boolean[][] contorno = calcContorno(rpixels,w,h);
        return calcContornoRegioes(im, contorno);
    }
*/
    public BufferedImage calcMosaic(InputGraph1 Gi) {
        BufferedImage im = Gi.getImRGB();
        int w = im.getWidth();
        int h = im.getHeight();
        BufferedImage res = new BufferedImage(w, h, BufferedImage.TYPE_3BYTE_BGR);
        WritableRaster oup = res.getRaster();
        Vertex[] refv = Gi.getVertices();
        int totv = Gi.getNumVertices();
        for (int i = 0; i < totv; i++) {
            Vertex v = refv[i];
            float[] rgb = Gi.getRGB(i);
            int g = (int)((rgb[0]+rgb[1]+rgb[2])/3);
            Region r = v.getRegion();
            LinkedList pixels = r.getPixels();
            Iterator it = pixels.iterator();
            while (it.hasNext()) {
                Pixel p = (Pixel)it.next();
                int x = p.getx();
                int y = p.gety();
                for (int c = 0; c < 3; c++)
                    oup.setSample(x,y,c,g);
            }
        }
        return res;
    }
    public BufferedImage calcMosaicColor(InputGraph1 Gi) {
        BufferedImage im = Gi.getImRGB();
        int w = im.getWidth();
        int h = im.getHeight();
        BufferedImage res = new BufferedImage(w, h, BufferedImage.TYPE_3BYTE_BGR);
        WritableRaster oup = res.getRaster();
        Vertex[] refv = Gi.getVertices();
        int totv = Gi.getNumVertices();
        for (int i = 0; i < totv; i++) {
            Vertex v = refv[i];
            float[] rgb = Gi.getRGB(i);
            Region r = v.getRegion();
            LinkedList pixels = r.getPixels();
            Iterator it = pixels.iterator();
            while (it.hasNext()) {
                Pixel p = (Pixel)it.next();
                int x = p.getx();
                int y = p.gety();
                for (int c = 0; c < 3; c++)
                    oup.setSample(x,y,c,(int)(rgb[c]));
            }
        }
        return res;
    }
    /*
    public BufferedImage grade(BufferedImage im) {
        int w = im.getWidth();
        int h = im.getHeight();
        BufferedImage res = new BufferedImage(w, h, BufferedImage.TYPE_3BYTE_BGR);//this.copyImage(im);
        //2008set09: coloca grade para enriquecer informacao do background, por exemplo...
        // -> nao quero 1 vertice com grau muito alto (ou seja, com muitas arestas)
        int div = 10;
        int gray = 0;
        if (w >= 100 && h >= 100) {
            WritableRaster oup = res.getRaster();
            int dw = w/div;
            int dh = h/div;
            for (int x=1; x<div; x++) {
                int x2 = x*dw;
                if (x2 >= w)
                    break;
                for (int y2=0; y2<h; y2++)
                    for (int c=0; c<3; c++)
                        oup.setSample(x2, y2, c, gray);
            }
            for (int y=1; y<div; y++) {
                int y2 = y*dh;
                if (y2 >= w)
                    break;
                for (int x2=0; x2<w; x2++)
                    for (int c=0; c<3; c++)
                        oup.setSample(x2, y2, c, gray);
            }
        }
        return res;
    }*/
    public void grade(BufferedImage im) {
        int div = 10;
        grade(im, div);
    }
    public void grade(BufferedImage im, int div) {
        int w = im.getWidth();
        int h = im.getHeight();
        //2008set09: coloca grade para enriquecer informacao do background, por exemplo...
        // -> nao quero 1 vertice com grau muito alto (ou seja, com muitas arestas)
        int gray = 0;
        if (w >= 100 && h >= 100) {
            WritableRaster oup = im.getRaster();
            int dw = w/div;
            int dh = h/div;
            for (int x=1; x<=div; x++) {
                int x2 = x*dw;
                if (x2 >= w)
                    break;
                for (int y2=0; y2<h; y2++)
                    for (int c=0; c<3; c++)
                        oup.setSample(x2, y2, c, gray);
            }
            for (int y=1; y<=div; y++) {
                int y2 = y*dh;
                if (y2 >= h)
                    break;
                for (int x2=0; x2<w; x2++)
                    for (int c=0; c<3; c++)
                        oup.setSample(x2, y2, c, gray);
            }
        }
    }


    public void grade(BufferedImage im, int divx, int divy, int gray, int channels) {
        int w = im.getWidth();
        int h = im.getHeight();
        if (w >= 100 && h >= 100) {
            WritableRaster oup = im.getRaster();
            int dw = w/divx;
            int dh = h/divy;
            for (int x=1; x<=divx+1; x++) {
                int x2 = x*dw;
                if (x2 >= w-1)
                    break;
                for (int y2=0; y2<h; y2++)
                    for (int c=0; c<channels; c++)
                        oup.setSample(x2, y2, c, gray);
            }
            for (int y=1; y<=divy+1; y++) {
                int y2 = y*dh;
                if (y2 >= h-1)
                    break;
                for (int x2=0; x2<w; x2++)
                    for (int c=0; c<channels; c++)
                        oup.setSample(x2, y2, c, gray);
            }
        }
    }


}
