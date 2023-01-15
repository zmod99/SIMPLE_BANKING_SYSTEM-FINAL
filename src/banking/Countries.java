package banking;

public enum Countries {

    CROATIA(11),
    SLOVENIA(13),
    BULGARIA(10);

    public final int idNumberLength;


    Countries(int idNumberLength) {
        this.idNumberLength = idNumberLength;
    }



    @Override
    public String toString() {
        return this.name();
    }
}


