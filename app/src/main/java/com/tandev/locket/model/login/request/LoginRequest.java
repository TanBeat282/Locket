package com.tandev.locket.model.login.request;

public class LoginRequest {
    private String email;
    private String password;
    private String clientType;
    private boolean returnSecureToken;

    public LoginRequest(String email, String password, String clientType, boolean returnSecureToken) {
        this.email = email;
        this.password = password;
        this.clientType = clientType;
        this.returnSecureToken = returnSecureToken;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getClientType() {
        return clientType;
    }

    public void setClientType(String clientType) {
        this.clientType = clientType;
    }

    public boolean isReturnSecureToken() {
        return returnSecureToken;
    }

    public void setReturnSecureToken(boolean returnSecureToken) {
        this.returnSecureToken = returnSecureToken;
    }
}
