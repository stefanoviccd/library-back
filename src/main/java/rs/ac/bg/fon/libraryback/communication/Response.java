package rs.ac.bg.fon.libraryback.communication;

public class Response {
    private Object responseData;
    private Exception responseException;

    public void setResponseData(Object responseData) {
        this.responseData = responseData;
    }

    public Object getResponseData() {
        return responseData;
    }

    public Exception getResponseException() {
        return responseException;
    }

    public void setResponseException(Exception responseException) {
        this.responseException = responseException;
    }
}
