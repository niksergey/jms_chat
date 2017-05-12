package ru.stc5;

import server.ServerMessageWorker;

public class ChatServer {

    public static void main(String[] args) {
        Thread t1 = new Thread(new ServerMessageWorker("10.240.17.168"), "ChatServer");
        t1.start();

        try {
            t1.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}


