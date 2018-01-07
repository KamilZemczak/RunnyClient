package kamilzemczak.runny.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.Date;

/**
 * Created by Kamil Zemczak on 07.01.2018.
 */

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "id",
        "author",
        "type",
        "objective",
        "time"
})

public class Objective {
    @JsonProperty("id")
    private Integer id;
    @JsonProperty("author")
    private User author;
    @JsonProperty("type")
    private String type;
    @JsonProperty("objective")
    private String objective;
    @JsonProperty("time")
    private Date time;
    @JsonIgnore
    private String dateTime;

    public Objective() {

    }

    public Objective(User author, String type, String objective) {
        this.author = author;
        this.setType(type);
        this.setObjective(objective);
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

    @JsonProperty("type")
    public String getType() {
        return type;
    }

    @JsonProperty("type")
    public void setType(String type) {
        this.type = type;
    }

    @JsonProperty("objective")
    public String getObjective() {
        return objective;
    }

    @JsonProperty("objective")
    public void setObjective(String objective) {
        this.objective = objective;
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
