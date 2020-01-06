package common;

/**
 * author       : frog
 * time         : 2019/4/23 上午9:45
 * desc         :
 * version      : 1.3.0
 */
public class RespData<T> {

    private int status;
    private String reason;
    private T data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
