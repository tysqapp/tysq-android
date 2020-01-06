package request;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * author       : frog
 * time         : 2019-07-23 11:43
 * desc         : 获取配置
 * version      : 1.3.0
 */
public class ConfigurationReq {

    @SerializedName("actions")
    private List<String> actions;

    public List<String> getActions() {
        return actions;
    }

    public void setActions(List<String> actions) {
        this.actions = actions;
    }

}
