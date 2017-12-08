package main;

import java.util.List;

public class ComputePairsThread implements Runnable {
    List<Integer> integerList;
    List<Integer> zRezultat;
    Pereche intervalDeInmultit;

    @Override
    public void run() {
        int start = intervalDeInmultit.getX();
        int finish = intervalDeInmultit.getY();
        int poz = start;
        while (start < finish) {
            int aux = start;
            zRezultat.add(poz, integerList.get(aux) + integerList.get(aux + 1));
            start += 2;
            poz +=1;
        }

    }
}
