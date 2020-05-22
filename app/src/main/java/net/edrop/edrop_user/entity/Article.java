package net.edrop.edrop_user.entity;

import java.util.List;
import java.util.Map;

public class Article {
    private Integer id;
    private String content;
    private List<String> imgsPath;
    private String datetime;
    private boolean praise;
    private List<List<Map<String,String>>> comments;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<String> getImgsPath() {
        return imgsPath;
    }

    public void setImgsPath(List<String> imgsPath) {
        this.imgsPath = imgsPath;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public boolean isPraise() {
        return praise;
    }

    public void setPraise(boolean praise) {
        this.praise = praise;
    }

    public List<List<Map<String, String>>> getComments() {
        return comments;
    }

    public void setComments(List<List<Map<String, String>>> comments) {
        this.comments = comments;
    }
}
