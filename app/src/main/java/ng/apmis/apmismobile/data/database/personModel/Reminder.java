package ng.apmis.apmismobile.data.database.personModel;

public class Reminder {

    private String type;
    private String name;
    private String message;
    private String time;

    public Reminder() {
    }

    public Reminder(String type, String name, String message, String time) {
        this.type = type;
        this.name = name;
        this.message = message;
        this.time = time;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "Reminder{" +
                "type='" + type + '\'' +
                ", name='" + name + '\'' +
                ", message='" + message + '\'' +
                ", time='" + time + '\'' +
                '}';
    }
}
