import java.util.ArrayList;
import java.util.List;

public class ModeratedRoom implements MessageExchange {
    /* instance variables */
    private ArrayList<User> users, banned;
    private ArrayList<Message> log;
    private User moderator;
    private int numVisibleLog;

    /**
     * Constructor method
     * @param moderator User who creates the moderated room/
     */
    public ModeratedRoom(PremiumUser moderator) {
        /* Initializes the Moderated room instances */
        this.users = new ArrayList<User>();
        this.banned = new ArrayList<User>();
        this.log = new ArrayList<Message>();
        this.moderator = moderator;
        this.numVisibleLog = Integer.MAX_VALUE;
        this.users.add(moderator);
    }

    /**
     * Method to get the log of the room
     * @param requester The user that requests this operation.
     * @return The Log of the room
     */
    public ArrayList<Message> getLog(User requester) {
        /* Returns the log of the Moderated room */
        if (requester == this.moderator) {
            /* Returns the entire log as user is the moderator */
            return this.log;
        } else {
            if (this.log.size() <= this.numVisibleLog) {
                /* Returns enitre log if it is less than the numVisibleLog */
                return this.log;
            } else {
                ArrayList<Message> tempLog =
                        new ArrayList<Message>(this.log.subList(this.log.size()
                                - this.numVisibleLog, this.log.size()));
                return tempLog;
            }
        }
    }

    /**
     * Method to add user to the room
     * @param u User to add.
     * @return True if user is added, false otherwise
     */
    public boolean addUser(User u) {
        /* Adds user to the room if user is not banned or already in the room */
        if (this.banned.contains(u) || this.users.contains(u)){
            return false;
        } else {
            this.users.add(u);
            return true;
        }
    }

    /**
     * Method to remove a user
     * @param requester The user that requests this operation.
     * @param u User to remove.
     * @return True if user is remove, false otherwise
     */
    public boolean removeUser(User requester, User u) {
        /* Removes the user that is specified of the requester is the moderator */
        if (requester == this.moderator){
            if (this.users.contains(u) && this.moderator != u){
                /* Removes user and returns true if user is in the room and the requester is
                either the moderator or the user him/her self */
                this.users.remove(u);
                return true;
            } else {
                /* Return false as user is not in the room */
                return false;
            }
        } else {
            /* Returns false as requester is not a moderator or the user him/her self */
            return false;
        }
    }

    /**
     * Getter Method
     * @return ArrayList of users
     */
    public ArrayList<User> getUsers() {
        /* Returns the users in the moderated room */
        return this.users;
    }

    /**
     * Method to add a new message to the log
     * @param m Message to add.
     * @return True if the message is successfully added
     */
    public boolean recordMessage(Message m) {
        /* Adds a message to the log */
        this.log.add(m);
        return true;
    }

    /**
     * Method to ban a user if the requester is the moderator
     * @param requester The User who request the ban
     * @param u The user to ban
     * @return True if user is banned, false otherwise
     */
    public boolean banUser(User requester, User u) {
        /* Bans the user by adding the user to the banned ArrayList */
        if (requester == this.moderator){ //Requester is a moderator
            if (u == this.moderator){
                /* User to ban is moderator, returns false */
                return false;
            } else {
                if (this.users.contains(u)){
                    /* User is not moderator and is in the room, bans and remove from room and
                    returns true */
                    removeUser(this.moderator, u);
                    u.rooms.remove(this);
                    this.banned.add(u);
                    return true;
                } else {
                    /* Returns false as user is not in the room */
                    return false;
                }
            }
        } else {
            /* Returns false if the requester is not the moderator */
            return false;
        }
    }

    /**
     * Method to unban a user
     * @param requester User that request the unban
     * @param u The User to unban
     * @return True if the user is a moderator and unbans the User, false otherwise
     */
    public boolean unbanUser(User requester, User u) {
        /* Removes the user from the Banned Arraylist if the requester is the moderator */
        if (requester == this.moderator) {
            if (this.banned.contains(u) && u != this.moderator){
                /* Unbans User u if requested by the moderator and is in the banned user list */
                this.banned.remove(u);
                return true;
            } else {
                /* Return true if the user who requested is the moderator regardless of whether
                the user is in the Arraylist or is the moderator himself */
                return true;
            }
        } else {
            /* Returns false if the requester is not the moedrator */
            return false;
        }
    }

    /**
     * Setter Method
     * @param requester The user who requests the change
     * @param newNum The new limit of visble messages
     * @return True if requester is the moderator of the room
     */
    public boolean setNumVisibleLog(User requester, int newNum) {
        /* Changes the number of visible logs for everyone apart from the moderator */
        if (requester == this.moderator){
            this.numVisibleLog = newNum;
            return true;
        } else {
            return false;
        }
    }
}
