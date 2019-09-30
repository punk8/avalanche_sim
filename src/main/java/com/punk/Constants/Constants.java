package com.punk.Constants;

public final class Constants {


    public static final double ALPHA =  0.2; //只要全网到达这个数就当作已经全网共识了
    public static final long DEFAULT_K = 10;

    public static int N = 1000; //总共节点数
    public static int K = 31; // 抽样节点数
    public static int Alpha = 16; //同种颜色的个数超过alpha则设置颜色为该颜色
    public static int ROUND = 4;
    public static int BETA = 4;


    public static int M = (int) (N*ALPHA);//拜占庭节点个数


    public static final int BASEDLYBTWRP = 2000;				//节点之间的基础网络时延2s
    public static final int DLYRNGBTWRP = 300;				//节点间的网络时延扰动范围0.3s
    public static final int BASEDLYBTWRPANDCLI = 1000;		//节点与客户端之间的基础网络时延
    public static final int DLYRNGBTWRPANDCLI = 1500;			//节点与客户端之间的网络时延扰动范围


    public static final int NORMAL = 0;
    public static final int OFFLINE = 1;
    public static final int BYZT = 2;




}
