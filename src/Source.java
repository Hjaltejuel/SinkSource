/**
 * Created by Michelle on 25-10-2017.
 */
i

public class Source {
    public static void main(String[] args){
        try{
            Context ctx = new InitialContext();
            TopicConnectionFactory topicConnectionFactory = (TopicConnectionFactory)ctx.lookup("TopicConnectionFactory");
            Topic topic = (Topic)ctx.lookup("Alarms");
            TopicConnection topicConn =
            topicConnectionFactory.createTopicConnection();
            TopicSession topicSess = topicConn.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);
            TopicSubscriber topicSub = topicSess.createSubscriber(topic);
            topicSub.start();
            TextMessage msg = (TextMessage) topicSub.receive();
            return msg.getText();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
