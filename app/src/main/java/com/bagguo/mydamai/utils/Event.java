package com.bagguo.mydamai.utils;

import java.security.PublicKey;

public class Event {
    public static final int LIST_TOPIC_OK = 1;
    public static final int LIST_TOPIC_ERROR = 2;

    private int type;
    private Object data;

    public Event(int type, Object data) {
        this.type = type;
        this.data = data;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
