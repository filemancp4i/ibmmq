package com.ibm.client.engineering;

import com.ibm.client.engineering.mq.JmsPubSub;
import com.ibm.client.engineering.mq.JmsPutGet;

public class HelloWorld {

    public static void main(String[] p) {
        JmsPutGet.startPut();
        // JmsPutGet.startGet();
        // JmsPubSub.startPub();
        // JmsPubSub.startSub();
    }
}
