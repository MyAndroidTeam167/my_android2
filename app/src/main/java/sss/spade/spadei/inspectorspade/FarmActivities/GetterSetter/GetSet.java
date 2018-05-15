package sss.spade.spadei.inspectorspade.FarmActivities.GetterSetter;

/**
 * Created by user on 3/2/18.
 */

public class GetSet {
    private String message;
    private String date;
    private String byFarmer;
    private String byAdmin;
    private String byInspector;
    private String msgType;
    private String audioReply;

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    public String getAudioReply() {
        return audioReply;
    }

    public void setAudioReply(String audioReply) {
        this.audioReply = audioReply;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setByFarmer(String byFarmer) {
        this.byFarmer = byFarmer;
    }

    public void setByAdmin(String byAdmin) {
        this.byAdmin = byAdmin;
    }

    public void setByInspector(String byInspector) {
        this.byInspector = byInspector;
    }

    public String getMessage() {
        return this.message;
    }

    public String getDate() {
        return this.date;
    }

    public String getByFarmer() {
        return this.byFarmer;
    }

    public String getByAdmin() {
        return this.byAdmin;
    }

    public String getByInspector() {
        return this.byInspector;
    }
}