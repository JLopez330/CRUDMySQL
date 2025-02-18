package Clases;

public class Vehiculo {
    private int id;
    private String tipo;
    private String color;
    private String manejo;
    private int id_persona;

    public Vehiculo() {

    }

    public Vehiculo(String tipo, String color, String manejo, int id_Persona) {
        this.tipo = tipo;
        this.color = color;
        this.manejo = manejo;
        this.id_persona = id_Persona;
    }

    public Vehiculo(int id, String tipo, String color, String manejo, int id_Persona) {
        this.id = id;
        this.tipo = tipo;
        this.color = color;
        this.manejo = manejo;
        this.id_persona = id_Persona;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getId_persona() {
        return id_persona;
    }

    public void setId_persona(int id_persona) {
        this.id_persona = id_persona;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getManejo() {
        return manejo;
    }

    public void setManejo(String manejo) {
        this.manejo = manejo;
    }
}
