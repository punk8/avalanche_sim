package com.punk.node;

import com.punk.algorim.Config;
import com.punk.algorim.SlushConfig;
import com.punk.message.Message;
import com.punk.message.ReplyMessage;
import com.punk.message.RequestMessage;
import sun.misc.Request;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static com.punk.message.Message.REQUEST;

public class Node {

    public Color color; //表示节点颜色

    public Config config;

    public int id;

    public int netDlys[]; //与其她节点的网络延迟
    public int netDlyToClis[]; //与客户端的延迟



    //map存放对应id节点发送的color
    public Map<Integer,Color> receiveMap = new HashMap<Integer, Color>();

    public Node(int id,int[] netDlys,int[] netDlyToClis){
        this.id = id;
        this.netDlys = netDlys;
        this.netDlyToClis = netDlyToClis;
        this.color = Color.None;

    }

//    public Node(int id,Config config){
//        this.config = config;
//        this.id = id;
//        color = Color.None;
//    }

    public void run(){

    }

    public void msgProcess(Message msg){
        switch (msg.type){
            case Message.REQUEST:
                receiveRequest(msg);
                break;
            case Message.REPLY:
                receiveREPLY(msg);
                break;
            default:
                System.out.println("【Error】消息类型错误！");
                return;
        }


    }

    public void receiveRequest(Message msg) {
        if(msg == null) return;
        RequestMessage requestMessage = (RequestMessage)msg;


    }

    public void receiveREPLY(Message msg){
        if(msg == null)return;
        ReplyMessage replyMessage = (ReplyMessage)msg;
    }

    public void query(){

    }




    public String toString(){
        String str = "";
        str = str + "[node "+id+"]"+"color: "+this.color;
        return str;
    }
}
