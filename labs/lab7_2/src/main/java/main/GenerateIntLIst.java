package main;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GenerateIntLIst {
    public List<Integer> consecutive(int bound) {

        List<Integer> integerList = new ArrayList<>();
        for (int i = 0; i < bound; i++) {
            integerList.add(i);
        }
        if(bound%2==1){
            integerList.add(0);
        }
        return integerList;
    }

    public List<Integer> random(int bound, int max) {
        List<Integer> integerList = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < bound; i++) {
            integerList.add(random.nextInt(max));
        }
        return integerList;

    }

    public List<Pereche> impartePePerechi(int bound, int nrThreads) {
        List<Pereche> perechi = new ArrayList<>();
        int cat = bound / nrThreads;
        int rest = bound % nrThreads;
        int poz = 0;
        /*for (int i = 0; i < nrThreads; i++) {
            if(i==nrThreads-1) {
                perechi.add(poz, poz + cat );
            }
        }
*/
        return perechi;
    }
}

