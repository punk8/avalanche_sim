package com.punk.message;

import com.punk.node.Color;

import java.util.Comparator;

public class Message {
    public static final int REQUEST = 0;

    public static final int REPLY = 1;

    public Color color;

    public int sendID;
    public int receiveID;

    public int type;

    public long rcvtime;  			//消息接收时间
    public long len;				//消息大小



    public Message(int sendID,int receiveID,Color color,long rcvtime){
        this.sendID = sendID;
        this.receiveID = receiveID;
        this.color = color;
        this.rcvtime = rcvtime;
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

    public static Comparator<Message> cmp = new Comparator<Message>(){
        @Override
        public int compare(Message c1, Message c2) {
            return (int) (c1.rcvtime - c2.rcvtime);
        }
    };

    public void print(String tag) {
//        if(!Simulator.SHOWDETAILINFO) return;
        String prefix = "【"+tag+"】";
        System.out.println(prefix+toString());
    }

    public String toString() {
        String[] typeName = {"Request","Reply"};
        return "消息类型:"+typeName[this.type]+";发送者id:"
                +sendID+";发送颜色:"+this.color+";接收者id:"+receiveID+";消息接收时间戳:"+rcvtime+";";
    }
    public Message copy(int rcvId, long rcvtime,int type) {
        if(type == REPLY){
            return new ReplyMessage(sendID, rcvId,this.color, rcvtime);
        }else if(type == REQUEST){

            return new RequestMessage(sendID, rcvId,this.color, rcvtime);

        }
        throw new Error("type error");
    }
}
