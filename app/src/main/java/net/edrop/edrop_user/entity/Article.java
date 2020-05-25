package net.edrop.edrop_user.entity;

import java.util.List;
import java.util.Map;

public class Article {
    private Integer id;//id，不解释
    private String content;//内容描述
    private String contentDetails;//详细内容，就是Markdown文档
    private List<String> imgsPath;//需要加载的图片
    private String datetime;//发布时间
    private boolean praise;//是否进行过点赞
    private List<List<Map<String,String>>> comments;//评论，这个可以先放放
    private String praiseCount;//点赞数量
    private String discussCount;//评论数量

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

    public boolean getPraise() {
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

    public String getPraiseCount() {
        return praiseCount;
    }

    public void setPraiseCount(String praiseCount) {
        this.praiseCount = praiseCount;
    }

    public String getDiscussCount() {
        return discussCount;
    }

    public void setDiscussCount(String discussCount) {
        this.discussCount = discussCount;
    }

    public String getContentDetails() {
        return contentDetails;
    }

    public void setContentDetails(String contentDetails) {
        this.contentDetails = contentDetails;
    }
}
