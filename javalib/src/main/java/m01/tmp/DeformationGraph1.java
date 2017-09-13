/*

2008mar11

*/

package m01.tmp;

import m00.*;
import m00.tmp.DeformationGraph;
import m01.InputGraph1;
import m01.ModelGraph1;

// Deformation graph (Gd)
public class DeformationGraph1 extends DeformationGraph {
    public float[] r;
    public float[] g;
    public float[] b;
    
    public DeformationGraph1(InputGraph1 input, ModelGraph1 model) {
        super((Graph)input,(Graph)model);
        r = new float[this.totvm];
        g = new float[this.totvm];
        b = new float[this.totvm];
        
        //initializes Gd=GM
        for (int i=0; i<totvm; i++) {
            Vertex v = refvm[i];
            
            if (v.getId() != i) {
                System.out.println("Error: DeformationGraph()  id's diferentes!");
                System.exit(1);
            }
            float[] rgb = ((ModelGraph1)model).getRGB(i);
            r[i] = rgb[0];
            g[i] = rgb[1];
            b[i] = rgb[2];
        }
    }
    
    public void atualizaAtributos(Vertex vi, Vertex vm) {
        int vm_id = vm.getId();
        float[] rgb = ((InputGraph1)input).getRGB(vi.getId());
        r[vm_id] = (r[vm_id]+rgb[0])/2.0f;
        g[vm_id] = (g[vm_id]+rgb[1])/2.0f;
        b[vm_id] = (b[vm_id]+rgb[2])/2.0f;
    } 

    public void restauraAtributos(Vertex vm) {
        int vm_id = vm.getId();
        float[] rgb = ((ModelGraph1)model).getRGB(vm.getId());
        r[vm_id] = rgb[0];
        g[vm_id] = rgb[1];
        b[vm_id] = rgb[2];
    }
    
}

