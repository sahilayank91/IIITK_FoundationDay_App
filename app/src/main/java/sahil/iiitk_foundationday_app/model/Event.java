package sahil.iiitk_foundationday_app.model;
// Made by tanuj
import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

public class Event {

    private long event_id;
    private String name,date,start_time,end_time,photo,details,coordinator,location;
    private int prize,max_members;

    public void Event(){
    }
    public Event(Context context){
    }

    public  Event(JSONObject object) throws JSONException {
        if (object.has("event_id")) this.event_id=object.getLong("event_id");
        if (object.has("name")) this.name=object.getString("name");
        if (object.has("date")) this.date=object.getString("date");
        if (object.has("start_time")) this.start_time=object.getString("start_time");
        if (object.has("end_time")) this.end_time=object.getString("end_time");
        if (object.has("photo")) this.photo=object.getString("photo");
        if (object.has("details")) this.details=object.getString("details");
        if (object.has("coordinator")) this.coordinator=object.getString("coordinator");
        if (object.has("location")) this.location=object.getString("location");
        if (object.has("prize")) this.prize=object.getInt("prize");
        if (object.has("max_members")) this.max_members=object.getInt("max_members");
    }

    @Override
    public boolean equals(Object obj) {
        Event event=(Event)obj;
        if(event.getEvent_id()==this.event_id)
            return true;
        else return false;
    }

    public long getEvent_id() {
        return event_id;
    }

    public void setEvent_id(long event_id) {
        this.event_id = event_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getCoordinator() {
        return coordinator;
    }

    public void setCoordinator(String coordinator) {
        this.coordinator = coordinator;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getPrize() {
        return prize;
    }

    public void setPrize(int prize) {
        this.prize = prize;
    }

    public int getMax_members() {
        return max_members;
    }

    public void setMax_members(int max_members) {
        this.max_members = max_members;
    }
}
