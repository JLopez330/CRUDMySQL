package Clases;

public class Persona {
    private int id; //Id de la persona
    private String nombre; //Nombre de la persona
    private String direccion; //Direccion de la persona

    public Persona() {

    }

    public Persona(String nombre, String direccion) {
        this.nombre = nombre;
        this.direccion = direccion;
    }

    public Persona(int id, String nombre, String direccion) {
        this.id = id;
        this.nombre = nombre;
        this.direccion = direccion;
    }

    //Metodos Setters y Getters de la Clase
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setDireccion(String direccion) { this.direccion = direccion; }
    public String getNombre() { return nombre; }
    public String getDireccion() { return direccion; }

}
