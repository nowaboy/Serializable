import java.util.*;
import java.io.*;

class Student implements Serializable {
    private int id;
    private String name;
    private int age;
    private String major;
    private double gpa;
    
    public Student(int id, String name, int age, String major, double gpa) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.major = major;
        this.gpa = gpa;
    }
    
    public int getId() { return id; }
    public String getName() { return name; }
    public int getAge() { return age; }
    public String getMajor() { return major; }
    public double getGpa() { return gpa; }
    
    public void setName(String name) { this.name = name; }
    public void setAge(int age) { this.age = age; }
    public void setMajor(String major) { this.major = major; }
    public void setGpa(double gpa) { this.gpa = gpa; }
    
    @Override
    public String toString() {
        return String.format("ID: %d | Name: %s | Age: %d | Major: %s | GPA: %.2f", 
            id, name, age, major, gpa);
    }
}

class StudentManager {
    private List<Student> students;
    private String filename;
    
    public StudentManager(String filename) {
        this.filename = filename;
        this.students = loadStudents();
    }
    
    @SuppressWarnings("unchecked")
    private List<Student> loadStudents() {
        try {
            FileInputStream fis = new FileInputStream(filename);
            ObjectInputStream ois = new ObjectInputStream(fis);
            List<Student> loaded = (List<Student>) ois.readObject();
            ois.close();
            return loaded;
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }
    
    private void saveStudents() {
        try {
            FileOutputStream fos = new FileOutputStream(filename);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(students);
            oos.close();
        } catch (IOException e) {
            System.out.println("Error saving: " + e.getMessage());
        }
    }
    
    public void addStudent(String name, int age, String major, double gpa) {
        int newId = students.isEmpty() ? 1 : students.get(students.size() - 1).getId() + 1;
        Student student = new Student(newId, name, age, major, gpa);
        students.add(student);
        saveStudents();
        System.out.println("Student added with ID: " + newId);
    }
    
    public void deleteStudent(int id) {
        students.removeIf(s -> s.getId() == id);
        saveStudents();
        System.out.println("Student deleted");
    }
    
    public void updateStudent(int id, String field, String value) {
        for (Student s : students) {
            if (s.getId() == id) {
                switch (field) {
                    case "name":
                        s.setName(value);
                        break;
                    case "age":
                        s.setAge(Integer.parseInt(value));
                        break;
                    case "major":
                        s.setMajor(value);
                        break;
                    case "gpa":
                        s.setGpa(Double.parseDouble(value));
                        break;
                }
                saveStudents();
                System.out.println("Student updated");
                return;
            }
        }
        System.out.println("Student not found");
    }
    
    public void listAll() {
        if (students.isEmpty()) {
            System.out.println("No students found");
            return;
        }
        for (Student s : students) {
            System.out.println(s);
        }
    }
    
    public void searchByMajor(String major) {
        List<Student> results = new ArrayList<>();
        for (Student s : students) {
            if (s.getMajor().equalsIgnoreCase(major)) {
                results.add(s);
            }
        }
        if (results.isEmpty()) {
            System.out.println("No students found");
        } else {
            for (Student s : results) {
                System.out.println(s);
            }
        }
    }
    
    public void calculateAverageGPA() {
        if (students.isEmpty()) {
            System.out.println("No students");
            return;
        }
        double sum = 0;
        for (Student s : students) {
            sum += s.getGpa();
        }
        System.out.printf("Average GPA: %.2f\n", sum / students.size());
    }
}

public class StudentManagementSystem {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        StudentManager manager = new StudentManager("students.dat");
        
        while (true) {
            System.out.println("\n=== Student Management System ===");
            System.out.println("1. Add Student");
            System.out.println("2. Delete Student");
            System.out.println("3. Update Student");
            System.out.println("4. List All Students");
            System.out.println("5. Search by Major");
            System.out.println("6. Calculate Average GPA");
            System.out.println("7. Exit");
            
            System.out.print("\nEnter choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine();
            
            switch (choice) {
                case 1:
                    System.out.print("Name: ");
                    String name = scanner.nextLine();
                    System.out.print("Age: ");
                    int age = scanner.nextInt();
                    scanner.nextLine();
                    System.out.print("Major: ");
                    String major = scanner.nextLine();
                    System.out.print("GPA: ");
                    double gpa = scanner.nextDouble();
                    manager.addStudent(name, age, major, gpa);
                    break;
                case 2:
                    System.out.print("Student ID: ");
                    int deleteId = scanner.nextInt();
                    manager.deleteStudent(deleteId);
                    break;
                case 3:
                    System.out.print("Student ID: ");
                    int updateId = scanner.nextInt();
                    scanner.nextLine();
                    System.out.print("Field (name/age/major/gpa): ");
                    String field = scanner.nextLine();
                    System.out.print("New value: ");
                    String value = scanner.nextLine();
                    manager.updateStudent(updateId, field, value);
                    break;
                case 4:
                    manager.listAll();
                    break;
                case 5:
                    System.out.print("Major: ");
                    String searchMajor = scanner.nextLine();
                    manager.searchByMajor(searchMajor);
                    break;
                case 6:
                    manager.calculateAverageGPA();
                    break;
                case 7:
                    scanner.close();
                    System.exit(0);
                default:
                    System.out.println("Invalid choice");
            }
        }
    }
}
