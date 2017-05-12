package messaging;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class MyMessageProvider {

    public Connection createConnection() throws JMSException {
        ActiveMQConnectionFactory activeMQConnectionFactory =
                new ActiveMQConnectionFactory("tcp://10.240.17.168:61616");

        return activeMQConnectionFactory.createConnection();
    }

    public void sendMessage(String message){
        try {
            Connection connection = createConnection();
            connection.start();
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Destination destination = session.createQueue("Chat");
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
