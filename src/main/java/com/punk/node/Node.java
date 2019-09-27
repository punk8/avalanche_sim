package com.punk.node;

import com.punk.algorim.Config;
import com.punk.message.Message;
import com.punk.message.ReplyMessage;
import com.punk.message.RequestMessage;
import com.punk.network.Network;

import java.util.*;

import static com.punk.network.Network.*;

public class Node {

    public Color color; //表示节点颜色

    public Config config;

    public int id;

    public int netDlys[]; //与其她节点的网络延迟
    public int netDlyToClis[]; //与客户端的延迟

    public String receiveTag = "Receive";

    public String sendTag = "Send";

    //消息缓存<type, <msg>>:type消息类型;
    public Map<Integer, Set<Message>> msgCache;

    public int count;// 记录当前接收到多少个响应了
    public Color finalColor;

    //map存放对应id节点发送的color
    public Map<Integer,Color> receiveMap = new HashMap<Integer, Color>();

    public int Round;



    public Node(int id,int[] netDlys,int[] netDlyToClis){
        this.id = id;
        this.netDlys = netDlys;
        this.netDlyToClis = netDlyToClis;
        this.color = Color.None;
        this.count = 0;
        this.Round = 0;
    }

//    public Node(int id,Config config){
//        this.config = config;
//        this.id = id;
//        color = Color.None;
//    }

    public void run(){

    }

    public void msgProcess(Message msg){
        msg.print(receiveTag);
        switch (msg.type){
            case Message.REQUEST:
                receiveRequest(msg);
                break;
            case Message.REPLY:
                receiveReply(msg);
                break;
            default:
                System.out.println("【Error】消息类型错误！");
                return;
        }


    }

    public void receiveRequest(Message msg) {
        if(msg == null) return;
        RequestMessage requestMessage = (RequestMessage)msg;
        Color recvColor = requestMessage.color;
        long recTime = msg.rcvtime + netDlys[msg.sendID];
        if(this.color == Color.None){
            this.color = color;
            Message queryMessage = new RequestMessage(this.id,0,this.color,recTime);
            Network.sendMsgToKOthers(queryMessage,this.id,sendTag);
        }
        Message replyMessage = new ReplyMessage(this.id,msg.sendID,this.color,recTime);
        Network.sendMsg(replyMessage,sendTag);
    }

    public void receiveReply(Message msg){
        if(msg == null)return;
        ReplyMessage replyMessage = (ReplyMessage)msg;
        long recTime = msg.rcvtime + netDlys[msg.sendID];
        this.count ++;
        receiveMap.put(msg.sendID,msg.color);
        if(this.count == K){
            Round ++;
            Map<Color, Integer> res=new HashMap<>();
            for (Map.Entry<Integer,Color> entry:receiveMap.entrySet()){
                if (res.containsKey(entry.getValue())){
                    res.put(entry.getValue(),res.get(entry.getValue())+1);
                }else{
                    res.put(entry.getValue(),1);
                }
            }
            if(res.get(Color.Bule) > Alpha){
                this.color = Color.Bule;
            }else if(res.get(Color.Red)>Alpha){
                this.color = Color.Red;
            }

            if (Round == ROUND){
                this.finalColor = this.color;
                return;
            }

        }

        Message queryMessage = new RequestMessage(this.id,0,this.color,recTime);
        Network.sendMsgToKOthers(queryMessage,this.id,sendTag);

    }




    public String toString(){
        String str = "";
        str = str + "[node "+id+"]"+"color: "+this.color;
        return str;
    }

    /**
     * 将消息存到缓存中
     * @param m
     */
    private void addMessageToCache(Message m) {
        Set<Message> msgSet = msgCache.get(m.type);
        if(msgSet == null) {
            msgSet = new HashSet<>();
            msgCache.put(m.type, msgSet);
        }
        msgSet.add(m);
    }
}
