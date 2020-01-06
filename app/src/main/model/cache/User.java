package cache;

import com.google.gson.annotations.SerializedName;

/**
 * author       : frog
 * time         : 2019/4/11 下午12:10
 * desc         : 用户信息
 * version      : 1.3.0
 */
public class User {

    /**
     * account_id : 0
     * account :
     * sex : 0
     * age : 0
     * nickname :
     * email :
     * phone :
     * head_url :
     * signature :
     * personal_profile :
     * trade :
     * education_institution :
     * education :
     * home_address :
     * email_status : 0
     * is_moderator : false
     */

    @SerializedName("account_id")
    private int accountId;
    @SerializedName("account")
    private String account;
    @SerializedName("sex")
    private int sex;
    @SerializedName("age")
    private int age;
    @SerializedName("nickname")
    private String nickname;
    @SerializedName("email")
    private String email;
    @SerializedName("phone")
    private String phone;
    @SerializedName("head_url")
    private String headUrl;
    @SerializedName("signature")
    private String signature;
    @SerializedName("personal_profile")
    private String personalProfile;
    @SerializedName("trade")
    private String trade;
    @SerializedName("career")
    private String career;
    @SerializedName("education_institution")
    private String educationInstitution;
    @SerializedName("education")
    private String education;
    @SerializedName("home_address")
    private String homeAddress;
    @SerializedName("email_status")
    private int emailStatus;
    @SerializedName("is_moderator")
    private boolean isModerator;
    @SerializedName("icon_id")
    private int iconId;

    public int getIconId() {
        return iconId;
    }

    public void setIconId(int iconId) {
        this.iconId = iconId;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getHeadUrl() {
        return headUrl;
    }

    public void setHeadUrl(String headUrl) {
        this.headUrl = headUrl;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getPersonalProfile() {
        return personalProfile;
    }

    public void setPersonalProfile(String personalProfile) {
        this.personalProfile = personalProfile;
    }

    public String getTrade() {
        return trade;
    }

    public void setTrade(String trade) {
        this.trade = trade;
    }

    public String getCareer() {
        return career;
    }

    public void setCareer(String career) {
        this.career = career;
    }

    public String getEducationInstitution() {
        return educationInstitution;
    }

    public void setEducationInstitution(String educationInstitution) {
        this.educationInstitution = educationInstitution;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getHomeAddress() {
        return homeAddress;
    }

    public void setHomeAddress(String homeAddress) {
        this.homeAddress = homeAddress;
    }

    public int getEmailStatus() {
        return emailStatus;
    }

    public void setEmailStatus(int emailStatus) {
        this.emailStatus = emailStatus;
    }

    public boolean isModerator() {
        return isModerator;
    }

    public void setModerator(boolean moderator) {
        isModerator = moderator;
    }
}
