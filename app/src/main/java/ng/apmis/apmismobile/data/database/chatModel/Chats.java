package ng.apmis.apmismobile.data.database.chatModel;

/**
 * Created by Thadeus-APMIS on 6/7/2018.
 */

public class Chats {

    private String chatMessage, userName;
    private int userImage;

    public Chats(String chatMessage, String userName, int userImage) {
        this.chatMessage = chatMessage;
        this.userName = userName;
        this.userImage = userImage;
    }

    public String getChatMessage() {
        return chatMessage;
    }

    public void setChatMessage(String chatMessage) {
        this.chatMessage = chatMessage;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getUserImage() {
        return userImage;
    }

    public void setUserImage(int userImage) {
        this.userImage = userImage;
    }

    @Override
    public String toString() {
        return "Chats{" +
                "chatMessage='" + chatMessage + '\'' +
                ", userName='" + userName + '\'' +
                ", userImage=" + userImage +
                '}';
    }
}
