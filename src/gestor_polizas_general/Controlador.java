
package gestor_polizas_general;

import gestor_polizas_usuario.UsuarioControl;
import gestor_polizas_usuario.UsuarioVista;

public class Controlador {
    protected Bienvenida vista;
    protected UsuarioVista usuariosVista;
    public Controlador(Bienvenida vista) {
        this.vista = vista;
        this.eventos();
    }

    private void eventos() {
      vista.clientes.addActionListener(e->{
      //Aqui instancia de que, clientes vista y clientes y marcas controler ejemplo :
       //     this.mascasVista = new MarcasVista();
         //   new MarcasControl(mascasVista);
           // vista.pc.add(mascasVista, "mk");
           // vista.c1.show(vista.pc, "mk");
      });

      vista.usuarios.addActionListener(e -> {
          if (usuariosVista == null) {
              usuariosVista = new UsuarioVista();
              new UsuarioControl(usuariosVista);
              vista.pc.add(usuariosVista, "usuarios");
          }
          vista.c1.show(vista.pc, "usuarios");
      });
    }
}
