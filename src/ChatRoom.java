import java.util.ArrayList;

public class ChatRoom implements MessageExchange {

    /* instance variables */
    private ArrayList<User> users;
    private ArrayList<Message> log;

    /**
     * Constructor Method
     */
    public ChatRoom() {
        /* Initializes the User list and message list for a new chatroom */
        this.users = new ArrayList<User>();
        this.log = new ArrayList<Message>();
    }

    /**
     * Getter Method
     * @param requester The user that requests this operation.
     * @return The ArrayList of all the messages in the Chatroom
     */
    public ArrayList<Message> getLog(User requester) {
        /* Returns the ArrayList of the log of the Chatroom */
        return this.log;
    }

    /**
     * Method to add a user into a room
     * @param u User to add.
     * @return True is user is added, false if user can't be added as the user is already in the
     * room
     */
    public boolean addUser(User u) {
        /* Adds the User into the user list for the room */
        if (this.users.contains(u)){
            return false;
        } else {
            this.users.add(u);
            return true;
        }
    }

    /**
     * Method to remove a user from the room
     * @param requester The user that requests this operation.
     * @param u User to remove.
     * @return True if user is removed, false if user cannot be found/removed
     */
    public boolean removeUser(User requester, User u) {
        /* Removes a user from the user list for the room */
        if (this.users.contains(u)){
            this.users.remove(u);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Getter Method
     * @return Arraylist of Users in the room
     */
    public ArrayList<User> getUsers() {
        /* Returns the Arraylist containing the users in the room */
        return this.users;
    }

    /**
     * Method to add message sent to the log
     * @param m Message to add.
     * @return True when the message is added
     */
    public boolean recordMessage(Message m) {
        /* Adds a message to the log */
        this.log.add(m);
        return true;
    }

}