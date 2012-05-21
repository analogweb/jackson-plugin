package org.analogweb.jackson;

import java.util.Date;

public class Bean {

    private String name;
    private boolean alive;
    private Date date;
    public Bean() {
        super();
    }
    public Bean(String name, boolean alive, Date date) {
        super();
        this.name = name;
        this.alive = alive;
        this.date = date;
    }
    public String getName() {
        return name;
    }
    public boolean isAlive() {
        return alive;
    }
    public Date getDate() {
        return date;
    }

}
