package com.punk.network;


import com.punk.Client;
import com.punk.Constants.Constants;
import com.punk.Utils;
import com.punk.message.Message;
import com.punk.message.RequestMessage;
import com.punk.node.Color;
import com.punk.node.Node;

import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.LinkedBlockingQueue;

public class Network {



    //消息优先队列（按消息计划被处理的时间戳排序）
    public static Queue<Message> msgQue = new PriorityQueue<>(Message.cmp);


//    //正在网络中传播的消息的总大小
//    public static long inFlyMsgLen = 0;

    public static Random r = new Random();



    //初始化节点间的网络延时以及客户端与节点的网络延时
    public static int[][] netDlys = netDlyBtwRpInit(Constants.N);

    public static int[][] netDlysToClis = netDlyBtwRpAndCliInit(Constants.N, 1);

    public static int[][] netDlysToNodes = Utils.flipMatrix(netDlysToClis);

    /**
     * 随机初始化replicas节点之间的基础网络传输延迟
     * @param n 表示节点总数
     * @return	返回节点之间的基础网络传输延迟数组
     */
    public static int[][] netDlyBtwRpInit(int n){
        int[][] ltcs = new int[n][n];
        Random rand = new Random(999);
        for(int i = 0; i < n; ++i)
            for(int j = 0; j < n; ++j)
                if(i < j && ltcs[i][j] == 0) {
                    ltcs[i][j] = Constants.BASEDLYBTWRP + rand.nextInt(Constants.DLYRNGBTWRP);
                    ltcs[j][i] = ltcs[i][j];
                }
        return ltcs;
    }

    /**
     * 随机初始化客户端与各节点之间的基础网络传输延迟
     * @param n 表示节点数量
     * @param m 表示客户端数量
     * @return 返回客户端与各节点之间的基础网络传输延迟
     */
    public static int[][] netDlyBtwRpAndCliInit(int n, int m){
        int[][] ltcs = new int[n][m];
        Random rand = new Random(666);
        for(int i = 0; i < n; i++)
            for(int j = 0; j < m; j++)
                ltcs[i][j] = Constants.BASEDLYBTWRPANDCLI + rand.nextInt(Constants.DLYRNGBTWRPANDCLI);
        return ltcs;
    }


    public static void main(String[] args) {
        //初始化包含FN个拜占庭意节点的RN个replicas
//		boolean[] byzts = byztDistriInit(RN, FN);
        Node[] nodes = new Node[Constants.N];
        for(int i = 0; i < Constants.N; i++) {
            nodes[i] = new Node(i, netDlys[i], netDlysToClis[i]);

        }




//        //初始化CN个客户端
//        Client cli = new Client();
//
//        //初始随机发送INFLIGHT个请求消息
//        Random rand = new Random(555);
//        int requestNums = 0;
//        for(int i = 0; i < Math.min(INFLIGHT, REQNUM); i++) {
//            //随机一个客户端发送请求
//            clis[rand.nextInt(CN)].sendRequest(0);
//            requestNums++;
//        }
//
//        long timestamp = 0;
//        //消息处理
////		int ttt = 0;
//        while(!msgQue.isEmpty()) {
//            Message msg = msgQue.poll();
////            switch(msg.type) {
////                case Message.REPLY:
////                default:
////                    nodes[msg.receiveID].msgProcess(msg);
////            }
//            nodes[msg.receiveID].msgProcess(msg);
//            //如果还未达到稳定状态的request消息小于INFLIGHT，随机选择一个客户端发送请求消息
//            if(requestNums - getStableRequestNum(clis) < INFLIGHT && requestNums < REQNUM) {
//                clis[rand.nextInt(CN)].sendRequest(msg.rcvtime);
//                requestNums++;
//            }
//            inFlyMsgLen -= msg.len;
//            timestamp = msg.rcvtime;
//            if(getNetDelay(inFlyMsgLen, 0) > COLLAPSEDELAY ) {
//                System.out.println("【Error】网络消息总负载"+inFlyMsgLen
//                        +"B,网络传播时延超过"+COLLAPSEDELAY/1000
//                        +"秒，系统已严重拥堵，不可用！");
//                break;
//            }
//        }
//        long totalTime = 0;
//        long totalStableMsg = 0;
//        for(int i = 0; i < CN; i++) {
//            totalTime += clis[i].accTime;
//            totalStableMsg += clis[i].stableMsgNum();
//        }
//        double tps = getStableRequestNum(clis)/(double)(timestamp/1000);
//        System.out.println("【The end】消息平均确认时间为:"+totalTime/totalStableMsg
//                +"毫秒;消息吞吐量为:"+tps+"tps");
    }


    public static void sendMsg(Message msg, String tag) {
        msg.print(msg.sendID+" process:"+tag);
        msgQue.add(msg);
//        inFlyMsgLen += msg.len;
    }

    //必须发送K条
    public static void sendMsgToKOthers(Message msg, int id, String tag) {
        for(int i = 0; i < Constants.K; i++) {
            int toID = r.nextInt(Constants.N);
            toID = checkID(toID,id);
            if(toID != id) {
                Message m = msg.copy(toID, msg.rcvtime + netDlys[id][i],msg.type);
//                m = (RequestMessage)m;
                sendMsg(m, tag);
            }
        }
//        for(int i =0 ;i<sendTo.size();i++){
//            Message m = msg.copy(sendTo.get(i), msg.rcvtime + netDlys[id][i],msg.type);
////                m = (RequestMessage)m;
//            sendMsg(m, tag);
//        }
    }

    public static int checkID(int toID,int sendID){
        if (toID != sendID){
            return toID;
        }else {
            toID = r.nextInt(Constants.N);
            toID = checkID(toID,sendID);
            return toID;
        }
    }

//    public static void sendWait(){
//        long time = System.currentTimeMillis();
//        Message msg = new Message(0,0, Color.None,time);
//        msg.type = 2;
//        msgQue.add(msg);
//    }

}
