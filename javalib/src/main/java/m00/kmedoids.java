
/*

http://en.wikipedia.org/wiki/K-medoids

2009nov04: choosing prototypes

.k=100 -> media de 5 prototipos por obj
.usar kmod=5 -> 14 prototipos por obj, igualmente espacados

(1-NN)
 .kmod=5  Errors: 7 / 1120 (0.00625)
 1 : 1 erros
 4 : 3 erros
 5 : 2 erros
 18 : 1 erros

.kmod=9
 0 : 1 erros
 1 : 6 erros
 2 : 12 erros
 3 : 3 erros
 4 : 17 erros
 5 : 12 erros
 10 : 2 erros
 12 : 4 erros
 18 : 15 erros

 .kmod=15
 0 : 1 erros
 1 : 11 erros
 2 : 16 erros
 3 : 9 erros
 4 : 32 erros ----
 5 : 15 erros
 6 : 3 erros
 7 : 1 erros
 8 : 1 erros
 10 : 1 erros
 12 : 23 erros ----
 15 : 1 erros
 17 : 2 erros
 18 : 16 erros

*/

package m00;

//import m02.*;
//import m12.*;
//import m13.*;


public class kmedoids {

    //recebe matriz de distancias e a lista dos medoids iniciais
    public kmedoids(float[][] d, int[] medoids) {
        int maxiter = 100;
        int k = medoids.length;
        int[] medoids0 = new int[k];
        for (int i = 0; i < k; i++)
            medoids0[i] = -1;
        int iter = 0;
        while (this.diferentes(medoids0,medoids) && iter < maxiter) {
            iter++;
//System.out.println(iter);
            this.copia(medoids,medoids0);
            this.iteracao(d,medoids);
        }

    }

    public boolean diferentes(int[] v1, int[] v2) {
        if (v1.length != v2.length)
            return true;
        int tot = v1.length;
        for (int i = 0; i < tot; i++)
            if (v1[i] != v2[i])
                return true;
        return false;
    }

    //v2 <- v1
    public void copia(int[] v1, int[] v2) {
        int tot = v1.length;
        for (int i = 0; i < tot; i++)
            v2[i] = v1[i];
    }

    public void iteracao(float[][] d, int[] medoids) {
        int k = medoids.length;

        //1. atribui cada elemento ao medoid mais proximo
        int tot = d.length;
        int[] lab = new int[tot];
        for (int x = 0; x < tot; x++) {
            float mind = 999999.9f;
            int minm=-1;
            for (int y = 0; y < k; y++) {
                int m = medoids[y];
                if (d[x][m] < mind) {
                    mind = d[x][m];
                    minm = y;
                }
            }
            lab[x] = minm;
        }
        /*
System.out.println("--------");
for (int i = 0; i < k; i++)
    System.out.print(medoids[i]+", ");
        */
        //2. atualiza medoid com aquele que minimiza a media das distancias
        for (int y = 0; y < k; y++) {
            // para cada medoid, testar outros candidatos a medoid
            int minm=-1;
            float mind = 999999.9f; //distancia media minima
            for (int x = 0; x < tot; x++) {
//System.out.print(x+": ");
                if (lab[x] == y) {
                    float dist = 0.0f;
                    int cont = 0;
                    for (int y2 = 0; y2 < tot; y2++)
                        if (lab[y2] == y) {
//System.out.print(y2+" ");
                            dist += d[x][y2];
                            cont++;
                        }
//System.out.print(" ->"+dist);
                    if (cont > 0 && dist < mind) {
                        mind = dist;
                        minm = x;
                    }
                }
//System.out.println(" ->minm="+minm);
            }
            if (minm >= 0)
                medoids[y] = minm;
        }
        /*       
System.out.println("--------");
for (int i = 0; i < k; i++)
    System.out.print(medoids[i]+", ");
System.out.println("--------");
        //imprime clusters
        for (int y = 0; y < k; y++) {
            for (int x = 0; x < tot; x++)
                if (lab[x] == y)
                    System.out.print(x+", ");
            System.out.println();
        }
        */
    }

    public static void main( String args[] ) {
        float[] vx = {2,3,3,4,6,6,7,7,8,7};
        float[] vy = {6,4,8,7,2,4,3,4,5,6};
        /*
float[] vx = {1,2,2,2,3, 7,8,8,8,9};
float[] vy = {2,1,2,3,2, 2,1,2,3,2};
        */
        int tot = vx.length;
        /*
        System.out.println("Pontos:");
        for (int i = 0; i < tot; i++)
            System.out.print("("+vx[i]+","+vy[i]+") ");
        System.out.println();
        */
        // matriz de distancias
        float[][] d = new float[tot][tot];
        Ferramentas fe = new Ferramentas();
        for (int x = 0; x < tot; x++)
            for (int y = 0; y < tot; y++) {
                double x1 = vx[x];
                double y1 = vy[x];
                double x2 = vx[y];
                double y2 = vy[y];
                d[x][y] = (float)fe.euclideanDistance(x1,y1,x2,y2);
            }
        /*
        System.out.println("Matrix de distancias:");
        for (int x = 0; x < tot; x++) {
            for (int y = 0; y < tot; y++)
                System.out.print(d[x][y]+" ");
            System.out.println();
        }
        */
        //int[] medoids_iniciais = {1,7};
        int[] medoids_iniciais = {0,8};
        
//int[] medoids_iniciais = {0,9};
        int k = medoids_iniciais.length;
        System.out.println("Medoids iniciais:");
        for (int i = 0; i < k; i++) {
            int j = medoids_iniciais[i];
            System.out.println(vx[j]+", "+vy[j]);
        }
        System.out.println();
        kmedoids s = new kmedoids(d, medoids_iniciais);
        int[] medoids = medoids_iniciais;
        System.out.println("Medoids finais:");
        for (int i = 0; i < k; i++) {
            int j = medoids[i];
            System.out.println(vx[j]+", "+vy[j]);
        }
        System.out.println();
    }
}
