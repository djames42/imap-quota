package com.djames42.models;

public class MyQuota {
    private long usage;
    private long limit;
    private float ratio;

    public MyQuota(long usage, long limit) {
        this.usage = usage;
        this.limit = limit;
        this.ratio = (float) this.usage / (float) this.limit;
    }

    public long getUsage() {
        return usage;
    }
    public long getLimit() {
        return limit;
    }
    public float getRatio() {
        return ratio * 100;
    }
}
