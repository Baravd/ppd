package main;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public class FirstPass implements Callable<Integer> {
    int begin;
    int end;
    List<Integer> inputList;
    List<Integer> outputList;
    ExecutorService executorService;

    public FirstPass(int begin, int end, List<Integer> inputList, List<Integer> outputList, ExecutorService executorService) {
        this.begin = begin;
        this.end = end;
        this.inputList = inputList;
        this.outputList = outputList;
        this.executorService = executorService;
    }

    public List<Integer> getOutputList() {
        return outputList;
    }

    public void setOutputList(List<Integer> outputList) {
        this.outputList = outputList;
    }

    @Override
    public Integer call() throws Exception {

        if (begin == end) {
            return inputList.get(begin);
        } else {
            int middle = (begin + end) / 2;
            Callable<Integer> leftF = new FirstPass(begin, middle, inputList, outputList, executorService);
            Callable<Integer> rightF = new FirstPass(middle + 1, end, inputList, outputList, executorService);
            Future<Integer> submit = executorService.submit(leftF);
            Future<Integer> submit1 = executorService.submit(rightF);
            int sum = submit.get() + submit1.get();
            outputList.set(end, sum);
            return sum;
        }
    }
}
