package inet.entity;

import javax.persistence.*;
import java.util.Date;

/**
 * @author Edward Grippe
 */
@Entity
public class Thread {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String title;

    private String description;

    private Integer userId;  // the creator of the thread

    private Integer categoryId;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "thread_date")
    private Date date;

    public Thread() {}

    public Thread(Integer id, String title, String description, Integer userId, Integer categoryId) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.userId = userId;
        this.categoryId = categoryId;
        this.date = new Date();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Date getDate(){
        return date;
    }

    public void setDate(Date date){
        this.date = date;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }
}