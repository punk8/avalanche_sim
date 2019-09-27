package com.punk;

public class Utils {

    //矩阵翻转
    public static int[][] flipMatrix(int[][] matrix){
        int m = matrix.length,n = matrix[0].length;
        int[][] flipMa = new int[n][m];
        for(int i = 0;i<n;i++){
            for(int j = 0;j<m;j++){
                flipMa[i][j] = matrix[j][i];
            }
        }
        return flipMa;
    }

    /**
     * 将字节数组转换成16进制字符串
     * @param bytes 即将转换的数据
     * @return 16进制字符串
     */
    private static String bytesToHexString(byte[] bytes){
        StringBuffer sb = new StringBuffer(bytes.length);
        String temp = null;
        for (int i = 0;i< bytes.length;i++){
            temp = Integer.toHexString(0xFF & bytes[i]);
            if (temp.length() <2){
                sb.append(0);
            }
            sb.append(temp);
        }
        return sb.toString();
    }
}
