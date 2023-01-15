package banking;

public class User {

    private int id;

    private String firstName;

    private String lastName;

    private String email;

    private String password;

    private String id_number;

    private Countries country;

    public int getId() {
        return id;

    }

    public void setId(int id) {
        this.id = id;
    }

    public void setCountry(String countryInput) {
        switch (countryInput.substring(0, 3).toUpperCase()) {
            case "CRO":
                this.country = Countries.CROATIA;
                return;
            case "SLO":
                this.country = Countries.SLOVENIA;
                return;
            case "BUL":
                this.country = Countries.BULGARIA;

        }
    }

    public Countries getCountry() {
        return country;
    }

    public String getId_number() {
        return id_number;
    }

    public void setId_number(String id_number) {
        if (this.country.idNumberLength != id_number.length()) {
            System.out.printf("%s ID number must contain %d characters to be valid, please try again!\n\n", this.country,this.country.idNumberLength);
            return;
        }
        this.id_number = id_number;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }



    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", id_number='" + id_number + '\'' +
                ", country=" + country +
                '}';
    }
}
