package com.punk;

import com.punk.message.Message;
import com.punk.message.RequestMessage;
import com.punk.network.Network;
import com.punk.node.Color;
import com.punk.node.Node;

import java.sql.Time;

import static com.punk.network.Network.*;

public class NodeTest {

    public static void main(String[] args){
        Node[] nodes = new Node[N];
        for(int i = 0; i < N; i++) {
            nodes[i] = new Node(i, netDlys[i], netDlysToClis[i]);
        }

        nodes[0].color = Color.Bule;
//        for(int i = 0; i < N; i++) {
//            System.out.println(nodes[i]);
//        }

        Message message = new RequestMessage(nodes[0].id,0,nodes[0].color, System.currentTimeMillis());
        System.out.println(message.type);
        Network.sendMsgToKOthers(message,nodes[0].id,"send");
        while (!msgQue.isEmpty()){
            Message msg = msgQue.poll();
            switch(msg.type){
                case Message.REPLY:
                case Message.REQUEST:
                    nodes[msg.receiveID].msgProcess(msg);
                    break;
            }


        }
    }
}
