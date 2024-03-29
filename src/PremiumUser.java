import sun.rmi.runtime.Log;

import java.util.ArrayList;

public class PremiumUser extends User {

    /* instance variable */
    private String customTitle;

    /**
     * Constructor
     * @param username User's username
     * @param bio User's Bio
     */
    public PremiumUser(String username, String bio) {
        /* Initializes the Premium User */
        super(username, bio);
        this.customTitle = "Premium";
    }

    /**
     * Method to get the log of a chat room
     * @param me The Chatroom to get the log from
     * @return A String of the log of the chatroom
     */
    public String fetchMessage(MessageExchange me) {
        /* Gets the logged messages from the room specified and returns the entire log as a
        string */
        StringBuilder LogString = new StringBuilder();
        if (me == null  || !this.rooms.contains(me)){
            throw new IllegalArgumentException();
        } else {
            ArrayList<Message> logArray = me.getLog(this);
            /* Loops through the logArray and adds the log to the final string */
            for (Message message : logArray){
                LogString.append(message.getContents()).append("\n");
            }
        }
        return LogString.toString();
    }

    /**
     * Getter Method
     * @return Premium user display name
     */
    public String displayName() {
        /* Sets up and returns the special display name for premium users */
        return "<" + this.customTitle + "> " + this.username;  // placeholder for checkpoint test.
                                    // replace it with your own after checkpoint submission.
    }

    /**
     * Setter Method
     * @param newTitle The new title for the User
     */
    public void setCustomTitle(String newTitle) {
        /* Sets the custom title to a new one provided */
        this.customTitle = newTitle;
    }

    /**
     * Method to create a new moderated room with the premium user who calls it as the moderator
     * @param users An Arraylist containing all the users to be added to the new room
     * @return The newly created moderated room
     */
    public MessageExchange createModeratedRoom(ArrayList<User> users) {
        /* Creates a new moderated room, which can only be done by a premium user and set the
        moderator of the room to be the user him/her self */
        ModeratedRoom modRoom = new ModeratedRoom(this);
        if (users == null){
            throw new IllegalArgumentException();
        } else {
            try{
                /* Adds all the users from the Arraylist of users to the room not including the
                moderator as the moderator is added during initialization */
                this.rooms.add(modRoom);
                for (User user : users){
                    modRoom.addUser(user);
                    user.rooms.add(modRoom);
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            } finally {
                return modRoom;
            }
        }
    }

    /**
     * Method to ban a user
     * @param room Room which the ban should happen in
     * @param u The user to ban
     * @return True if ban is successfully, false otherwise
     */
    public boolean banUser(ModeratedRoom room, User u) {
        /* Bans a user with this user as the moderator */
        if (room == null || u == null){
            throw new IllegalArgumentException();
        } else {
            return room.banUser(this, u);
        }
    }

    /**
     * Method to Unban a user
     * @param room Room which to unban should happen in
     * @param u The User to unban
     * @return True if User is successfully unban false otherwise
     */
    public boolean unbanUser(ModeratedRoom room, User u) {
        /* UnBans a user with this user as the moderator */
        if (room == null || u == null){
            throw new IllegalArgumentException();
        } else {
            return room.unbanUser(this, u);
        }
    }

    /**
     * Method to update the visible number of message for non-moderator users in the moderator room
     * @param room The room to change the NumVisibleLog in
     * @param newNum The new limit of messages
     * @return True if successful, false otherwise
     */
    public boolean setNumVisibleLog(ModeratedRoom room, int newNum) {
        /* Sets the number of visible message log to a new number specified */
        int newNUM_MIN_SIZE = 10;
        if (room == null || newNum < newNUM_MIN_SIZE){
            throw new IllegalArgumentException();
        } else {
            return room.setNumVisibleLog(this, newNum);
        }
    }

}
