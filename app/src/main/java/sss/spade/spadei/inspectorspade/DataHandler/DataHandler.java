package sss.spade.spadei.inspectorspade.DataHandler;

/**
 * Created by hp on 17-02-2018.
 */

public class DataHandler {
    private static DataHandler datahandler=null;
    String signUpmail;
    String signUpMobile;
    String signUpPassword;
    String loginEmail;
    String loginMobile;
    String loginUsernum;
    String profilepersonnum;
    String moborEmailonforget;
    String passwordonfrgt;
    String farmnum;
    String user_num;
    String person_num;

    public String getUser_num() {
        return user_num;
    }

    public void setUser_num(String user_num) {
        this.user_num = user_num;
    }

    public String getPerson_num() {
        return person_num;
    }

    public void setPerson_num(String person_num) {
        this.person_num = person_num;
    }

    public String getFarmnum() {
        return farmnum;
    }

    public void setFarmnum(String farmnum) {
        this.farmnum = farmnum;
    }

    public String getLanding_farm_pet_name() {
        return landing_farm_pet_name;
    }

    public void setLanding_farm_pet_name(String landing_farm_pet_name) {
        this.landing_farm_pet_name = landing_farm_pet_name;
    }

    String landing_farm_pet_name;

    public String getPasswordonfrgt() {
        return passwordonfrgt;
    }

    public void setPasswordonfrgt(String passwordonfrgt) {
        this.passwordonfrgt = passwordonfrgt;
    }

    public String getMoborEmailonforget() {
        return moborEmailonforget;
    }

    public void setMoborEmailonforget(String moborEmailonforget) {
        this.moborEmailonforget = moborEmailonforget;
    }

    public String getProfilepersonnum() {
        return profilepersonnum;
    }

    public void setProfilepersonnum(String profilepersonnum) {
        this.profilepersonnum = profilepersonnum;
    }

    public String getLoginEmail() {
        return loginEmail;
    }

    public void setLoginEmail(String loginEmail) {
        this.loginEmail = loginEmail;
    }

    public String getLoginMobile() {
        return loginMobile;
    }

    public void setLoginMobile(String loginMobile) {
        this.loginMobile = loginMobile;
    }

    public String getLoginUsernum() {
        return loginUsernum;
    }

    public void setLoginUsernum(String loginUsernum) {
        this.loginUsernum = loginUsernum;
    }

    public String getSignUpmail() {
        return signUpmail;
    }

    public void setSignUpmail(String signUpmail) {
        this.signUpmail = signUpmail;
    }

    public String getSignUpMobile() {
        return signUpMobile;
    }

    public void setSignUpMobile(String signUpMobile) {
        this.signUpMobile = signUpMobile;
    }

    public String getSignUpPassword() {
        return signUpPassword;
    }

    public void setSignUpPassword(String signUpPassword) {
        this.signUpPassword = signUpPassword;
    }



    public static DataHandler newInstance(){
        if(datahandler==null){
            datahandler=new DataHandler();
        }
        return datahandler;
    }
}
