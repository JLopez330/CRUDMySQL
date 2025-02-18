package Clases;

import javafx.scene.control.Alert;

import java.sql.Connection;
import java.sql.DriverManager;

public class BaseConexion {

    // Variable de tipo Connection para gestionar la conexión a la base de datos
    Connection conectar = null;

    // Credenciales y parámetros para la conexión
    String usuario = "root";  // Usuario de la base de datos
    String contrasenia = "orange00";  // Contraseña del usuario
    String bd = "baseusuarios";  // Nombre de la base de datos a la que se conecta
    String ip = "localhost";  // Dirección IP del servidor de la base de datos
    String port = "3306";  // Puerto por defecto para MySQL

    // Cadena de conexión formada con los parámetros anteriores
    String cadena = "jdbc:mysql://"+ip+":"+port+"/"+bd;

    // Metodo para realizar la conexión a la base de datos
    public Connection iniciarConexion(){
        try{
            // Cargar el driver JDBC para MySQL (com.mysql.jdbc.Driver)
            Class.forName("com.mysql.jdbc.Driver");

            // Intentar realizar la conexión con los parámetros proporcionados
            conectar = DriverManager.getConnection(cadena,usuario,contrasenia);

            // Si la conexión es exitosa, mostrar un mensaje
            //showAlert("Mensaje","Conexión Exitosa");

        }catch (Exception e){
            // Si ocurre un error en la conexión, se captura la excepción y se muestra el error
            showAlert("Mensaje","No fue posible conectar con la base de datos\nError: "+e.toString());
        }

        // Retorna el objeto Connection, que se puede utilizar para realizar consultas a la base de datos
        return conectar;
    }

    // Metodo para mostrar una alerta en la interfaz gráfica
    private void showAlert(String title, String content){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);  // Establecer el título de la alerta
        alert.setHeaderText(null);  // No mostrar un encabezado
        alert.setContentText(content);  // Establecer el contenido de la alerta
        alert.showAndWait();  // Mostrar la alerta y esperar a que el usuario la cierre
    }

    // Cerrar la conexión a la base de datos
    public void cerrarConexion(){
        try{
            // Verificar si la conexión no es nula y si no está ya cerrada
            if(conectar != null && !conectar.isClosed()){
                conectar.close();  // Cerrar la conexión
                //showAlert("Mensaje","Conexion cerrada");  // Mostrar un mensaje indicando que se cerró la conexión
            }
        }catch (Exception e){
            // Si ocurre un error al cerrar la conexión, mostrar un mensaje de error
            showAlert("Mensaje","No fue posible cerrar conexion\nError: "+e.toString());
        }
    }

}
