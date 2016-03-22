package com.aack.meinv.response;

import java.io.Serializable;

public class VideoModel implements Serializable{
private String movieid;

private String title;

private String url;

private String poster;

private String play_url;

private String duration;

private String thumbup_count;

public void setMovieid(String movieid){
this.movieid = movieid;
}
public String getMovieid(){
return this.movieid;
}
public void setTitle(String title){
this.title = title;
}
public String getTitle(){
return this.title;
}
public void setUrl(String url){
this.url = url;
}
public String getUrl(){
return this.url;
}
public void setPoster(String poster){
this.poster = poster;
}
public String getPoster(){
return this.poster;
}
public void setPlay_url(String play_url){
this.play_url = play_url;
}
public String getPlay_url(){
return this.play_url;
}
public void setDuration(String duration){
this.duration = duration;
}
public String getDuration(){
return this.duration;
}
public void setThumbup_count(String thumbup_count){
this.thumbup_count = thumbup_count;
}
public String getThumbup_count(){
return this.thumbup_count;
}

}