package GUI.Gallery.data.dao;

import GUI.Gallery.SetupWindowController;
import GUI.Gallery.data.entity.Company;
import GUI.Gallery.data.entity.Event;
import GUI.Gallery.data.entity.Sender;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AvailableSettings;
import org.hibernate.cfg.Configuration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicInteger;

public class BaseDAO {

    private static BaseDAO instance;

    private BaseDAO() {
    }

    public static BaseDAO getInstance() {
        if (Objects.isNull(instance)) {
            instance = new BaseDAO();
        }
        return instance;
    }

    private Session session;

    public String openConnection(String user, String password) {
        var result = "ERROR";
        final var settings = new Properties();
        settings.put(AvailableSettings.DRIVER, "org.postgresql.Driver");
        settings.put(AvailableSettings.URL, "jdbc:postgresql://localhost:5432/mailsender");
        settings.put(AvailableSettings.USER, user);
        settings.put(AvailableSettings.PASS, password);
        settings.put(AvailableSettings.DIALECT, "org.hibernate.dialect.PostgreSQLDialect");
        settings.put(AvailableSettings.HBM2DDL_AUTO, "update");
        settings.put(AvailableSettings.POOL_SIZE, "10");
        settings.put(AvailableSettings.SHOW_SQL, "true");
        settings.put(AvailableSettings.CURRENT_SESSION_CONTEXT_CLASS, "thread");
        try {
            session = openSession(settings);
        } catch (Exception e) {
            createDatabase();
            session = openSession(settings);
        } finally {
            if (session.isOpen()) {
                result = "SUCCESS";
            }
        }
        return result;
    }

    private Session openSession(Properties settings) {
        final var sessionFactory = new Configuration()
                .setProperties(settings)
                .addAnnotatedClass(Company.class)
                .addAnnotatedClass(Event.class)
                .addAnnotatedClass(Sender.class)
                .buildSessionFactory();
        return sessionFactory.openSession();
    }

    private void createDatabase() {
        try {
            final var command = "createdb mailsender";
            final var process = Runtime.getRuntime().exec(command);
            process.waitFor();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Sender> getSender() {
        session.beginTransaction();
        final var result = session.createQuery("FROM Sender s WHERE s.status = :status", Sender.class)
                .setParameter("status", "NEW")
                .getResultList();
        session.getTransaction().commit();
        return result;
    }

    public List<Event> getEvents() {
        session.beginTransaction();
        final var result = session.createQuery("FROM Event", Event.class).getResultList();
        session.getTransaction().commit();
        return result;
    }

    public Event getEventById(int id) {
        session.beginTransaction();
        final var result = session.createQuery("FROM Event WHERE id = :id", Event.class).setParameter("id", id).getSingleResult();
        session.getTransaction().commit();
        return result;
    }

    public void updateSenderStatus(String status, Sender sender) {
        session.beginTransaction();
        session.createQuery("UPDATE Sender SET status = :status WHERE idSender = :sender")
                .setParameter("status", status)
                .setParameter("sender", sender.getIdSender())
                .executeUpdate();
        session.getTransaction().commit();
    }

    public void setEvent(Date dateEvent, String text, String companyName) {
        final var event = new Event();
        event.setDescription(text);
        final var sqlDate = new java.sql.Date(dateEvent.getTime());
        event.setDate(sqlDate);
        final var findCompany = new ArrayList<>(BaseDAO.getInstance().getCompany());
        findCompany.stream().filter(company -> company.getName().equals(companyName)).forEach(event::setCompany);
        session.beginTransaction();
        session.save(event);
        session.getTransaction().commit();
    }

    public boolean setSender(String mail, String path, Event event) {
        final var sender = new Sender();
        sender.setPath(path);
        sender.setMail(mail);
        sender.setEvent(event);
        sender.setStatus("NEW");
        session.beginTransaction();
        session.save(sender);
        session.getTransaction().commit();
        return true;
    }

    public List<Event> getEventsFromCompany(String companyName) {
        final var companyId = new AtomicInteger();
        final var companies = new ArrayList<>(getCompany());
        companies.stream().filter(company -> company.getName().equals(companyName)).forEach(company -> companyId.set(company.getIdCompany()));
        session.beginTransaction();
        final var result = session.createQuery("FROM Event e WHERE e.company.idCompany = :companyId", Event.class)
                .setParameter("companyId", companyId.get())
                .getResultList();
        session.getTransaction().commit();
        return result;
    }

    public List<Company> getCompany() {
        session.beginTransaction();
        final var result = session.createQuery("FROM Company", Company.class).getResultList();
        session.getTransaction().commit();
        return result;
    }

    public void setCompany(String name) {
        final var company = new Company();
        company.setName(name);
        session.beginTransaction();
        session.save(company);
        session.getTransaction().commit();
    }

    public void removeCompany(String name) {
        session.beginTransaction();
        session.createQuery("DELETE FROM Company WHERE name = :name").setParameter("name", name).executeUpdate();
        session.getTransaction().commit();
    }

    public List<Sender> getMails() {
        session.beginTransaction();
        final var result = session.createQuery("FROM Sender s WHERE s.event.idEvent = :eventId", Sender.class)
                .setParameter("eventId", SetupWindowController.getIdEvent())
                .getResultList();
        session.getTransaction().commit();
        return result;
    }

    public void closeConnection() {
        session.close();
    }

}
