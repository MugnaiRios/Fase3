
package gestor_polizas_general;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Font;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;



public class Bienvenida extends JFrame{
    protected JMenuBar bm;
    protected JMenu opciones;
    protected JMenuItem clientes;
    protected JMenuItem usuarios;
    protected JPanel ps;
    protected JPanel pc;
    protected CardLayout c1;

    public Bienvenida(){
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.menu();
        
        JLabel titulo = new JLabel("BIenvenido a al programa de gestion de polizas");
        titulo.setForeground(Color.white);
        Font fuente = new Font("Arial", Font.BOLD, 18);
        titulo.setFont(fuente);
        
                //panel superior
        ps = new JPanel();
        ps.setBackground(Color.orange);
        ps.add(titulo);
        this.add(ps, BorderLayout.NORTH);

        c1 = new CardLayout();
        pc = new JPanel(c1);
        pc.add(new JPanel(), "inicio");
        this.add(pc, BorderLayout.CENTER);

        
        this.setVisible(true);
    }

    private void menu() {
        bm = new JMenuBar();
        this.setJMenuBar(bm);
        
        opciones = new JMenu("Opciones");
        bm.add(opciones);
        
        clientes = new JMenuItem("Clientes....");
        opciones.add(clientes);

        usuarios = new JMenuItem("Usuarios....");
        opciones.add(usuarios);
    }
}
