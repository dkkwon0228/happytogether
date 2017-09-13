package utils;/*

2008nov17

Branqueia imagem (branco transparente para deixar desbotado).

*/

//package utils;

import m00.*;
import m01.*;
import java.awt.image.*;
import java.awt.*;

public class branqueia {

    BufferedImage res;
    private Ferramentas1 fe = new Ferramentas1();

    public branqueia(String fn1) {
	BufferedImage im = fe.loadImage(fn1);
   	init(im);
    }
    public void init(BufferedImage im) {
	int w = im.getWidth();
	int h = im.getHeight();
	this.res = sobrepoe(im);
	//fe.saveImage(this.res, "zzz.png");
    }

    // engrossa contorno e sobrepoe sobre imagem de entrada (translucida)
    private BufferedImage sobrepoe(BufferedImage img) {
	BufferedImage branco = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
	this.preenche(branco,Color.white);
        BufferedImage dest = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_ARGB);  
        Graphics2D destG = dest.createGraphics();
        destG.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC, 1.0f));
        destG.drawImage(branco, 0, 0, null);
        destG.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.6f));
        destG.drawImage(img, 0, 0, null);
	return dest;
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
	if (args.length == 1) {
	    String fn1;
	    fn1 = args[0];
	    branqueia a = new branqueia(fn1);
	    BufferedImage r = a.getResult();
	    Ferramentas.saveImage(r, "zzz.png");
	}
	else 
	    System.out.println("Uso: java branqueia image");
    }
}
