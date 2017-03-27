package inet.entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * @author Edward Grippe
 */
@Entity
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    //TimeStamp generated on create of a post
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "post_date")
    private Date date;

    private String content;

    private Integer threadId;

    private Integer userId;

    public Post() {}

    public Post(Integer id, String content, Integer threadId, Integer userId) {
        this.id = id;
        this.content = content;
        this.threadId = threadId;
        this.userId = userId;
        this.date = new Date();//TODO:: maybe remove and add as parameter in constructor?
    }

    public Date getDate(){
        return date;
    }

    public void setDate(Date date){
        this.date = date;
    }

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

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getThreadId() {
        return threadId;
    }

    public void setThreadId(Integer threadId) {
        this.threadId = threadId;
    }
}