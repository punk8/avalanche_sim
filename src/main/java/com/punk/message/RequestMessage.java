package com.punk.message;

import com.punk.node.Color;

public class RequestMessage extends Message {

    public RequestMessage(int sendID, int receiveID, Color color,long rcvtime) {
        super(sendID, receiveID, color,rcvtime);
        this.type = REQUEST;
    }
}
