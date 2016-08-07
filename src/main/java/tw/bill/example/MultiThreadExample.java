package tw.bill.example;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bill33 on 2016/8/7.
 */
public class MultiThreadExample {

    public static void main(String[] args) {
        System.out.println("start");

        List<Integer> test = new ArrayList<>();
        for(int i=0; i<1000000; i++) {
//            System.out.println(i);
            test.add(i);
        }

        long startTime = System.currentTimeMillis();
        test.parallelStream().forEach(item -> {
            System.out.println(item);
        });
        long parallelInterval = System.currentTimeMillis() - startTime;


        startTime = System.currentTimeMillis();
        test.stream().forEach(item -> {
            System.out.println(item);
        });
        long sequencialInterval = System.currentTimeMillis() - startTime;

        System.out.println("parallel interval: " + parallelInterval);
        System.out.println("sequencail interval: " + sequencialInterval);
        System.out.println("end");
     }

}
