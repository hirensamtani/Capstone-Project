package com.example.hirensamtani;

/**
 * Created by hirensamtani on 21/6/16.
 */
public class EmailBean {
    private String fromEmailAddress;
    private String fromEmailPassword;
    private String toEmail;
    private String  subject;
    private String message;
    private String host;
    //"smtp.gmail.com"

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getFromEmailAddress() {
        return fromEmailAddress;
    }

    public void setFromEmailAddress(String fromEmailAddress) {
        this.fromEmailAddress = fromEmailAddress;
    }

    public String getFromEmailPassword() {
        return fromEmailPassword;
    }

    public void setFromEmailPassword(String fromEmailPassword) {
        this.fromEmailPassword = fromEmailPassword;
    }

    public String getToEmail() {
        return toEmail;
    }

    public void setToEmail(String toEmail) {
        this.toEmail = toEmail;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
