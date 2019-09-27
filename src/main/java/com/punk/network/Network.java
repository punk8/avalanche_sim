package com.punk.network;


import com.punk.Client;
import com.punk.Utils;
import com.punk.node.Node;

import java.util.Random;

public class Network {

    public static int N = 1000; //总共节点数
    public static int K = 20; // 抽样节点数
    public static int Alpha = 15; //同种颜色的个数超过alpha则设置颜色为该颜色

    public static final int BASEDLYBTWRP = 2;				//节点之间的基础网络时延
    public static final int DLYRNGBTWRP = 1;				//节点间的网络时延扰动范围
    public static final int BASEDLYBTWRPANDCLI = 10;		//节点与客户端之间的基础网络时延
    public static final int DLYRNGBTWRPANDCLI = 15;			//节点与客户端之间的网络时延扰动范围




    //初始化节点间的网络延时以及客户端与节点的网络延时
    public static int[][] netDlys = netDlyBtwRpInit(K);

    public static int[][] netDlysToClis = netDlyBtwRpAndCliInit(K, 1);

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
                    ltcs[i][j] = BASEDLYBTWRP + rand.nextInt(DLYRNGBTWRP);
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
                ltcs[i][j] = BASEDLYBTWRPANDCLI + rand.nextInt(DLYRNGBTWRPANDCLI);
        return ltcs;
    }


    public static void main(String[] args) {
        //初始化包含FN个拜占庭意节点的RN个replicas
//		boolean[] byzts = byztDistriInit(RN, FN);
        Node[] nodes = new Node[K];
        for(int i = 0; i < K; i++) {
            nodes[i] = new Node(i, netDlys[i], netDlysToClis[i]);

        }



        //初始化CN个客户端
        Client cli = new Client();

        //初始随机发送INFLIGHT个请求消息
        Random rand = new Random(555);
        int requestNums = 0;
        for(int i = 0; i < Math.min(INFLIGHT, REQNUM); i++) {
            //随机一个客户端发送请求
            clis[rand.nextInt(CN)].sendRequest(0);
            requestNums++;
        }

        long timestamp = 0;
        //消息处理
//		int ttt = 0;
        while(!msgQue.isEmpty()) {
            Message msg = msgQue.poll();
            switch(msg.type) {
                case Message.REPLY:
                case Message.CLITIMEOUT:
                    clis[Client.getCliArrayIndex(msg.rcvId)].msgProcess(msg);
                    break;
                default:
                    reps[msg.rcvId].msgProcess(msg);
            }
            //如果还未达到稳定状态的request消息小于INFLIGHT，随机选择一个客户端发送请求消息
            if(requestNums - getStableRequestNum(clis) < INFLIGHT && requestNums < REQNUM) {
                clis[rand.nextInt(CN)].sendRequest(msg.rcvtime);
                requestNums++;
            }
            inFlyMsgLen -= msg.len;
            timestamp = msg.rcvtime;
            if(getNetDelay(inFlyMsgLen, 0) > COLLAPSEDELAY ) {
                System.out.println("【Error】网络消息总负载"+inFlyMsgLen
                        +"B,网络传播时延超过"+COLLAPSEDELAY/1000
                        +"秒，系统已严重拥堵，不可用！");
                break;
            }
        }
        long totalTime = 0;
        long totalStableMsg = 0;
        for(int i = 0; i < CN; i++) {
            totalTime += clis[i].accTime;
            totalStableMsg += clis[i].stableMsgNum();
        }
        double tps = getStableRequestNum(clis)/(double)(timestamp/1000);
        System.out.println("【The end】消息平均确认时间为:"+totalTime/totalStableMsg
                +"毫秒;消息吞吐量为:"+tps+"tps");
    }

}
