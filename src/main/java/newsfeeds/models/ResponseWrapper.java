package newsfeeds.models;

/**
 * Created by samarth on 10/11/14.
 */
public class ResponseWrapper {
    private boolean success;
    private Object result;
    private Integer count;

    public boolean getSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}
