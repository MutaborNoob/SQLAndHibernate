import org.hibernate.Session;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;


public class Main {
    public static void main(String [] args){
        //first task
        /*Scanner scanner = new Scanner(System.in);

        String pass = scanner.nextLine();
        String url = "jdbc:mysql://localhost:3306/skillbox";
        String user = "root";

        try {
            Connection connection = DriverManager.getConnection(url, user, pass);
            Statement statement = connection.createStatement();

            ResultSet resultSet = statement.executeQuery("SELECT course_name, COUNT(subscription_date)/(MAX(MONTH(subscription_date)) - MIN(MONTH(subscription_date)) + 1)\n" +
                    "AS dif_month\n" +
                    "FROM Purchaselist GROUP BY course_name;");
            while (resultSet.next()){
                String coursName = resultSet.getString("course_name");
                String coursDur = resultSet.getString("dif_month");
                System.out.println(coursName + " " + coursDur);
            }
            resultSet.close();
            statement.close();
            connection.close();
        }catch (Exception ex){
            ex.printStackTrace();
        }*/

        //second task

        /*StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure("hibernate.cfg.xml").build();
        Metadata metadata = new MetadataSources(registry).getMetadataBuilder().build();
        SessionFactory sessionFactory = metadata.getSessionFactoryBuilder().build();

        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        Course course = session.get(Course.class, 1);
        System.out.println("Название курса - " + course.getName() + ". Количество студентов данного курса - " + course.getStudentsCount());
        System.out.println(course.getStudents().size());
        course = session.get(Course.class, 2);
        System.out.println("Название курса - " + course.getName() + ". Количество студентов данного курса - " + course.getStudentsCount());
        System.out.println(course.getStudents().size());


        transaction.commit();
        sessionFactory.close();*/
        Session session = HibernateUtil.getSessionFactory().openSession();
        String hqlPurchase = "FROM " + PurchaseList.class.getSimpleName();
        List<PurchaseList> purchaseList = session.createQuery(hqlPurchase).getResultList();

        session.beginTransaction();
        for(PurchaseList purchase : purchaseList) {

            CriteriaBuilder builder = session.getCriteriaBuilder();//то, что строит запросы
            CriteriaQuery<Course> courseQuery = builder.createQuery(Course.class);//формирование запроса
            Root<Course> courseRoot = courseQuery.from(Course.class);//выбор таблицы, от которой отталкиваемя
            courseQuery.select(courseRoot).where(builder.equal(courseRoot.get("name"), purchase.getCourseName()));
            Course course = session.createQuery(courseQuery).getSingleResult();

            CriteriaQuery<Student> studentQuery = builder.createQuery(Student.class);
            Root<Student> studentRoot = studentQuery.from(Student.class);
            studentQuery.select(studentRoot).where(builder.equal(studentRoot.get("name"), purchase.getStudentName()));
            Student student = session.createQuery(studentQuery).getSingleResult();

            LinkedPurchaseList linkedPurchaseList = new LinkedPurchaseList();
            linkedPurchaseList.setId(new LinkedPurchListKey(course.getId(), student.getId()));
            linkedPurchaseList.setCourseId(course.getId());
            linkedPurchaseList.setStudentId(student.getId());
            session.save(linkedPurchaseList);
        }
        session.getTransaction().commit();
        session.close();

    }
}
