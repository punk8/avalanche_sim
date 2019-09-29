package com.punk.Constants;

public final class Constants {


    public static final double ALPHA =  0.5;
    public static final long DEFAULT_K = 10;

    public static int N = 6000; //总共节点数
    public static int K = 3; // 抽样节点数
    public static int Alpha = 2; //同种颜色的个数超过alpha则设置颜色为该颜色
    public static int ROUND = 1;

    public static final int BASEDLYBTWRP = 2000;				//节点之间的基础网络时延2s
    public static final int DLYRNGBTWRP = 3000;				//节点间的网络时延扰动范围3s
    public static final int BASEDLYBTWRPANDCLI = 1000;		//节点与客户端之间的基础网络时延
    public static final int DLYRNGBTWRPANDCLI = 1500;			//节点与客户端之间的网络时延扰动范围

}
