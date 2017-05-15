package me.xaanit.apparatus.objects.enums;

/**
 * Created by Jacob on 5/14/2017.
 */
public enum CColors {

    ERROR("#e50000"),
    BASIC("#249999");

    private String hex;

    CColors(String hex) {
        this.hex = hex;
    }

    public String getHex() {
        return hex;
    }
}
