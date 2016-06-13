package com.dheeru.apps.twitter.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;
/**
 * Created by dkthaku on 6/9/16.
 */
public class Message{

    @SerializedName("id")
    @Expose
    private Long id;
    @SerializedName("id_str")
    @Expose
    private String idStr;
    @SerializedName("text")
    @Expose
    private String text;
    @SerializedName("sender")
    @Expose
    private User sender;
    @SerializedName("sender_id")
    @Expose
    private Long senderId;
    @SerializedName("sender_id_str")
    @Expose
    private String senderIdStr;
    @SerializedName("sender_screen_name")
    @Expose
    private String senderScreenName;
    @SerializedName("recipient")
    @Expose
    private User recipient;
    @SerializedName("recipient_id")
    @Expose
    private Long recipientId;
    @SerializedName("recipient_id_str")
    @Expose
    private String recipientIdStr;
    @SerializedName("recipient_screen_name")
    @Expose
    private String recipientScreenName;
    @SerializedName("created_at")
    @Expose
    private Date createdAt;
    @SerializedName("entities")
    @Expose
    private TweetEntities entities;

    /**
     *
     * @return
     *     The id
     */
    public Long getId() {
        return id;
    }

    /**
     *
     * @param id
     *     The id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     *
     * @return
     *     The idStr
     */
    public String getIdStr() {
        return idStr;
    }

    /**
     *
     * @param idStr
     *     The id_str
     */
    public void setIdStr(String idStr) {
        this.idStr = idStr;
    }

    /**
     *
     * @return
     *     The text
     */
    public String getText() {
        return text;
    }

    /**
     *
     * @param text
     *     The text
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     *
     * @return
     *     The sender
     */
    public User getSender() {
        return sender;
    }

    /**
     *
     * @param sender
     *     The sender
     */
    public void setSender(User sender) {
        this.sender = sender;
    }

    /**
     *
     * @return
     *     The senderId
     */
    public Long getSenderId() {
        return senderId;
    }

    /**
     *
     * @param senderId
     *     The sender_id
     */
    public void setSenderId(Long senderId) {
        this.senderId = senderId;
    }

    /**
     *
     * @return
     *     The senderIdStr
     */
    public String getSenderIdStr() {
        return senderIdStr;
    }

    /**
     *
     * @param senderIdStr
     *     The sender_id_str
     */
    public void setSenderIdStr(String senderIdStr) {
        this.senderIdStr = senderIdStr;
    }

    /**
     *
     * @return
     *     The senderScreenName
     */
    public String getSenderScreenName() {
        return senderScreenName;
    }

    /**
     *
     * @param senderScreenName
     *     The sender_screen_name
     */
    public void setSenderScreenName(String senderScreenName) {
        this.senderScreenName = senderScreenName;
    }

    /**
     *
     * @return
     *     The recipient
     */
    public User getRecipient() {
        return recipient;
    }

    /**
     *
     * @param recipient
     *     The recipient
     */
    public void setRecipient(User recipient) {
        this.recipient = recipient;
    }

    /**
     *
     * @return
     *     The recipientId
     */
    public Long getRecipientId() {
        return recipientId;
    }

    /**
     *
     * @param recipientId
     *     The recipient_id
     */
    public void setRecipientId(Long recipientId) {
        this.recipientId = recipientId;
    }

    /**
     *
     * @return
     *     The recipientIdStr
     */
    public String getRecipientIdStr() {
        return recipientIdStr;
    }

    /**
     *
     * @param recipientIdStr
     *     The recipient_id_str
     */
    public void setRecipientIdStr(String recipientIdStr) {
        this.recipientIdStr = recipientIdStr;
    }

    /**
     *
     * @return
     *     The recipientScreenName
     */
    public String getRecipientScreenName() {
        return recipientScreenName;
    }

    /**
     *
     * @param recipientScreenName
     *     The recipient_screen_name
     */
    public void setRecipientScreenName(String recipientScreenName) {
        this.recipientScreenName = recipientScreenName;
    }

    /**
     *
     * @return
     *     The createdAt
     */
    public Date getCreatedAt() {
        return createdAt;
    }

    /**
     *
     * @param createdAt
     *     The created_at
     */
    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    /**
     *
     * @return
     *     The entities
     */
    public TweetEntities getEntities() {
        return entities;
    }

    /**
     *
     * @param entities
     *     The entities
     */
    public void setEntities(TweetEntities entities) {
        this.entities = entities;
    }

}

