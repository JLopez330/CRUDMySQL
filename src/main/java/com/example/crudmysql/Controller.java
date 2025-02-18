package com.example.crudmysql;

import Clases.BaseDAO;
import Clases.Persona;
import Clases.Telefono;
import Clases.Vehiculo;
import Clases.AlertManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

import java.net.URL;

public class Controller implements Initializable {
    //Componentes de la interfaz grafica
    @FXML
    private TextField nombreField;
    @FXML
    private TextField direccionField;
    @FXML
    private TextField telefonoField;
    @FXML
    private TextField idField;
    @FXML
    private TextField colorField;
    @FXML
    private TextField manejoField;

    @FXML
    private TableView<Persona> tablaPersonas;
    @FXML
    private TableView<Telefono> tablaTelefonos;
    @FXML
    private TableView<Vehiculo> tablaVehiculo;

    @FXML
    private TableColumn<Persona, Integer> columnaIdPersona;
    @FXML
    private TableColumn<Persona, String> columnaNombre;
    @FXML
    private TableColumn<Persona, String> columnaDireccion;

    @FXML
    private TableColumn<Telefono, Integer> columnaIdTelefono;
    @FXML
    private TableColumn<Telefono, String> columnaTelefono;
    @FXML
    private TableColumn<Telefono, Integer> columnaIdPersonaTelefono;

    @FXML
    private TableColumn<Vehiculo, Integer> columnaIdVehiculo;
    @FXML
    private TableColumn<Vehiculo, String> columnaTipoVehiculo;
    @FXML
    private TableColumn<Vehiculo, String> columnaColorVehiculo;
    @FXML
    private TableColumn<Vehiculo, String> columnaManejoVehiculo;
    @FXML
    private TableColumn<Vehiculo, Integer> columnaIdPersonaVehiculo;
    @FXML
    private ChoiceBox<String> vehiculoBox;

    private AlertManager alertManager;

    //Metodo para inicializar la interfaz grafica
    public void initialize(URL url, ResourceBundle rb) {
        if (vehiculoBox!=null){
            vehiculoBox.setItems(FXCollections.observableArrayList(
                    "automovil","camion","maritimo","motocicleta","bicicleta"
            ));
        }

        alertManager = new AlertManager();

        Clases.BaseConexion conexion = new Clases.BaseConexion();
        conexion.iniciarConexion();

        // Configurar columnas de la tabla de Personas
        columnaIdPersona.setCellValueFactory(new PropertyValueFactory<>("id"));
        columnaNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        columnaDireccion.setCellValueFactory(new PropertyValueFactory<>("direccion"));

        // Configurar columnas de la tabla de Teléfonos
        columnaIdTelefono.setCellValueFactory(new PropertyValueFactory<>("id"));
        columnaTelefono.setCellValueFactory(new PropertyValueFactory<>("telefono"));
        columnaIdPersonaTelefono.setCellValueFactory(new PropertyValueFactory<>("idPersona"));

        // Configurar columnas de la tabla de Vehiculos
        columnaIdVehiculo.setCellValueFactory(new PropertyValueFactory<>("id"));
        columnaTipoVehiculo.setCellValueFactory(new PropertyValueFactory<>("tipo"));
        columnaColorVehiculo.setCellValueFactory(new PropertyValueFactory<>("color"));
        columnaManejoVehiculo.setCellValueFactory(new PropertyValueFactory<>("manejo"));
        columnaIdPersonaVehiculo.setCellValueFactory(new PropertyValueFactory<>("id_persona"));

        cargarDatos(tablaPersonas,tablaTelefonos,tablaVehiculo);
        idField.setDisable(true);

    }

    /**
     * Metodo para guardar los datos capturados y mostrarlos en las tablas cuando el botón guardar
     * es presionado
     **/
    @FXML
    public void guardarDatosGenerico(ActionEvent event) {
        Clases.Persona persona = new Persona(nombreField.getText(), direccionField.getText());
        Clases.BaseDAO<Persona> altaPersona = new BaseDAO<>(Persona.class, "persona");
        int id_persona = altaPersona.insertar(persona);

        System.out.println("ID de persona generado: " + id_persona); // Depuración

        if (id_persona != -1) {
            Clases.Telefono telefono = new Telefono(telefonoField.getText(), id_persona);
            Clases.BaseDAO<Telefono> altaTelefono = new BaseDAO<>(Telefono.class, "telefono");
            altaTelefono.insertar(telefono);

            // Depuración de ChoiceBox
            String tipoVehiculo = vehiculoBox.getValue();
            System.out.println("Vehículo seleccionado: " + tipoVehiculo);

            // Verificar si el ChoiceBox tiene valor
            if (tipoVehiculo != null && !tipoVehiculo.isEmpty()) {
                Clases.Vehiculo vehiculo = new Vehiculo(tipoVehiculo, colorField.getText(), manejoField.getText(), id_persona);
                Clases.BaseDAO<Vehiculo> altaVehiculo = new BaseDAO<>(Vehiculo.class, "vehiculo");
                int id_vehiculo = altaVehiculo.insertar(vehiculo);
                System.out.println("ID de vehículo generado: " + id_vehiculo);
                alertManager.showAlert("Información","Datos Guardados con Exito!");
            } else {
                System.out.println("Error: No se seleccionó ningún tipo de vehículo.");
                alertManager.showAlert("Error","Error al guardar datos.");
            }
        }

        cargarDatos(tablaPersonas, tablaTelefonos, tablaVehiculo);
        limpiarCampos();
    }


    /**
     * Metodo para modificar los datos de una persona basando en su Id existente
     * en caso de añadir un nuevo número telefónico se agrega a la tabla de teléfonos
     * cuando el botón modificar es presionado y los campos se encuentran llenos
     */
    @FXML
    public void modificarDatos(ActionEvent event) {
        // Obtener la persona seleccionada de la tabla
        Persona personaSeleccionada = tablaPersonas.getSelectionModel().getSelectedItem();

        // Verificar si se ha seleccionado una persona
        if (personaSeleccionada == null) {
            alertManager.showAlert("Error","Selecciona una persona para editar");
            return;
        }

        // Actualizar los campos de la persona (nombre y dirección)
        personaSeleccionada.setNombre(nombreField.getText());
        personaSeleccionada.setDireccion(direccionField.getText());

        // Crear una instancia de BaseDAO para la clase Persona
        BaseDAO<Persona> daoPersona = new BaseDAO<>(Persona.class, "persona");

        // Modificar los datos de la persona en la base de datos
        if (daoPersona.modificar(personaSeleccionada, personaSeleccionada.getId())) {
            System.out.println("Persona modificada exitosamente");

            // Verificar si se han ingresado datos del vehículo
            String tipoVehiculo = vehiculoBox.getValue();
            String color = colorField.getText();
            String manejo = manejoField.getText();

            if ((tipoVehiculo != null && !tipoVehiculo.isEmpty()) ||
                    (color != null && !color.isEmpty()) ||
                    (manejo != null && !manejo.isEmpty())) {
                // Solo crear o modificar un vehículo si hay datos completos para ello
                if (tipoVehiculo != null && !tipoVehiculo.isEmpty() &&
                        !color.isEmpty() && !manejo.isEmpty()) {
                    Clases.Vehiculo vehiculo = new Vehiculo(tipoVehiculo, color, manejo, personaSeleccionada.getId());
                    BaseDAO<Vehiculo> daoVehiculo = new BaseDAO<>(Vehiculo.class, "vehiculo");
                    int id_vehiculo = daoVehiculo.insertar(vehiculo);
                    System.out.println("ID de vehículo generado: " + id_vehiculo);
                } else {
                    System.out.println("No se ha ingresado un vehículo completo. No se creará uno nuevo.");
                }
            } else {
                System.out.println("No se ha modificado el vehículo.");
            }

            // Refrescar los datos en las tablas
            cargarDatos(tablaPersonas, tablaTelefonos, tablaVehiculo);
        } else {
            alertManager.showAlert("Error","No se ha podido editar a la persona");
        }

        // Limpiar los campos en la interfaz después de la modificación
        limpiarCampos();
    }




    /**
     * Metodo para borrar los datos de una persona incluyendo sus telefonos y vehiculos vinculados
     */
    @FXML
    public void eliminarDatos(ActionEvent event) {
        // Obtener la persona seleccionada de la tabla
        Persona personaSeleccionada = tablaPersonas.getSelectionModel().getSelectedItem();

        // Verificar si se ha seleccionado una persona
        if (personaSeleccionada == null) {
            alertManager.showAlert("Error","Selecciona una persona para eliminar");
            return;
        }

        // Crear una instancia de BaseDAO para la clase Persona
        BaseDAO<Persona> daoPersona = new BaseDAO<>(Persona.class, "persona");

        // Eliminar la persona (esto eliminará automáticamente los teléfonos y vehículos asociados debido al ON DELETE CASCADE)
        if (daoPersona.eliminar(personaSeleccionada)) {
            alertManager.showAlert("Información","Persona eliminada exitosamente");
            // Refrescar los datos en las tablas
            cargarDatos(tablaPersonas, tablaTelefonos, tablaVehiculo);
        } else {
            alertManager.showAlert("Error","Error al eliminar persona");
        }

        // Limpiar los campos en la interfaz después de la eliminación
        limpiarCampos();
    }



    /**
     * Metodo para cargar los datos de la base de datos a las tablas de la interfaz gráfica.
     */
    public void cargarDatos(
            TableView<Persona> tablaPersonas,
            TableView<Telefono> tablaTelefonos,
            TableView<Vehiculo> tablaVehiculos) {

        BaseDAO<Persona> personaDAO = new BaseDAO<>(Persona.class, "persona");
        BaseDAO<Telefono> telefonoDAO = new BaseDAO<>(Telefono.class, "telefono");
        BaseDAO<Vehiculo> vehiculoDAO = new BaseDAO<>(Vehiculo.class, "vehiculo");

        try {
            // Obtener listas desde BaseDAO
            ObservableList<Persona> listaPersonas = FXCollections.observableArrayList(personaDAO.obtenerTodos());
            ObservableList<Telefono> listaTelefonos = FXCollections.observableArrayList(telefonoDAO.obtenerTodos());
            ObservableList<Vehiculo> listaVehiculos = FXCollections.observableArrayList(vehiculoDAO.obtenerTodos());

            // Asignar datos a las tablas
            tablaPersonas.setItems(listaPersonas);
            tablaTelefonos.setItems(listaTelefonos);
            tablaVehiculos.setItems(listaVehiculos);

        } catch (Exception e) {
            alertManager.showAlert("Error","Error al obtener datos: "+e.getMessage());
        }
    }

    /**
     * Metodo para seleccionar una persona de la tabla de personas, toma sus datos de dirección y nombre
     */
    @FXML
    public void seleccionarPersona(MouseEvent event) {
        // Obtener la persona seleccionada en la tabla
        Persona personaSeleccionada = tablaPersonas.getSelectionModel().getSelectedItem();

        // Verificar si se ha seleccionado una persona
        if (personaSeleccionada != null) {
            // Cargar los datos de la persona seleccionada en los campos de texto
            idField.setText(String.valueOf(personaSeleccionada.getId()));
            nombreField.setText(personaSeleccionada.getNombre());
            direccionField.setText(personaSeleccionada.getDireccion());

        }
    }

    //Metodo para limpiar los campos de la interfaz gráfica
    public void limpiarCampos() {
        idField.setText("");
        nombreField.setText("");
        direccionField.setText("");
        telefonoField.setText("");
        colorField.setText("");
        manejoField.setText("");
    }

}