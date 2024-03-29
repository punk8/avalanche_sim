package com.punk.node;

import com.punk.Constants.Constants;
import com.punk.algorim.Config;
import com.punk.message.Message;
import com.punk.message.ReplyMessage;
import com.punk.message.RequestMessage;
import com.punk.network.Network;
import org.omg.Messaging.SYNC_WITH_TRANSPORT;

import java.util.*;

import static com.punk.network.Network.*;

public class Node {

    public int type;

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

    public long startTime;
    public long finalTime;

    //map存放对应id节点发送的color
    public Map<Integer,Color> receiveMap = new HashMap<Integer, Color>();

    public int Round;

    public List<Integer> alreadySendList;

//    //最新的回复
//    public long lastReply;



    public Node(int id,int[] netDlys,int[] netDlyToClis){
        this.type = Constants.NORMAL;
        this.id = id;
        this.netDlys = netDlys;
        this.netDlyToClis = netDlyToClis;
        this.color = Color.None;
        this.count = 0;
        this.Round = 0;
        this.startTime = 0;
        this.alreadySendList = new ArrayList<Integer>();
        this.finalColor = Color.None;
    }

//    public Node(int id,Config config){
//        this.config = config;
//        this.id = id;
//        color = Color.None;
//    }

    public void run(){

    }

    public void msgProcess(Message msg){
        if(this.type == Constants.OFFLINE){
            System.out.println("["+this.id+"]"+" lost connection..");
            return;
        }
        msg.print(this.id+" process:"+receiveTag);
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
            this.startTime = recTime;
            this.color = recvColor;
            Message queryMessage = new RequestMessage(this.id,0,this.color,recTime);
//            List<Integer> sendTo = findSendTo();
//            Network.sendMsgToKOthers(queryMessage,this.id,sendTag,sendTo);
            Network.sendMsgToKOthers(queryMessage,this.id,sendTag);
        }
//        Message queryMessage = new RequestMessage(this.id,0,this.color,recTime);
//        Network.sendMsgToKOthers(queryMessage,this.id,sendTag);
        Message replyMessage = new ReplyMessage(this.id,msg.sendID,this.color,recTime);
        Network.sendMsg(replyMessage,sendTag);
    }

    public void receiveReply(Message msg){
//        System.out.println("current round = "+Round);

        if(msg == null)return;
        ReplyMessage replyMessage = (ReplyMessage)msg;
        long recTime = replyMessage.rcvtime + netDlys[msg.sendID];
//        lastReply = recTime;
        this.count ++;
        receiveMap.put(replyMessage.sendID,replyMessage.color);
        if(this.count == Constants.K){
            this.count = 0;

            Round ++;
            Map<Color, Integer> res=new HashMap<>();
            for (Map.Entry<Integer,Color> entry:receiveMap.entrySet()){
                if (res.containsKey(entry.getValue())){
                    res.put(entry.getValue(),res.get(entry.getValue())+1);
                }else{
                    res.put(entry.getValue(),1);
                }
            }
            if(res!=null&&res.containsKey(Color.Blue) &&(res.get(Color.Blue) >= Constants.Alpha)){
                this.color = Color.Blue;

            }else if(res!=null&&res.containsKey(Color.Red) &&res.get(Color.Red)>=Constants.Alpha){
                this.color = Color.Red;
            }

            if (Round >= Constants.ROUND){

                this.finalColor = this.color;
                this.finalTime = recTime;
                return;
            }else {
                //如果进入了下一轮
                Message queryMessage = new RequestMessage(this.id,0,this.color,recTime);
//                List<Integer> sendTo = findSendTo();
//                Network.sendMsgToKOthers(queryMessage,this.id,sendTag,sendTo);
                Network.sendMsgToKOthers(queryMessage,this.id,sendTag);

            }
            receiveMap.clear();

        }else {
//            System.currentTimeMillis()>=this.timer+
//            Network.sendWait();
            return;
        }
    }

    public List<Integer> findSendTo(){
        List<Integer> sendTo = new ArrayList<>();
        for(int i = 0; i < Constants.K; i++) {
            int toID = r.nextInt(Constants.N);
            if(toID != id) {
                if(!alreadySendList.contains(toID)){
                    sendTo.add(toID);
                    alreadySendList.add(toID);
                }
            }
        }
        return sendTo;
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
