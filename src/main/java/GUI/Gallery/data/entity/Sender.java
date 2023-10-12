package GUI.Gallery.data.entity;

import lombok.Data;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
@Data
public class Sender {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_sender", nullable = false)

    private int idSender;
    @Column(name = "mail", nullable = false)

    private String mail;
    @Column(name = "path", nullable = false)

    private String path;
    @Column(name = "status")

    private String status;
    @ManyToOne(cascade = CascadeType.ALL)

    private Event event;

}
