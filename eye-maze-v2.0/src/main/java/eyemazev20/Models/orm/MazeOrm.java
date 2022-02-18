package eyemazev20.Models.orm;

import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "Mazes")
@NoArgsConstructor
public class MazeOrm {
    @Id
    @GeneratedValue
    @Column(name = "maze_id", updatable = false)
    private int id;

    @Column(name = "mzname", unique = true, nullable = false)
    private String name;

    @Column(name = "form")
    private String form;

    @JoinColumn(name = "mkerid", referencedColumnName = "id", updatable = false)
    private Integer mkerId;

    public MazeOrm(String name, String form, Integer mkerId) {
        this.name = name;
        this.form = form;
        this.mkerId = mkerId;
    }

    public String getForm() {
        return form;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
