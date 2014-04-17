package com.pi4j.component.dht;

/**
 * Created by azeez on 4/17/14.
 */
public enum DHTSensorType {

    DHT11(11),DHT21(21), DHT22(22);

    private int type;

    DHTSensorType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }
}
