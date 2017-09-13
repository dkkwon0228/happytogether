/*

2009out13: afinamento baseado na implementacao do prof. Hae Yong Kim, extraida da biblioteca IMG.

javac Thinning.java

java Thinning thinning-ent.bmp

*/

package m00;

import java.awt.image.BufferedImage;
import java.awt.image.*;

public class Thinning {
    private Ferramentas fe = new Ferramentas();
    private int limiar = 1;
    private int[][] res;

    public Thinning(String fn) {
        BufferedImage im = fe.loadImage(fn);
        this.init(im);
    }
    public Thinning(BufferedImage im) {
        this.init(im);
    }
    public void init(BufferedImage im) {
        int[][] a = new int[im.getHeight()][im.getWidth()];
        WritableRaster inp = im.getRaster();
        for (int y = 0; y < im.getHeight(); y++)
            for (int x = 0; x < im.getWidth(); x++) {
                int gray = inp.getSample(x,y,0);
                //@@@@ neste exemplo, fundo branco e objeto em preto
                if (gray >= limiar)
                    a[y][x] = 0; // obj
                else
                    a[y][x] = 1; // fundo
            }
        this.init2(a);
    }

    //fundo branco, objeto em preto
    public Thinning(int[][] a) {
        this.init2(a);
    }
    public BufferedImage getBufferedImage() {
        return fe.imGray(res);
    }
    public void init2(int[][] a) {
        int cdqto=1;
        int[][] e1 = { {255,255,255}, {128, 0,128}, {0, 0, 0} };
        int[][] e2 = { {128,255,128}, {0,   0,255}, {0,0,128} };
        int[][] e3=rot270a(e1);
        int[][] e4=rot270a(e2);
        int[][] e5=rot270a(e3); 
        int[][] e6=rot270a(e4);
        int[][] e7=rot270a(e5); 
        int[][] e8=rot270a(e6);
        
        int[][] t;
        String st;
        int i=0; 
        boolean igual=false;
        while (!igual) {
            t=copiaMatriz(a);
            a=operadorOR(a,hitmissb(a,e1));
            a=operadorOR(a,hitmissb(a,e2));
            a=operadorOR(a,hitmissb(a,e3));
            a=operadorOR(a,hitmissb(a,e4));
            a=operadorOR(a,hitmissb(a,e5));
            a=operadorOR(a,hitmissb(a,e6));
            a=operadorOR(a,hitmissb(a,e7));
            a=operadorOR(a,hitmissb(a,e8));
            igual = operadorEQ(a,t);
            if (i%cdqto==0 || igual) {
                String fout = String.format("zzz-%03d.png",i);
                //fe.saveImage(fe.imGray(a),fout);
            }
            //System.out.println(i);
            i++;
        }
        int[][] e9 = { {128,255,128}, {255, 0,255}, {0, 0, 0} };
        int[][] e10=rot90a(e9);
        int[][] e11=rot90a(e10);
        int[][] e12=rot90a(e11);
        int[][] e13 = { {128,255,128}, {0, 0, 0}, {0, 0, 0} };
        int[][] e14=rot90a(e13);
        int[][] e15=rot90a(e14);
        int[][] e16=rot90a(e15);
        igual=false;
        while (!igual) {
            t=copiaMatriz(a);
            a=operadorOR(a,hitmissb(a,e9));
            a=operadorOR(a,hitmissb(a,e10));
            a=operadorOR(a,hitmissb(a,e11));
            a=operadorOR(a,hitmissb(a,e12));
            a=operadorOR(a,hitmissb(a,e13));
            a=operadorOR(a,hitmissb(a,e14));
            a=operadorOR(a,hitmissb(a,e15));
            a=operadorOR(a,hitmissb(a,e16));
            igual = operadorEQ(a,t);
            if (i%cdqto==0 || igual) {
                String fout = String.format("zzz-%03d.png",i);
                //fe.saveImage(fe.imGray(a),fout);
            }
            //System.out.println(i);
            i++;
        }
        this.res = a;
    }

    // Rotaciona 270 graus sentido anti-horario.
    public int[][] rot270a(int[][] a) {
        int nl = a.length;
        int nc = a[0].length;
        int[][] d = new int[nc][nl];
        for (int l=0; l<nl; l++)
            for (int c=0; c<nc; c++)
                if (nl-l-1 >= 0)
                    d[c][nl-l-1] = a[l][c];
        return d;
    }

    // Rotaciona 90 graus sentido anti-horario.
    public int[][] rot90a(int[][] a) {
        int nl = a.length;
        int nc = a[0].length;
        int[][] d = new int[nc][nl];
        for (int l=0; l<nl; l++)
            for (int c=0; c<nc; c++)
                if (nc-c-1 >= 0)
                    d[nc-c-1][l] = a[l][c];
        return d;
    }

    // Calcula hit-miss binario de a com elemento estruturante b.
    // Na imagem b, valores 0 e 255 tornam-se obrigatorios.
    // Qualquer valor diferente de 0 e 255 sao "don't care".
    // Se bate, fica branco (255). Nao batendo, fica preto (0).
    public int[][] hitmissb(int[][] a, int[][] b) {
        int nl = a.length;
        int nc = a[0].length;
        int[][] d = new int[nl][nc];
        int nl2 = b.length;
        int nc2 = b[0].length;
        int lc2 = nl2/2;
        int cc2 = nc2/2;
        for (int l=0; l<nl; l++)
            for (int c=0; c<nc; c++) {
                boolean bate=true;
                for (int l2=0; l2<nl2; l2++) {
                    boolean continua = true;
                    for (int c2=0; c2<nc2 && continua; c2++) {
                        if ((l-lc2+l2)>0&&(l-lc2+l2)<nl&&(c-cc2+c2)>0&&(c-cc2+c2)<nc&&
                             b[l2][c2]==0 && a[l-lc2+l2][c-cc2+c2]!=0) { 
                            bate=false; 
                            continua = false;
                        }
                        if ((l-lc2+l2)>0&&(l-lc2+l2)<nl&&(c-cc2+c2)>0&&(c-cc2+c2)<nc&&
                             continua && b[l2][c2]==255 && a[l-lc2+l2][c-cc2+c2]!=1) { 
                            bate=false; 
                            continua = false;
                        }
                    }
                    if (bate)
                        d[l][c]=1;
                    else
                        d[l][c]=0;
                }
            }
        return d;
    }
    
    public int[][] operadorOR(int[][] a, int[][] b) { 
        int nl = a.length;
        int nc = a[0].length;
        int nl2 = b.length;
        int nc2 = b[0].length;
        if (nl != nl2 || nc != nc2) {
            System.out.println("Erro (operadorOR): As imagens nao tem o mesmo tamanho!");
            System.exit(1);
        }
        int[][] d = new int[nl][nc];
        for (int l=0; l<nl; l++)
            for (int c=0; c<nc; c++)
                if (b[l][c] != 0 || a[l][c] != 0)
                    d[l][c]= 1;
                else
                    d[l][c]= 0;
        return d;
    }

    public boolean operadorEQ (int[][] a, int[][] b) { 
        int nl = a.length;
        int nc = a[0].length;
        int nl2 = b.length;
        int nc2 = b[0].length;
        if (nl != nl2 || nc != nc2)
            return false;
        for (int l=0; l<nl; l++)
            for (int c=0; c<nc; c++)
                if (a[l][c]!=b[l][c]) 
                    return false;
        return true;
    }

    public int[][] copiaMatriz(int[][] a) {
        int nl = a.length;
        int nc = a[0].length;
        int[][] d = new int[nl][nc];
        for (int l=0; l<nl; l++)
            for (int c=0; c<nc; c++)
                d[l][c] = a[l][c];
        return d;
    }

    /*
    public static void main( String args[] ) {
	if (args.length == 1) {
            String fn = args[0];
            Thinning s = new Thinning(fn);
        }
        else {
            System.out.println("Use: java Thinning imagem");
        }
    }
    */
}
