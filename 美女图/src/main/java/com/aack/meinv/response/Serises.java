package com.aack.meinv.response;
import java.util.List;
public class Serises {

private boolean is_new;

private String show_videostage;

private String title;//标题

private String show_videoseq;

private String videoid;

private int tag_type;

    private String name;

    int order;

    private String url;

public void setIs_new(boolean is_new){
this.is_new = is_new;
}
public boolean getIs_new(){
return this.is_new;
}
public void setShow_videostage(String show_videostage){
this.show_videostage = show_videostage;
}
public String getShow_videostage(){
return this.show_videostage;
}
public void setTitle(String title){
this.title = title;
}
public String getTitle(){
return this.title;
}
public void setShow_videoseq(String show_videoseq){
this.show_videoseq = show_videoseq;
}
public String getShow_videoseq(){
return this.show_videoseq;
}
public void setVideoid(String videoid){
this.videoid = videoid;
}
public String getVideoid(){
return this.videoid;
}
public void setTag_type(int tag_type){
this.tag_type = tag_type;
}
public int getTag_type(){
return this.tag_type;
}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}