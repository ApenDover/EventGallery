package GUI.Gallery.data.connections;

import GUI.Gallery.SetupWindowController;
import GUI.Gallery.data.entity.Company;
import GUI.Gallery.data.entity.Event;
import GUI.Gallery.data.entity.Sender;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AvailableSettings;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.spi.ServiceException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicInteger;

public class BaseConnection {

    private BaseConnection() {
    }

    static Session session;

    public static void openConnection(String user, String password) {
        Properties settings = new Properties();
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
            openSession(settings);
        } catch (ServiceException exception) {
            createDatabase();
            openSession(settings);
        }
    }

    private static void openSession(Properties settings) {
        SessionFactory sessionFactory = new Configuration()
                .setProperties(settings)
                .addAnnotatedClass(Company.class)
                .addAnnotatedClass(Event.class)
                .addAnnotatedClass(Sender.class)
                .buildSessionFactory();
        session = sessionFactory.openSession();
    }

    private static void createDatabase() {
        try {
            String command = "createdb mailsender";
            Process process = Runtime.getRuntime().exec(command);
            process.waitFor();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<Sender> getSender() {
        session.beginTransaction();
        final var result = session.createQuery("FROM Sender s WHERE s.status = :status", Sender.class)
                .setParameter("status", "NEW")
                .getResultList();
        session.getTransaction().commit();
        return result;
    }

    public static List<Event> getEvents() {
        session.beginTransaction();
        final var result = session.createQuery("FROM Event", Event.class).getResultList();
        session.getTransaction().commit();
        return result;
    }

    public static Event getEventById(int id) {
        session.beginTransaction();
        final var result = session.createQuery("FROM Event WHERE id = :id", Event.class).setParameter("id", id).getSingleResult();
        session.getTransaction().commit();
        return result;
    }

    public static void updateSenderStatus(String status, Sender sender) {
        session.beginTransaction();
        session.createQuery("UPDATE Sender SET status = :status WHERE idSender = :sender")
                .setParameter("status", status)
                .setParameter("sender", sender.getIdSender())
                .executeUpdate();
        session.getTransaction().commit();
    }

    public static void setEvent(Date dateEvent, String text, String companyName) {
        Event event = new Event();
        event.setDescription(text);
        java.sql.Date sqlDate = new java.sql.Date(dateEvent.getTime());
        event.setDate(sqlDate);
        ArrayList<Company> findCompany = new ArrayList<>(BaseConnection.getCompany());
        findCompany.stream().filter(company -> company.getName().equals(companyName)).forEach(event::setCompany);
        session.beginTransaction();
        session.save(event);
        session.getTransaction().commit();
    }

    public static boolean setSender(String mail, String path, Event event) {
        Sender sender = new Sender();
        sender.setPath(path);
        sender.setMail(mail);
        sender.setEvent(event);
        sender.setStatus("NEW");
        session.beginTransaction();
        session.save(sender);
        session.getTransaction().commit();
        return true;
    }

    public static List<Event> getEventsFromCompany(String companyName) {
        AtomicInteger companyId = new AtomicInteger();
        ArrayList<Company> companies = new ArrayList<>(getCompany());
        companies.stream().filter(company -> company.getName().equals(companyName)).forEach(company -> companyId.set(company.getIdCompany()));
        session.beginTransaction();
        final var result = session.createQuery("FROM Event e WHERE e.company.idCompany = :companyId", Event.class)
                .setParameter("companyId", companyId.get())
                .getResultList();
        session.getTransaction().commit();
        return result;
    }

    public static List<Company> getCompany() {
        session.beginTransaction();
        final var result = session.createQuery("FROM Company", Company.class).getResultList();
        session.getTransaction().commit();
        return result;
    }

    public static void setCompany(String name) {
        Company company = new Company();
        company.setName(name);
        session.beginTransaction();
        session.save(company);
        session.getTransaction().commit();
    }

    public static void removeCompany(String name) {
        session.beginTransaction();
        session.createQuery("DELETE FROM Company WHERE name = :name").setParameter("name", name).executeUpdate();
        session.getTransaction().commit();
    }

    public static List<Sender> getMails() {
        session.beginTransaction();
        final var result = session.createQuery("FROM Sender s WHERE s.event.idEvent = :eventId", Sender.class)
                .setParameter("eventId", SetupWindowController.getIdEvent())
                .getResultList();
        session.getTransaction().commit();
        return result;
    }

    public static void closeConnection() {
        session.close();
    }

}
