package br.com.emerson.mail;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Message implements Serializable {

    private static final long serialVersionUID = 6423133505754922435L;

    @JsonIgnore
    private String from;

    private String[] to;

    private String[] cc;

    private String subject;

    private String text;

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String[] getTo() {
        return to;
    }

    public void setTo(String[] to) {
        this.to = to;
    }

    public String[] getCc() {
        return cc;
    }

    public void setCc(String[] cc) {
        this.cc = cc;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

}