import sun.rmi.runtime.Log;

import java.util.ArrayList;
import java.util.List;

public class StandardUser extends User {

    /* Message to append when fetching non-text message */
    private static final String FETCH_DENIED_MSG =
            "This message cannot be fetched because you are not a premium user.";

    /**
     * Constructor
     * @param username Users username
     * @param bio Users Bio
     */
    public StandardUser(String username, String bio) {
        /* Initializes a standard user using super to use constructor from abstract user class. */
        super(username, bio);
    }

    /**
     * Method to get the last 100 message from a chat room
     * @param me Room to get the messages from
     * @return String of the log of messages
     */
    public String fetchMessage(MessageExchange me) {
        /* Gets the last 100 messages sent in a chat room */
        StringBuilder LogString = new StringBuilder();
        int LogCount = 0;
        if (me == null || !this.rooms.contains(me)) {
            throw new IllegalArgumentException();
        } else {
            ArrayList<Message> LogArray = me.getLog(this);
            ArrayList<Message> TempArray;
            if (LogArray.size() > 100) {
                TempArray = new ArrayList<Message>(LogArray.subList(LogArray.size() - 100,
                        LogArray.size()));
            } else {
                TempArray = LogArray;
            }
            for (Message message : TempArray) {
                /*
                Loops through the Array of logs and appends the chat into a string if it is a
                text message, if it is not appends an error message and break the loop if the
                limit for a standard user is reached.
                 */
                if (message instanceof TextMessage) {
                    LogString.append(message.getContents()).append("\n");
                } else {
                    LogString.append(FETCH_DENIED_MSG).append("\n");
                }
            }
        }
        return LogString.toString();
    }

    /**
     * Getter method
     * @return This Users Username
     */
    public String displayName() {
        /* Returns the username of the user */
        return this.username;  // placeholder for checkpoint test.
                               // replace it with your own after checkpoint submission.
    }
}
