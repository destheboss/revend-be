package revend.business.exception;

public class ConversationNotFoundException extends RuntimeException{
    public ConversationNotFoundException(Long id) { super("Conversation with ID " + id + " not found");}
}
