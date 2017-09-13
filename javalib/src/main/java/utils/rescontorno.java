/*

2008abr01

Programa que dado uma mapa de rotulos calcula os contornos da segmentacao.

java rescontorno ../res/01-esk-a04.png ../pics/01-mdeskimo.png
java rescontorno ../res/02-roc-a05.png ../pics/02-mdrocks.png
java rescontorno ../res/03-pir-a04.png ../pics/03-mdpiram.png
java rescontorno ../res/04-arv-a02.png ../pics/04-mdarv.png

java rescontorno ../res/01-esk-consul-a02d005.png ../pics/01-mdeskimo.png
java rescontorno ../res/02-roc-consul-a02d005.png ../pics/02-mdrocks.png
java rescontorno ../res/03-pir-consul-a02d005.png ../pics/03-mdpiram.png
java rescontorno ../res/04-arv-consul-a02d005.png ../pics/04-mdarv.png

*/

package utils;

import m00.*;
import m01.*;
import java.awt.image.*;
import java.awt.*;

public class rescontorno {

    BufferedImage res;
    private Ferramentas1 fe = new Ferramentas1();

    public rescontorno(String fn1, String fn2) {
        BufferedImage mapa = fe.loadImage(fn1);
        BufferedImage im = null;
        if (fn2 != null) {
            BufferedImage tmp = fe.loadImage(fn2);
            Conversion opConv = new Conversion(tmp, true);
            BufferedImage imRGB = opConv.imgConverted();
            im = imRGB;
        }
        int len = 2;
        init(mapa, im,len);
    }
    public rescontorno(String fn1, String fn2, int len) {
        BufferedImage mapa = fe.loadImage(fn1);
        BufferedImage im = null;
        if (fn2 != null) {
            BufferedImage tmp = fe.loadImage(fn2);
            Conversion opConv = new Conversion(tmp, true);
            BufferedImage imRGB = opConv.imgConverted();
            im = imRGB;
        }
        init(mapa, im,len);
    }
    public rescontorno(BufferedImage mapa, BufferedImage im) {
        int len = 2;
        init(mapa,im, len);
    }
    public rescontorno(BufferedImage mapa, BufferedImage im, int len) {
        init(mapa,im, len);
    }
    public void init(BufferedImage mapa, BufferedImage im, int len) {
        if (im == null)
	    im = new BufferedImage(mapa.getWidth(), mapa.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
        int w = mapa.getWidth();
        int h = mapa.getHeight();
        BufferedImage preto = new BufferedImage(w, h, BufferedImage.TYPE_3BYTE_BGR);
        boolean[][] contornob = fe.calcContorno(mapa);
        BufferedImage contorno = fe.calcContornoRegioes(preto, contornob, Color.white);
        //fe.saveImage(contorno, "zzz1.png");
        //int len = 2;
        BufferedImage contorno2 = this.engrossa(contorno, len);
        //fe.saveImage(contorno2, "zzz2.png");
        contorno2 = this.transparente(contorno2, Color.white);
        //fe.saveImage(contorno2, "zzz3.png");
        this.res = sobrepoe(contorno2, im);
        //fe.saveImage(this.res, "zzz4.png");
    }

    // engrossa contorno e sobrepoe sobre imagem de entrada (translucida)
    private BufferedImage sobrepoe(BufferedImage contorno, BufferedImage img) {
	BufferedImage branco = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
	this.preenche(branco,Color.white);
        BufferedImage dest = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_ARGB);  
        Graphics2D destG = dest.createGraphics();
        destG.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC, 1.0f));
        destG.drawImage(branco, 0, 0, null);
        destG.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.6f));
        destG.drawImage(img, 0, 0, null);
        destG.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
        destG.drawImage(contorno, 0, 0, null);
	return dest;
    }

    private BufferedImage transparente(BufferedImage img, Color corTransp) {
	int ctR = corTransp.getRed();
	int ctG = corTransp.getGreen();
	int ctB = corTransp.getBlue();
        WritableRaster inp = img.copyData(null);
	int w = img.getWidth();
	int h = img.getHeight();
        BufferedImage transp = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        WritableRaster oup = transp.getAlphaRaster();
        for (int x=0; x<w; x++)
            for (int y=0; y<h; y++) {
		int r = inp.getSample(x,y,0);
		int g = inp.getSample(x,y,1);
		int b = inp.getSample(x,y,2);
		if (r == ctR && g == ctG && b == ctB)
		    oup.setSample(x,y,0,0);
		else
		    oup.setSample(x,y,0,255);
	    }
	return transp;
    }

    //fundo branco, linhas pretas
    private BufferedImage engrossa(BufferedImage im, int len) {
        boolean[][] a = calculaboolean(im);
        int w = im.getWidth();
        int h = im.getHeight();
        BufferedImage contorno = new BufferedImage(w, h, BufferedImage.TYPE_3BYTE_BGR);
        this.preenche(contorno,Color.white); // fundo branco
        Graphics2D g2d = contorno.createGraphics();
        g2d.setColor(Color.black);
        for (int x=0; x<w; x++)
            for (int y=0; y<h; y++)
		if (a[x][y])
		    g2d.fillRect(x, y, len, len);
        return contorno;
    }

    private boolean[][] calculaboolean(BufferedImage im) {
	int w = im.getWidth();
	int h = im.getHeight();
	boolean[][] a = new boolean[w][h];
        WritableRaster inp = im.copyData(null);
        for (int x=0; x<w; x++)
            for (int y=0; y<h; y++) {
		int r = inp.getSample(x,y,0);
		int g = inp.getSample(x,y,1);
		int b = inp.getSample(x,y,2);
		if (r == 0 && g == 0 && b == 0)
		    a[x][y] = false;
		else
		    a[x][y] = true;
	    }
	return a;
    }

    private void preenche(BufferedImage img, Color cor) {
        Graphics2D g2d = img.createGraphics();
        g2d.setColor(cor);
        g2d.fillRect(0,0,img.getWidth(),img.getHeight());
    }

    public BufferedImage getResult() {
	return res;
    }

    public static void main (String args[]) {
	if (args.length == 2) {
	    String fn1, fn2 = null;
	    fn1 = args[0];
	    fn2 = args[1];
	    rescontorno a = new rescontorno(fn1, fn2);
	    BufferedImage r = a.getResult();
	    Ferramentas.saveImage(r, "zzz.png");
	}
	else 
	    System.out.println("Programa para extrair contorno de um mapa de rotulos.\nUso: java rescontorno mapa image");
    }
}
