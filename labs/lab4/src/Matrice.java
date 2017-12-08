import java.util.Random;

/**
 * Created by Vali on 09.10.2017.
 */
public class Matrice {
    int[][] matrice;
    int nrLinii;
    int nrColoane;

    public Matrice(int nrLinii, int nrColoane) {
        this.nrLinii = nrLinii;
        this.nrColoane = nrColoane;
        matrice = new int[nrLinii][nrColoane];
    }

    public int get(int i, int j) {
        return matrice[i][j];
    }

    public void set(int i, int j, int val) {
        matrice[i][j] = val;
    }

    public void populateRandom() {
        Random random = new Random();
        for (int i = 0; i < nrLinii; i++) {
            for (int j = 0; j < nrColoane; j++) {
                matrice[i][j] = random.nextInt(10);
            }
        }
    }


    @Override
    public String toString() {
        String s = "";
        for (int i = 0; i < nrLinii; i++) {
            for (int j = 0; j < nrColoane; j++) {
                s += matrice[i][j];
                s += ' ';
            }
            s += '\n';
        }
        return s;
    }

    public int getNrLinii() {
        return nrLinii;
    }

    public int getNrColoane() {
        return nrColoane;
    }

    public void  multiply(Pereche linii, Matrice m1, Matrice m2, Matrice rezultat) {
        int val = 0;
        for (int i = linii.i; i < linii.j; i++) {
            for (int j = 0; j < m2.getNrColoane(); j++) {
                for (int l = 0; l < m1.getNrColoane(); l++) {
                    int aux = rezultat.get(i, j);
                    val += m1.get(i, l) * m2.get(l, j);
                    rezultat.set(i, j, aux + val);
                }
            }
        }

    }
}
