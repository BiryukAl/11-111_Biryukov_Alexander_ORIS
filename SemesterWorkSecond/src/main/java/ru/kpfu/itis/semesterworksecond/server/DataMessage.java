package ru.kpfu.itis.semesterworksecond.server;

import java.io.Serializable;

public class DataMessage implements Serializable {
    private static final long serialVersionUID = 1L;


    // Head
    public final int head;

    // Body
    public final Integer bodyPartX;
    public final Integer bodyPartY;


    public DataMessage(int head, Integer bodyPartX, Integer bodyPartY) {
        this.head = head;
        this.bodyPartX = bodyPartX;
        this.bodyPartY = bodyPartY;
    }

    public DataMessage(int head) {
        this(head, null, null);
    }

    public int getHead() {
        return head;
    }

    public Integer getBodyPartX() {
        return bodyPartX;
    }

    public Integer getBodyPartY() {
        return bodyPartY;
    }
}
