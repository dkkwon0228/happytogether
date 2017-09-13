package m00;
/*
 * QTsampling.java
 *
 * Created on 25 de Fevereiro de 2007, 09:33
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

import java.awt.image.*;
import java.util.*;


public class QTsampling {
    
    public enum Criteria { ERROR, STDEV, ENTROPY };
    private BufferedImage inpImg;
    private BufferedImage oupImg;
    private double[][] mtGray;
    private int[][] mtLab;
    private LinkedList lpoints;
    private LinkedList lQTparts;
    private int w, h;
    private double thr;
    private int lim_size;

    private Consts cte = new Consts();

    /** Creates a new instance of QTsampling */
    public QTsampling(BufferedImage inp, int tipo, double thr, int sz, boolean pure) {
        common(inp, tipo, thr, sz, pure);
    }
    public QTsampling(BufferedImage inp, Criteria type, double thr, int sz, boolean pure) {
        int tipo = 0;
        switch (type) {
        case STDEV: 
            tipo = 1;
            break;
        case ENTROPY: 
            tipo = 2;
            break;
        }
        common(inp, tipo, thr, sz, pure);
    }
    public void common(BufferedImage inp, int tipo, double thr, int sz, boolean pure) {
        double val = 0;

        this.thr = thr;
        lim_size = sz;
        inpImg = inp;
        w = inpImg.getWidth();
        h = inpImg.getHeight();
        
        mtLab = new int[w][h];
        
        WritableRaster input = inpImg.copyData(null);
        mtGray = new double[w][h];
        for (int i=0; i < w; i++)
            for (int j=0; j < h; j++) {
                   mtGray[i][j] = (double)input.getSample(i, j, 0);
            }
        lpoints = new LinkedList();
        lQTparts = new LinkedList();
        QTpart qtp = new QTpart(0, 0, w, h);
        lQTparts.addLast(qtp);
        boolean testval = false;
        while (!lQTparts.isEmpty()) {
            QTpart qtx = (QTpart)lQTparts.removeFirst();
            switch (tipo) {
                case 0: //ERROR: 
                    val = error(qtx.getX(), qtx.getY(), qtx.getW(), qtx.getH());
                    testval = (val <= thr);
                    break;
                case 1://STDEV: 
                    val = stdv(qtx.getX(), qtx.getY(), qtx.getW(), qtx.getH());
                    testval = (val <= thr);
                    break;
                case 2: //ENTROPY: 
                    val = entropy(qtx.getX(), qtx.getY(), qtx.getW(), qtx.getH());
                    testval = (val <= thr);
                    break;
            }
            if ((testval) || (qtx.size() <= lim_size)) {
                Pixel px = null;
                if (!pure)
                    px = new Pixel(qtx.middleX(), qtx.middleY(), 0);
                else
                    px = new Pixel(qtx.centerX(), qtx.centerY(), 0);
                px.setLabel(lpoints.size()+1);
                lpoints.addLast(px);
                for (int ki=0; ki<qtx.getW(); ki++) 
                    for (int kj=0; kj<qtx.getH(); kj++)
                        mtLab[ki+qtx.getX()][kj+qtx.getY()] = px.getLabel();
            }
            else {
                int w0 = qtx.getW()/2; 
                int w1 = qtx.getW() - w0;
                if (w1<0) w1 = 0;
                int h0 = qtx.getH()/2; 
                int h1 = qtx.getH() - h0;
                if (h1<0) h1 = 0;
                
                QTpart qt00 = new QTpart(qtx.getX(), qtx.getY(), w0, h0);
                QTpart qt10 = new QTpart(qtx.getX(), qtx.getY()+h0, w0, h1);
                QTpart qt01 = new QTpart(qtx.getX()+w0, qtx.getY(), w1, h0);
                QTpart qt11 = new QTpart(qtx.getX()+w0, qtx.getY()+h0, w1, h1);
                
                lQTparts.addLast(qt00); lQTparts.addLast(qt10); 
                lQTparts.addLast(qt01); lQTparts.addLast(qt11);
            }
        }
    }
    
    public double error(int xx, int yy, int ww, int hh) {
	double error;
	double sum = 0.0;
        double mean = 0.0;
	for (int i=0; i<ww; i++)
            for (int j=0; j<hh; j++)
                sum += mtGray[xx+i][yy+j];
        mean = sum/(ww*hh);
	error=0.0;
	for (int i=0; i<ww; i++)
            for (int j=0; j<hh; j++) {
                double e = mean -  mtGray[xx+i][yy+j];
                e *= e;
                if (e > error) error = e;
            }
	return Math.sqrt(error);
    }

    public double stdv(int xx, int yy, int ww, int hh) {
	double stdev;
	double mean;
	double sum = 0.0;
	for (int i=0; i<ww; i++)
            for (int j=0; j<hh; j++) {
		sum += mtGray[xx+i][yy+j];
	    }
        mean = sum/(ww*hh);
	sum=0; 
	for (int i=0; i<ww; i++)
  	    for (int j=0; j<hh; j++) {
                double e = mtGray[xx+i][yy+j] - mean;
                e *= e;
		sum += e;
	    }
        stdev = Math.sqrt(sum/(ww*hh));
	return stdev;
    }

    public double entropy(int xx, int yy, int ww, int hh) {
        int gl;
        double[] fdp = new double[256];
        for (int i=0; i<256; i++) fdp[i] = 0.0;
	for (int i=0; i<ww; i++)
            for (int j=0; j<hh; j++) {
		gl = (int)(255.0*mtGray[xx+i][yy+j]);
                if ((gl >= 0) && (gl < 256)) 
                    fdp[gl] += 1.0;
	    }
        for (int i=0; i<256; i++) fdp[i] /= (double)(ww*hh);
	double entr = 0.0;
        for (int i=0; i<256; i++) 
            if (fdp[i] > 0.0)
               entr += (fdp[i]*Math.log(fdp[i])/Math.log(2.0));
        entr *= -1.0;
	return entr;
    }
    
    public void prepareDisplay() {
        oupImg = new BufferedImage(w, h, BufferedImage.TYPE_3BYTE_BGR);
    }

    public void drawCellMeanGrayLevel() {
        double[] cmean = new double[lpoints.size()+1];
        double[] csize = new double[lpoints.size()+1];
        for (int k=0; k<=lpoints.size(); k++)
            cmean[k] = 0.0;
	for (int i=0; i<w; i++)
            for (int j=0; j<h; j++) {
                 cmean[mtLab[i][j]] += mtGray[i][j]; 
                 csize[mtLab[i][j]] += 1.0; 
            }
        for (int k=0; k<=lpoints.size(); k++)
            cmean[k] /= csize[k];

        WritableRaster output = oupImg.getRaster();
        for (int i=0; i < w; i++)
            for (int j=0; j < h; j++) {
                int c = (int)(Math.floor(cmean[mtLab[i][j]]));
                output.setSample(i, j, 0, c);
                output.setSample(i, j, 1, c);
                output.setSample(i, j, 2, c);
            }
    }

    public void drawOriginal() {
        WritableRaster output = oupImg.getRaster();
        WritableRaster input = inpImg.copyData(null);
	for (int i=0; i<w-1; i++) {
            for (int j=0; j<h-1; j++) {
                output.setSample(i, j, 0, input.getSample(i, j, 0));
                output.setSample(i, j, 1, input.getSample(i, j, 0));
                output.setSample(i, j, 2, input.getSample(i, j, 0));
            }
	}
    }
    
    public void drawCellBounds() {
        WritableRaster output = oupImg.getRaster();
	for (int i=0; i<w-1; i++) {
            for (int j=0; j<h-1; j++) {
                if ((mtLab[i][j] != mtLab[i][j+1]) || (mtLab[i][j] != mtLab[i+1][j])) {
                   output.setSample(i, j, 0, 0);
                   output.setSample(i, j, 1, 0);
                   output.setSample(i, j, 2, 255);
		}
            }
	}
    }
    
    public void drawCellPoints() {
        WritableRaster output = oupImg.getRaster();
        Iterator kit = lpoints.iterator();
        while (kit.hasNext()) {
            Pixel px = (Pixel)kit.next();
            if ((px.getx() >= 0) && (px.gety() >= 0) && (px.getx() < w) && (px.gety() < h)) {
                output.setSample(px.getx(), px.gety(), 0, 0);
                output.setSample(px.getx(), px.gety(), 1, 255);
                output.setSample(px.getx(), px.gety(), 2, 0);
            }
            for (int k=0; k<8; k++) {
                int cx = px.getx() + cte.viz8x[k];
                int cy = px.gety() + cte.viz8y[k];
                if ((cx >= 0) && (cy >= 0) && (cx < w) && (cy < h)) {
                    if ((px.getx() > 0) && (px.gety() > 0) && (px.getx() < w) && (px.gety() < h)) {
                        output.setSample(cx, cy, 0, 0);
                        output.setSample(cx, cy, 1, 255);
                        output.setSample(cx, cy, 2, 0);
                    }
                }
            }
        }
    }
    
    public BufferedImage showResults(boolean original, boolean mean, boolean edges, boolean points) {
        prepareDisplay();
        if (original) drawOriginal();
        if (mean) drawCellMeanGrayLevel();
        if (edges) drawCellBounds();
        if (points) drawCellPoints();
        return (oupImg);
    }    
    
    public LinkedList getPoints() {
        return (lpoints);
    }
    
    public LinkedList getMarkers() {
        LinkedList markers = new LinkedList();
        Iterator lit = lpoints.iterator();
        while (lit.hasNext()) {
            Pixel ppx = (Pixel)lit.next();
            markers.addLast(ppx);
            for (int k=0; k<4; k++) {
                int xx = ppx.getx() + cte.viz4x[k];
                int yy = ppx.gety() + cte.viz4y[k];
                if ((xx >= 0) && (yy >= 0) && (xx < w) && (yy < h)) {
                    Pixel npx = new Pixel(xx, yy, (int)mtGray[xx][yy]);
                    npx.setLabel(ppx.getLabel());
                    markers.addLast(npx);
                }
            }
        }
        LinkedList markers2 = new LinkedList();
        Iterator mit = markers.iterator();
        while (mit.hasNext()) {
            Pixel ppx = (Pixel)mit.next();
            markers2.addLast(ppx);
            for (int k=0; k<4; k++) {
                int xx = ppx.getx() + cte.viz4x[k];
                int yy = ppx.gety() + cte.viz4y[k];
                if ((xx >= 0) && (yy >= 0) && (xx < w) && (yy < h)) {
                    Pixel npx = new Pixel(xx, yy, (int)mtGray[xx][yy]);
                    npx.setLabel(ppx.getLabel());
                    markers2.addLast(npx);
                }
            }
        }
        return markers2; 
    }    
    
    public int getNPoints() {
        return (lpoints.size());
    }
}


class QTpart {
    private int x;
    private int y;
    private int w;
    private int h;
    /** Creates a new instance of QTpoints */
    public QTpart(int x, int y, int w, int h) {
        this.x = x; this.y = y; this.w = w; this.h = h;
    }
    
    public int getX() { return x; }
    public int getY() { return y; }
    public int getW() { return w; }
    public int getH() { return h; }
    public int size() { return(w*h); }
    public int middleX() {
        return(x + w/2 + delta(5));
    }
    public int middleY() {
        return(y + h/2 + delta(5));
    }
    public int centerX() {
        return(x + w/2);
    }
    public int centerY() {
        return(y + h/2);
    }
    
    private int delta(int d) {
	int m=2*d+1;
	return(-d + (int)(Math.floor(m*Math.random())));
    }

    
}
