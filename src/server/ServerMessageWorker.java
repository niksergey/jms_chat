package server;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.util.Date;


public class ServerMessageWorker implements Runnable
{
    private MessageConsumer messageConsumer;
    private MessageProducer messageProducer;
    private Session session;
    private String host;

    public ServerMessageWorker(String host) {
        this.host = host;
    }

    private Connection createConnection() throws JMSException {
        ActiveMQConnectionFactory activeMQConnectionFactory =
                new ActiveMQConnectionFactory("tcp://" + host +":61616");

        return activeMQConnectionFactory.createConnection();
    }

    @Override
    public void run() {
        Connection connection;
        Destination destinationInput;

        try {
            connection = createConnection();
            connection.start();
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            destinationInput = session.createQueue("ServerInput");
            messageConsumer = session.createConsumer(destinationInput);

            Destination destinationOutput = session.createTopic("ServerOutput");
            messageProducer = session.createProducer(destinationOutput);
            messageProducer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

            while (true) {
                String msg = receiveMessage();

                if ("stop".equals(msg)) {
                    break;
                } else if ("TimeOut".equals(msg)) {
                    System.out.printf(msg);
                    break;
                }

                sendTopicMessage(new Date() + msg);
            }
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    private String receiveMessage(){
        String messageStr = null;
        try {
            Message message = messageConsumer.receive(1_000_000);
            if (message == null) {
                return "TimeOut";
            }
            messageStr = ((TextMessage)message).getText();
            System.out.println("Received message: " + messageStr);
        } catch (JMSException e) {
            e.printStackTrace();
        }

        return messageStr;
    }

    private void sendTopicMessage(String message){
        try {
            TextMessage textMessage = session.createTextMessage(message);
            messageProducer.send(textMessage);
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}