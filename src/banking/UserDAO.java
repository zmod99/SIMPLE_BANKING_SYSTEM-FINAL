package banking;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {

    private final Connection conn;

    // find users in database, loadusers into list,

    public UserDAO(Connection conn) {
        this.conn = conn;
        String sql = "CREATE TABLE IF NOT EXISTS user (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "first_name text NOT NULL," +
                "last_name text NOT NULL," +
                "email text NOT NULL UNIQUE," +
                "password text NOT NULL," +
                "id_number TEXT NOT NULL," +
                "country text NOT NULL)";

        try {
            Statement statement = conn.createStatement();
            statement.execute(sql);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    public List<User> getUsersToList() {
        List<User> userList = new ArrayList<>();
        String sql = "SELECT * FROM user";
        try (Statement statement = conn.createStatement()) {
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getInt("id"));
                user.setFirstName(resultSet.getString("first_name"));
                user.setLastName(resultSet.getString("last_name"));
                user.setEmail(resultSet.getString("email"));
                user.setPassword(resultSet.getString("password"));
                user.setCountry(resultSet.getString("country"));
                user.setId_number(resultSet.getString("id_number"));
                userList.add(user);
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return userList;

    }

    public void addUser(User user) {
        String sql = "INSERT INTO user(first_name, last_name, email, password, id_number, country) VALUES(?,?,?,?,?,?)";
        try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setString(1, user.getFirstName());
            preparedStatement.setString(2, user.getLastName());
            preparedStatement.setString(3, user.getEmail());
            preparedStatement.setString(4, user.getPassword());
            preparedStatement.setString(5, user.getId_number());
            preparedStatement.setString(6, user.getCountry().name());
            preparedStatement.execute();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public User getUser(String email, String password) {
        String sql = "SELECT * FROM user WHERE email='" + email + "'" + " AND password='" + password + "';";
        try (Statement statement = conn.createStatement()) {
            ResultSet resultSet = statement.executeQuery(sql);
            if (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getInt("id"));
                user.setFirstName(resultSet.getString("first_name"));
                user.setLastName(resultSet.getString("last_name"));
                user.setEmail(resultSet.getString("email"));
                user.setPassword(resultSet.getString("password"));
                user.setCountry(resultSet.getString("country"));
                user.setId_number(resultSet.getString("id_number"));
                return user;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
}
