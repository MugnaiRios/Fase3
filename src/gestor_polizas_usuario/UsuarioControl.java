package gestor_polizas_usuario;

import java.util.List;
import javax.swing.JOptionPane;

public class UsuarioControl {

    protected UsuarioVista vista;
    protected UsuarioDAO dao;
    protected List<Usuario> usuarios;

    public UsuarioControl(UsuarioVista vista) {
        this.vista = vista;
        this.dao = new UsuarioDAO();
        this.eventos();
        this.cargarTabla();
        this.limpiar();
    }

    private void eventos() {
        vista.btnGuardar.addActionListener(e -> guardar());
        vista.btnActualizar.addActionListener(e -> actualizar());
        vista.btnEliminar.addActionListener(e -> eliminar());
        vista.btnLimpiar.addActionListener(e -> limpiar());
        vista.tabla.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                seleccionar();
            }
        });
    }

    private void cargarTabla() {
        usuarios = dao.read();
        vista.modelo.setRowCount(0);
        for (Usuario u : usuarios) {
            vista.modelo.addRow(new Object[]{
                u.getIdUsuario(), u.getNombre(), u.getCorreo(), u.getTelefono(),
                u.getRol(), u.isActivo() ? "Sí" : "No"
            });
        }
    }

    private void guardar() {
        Usuario u = leerFormulario();
        if (u == null) {
            return;
        }
        int r = dao.create(u);
        if (r == UsuarioDAO.OK) {
            mensaje("Usuario registrado.");
            cargarTabla();
            limpiar();
        } else if (r == UsuarioDAO.DUPLICADO) {
            error("Ya existe un usuario con ese correo.");
        } else {
            error("No se pudo registrar el usuario. Revisa la conexión con la base de datos.");
        }
    }

    private void actualizar() {
        if (vista.txtId.getText().isEmpty()) {
            error("Primero selecciona un usuario de la tabla.");
            return;
        }
        Usuario u = leerFormulario();
        if (u == null) {
            return;
        }
        u.setIdUsuario(Integer.parseInt(vista.txtId.getText()));
        int r = dao.update(u);
        if (r == UsuarioDAO.OK) {
            mensaje("Usuario actualizado.");
            cargarTabla();
            limpiar();
        } else if (r == UsuarioDAO.DUPLICADO) {
            error("Ya existe otro usuario con ese correo.");
        } else {
            error("No se pudo actualizar el usuario.");
        }
    }

    private void eliminar() {
        if (vista.txtId.getText().isEmpty()) {
            error("Primero selecciona un usuario de la tabla.");
            return;
        }
        int opcion = JOptionPane.showConfirmDialog(vista,
                "¿Seguro que quieres eliminar a " + vista.txtNombre.getText() + "?",
                "Eliminar usuario", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (opcion != JOptionPane.YES_OPTION) {
            return;
        }
        Usuario u = new Usuario();
        u.setIdUsuario(Integer.parseInt(vista.txtId.getText()));
        int r = dao.delate(u);
        if (r == UsuarioDAO.OK) {
            mensaje("Usuario eliminado.");
            cargarTabla();
            limpiar();
        } else if (r == UsuarioDAO.TIENE_REGISTROS) {
            error("No se puede eliminar: este usuario ya tiene cotizaciones, pólizas o cancelaciones registradas.\n"
                    + "Para que ya no use el sistema, quítale la palomita de \"Activo\" y da clic en Actualizar.");
        } else {
            error("No se pudo eliminar el usuario.");
        }
    }

    private void seleccionar() {
        int fila = vista.tabla.getSelectedRow();
        if (fila < 0 || fila >= usuarios.size()) {
            return;
        }
        Usuario u = usuarios.get(fila);
        vista.txtId.setText(String.valueOf(u.getIdUsuario()));
        vista.txtNombre.setText(u.getNombre());
        vista.txtCorreo.setText(u.getCorreo());
        vista.txtTelefono.setText(u.getTelefono() == null ? "" : u.getTelefono());
        vista.txtContrasena.setText(u.getContrasena());
        vista.cbRol.setSelectedItem(u.getRol());
        vista.chkActivo.setSelected(u.isActivo());
        vista.btnGuardar.setEnabled(false);
        vista.btnActualizar.setEnabled(true);
        vista.btnEliminar.setEnabled(true);
    }

    private void limpiar() {
        vista.txtId.setText("");
        vista.txtNombre.setText("");
        vista.txtCorreo.setText("");
        vista.txtTelefono.setText("");
        vista.txtContrasena.setText("");
        vista.cbRol.setSelectedIndex(0);
        vista.chkActivo.setSelected(true);
        vista.tabla.clearSelection();
        vista.btnGuardar.setEnabled(true);
        vista.btnActualizar.setEnabled(false);
        vista.btnEliminar.setEnabled(false);
        vista.txtNombre.requestFocus();
    }

    private Usuario leerFormulario() {
        String nombre = vista.txtNombre.getText().trim();
        String correo = vista.txtCorreo.getText().trim();
        String telefono = vista.txtTelefono.getText().trim();
        String contrasena = new String(vista.txtContrasena.getPassword());

        if (nombre.isEmpty() || correo.isEmpty() || contrasena.isEmpty()) {
            error("Nombre, correo y contraseña son obligatorios.");
            return null;
        }
        if (!correo.matches("^[\\w.+-]+@[\\w-]+(\\.[\\w-]+)+$")) {
            error("El correo no es válido. Ejemplo: laura@despacho.com");
            return null;
        }
        if (!telefono.isEmpty() && !telefono.matches("\\d{10}")) {
            error("El teléfono debe tener 10 números.");
            return null;
        }
        if (contrasena.length() < 4) {
            error("La contraseña debe tener al menos 4 caracteres.");
            return null;
        }

        Usuario u = new Usuario();
        u.setNombre(nombre);
        u.setCorreo(correo);
        u.setTelefono(telefono.isEmpty() ? null : telefono);
        u.setContrasena(contrasena);
        u.setRol((String) vista.cbRol.getSelectedItem());
        u.setActivo(vista.chkActivo.isSelected());
        return u;
    }

    private void mensaje(String texto) {
        JOptionPane.showMessageDialog(vista, texto, "Usuarios", JOptionPane.INFORMATION_MESSAGE);
    }

    private void error(String texto) {
        JOptionPane.showMessageDialog(vista, texto, "Usuarios", JOptionPane.ERROR_MESSAGE);
    }
}
