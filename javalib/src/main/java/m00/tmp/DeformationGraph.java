/*

2008mar18

*/

package m00.tmp;


import m00.Graph;
import m00.Vertex;

// Deformation graph (Gd)
public abstract class DeformationGraph {
    protected Graph input;
    protected Graph model;
    public int totvm;        // total vertices in the model Gm
    public Vertex[] refvm;   // references to vertices in Gm
    public float[] x;     // coords (x, y) of centroides in Gd
    public float[] y;
    public int[] cont;    
    
    public DeformationGraph(Graph input, Graph model) {
        this.input = input;
        this.model = model;
    
        this.totvm = model.getNumVertices();
        refvm = model.getVertices();

        x = new float[totvm];
        y = new float[totvm];
        cont = new int[totvm];
        
        //initializes Gd=GM
        for (int i=0; i<totvm; i++) {
            Vertex v = refvm[i];
            
            if (v.getId() != i) {
                System.out.println("Error: DeformationGraph()  id's diferentes!");
                System.exit(1);
            }
            
            x[i] = v.getx();
            y[i] = v.gety();
            cont[i] = 1;
        }
    }
    
    //calculates local deformation due to (vi,vm)
    public void atualiza(Vertex vi, Vertex vm) {
        int vm_id = vm.getId();
        cont[vm_id]++;

        float x_ = vi.getx();
        float y_ = vi.gety();
        x[vm_id] = (x[vm_id] +x_)/2.0f;
        y[vm_id] = (y[vm_id] +y_)/2.0f;

//2009abr11: Iury : no lugar de usar o ponto media (as 2 linhas anteriores), usar diretamente as coords de vi:
//2009abr20: teste com atributos de Gi
x[vm_id] = x_;
y[vm_id] = y_;

        /*
        if (x[vm_id] < 0.0 || y[vm_id] < 0.0) {
            System.out.println("Error: Gd.atualiza()  vm.id="+vm_id+" x="+x[vm_id]+" y="+y[vm_id]);
            System.exit(1);
        }
        */
        atualizaAtributos(vi, vm);
    }
    // cada problema tem o seu atributo: ex. segmentacao � RGB, spots � shape context...
    public abstract void atualizaAtributos(Vertex vi, Vertex vm);
    
    //restores Gd=Gm
    public void restaura(Vertex vm) {
        int vm_id = vm.getId();
        x[vm_id] = vm.getx();
        y[vm_id] = vm.gety();
        cont[vm_id] = 1;
        /*
        float[] rgb = model.getRGB(vm.getId());
        r[vm_id] = rgb[0];
        g[vm_id] = rgb[1];
        b[vm_id] = rgb[2];
        */
        restauraAtributos(vm);
    }
    public abstract void restauraAtributos(Vertex vm);
}

