package com.jimmy.development.http.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by jinguochong on 2017/8/23.
 */

public class ImageListBean {

    @SerializedName("_id")
    @Expose
    public String id;
    @SerializedName("createdAt")
    @Expose
    public String createdAt;
    @SerializedName("desc")
    @Expose
    public String desc;
    @SerializedName("publishedAt")
    @Expose
    public String publishedAt;
    @SerializedName("source")
    @Expose
    public String source;
    @SerializedName("type")
    @Expose
    public String type;
    @SerializedName("url")
    @Expose
    public String url;
    @SerializedName("used")
    @Expose
    public Boolean used;
    @SerializedName("who")
    @Expose
    public String who;
    @SerializedName("images")
    @Expose
    public List<String> images = null;

    @Override
    public String toString() {
        return "ImageListBean{" +
                "id='" + id + '\'' +
                ", createdAt='" + createdAt + '\'' +
                ", desc='" + desc + '\'' +
                ", publishedAt='" + publishedAt + '\'' +
                ", source='" + source + '\'' +
                ", type='" + type + '\'' +
                ", url='" + url + '\'' +
                ", used=" + used +
                ", who='" + who + '\'' +
                ", images=" + images +
                '}';
    }
}
