/*

2009jul20

*/

package m15;

import m00.*;

import java.awt.image.*;

////////////////////////////////////////////////////////////////////////////
// algorithm for graph matching
public class GraphMatch15 extends GraphMatch {
    private Ferramentas15 fe = new Ferramentas15();

    private BufferedImage mapaRotulos;

    public GraphMatch15(InputGraph15 inp, ModelGraph15 mdl, float alpha, float delta) {
        super((Graph)inp, (Graph)mdl, alpha, delta);
        float dmax = (float)Math.max(inp.getDmax(),mdl.getDmax());
        Cost15 custo = new Cost15(inp,mdl,alpha,delta,dmax);
        this.setCost((Cost)custo);
        System.out.println("Homomorphism: alpha="+alpha+" delta="+delta+" dmax="+dmax);

        this.runMatchingAlgorithm();

        int w = ((InputGraph15) input).getWidth();
        int h = ((InputGraph15) input).getHeight();
        int[][] rpixels = fe.rotulaPixels(w, h, this.result, this.totvi, this.refvi);
        this.mapaRotulos = fe.calcMapaRotulos(rpixels, w, h, mdl);
    }
    
    public BufferedImage getResult() {
        return this.mapaRotulos;
    }
    
}

////////////////////////////////////////////////////////////////////////////
