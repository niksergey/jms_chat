package ru.stc5;

import messaging.MyMessageConsumer;
import messaging.MyMessageProvider;

import java.util.Date;
import java.util.Scanner;

public class ChatClient {
    public static void main(String[] args) {
        Thread t1 = new Thread(new MyMessageConsumer("Sergey"), "t1");
        t1.start();

        MyMessageProvider myMessageProducer = new MyMessageProvider();

        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.print(new Date() + ": ");
            String msg = sc.next();
            if ("Stop".equals(msg)) {
                break;
            }
            myMessageProducer.sendMessage(msg);
        }

        try {
            t1.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
