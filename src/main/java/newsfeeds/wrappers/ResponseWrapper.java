package newsfeeds.wrappers;

import org.codehaus.jackson.annotate.JsonProperty;

import java.io.Serializable;

/**
 * Created by samarth on 10/11/14.
 */
public class ResponseWrapper implements Serializable {
    @JsonProperty(value = "success")
    private boolean success;
    private Object result;
    private String status;

    public String getStatus() {
        return status;
    }


    public void setStatus(String status) {
        this.status = status;
    }

    @JsonProperty(value = "success")
    public boolean isSuccess() {
        return success;
    }
    @JsonProperty(value = "success")
    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

}
