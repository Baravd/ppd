package main;

import java.util.concurrent.ExecutionException;

public class Main {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        Problem problem = new Problem();
        problem.firstPass();
        System.out.println(problem.getOutputList());



    }


}
