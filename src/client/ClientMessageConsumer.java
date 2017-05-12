package client;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;


public class ClientMessageConsumer implements Runnable
{
    private MessageConsumer messageConsumer;
    private String host;

    public ClientMessageConsumer(String host) {
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
        Destination destination;
        Session session;

        try {
            connection = createConnection();
            connection.start();
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            destination = session.createTopic("ServerOutput");
            messageConsumer = session.createConsumer(destination);

            while (true) {
                String msg = receiveMessage();

                if ("stop".equals(msg)) {
                    break;
                } else if ("TimeOut".equals(msg)) {
                    System.out.printf(msg);
                    break;
                }

                System.out.println(msg);
            }
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    private String receiveMessage(){
        String messageStr = null;
        try {
            Message message = messageConsumer.receive(120_000_000);
            if (message == null) {
                return "TimeOut";
            }
            messageStr = ((TextMessage)message).getText();
        } catch (JMSException e) {
            e.printStackTrace();
        }

        return messageStr;
    }
}