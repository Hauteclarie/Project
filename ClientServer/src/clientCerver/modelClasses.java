package clientCerver;
import java.io.Serializable;

class Kot implements Serializable {
    private static final long serialVersionUID = 1L;
    private String name;

    public Kot(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Kot{" + "name='" + name + '\'' + '}';
    }
}

class Pies implements Serializable {
    private static final long serialVersionUID = 1L;
    private String name;

    public Pies(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Pies{" + "name='" + name + '\'' + '}';
    }
}

class Rybka implements Serializable {
    private static final long serialVersionUID = 1L;
    private String name;

    public Rybka(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Rybka{" + "name='" + name + '\'' + '}';
    }
}