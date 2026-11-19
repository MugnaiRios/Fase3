
package gestor_polizas_general;

public class Controlador {
    protected Bienvenida vista;
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
    }
}
