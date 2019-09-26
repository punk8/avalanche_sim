package com.punk.message;

import com.punk.node.Color;

public class Message {
    public static final int REQUEST = 1;

    public static final int REPLY = 2;

    public Color color;

    public int sendID;
    public int receiveID;

    public int type;


    public Message(int sendID,int receiveID,Color color){
        this.sendID = sendID;
        this.receiveID = receiveID;
        this.color = color;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public int getSendID() {
        return sendID;
    }

    public void setSendID(int sendID) {
        this.sendID = sendID;
    }

    public int getReceiveID() {
        return receiveID;
    }

    public void setReceiveID(int receiveID) {
        this.receiveID = receiveID;
    }
}
