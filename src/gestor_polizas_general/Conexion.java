
package gestor_polizas_general;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Conexion {
        //atributos
    public Connection cnx;
    public PreparedStatement ps;
    public ResultSet rs;

       //metodos
    //Aqui checa si tu informacion deberia ser la misma xdd
    //characterEncoding=UTF-8 hace que los acentos (Pérez, García) se guarden y lean bien
    public void conectar() throws SQLException {
        try {
            cnx = DriverManager.getConnection(
                    "jdbc:mysql://localhost/gestor_polizas?useUnicode=true&characterEncoding=UTF-8", "root", "");
        } catch (SQLException ex) {
            System.out.println("Error de conexion: " + ex.getMessage());
            throw ex; // se avisa al DAO para que no intente usar una conexion que no existe
        }

    }

    public void close() {
        try {
            if (rs != null) {
                rs.close();
            }
            if (ps != null) {
                ps.close();
            }
            if (cnx != null) {
                cnx.close();
            }
        } catch (SQLException ex) {
            System.getLogger(Conexion.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        } finally {
            rs = null;
            ps = null;
            cnx = null;
        }
    }
}
