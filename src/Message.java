import java.time.LocalDate;

public abstract class Message {

    /* Error message to use in OperationDeniedException */
    protected static final String DENIED_USER_GROUP =
            "This operation is disabled in your user group.";

    /* instance variables */
    private LocalDate date;
    private User sender;
    protected String contents;

    /**
     * Constructor
     * @param sender name of the message sender
     */
    public Message(User sender) {
        this.date = LocalDate.now();
        /* Constructor setting the sender's name */
        if (sender == null){
            throw new IllegalArgumentException();
        } else {
            this.sender = sender;
        }
    }

    public LocalDate getDate() {
        return date;
    }

    /**
     * Getter Method
     * @return name of sender
     */
    public User getSender() {
        /* Returns the name of the sender */
        return this.sender;
    }

    public abstract String getContents();

}