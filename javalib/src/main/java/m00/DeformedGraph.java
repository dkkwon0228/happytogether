/*

2009jul20

*/

package m00;


// Deformed graph (Gd)
public class DeformedGraph {
    protected Graph input;
    protected Graph model;
    public int totvm;        // total vertices in the model Gm
    public Vertex[] refvm;   // references to vertices in Gm
    public float[] x;     // coords (x, y) of centroides in Gd
    public float[] y;
    
    public DeformedGraph(Graph input, Graph model) {
        this.input = input;
        this.model = model;
        this.totvm = model.getNumVertices();
        refvm = model.getVertices();
        x = new float[totvm];
        y = new float[totvm];
        //initializes Gd=GM
        for (int i=0; i<totvm; i++) {
            Vertex v = refvm[i];
            if (v.getId() != i) {
                System.out.println("Error: DeformationGraph()  id's diferentes!");
                System.exit(1);
            }
            x[i] = v.getx();
            y[i] = v.gety();
        }
    }
    
    //calculates 'local' structural deformation due to (vi,vm)
    public void atualiza(Vertex vi, Vertex vm) {
        int vm_id = vm.getId();
        x[vm_id] = vi.getx();
        y[vm_id] = vi.gety();
    }
    
    //restores Gd=Gm
    public void restaura(Vertex vm) {
        int vm_id = vm.getId();
        x[vm_id] = vm.getx();
        y[vm_id] = vm.gety();
    }
}

