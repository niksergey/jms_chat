package client;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class ClientMessageProvider {

    private String host;

    public ClientMessageProvider(String host) {
        this.host = host;
    }

    private Connection createConnection() throws JMSException {
        ActiveMQConnectionFactory activeMQConnectionFactory =
                new ActiveMQConnectionFactory("tcp://" + host + ":61616");

        return activeMQConnectionFactory.createConnection();
    }

    public void sendMessage(String message){
        try {
            Connection connection = createConnection();
            connection.start();
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Destination destination = session.createQueue("ServerInput");
            MessageProducer messageProducer = session.createProducer(destination);
            messageProducer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
            TextMessage textMessage = session.createTextMessage(message);
            messageProducer.send(textMessage);
            session.close();
            connection.close();
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
