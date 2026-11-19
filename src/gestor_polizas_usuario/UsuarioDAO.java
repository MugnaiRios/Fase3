package gestor_polizas_usuario;

import gestor_polizas_general.Conexion;
import gestor_polizas_general.DAO;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

// DAO: aqui van las consultas SQL de la tabla usuario (Create, Read, Update, Delete)
public class UsuarioDAO extends Conexion implements DAO {

    // Codigos que regresan los metodos para que el controlador sepa que paso
    public static final int OK = 1;
    public static final int ERROR = 0;
    public static final int DUPLICADO = -1;      // correo repetido (UNIQUE)
    public static final int TIENE_REGISTROS = -2; // tiene cotizaciones, polizas o cancelaciones (FK)

    //CREATE: inserta un usuario nuevo
    @Override
    public int create(Object obj) {
        Usuario u = (Usuario) obj;
        String sql = "INSERT INTO usuario (nombre, correo, telefono, contrasena, rol, activo) VALUES (?, ?, ?, ?, ?, ?)";
        try {
            conectar();
            ps = cnx.prepareStatement(sql);
            ps.setString(1, u.getNombre());
            ps.setString(2, u.getCorreo());
            ps.setString(3, u.getTelefono());
            ps.setString(4, u.getContrasena());
            ps.setString(5, u.getRol());
            ps.setBoolean(6, u.isActivo());
            ps.executeUpdate();
            return OK;
        } catch (SQLIntegrityConstraintViolationException ex) {
            return DUPLICADO;
        } catch (SQLException ex) {
            System.out.println("Error al registrar usuario: " + ex.getMessage());
            return ERROR;
        } finally {
            close();
        }
    }

    //READ: regresa todos los usuarios
    @Override
    public List read() {
        List<Usuario> lista = new ArrayList<>();
        String sql = "SELECT idUsuario, nombre, correo, telefono, contrasena, rol, activo FROM usuario ORDER BY idUsuario";
        try {
            conectar();
            ps = cnx.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(new Usuario(
                        rs.getInt("idUsuario"),
                        rs.getString("nombre"),
                        rs.getString("correo"),
                        rs.getString("telefono"),
                        rs.getString("contrasena"),
                        rs.getString("rol"),
                        rs.getBoolean("activo")));
            }
        } catch (SQLException ex) {
            System.out.println("Error al consultar usuarios: " + ex.getMessage());
        } finally {
            close();
        }
        return lista;
    }

    //UPDATE: modifica un usuario existente (se busca por idUsuario)
    @Override
    public int update(Object obj) {
        Usuario u = (Usuario) obj;
        String sql = "UPDATE usuario SET nombre = ?, correo = ?, telefono = ?, contrasena = ?, rol = ?, activo = ? WHERE idUsuario = ?";
        try {
            conectar();
            ps = cnx.prepareStatement(sql);
            ps.setString(1, u.getNombre());
            ps.setString(2, u.getCorreo());
            ps.setString(3, u.getTelefono());
            ps.setString(4, u.getContrasena());
            ps.setString(5, u.getRol());
            ps.setBoolean(6, u.isActivo());
            ps.setInt(7, u.getIdUsuario());
            return ps.executeUpdate() > 0 ? OK : ERROR;
        } catch (SQLIntegrityConstraintViolationException ex) {
            return DUPLICADO;
        } catch (SQLException ex) {
            System.out.println("Error al actualizar usuario: " + ex.getMessage());
            return ERROR;
        } finally {
            close();
        }
    }

    //DELETE: elimina un usuario. Si ya tiene registros ligados, MySQL no lo deja (ON DELETE RESTRICT)
    @Override
    public int delate(Object obj) {
        Usuario u = (Usuario) obj;
        String sql = "DELETE FROM usuario WHERE idUsuario = ?";
        try {
            conectar();
            ps = cnx.prepareStatement(sql);
            ps.setInt(1, u.getIdUsuario());
            return ps.executeUpdate() > 0 ? OK : ERROR;
        } catch (SQLIntegrityConstraintViolationException ex) {
            return TIENE_REGISTROS;
        } catch (SQLException ex) {
            System.out.println("Error al eliminar usuario: " + ex.getMessage());
            return ERROR;
        } finally {
            close();
        }
    }
}
