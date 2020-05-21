package ro.pub.cs.systems.eim.practicaltest02.model;

public class AlarmInformation {

    private String hour;
    private String minute;

    public AlarmInformation() {
        this.hour = null;
        this.minute = null;
    }

    public AlarmInformation(String hour, String minute) {
        this.hour = hour;
        this.minute = minute;
    }

    public String getHour() {
        return hour;
    }

    public String getMinute() {
        return minute;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public void setMinute(String minute) {
        this.minute = minute;
    }

    @Override
    public String toString() {
        return "AlarmInformation{" +
                "hour='" + hour + '\'' +
                ", minute='" + minute + '\'' +
                '}';
    }
}
