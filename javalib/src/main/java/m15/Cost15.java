/*

2009jul20

*/

package m15;

import m00.*;
import m01.*;

public class Cost15 extends Cost {
    Ferramentas1 fe = new Ferramentas1();
    
    public Cost15(InputGraph15 inp, ModelGraph15 mdl, float alpha, float delta, float mxdst) {
        super((Graph)inp,(Graph)mdl,alpha,delta,mxdst);
        DeformedGraph Gd = new DeformedGraph(inp, mdl);
        this.setGd((DeformedGraph)Gd);
    }

    public float costCV(Vertex vi, Vertex vm) {
    
        float[] rgbm = ((ModelGraph15)model).getRGB(vm.getId());
        float[] rgbi = ((InputGraph15)input).getRGB(vi.getId());
        return fe.euclideanDistance(fe.rgb2cielab(rgbm),fe.rgb2cielab(rgbi),3)/100.0f;
/*       
        //2009jul21: FGT probabilities
        int fg = ((ModelGraph15)model).BgORFg(vm.getId());
        double cv = ((InputGraph15)input).getPFGT(vi.getId());
        if (fg == 1)
            cv = 1.0 - cv;
        return (float)cv;
        */
    }
}
