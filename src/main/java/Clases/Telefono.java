package Clases;

public class Telefono {
    private int id; //Id del telefono registrado
    private String telefono; //Telefono registrado
    private int id_persona; //Id de la persona a la que corresponde el telefono registrado

    public Telefono() {

    }

    public Telefono(String telefono, int idPersona) {
        this.telefono = telefono;
        this.id_persona = idPersona;
    }

    public Telefono(int id, String telefono, int id_persona) {
        this.telefono = telefono;
        this.id_persona = id_persona;
        this.id = id;
    }

    //Metodos Setters y Getters de la Clase
    public int getId() { return id; }
    public String getTelefono() { return telefono; }
    public int getIdPersona() { return id_persona; }

}
