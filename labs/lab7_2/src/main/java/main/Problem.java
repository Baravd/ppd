package main;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class Problem {
    int n = 10;
    int max = 10;
    GenerateIntLIst generateIntLIst = new GenerateIntLIst();
    List<Integer> integerList = generateIntLIst.consecutive(n);
    List<Integer> outputList = generateIntLIst.consecutive(n);
    List<Thread> threads = new ArrayList<>();
    ExecutorService executorService = Executors.newFixedThreadPool(10);

    public void firstPass() throws ExecutionException, InterruptedException {
        int begin = 0;
        int end = n - 1;

        Callable<Integer> callable = new FirstPass(begin,end,integerList,outputList, executorService);
        Future<Integer> submit = executorService.submit(callable);
        submit.get();
        outputList.add(n-1,submit.get());
        executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        executorService.shutdown();

    }

    public List<Integer> getOutputList() {
        return outputList;
    }
}
