package GUI.Gallery.data.entities;

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

    public int getIdEvent() {
        return idEvent;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Sender> getSenders() {
        return senders;
    }

    public void setSenders(List<Sender> senders) {
        this.senders = senders;
    }

}
