package com.tandev.locket.model.user;

import java.io.Serializable;
import java.util.List;

public class User implements Serializable {
    private String localId;
    private String email;
    private String displayName;
    private String photoUrl;
    private String passwordHash;
    private boolean emailVerified;
    private long passwordUpdatedAt;
    private List<ProviderUserInfo> providerUserInfo;
    private String validSince;
    private boolean disabled;
    private long lastLoginAt;
    private long createdAt;
    private boolean customAuth;
    private String lastRefreshAt;

    public String getLocalId() {
        return localId;
    }

    public void setLocalId(String localId) {
        this.localId = localId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public boolean isEmailVerified() {
        return emailVerified;
    }

    public void setEmailVerified(boolean emailVerified) {
        this.emailVerified = emailVerified;
    }

    public long getPasswordUpdatedAt() {
        return passwordUpdatedAt;
    }

    public void setPasswordUpdatedAt(long passwordUpdatedAt) {
        this.passwordUpdatedAt = passwordUpdatedAt;
    }

    public List<ProviderUserInfo> getProviderUserInfo() {
        return providerUserInfo;
    }

    public void setProviderUserInfo(List<ProviderUserInfo> providerUserInfo) {
        this.providerUserInfo = providerUserInfo;
    }

    public String getValidSince() {
        return validSince;
    }

    public void setValidSince(String validSince) {
        this.validSince = validSince;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    public long getLastLoginAt() {
        return lastLoginAt;
    }

    public void setLastLoginAt(long lastLoginAt) {
        this.lastLoginAt = lastLoginAt;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public boolean isCustomAuth() {
        return customAuth;
    }

    public void setCustomAuth(boolean customAuth) {
        this.customAuth = customAuth;
    }

    public String getLastRefreshAt() {
        return lastRefreshAt;
    }

    public void setLastRefreshAt(String lastRefreshAt) {
        this.lastRefreshAt = lastRefreshAt;
    }
}
