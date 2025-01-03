package org.example;

import java.sql.*;
import java.util.Scanner;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/elearning?useSSL=false&serverTimezone=UTC";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "mehak@1310";

    public static Connection conn;
    public static Scanner scn = new Scanner(System.in);

    public static void main(String args[]){

        try {
            conn = DriverManager.getConnection(DB_URL , DB_USER , DB_PASSWORD);
            System.out.println("Connected to Database");

            while (true){
                System.out.println("\nE-Learning Platform");
                System.out.println("1. Login");
                System.out.println("2. Register");
                System.out.println("3. Exit");
                System.out.print("Choose an option: ");
                int choice = scn.nextInt();
                scn.nextLine();

                switch (choice){
                    case 1 -> login();
                    case 2 -> register();
                    case 3 -> {
                        System.out.println("Exiting...");
                        conn.close();
                        return;
                    }
                    default -> System.out.println("Invalid option. Try again.");
                }
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }


        private static void login() throws SQLException{
            System.out.println("Enter Username: ");
            String username = scn.nextLine();
            System.out.println("Enter Password: ");
            String password = scn.nextLine();

            String query = "SELECT * FROM USERS WHERE USERNAME = ? AND PASSWORD = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1 , username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()){
                String role = rs.getString("ROLE");
                System.out.println("Login successful! Role: " + role);
                if (role.equals("ADMIN")){
                    adminMenu();
                }
                else {
                    studentMenu();
                }
            }
            else {
                System.out.println("Invalid username or password");
            }
        }
        private static void register() throws SQLException {
            System.out.print("Enter username: ");
            String username = scn.nextLine();
            System.out.print("Enter password: ");
            String password = scn.nextLine();
            System.out.print("Enter role (Student/Admin): ");
            String role = scn.nextLine();

            String query = "INSERT INTO Users (username, password, role) VALUES (?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.setString(3, role);
            stmt.executeUpdate();

            System.out.println("Registration successful!");
        }
        private static void adminMenu() throws SQLException{
            while (true){
                System.out.println("\nAdmin Menu");
                System.out.println("1. Add Course");
                System.out.println("2. Add Lesson");
                System.out.println("3. Logout");
                System.out.print("Choose an option: ");
                int choice = scn.nextInt();
                scn.nextLine();

                switch (choice){
                    case 1 -> addCourse();
                    case 2 -> addLesson();
                    case 3 -> {
                        System.out.println("Logging out...");
                        return;
                    }
                    default -> System.out.println("Invalid option. Try again.");
                }
            }
        }
        private static void addCourse() throws SQLException {
            System.out.println("Enter course name: ");
            String courseName = scn.nextLine();
            System.out.println("Enter course description: ");
            String description = scn.nextLine();
            System.out.println("Enter instructor name: ");
            String instructor = scn.nextLine();

            String query = "INSERT INTO COURSES (COURSE_NAME , DESCRIPTION , INSTRUCTOR) VALUES (?,?,?)";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1,courseName);
            stmt.setString(2,description);
            stmt.setString(3,instructor);
            stmt.executeUpdate();

            System.out.println("Course added successfully !");

        }

        private static void addLesson() throws SQLException{
            System.out.println("Enter course ID: ");
            int courseID = scn.nextInt();
            scn.nextLine();
            System.out.println("Enter lesson title: ");
            String lessonTitle = scn.nextLine();
            System.out.println("Enter lesson content: ");
            String content = scn.nextLine();

            String query = "INSERT INTO LESSONS (COURSE_ID , LESSON_TITLE , CONTENT) VALUES (?,?,?)";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1 ,courseID);
            stmt.setString(2, lessonTitle);
            stmt.setString(3,content);
            stmt.executeUpdate();

            System.out.println("Lesson added successfully !");

        }
        private static void studentMenu() throws SQLException{
            while (true){
                System.out.println("\nStudent Menu");
                System.out.println("1. View Courses");
                System.out.println("2. View Lessons");
                System.out.println("3. Logout");
                System.out.println("4. View Course Progress");
                System.out.println("5. Logout");
                System.out.print("Choose an option: ");
                int choice = scn.nextInt();
                scn.nextLine();

                switch (choice) {
                    case 1 -> viewCourses();
                    case 2 -> viewLessons();
                    case 3 -> markLessonCompleted();
                    case 4 -> viewProgress();
                    case 5 -> {
                        System.out.println("Logging out...");
                        return;
                    }
                    default -> System.out.println("Invalid option. Try again.");
                }
            }
        }

        private static void viewCourses() throws SQLException {
            String query = "SELECT * FROM COURSES";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            System.out.println("\nAvailable Courses:");
            while (rs.next()) {
                System.out.printf("ID: %d, Name: %s, Instructor: %s\n", rs.getInt("courseID"), rs.getString("courseName"), rs.getString("instructor"));
            }
        }
        private static void viewLessons() throws SQLException {
            System.out.print("Enter course ID: ");
            int courseID = scn.nextInt();
            scn.nextLine(); // Consume newline

            String query = "SELECT * FROM Lessons WHERE courseID = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, courseID);
            ResultSet rs = stmt.executeQuery();

            System.out.println("\nLessons:");
            while (rs.next()) {
                System.out.printf("ID: %d, Title: %s\n", rs.getInt("lessonID"), rs.getString("lessonTitle"));
            }
        }

        private static void markLessonCompleted() throws SQLException {
            System.out.print("Enter your User ID: ");
            int userID = scn.nextInt();
            System.out.print("Enter Course ID: ");
            int courseID = scn.nextInt();
            System.out.print("Enter Lesson ID: ");
            int lessonID = scn.nextInt();

            // Check if the lesson is already completed
            String checkQuery = "SELECT * FROM Progress WHERE userID = ? AND courseID = ? AND lessonID = ?";
            PreparedStatement checkStmt = conn.prepareStatement(checkQuery);
            checkStmt.setInt(1, userID);
            checkStmt.setInt(2, courseID);
            checkStmt.setInt(3, lessonID);
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next()) {
                System.out.println("Lesson already marked as completed!");
            } else {
                String query = "INSERT INTO Progress (userID, courseID, lessonID, status) VALUES (?, ?, ?, 'Completed')";
                PreparedStatement stmt = conn.prepareStatement(query);
                stmt.setInt(1, userID);
                stmt.setInt(2, courseID);
                stmt.setInt(3, lessonID);
                stmt.executeUpdate();

                System.out.println("Lesson marked as completed!");
            }
        }
        private static void viewProgress() throws SQLException {
            System.out.print("Enter your User ID: ");
            int userID = scn.nextInt();
            System.out.print("Enter Course ID: ");
            int courseID = scn.nextInt();

            // Get total lessons in the course
            String totalLessonsQuery = "SELECT COUNT(*) AS total FROM Lessons WHERE courseID = ?";
            PreparedStatement totalLessonsStmt = conn.prepareStatement(totalLessonsQuery);
            totalLessonsStmt.setInt(1, courseID);
            ResultSet totalLessonsRs = totalLessonsStmt.executeQuery();

            int totalLessons = 0;
            if (totalLessonsRs.next()) {
                totalLessons = totalLessonsRs.getInt("total");
            }

            // Get completed lessons for the user in the course
            String completedLessonsQuery = "SELECT COUNT(*) AS completed FROM Progress WHERE userID = ? AND courseID = ?";
            PreparedStatement completedLessonsStmt = conn.prepareStatement(completedLessonsQuery);
            completedLessonsStmt.setInt(1, userID);
            completedLessonsStmt.setInt(2, courseID);
            ResultSet completedLessonsRs = completedLessonsStmt.executeQuery();

            int completedLessons = 0;
            if (completedLessonsRs.next()) {
                completedLessons = completedLessonsRs.getInt("completed");
            }

            // Display progress
            System.out.printf("Course Progress: %d/%d lessons completed (%.2f%%)\n",
                    completedLessons, totalLessons,
                    (totalLessons > 0 ? (completedLessons * 100.0 / totalLessons) : 0));
        }

}