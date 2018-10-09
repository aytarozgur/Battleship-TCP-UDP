package battleship.Network;

/**
 *
 * @author Ozgur Aytar
 * @author Ozgur Yildiz
 */
public class MessageFactory<T> {

    public Message<T> createMessage(eMessageType type, T dataContainer) {
        Message<T> message  = new Message<>();
        message.setMessageType(type);
        message.setDataContainer(dataContainer);
        return message;
    }
   
}
