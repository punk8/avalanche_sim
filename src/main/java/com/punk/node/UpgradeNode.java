package com.punk.node;

import com.punk.Constants.Constants;
import com.punk.message.Message;
import com.punk.message.ReplyMessage;
import com.punk.message.RequestMessage;
import com.punk.network.Network;

import java.util.HashMap;
import java.util.Map;

public class UpgradeNode extends Node {

    public int counter;

    public Color latestColor;

    public UpgradeNode(int id, int[] netDlys, int[] netDlyToClis) {
        super(id, netDlys, netDlyToClis);
        this.counter = 0;
        this.latestColor = Color.None;
    }

    @Override
    public void receiveRequest(Message msg) {
        if(msg == null) return;
//        if(finalColor != Color.None)return;
        RequestMessage requestMessage = (RequestMessage)msg;
        Color recvColor = requestMessage.color;
        long recTime = msg.rcvtime + netDlys[msg.sendID];
        if(this.color == Color.None){
            this.startTime = recTime;
            this.color = recvColor;
            this.latestColor = recvColor;
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

    @Override
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
                if(this.latestColor == Color.Blue){
                    counter++;
                }else {
                    this.latestColor = Color.Blue;
                    counter = 0;
                }


            }else if(res!=null&&res.containsKey(Color.Red) &&res.get(Color.Red)>=Constants.Alpha){
                this.color = Color.Red;
                if(this.latestColor == Color.Red){
                    counter++;
                }else {
                    this.latestColor = Color.Red;
                    counter = 0;
                }
            }
            if(counter>=Constants.BETA){
                this.finalColor = this.color;
                this.finalTime = recTime;
                return;
            } else {
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

}
