package GUI.Gallery.data.connections;

import GUI.Gallery.data.entities.Company;
import GUI.Gallery.data.entities.Event;
import GUI.Gallery.data.entities.Sender;
import GUI.Gallery.SetupWindowController;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.AvailableSettings;
import org.hibernate.cfg.Configuration;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicInteger;

public class BaseConnection {

    private BaseConnection() {
    }

    static Session session;

    public static void addConnection(String user, String password) {

        Properties settings = new Properties();
        settings.put(AvailableSettings.DRIVER, "org.postgresql.Driver");
        settings.put(AvailableSettings.URL, "jdbc:postgresql://localhost:5432/mailsender?createDatabaseIfNotExist=true");
        settings.put(AvailableSettings.USER, user);
        settings.put(AvailableSettings.PASS, password);
        settings.put(AvailableSettings.DIALECT, "org.hibernate.dialect.PostgreSQLDialect");
        settings.put(AvailableSettings.POOL_SIZE, "10");
        settings.put(AvailableSettings.SHOW_SQL, "true");
        settings.put(AvailableSettings.CURRENT_SESSION_CONTEXT_CLASS, "thread");
        settings.put(AvailableSettings.HBM2DDL_AUTO, "update");

        SessionFactory sessionFactory = new Configuration()
                .setProperties(settings)
                .addAnnotatedClass(Company.class)
                .addAnnotatedClass(Event.class)
                .addAnnotatedClass(Sender.class)
                .buildSessionFactory();
        session = sessionFactory.openSession();
    }

    public static List<Sender> getSender() {
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Sender> query = builder.createQuery(Sender.class);
        Root<Sender> rootQuery = query.from(Sender.class);
        query.select(rootQuery).where(builder.equal(rootQuery.get("status"), "NEW"));
        return session.createQuery(query).getResultList();
    }

    public static List<Event> getEvents() {
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Event> query = builder.createQuery(Event.class);
        Root<Event> rootQuery = query.from(Event.class);
        query.select(rootQuery);
        return session.createQuery(query).getResultList();
    }

    public static void updateSenderStatus(String status, Sender sender) {
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaUpdate<Sender> criteriaUpdate = builder.createCriteriaUpdate(Sender.class);
        Root<Sender> rootUpdate = criteriaUpdate.from(Sender.class);
        criteriaUpdate.set("status", status);
        criteriaUpdate.where(builder.equal(rootUpdate.get("idSender"), sender.getIdSender()));
        Transaction transaction = session.beginTransaction();
        session.createQuery(criteriaUpdate).executeUpdate();
        transaction.commit();
    }

    public static void setEvent(Date dateEvent, String text, String companyName) {
        Event event = new Event();
        event.setDescription(text);
        java.sql.Date sqlDate = new java.sql.Date(dateEvent.getTime());
        event.setDate(sqlDate);
        ArrayList<Company> findCompany = new ArrayList<>(BaseConnection.getCompany());
        findCompany.stream().filter(company -> company.getName().equals(companyName)).forEach(event::setCompany);
        session.save(event);
    }

    public static boolean setSender(String mail, String path, Event event) {
        Sender sender = new Sender();
        sender.setPath(path);
        sender.setMail(mail);
        sender.setEvent(event);
        sender.setStatus("NEW");
        session.save(sender);
        return true;
    }

    public static List<Event> getEventsFromCompany(String companyName) {
        AtomicInteger companyId = new AtomicInteger();
        ArrayList<Company> companies = new ArrayList<>(getCompany());
        companies.stream().filter(company -> company.getName().equals(companyName)).forEach(company -> companyId.set(company.getIdCompany()));
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Event> query = builder.createQuery(Event.class);
        Root<Event> rootQuery = query.from(Event.class);
        query.select(rootQuery).where(builder.equal(rootQuery.get("company").get("idCompany"), companyId.get()));
        return session.createQuery(query).getResultList();
    }

    public static List<Company> getCompany() {
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Company> query = builder.createQuery(Company.class);
        Root<Company> rootQuery = query.from(Company.class);
        query.select(rootQuery);
        return session.createQuery(query).getResultList();
    }

    public static void setCompany(String name) {
        Company company = new Company();
        company.setName(name);
        Transaction transaction = session.beginTransaction();
        session.save(company);
        transaction.commit();
    }

    public static void removeCompany(String name) {
        List<Company> companyList = getCompany();
        companyList.forEach(company -> {
            if (company.getName().equals(name)) {
                Transaction transaction = session.beginTransaction();
                session.remove(company);
                transaction.commit();
            }
        });
    }

    public static List<Sender> getMails() {
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Sender> query = builder.createQuery(Sender.class);
        Root<Sender> rootQuery = query.from(Sender.class);
        query.select(rootQuery).where(builder.equal(rootQuery.get("event").get("idEvent"), SetupWindowController.IdEvent));
        return session.createQuery(query).getResultList();
    }

    public static void closeConnection() {
        session.close();
    }

}
