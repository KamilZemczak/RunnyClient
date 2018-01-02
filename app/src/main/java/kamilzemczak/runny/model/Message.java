package kamilzemczak.runny.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.Date;


@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "id",
        "author",
        "recipient",
        "time",
        "contents"
})

public class Message {
    @JsonProperty("id")
    private Integer id;
    @JsonProperty("author")
    private User author;
    @JsonProperty("recipient")
    private User recipient;
    @JsonProperty("time")
    private Date time;
    @JsonProperty("contents")
    private String contents;
    @JsonIgnore
    private boolean isMe;
    @JsonIgnore
    private String dateTime;

    public Message() {

    }

    public Message(User author, User recipient, String contents) {
        this.author = author;
        this.recipient = recipient;
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

    @JsonProperty("recipient")
    public User getRecipient() {
        return recipient;
    }

    @JsonProperty("recipient")
    public void setRecipient(User recipient) {
        this.recipient = recipient;
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

    @JsonIgnore
    public boolean isMe() {
        return isMe;
    }

    @JsonIgnore
    public void setMe(boolean me) {
        isMe = me;
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
