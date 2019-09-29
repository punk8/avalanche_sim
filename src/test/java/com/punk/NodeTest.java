package com.punk;

import com.punk.Constants.Constants;
import com.punk.message.Message;
import com.punk.message.RequestMessage;
import com.punk.network.Network;
import com.punk.node.ByztNode;
import com.punk.node.Color;
import com.punk.node.Node;
import com.punk.node.UpgradeNode;

import java.sql.Time;

import static com.punk.network.Network.*;

public class NodeTest {

    public static void main(String[] args){
        long totaltime = 0;
        long BlueColor = 0;
        long RedColor = 0;
        long noColor = 0;


        Node[] nodes = new Node[Constants.N];
        for(int i = 0; i < Constants.N; i++) {
//            nodes[i] = new Node(i, netDlys[i], netDlysToClis[i]);
            if(2<i&&i<203){
                nodes[i] = new ByztNode(i,netDlys[i],netDlysToClis[i]);
            }else {

                nodes[i] = new Node(i, netDlys[i], netDlysToClis[i]);

            }
        }


        long startTime = System.currentTimeMillis();

//        for(int i = 0;i<1;i++){
//            nodes[i+13].type = Constants.OFFLINE;
//        }


//        nodes[0].color = Color.Red;
//        Message message = new RequestMessage(nodes[0].id,0,nodes[0].color, startTime);
//        nodes[0].startTime = startTime;
//        System.out.println(message.type);
//        Network.sendMsgToKOthers(message,nodes[0].id,"send",nodes[0].findSendTo());


//        nodes[1].color = Color.Blue;
//        Message message1 = new RequestMessage(nodes[1].id,0,nodes[1].color, startTime);
//        nodes[1].startTime = startTime;
////        System.out.println(message1.type);
//        Network.sendMsgToKOthers(message,nodes[0].id,"send",nodes[0].findSendTo());


//        Network.sendMsgToKOthers(message1,nodes[1].id,"send");


//        Network.sendMsgToKOthers(message,nodes[0].id,"send");
//        Network.sendMsgToKOthers(message,nodes[0].id,"send");




        nodes[2].color = Color.Blue;
        Message message2 = new RequestMessage(nodes[2].id,0,nodes[2].color, startTime);
        nodes[2].startTime = startTime;
        System.out.println(message2.type);
//        Network.sendMsgToKOthers(message,nodes[0].id,"send",nodes[0].findSendTo());
        Network.sendMsgToKOthers(message2,nodes[2].id,"send");
outterLoop:
        while (!msgQue.isEmpty()){
            Message msg = msgQue.poll();
            nodes[msg.receiveID].msgProcess(msg);
            if(isStable(nodes)){
                break outterLoop;
            }
        }

//        msgQue.isEmpty();
        BlueColor = 0;
        RedColor = 0;
        noColor = 0;
        long roundCount = 0;
        for(int i=0;i<Constants.N;i++){

            if(nodes[i].finalColor == Color.Blue){
                BlueColor ++;
            }else if(nodes[i].finalColor == Color.Red){
                RedColor ++;
            }else {
                noColor ++;
            }

            totaltime += (nodes[i].finalTime-nodes[i].startTime);

        }

        System.out.println("average time = "+totaltime/Constants.N+" blue color = "+BlueColor+" Red color = "+RedColor+" no color = "+noColor);


    }
    public static boolean isStable(Node[] nodes){
        long BlueColor = 0;
        long RedColor = 0;
        long noColor = 0;
        for(int i=0;i<Constants.N;i++){
            if(nodes[i].finalColor == Color.Blue){
                BlueColor ++;
            }else if(nodes[i].finalColor == Color.Red){
                RedColor ++;
            }else {
                noColor ++;
            }
            if(BlueColor>=Constants.ALPHA || RedColor>=Constants.ALPHA){
                return true;

            }
        }
        return false;

    }
}
