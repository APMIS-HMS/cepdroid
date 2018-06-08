package ng.apmis.apmismobile.data.database.chatModel;

/**
 * Created by Thadeus-APMIS on 6/7/2018.
 */

public class Chats {

    private String chatMessage, userName;
    private int userImage;
    private long date;

    public Chats(String chatMessage, String userName, int userImage, long date) {
        this.chatMessage = chatMessage;
        this.userName = userName;
        this.userImage = userImage;
        this.date = date;
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

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Chats{" +
                "chatMessage='" + chatMessage + '\'' +
                ", userName='" + userName + '\'' +
                ", userImage=" + userImage +
                ", date=" + date +
                '}';
    }
}
