package inet.entity;

import javax.persistence.*;

/**
 * @author Edward Grippe
 */
@Entity
@Table(name = "ROLE")
public class Role {
    @Id
    @Column(name = "ROLE_ID", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name = "ROLE_NAME", nullable = false)
    private String name;

    public Role() {}

    public Role(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}