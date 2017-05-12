package messaging;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;


public class MyMessageConsumer implements Runnable {
    private String nameThread;
    private MessageConsumer messageConsumer;

    private Connection createConnection() throws JMSException {
        ActiveMQConnectionFactory activeMQConnectionFactory =
                new ActiveMQConnectionFactory("tcp://10.240.17.168:61616");

        return activeMQConnectionFactory.createConnection();
    }

    public MyMessageConsumer(String clientName) {
        nameThread = clientName;
        System.out.println(clientName);
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
            destination = session.createQueue("Chat");
            messageConsumer = session.createConsumer(destination);

            while (true) {
                String msg = receiveMessage();

                if ("stop".equals(msg)) {
                    break;
                } else if ("TimeOut".equals(msg)) {
                    System.out.printf("MQ error");
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
            Message message = messageConsumer.receive(60000);
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