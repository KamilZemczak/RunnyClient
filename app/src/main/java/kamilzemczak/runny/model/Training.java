package kamilzemczak.runny.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Kamil Zemczak on 05.01.2018.
 */

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "id",
        "author",
        "contents",
        "distance",
        "duration",
        "calories",
        "notes",
        "time",
        "tcomments"
})

public class Training {
    @JsonProperty("id")
    private Integer id;
    @JsonProperty("author")
    private User author;
    @JsonProperty("contents")
    private String contents;
    @JsonProperty("distance")
    private Integer distance;
    @JsonProperty("duration")
    private Integer duration;
    @JsonProperty("calories")
    private Integer calories;
    @JsonProperty("notes")
    private String notes;
    @JsonProperty("time")
    private Date time;
    @JsonProperty
    private List<TComment> tcomments = new ArrayList<TComment>();
    @JsonIgnore
    private String dateTime;
    @JsonIgnore
    private String size;

    public Training() {

    }

    public Training(User author, String contents, Integer distance, Integer duration, Integer calories, String notes) {
        this.author = author;
        this.contents = contents;
        this.distance = distance;
        this.duration = duration;
        this.calories = calories;
        this.notes = notes;
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

    @JsonProperty("contents")
    public String getContents() {
        return contents;
    }

    @JsonProperty("contents")
    public void setContents(String contents) {
        this.contents = contents;
    }

    @JsonProperty("distance")
    public Integer getDistance() {
        return distance;
    }

    @JsonProperty("distance")
    public void setDistance(Integer distance) {
        this.distance = distance;
    }

    @JsonProperty("duration")
    public Integer getDuration() {
        return duration;
    }

    @JsonProperty("duration")
    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    @JsonProperty("calories")
    public Integer getCalories() {
        return calories;
    }

    @JsonProperty("calories")
    public void setCalories(Integer calories) {
        this.calories = calories;
    }

    @JsonProperty("notes")
    public String getNotes() {
        return notes;
    }

    @JsonProperty("notes")
    public void setNotes(String notes) {
        this.notes = notes;
    }

    @JsonProperty("time")
    public Date getTime() {
        return time;
    }

    @JsonProperty("time")
    public void setTime(Date time) {
        this.time = time;
    }

    @JsonProperty("tcomments")
    public List<TComment> getTcomments() {
        return tcomments;
    }

    @JsonProperty("tcomments")
    public void setTcomments(List<TComment> tcomments) {
        this.tcomments = tcomments;
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
