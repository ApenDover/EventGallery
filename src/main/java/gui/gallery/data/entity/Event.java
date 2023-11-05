package gui.gallery.data.entity;

import lombok.Data;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_event", nullable = false)
    private int idEvent;

    @ManyToOne(cascade = CascadeType.ALL)
    private Company company;

    @Column(name = "date", nullable = false)
    private Date date;

    private String description;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "event")
    private List<Sender> senders = new ArrayList<>();

}
