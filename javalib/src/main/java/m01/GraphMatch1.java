/*

2009jul20

*/

package m01;

import m00.*;

import java.awt.image.*;

////////////////////////////////////////////////////////////////////////////
// algorithm for graph matching
public class GraphMatch1 extends GraphMatch {
    private Ferramentas1 fe = new Ferramentas1();

    private BufferedImage mapaRotulos;

    public GraphMatch1(InputGraph1 inp, ModelGraph1 mdl, float alpha, float delta) {
        super((Graph)inp, (Graph)mdl, alpha, delta);
        float dmax = (float)Math.max(inp.getDmax(),mdl.getDmax());
        Cost1 custo = new Cost1(inp,mdl,alpha,delta,dmax);
        this.setCost((Cost)custo);
        System.out.println("Homomorphism: alpha="+alpha+" delta="+delta+" dmax="+dmax);

        this.runMatchingAlgorithm();

        BufferedImage im = ((InputGraph1) input).getImRGB();
        int w = im.getWidth();
        int h = im.getHeight();
        int[][] rpixels = fe.rotulaPixels(w, h, this.result, this.totvi, this.refvi);
        this.mapaRotulos = fe.calcMapaRotulos(rpixels, w, h, mdl);
    }
    
    public BufferedImage getResult() {
        return this.mapaRotulos;
    }
    
}

////////////////////////////////////////////////////////////////////////////
