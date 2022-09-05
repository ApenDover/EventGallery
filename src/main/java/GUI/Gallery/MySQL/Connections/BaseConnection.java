package GUI.Gallery.MySQL.Connections;

import GUI.Gallery.MySQL.Entities.Company;
import GUI.Gallery.MySQL.Entities.Event;
import GUI.Gallery.MySQL.Entities.Sender;
import GUI.Gallery.SetupWindowController;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.criteria.Root;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class BaseConnection {
    static Session session;

    public static void addConnection(String user, String password)
    {

//        StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure("hibernate.cfg.xml").build();
//        Metadata metadata = new MetadataSources(registry).getMetadataBuilder().build();
//        SessionFactory sessionFactory = metadata.getSessionFactoryBuilder().build();
//        session = sessionFactory.openSession();

        Properties settings = new Properties();
        settings.put(Environment.DRIVER, "com.mysql.cj.jdbc.Driver");
        settings.put(Environment.URL, "jdbc:mysql://localhost:3306/MailSender?createDatabaseIfNotExist=true");
        settings.put(Environment.USER, user);
        settings.put(Environment.PASS, password);
        settings.put(Environment.DIALECT, "org.hibernate.dialect.MySQL8Dialect");
        settings.put(Environment.POOL_SIZE, "10");
        settings.put(Environment.SHOW_SQL, "true");
        settings.put(Environment.CURRENT_SESSION_CONTEXT_CLASS, "thread");
        settings.put(Environment.HBM2DDL_AUTO, "update");
        SessionFactory sessionFactory = new Configuration()
                .setProperties(settings)
                .addAnnotatedClass(Company.class)
                .addAnnotatedClass(Event.class)
                .addAnnotatedClass(Sender.class)
                .buildSessionFactory();
        session = sessionFactory.openSession();
    }

    public static List<Sender> getSender()
    {
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Sender> query = builder.createQuery(Sender.class);
        Root<Sender> rootQuery = query.from(Sender.class);
        query.select(rootQuery).where(builder.equal(rootQuery.get("status"), "NEW"));
        List<Sender> senderList = session.createQuery(query).getResultList();
        return senderList;
    }

    public static List<Event> getEvents()
    {
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Event> query = builder.createQuery(Event.class);
        Root<Event> rootQuery = query.from(Event.class);
        query.select(rootQuery);
        List<Event> eventList = session.createQuery(query).getResultList();
        return eventList;
    }

    public static void updateSenderStatus(String status, Sender sender)
    {
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaUpdate<Sender> criteriaUpdate = builder.createCriteriaUpdate(Sender.class);
        Root<Sender> rootUpdate = criteriaUpdate.from(Sender.class);
        criteriaUpdate.set("status", status);
        criteriaUpdate.where(builder.equal(rootUpdate.get("idSender"), sender.getIdSender()));
        Transaction transaction = session.beginTransaction();
        session.createQuery(criteriaUpdate).executeUpdate();
        transaction.commit();
    }

    public static void setEvent(Date dateEvent, String text, String companyName)
    {
            Event event = new Event();
            event.setDescription(text);
            Date date = dateEvent;
            java.sql.Date sqlDate = new java.sql.Date(date.getTime());
            event.setDate(sqlDate);
            ArrayList<Company> findCompany = new ArrayList<>(BaseConnection.getCompany());
            findCompany.forEach(c -> {
                if (c.getName().equals(companyName))
                {
                    event.setCompany(c);
                }
            });
            Transaction transaction = session.beginTransaction();
            session.save(event);
            transaction.commit();
    }

    public static boolean setSender(String mail, String path, Event event)
    {
        Sender sender = new Sender();
        sender.setPath(path);
        sender.setMail(mail);
        sender.setEvent(event);
        sender.setStatus("NEW");
        Transaction transaction = session.beginTransaction();
        session.save(sender);
        transaction.commit();
        return true;
    }

    public static List<Event> getEventsFromCompany(String companyName)
    {
        AtomicInteger companyId = new AtomicInteger();
        ArrayList<Company> companies = new ArrayList<>(getCompany());
        companies.forEach(company -> {
            if (company.getName().equals(companyName)){
                companyId.set(company.getIdCompany());
            }
        });
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Event> query = builder.createQuery(Event.class);
        Root<Event> rootQuery = query.from(Event.class);
        query.select(rootQuery).where(builder.equal(rootQuery.get("company").get("idCompany"), companyId.get()));
        List<Event> eventList = session.createQuery(query).getResultList();
        return eventList;
    }

    public static List<Company> getCompany()
    {
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Company> query = builder.createQuery(Company.class);
        Root<Company> rootQuery = query.from(Company.class);
        query.select(rootQuery);
        List<Company> companyList = session.createQuery(query).getResultList();
        return companyList;
    }

    public static boolean setCompany(String name)
    {
        Company company = new Company();
        company.setName(name);
        Transaction transaction = session.beginTransaction();
        session.save(company);
        transaction.commit();
        return true;
    }

    public static boolean removeCompany(String name)
    {
        List<Company> companyList = getCompany();
        companyList.forEach(company -> {
            if (company.getName().equals(name))
            {
                Transaction transaction = session.beginTransaction();
                session.remove(company);
                transaction.commit();
            }
        });
        return true;
    }

    public static List<Sender> getMails()
    {
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Sender> query = builder.createQuery(Sender.class);
        Root<Sender> rootQuery = query.from(Sender.class);
        query.select(rootQuery).where(builder.equal(rootQuery.get("event").get("idEvent"), SetupWindowController.IdEvent));
        List<Sender> senderList = session.createQuery(query).getResultList();
        return senderList;
    }

    public static void closeConnection()
    {
        session.close();
    }

}
