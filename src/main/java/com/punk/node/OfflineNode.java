package com.punk.node;

import com.punk.Constants.Constants;
import com.punk.algorim.Config;

public class OfflineNode extends Node {

    public OfflineNode(int id, int[] netDlys, int[] netDlyToClis) {
        super(id, netDlys, netDlyToClis);
        this.type = Constants.OFFLINE;
    }
}
