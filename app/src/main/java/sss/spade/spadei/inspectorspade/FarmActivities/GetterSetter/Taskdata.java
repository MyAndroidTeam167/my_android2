package sss.spade.spadei.inspectorspade.FarmActivities.GetterSetter;

/**
 * Created by hp on 05-03-2018.
 */


/**
 * Created by hp on 12/16/2017.
 */
public class Taskdata {

    String taskTitle;
    String taskDate;
    String taskId;
    String taskDescription;
    String taskStatus;
    String isDone;
    String verification_date;

    public String getFarm_dwork_num() {
        return farm_dwork_num;
    }

    public void setFarm_dwork_num(String farm_dwork_num) {
        this.farm_dwork_num = farm_dwork_num;
    }

    String farm_dwork_num;

    public String getVerification_date() {
        return verification_date;
    }





    public void setVerification_date(String verification_date) {
        this.verification_date = verification_date;
    }

    public String getIsDone(){
        return isDone;
    }
    public void setIsDone(String isDone){
        this.isDone=isDone;
    }
    public String getImgBgLink() {
        return imgBgLink;
    }

    public void setImgBgLink(String imgBgLink) {
        this.imgBgLink = imgBgLink;
    }

    String imgBgLink;

    public String getTaskTitle() {
        return taskTitle;
    }

    public void setTaskTitle(String taskTitle) {
        this.taskTitle = taskTitle;
    }

    public String getTaskDate() {
        return taskDate;
    }

    public void setTaskDate(String taskDate) {
        this.taskDate = taskDate;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getTaskDescription() {
        return taskDescription;
    }

    public void setTaskDescription(String taskDescription) {
        this.taskDescription = taskDescription;
    }

    public String getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(String taskStatus) {
        this.taskStatus = taskStatus;
    }


}
