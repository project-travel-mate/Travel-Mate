package utils;

/**
 * Created by momen on 6/12/2018.
 */

public class ValidationException extends Exception {

    private String Message;

    public ValidationException(String message){
        Message = message;
    }
    public void setMessage(String message) {
        Message = message;
    }
    @Override
    public String getMessage() {
        return Message;
    }
}
