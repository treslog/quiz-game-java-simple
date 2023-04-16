import javax.swing.JOptionPane;

public class Main {
    public static void main(String[] args) throws Exception {

        String respuesta = "";
        String resultado = Juego.jugar("inicio");

        while (true) {
            respuesta = JOptionPane.showInputDialog(resultado);
            resultado = Juego.jugar(respuesta);
            if (respuesta == null || respuesta.isEmpty()) {
                break;
            }
        }

    }
}