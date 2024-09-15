package com.tandev.locket.model.login.check_email;

public class CheckEmailResponse {
    private Result result;

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public static class Result {
        private boolean needs_registration;
        private int status;

        public boolean isNeeds_registration() {
            return needs_registration;
        }

        public void setNeeds_registration(boolean needs_registration) {
            this.needs_registration = needs_registration;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }
    }
}
