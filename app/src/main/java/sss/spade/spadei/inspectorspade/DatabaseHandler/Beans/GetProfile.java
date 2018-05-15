package sss.spade.spadei.inspectorspade.DatabaseHandler.Beans;

/**
 * Created by hp on 9/13/2017.
 */
public class GetProfile {
    String activity;
    String notification;
    int _id;
    int position;

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public GetProfile(int position){
        this.position=position;
    }


    String noticationdescription;

    public String getNotidate() {
        return notidate;
    }

    public void setNotidate(String notidate) {
        this.notidate = notidate;
    }

    String notidate;

    public String getNoticationdescription() {
        return noticationdescription;
    }

    public void setNoticationdescription(String noticationdescription) {
        this.noticationdescription = noticationdescription;
    }

    public GetProfile(int id, String notification, String notificationdescription, String notidate) {
        this._id = id;
        this.notification = notification;
        this.noticationdescription=notificationdescription;
        this.notidate=notidate;

    }

    public GetProfile(String notification, String noticationdescription, String notidate){
        this.notification=notification;
        this.noticationdescription=noticationdescription;
        this.notidate=notidate;
    }
    public GetProfile(){}

    public GetProfile(String notification){
        this.notification=notification;
    }


    public String getNotification() {
        return this.notification;
    }

    public void setNotification(String notification) {
        this.notification = notification;
    }

    public int get_id() {
        return this._id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

}
