package Clases;

import javafx.scene.control.Alert;

public class AlertManager {

    private Alert alert;

    public AlertManager() {
        alert = new Alert(Alert.AlertType.INFORMATION);
    }

    // Metodo privado para mostrar alertas de información
    public void showAlert(String title, String content) {
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
