import java.util.ArrayList;

public abstract class User {

    /* Error message to use in OperationDeniedException */
    protected static final String JOIN_ROOM_FAILED =
            "Failed to join the chat room.";
    protected static final String INVALID_MSG_TYPE =
            "Cannot send this type of message to the specified room.";

    /* instance variables */
    protected String username;
    protected String bio;
    protected ArrayList<MessageExchange> rooms;

    /**
     * Constructor
     * @param username Users, username
     * @param bio The Users bio
     */
    public User(String username, String bio) {
        /* Initializes the Class */
        if (username == null || bio == null){
            throw new IllegalArgumentException();
        } else {
            this.username = username;
            this.bio = bio;
            this.rooms = new ArrayList<MessageExchange>();
        }
    }

    /**
     * Setter Method
     * @param newBio The new bio to be changed to.
     */
    public void setBio(String newBio) {
        /* Changes the Users bio */
        if (newBio == null){
            throw new IllegalArgumentException();
        } else {
            this.bio = newBio;
        }
    }

    /**
     * Getter Method
     * @return Users bio
     */
    public String displayBio() {
        /* Returns the Users bio */
        return this.bio;
    }

    /**
     * Method to join a specified room
     * @param me The room to be joined
     * @throws OperationDeniedException Throws  when user is already in the room of if the
     * request to join fails.
     */
    public void joinRoom(MessageExchange me) throws OperationDeniedException {
        /* Adds the user into the message room */
        if (me == null){
            /* Throw exception if the input is null */
            throw new IllegalArgumentException();
        } else if (me.addUser(this) & !this.rooms.contains(me)){
            this.rooms.add(me);
        } else {
            /* Throw exception if the user is already in the room or if the joining of the room
            failed */
            throw new OperationDeniedException(JOIN_ROOM_FAILED);
        }
    }

    /**
     * Method that leaves the room specified
     * @param me the room to quit
     */
    public void quitRoom(MessageExchange me) {
        /* Quits the room by using the quit room function provided and removed from th rooms list */
        if (me == null){
            /* Throw exception if the input is null */
            throw new IllegalArgumentException();
        } else {
            /* Removed the user from the room and removes room from user list */
            me.removeUser(this, this);
            this.rooms.remove(me);
        }
    }

    /**
     * Method that creates a new chat room and adds all the users given and returns the chat room
     * instance
     * @param users ArrayList of users that will be added
     * @return The instance of the chat room
     */
    public MessageExchange createChatRoom(ArrayList<User> users) {
        /* Creates a new chat room with the User in the ArrayList and returns the chat room
        instance. */
        ChatRoom newRoom = new ChatRoom();
        if (users == null){
            throw new IllegalArgumentException();
        } else {
            try{
                newRoom.addUser(this); /* Adds the current user himself */
                for (User user : users) {
                    /* Iterates through the ArrayList of users and adds each one to the chat room */
                    newRoom.addUser(user);
                    user.rooms.add(newRoom);
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            } finally {
                return newRoom;
            }
        }
    }

    /**
     * Method to send a message into a chat room from the user
     * @param me The room to send the message
     * @param msgType The type of message to send
     * @param contents The contents of the message
     */
    public void sendMessage(MessageExchange me, MessageType msgType, String contents) {
        /* Sends a message in a specific group coming from this user */
        if (me == null || msgType == null || contents == null) {
            /* Throws exception if the input for any of the inputs are null */
            throw new IllegalArgumentException();
//        } else if (msgType != MessageType.TEXT) {
//            /* Throws an exception if the msgtype is not a photo or text */
//            throw new IllegalArgumentException();
        } else if (!this.rooms.contains(me)) {
            /* Throw exception if the room is not found in the list of rooms */
            throw new IllegalArgumentException();
        } else {
            try {
                if (msgType == MessageType.PHOTO) {
                    /* Creates a new photo message and records it in the Message room */
                    PhotoMessage NewPhotoMessage = new PhotoMessage(this, contents);
                    me.recordMessage(NewPhotoMessage);
                } else if (msgType == MessageType.TEXT){
                    /* Creates a new text message and records it in the Message room */
                    TextMessage newTextMessage = new TextMessage(this, contents);
                    me.recordMessage(newTextMessage);
                } else {
                    throw new IllegalArgumentException();
                }
            } catch (OperationDeniedException e) {
                System.out.println(e.getMessage());
            }
        }
    }
    public abstract String fetchMessage(MessageExchange me);

    public abstract String displayName();
}
