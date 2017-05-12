package ru.stc5;

import client.ClientMessageConsumer;
import client.ClientMessageProvider;

import java.util.Date;
import java.util.Scanner;

public class ChatClient {
    public static void main(String[] args) {
        Thread t1 = new Thread(new ClientMessageConsumer("10.240.17.168"), "ClientChat");
        t1.start();

        ClientMessageProvider provider = new ClientMessageProvider("10.240.17.168");

        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.print("Enter message: ");
            String msg = sc.next();
            if ("Stop".equals(msg)) {
                break;
            }
            provider.sendMessage(msg);
        }

        try {
            t1.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
