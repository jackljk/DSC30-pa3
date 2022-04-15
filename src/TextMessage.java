public class TextMessage extends Message {

    /* Error message to use in OperationDeniedException */
    private static final String EXCEED_MAX_LENGTH =
            "Your input exceeded the maximum length limit.";
    private static final int MAX_TEXT_LENGTH = 500;

    /**
     * Constructor
     * @param sender name of message sender
     * @param text Contents of the text
     * @throws OperationDeniedException Throws exception when text length exceeds max length
     */
    public TextMessage(User sender, String text)
            throws OperationDeniedException {
        /* Constructor for textMessage init the sender name using super and the contents of the
        text. */
        super(sender);
        if (text == null){
            throw new IllegalArgumentException();
        } else if (text.length() > MAX_TEXT_LENGTH){
            throw new OperationDeniedException(EXCEED_MAX_LENGTH);
        } else {
            this.contents = text;
        }
    }

    /**
     * Getter Method
     * @return String of name, date and text of text message.
     */
    public String getContents() {
        /* Returns a string with the name, date and text of the text message */
        return this.getSender().displayName() + " [" + this.getDate().toString() + "]: " +
                this.contents;
    }

}
