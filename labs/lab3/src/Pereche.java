/**
 * Created by Vali on 09.10.2017.
 */
public class Pereche {
    int i;
    int j;

    public Pereche(int i, int j) {
        this.i = i;
        this.j = j;
    }

    public int getI() {
        return i;
    }

    public void setI(int i) {
        this.i = i;
    }

    public int getJ() {
        return j;
    }

    public void setJ(int j) {
        this.j = j;
    }

    @Override
    public String toString() {
        return "Pereche{" +
                "i=" + i +
                ", j=" + j +
                '}';
    }
}
