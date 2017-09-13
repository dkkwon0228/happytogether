/*

2008mar18

tools to open / copy / save images


*/

package m00;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.*;
import java.awt.image.*;
import java.util.*;


////////////////////////////////////////////////////////////////////////////
public class Ferramentas {
    private static String msg0a = "Error: ";
    private static String msg0b = "Aborting...";
    private static String msg1 = "Could not read the input image! ";
    private static String msg2 = "";//"Maybe image format not supported! Please try jpg, bmp or png. ";
    private static String msg3 = "Image format not supported! Please try jpg or bmp. ";
    protected Consts cts = new Consts();

    public double[] readTextLineDoubles(DataInputStream d) {
        int max = 100;
        double[] res = new double[max];
        double[] res2 = null;
        for (int i=0; i<max; i++)
            res[i] = -1.0;
        try {
            String s = d.readLine();
            int pos = -1;
            if (s != null) {
                StringTokenizer parser = new StringTokenizer(s);
                while (parser.hasMoreTokens()) {
                    String s2 = parser.nextToken();
                    pos++;
                    res[pos] = Double.parseDouble(s2);
                }
                res2 = new double[pos+1];
                for (int i=0; i<=pos; i++)
                    res2[i] = res[i];
            }
        }
        catch (IOException e) {
          System.err.println ("Unable to read from file");
          System.exit(-1);
        }
        return res2;
    }

    public byte[] loadBinaryFile(String fn) {
        byte[] bytearray = null;
        try {   
 
            FileInputStream fileinputstream = new FileInputStream(fn);
            int numberBytes = fileinputstream.available();
            bytearray = new byte[numberBytes];
            fileinputstream.read(bytearray);
            //System.out.println("total bytes: "+numberBytes);
            fileinputstream.close();        

        } catch (Exception e) {   
            e.printStackTrace();   
            System.out.println("Erro: loadBinaryFile!");
            System.exit(1);
        }
        return bytearray;
    }
    public void writeBinaryFile(byte[] data, String fout) {
        try {   
            FileOutputStream fileoutputstream = new FileOutputStream(fout);
            for (int i = 0; i < data.length; i++) {
                fileoutputstream.write(data[i]);
            }
            fileoutputstream.close();
        } catch (Exception e) {   
            e.printStackTrace();   
            System.out.println("Erro: writeBinaryFile!");
            System.exit(1);
        }
    }
    public void writeBinaryFile(short[] data, String fout) {
        byte[] tmpb = this.toByteArray(data);
        this.writeBinaryFile(tmpb, fout);
    }
    public void writeBinaryFile(int[] data, String fout) {
        byte[] tmpb = this.toByteArray(data);
        this.writeBinaryFile(tmpb, fout);
    }
    public void writeBinaryFile(float[] data, String fout) {
        byte[] tmpb = this.toByteArray(data);
        this.writeBinaryFile(tmpb, fout);
    }
    public float[] readBinaryFileFloat(String fn) {
        byte[] tmpb2 = this.loadBinaryFile(fn);
        float[] tmpf2 = this.toFloatArray(tmpb2);
        return tmpf2;
    }
    public short[] readBinaryFileShort(String fn) {
        byte[] tmpb2 = this.loadBinaryFile(fn);
        short[] tmpf2 = this.toShortArray(tmpb2);
        return tmpf2;
    }

    public static byte[] toByteArray(float data) {
        return toByteArray(Float.floatToRawIntBits(data));
    }
    public static byte[] toByteArray(float[] data) {
        if (data == null) return null;
        byte[] byts = new byte[data.length * 4];
        for (int i = 0; i < data.length; i++)
            System.arraycopy(toByteArray(data[i]), 0, byts, i * 4, 4);
        return byts;
    }
    public static byte[] toByteArray(int data) {
        return new byte[] {
            (byte)((data >> 24) & 0xff),
            (byte)((data >> 16) & 0xff),
            (byte)((data >> 8) & 0xff),
            (byte)((data >> 0) & 0xff),
        };
    }
    public static byte[] toByteArray(int[] data) {
        if (data == null) return null;
        byte[] byts = new byte[data.length * 4];
        for (int i = 0; i < data.length; i++)
            System.arraycopy(toByteArray(data[i]), 0, byts, i * 4, 4);
        return byts;
    }
    public static byte[] toByteArray(short data) {
        return new byte[] {
            (byte)((data >> 8) & 0xff),
            (byte)((data >> 0) & 0xff),
        };
    }
    public static byte[] toByteArray(short[] data) {
        if (data == null) return null;
        byte[] byts = new byte[data.length * 2];
        for (int i = 0; i < data.length; i++)
            System.arraycopy(toByteArray(data[i]), 0, byts, i * 2, 2);
        return byts;
    }
    public static short toShort(byte[] data) {
        if (data == null || data.length != 2) return 0x0;
        return (short)(
                       (0xff & data[0]) << 8 |
                       (0xff & data[1]) << 0
                       );
    }
    public static short[] toShortArray(byte[] data) {
        if (data == null || data.length % 2 != 0) return null;
        short[] shts = new short[data.length / 2];
        for (int i = 0; i < shts.length; i++) {
            shts[i] = toShort( new byte[] {
                    data[(i*2)],
                    data[(i*2)+1]
                } );
        }
        return shts;
    }
    public static int toInt(byte[] data) {
        if (data == null || data.length != 4) return 0x0;
        return (int)( // NOTE: type cast not necessary for int
                     (0xff & data[0]) << 24 |
                     (0xff & data[1]) << 16 |
                     (0xff & data[2]) << 8 |
                     (0xff & data[3]) << 0
                      );
    }
    public static int[] toIntArray(byte[] data) {
        if (data == null || data.length % 4 != 0) return null;
        int[] ints = new int[data.length / 4];
        for (int i = 0; i < ints.length; i++)
            ints[i] = toInt( new byte[] {
                    data[(i*4)],
                    data[(i*4)+1],
                    data[(i*4)+2],
                    data[(i*4)+3],
                } );
        return ints;
    }
    public static float toFloat(byte[] data) {
        if (data == null || data.length != 4) return 0x0;
        return Float.intBitsToFloat(toInt(data));
    }
    public static float[] toFloatArray(byte[] data) {
        if (data == null || data.length % 4 != 0) return null;
        float[] flts = new float[data.length / 4];
        for (int i = 0; i < flts.length; i++) {
            flts[i] = toFloat( new byte[] {
                    data[(i*4)],
                    data[(i*4)+1],
                    data[(i*4)+2],
                    data[(i*4)+3],
                } );
        }
        return flts;
    }
    
    public BufferedImage inverteImGray(BufferedImage im) {
        int w = im.getWidth();
        int h = im.getHeight();
        BufferedImage res = new BufferedImage(w, h, BufferedImage.TYPE_BYTE_GRAY);
        WritableRaster oup = res.getRaster();
        WritableRaster inp = im.getRaster();
        for (int x = 0; x < w; x++)
            for (int y = 0; y < h; y++) {
                int gray = inp.getSample(x,y,0);
                oup.setSample(x,y,0,255-gray);
            }
        return res;
    }

    //2010ago03: engrossa cada pixel de cor 'cor', desenhando um quadrado de lado 'tam'
    public BufferedImage engrossaRGB(BufferedImage im, Color cor, int tam) {
        int w = im.getWidth();
        int h = im.getHeight();
        BufferedImage dest = new BufferedImage(w, h, BufferedImage.TYPE_3BYTE_BGR);  
        WritableRaster oup = dest.getRaster();
        WritableRaster inp = im.getRaster();
        Graphics2D g2d = dest.createGraphics();
        g2d.setColor(cor);
        for (int x = 0; x < w; x++)
            for (int y = 0; y < h; y++) {
                if (sameRGB(inp, x, y, cor)) 
                    g2d.fillRect(x-tam/2, y-tam/2, tam, tam);
                else
                    for (int c=0; c<3; c++)
                        oup.setSample(x, y, c, inp.getSample(x,y,c));
            }
        return dest;
    }

    public BufferedImage montaImagemSobreposta(BufferedImage im, BufferedImage map, float alpha) {
        BufferedImage dest = new BufferedImage(im.getWidth(), im.getHeight(), BufferedImage.TYPE_INT_ARGB);  
        Graphics2D destG = dest.createGraphics();
        destG.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC, 1.0f));
        destG.drawImage(im, 0, 0, null);
        destG.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
        destG.drawImage(map, 0, 0, null);
        return dest;
    }

    public BufferedImage montaImagemSobreposta(BufferedImage im, BufferedImage strk) {
        BufferedImage dest = new BufferedImage(im.getWidth(), im.getHeight(), BufferedImage.TYPE_INT_ARGB);  
        Graphics2D destG = dest.createGraphics();
        destG.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC, 1.0f));
        destG.drawImage(im, 0, 0, null);
        destG.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
        destG.drawImage(strk, 0, 0, null);
        return dest;
    }

    public BufferedImage incluiMoldura(BufferedImage im, int largura) {
        int w = im.getWidth() + largura*2;
        int h = im.getHeight() + largura*2;
        BufferedImage res = new BufferedImage(w, h, BufferedImage.TYPE_BYTE_GRAY);;
        WritableRaster oup = res.getRaster();
        WritableRaster inp = im.getRaster();
        for (int x = 0; x < w; x++)
            for (int y = 0; y < h; y++)
                oup.setSample(x,y,0,0);
        for (int x = 0+largura; x < w-largura; x++)
            for (int y = 0+largura; y < h-largura; y++) {
                int gray = inp.getSample(x-largura, y-largura, 0);
                oup.setSample(x,y,0,gray);
            }
        return res;
    }

    public void replaceColor(BufferedImage imRGB, Color oldc, Color newc) {
        WritableRaster inp = imRGB.getRaster();
        int w = imRGB.getWidth();
        int h = imRGB.getHeight();
        int[] rgb = new int[3];
        int[] rgb2 = new int[3];
        rgb2[0] = newc.getRed();
        rgb2[1] = newc.getGreen();
        rgb2[2] = newc.getBlue();
        for (int y = 0; y < h; y++)
            for (int x = 0; x < w; x++) {
                for (int c = 0; c < 3; c++)
                    rgb[c] = inp.getSample(x,y,c);
                if (this.sameRGB(new Color(rgb[0],rgb[1],rgb[2]), oldc))
                    for (int c = 0; c < 3; c++)
                        inp.setSample(x,y,c, rgb2[c]);
            }
    }

    public double[][] vector2matrixRow(double[] p, int totlin, int totcol) {
        double[][] mp = new double[totlin][totcol];
        for (int y = 0; y < totlin; y++)
            for (int x = 0; x < totcol; x++)
                mp[y][x] = p[y*totcol+x];
        return mp;
    }

    public double[] matrix2vectorCol(double[][] m) {
        int totlin = m.length;
        int totcol = m[0].length;
        double v[] = new double[totlin*totcol];
        //converte para vetor (por coluna)
        for (int x = 0; x < totcol; x++)
            for (int y = 0; y < totlin; y++)
                v[x*totlin+y] = m[y][x];
        return v;
    }

    public int[][] transposta(int[][] m) {
        int totlin = m.length;
        int totcol = m[0].length;
        //inverte
        int[][] mt = new int[totcol][totlin];
        for (int i = 0; i < totlin; i++)
            for (int j = 0; j < totcol; j++)
                mt[j][i] = m[i][j];
        return mt;
    }
    public int[][] copyMatrix(int[][] m) {
        int totlin = m.length;
        int totcol = m[0].length;
        int[][] mc = new int[totlin][totcol];
        for (int i = 0; i < totlin; i++)
            for (int j = 0; j < totcol; j++)
                mc[i][j] = m[i][j];
        return mc;
    }

    public BufferedImage sobrepoe(BufferedImage bg, BufferedImage fg) {
        int w = bg.getWidth();
        int h = bg.getHeight();
        BufferedImage res = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);  
        Graphics2D g2d = res.createGraphics();
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC, 1.0f));
        g2d.drawImage(bg, 0, 0, null);
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));
        g2d.drawImage(fg, 0, 0, null);
	return res;
    }

	//indices 0..totv-1; total de indices aleatorios distintos = totr
        //devolve vetor de indices escolhidos aleatoriamente
	public int[] randomIndices(int totv, int totr) {
		Date da = new Date();
		long seed = da.getTime();
		Random rn = new Random(seed);

		int[] vind = new int[totr];
		boolean[] escolhido = new boolean[totv];
		for (int i = 0; i < totv; i++)
			escolhido[i] = false;
		while (totr > 0) {
			int ri = rn.nextInt(totv);
			if (!escolhido[ri]) {
				escolhido[ri] = true;
				totr--;
				vind[totr] = ri;
			}
		}
		Arrays.sort(vind);
		return vind;
	}

	public float[] vecInt2Float(int v[]) {
		int tot = v.length;
		float[] f = new float[tot];
		for (int i = 0; i < tot; i++)
			f[i] = (float) v[i];
		return f;
	}

    //concatena um do lado do outro
    public static BufferedImage concatImageGray(BufferedImage im1, BufferedImage im2) {
        int w1 = im1.getWidth();
        int h1 = im1.getHeight();
        int w2 = im2.getWidth();
        int h2 = im2.getHeight();
        int w = w1+w2;
        int h = Math.max(h1,h2);
        BufferedImage imGray = new BufferedImage(w, h, BufferedImage.TYPE_BYTE_GRAY);
        
        Graphics2D g2d = imGray.createGraphics();
        g2d.setColor(Color.white);
        g2d.fillRect(0, 0, w, h);
        Graphics g = imGray.getGraphics(); 
        g.drawImage(im1, 0, 0, null); //-> OBS: cor transparente fica preto !!!
        
        WritableRaster oup = imGray.getRaster();
        WritableRaster inp = im2.copyData(null);
        for (int x = 0; x < w2; x++)
            for (int y = 0; y < h2; y++)
                oup.setSample(x+w1,y,0,inp.getSample(x,y,0));
        return imGray;
    }
    //concatena um abaixo do outro
    public static BufferedImage concatImageGray2(BufferedImage im1, BufferedImage im2) {
        int w1 = im1.getWidth();
        int h1 = im1.getHeight();
        int w2 = im2.getWidth();
        int h2 = im2.getHeight();
        int w = Math.max(w1,w2);
        int h = h1+h2;
        BufferedImage imGray = new BufferedImage(w, h, BufferedImage.TYPE_BYTE_GRAY);
        
        Graphics2D g2d = imGray.createGraphics();
        g2d.setColor(Color.white);
        g2d.fillRect(0, 0, w, h);
        Graphics g = imGray.getGraphics(); 
        g.drawImage(im1, 0, 0, null); //-> OBS: cor transparente fica preto !!!
        
        WritableRaster oup = imGray.getRaster();
        WritableRaster inp = im2.copyData(null);
        for (int x = 0; x < w2; x++)
            for (int y = 0; y < h2; y++)
                oup.setSample(x,y+h1,0,inp.getSample(x,y,0));
        return imGray;
    }

    //concatena um do lado do outro
    public static BufferedImage concatImageRGB(BufferedImage im1, BufferedImage im2) {
        int w1 = im1.getWidth();
        int h1 = im1.getHeight();
        int w2 = im2.getWidth();
        int h2 = im2.getHeight();
        int w = w1+w2;
        int h = Math.max(h1,h2);
        BufferedImage imRGB = new BufferedImage(w, h, BufferedImage.TYPE_3BYTE_BGR);
        
        Graphics2D g2d = imRGB.createGraphics();
        g2d.setColor(Color.white);
        g2d.fillRect(0, 0, w, h);
        Graphics g = imRGB.getGraphics(); 
        g.drawImage(im1, 0, 0, null); //-> OBS: cor transparente fica preto !!!
        
        WritableRaster oup = imRGB.getRaster();
        WritableRaster inp = im2.copyData(null);
        for (int x = 0; x < w2; x++)
            for (int y = 0; y < h2; y++)
                for (int c = 0; c < 3; c++)
                    oup.setSample(x+w1,y,c,inp.getSample(x,y,c));
        return imRGB;
    }

    // desempate de rotulos
    //desempata para calculo de isomorfismo (na verdade, (inexact) Maximal Common Subgraph)
    public int[] desempataRotulos(int[] result, float[] mcost, int totlabels) {
        int totvi = result.length;
        int totvm = totlabels;
        //para cada rotulo vm, guarda os vi's associados a este vm
        int[][] r = new int[totvm][totvi];
        int[] contv = new int[totvm];
        for (int i=0; i<totvm; i++) {
            contv[i] = 0; //conta qtos vi's estao associados ao vm
            for (int j=0; j<totvi; j++)
                r[i][j] = -1; //vazio
        }
        for (int i=0; i<totvi; i++) {
            int vmid = result[i];
            r[vmid][contv[vmid]] = i;
            contv[vmid]++;
        }
        /*
          for (int i=0; i<this.totvm; i++) {
          System.out.print("\n"+(i+1)+": ");
          for (int j=0; j<contv[i]; j++)
          System.out.print((r[i][j]+1) + "("+mcost[r[i][j]]+"); ");
          }
        */
        //para cada vm, pega o vi de custo minimo
        for (int i=0; i<totvm; i++) {
            if (contv[i] > 1) {
                int min = r[i][0];
                float minc = mcost[min];
                for (int j=1; j<contv[i]; j++) {
                    int viid = r[i][j];
                    if (mcost[viid] < minc) {
                        min = viid;
                        minc = mcost[min];
                    }
                }
                for (int j=0; j<contv[i]; j++) {
                    int viid = r[i][j];
                    result[viid] = -2;
                }
                result[min] = i;
            }
        }
        return result;
    }    


    public float calcDmax(LinkedList ldx, LinkedList ldy) {
        Iterator itx = ldx.iterator();
        Iterator ity = ldy.iterator();
        int tot = ldx.size();
        int[] vx = new int[tot];
        int[] vy = new int[tot];
        int cont = 0;
        while (itx.hasNext()) {
            int x = ((Integer)itx.next()).intValue();
            int y = ((Integer)ity.next()).intValue();
            vx[cont] = x;
            vy[cont] = y;
            cont++;
        }
        return this.calcDmax(vx, vy);
    }
    public float calcDmax(float[][] xy) {
        int tot = xy.length;
        int[] vx = new int[tot];
        int[] vy = new int[tot];
        for (int i = 0; i < tot; i++) {
            vx[i] = (int)xy[i][0];
            vy[i] = (int)xy[i][1];
        }
        return this.calcDmax(vx, vy);
    }
    public float calcDmax(int[] vx, int[] vy) {
        int tot = vx.length;
        float max = 0.0f;
        for (int i = 0; i < tot; i++)
            for (int j = 0; j < tot; j++) {
                float dist = (float) this.euclideanDistance(vx[i],vy[i], vx[j],vy[j]);
                if (dist > max)
                    max = dist;
            }
        return max;
    }

    public int[][] regionGrowing(int[][] mtLab, int[] totc) {
        int borda = -9999; //valor inexistente para considerar todos os rotulos
        return regionGrowing(mtLab, totc, borda);
    }
    public int[][] regionGrowing(int[][] mtLab, int[] totc, int ignorado) {
        int w = mtLab.length;
        int h = mtLab[0].length;
//System.out.println("w="+w+"  h="+h);
        int[][] rotulos = new int[w][h];
        for (int x=0; x<w; x++)
            for (int y=0; y<h; y++)
                rotulos[x][y] = -1;
        int r = -1;
        LinkedList lp = new LinkedList();
        for (int x=0; x<w; x++)
            for (int y=0; y<h; y++) {
                if (rotulos[x][y] == -1 && mtLab[x][y] != ignorado) {
                    r++;
                    lp.addFirst(new Coords2d(x,y));
                }
                while(!lp.isEmpty()) {
                    Coords2d p = (Coords2d)lp.removeFirst();
                    int x3 = p.getx(), y3 = p.gety();
                    rotulos[x3][y3] = r;
                    for (int k=0; k<4; k++) {
                        int x2 = x3 + cts.viz4x[k];
                        int y2 = y3 + cts.viz4y[k];
                        if (x2 >= 0 && x2 < w && y2 >= 0 && y2 < h && rotulos[x2][y2] == -1) {
//System.out.println("x3="+x3+"  y3="+y3+"       x2="+x2+"  y2="+y2);
                            if (mtLab[x3][y3]==mtLab[x2][y2])
                                lp.addFirst(new Coords2d(x2,y2));
                        }
                    }
                }
            }
        totc[0] = r+1; //somente para devolver o total de componentes ou regioes
        /*
        for (int x=0; x<w; x++)
            for (int y=0; y<h; y++)
                if (rotulos[x][y] == -1) {
                    System.out.println("Error: comps; rotulos[x][y] == -1");
                    System.exit(1);
                }
                */
        return rotulos;
    }

    private void insereCor(Color cor, LinkedList lcor) {
        if (lcor.size() <= 0)
            lcor.addLast(cor);
        else {
            Iterator it = lcor.iterator();
            boolean achou = false;
            while(it.hasNext() && !achou) {
                Color c = (Color)it.next();
                if (this.sameRGB(c, cor))
                    achou = true;
            }
            if (!achou)
                lcor.addLast(cor);
        }
    }

    public void drawPoints(BufferedImage im, float[][] pointxy, Color cor, int squareLen) {
        int tot = pointxy.length;
        Color[] vcor = new Color[tot];
        for (int i = 0; i < tot; i++)
            vcor[i] = cor;
        this.drawPoints(im, pointxy, vcor, squareLen);
    }
    public void drawPoints(BufferedImage im, float[][] pointxy, Color[] cor, int squareLen) {
        Graphics2D g2d = im.createGraphics();
        int tot = pointxy.length;
        for (int i = 0; i < tot; i++) {
            g2d.setColor(cor[i]);
            int x = (int)pointxy[i][0];
            int y = (int)pointxy[i][1];
//System.out.println(x+" "+y);
            //g2d.fillRect(x-squareLen/2, y-squareLen/2, squareLen, squareLen);
            g2d.fillOval(x-squareLen/2, y-squareLen/2, squareLen, squareLen);
            //g2d.setColor(Color.black);
            //g2d.drawOval(x-squareLen/2, y-squareLen/2, squareLen, squareLen);
        }
    }

    public BufferedImage imGray(int[][] mtLab) {
        int totlin = mtLab.length;
        int totcol = mtLab[0].length;
        BufferedImage res = new BufferedImage(totcol, totlin, BufferedImage.TYPE_BYTE_GRAY);
        WritableRaster oup = res.getRaster();
        for (int y = 0; y < totlin; y++)
            for (int x = 0; x < totcol; x++)
                oup.setSample(x,y,0,mtLab[y][x]*255);
        return res;
    }
    public BufferedImage imGray(double[][] mtLab) {
        int totlin = mtLab.length;
        int totcol = mtLab[0].length;
        BufferedImage res = new BufferedImage(totcol, totlin, BufferedImage.TYPE_BYTE_GRAY);
        WritableRaster oup = res.getRaster();
        for (int y = 0; y < totlin; y++)
            for (int x = 0; x < totcol; x++)
                oup.setSample(x,y,0,(int)(mtLab[y][x]*255.0));
        return res;
    }

    public BufferedImage imGray(int[][] mtLab, int mult) {
        int w = mtLab.length;
        int h = mtLab[0].length;
        BufferedImage res = new BufferedImage(w, h, BufferedImage.TYPE_BYTE_GRAY);
        WritableRaster oup = res.getRaster();
        for (int x = 0; x < w; x++)
            for (int y = 0; y < h; y++)
                oup.setSample(x,y,0,(mtLab[x][y]*mult)%256);
        return res;
    }
    public BufferedImage imRGB(int[][] mtLab, Color cor0, Color cor1) {
        int w = mtLab.length;
        int h = mtLab[0].length;
        BufferedImage res = new BufferedImage(w, h, BufferedImage.TYPE_3BYTE_BGR);
        WritableRaster oup = res.getRaster();
        for (int x = 0; x < w; x++)
            for (int y = 0; y < h; y++)
                if (mtLab[x][y] == 0) {
                    int r = cor0.getRed();
                    int g = cor0.getGreen();
                    int b = cor0.getBlue();
                    oup.setSample(x,y,0,r);
                    oup.setSample(x,y,1,g);
                    oup.setSample(x,y,2,b);
                }
                else {
                    int r = cor1.getRed();
                    int g = cor1.getGreen();
                    int b = cor1.getBlue();
                    oup.setSample(x,y,0,r);
                    oup.setSample(x,y,1,g);
                    oup.setSample(x,y,2,b);
                }
        return res;
    }

    public BufferedImage imVaziaRGB(Color cor, int w, int h) {
        BufferedImage res = new BufferedImage(w, h, BufferedImage.TYPE_3BYTE_BGR);
        this.limpaImagem(res, cor);
        return res;
    }
    public void limpaImagem(BufferedImage im, Color cor) {
        Graphics2D g2d = im.createGraphics();
        g2d.setColor(cor);
        g2d.fillRect(0,0,im.getWidth(),im.getHeight());
    }

    //debug
    public void saveImagesComponents(int[][] rotulos, int totc, String fout) {
        int w = rotulos.length;
        int h = rotulos[0].length;
        //imprime cada regiao
        for (int i = 0; i < totc; i++) {
            int[][] tmprot = new int[w][h];
            for (int x = 0; x < w; x++)
                for (int y = 0; y < h; y++) {
                    if (rotulos[x][y] == i)
                        tmprot[x][y] = i;
                    else
                        tmprot[x][y] = -1;
                }
            //componente preto, fundo branco
            String fout2 = String.format(fout, i);
            //String fout = String.format("zzz-imgray-rotulos-%03d.png", i);
            this.saveImage(this.imGray(tmprot,1), fout2);
        }
    }
    public void saveImagesComponentsColor(int[][] rotulos, Color[] cores, String fout) {
        int totc = cores.length;
        int w = rotulos.length;
        int h = rotulos[0].length;
        //imprime cada regiao
        for (int i = 0; i < totc; i++) {
            int[][] tmprot = new int[w][h];
            for (int x = 0; x < w; x++)
                for (int y = 0; y < h; y++) {
                    if (rotulos[x][y] == i)
                        tmprot[x][y] = 1;
                    else
                        tmprot[x][y] = 0;
                }
            //componente preto, fundo branco
            String fout2 = String.format(fout, i);
            //String fout = String.format("zzz-imgray-rotulos-%03d.png", i);
            this.saveImage(this.imRGB(tmprot,Color.white,cores[i]), fout2);
        }
    }

    public int[] getBoundingBoxStroke(BufferedImage strk, Color corFundo) {
        //BufferedImage strk = this.drawPanel.getStrokeImage();
        int w = strk.getWidth();
        int h = strk.getHeight();
        //procura canto esq superior e dir inferior do stroke
        int minx=-1, maxx=-1, miny=-1, maxy=-1;
        boolean empty = true;
        WritableRaster inp = strk.copyData(null);
        for(int y = 0; y < h; y++)  
            for(int x = 0; x < w; x++)
            //if(strk.getRGB(x, y) != 0xffffff) {
            if (inp.getSample(x,y,0)!=corFundo.getRed() ||
                inp.getSample(x,y,1)!=corFundo.getGreen() ||
                inp.getSample(x,y,2)!=corFundo.getBlue()) {
                empty = false;
                if (x < minx || minx == -1) minx = x;
                if (x > maxx || maxx == -1) maxx = x;
                if (y < miny || miny == -1) miny = y;
                if (y > maxy || maxy == -1) maxy = y;
            }
        int topx=minx, topy=miny, botx=maxx, boty=maxy;
        int[] res = null;
        if (!empty) {
            res = new int[4];
            res[0] = topx;
            res[1] = topy;
            res[2] = botx;
            res[3] = boty;
        }
        return res;
    }

    public BufferedImage gray2RGB(BufferedImage imgray) {   
        return this.grayToRGB(imgray);
    }
    
    public static BufferedImage grayToRGB(BufferedImage imgray) {   
        int w = imgray.getWidth();
        int h = imgray.getHeight();
//System.out.println("grayToRGB");
        BufferedImage imRGB = new BufferedImage(w, h, BufferedImage.TYPE_3BYTE_BGR);
        WritableRaster oup = imRGB.getRaster();
        WritableRaster inp = imgray.copyData(null);
        for (int x=0; x<w; x++)
            for (int y=0; y<h; y++)
                for (int c=0; c<3; c++)
                    oup.setSample(x,y,c,inp.getSample(x,y,0));
        return imRGB;
    }

    public static BufferedImage imageRGB(BufferedImage strk) {   
        int w = strk.getWidth();
        int h = strk.getHeight();
        BufferedImage res = new BufferedImage(w, h, BufferedImage.TYPE_3BYTE_BGR);
        WritableRaster oup = res.getRaster();
        WritableRaster inp = strk.copyData(null);
        for (int x=0; x<w; x++)
            for (int y=0; y<h; y++)
                for (int c=0; c<3; c++)
                    oup.setSample(x,y,c,inp.getSample(x,y,c));
        return res;
    }

    public static BufferedImage loadImage(String ref) {   
        BufferedImage bimg = null;   
        try {   
  
            bimg = ImageIO.read(new File(ref));   
        } catch (Exception e) {   
            e.printStackTrace();   
            System.out.println(msg0a+msg1+msg2+msg0b+ref);
            System.exit(1);
        }
        if (bimg == null) {
            System.out.println(msg0a+msg1+msg2+msg0b+ref+"->img==null");
            System.exit(1);
         }
        //System.out.println(getImageType(bimg));
        //System.out.println("Image loaded: "+ref+" "+bimg.getWidth()+" "+bimg.getHeight());
        return bimg;   
    }  

    public static BufferedImage copyImage(BufferedImage im) {
        BufferedImage copy_im = new BufferedImage(im.getWidth(), im.getHeight(), im.getType());
        Graphics g = copy_im.getGraphics(); 
        g.drawImage(im, 0, 0, null); //-> OBS: cor transparente fica preto !!!
        return copy_im;
    }

    public static BufferedImage copyImageRGB(BufferedImage im) {
        BufferedImage copy_im = new BufferedImage(im.getWidth(), im.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
        Graphics g = copy_im.getGraphics(); 
        g.drawImage(im, 0, 0, null); //-> OBS: cor transparente fica preto !!!
        return copy_im;
    }
    public static BufferedImage copyImageARGB(BufferedImage imARGB) {
        int w = imARGB.getWidth();
        int h = imARGB.getHeight();
        BufferedImage res = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        WritableRaster inp = imARGB.getRaster();
        WritableRaster inpa = imARGB.getAlphaRaster();
        WritableRaster oup = res.getRaster();
        WritableRaster oupa = res.getAlphaRaster();
        for (int y = 0; y < h; y++)
            for (int x = 0; x < w; x++) {
                for (int c = 0; c < 3; c++)
                    oup.setSample(x,y,c,inp.getSample(x,y,c));
                oupa.setSample(x,y,0,inpa.getSample(x,y,0));
            }
        return res;
    }


    public static void saveImage(BufferedImage bImg, String outFileName) {
        try {
            ImageIO.write(bImg, "png", new File(outFileName));
        } 
        catch (IOException ioe) {
            System.out.println("Erro: Problemas na escrita do arquivo.");
        }
        System.out.println("output: "+outFileName);
    }

    public void corTransparente(BufferedImage im, Color corTransp) {
        WritableRaster inp = im.copyData(null);
        WritableRaster oup = im.getAlphaRaster();
        int w = im.getWidth();
        int h = im.getHeight();
        this.corTransparente(inp, oup, corTransp, w, h);
    }
    public void corTransparente(WritableRaster inp, WritableRaster oup, Color corTransp, int w, int h) {
        for(int x = 0; x < w; x++)
            for(int y = 0; y < h; y++)  
                //if(im.getRGB(j, i) == corTransp.getRGB())
                if(inp.getSample(x,y,0) == corTransp.getRed() && 
                   inp.getSample(x,y,1) == corTransp.getGreen() && 
                   inp.getSample(x,y,2) == corTransp.getBlue())
                    //im.setRGB(j, i, 0x00FFFFFF); 
                    oup.setSample(x,y,0,0); //sets alpha zero
		else
                    oup.setSample(x,y,0,255);
    }

    
    public static String getImageType(BufferedImage bSrc) {
        String message;
        boolean abortar = false;
        switch (bSrc.getType()) {
            case BufferedImage.TYPE_BYTE_BINARY: 
                message = "TYPE_BYTE_BINARY.";// Imagem Binaria. Ja esta limiarizada";
                break;
            case BufferedImage.TYPE_BYTE_INDEXED: 
                message = "TYPE_BYTE_INDEXED.";// Imagem em Bytes com indice de Cores. Exige mapeamento.";
                abortar = true;
                break;
            case BufferedImage.TYPE_BYTE_GRAY:
                message = "TYPE_BYTE_GRAY.";// Imagem em Niveis de Cinza. 8 bits sem sinal. Limiarizavel";
                break;
            case BufferedImage.TYPE_USHORT_GRAY:
                message = "TYPE_USHORT_GRAY.";// Imagem em Niveis de Cinza. 16 bits sem sinal.";
                break;
            case BufferedImage.TYPE_USHORT_555_RGB: 
                message = "TYPE_USHORT_555_RGB.";// Imagem Colorida em 3 canais. Cada canal com 5 bits e sem sinal.";
                break;
            case BufferedImage.TYPE_USHORT_565_RGB: 
                message = "TYPE_USHORT_565_RGB.";// Imagem Colorida em 3 canais. Cada canal com 5 bits e sem sinal, mas o canal verde tem 6 bits.";
                break;
            case BufferedImage.TYPE_3BYTE_BGR:
                message = "TYPE_3BYTE_BGR.";// Imagem Colorida. 3 canais e cada canal com 8 bits sem sinal.";
                break;
            case BufferedImage.TYPE_4BYTE_ABGR:
                message = "TYPE_4BYTE_BGR.";// Imagem Colorida. 4 canais (um alfa) e cada canal com 8 bits sem sinal.";
                break;
            case BufferedImage.TYPE_4BYTE_ABGR_PRE:
                message = "TYPE_4BYTE_BGR_PRE.";// Imagem Colorida. 4 canais (um alfa) e cada canal com 8 bits sem sinal.";
                break;
            case BufferedImage.TYPE_INT_RGB:
                message = "TYPE_INT_RGB.";// Imagem Colorida. 3 canais e cada canal com 8 bits com sinal.";
                break;
            case BufferedImage.TYPE_INT_BGR:
                message = "TYPE_INT_BGR.";// Imagem Colorida. 3 canais e cada canal com 8 bits com sinal.";
                break;
            case BufferedImage.TYPE_INT_ARGB:
                message = "TYPE_INT_ARGB.";// Imagem Colorida. 4 canais (um alfa) e cada canal com 8 bits com sinal.";
                break;
            case BufferedImage.TYPE_INT_ARGB_PRE:
                message = "TYPE_INT_ARGB_PRE.";// Imagem Colorida. 4 canais (um alfa) e cada canal com 8 bits com sinal.";
                break;
            case BufferedImage.TYPE_CUSTOM:
                message = "TYPE_CUSTOM.";// Tipo proprietario."; 
                abortar = true;
                break;
            default:
                message = "Unknown type.";//"Tipo desconhecido.";
                abortar = true;
        }
    /*
        if (abortar) {
            System.out.println(message);
            System.out.println(msg0a+msg3+msg0b);
            System.exit(1);
        }
    */
        return message;
    }

    public boolean sameRGB(WritableRaster inp, int x, int y, Color cor) {
        return (inp.getSample(x,y,0)==cor.getRed() &&
                 inp.getSample(x,y,1)==cor.getGreen() &&
                 inp.getSample(x,y,2)==cor.getBlue());
    }

    public boolean sameRGB(WritableRaster inp, int x, int y, int x2, int y2) {
        return (inp.getSample(x,y,0)==inp.getSample(x2,y2,0) &&
                 inp.getSample(x,y,1)==inp.getSample(x2,y2,1) &&
                 inp.getSample(x,y,2)==inp.getSample(x2,y2,2));
    }

    public boolean sameRGB(Color c1, Color c2) {
        return (c1.getRed()==c2.getRed() &&
                 c1.getGreen()==c2.getGreen() &&
                 c1.getBlue()==c2.getBlue());
    }
    public boolean sameRGB(int r1, int g1, int b1, int r2, int g2, int b2) {
        return (r1==r2 && g1==g2 && b1==b2);
    }

    // Euclidean Distance
    public static float euclideanDistance(float[] a, float[] b, int k) {
        float res = 0.0f;
        for (int i = 0; i < k; i++) {
            float di = a[i] - b[i];
            res = res + di*di;
        }
        return (float)(Math.sqrt(res));
    }

    public static double euclideanDistance(double x1, double y1, double z1, 
                         double x2, double y2, double z2) {
        double d1 = x1-x2;
        double d2 = y1-y2;
        double d3 = z1-z2;
        double res = Math.sqrt(d1*d1 + d2*d2 + d3*d3);
        return res;
    }
    
    public static double euclideanDistance(double x1, double y1, 
                         double x2, double y2) {
        double d1 = x1-x2;
        double d2 = y1-y2;
        double res = Math.sqrt(d1*d1 + d2*d2);
        return res;
    }
    
    public float[] rgb2xyz(float[] rgb) {
        float R = rgb[0];
        float G = rgb[1];
        float B = rgb[2];
        float var_R = ( R / 255.0f );        //R from 0 to 255
        float var_G = ( G / 255.0f );        //G from 0 to 255
        float var_B = ( B / 255.0f );        //B from 0 to 255
        if ( var_R > 0.04045f ) {
            var_R = ( ( var_R + 0.055f ) / 1.055f );// ^ 2.4;
            var_R = (float)(Math.pow(var_R, 2.4));
        }
        else
            var_R = var_R / 12.92f;
        if ( var_G > 0.04045f ) {
            var_G = ( ( var_G + 0.055f ) / 1.055f );// ^ 2.4;
            var_G = (float)(Math.pow(var_G, 2.4));
        }
        else
            var_G = var_G / 12.92f;
        if ( var_B > 0.04045f ) {
            var_B = ( ( var_B + 0.055f ) / 1.055f );// ^ 2.4;
            var_B = (float)(Math.pow(var_B, 2.4));
        }
        else
            var_B = var_B / 12.92f;
        var_R = var_R * 100.0f;
        var_G = var_G * 100.0f;
        var_B = var_B * 100.0f;

        float X = var_R * 0.4124f + var_G * 0.3576f + var_B * 0.1805f;
        float Y = var_R * 0.2126f + var_G * 0.7152f + var_B * 0.0722f;
        float Z = var_R * 0.0193f + var_G * 0.1192f + var_B * 0.9505f;
        
        float[] xyz = new float[3];
        xyz[0] = X;
        xyz[1] = Y;
        xyz[2] = Z;
        return xyz;
    }

    public float[] xyz2cielab(float[] xyz) {
        float X = xyz[0];
        float Y = xyz[1];
        float Z = xyz[2];
        float ref_X =  95.047f;
        float var_X = X / ref_X;          //ref_X =  95.047   
        float ref_Y = 100.000f;
        float var_Y = Y / ref_Y;          //ref_Y = 100.000
        float ref_Z = 108.883f;
        float var_Z = Z / ref_Z;          //ref_Z = 108.883
        if ( var_X > 0.008856f ) 
            var_X = (float)(Math.pow(var_X, 1.0f/3.0f)); // ^ ( 1/3 );
        else
            var_X = ( 7.787f * var_X ) + ( 16.0f / 116.0f );
        if ( var_Y > 0.008856f )
            var_Y = (float)(Math.pow(var_Y, 1.0f/3.0f)); //  ^ ( 1/3 )
        else
            var_Y = ( 7.787f * var_Y ) + ( 16.0f / 116.0f );
        if ( var_Z > 0.008856f ) 
            var_Z = (float)(Math.pow(var_Z, 1.0f/3.0f)); // ^ ( 1/3 )
        else
            var_Z = ( 7.787f * var_Z ) + ( 16.0f / 116.0f );
        float CIE_L = ( 116.0f * var_Y ) - 16.0f;
        float CIE_a = 500.0f * ( var_X - var_Y );
        float CIE_b = 200.0f * ( var_Y - var_Z );
        
        float[] cielab = new float[3];
        cielab[0] = CIE_L;
        cielab[1] = CIE_a;
        cielab[2] = CIE_b;
        return cielab;
    }

    public float[] rgb2cielab(float[] rgb) {
        float[] xyz = this.rgb2xyz(rgb);
        float[] cielab = this.xyz2cielab(xyz);
        return cielab;
    }

    public float[] rgb2cielab(int r, int g, int b) {
        float[] rgb = new float[3];
        rgb[0] = (float) r;
        rgb[1] = (float) g;
        rgb[2] = (float) b;
        return this.rgb2cielab(rgb);
    }

    public float[] rgb2cielab(int[] rgb) {
        float[] frgb = new float[3];
        for (int i = 0; i < 3; i++)
            frgb[i] = (float) rgb[i];
        return this.rgb2cielab(frgb);
    }

    public boolean[][] calcContorno(int[][] rpixels) {
        int w = rpixels.length;
        int h = rpixels[0].length;
        return calcContorno(rpixels, w, h);
    }
    public boolean[][] calcContorno(int[][] rpixels, int w, int h) {
        boolean[][] res = new boolean[w][h];
        for (int x=0; x<w; x++)
            for (int y=0; y<h; y++)
                res[x][y] = false;
        for (int x=0; x<w; x++)
            for (int y=0; y<h-1; y++)
                if (rpixels[x][y]-rpixels[x][y+1] != 0)
                    res[x][y] = true;
        for (int x=0; x<w-1; x++)
            for (int y=0; y<h; y++)
                if (rpixels[x][y]-rpixels[x+1][y] != 0)
                    res[x][y] = true;
        return res;
    }

    //@@@@@@2009ago28: nao usar!!!
    //devolve maior id
    public int acertaIdentificadores(int[][] mtLab, LinkedList[] r) {
        int totr = r.length;
        int[] identificador = new int[totr];
        int k = -1;
        for (int i = 0; i < totr; i++) {
            if (r[i].size() > 0) {
                k++;
                int id = k;
                LinkedList pixels = r[i];
                Iterator it = pixels.iterator();
                while (it.hasNext()) {
                    Coords2d p = (Coords2d)it.next();
                    int x = p.getx(), y = p.gety();
                    mtLab[x][y] = id;
                }
            }
        }
        int maior = k;
        return maior;
    }

    public int[][] dilatacaoCaixa(int[][] m) {
        LinkedList se = new LinkedList();
        se.add(new Coords2d(0,1));
        se.add(new Coords2d(0,-1));
        se.add(new Coords2d(1,0));
        se.add(new Coords2d(-1,0));

        se.add(new Coords2d(-1,-1));
        se.add(new Coords2d(1,1));
        se.add(new Coords2d(-1,1));
        se.add(new Coords2d(1,-1));
        return this.supremo(m, se);
    }
    public int[][] dilatacaoCruz(int[][] m) {
        LinkedList se = new LinkedList();
        se.add(new Coords2d(0,1));
        se.add(new Coords2d(0,-1));
        se.add(new Coords2d(1,0));
        se.add(new Coords2d(-1,0));
        return this.supremo(m, se);
    }
    public int[][] erosaoCruz(int[][] m) {
        LinkedList se = new LinkedList();
        se.add(new Coords2d(0,1));
        se.add(new Coords2d(0,-1));
        se.add(new Coords2d(1,0));
        se.add(new Coords2d(-1,0));
        return this.infimo(m, se);
    }
    
    //erosao
    public int[][] infimo(int[][] mtLab, LinkedList se) {
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
                        if (d < min)
                            min = d;
                    }
                }
                m[x][y] = min;
            }
        return m;
    }

    //dilatacao
    public int[][] supremo(int[][] mtLab, LinkedList se) {
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
                        if (d > max)
                            max = d;
                    }
                }
                m[x][y] = max;
            }
        return m;
    }
    /*
    public boolean supremo(int[][] mtLab, LinkedList se, int target) {
        int w = mtLab.length;
        int h = mtLab[0].length;
        int[][] m = new int[w][h];
        boolean modificou = false;
        for (int x = 0; x < w; x++)
            for (int y = 0; y < h; y++) 
                if (mtLab[x][y] == target) {
                    int max = mtLab[x][y];
                    Iterator it = se.iterator();
                    while(it.hasNext()) {
                        Coords2d c = (Coords2d)it.next();
                        int x2 = x+c.getx();
                        int y2 = y+c.gety();
                        if (x2 >= 0 && x2 < w && y2 >= 0 && y2 < h) {
                            int d = mtLab[x2][y2];
                            if (d > max) {
                                max = d;
                                modificou = true;
                            }
                        }
                    }
                    m[x][y] = max;
                }
        mtLab = m;
        return modificou;
    }
    */
    public int[][] supremo(int[][] mtLab, LinkedList se, int target, boolean[] modific) {
        int w = mtLab.length;
        int h = mtLab[0].length;
        int[][] m = new int[w][h];
        boolean modificou = false;
        for (int x = 0; x < w; x++)
            for (int y = 0; y < h; y++) {
                if (mtLab[x][y] == target) {
                    int max = mtLab[x][y];
                    Iterator it = se.iterator();
                    while(it.hasNext()) {
                        Coords2d c = (Coords2d)it.next();
                        int x2 = x+c.getx();
                        int y2 = y+c.gety();
                        if (x2 >= 0 && x2 < w && y2 >= 0 && y2 < h) {
                            int d = mtLab[x2][y2];
                            if (d > max) {
                                max = d;
                                modificou = true;
                            }
                        }
                    }
                    m[x][y] = max;
                }
                else
                    m[x][y] = mtLab[x][y];
            }
        modific[0] = modificou;
        return m;
    }
    
    //abertura
    public int[][] infsup(int[][] mtLab, LinkedList se) {
        mtLab = infimo(mtLab, se);
        mtLab = supremo(mtLab, se);
        return mtLab;
    }
    
    //fechamento
    public int[][] supinf(int[][] mtLab, LinkedList se) {
        mtLab = supremo(mtLab, se);
        mtLab = infimo(mtLab, se);
        return mtLab;
    }
    

    public int getIntegerFromTxt(DataInputStream d) {
        /*
        int tot = 0;
        try {
            String s = d.readLine();
            if (s != null) {
                StringTokenizer parser = new StringTokenizer(s);
                if (parser.hasMoreTokens()) {
                    String s2 = parser.nextToken();
                    tot = Integer.parseInt(s2);
                }
            }
        }
        catch (IOException e) {
            System.err.println ("Unable to read from file");
            System.exit(-1);
        }
        return tot;
        */
        int res = 0;
        int[] tmp = this.getIntegersFromTxt(d);
        if (tmp != null)
            res = tmp[0];
        return res;
    }

    public int[] getIntegersFromTxt(DataInputStream d) {
        int[] res = null;
        LinkedList numbers = new LinkedList();
        try {
            String s = d.readLine();
            if (s != null) {
                StringTokenizer parser = new StringTokenizer(s);
                while (parser.hasMoreTokens()) {
                    String s2 = parser.nextToken();
                    int num = Integer.parseInt(s2);
                    numbers.addLast(num);
                }
            }
            int tot = numbers.size();
            res = new int[tot];
            Iterator it = numbers.iterator();
            int cont = 0;
            while (it.hasNext()) {
                int num = ((Integer)it.next()).intValue();
                res[cont] = num;
                cont++;
            }
        }
        catch (IOException e) {
            System.err.println ("Error (getIntegersFromTxt): Unable to read from file!");
            System.exit(-1);
        }
        return res;
    }

    
}

