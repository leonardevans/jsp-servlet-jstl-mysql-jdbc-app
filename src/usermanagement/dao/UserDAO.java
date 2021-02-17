package usermanagement.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import usermanagement.model.User;

//DAO - data access object
//this class provides CRUD database operations for the table users in the database
public class UserDAO {
    private String jdbcURL = "jdbc:mysql://localhost:3306/jsp_jstl_mvc_servlet?useSSL=false";
    private String jdbcUsername = "invalid";
    private String jdbcPassword = "dbconnect";

    private static final String INSERT_USERS_SQL = "INSERT INTO users" + " (name, email, country) VALUES " + " (?, ?, ?)";
    private static final String SELECT_USER_BY_ID = "SELECT id, name, email, country FROM users WHERE id = ?";
    private static final String SELECT_ALL_USERS = "SELECT * FROM users";
    private static final String DELETE_USERS_SQL = "DELETE FROM users WHERE id = ?";
    private static final String UPDATE_USERS_SQL = "UPDATE users SET name = ?, email = ?, country = ? WHERE id = ?";

    //    database connection method
    protected Connection getConnection() {
        Connection connection = null;
        try {
//            Class.forName(com.mysql.jdbc.Driver);
            connection = DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);
        } catch (SQLException e) {
//            to do auto-generated catch block
            e.printStackTrace();
        }
//        catch(ClassNotFoundException e){
//         //to do auto generated catch block
//            e.printStackTrace();
//        }
        return connection;
    }

    //    create or insert user
    public void insertUser(User user) throws SQLException {
        try (
                Connection connection = getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(INSERT_USERS_SQL);
        ) {
            preparedStatement.setString(1, user.getName());
            preparedStatement.setString(2, user.getEmail());
            preparedStatement.setString(3, user.getCountry());
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //    update user
    public boolean updateUser(User user) {
        boolean rowUpdated = false;
        try (
                Connection connection = getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_USERS_SQL);
        ) {
            preparedStatement.setString(1, user.getName());
            preparedStatement.setString(2, user.getEmail());
            preparedStatement.setString(3, user.getCountry());
            preparedStatement.setInt(4, user.getId());
            preparedStatement.executeUpdate();

            rowUpdated = preparedStatement.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return rowUpdated;
    }

    //    select user  by id
    public User selectUser(int id) {
        User user = null;
//        step 1: Establishing a connection
        try (
                Connection connection = getConnection();
//                create a statement using connection object
                PreparedStatement preparedStatement = connection.prepareStatement(SELECT_USER_BY_ID);

        ) {
            preparedStatement.setInt(1, id);
            System.out.println(preparedStatement);
//            execute the query and save the result in a result set
            ResultSet rs = preparedStatement.executeQuery();

//            process the result set object
            while (rs.next()) {
                String name = rs.getString("name");
                String email = rs.getString("email");
                String country = rs.getString("country");
                user = new User(id, name, email, country);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    //    select users
    public List<User> selectAllUsers() {
        List<User> users = new ArrayList<>();

//        step 1: Establishing a connection
        try (
                Connection connection = getConnection();
//                create a statement using connection object
                PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_USERS);

        ) {
            System.out.println(preparedStatement);
//            execute the query and save the result in a result set
            ResultSet rs = preparedStatement.executeQuery();

//            process the result set object
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String email = rs.getString("email");
                String country = rs.getString("country");
                users.add(new User(id, name, email, country));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    //    delete user
    public boolean deleteUser(int id) throws SQLException {
        boolean rowDeleted = false;
        try (
                Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(DELETE_USERS_SQL);
        ) {
            statement.setInt(1, id);
            rowDeleted = statement.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rowDeleted;
    }
}
