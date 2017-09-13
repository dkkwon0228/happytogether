package com.c9;/*

2010ago20: segmenta, linha de comando (sem interface)

.recebe a imagem e os tracos

java -Xmx1024m com.c9.segmcl pics/05.jpg pics/05-strk.png
java -Xmx1024m com.c9.segmcl pics/3663.jpg pics/3663-strk.png
java -Xmx1024m com.c9.segmcl pics/ayuV.png pics/ayuV-strk.png
java -Xmx1024m com.c9.segmcl pics/ayuV2.png pics/ayuV2-strk.png


*/

import java.awt.image.BufferedImage;

import m00.Elapsed;
import m00.Ferramentas;
import m15.*;

public class segmcl {

    private Ferramentas15 fe = new Ferramentas15();
    long[] elapsedTimes = {0,0,0,0};
    int totvi, totvm;
    
    public segmcl(String fnimg, String fnstrk) { 
    
        BufferedImage map = this.segmenta(fnimg, fnstrk);
        int acum = 0;
        System.out.println("\n|V_I|, |V_M|: "+this.totvi+" "+this.totvm);
        System.out.print("Times: ");
        for (int i = 0; i < 4; i++) {
            System.out.print(elapsedTimes[i]+"  ");
            acum += elapsedTimes[i];
        }
        System.out.println("\nTotal: "+acum+" msecs");
    /*
    String fout = String.format("zzzmap-%02d.png",i);
    fe.saveImage(map,fout);
    */
        
    }



    public BufferedImage segmenta(String fnimg, String fnstrk) { 
        int minsize = 25;


        Elapsed elap = new Elapsed();
        elap.setFirstEvent();
        InputGraph15 GI = new InputGraph15(fnimg, minsize);
        GI.buildVertices();
        elap.setLastEvent();
        System.out.println("GI Time: "+elap.getElapInHMS()+"("+elap.getElapInMillis()+" msecs)");
        elapsedTimes[0] = elap.getElapInMillis();

        elap.setFirstEvent();
        BufferedImage imstrk = Ferramentas.loadImage(fnstrk);
        ModelGraph15 GM = new ModelGraph15(imstrk, GI.getOpWshed());
        GM.buildModelVerticesAndEdges();
        elap.setLastEvent();
        System.out.println("GM Time: "+elap.getElapInHMS()+"("+elap.getElapInMillis()+" msecs)");
        elapsedTimes[1] = elap.getElapInMillis();
        
        this.totvi = GI.getNumVertices();
        this.totvm = GM.getNumVertices();

        float alpha = 0.5f;//1.0f;//0.5f; 
        float delta = 0.5f; 
        BufferedImage immap = runHomomorphism(GI, GM, alpha, delta);

        fe.saveResultsSegm(GI,GM,immap);
        return immap;
    }

    public BufferedImage runHomomorphism(InputGraph15 GI, ModelGraph15 GM, float alpha, float delta) {
        Elapsed elap = new Elapsed();
        elap.setFirstEvent();
        GraphMatch15 s = new GraphMatch15(GI, GM, alpha, delta);
        elap.setLastEvent();
        System.out.println("GraphMatch Time: "+elap.getElapInHMS()+"("+elap.getElapInMillis()+" msecs)");
        elapsedTimes[2] = elap.getElapInMillis();

        //2009jun11: heuristicas para melhorar borda
        BufferedImage iminp = GI.getImRGB();
        BufferedImage imstrk = GM.getStrokeImage();
        BufferedImage immap = s.getResult();
        int iters = 10;
        int raio = 10;
        elap.setFirstEvent();
        BufferedImage resmap = fe.heuristicsImproveSegm(iminp, imstrk, immap, iters, raio);
        elap.setLastEvent();
        System.out.println("PostProc Time: "+elap.getElapInHMS()+"("+elap.getElapInMillis()+" msecs)");
        elapsedTimes[3] = elap.getElapInMillis();

        return resmap;
    }

    public static void main( String args[] ) {
        if (args.length == 2) {
            segmcl a = new segmcl(args[0], args[1]);
        }
        else {
            System.out.println("Uso: java -Xmx1024m com.c9.segmcl inputimage.jpg strokes.png");
        }
    }

}

