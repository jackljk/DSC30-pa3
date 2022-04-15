/*
  Name: Jack Kai lim
  PID:  A16919063
 */

import java.time.LocalDate;
import java.util.ArrayList;

import com.sun.org.apache.xpath.internal.operations.Mod;
import org.junit.*;
import static org.junit.Assert.*;

/**
 * Messenger Application Test
 * @author Jack Kai Lim
 * @since  04/11/2022
 */
public class MessengerApplicationTest {

    /*
      Messages defined in starter code. Remember to copy and paste these strings to the
      test file if you cannot directly access them. DO NOT change the original declaration
      to public.
     */
    private static final String INVALID_INPUT =
            "The source path given cannot be parsed as photo.";
    private static final String EXCEED_MAX_LENGTH =
            "Your input exceeded the maximum length limit.";
    private static final String FETCH_DENIED_MSG =
            "This message cannot be fetched because you are not a premium user.";
    private static final String DENIED_USER_GROUP =
            "This operation is disabled in your user group.";
    private static final String JOIN_ROOM_FAILED =
            "Failed to join the chat room.";



    /*
      Global test variables. Initialize them in @Before method.
     */
    PremiumUser marina;
    MessageExchange room;
    StandardUser jack;
    PremiumUser matt;

    /*
      The date used in Message and its subclasses. You can directly
      call this in your test methods.
     */
    LocalDate date = LocalDate.now();

    /*
     * Setup
     */
    @Before
    public void setup() {
        marina = new PremiumUser("Marina", "Instructor");
        jack = new StandardUser("Jackljk", "DSC30 Student");
        matt = new PremiumUser("Matty", "DSC30 and DSC40B Student");
        room = new ChatRoom();
    }

    /*
      Recap: Assert exception without message
     */
    @Test(expected = IllegalArgumentException.class)
    public void testPremiumUserThrowsIAE() {
        marina = new PremiumUser("Marina", null);
    }

    /*
      Assert exception with message
     */
    @Test
    public void testPhotoMessageThrowsODE() {
        try {
            PhotoMessage pm = new PhotoMessage(marina, "PA02.zip");
        } catch (OperationDeniedException ode) {
            assertEquals(INVALID_INPUT, ode.getMessage());
        }
    }

    /*
     * Assert message content without hardcoding the date
     */
    @Test
    public void testTextMessageGetContents() {
        try {
            TextMessage tm = new TextMessage(marina, "A sample text message.");

            // concatenating the current date when running the test
            String expected = "<Premium> Marina [" + date + "]: A sample text message.";
            assertEquals(expected, tm.getContents());
        } catch (OperationDeniedException ode) {
            fail("ODE should not be thrown");
        }
    }

    /*
      TODO: Add your tests
     */
    @Test
    public void testCreateModRoomAndModRoomTest() throws OperationDeniedException {
        /* Marina the premium user creates a Mod room */
        ArrayList<User> users = new ArrayList<User>();
        users.add(marina);
        users.add(jack);
        users.add(matt);
        MessageExchange ModRoom1 = marina.createModeratedRoom(users);
        assertTrue(ModRoom1 instanceof ModeratedRoom);
        assertEquals(users, ModRoom1.getUsers());
        /* Testing sending messages from all users */
        ((ModeratedRoom) ModRoom1).setNumVisibleLog(marina, 10); //To test limits on nonmods
        matt.setCustomTitle("TA");
        marina.setCustomTitle("Instructor");
        /* Setting up text messages */
        TextMessage tm1 = new TextMessage(marina, "hello");
        TextMessage tm2 = new TextMessage(jack, "Hola");
        TextMessage tm3 = new TextMessage(matt, "Bonjour");
        PhotoMessage pm1 = new PhotoMessage(marina, "Photo.jpg");
        PhotoMessage pm2 = new PhotoMessage(matt, "Test.png");
        ModRoom1.recordMessage(tm1);
        /* Test with 1 text message */
        String exp1 = "<Instructor> Marina [" + date + "]: hello\n";
        assertEquals(exp1, marina.fetchMessage(ModRoom1));
        ModRoom1.recordMessage(tm2);
        ModRoom1.recordMessage(tm3);
        /* Test with 3 text messages */
        String exp2 = "<Instructor> Marina [" + date + "]: hello\n" +
                "Jackljk [" + date + "]: Hola\n" +
                "<TA> Matty [" + date + "]: Bonjour\n";
        assertEquals(exp2, matt.fetchMessage(ModRoom1));
        /* Test with 5 messages 2 of which are photo-messages */
        ModRoom1.recordMessage(pm1);
        ModRoom1.recordMessage(pm2);
        String exp3 = "<Instructor> Marina [" + date + "]: hello\n" +
                "Jackljk [" + date + "]: Hola\n" +
                "<TA> Matty [" + date + "]: Bonjour\n" +
                "<Instructor> Marina [" + date + "]: Picture at Photo.jpg\n" +
                "<TA> Matty [" + date + "]: Picture at Test.png\n";
        assertEquals(exp3, marina.fetchMessage(ModRoom1));
        /* Test if num visible works */
        for (int i = 0; i < 10; i++) {
            ModRoom1.recordMessage(tm1);
        }
        /* Room should have more than 10 messages now. */
        String if_mod = "<Instructor> Marina [" + date + "]: hello\n" +
                "Jackljk [" + date + "]: Hola\n" +
                "<TA> Matty [" + date + "]: Bonjour\n" +
                "<Instructor> Marina [" + date + "]: Picture at Photo.jpg\n" +
                "<TA> Matty [" + date + "]: Picture at Test.png\n" +
                "<Instructor> Marina [" + date + "]: hello\n" +
                "<Instructor> Marina [" + date + "]: hello\n" +
                "<Instructor> Marina [" + date + "]: hello\n" +
                "<Instructor> Marina [" + date + "]: hello\n" +
                "<Instructor> Marina [" + date + "]: hello\n" +
                "<Instructor> Marina [" + date + "]: hello\n" +
                "<Instructor> Marina [" + date + "]: hello\n" +
                "<Instructor> Marina [" + date + "]: hello\n" +
                "<Instructor> Marina [" + date + "]: hello\n" +
                "<Instructor> Marina [" + date + "]: hello\n";
        /* Should get all messages if moderator calls */
        assertEquals(if_mod, marina.fetchMessage(ModRoom1));
        String not_mod = "<Instructor> Marina [" + date + "]: hello\n" +
                "<Instructor> Marina [" + date + "]: hello\n" +
                "<Instructor> Marina [" + date + "]: hello\n" +
                "<Instructor> Marina [" + date + "]: hello\n" +
                "<Instructor> Marina [" + date + "]: hello\n" +
                "<Instructor> Marina [" + date + "]: hello\n" +
                "<Instructor> Marina [" + date + "]: hello\n" +
                "<Instructor> Marina [" + date + "]: hello\n" +
                "<Instructor> Marina [" + date + "]: hello\n" +
                "<Instructor> Marina [" + date + "]: hello\n";
        assertEquals(not_mod, matt.fetchMessage(ModRoom1));
        assertEquals(not_mod, jack.fetchMessage(ModRoom1));
        /* Testing Ban, unbans, Remove and add users and misc methods  */
        marina.banUser((ModeratedRoom) ModRoom1, jack); //Testing ban with mod
        users.remove(jack);
        assertEquals(users, ModRoom1.getUsers());
        assertFalse(ModRoom1.addUser(jack));
        marina.unbanUser((ModeratedRoom) ModRoom1, jack); //Testing unban with mod
        assertTrue(ModRoom1.addUser(jack));
        users.add(jack);
        assertTrue(ModRoom1.removeUser(marina, matt)); //Testing remove with mod
        users.remove(matt);
        assertEquals(users, ModRoom1.getUsers());
        assertFalse(ModRoom1.removeUser(marina, marina));//Testing remove of mod
        assertFalse(ModRoom1.removeUser(jack, marina));//Testing remove if not mod
        assertFalse(ModRoom1.addUser(jack));//Testing add if already in room
        assertFalse(((ModeratedRoom) ModRoom1).banUser(jack, marina));//Testing ban if not mod
        ModRoom1.addUser(matt);
        //Testing set if not mod
        assertFalse(matt.setNumVisibleLog((ModeratedRoom) ModRoom1, 100));
        marina.banUser((ModeratedRoom) ModRoom1, matt);
        assertFalse(((ModeratedRoom) ModRoom1).unbanUser(jack, matt));//Testing unban if not mod
    }

    @Test
    public void testInChatRooms() {
        ArrayList<User> users = new ArrayList<User>();
        users.add(jack);
        users.add(marina);
        users.add(matt);
        /* Jack creates a chatroom */
        ChatRoom cr_jack = (ChatRoom) jack.createChatRoom(users);
        assertEquals(users, cr_jack.getUsers());
        /* Matt creates a chatroom */
        ChatRoom cr_matt = (ChatRoom) matt.createChatRoom(users);
        /* Testing send messages and fetch message */
        jack.sendMessage(cr_jack, MessageType.TEXT, "Hello everybody");
        matt.sendMessage(cr_matt, MessageType.PHOTO, "TEST.png");
        String cr_jack1 = "Jackljk [" + date + "]: Hello everybody\n";
        String cr_matt1 = "<Premium> Matty [" + date + "]: Picture at TEST.png\n";
        assertEquals(cr_jack1, marina.fetchMessage(cr_jack));
        assertEquals(cr_matt1, marina.fetchMessage(cr_matt));
        marina.setCustomTitle("Lecturer");
        marina.sendMessage(cr_matt, MessageType.PHOTO, "TEST2.gif");
        String cr_matt2 = "<Premium> Matty [" + date + "]: Picture at TEST.png\n" +
                "<Lecturer> Marina [" + date + "]: Picture at TEST2.gif\n";
        assertEquals(cr_matt2, matt.fetchMessage(cr_matt));
        /* Test for 100 message limit */
        StringBuilder mess_limit_for_standard = new StringBuilder();
        for (int i = 0; i < 100; i++) {
            jack.sendMessage(cr_jack, MessageType.TEXT, "100");
            mess_limit_for_standard.append("Jackljk [" + date + "]: 100\n");
        }
        assertEquals(mess_limit_for_standard.toString(), jack.fetchMessage(cr_jack));
        /* Testing join and quit room */
        jack.quitRoom(cr_matt);
        users.remove(jack);
        assertTrue(users.size() == cr_matt.getUsers().size() &&
                users.containsAll(cr_matt.getUsers()) && cr_matt.getUsers().containsAll(users));
        try {
            jack.joinRoom(cr_matt);
            users.add(jack);
        } catch (OperationDeniedException e) {
            System.out.println(e.getMessage());
        }
        assertTrue(users.size() == cr_matt.getUsers().size() &&
                users.containsAll(cr_matt.getUsers()) && cr_matt.getUsers().containsAll(users));
        /* Testing bios */
        assertEquals("DSC30 Student", jack.displayBio());
        jack.setBio("I am no longer in DSC30");
        assertEquals("I am no longer in DSC30", jack.displayBio());
    }

    @Test
    public void additionalTests() throws OperationDeniedException {
        /* Additional Chatroom creation test */
        ArrayList<User> users = new ArrayList<>();
        ChatRoom new1 = (ChatRoom) jack.createChatRoom(users);
        users.add(jack);
        assertEquals(users, new1.getUsers());
        ChatRoom new2 = (ChatRoom) jack.createChatRoom(users);
        assertEquals(users, new2.getUsers());
        /* Additional join and leave room test */
        matt.joinRoom(new1);
        users.add(matt);
        assertTrue(users.size() == new1.getUsers().size() &&
                users.containsAll(new1.getUsers()) && new1.getUsers().containsAll(users));
        marina.joinRoom(new2);
        users.remove(matt);
        users.add(marina);
        assertTrue(users.size() == new2.getUsers().size() &&
                users.containsAll(new2.getUsers()) && new2.getUsers().containsAll(users));
        marina.quitRoom(new2);
        matt.quitRoom(new1);
        users.remove(marina);
        assertEquals(users, new1.getUsers());
        assertEquals(users, new2.getUsers());
        users.remove(jack);
        /* Creating more moderated rooms */
        ModeratedRoom mod1 = (ModeratedRoom) marina.createModeratedRoom(users);
        ModeratedRoom mod2 = (ModeratedRoom) matt.createModeratedRoom(users);
        users.add(marina);
        assertEquals(users, mod1.getUsers());
        users.remove(marina);
        users.add(matt);
        assertEquals(users, mod2.getUsers());
        jack.joinRoom(mod2);
        users.add(jack);
        assertTrue(users.size() == mod2.getUsers().size() &&
                users.containsAll(mod2.getUsers()) && mod2.getUsers().containsAll(users));
        /* Additional ban and unban test */
        jack.joinRoom(mod1);
        marina.banUser(mod1, jack);
        users.remove(matt);
        users.add(marina);
        users.remove(jack);
        assertEquals(users, mod1.getUsers());
        marina.unbanUser(mod1, jack);
        jack.joinRoom(mod1);
        users.add(jack);
        assertEquals(users, mod1.getUsers());
        marina.joinRoom(mod2);
        users.remove(jack);
        users.remove(marina);
        users.add(matt);
        users.add(jack);
        users.add(marina);
        assertEquals(users, mod2.getUsers());
        matt.banUser(mod2, marina);
        users.remove(marina);
        assertEquals(users, mod2.getUsers());
        matt.unbanUser(mod2, marina);
        marina.joinRoom(mod2);
        users.add(marina);
        assertEquals(users, mod2.getUsers());
    }

    @Test(expected = IllegalArgumentException.class)
    public void messageIAE1() throws OperationDeniedException {
        TextMessage test = new TextMessage(null, "LOl");
    }

    @Test(expected = IllegalArgumentException.class)
    public void messageIAE2() throws OperationDeniedException {
        TextMessage test = new TextMessage(jack, null);
    }

    @Test
    public void messageODEmax() {
        try {
            TextMessage test = new TextMessage(jack, "ALLLLLLLLLALLLLLLLLLALLLLL" +
                    "LLLLALLLLLLLLLALLLLLLLLLALLLLLLLLLALLLLLLLLLALLLLLLLLLALLLLL" +
                    "LLLLALLLLLLLLLALLLLLLLLLALLLLLLLLLALLLLLLLLLALLLLLLLLLALLLLLLLL" +
                    "LALLLLLLLLLALLLLLLLLLALLLLLLLALLLLLLLLL" +
                    "ALLLLLLLLLALLLLLLLLLALLLLLLLLLALLLLLLLLLALLLLLLLLLALLLLLLLLLLLALLL" +
                    "LLLLLLALLLLLLLLLALLLLLLLLLALLLLLLLLLALLLLLLLLLALLLLLLLLLAL" +
                    "LLLLLLLLALLLLLLLLL");
        } catch (OperationDeniedException e) {
            assertEquals(EXCEED_MAX_LENGTH, e.getMessage());
        }
    }

    @Test
    public void pMessageODE1() {
        try {
            PhotoMessage test = new PhotoMessage(jack, "Test.gif");
        } catch (OperationDeniedException e) {
            assertEquals(DENIED_USER_GROUP, e.getMessage());
        }
    }

    @Test
    public void pMessageODE2() {
        try {
            PhotoMessage test = new PhotoMessage(marina, "Test.zip");
        } catch (OperationDeniedException e) {
            assertEquals(INVALID_INPUT, e.getMessage());
        }
    }
    @Test (expected = IllegalArgumentException.class)
    public void pMessageIAE1() throws OperationDeniedException {
        PhotoMessage test = new PhotoMessage(null, "Test.png");
    }
    @Test (expected = IllegalArgumentException.class)
    public void pMessageIAE2() throws OperationDeniedException {
        PhotoMessage test = new PhotoMessage(matt, null);
    }
    @Test (expected = IllegalArgumentException.class)
    public void userIAE1(){
        StandardUser test = new StandardUser(null, "hi");
    }
    @Test (expected = IllegalArgumentException.class)
    public void userIAE2(){
        StandardUser test = new StandardUser("POPOJING", null);
    }
    @Test (expected = IllegalArgumentException.class)
    public void userIAE3(){
        jack.setBio(null);
    }
    @Test (expected = IllegalArgumentException.class)
    public void userIAE4() throws OperationDeniedException {
        jack.joinRoom(null);
    }
    @Test (expected = IllegalArgumentException.class)
    public void userIAE5() throws OperationDeniedException {
        ArrayList<User> a = new ArrayList<User>();
        ChatRoom test = (ChatRoom) jack.createChatRoom(a);
        matt.joinRoom(test);
        matt.quitRoom(null);
    }
    @Test (expected = IllegalArgumentException.class)
    public void userIAE6() throws OperationDeniedException {
        ChatRoom test = (ChatRoom) jack.createChatRoom(null);
    }
    @Test (expected = IllegalArgumentException.class)
    public void userIAE7() throws OperationDeniedException {
        ArrayList<User> a = new ArrayList<User>();
        ChatRoom test = (ChatRoom) jack.createChatRoom(a);
        jack.sendMessage(null, MessageType.TEXT, "hi");
    }
    @Test (expected = IllegalArgumentException.class)
    public void userIAE8() throws OperationDeniedException {
        ArrayList<User> a = new ArrayList<User>();
        ChatRoom test = (ChatRoom) jack.createChatRoom(a);
        jack.sendMessage(test, null, "hi");
    }
    @Test (expected = IllegalArgumentException.class)
    public void userIAE9() throws OperationDeniedException {
        ArrayList<User> a = new ArrayList<User>();
        ChatRoom test = (ChatRoom) jack.createChatRoom(a);
        jack.sendMessage(test, MessageType.TEXT, null);
    }
    @Test (expected = IllegalArgumentException.class)
    public void userIAE10() throws OperationDeniedException {
        ArrayList<User> a = new ArrayList<User>();
        ChatRoom test = (ChatRoom) jack.createChatRoom(a);
        matt.sendMessage(test, MessageType.TEXT, "hi");
    }
    @Test
    public void userODE(){
        ArrayList<User> a = new ArrayList<User>();
        ChatRoom test = (ChatRoom) jack.createChatRoom(a);
        try{
            jack.joinRoom(test);
        } catch (OperationDeniedException e) {
            assertEquals(JOIN_ROOM_FAILED, e.getMessage());
        }
    }
    @Test (expected = IllegalArgumentException.class)
    public void standarduserIAE1() throws OperationDeniedException {
        ArrayList<User> a = new ArrayList<User>();
        ChatRoom test = (ChatRoom) jack.createChatRoom(a);
        jack.sendMessage(test, MessageType.TEXT, "hi");
        jack.fetchMessage(null);
    }
    @Test (expected = IllegalArgumentException.class)
    public void standarduserIAE2() throws OperationDeniedException {
        ArrayList<User> a = new ArrayList<User>();
        ChatRoom test = (ChatRoom) jack.createChatRoom(a);
        jack.sendMessage(test, MessageType.TEXT, "hi");
        matt.fetchMessage(test);
    }
    @Test (expected = IllegalArgumentException.class)
    public void premiumUserIAE1() throws OperationDeniedException {
        ArrayList<User> a = new ArrayList<User>();
        ModeratedRoom test = (ModeratedRoom) matt.createModeratedRoom(null);
    }
    @Test (expected = IllegalArgumentException.class)
    public void premiumUserIAE2() throws OperationDeniedException {
        ArrayList<User> a = new ArrayList<User>();
        ModeratedRoom test = (ModeratedRoom) matt.createModeratedRoom(a);
        jack.joinRoom(test);
        matt.banUser(null, jack);
    }
    @Test (expected = IllegalArgumentException.class)
    public void premiumUserIAE3() throws OperationDeniedException {
        ArrayList<User> a = new ArrayList<User>();
        ModeratedRoom test = (ModeratedRoom) matt.createModeratedRoom(a);
        jack.joinRoom(test);
        matt.banUser(test, null);
    }
    @Test (expected = IllegalArgumentException.class)
    public void premiumUserIAE4() throws OperationDeniedException {
        ArrayList<User> a = new ArrayList<User>();
        ModeratedRoom test = (ModeratedRoom) matt.createModeratedRoom(a);
        jack.joinRoom(test);
        matt.banUser(test, jack);
        matt.unbanUser(null, jack);
    }
    @Test (expected = IllegalArgumentException.class)
    public void premiumUserIAE5() throws OperationDeniedException {
        ArrayList<User> a = new ArrayList<User>();
        ModeratedRoom test = (ModeratedRoom) matt.createModeratedRoom(a);
        jack.joinRoom(test);
        matt.banUser(test, jack);
        matt.unbanUser(test, null);
    }
    @Test (expected = IllegalArgumentException.class)
    public void premiumUserIAE6() throws OperationDeniedException {
        ArrayList<User> a = new ArrayList<User>();
        ModeratedRoom test = (ModeratedRoom) matt.createModeratedRoom(a);
        matt.setNumVisibleLog(null, 100);
    }
    @Test (expected = IllegalArgumentException.class)
    public void premiumUserIAE7() throws OperationDeniedException {
        ArrayList<User> a = new ArrayList<User>();
        ModeratedRoom test = (ModeratedRoom) matt.createModeratedRoom(a);
        matt.setNumVisibleLog(test, 5);
    }
}

