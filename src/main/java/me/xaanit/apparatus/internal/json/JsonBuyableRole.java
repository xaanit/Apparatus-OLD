package me.xaanit.apparatus.internal.json;

import me.xaanit.apparatus.enums.TatsuType;

/**
 * Created by Jacob on 5/19/2017.
 */
public class JsonBuyableRole {
    private long id = 0;
    private long price = 0;
    private TatsuType type = TatsuType.UNKNOWN;

    public JsonBuyableRole(){}

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public TatsuType getType() {
        return type;
    }

    public void setType(TatsuType type) {
        this.type = type;
    }
}
