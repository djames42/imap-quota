package com.djames42.models;

public class MyQuota {
    private int usage;
    private int limit;
    private float ratio;

    public MyQuota(int usage, int limit) {
        this.usage = usage;
        this.limit = limit;
        this.ratio = (float) this.usage / (float) this.limit;
    }

    public int getUsage() {
        return usage;
    }
    public int getLimit() {
        return limit;
    }
    public float getRatio() {
        return ratio * 100;
    }
}
