package banking;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AccountDAO {

    final private Connection conn;

    public AccountDAO(Connection conn) {
        this.conn = conn;
        try {
            String createTableSQL = """
                    CREATE TABLE IF NOT EXISTS card (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,\s
                    number TEXT NOT NULL,\s
                    pin TEXT NOT NULL,\s
                    balance INTEGER DEFAULT 0,\s
                    user_id INTEGER NOT NULL,\s
                    FOREIGN KEY(user_id) REFERENCES user(id))""";
            Statement statement = conn.createStatement();
            statement.execute(createTableSQL);
            statement.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // loads passed AccountList with accounts from database
    public List<Account> getAccountsToList () {
        String selector = "SELECT * FROM card";
        List<Account> accountList = new ArrayList<>();
        try {
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery(selector);
            while (resultSet.next()) {
                Account account = new Account();
                account.setCardNumber(resultSet.getString("number"));
                account.setCardPin(resultSet.getString("pin"));
                account.setBalance(resultSet.getInt("balance"));
                accountList.add(account);
            }



        } catch (SQLException e) {
            e.printStackTrace();
        }
        return accountList;
    }

    public void addAccount(Account account) {
        try {
            Statement statement = conn.createStatement();
            String sql = "INSERT INTO card(number,pin,balance,user_id) VALUES("
                    + account.getCardNumber()
                    + "," + account.getCardPin()
                    + "," + account.getBalance()
                    + "," + account.getUser_id()
                    + ")";

            statement.execute(sql);
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Account findAccountByNumber(String cardNumber) {
        String sql = "SELECT * FROM card WHERE number=" + cardNumber;
        try {
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            if (resultSet.next()) {
                Account account = new Account();
                account.setCardNumber(resultSet.getString("number"));
                account.setCardPin(resultSet.getString("pin"));
                account.setBalance(resultSet.getDouble("balance"));
                return account;
            }

        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public List<Account> getAllAccountsFromUser(int id) {
        String sql = "SELECT * FROM card WHERE user_id=" + id;
        List<Account> accountList = new ArrayList<>();
        try {
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                Account account = new Account();
                account.setCardNumber(resultSet.getString("number"));
                account.setCardPin(resultSet.getString("pin"));
                account.setBalance(resultSet.getDouble("balance"));
                account.setUser_id(resultSet.getInt("user_id"));
                accountList.add(account);
            }
            return accountList;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;

    }
    public void updateBalance(Account account) {
        try {
            Statement statement = conn.createStatement();
            String updateSQL = "UPDATE card SET balance=" + account.getBalance() + " WHERE number=" + account.getCardNumber();
            statement.execute(updateSQL);
            statement.close();


        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void deleteAccount(Account account) {
        try {
            Statement statement = conn.createStatement();
            String deleteRowSQL = "DELETE FROM card WHERE number=" + account.getCardNumber();
            statement.execute(deleteRowSQL);
            statement.close();


        } catch (SQLException e) {
            e.printStackTrace();
        }

    }


}
