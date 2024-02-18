package com.zorrix;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        ListFillingService fillingService = new ListFillingService();

        fillingService.startFilling();
    }
}

class ListFillingService{
    private final long TARGET = 10_000_000;
    Random random = new Random();
    List<Integer> list1 = new ArrayList<>();
    List<Integer> list2 = new ArrayList<>();

    final Object lockObj1 = new Object();
    final Object lockObj2 = new Object();

    public void startFilling() throws InterruptedException {
        long start = System.currentTimeMillis();

        fillingList1();
        fillingList2();

        long end = System.currentTimeMillis();
        System.out.println("Program with 1 thread has been running for " + (end-start) + "ms");
        System.out.println("List1 size: " + list1.size());
        System.out.println("List2 size: " + list2.size());
        list1.clear();
        list2.clear();

        start = System.currentTimeMillis();
        Thread thread1 = new Thread(this::fillingList1);
        Thread thread2 = new Thread(this::fillingList2);

        thread1.start();
        thread2.start();

        thread1.join();
        thread2.join();

        end = System.currentTimeMillis();
        System.out.println("Program with some threads has been running for " + (end-start) + "ms");
        System.out.println("List1 size: " + list1.size());
        System.out.println("List2 size: " + list2.size());
        list1.clear();
    }

    public void fillingList1(){
        long index = 0;
        synchronized (lockObj1) {
            while (index < TARGET) {
                list1.add(random.nextInt());
                index++;
            }
        }
    }
    public void fillingList2(){
        long index = 0;
        synchronized (lockObj1) {
            while (index < TARGET) {
                list2.add(random.nextInt());
                index++;
            }
        }
    }

}