package com.cs175.bulletinandroid.bulletin;

/**
 * Created by Lucky on 12/6/16.
 */

public class ConversationResponse {
    private String userStart;
    private String userWith;

    public String getUserStart() {
        return userStart;
    }

    public void setUserStart(String userStart) {
        this.userStart = userStart;
    }

    public String getUserWith() {
        return userWith;
    }

    public void setUserWith(String userWith) {
        this.userWith = userWith;
    }

    public String getUserWithProfilePicture() {
        return userWithProfilePicture;
    }

    public void setUserWithProfilePicture(String userWithProfilePicture) {
        this.userWithProfilePicture = userWithProfilePicture;
    }

    public String getUserStartProfilePicture() {
        return userStartProfilePicture;
    }

    public void setUserStartProfilePicture(String userStartProfilePicture) {
        this.userStartProfilePicture = userStartProfilePicture;
    }

    public String getItemTitle() {
        return itemTitle;
    }

    public void setItemTitle(String itemTitle) {
        this.itemTitle = itemTitle;
    }

    public Double getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(Double itemPrice) {
        this.itemPrice = itemPrice;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public Double getLastTimestamp() {
        return lastTimestamp;
    }

    public void setLastTimestamp(Double lastTimestamp) {
        this.lastTimestamp = lastTimestamp;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public int getMessageCount() {
        return messageCount;
    }

    public void setMessageCount(int messageCount) {
        this.messageCount = messageCount;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    private String userWithProfilePicture;
    private String userStartProfilePicture;
    private String itemTitle;
    private Double itemPrice;
    private String itemDescription;
    private String itemId;
    private Double lastTimestamp;
    private String lastMessage;
    private int messageCount;
    private String _id;


}
