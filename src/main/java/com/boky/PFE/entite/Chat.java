package com.boky.PFE.entite;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.List;

@Table(name="chats")
@Entity
public class Chat {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int chatId;
    private String firstUserName;
    private String secondUserName;
    private  String emailfirstUserName;
    private String emailSecondeUser ;
    @OneToMany(mappedBy = "chat", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Message> messageList;

    public Chat() {
    }

    public Chat(int chatId, String firstUserName,String emailfirstUserName, String secondUserName, String emailSecondeUser ,List<Message> messageList) {
        this.chatId = chatId;
        this.firstUserName = firstUserName;
        this.emailfirstUserName=emailfirstUserName;
        this.secondUserName = secondUserName;
        this.emailSecondeUser=emailSecondeUser;
        this.messageList = messageList;
    }

    public String getEmailfirstUserName() {
        return emailfirstUserName;
    }

    public void setEmailfirstUserName(String emailfirstUserName) {
        this.emailfirstUserName = emailfirstUserName;
    }

    public String getEmailSecondeUser() {
        return emailSecondeUser;
    }

    public void setEmailSecondeUser(String emailSecondeUser) {
        this.emailSecondeUser = emailSecondeUser;
    }

    public int getChatId() {
        return chatId;
    }

    public void setChatId(int chatId) {
        this.chatId = chatId;
    }

    public String getFirstUserName() {
        return firstUserName;
    }

    public void setFirstUserName(String firstUserName) {
        this.firstUserName = firstUserName;
    }

    public String getSecondUserName() {
        return secondUserName;
    }

    public void setSecondUserName(String secondUserName) {
        this.secondUserName = secondUserName;
    }

    public List<Message> getMessageList() {
        return messageList;
    }

    public void setMessageList(List<Message> messageList) {
        this.messageList = messageList;
    }
}

