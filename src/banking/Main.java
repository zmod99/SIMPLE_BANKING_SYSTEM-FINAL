package banking;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class Main {

    /* instanciranje DAOa, objekti s kojima pristupama razlicitim tablicama u bazi podataka
       njima se kasnije u programu predaje Connection objekt, koji predstavlja vezu prema bazi

       instanciranje Connection kojeg koristimo i za userDAO i AccountDAO
     */

    static Scanner scanner = new Scanner(System.in);


    static AccountDAO accountDAO = null;

    static UserDAO userDAO = null;

    static Connection conn = null;


    public static void main(String[] args) {
        try {
            if (args[0].equalsIgnoreCase("-filename")) {
                makeDbConnection(args[1]);

            } else {
                System.out.println("You have to set database like \"-fileName someName.db\"");
                System.exit(0);
            }
        } catch (IndexOutOfBoundsException e) {
            System.out.println("You have to set database like \"-fileName someName.db\"");
            System.exit(0);
        }
        accountDAO = new AccountDAO(conn);
        userDAO = new UserDAO(conn);
        userMenu();
        System.out.println("Bye!");

    }

    static void userMenu() {
        //int userMenu je autoinicijaliziran kao 0
        int userMenu;
        do {
            System.out.println("\n1. Create an user");
            System.out.println("2. Log into user");
            System.out.println("3. List all users");
            System.out.println("0. Exit");
            System.out.print(">");
            userMenu = scanner.nextInt();
            scanner.nextLine();
            switch (userMenu) {
                case 0:
                    break;
                case 1:
                    User user = new User();
                    System.out.print("\n Enter your first name: ");
                    user.setFirstName(scanner.nextLine());
                    System.out.print("\n Enter your last name: ");
                    user.setLastName(scanner.nextLine());
                    do {
                        System.out.print("\n Enter your citizenship: (Croatia, Bulgaria or Slovenia):");
                        user.setCountry(scanner.nextLine());

                    } while (user.getCountry() == null);

                    do {
                        System.out.print("\n Enter your Identification number: ");
                        user.setId_number(scanner.nextLine());
                    } while (user.getId_number() == null);

                    System.out.print("\n Enter your email: ");
                    user.setEmail(scanner.nextLine());
                    System.out.print("\n Enter your password: ");
                    user.setPassword(scanner.nextLine());
                    userDAO.addUser(user);
                    break;
                case 2:
                    User user1;
                    System.out.print("Please enter your email: ");
                    String email = scanner.nextLine();
                    System.out.print("Please enter your password: ");
                    String password = scanner.nextLine();
                    user1 = userDAO.getUser(email, password);
                    if (user1 == null) {
                        System.out.println("Incorrect email or password, try again!\n");
                        break;
                    }
                    accountMenu(user1);
                    break;
                case 3:
                    List<User> userList = userDAO.getUsersToList();
                    for (User userFromList : userList
                    ) {
                        System.out.println(userFromList);
                    }

            }

        } while (userMenu != 0);


    }

    static void accountMenu(User loggedUser) {
        int choice = 1;
        while (choice != 0) {
            System.out.println("\n1. Create an account\n2. Log into account\n3. List all your accounts\n0. Exit");
            choice = scanner.nextInt();
            scanner.nextLine();
            switch (choice) {
                case 1:
                    Account account = new Account();
                    account.createCard();
                    account.createPIN();
                    account.setUser_id(loggedUser.getId());
                    System.out.println("Your account has been created");
                    System.out.println("Your card number:\n" + account.getCardNumber());
                    System.out.println("Your card PIN:\n" + account.getCardPin());
                    accountDAO.addAccount(account);
                    break;
                case 2:
                    System.out.println("Enter card number:");
                    String cardNumber = scanner.nextLine();
                    System.out.println("Enter card PIN:");
                    String cardPin = scanner.nextLine();
                    Account acc = accountDAO.findAccountByNumber(cardNumber);
                    if (acc != null && acc.getCardPin().equals(cardPin)) {
                        cardMenu(acc);
                    } else {
                        System.out.println("Incorrect card number or card PIN, try again!");
                    }
                    break;
                case 3:
                    List<Account> accountList;
                    accountList = accountDAO.getAllAccountsFromUser(loggedUser.getId());
                    for (Account acc1 : accountList
                    ) {
                        System.out.println(acc1);
                    }
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Wrong input!");

            }
        }
    }

    static void cardMenu(Account account) {

        System.out.println("You have successfully logged to your account!");
        boolean isActive = true;
        while (isActive) {
            System.out.println("1. Balance\n2. Add income\n3. Do transfer\n4. Close account\n5. Log out \n0. Exit");
            String choice = scanner.next();
            scanner.nextLine();
            System.out.println();
            switch (Integer.parseInt(choice)) {
                case 1:
                    System.out.println("$" + account.getBalance());
                    break;

                case 2:
                    System.out.println("Enter income:");
                    account.addIncome(scanner.nextDouble());
                    accountDAO.updateBalance(account);
                    System.out.println("Income was added!");
                    break;
                case 3:
                    boolean isFound = false;
                    System.out.println("Enter card number:");
                    String cardNumberReciever = scanner.nextLine();
                    if (cardNumberReciever.length() != 16) {
                        System.out.println("Wrong input!");
                        break;
                    }
                    // Ako je prosla luhnov algoritam, onda se provjerava da li postoji u bazi, ako ne onda se ispise
                    // da je greska u upisu broja kartice
                    if (Account.checkIfValid(cardNumberReciever)) {
                        for (Account receiver : accountDAO.getAccountsToList()) {
                            if (receiver.getCardNumber().equals(cardNumberReciever)) {
                                transfer(account, receiver);
                                isFound = true;
                            }
                        }
                        if (!isFound) {
                            System.out.println("This card does not exist in the system!");
                        }
                    } else {
                        System.out.println("You probably made a mistake in the card number. Please try again!");
                    }
                    break;

                case 4:
                    accountDAO.deleteAccount(account);
                    System.out.println("The account has been closed!\n");
                    isActive = false;
                    break;
                case 5:
                    isActive = false;
                    System.out.println("You have successfully logged out!");
                    break;
                case 0:
                    System.out.println("Bye!");
                    System.exit(0);
            }
        }

    }

    static void transfer(Account senderAccount, Account receiverAccount) {
        System.out.println("Enter the money amount that you wish to transfer:");
        double transferAmount = scanner.nextDouble();   // accounts are receiver(receiver) and account(sender)s

        if (senderAccount.getBalance() < transferAmount) {
            System.out.println("Not enough money!");
            return;
        }
        if (transferAmount <= 0L) {
            System.out.println("Wrong input!");
            return;
        }
        receiverAccount.addIncome(transferAmount);
        senderAccount.setBalance(senderAccount.getBalance() - transferAmount);
        System.out.println("Success!");
        accountDAO.updateBalance(senderAccount);
        accountDAO.updateBalance(receiverAccount);
    }

    private static void makeDbConnection(String fileName) {
        try {
            conn = DriverManager.getConnection("jdbc:sqlite:" + fileName);
        } catch (SQLException e) {
            throw new RuntimeException();
        }
    }


}