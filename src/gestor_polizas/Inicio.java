package gestor_polizas;

import gestor_polizas_general.*;
import javax.swing.SwingUtilities;

public class Inicio {
    //Atributods

    private Bienvenida vista;
    private Controlador controlador;

    //consttructor
    Inicio() {
        //Bienvenida instancia
        vista = new Bienvenida();
        controlador = new Controlador(vista);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Inicio::new);
    }
}
