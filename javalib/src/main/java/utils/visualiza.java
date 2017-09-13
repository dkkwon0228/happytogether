/*

2008mai29
Visualiza casamento (testes AlexFreire).


java -Xmx1024m visualiza ../pics/01-mdeskimo.png ../pics/01-mdeskimo-strk.png ../testes/grafos1a_ALEATORIO.txt
java -Xmx1024m visualiza ../pics/01-mdeskimo.png ../pics/01-mdeskimo-strk.png ../testes/grafos1a_MAX.txt
java -Xmx1024m visualiza ../pics/01-mdeskimo.png ../pics/01-mdeskimo-strk.png ../testes/grafos1a_MIN.txt

java -Xmx1024m visualiza ../pics/japanese-gm.png ../pics/japanese-strk.png ../testes/grafos2a_ALEATORIO.txt
java -Xmx1024m visualiza ../pics/japanese-gm.png ../pics/japanese-strk.png ../testes/grafos2a_MAX.txt
java -Xmx1024m visualiza ../pics/japanese-gm.png ../pics/japanese-strk.png ../testes/grafos2a_MIN.txt


*/

package utils;

import m00.*;
import m01.*;
//import m02.*;

import java.util.*;
import java.io.*;
import java.awt.image.*;

public class visualiza {

    public void leituraVetor(String fn, int[] v) {
		try {
			FileInputStream fin = new FileInputStream (fn);
			DataInputStream d = new DataInputStream(fin);
			String s = d.readLine();
			while (s != null) {
				StringTokenizer parser = new StringTokenizer(s);
				int i=-100, j=-100;
				if (parser.hasMoreTokens()) {
					String s2 = parser.nextToken();
					i = Integer.parseInt(s2);
				}
				if (parser.hasMoreTokens()) {
					String s2 = parser.nextToken();
					j = Integer.parseInt(s2);
				}
				//i--; j--; //corrige indices lidos do arquivo gabarito
				if (i > -100 && j > -100) {
					v[i] = j;
				}
				s = d.readLine();
			}
			fin.close();
		}
		catch (IOException e) {
			System.err.println ("Unable to read from file");
			System.exit(-1);
		}
    }

    public static void main( String args[] ) {
        if (args.length == 3) {
            String f1 = args[0];
            String f2 = args[1];
            String f3 = args[2];

			String fngm = f1;
			String fngi = f1;
			String fnstrk = f2;
			int blur=2;
			System.out.println("Blur: "+blur);
			InputGraph1 GI = new InputGraph1(fngi, blur);
			GI.buildVertices();

			Ferramentas1 fe = new Ferramentas1();
			BufferedImage strk = fe.loadImage(fnstrk);
			ModelGraph1 GM = new ModelGraph1(GI.getImRGB(), strk, GI.getOpWshed());
			GM.buildModelVerticesAndEdges();

			int totvi = GI.getNumVertices();
			int[] result = new int[totvi];
			visualiza fe2 = new visualiza();
			fe2.leituraVetor(f3, result);

			int w = strk.getWidth();
			int h = strk.getHeight();
			Vertex[] refvi = GI.getVertices();
			int[][] rpixels = fe.rotulaPixels(w,h, result, totvi, refvi);
			BufferedImage mapaRotulos = fe.calcMapaRotulos(rpixels, w, h, GM);
			Ferramentas.saveImage(mapaRotulos, "zzz-visualiza.png"); // map of labels
			
        }
        else
            System.out.println("Use: java -Xmx1024m visualiza immdl.jpg stroke.bmp casamento.txt");
    }

}
