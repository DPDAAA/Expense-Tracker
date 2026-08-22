package tracker.model;

public class Transaction {

    private static int nextID = 0;
    private int id;
    private String description;
    private Type type;
    private double sum;

    public Transaction(String description, Type type, double sum) {
        this.id = nextID++;
        this.setDescription(description);
        this.setType(type);
        this.setSum(sum);

    }

    public Transaction(int id, String description, Type type, double sum) {
        this.id = id;
        this.setDescription(description);
        this.setType(type);
        this.setSum(sum);

        if (id >= nextID) {
            nextID = id + 1;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (!(o.getClass().equals(this.getClass()))) {
            return false;
        }
        Transaction transaction = (Transaction) o;
        return this.id == transaction.id;
    }

    @Override
    public String toString() {
        return "Description: " + this.description + "\n" + "Type: " + this.type + "\n" + "Sum: " + this.sum + "\n"
                + "ID:" + this.id + "\n" + "-----------------------";
    }

    @Override
    public int hashCode() {
        return this.id;
    }

    public int getId() {
        return this.id;
    }

    public String getDescription() {
        return this.description;
    }

    public Type getType() {
        return this.type;
    }

    public double getSum() {
        return this.sum;
    }

    public final void setDescription(String description) {
       /*  if (description == null || description.isBlank()) {
            throw new IllegalArgumentException("Beschreibung darf nicht leer sein.");
        } */

        this.description = description;
    }

    public final void setType(Type type) {
        if (type == null) {
            throw new IllegalArgumentException("Type darf nicht null sein.");
        }

        this.type = type;
    }

 public final void setSum(double sum) {
    if (sum <= 0) {
        throw new IllegalArgumentException("Summe muss größer als 0 sein.");
    }

    this.sum = sum;
}
}