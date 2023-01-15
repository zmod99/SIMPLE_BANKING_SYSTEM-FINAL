package banking;


class Account {

    private double balance;
    private String cardNumber;
    private String cardPin;
    private int user_id;

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getCardPin() {
        return cardPin;
    }

    public void setCardPin(String cardPin) {
        this.cardPin = cardPin;
    }

    public void setUser_id(int id) {
        this.user_id = id;
    }

    public int getUser_id() {
        return user_id;
    }

    void createCard() {
        String cardNumber = "400000" + String.format("%09d", (long) (Math.random() * 999999999L));
        this.cardNumber = checkSum(cardNumber);
    }

    void createPIN() {
        this.cardPin = String.format("%04d", (long) (Math.random() * 9999));
    }

    static String checkSum(String cardNumber15) {
        int[] cardDigits = new int[15];
        for (int i = 0; i < cardNumber15.length(); i++) {
            cardDigits[i] = Integer.parseInt("" + cardNumber15.charAt(i));
        }
        int sum = 0;

        for (int i = 0; i < cardDigits.length; i++) {
            if (i % 2 == 0) {
                cardDigits[i] = returnValue(cardDigits[i]);
            }
            sum += cardDigits[i];
        }
        return (cardNumber15 + getLastDigit(sum));

    }

    static boolean checkIfValid(String cardNumber16) {
        int lastDigit = Integer.parseInt("" + cardNumber16.charAt(15));
        int[] cardDigits = new int[16];

        for (int i = 0; i < cardNumber16.length() - 1; i++) {
            cardDigits[i] = Integer.parseInt("" + cardNumber16.charAt(i));
        }
        int sum = 0;
        for (int i = 0; i < cardDigits.length; i++) {
            if (i % 2 == 0) {
                cardDigits[i] = returnValue(cardDigits[i]);
            }
            sum += cardDigits[i];
        }
        return getLastDigit(sum).equals(String.valueOf(lastDigit));

    }


    private static int returnValue(int num) {        //obavlje operaciju nad znamenkama i vraca rezultat znamenku
        num *= 2;
        if (num < 10) {
            return num;
        }
        return (num % 10 + 1);
    }

    private static String getLastDigit(int sum) { //dobiva sumu prvih 15 znamenki nakon modifikacija i racuna koja bi trebala biti zadnja znamenka
        int secondDigit = sum % 10;        // vraca znamenku kao string
        if (secondDigit == 0) {
            return "0";
        }
        return Integer.toString((10 - secondDigit));
    }

    void addIncome(double income) {
        this.balance += income;
    }

    @Override
    public String toString() {
        return "Account{" +
                "balance=" + balance +
                ", cardNumber='" + cardNumber + '\'' +
                ", cardPin='" + cardPin + '\'' +
                ", user_id=" + user_id +
                '}';
    }
}
