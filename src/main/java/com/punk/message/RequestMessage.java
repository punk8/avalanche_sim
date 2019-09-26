package com.punk.message;

import com.punk.node.Color;

public class RequestMessage extends Message {

    public RequestMessage(int sendID, int receiveID, Color color,int type) {
        super(sendID, receiveID, color);
        this.type = REQUEST;

    }
}
