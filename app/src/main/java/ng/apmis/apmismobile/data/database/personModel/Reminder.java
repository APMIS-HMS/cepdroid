package ng.apmis.apmismobile.data.database.personModel;

public class Reminder {

    private String type;
    private String name;
    private String time;
    private Object body;

    public Reminder() {
    }

    public Reminder(String type, String name, String time, Object body) {
        this.type = type;
        this.name = name;
        this.time = time;
        this.body = body;
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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Object getBody() {
        return body;
    }

    public void setBody(Object body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return "Reminder{" +
                "type='" + type + '\'' +
                ", name='" + name + '\'' +
                ", time='" + time + '\'' +
                '}';
    }
}
