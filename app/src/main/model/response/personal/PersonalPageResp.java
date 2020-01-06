package response.personal;

import com.google.gson.annotations.SerializedName;

public class PersonalPageResp {
    @SerializedName("account_info")
    private PersonalAccountInfo accountInfo;
    @SerializedName("asset")
    private PersonalAsset asset;

    public PersonalAccountInfo getAccountInfo() {
        return accountInfo;
    }

    public void setAccountInfo(PersonalAccountInfo accountInfo) {
        this.accountInfo = accountInfo;
    }

    public PersonalAsset getAsset() {
        return asset;
    }

    public void setAsset(PersonalAsset asset) {
        this.asset = asset;
    }
}
