package gestor_polizas_usuario;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

// Vista: solo dibuja la pantalla. Los eventos de los botones estan en UsuarioControl
public class UsuarioVista extends JPanel {

    //campos del formulario
    protected JTextField txtId;
    protected JTextField txtNombre;
    protected JTextField txtCorreo;
    protected JTextField txtTelefono;
    protected JPasswordField txtContrasena;
    protected JComboBox<String> cbRol;
    protected JCheckBox chkActivo;

    //botones
    protected JButton btnGuardar;
    protected JButton btnActualizar;
    protected JButton btnEliminar;
    protected JButton btnLimpiar;

    //tabla
    protected JTable tabla;
    protected DefaultTableModel modelo;

    public UsuarioVista() {
        this.setLayout(new BorderLayout(10, 10));
        this.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel titulo = new JLabel("Gestión de Usuarios");
        titulo.setFont(new Font("Arial", Font.BOLD, 20));
        this.add(titulo, BorderLayout.NORTH);

        JPanel centro = new JPanel(new BorderLayout(10, 10));
        centro.add(formulario(), BorderLayout.NORTH);
        centro.add(tabla(), BorderLayout.CENTER);
        this.add(centro, BorderLayout.CENTER);
    }

    private JPanel formulario() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Datos del usuario"));

        JPanel campos = new JPanel(new GridBagLayout());
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(5, 5, 5, 5);
        g.anchor = GridBagConstraints.WEST;

        txtId = new JTextField(6);
        txtId.setEditable(false); // el id lo pone MySQL (AUTO_INCREMENT)
        txtNombre = new JTextField(20);
        txtCorreo = new JTextField(20);
        txtTelefono = new JTextField(12);
        txtContrasena = new JPasswordField(12);
        cbRol = new JComboBox<>(new String[]{"ADMINISTRADOR", "VENDEDOR", "DESPACHO"});
        chkActivo = new JCheckBox("Activo", true);

        agregar(campos, g, 0, 0, "ID:", txtId);
        agregar(campos, g, 2, 0, "Nombre:", txtNombre);
        agregar(campos, g, 0, 1, "Correo:", txtCorreo);
        agregar(campos, g, 2, 1, "Teléfono:", txtTelefono);
        agregar(campos, g, 0, 2, "Contraseña:", txtContrasena);
        agregar(campos, g, 2, 2, "Rol:", cbRol);
        g.gridx = 1;
        g.gridy = 3;
        campos.add(chkActivo, g);

        JPanel botones = new JPanel(new FlowLayout(FlowLayout.LEFT));
        btnGuardar = new JButton("Guardar");
        btnActualizar = new JButton("Actualizar");
        btnEliminar = new JButton("Eliminar");
        btnLimpiar = new JButton("Limpiar");
        btnEliminar.setForeground(new Color(170, 0, 0));
        botones.add(btnGuardar);
        botones.add(btnActualizar);
        botones.add(btnEliminar);
        botones.add(btnLimpiar);

        panel.add(campos, BorderLayout.CENTER);
        panel.add(botones, BorderLayout.SOUTH);
        return panel;
    }

    private void agregar(JPanel panel, GridBagConstraints g, int x, int y, String texto, java.awt.Component campo) {
        g.gridx = x;
        g.gridy = y;
        panel.add(new JLabel(texto), g);
        g.gridx = x + 1;
        panel.add(campo, g);
    }

    private JScrollPane tabla() {
        // La contraseña no se muestra en la tabla
        modelo = new DefaultTableModel(new Object[]{"ID", "Nombre", "Correo", "Teléfono", "Rol", "Activo"}, 0) {
            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false; // la tabla es solo para ver; se edita en el formulario
            }
        };
        tabla = new JTable(modelo);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla.setRowHeight(24);
        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBorder(BorderFactory.createTitledBorder("Usuarios registrados (da clic en uno para editarlo)"));
        return scroll;
    }
}
