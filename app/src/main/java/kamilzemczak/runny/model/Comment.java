package kamilzemczak.runny.model;

/**
 * Created by Kamil Zemczak on 03.01.2018.
 */

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.Date;

/**
 * Created by Kamil Zemczak on 02.01.2018.
 */


@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "id",
        "author",
        "post",
        "contents",
        "time",
})

public class Comment {
    @JsonProperty("id")
    private Integer id;
    @JsonProperty("author")
    private User author;
    @JsonProperty
    private Post post;
    @JsonProperty("contents")
    private String contents;
    @JsonProperty("time")
    private Date time;
    @JsonIgnore
    private String dateTime;

    public Comment() {

    }

    public Comment(User author, Post post, String contents) {
        this.author = author;
        this.post = post;
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

    @JsonProperty("post")
    public Post getPost() {
        return post;
    }

    @JsonProperty("post")
    public void setPost(Post post) {
        this.post = post;
    }

    @JsonProperty("contents")
    public String getContents() {
        return contents;
    }

    @JsonProperty("contents")
    public void setContents(String contents) {
        this.contents = contents;
    }

    @JsonProperty("time")
    public Date getTime() {
        return time;
    }

    @JsonProperty("time")
    public void setTime(Date time) {
        this.time = time;
    }

    @JsonIgnore
    public String getDateTime() {
        return dateTime;
    }

    @JsonIgnore
    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }
}

