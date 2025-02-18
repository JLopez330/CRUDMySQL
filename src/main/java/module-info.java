module com.example.crudmysql {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.desktop;


    opens com.example.crudmysql to javafx.fxml;
    opens Clases to javafx.base;
    exports com.example.crudmysql;
}