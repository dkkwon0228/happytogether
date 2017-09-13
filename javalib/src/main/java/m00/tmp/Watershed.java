
/*

2008mar18

Pequena modificacao no modulo original do prof. Consularo 
(partition/wshed/Watershed.java)
-> evita revisitar os elementos j� examinados

*/

package m00.tmp;

import java.awt.image.BufferedImage;
import java.awt.image.*;
import java.util.*;

import m00.Blur;
import m00.Consts;
import m00.Conversion;
import m00.Coords2d;
import m00.Ferramentas;
import m00.Gradient;
import m00.Pixel;


/////////////////////////////////////////////////////////////////////////////////////////
class ComparatorPixel implements Comparator {
    public int compare(Object a, Object b) throws ClassCastException {
        int r = 0;
        double ca = ((Pixel)a).getg();
        double cb = ((Pixel)b).getg();
        if (ca < cb)
            r = -1;
        else 
            if (ca > cb)
                r = 1;
        return(r);
    }   
    
}
class ComparatorPixel2 implements Comparator {
    public int compare(Object a, Object b) throws ClassCastException {
        int r = 0;
        double ca = ((Pixel)a).getfloat(); 
        double cb = ((Pixel)b).getfloat();
        if (ca < cb)
            r = -1;
        else 
            if (ca > cb)
                r = 1;
        return(r);
    }   
    
}

public class Watershed {
    private int totr = 0; // total de regioes
    private BufferedImage imgray, imwshed;
    private int nb; //blur

    private int[][] mtLab, mtLab2;
    private double[][] mtDst;
    private LinkedList lstImage;
    private int w, h;
    
    private boolean[][] wshedline, wshedline2;

    private Consts cts = new Consts();
    private Ferramentas fe = new Ferramentas();
    final int INIT = -1;
    final int MASK = -2;
    final int WSHED = 0;

    public Watershed(String fn, int nb) {
        BufferedImage im = Ferramentas.loadImage(fn);
        commonInit(im, nb);
    }
    public Watershed(BufferedImage im, int nb) {
        commonInit(im, nb);
    }

    public Watershed(double[][] tmp, BufferedImage im) {
    }

    public void commonInit(BufferedImage im, int nb) {
        this.nb = nb;
        this.w = im.getWidth();
        this.h = im.getHeight();
        //se imagem colorida, calcula media dos 3 canais RGB
        Conversion opConv = new Conversion(im, false);
        this.imgray = opConv.imgConverted();
        Gradient opGradient = new Gradient(imgray);
        double[][] mtMod = opGradient.getModuleNormalized();
        if (this.nb > 0) {
            Blur opBlur = new Blur(mtMod, this.w, this.h, this.nb);
            opBlur.Normalize();
            double[][] mtBlur = opBlur.getBlur();
            this.calcWatershed(mtBlur, this.w, this.h);
        }
        else
            this.calcWatershed(mtMod, this.w, this.h);
        this.imwshed = calcBufferedImage();
    }

    public Watershed(double[][] mtInp) { //sem blur: para reducao de regioes do wshed (ReducedWshed)
        this.w = mtInp.length;
        this.h = mtInp[0].length;
        Gradient opGradient = new Gradient(mtInp,w,h);
        double[][] mtMod = opGradient.getModuleNormalized();
        this.calcWatershed(mtMod, this.w, this.h);
    }
    public void calcWatershed(double[][] mtInp, int w, int h) {
        int i, j, k, v;
        Pixel p, q;
        this.w = w; 
        this.h = h;
        Pixel ficticius = new Pixel(-1, -1, 0);
        mtLab = new int[w][h];
        mtDst = new double[w][h];
        int currlab = 0;
        double currDst;
        LinkedList lstQueue = new LinkedList();
        lstImage = new LinkedList();
        
        for (j=0; j<h; j++)
            for (i=0; i<w; i++) {
                mtLab[i][j] = INIT;
                mtDst[i][j] = 0.0;
                lstImage.add(new Pixel(i, j, (int)Math.floor(mtInp[i][j])));
            }

        Comparator comparator;
        comparator = new ComparatorPixel();
        Collections.sort(lstImage, comparator);
        
        p = (Pixel)lstImage.getLast();
        int hMax = p.getg();
        p = (Pixel)lstImage.getFirst();
        int hMin = p.getg();

        LinkedList examinados = new LinkedList();
        for (k=hMin; k<=hMax; k++) {
            while (lstImage.size()>0) {
                p = (Pixel)lstImage.removeFirst();
                if (p.getg() == k) {
                    examinados.addLast(p);
                    mtLab[p.getx()][p.gety()] = MASK;
                    
                    for (v=0; v<8; v++) {
                        q = new Pixel(p.getx()+cts.viz8x[v], p.gety()+cts.viz8y[v], 0);
                      /*  
                    for (v=0; v<4; v++) {
                        q = new Pixel(p.getx()+cts.viz4x[v], p.gety()+cts.viz4y[v], 0);
                        */
                        if (insideImg(q))
                            if ((mtLab[q.getx()][q.gety()] > 0) || (mtLab[q.getx()][q.gety()] == WSHED)) {
                                mtDst[p.getx()][p.gety()] = cts.dst8[v];
                                //mtDst[p.getx()][p.gety()] = 1.0;
                                lstQueue.addFirst(p);
                                break;
                            }
                    }
                }
                else 
                    if (p.getg() > k) {
                        lstImage.addFirst(p);                        
                        break;
                    }
            }
            currDst = 1.0;
            lstQueue.addFirst(ficticius);
            while (true) {
                p = (Pixel)lstQueue.removeLast();
                if (p == ficticius) {
                    if (lstQueue.isEmpty())
                        break;
                    else {
                        lstQueue.addFirst(ficticius);
                        currDst += 1.0;
                        p = (Pixel)lstQueue.removeLast();
                    }
                }
                for (v=0; v<8; v++) {
                    q = new Pixel(p.getx()+cts.viz8x[v], p.gety()+cts.viz8y[v], 0);
                    if (insideImg(q)) {
                        if ((mtDst[q.getx()][q.gety()] < currDst) &&
                            ((mtLab[q.getx()][q.gety()] > 0)  || (mtLab[q.getx()][q.gety()] == WSHED))) {
                                if (mtLab[q.getx()][q.gety()] > 0) {
                                    if ((mtLab[p.getx()][p.gety()] == MASK) || (mtLab[p.getx()][p.gety()] == WSHED))
                                        mtLab[p.getx()][p.gety()] = mtLab[q.getx()][q.gety()];
                                    else
                                        if (mtLab[p.getx()][p.gety()] != mtLab[q.getx()][q.gety()])
                                            mtLab[p.getx()][p.gety()] = WSHED;
                                }
                                else
                                    if (mtLab[p.getx()][p.gety()] == MASK)
                                        mtLab[p.getx()][p.gety()] = WSHED;
                        }
                        else
                            if ((mtLab[q.getx()][q.gety()] == MASK) && (Math.abs(mtDst[q.getx()][q.gety()]) <= 0)) {
                                mtDst[q.getx()][q.gety()] = currDst+cts.dst8[v];
                                q.setg((int)Math.floor(mtInp[q.getx()][q.gety()]));
                                lstQueue.addFirst(q);
                            }
                    }
                }
            }
            while (examinados.size()>0) {
                p = (Pixel)examinados.removeFirst();
                if (p.getg() == k) {
                    mtDst[p.getx()][p.gety()] = 0.0;
                    if (mtLab[p.getx()][p.gety()] == MASK) {
                        currlab++;
                        lstQueue.addFirst(p);
                        mtLab[p.getx()][p.gety()] = currlab;
                        while (!lstQueue.isEmpty()) {
                            q = (Pixel)lstQueue.removeLast();
                            for (v=0; v<8; v++) {
                                Pixel r = new Pixel(q.getx()+cts.viz8x[v], q.gety()+cts.viz8y[v], 0);
                                if (insideImg(r) && mtLab[r.getx()][r.gety()] == MASK) {
                                    r.setg((int)Math.floor(mtInp[r.getx()][r.gety()]));
                                    lstQueue.addFirst(r);
                                    mtLab[r.getx()][r.gety()] = currlab;
                                }
                            }
                        }
                    }
                }
            }
        }
        this.totr = currlab;
        this.mtLab2 = new int[w][h];
        //salva rotulos antes de sumir com as linhas do watershed
        //-> uso na criacao do Modelo
        for (int x=0; x<w; x++)
            for (int y=0; y<h; y++)
                mtLab2[x][y] = mtLab[x][y];
        this.wshedline2 = calcContorno2(); //pixels cujo rotulo � WSHED
        vanishWshed();
        this.wshedline = calcContorno();
    }

    public int getTotalRegions() {
        return this.totr;
    }
    public int[][] getRefLabels() { 
        return mtLab; 
    }
    public int[][] getRefLabelsBeforeVanishWatershed() { 
        return mtLab2; 
    }
    public boolean[][] getWshedLines() {
        return this.wshedline;
    }
    //pixels cujo rotulo � WSHED
    public boolean[][] getWshedLines2() {
        return this.wshedline2;
    }
    private boolean[][] calcContorno() {
        return fe.calcContorno(this.mtLab, this.w, this.h);
    }
    //pixels cujo rotulo � WSHED
    private boolean[][] calcContorno2() {
        boolean[][] res = new boolean[w][h];
        for (int x=0; x<w; x++)
            for (int y=0; y<h; y++)
                if (mtLab2[x][y] == WSHED)
                    res[x][y] = true;
                else
                    res[x][y] = false;
        return res;
    }
    
    public BufferedImage getImGray() {
        return this.imgray;
    }

    public BufferedImage getImWshed() {
        return this.imwshed;
    }
    private BufferedImage calcBufferedImage() {
        BufferedImage src = this.imgray;
        WritableRaster inpSrc = src.copyData(null);
        BufferedImage oupImg = new BufferedImage(w, h, BufferedImage.TYPE_3BYTE_BGR);
        WritableRaster oupSrc = oupImg.getRaster();
        for (int i=0; i<w; i++)
            for (int j=0; j<h; j++)
                if (this.wshedline[i][j]) {
                    oupSrc.setSample(i, j, 0, 255);
                    oupSrc.setSample(i, j, 1, 0);
                    oupSrc.setSample(i, j, 2, 0);
                }
                else {
                    oupSrc.setSample(i, j, 0, inpSrc.getSample(i, j, 0));
                    oupSrc.setSample(i, j, 1, inpSrc.getSample(i, j, 0));
                    oupSrc.setSample(i, j, 2, inpSrc.getSample(i, j, 0));
                }
        return oupImg;
    }

    public boolean insideImg(Pixel p) {
        return ((p.getx() >= 0) && (p.getx() < w) && (p.gety() >= 0) && (p.gety() < h));
    }

    public boolean insideImg(int x, int y) {
        return ((x >= 0) && (x < w) && (y >= 0) && (y < h));
    }
    
    public void vanishWshed() {
        //2008jun12: some com linhas do watershed por filtro conexo: NAO DEU CERTO!
        /*
        int[][] copiaLab = new int[w][h];
        for (int x=0; x<w; x++)
            for (int y=0; y<h; y++)
                copiaLab[x][y] = mtLab[x][y];
                *
        boolean restou = true;
        while(restou) {
        mtLab = filtroConexo(mtLab);
        restou = false;
        for (int x=0; x<w; x++)
            for (int y=0; y<h; y++)
                if (mtLab[x][y] == WSHED) {
                restou = true;
                /*
                    System.out.println("Watershed: ainda restou WSHED!");
                    System.exit(0);
                    *
                }
        }
        */
        
        int[] labels = new int[this.totr+1];
        for (int i=0; i<w; i++) 
            for (int j=0; j<h; j++)
                if (mtLab[i][j] == WSHED) {
                    for (int l=0; l<=this.totr; l++) 
                        labels[l] = 0;
                    for (int ki=-5; ki<=5; ki++)
                        for (int kj=-5; kj<=5; kj++)
                            if (i+ki > 0 && i+ki < w && j+kj > 0 && j+kj < h && mtLab[i+ki][j+kj] > 0)
                                labels[mtLab[i+ki][j+kj]] += 1;
                    int mxcont = labels[0];
                    int mxlab = 0;
                    for (int l=1; l<=this.totr; l++)
                        if (mxcont < labels[l]) {
                            mxcont = labels[l];
                            mxlab = l;
                        }
                    mtLab[i][j] = mxlab;
                }
         
    }
    private int[][] filtroConexo(int[][] m) {
        LinkedList se = new LinkedList();
        
        //linha vertical
        se.add(new Coords2d(0,1));
        se.add(new Coords2d(0,-1));
        m = fe.supinf(m, se);
        m = fe.infsup(m, se);

        //linha horizontal
        se.clear();
        se.add(new Coords2d(1,0));
        se.add(new Coords2d(-1,0));
        m = fe.supinf(m, se);
        m = fe.infsup(m, se);
        
        return m;
    }
    //erosao
    public int[][] infimo(int[][] mm, LinkedList se) {
        int w = mm.length;
        int h = mm[0].length;
        int[][] m = new int[w][h];
        for (int x = 0; x < w; x++)
            for (int y = 0; y < h; y++) {
                if (mm[x][y] == WSHED) {
                int min = mm[x][y];
                Iterator it = se.iterator();
                while(it.hasNext()) {
                    Coords2d c = (Coords2d)it.next();
                    int x2 = x+c.getx();
                    int y2 = y+c.gety();
                    if (x2 >= 0 && x2 < w && y2 >= 0 && y2 < h) {
                        int d = mm[x2][y2];
                        if (d < min && d != WSHED)
                            min = d;
                    }
                }
                m[x][y] = min;
                }
            }
        return m;
    }

    //dilatacao
    public int[][] supremo(int[][] mm, LinkedList se) {
        int w = mm.length;
        int h = mm[0].length;
        int[][] m = new int[w][h];
        for (int x = 0; x < w; x++)
            for (int y = 0; y < h; y++) {
                if (mm[x][y] == WSHED) {
                int max = mm[x][y];
                Iterator it = se.iterator();
                while(it.hasNext()) {
                    Coords2d c = (Coords2d)it.next();
                    int x2 = x+c.getx();
                    int y2 = y+c.gety();
                    if (x2 >= 0 && x2 < w && y2 >= 0 && y2 < h) {
                        int d = mm[x2][y2];
                        if (d > max && d != WSHED)
                            max = d;
                    }
                }
                m[x][y] = max;
                }
            }
        return m;
    }
    
    //abertura
    public int[][] infsup(int[][] mm, LinkedList se) {
        mm = infimo(mm, se);
        mm = supremo(mm, se);
        return mm;
    }
    
    //fechamento
    public int[][] supinf(int[][] mm, LinkedList se) {
        mm = supremo(mm, se);
        mm = infimo(mm, se);
        return mm;
    }

    /////////////////////////////////////////////////////////////////////////

    public Watershed(String fn, int nb, LinkedList lstCMarkers, LinkedList lstMarkers) {
        BufferedImage im = Ferramentas.loadImage(fn);
        commonInit2(im, nb, lstCMarkers, lstMarkers);
    }
    public Watershed(BufferedImage im, int nb, LinkedList lstCMarkers, LinkedList lstMarkers) {
        commonInit2(im, nb, lstCMarkers, lstMarkers);
    }
    public void commonInit2(BufferedImage im, int nb, LinkedList lstCMarkers, LinkedList lstMarkers) {
        this.nb = nb;
        this.w = im.getWidth();
        this.h = im.getHeight();
        //se imagem colorida, calcula media dos 3 canais RGB
        Conversion opConv = new Conversion(im, false);
        this.imgray = opConv.imgConverted();
        Gradient opGradient = new Gradient(imgray);
        double[][] mtMod = opGradient.getModuleNormalized();
        if (this.nb > 0) {
            Blur opBlur = new Blur(mtMod, this.w, this.h, this.nb);
            opBlur.Normalize();
            double[][] mtBlur = opBlur.getBlur();
            this.calcWatershed(mtBlur, lstCMarkers, lstMarkers);
        }
        else
            this.calcWatershed(mtMod, lstCMarkers, lstMarkers);
        this.imwshed = calcBufferedImage();
    }


    //2008set23: wshed com marcadores
    public void calcWatershed(double[][] mtInp, LinkedList lstCMarkers, LinkedList lstMarkers) {

        this.w = mtInp.length;
        this.h = mtInp[0].length;

        int i, j, k, v;
        Iterator itPixel;
        Pixel p, q;

        PriorityQueue<Pixel>
        pix_queue = new PriorityQueue<Pixel>(3, new ComparatorPixel2());
        int[][] L = new int[w][h];
        mtLab = new int[w][h];
        int[][] permanente = new int[w][h];
        int[][] ws = new int[w][h];
        float[][] dist = new float[w][h];

        for (i=0; i<w; i++) {
            for (j=0; j<h; j++) {
                L[i][j] = 0;
                permanente[i][j] = 0;
                ws[i][j] = 0;
                dist[i][j] = 0.0f;
            }
        }
        float maxdist = 0.0f;
        if (lstCMarkers != null) {
            boolean maxfirst = true;
            for (i=0; i<w; i++) {
                for (j=0; j<h; j++) {
                    dist[i][j] = 0.0f; boolean first = true;
                    itPixel = lstCMarkers.iterator();                // inicializa a matriz de rotulos
                    while (itPixel.hasNext()) {
                        p = (Pixel)itPixel.next();
                        int dx = i - p.getx();
                        int dy = j - p.gety();
                        float vdist = (float)Math.sqrt(dx*dx + dy*dy);
                        if (first) {
                            dist[i][j] = vdist;
                            first = false;
                        } else {
                            if (dist[i][j] > vdist) dist[i][j] = vdist;
                        }
                    }
                    if (maxfirst) {
                        maxdist = dist[i][j]; maxfirst = true;
                    } else
                        if (maxdist < dist[i][j]) maxdist = dist[i][j];
                }
            }
            for (i=0; i<w; i++) 
                for (j=0; j<h; j++) 
                    dist[i][j] /= maxdist;
        } else {
            for (i=0; i<w; i++)
                for (j=0; j<h; j++)
                    dist[i][j] = 0.0f;
        }
        

        
        itPixel = lstMarkers.iterator();                // inicializa a matriz de rotulos
        while (itPixel.hasNext()) {                 
            p = (Pixel)itPixel.next();                  // para cada pixel da lista de pixels    
//p.setg(-1); //p.setDlevel(-1); //mtInp[p.getx()][p.gety()]);
p.setfloat(-1);
            if ((p.getx() >= 0) && (p.gety() >= 0) && (p.getx() < w) && (p.gety() < h)) {
                L[p.getx()][p.gety()] = p.getLabel();
                pix_queue.offer(p);
            }
        }

        int kcont = 0; 
        while (!pix_queue.isEmpty()) {
            kcont++;
            p = pix_queue.poll();
            permanente[p.getx()][p.gety()] = -1;
            for (v=0; v<4; v++) {
                int pi = p.getx();
                int pj = p.gety();
                int qi = pi+cts.viz4x[v];
                int qj = pj+cts.viz4y[v];
                if ((qi >= 0) && (qj >= 0) && (qi < w) && (qj < h)) {
                    if (permanente[qi][qj] >= 0) {
                       if (L[qi][qj] == 0) {
                           L[qi][qj] = L[pi][pj];
                           q = new Pixel(qi, qj, 0);
//q.setg((int)(mtInp[qi][qj] + dist[qi][qj])); //q.setDlevel(mtInp[qi][qj] + dist[qi][qj]);
q.setfloat((float)(mtInp[qi][qj] + dist[qi][qj]));
                           q.setLabel(L[qi][qj]);
                           pix_queue.offer(q);
                       }
                       else {
                           if ((L[qi][qj] != L[pi][pj]) && (ws[qi][qj] == 0) && (ws[pi][pj] == 0))
                               ws[qi][qj] = 1;
                       }
                    }
                }
            }
        }

int maxLabel = 0;
        for (i=0; i<w; i++)
            for (j=0; j<h; j++) {
                mtLab[i][j] = L[i][j];
if (L[i][j] > maxLabel)
    maxLabel = L[i][j];
            }
        
this.totr = maxLabel;
this.mtLab2 = new int[w][h];
//salva rotulos antes de sumir com as linhas do watershed
//-> uso na criacao do Modelo
for (int x=0; x<w; x++)
    for (int y=0; y<h; y++)
        mtLab2[x][y] = mtLab[x][y];
this.wshedline2 = calcContorno2(); //pixels cujo rotulo � WSHED
vanishWshed();
this.wshedline = calcContorno();
        
    }
    
}
