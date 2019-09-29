package com.punk;

import com.punk.Constants.Constants;
import com.punk.message.Message;
import com.punk.message.RequestMessage;
import com.punk.network.Network;
import com.punk.node.Color;
import com.punk.node.Node;

import java.sql.Time;

import static com.punk.network.Network.*;

public class NodeTest {

    public static void main(String[] args){
        Node[] nodes = new Node[Constants.N];
        for(int i = 0; i < Constants.N; i++) {
            nodes[i] = new Node(i, netDlys[i], netDlysToClis[i]);
        }

        nodes[0].color = Color.Bule;

//        for(int i = 0; i < N; i++) {
//            System.out.println(nodes[i]);
//        }

        long startTime = System.currentTimeMillis();
        Message message = new RequestMessage(nodes[0].id,0,nodes[0].color, startTime);
        nodes[0].startTime = startTime;
        System.out.println(message.type);
        Network.sendMsgToKOthers(message,nodes[0].id,"send");
        while (!msgQue.isEmpty()){
            Message msg = msgQue.poll();
//            switch(msg.type){
//                case Message.REPLY:
//                case Message.REQUEST:
//                    nodes[msg.receiveID].msgProcess(msg);
////                    System.out.println("msgprocess");
//                    break;
//                case 2:
//                    break;
//            }

//            if(msgQue.size() <= 100){
//                System.out.println("1++++0");
//            }
            nodes[msg.receiveID].msgProcess(msg);
        }

        msgQue.isEmpty();
        long totaltime = 0;
        long consensusColor = 0;
        long noColor = 0;
        long roundCount = 0;
        for(int i=0;i<Constants.N;i++){
//            System.out.println(nodes[i].finalTime- nodes[i].startTime);
//            System.out.println(nodes[i].finalColor);
            if(nodes[i].finalColor == Color.Bule){
                consensusColor ++;
            }else if(nodes[i].finalColor == null){
                noColor ++;
            }

            if(nodes[i].Round == 1){
                roundCount ++;
            }
        }

        System.out.println("average time = "+totaltime+" consens color = "+consensusColor+" no color = "+noColor);
        System.out.println(roundCount);
        System.out.println(msgQue.isEmpty());

    }
}
