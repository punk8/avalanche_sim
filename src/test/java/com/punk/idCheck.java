package com.punk;

import com.punk.Constants.Constants;

import java.util.Random;

import static com.punk.network.Network.checkID;

public class idCheck {
    public static void main(String[] args){
        for(int i = 0;i<1000;i++){
            Random random = new Random(999);
            int id = random.nextInt(Constants.N);

            checkID(id,i);
        }

    }
}
