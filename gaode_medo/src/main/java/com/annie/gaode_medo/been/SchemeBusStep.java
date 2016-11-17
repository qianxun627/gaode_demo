package com.annie.gaode_medo.been;

import com.amap.api.services.route.BusStep;
/**
 * Created by Administrator on 2016/10/25.
 * 这个类大致就是让我们可以判断有没有步行, 公交, 铁路等路径信息, 如果有通过创建SchemeBusStep 对象,把相应的路径信息设置给BusStep保存起来
 * 同时为其打上是否有路径信息的标记
 */
public class SchemeBusStep extends BusStep {
    private boolean isWalk = false;
    private boolean isBus = false;
    private boolean israilway = false;
    private boolean istaxi = false;
    private boolean isStart = false;
    private boolean isEnd = false;

    public SchemeBusStep(BusStep step) {
        if (step != null) {
            this.setBusLine(step.getBusLine());
            this.setWalk(step.getWalk());
            this.setRailway(step.getRailway());
            this.setTaxi(step.getTaxi());
        }
    }

    public boolean isWalk() {
        return isWalk;
    }

    public void setWalk(boolean walk) {
        isWalk = walk;
    }

    public boolean isBus() {
        return isBus;
    }

    public void setBus(boolean bus) {
        isBus = bus;
    }

    public boolean israilway() {
        return israilway;
    }

    public void setRailway(boolean israilway) {
        this.israilway = israilway;
    }

    public boolean istaxi() {
        return istaxi;
    }

    public void setTaxi(boolean istaxi) {
        this.istaxi = istaxi;
    }

    public boolean isStart() {
        return isStart;
    }

    public void setStart(boolean start) {
        isStart = start;
    }

    public boolean isEnd() {
        return isEnd;
    }

    public void setEnd(boolean end) {
        isEnd = end;
    }
}
