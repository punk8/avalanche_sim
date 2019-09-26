package com.punk.message;

import com.punk.node.Color;

public class ReplyMessage extends Message {


    public ReplyMessage(int sendID, int receiveID, Color color) {
        super(sendID, receiveID, color);
        this.type = REPLY;
    }
}
