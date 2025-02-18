package Clases;
import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BaseDAO<T> {
    private final Class<T> type;
    private final String tableName;

    public BaseDAO(Class<T> type, String tableName) {
        this.type = type;
        this.tableName = tableName;
    }

    public int insertar(T objeto) {
        BaseConexion conn = new BaseConexion();
        Field[] fields = type.getDeclaredFields();
        StringBuilder query = new StringBuilder("INSERT INTO " + tableName + " (");
        StringBuilder values = new StringBuilder(" VALUES (");

        for (Field field : fields) {
            if (!field.getName().equalsIgnoreCase("id")) {
                query.append(field.getName()).append(", ");
                values.append("?, ");
            }
        }

        query.setLength(query.length() - 2);
        values.setLength(values.length() - 2);
        query.append(")").append(values).append(");");

        System.out.println("Consulta SQL generada: " + query);

        try (PreparedStatement ps = conn.iniciarConexion().prepareStatement(query.toString(), PreparedStatement.RETURN_GENERATED_KEYS)) {
            int index = 1;
            for (Field field : fields) {
                if (!field.getName().equalsIgnoreCase("id")) {
                    field.setAccessible(true);
                    Object value = field.get(objeto);
                    System.out.println("Valor para " + field.getName() + ": " + value);
                    ps.setObject(index++, value);
                }
            }
            int filasAfectadas = ps.executeUpdate();

            if (filasAfectadas == 0) {
                System.out.println("Error: No se insertaron registros en la tabla " + tableName);
                return -1;
            }

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error SQL en la inserción de " + tableName + ": " + e.getMessage());
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            System.out.println("Error al acceder a los campos del objeto: " + e.getMessage());
            e.printStackTrace();
        } finally {
            conn.cerrarConexion();
        }
        return -1;
    }

    public List<T> obtenerTodos() {
        List<T> lista = new ArrayList<>();
        BaseConexion conn = new BaseConexion();
        String query = "SELECT * FROM " + tableName;

        try (Connection connection = conn.iniciarConexion();
             PreparedStatement ps = connection.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                T objeto = type.getDeclaredConstructor().newInstance();
                for (Field field : type.getDeclaredFields()) {
                    field.setAccessible(true);
                    field.set(objeto, rs.getObject(field.getName()));
                }
                lista.add(objeto);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            conn.cerrarConexion();
        }
        return lista;
    }

    public boolean modificar(T objeto, int id) {
        BaseConexion conn = new BaseConexion();
        String sql = "UPDATE " + tableName + " SET ";

        List<String> columnas = new ArrayList<>();
        List<Object> valores = new ArrayList<>();

        // Obtener los campos del objeto
        for (Field field : objeto.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            try {
                if (!field.getName().equalsIgnoreCase("id")) {
                    // Si el campo no es el ID, se agrega al SQL
                    columnas.add(field.getName() + " = ?");
                    valores.add(field.get(objeto));
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                return false;  // Si no se puede acceder a un campo, retornar false
            }
        }

        // Construir la consulta SQL
        sql += String.join(", ", columnas) + " WHERE id = ?";

        try (PreparedStatement stmt = conn.iniciarConexion().prepareStatement(sql)) {
            int index = 1;

            // Establecer los valores de los campos en el PreparedStatement
            for (Object valor : valores) {
                stmt.setObject(index++, valor);
            }

            // Establecer el ID del objeto para aplicar la actualización
            stmt.setInt(index, id);

            // Ejecutar la consulta y verificar si se actualizó alguna fila
            int filasAfectadas = stmt.executeUpdate();
            return filasAfectadas > 0;  // Si se actualizó alguna fila, devuelve true
        } catch (SQLException e) {
            e.printStackTrace();
            return false;  // Si ocurre algún error en la consulta, retorna false
        } finally {
            conn.cerrarConexion();
        }
    }


    public boolean eliminar(T objeto) {
        BaseConexion conn = new BaseConexion();
        String sql = "DELETE FROM " + tableName + " WHERE id = ?";

        try (PreparedStatement stmt = conn.iniciarConexion().prepareStatement(sql)) {
            stmt.setInt(1, (Integer) objeto.getClass().getMethod("getId").invoke(objeto));
            return stmt.executeUpdate() > 0;
        } catch (SQLException | ReflectiveOperationException e) {
            e.printStackTrace();
            return false;
        } finally {
            conn.cerrarConexion();
        }
    }

}

