package response.search;

import com.google.gson.annotations.SerializedName;

public class SearchUsersInfoBean {

    /**
     * id : 1039
     * name : qbjf1571215429
     * grade_name : lv1
     * _ : 0
     * head_url :
     * home_address :
     * trade :
     * Career :
     */

    @SerializedName("id")
    private int id;
    @SerializedName("name")
    private String name;
    @SerializedName("grade")
    private int grade;
    @SerializedName("head_url")
    private String headUrl;
    @SerializedName("home_address")
    private String homeAddress;
    @SerializedName("trade")
    private String trade;
    @SerializedName("career")
    private String career;
    @SerializedName("personal_profile")
    private String personalProfile;

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public String getPersonalProfile() {
        return personalProfile;
    }

    public void setPersonalProfile(String personalProfile) {
        this.personalProfile = personalProfile;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHeadUrl() {
        return headUrl;
    }

    public void setHeadUrl(String headUrl) {
        this.headUrl = headUrl;
    }

    public String getHomeAddress() {
        return homeAddress;
    }

    public void setHomeAddress(String homeAddress) {
        this.homeAddress = homeAddress;
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

    public void setCareer(String Career) {
        this.career = Career;
    }
}
