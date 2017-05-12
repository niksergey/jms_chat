package ru.stc5;

import messaging.MyMessageProvider;

import java.util.Date;

public class Main {

    public static void main(String[] args) {
        MyMessageProvider myMessageProducer = new MyMessageProvider();


        myMessageProducer.sendMessage(new Date() + " msg from producer");
    }
}


