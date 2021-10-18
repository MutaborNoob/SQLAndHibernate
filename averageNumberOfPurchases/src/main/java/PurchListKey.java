import javax.persistence.Column;
import java.io.Serializable;
import java.util.Objects;

public class PurchListKey implements Serializable {

    @Column(name = "student_name")
    private String studentName;

    @Column(name = "course_name")
    private String courseName;

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    @Override
    public int hashCode() {
        return Objects.hash(studentName, courseName);
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj)
            return true;
        if(obj == null || getClass() != obj.getClass())
            return false;
        PurchListKey that = (PurchListKey) obj;

        return Objects.equals(studentName, that.studentName) && Objects.equals(courseName, that.courseName);
    }
}
