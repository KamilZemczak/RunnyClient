package kamilzemczak.runny.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Kamil Zemczak on 02.01.2018.
 */


@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "id",
        "author",
        "time",
        "contents",
        "comments"
})

public class Post {
    @JsonProperty("id")
    private Integer id;
    @JsonProperty("author")
    private User author;
    @JsonProperty("time")
    private Date time;
    @JsonProperty("contents")
    private String contents;
    @JsonProperty
    private List<Comment> comments = new ArrayList<Comment>();
    @JsonIgnore
    private String dateTime;
    @JsonIgnore
    private String size;

    public Post() {

    }

    public Post(User author, User recipient, String contents) {
        this.author = author;
        this.contents = contents;
        this.time = new Date();
    }

    @JsonProperty("id")
    public Integer getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(Integer id) {
        this.id = id;
    }

    @JsonProperty("author")
    public User getAuthor() {
        return author;
    }

    @JsonProperty("author")
    public void setAuthor(User author) {
        this.author = author;
    }

    @JsonProperty("time")
    public Date getTime() {
        return time;
    }

    @JsonProperty("time")
    public void setTime(Date time) {
        this.time = time;
    }

    @JsonProperty("contents")
    public String getContents() {
        return contents;
    }

    @JsonProperty("contents")
    public void setContents(String contents) {
        this.contents = contents;
    }

    @JsonProperty("comments")
    public List<Comment> getComments() {
        return comments;
    }

    @JsonProperty("comments")
    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    @JsonIgnore
    public String getDateTime() {
        return dateTime;
    }

    @JsonIgnore
    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    @JsonIgnore
    public String getSize() {
        return size;
    }

    @JsonIgnore
    public void setSize(String size) {
        this.size = size;
    }
}

