package com.tandev.locket.model.login.check_email;

public class CheckEmailRequest {
    private String operation;
    private String email;

    public CheckEmailRequest(String operation, String email) {
        this.operation = operation;
        this.email = email;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
