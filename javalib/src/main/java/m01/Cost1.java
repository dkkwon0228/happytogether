/*

2009jul20

*/

package m01;

import m00.*;

public class Cost1 extends Cost {
    Ferramentas1 fe = new Ferramentas1();
    
    public Cost1(InputGraph1 inp, ModelGraph1 mdl, float alpha, float delta, float mxdst) {
        super((Graph)inp,(Graph)mdl,alpha,delta,mxdst);
        DeformedGraph Gd = new DeformedGraph(inp, mdl);
        this.setGd((DeformedGraph)Gd);
    }
// Distancia Euclidiana dos componentes de cores CIELAB
//2009jul20: comparacao direta entre atributos de vi e de vm
    public float costCV(Vertex vi, Vertex vm) {
        float[] rgbm = ((ModelGraph1)model).getRGB(vm.getId());
        float[] rgbi = ((InputGraph1)input).getRGB(vi.getId());
       return fe.euclideanDistance(fe.rgb2cielab(rgbm),fe.rgb2cielab(rgbi),3)/100.0f;
    }
}
