package model;

import java.math.BigDecimal;

/**
 * Created by Jingwu Xu on 2019-04-11
 */
public class Location {
    public String name;
    public BigDecimal lat;
    public BigDecimal lng;

    public Location(String name, BigDecimal lat, BigDecimal lng) {
        this.name = name;
        this.lat = lat;
        this.lng = lng;
    }

    public Location() {

    }
}
