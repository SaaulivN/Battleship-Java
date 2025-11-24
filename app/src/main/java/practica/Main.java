package practica;

import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        // Swing debe iniciarse en el Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            // 1. Crear la Vista (Implementación Swing)
            IVista vistaSwing = new VistaSwing();
            
            // 2. Crear el Controlador e inyectarle la Vista
            BattleshipControlador controlador = new BattleshipControlador(vistaSwing);
            
            // 3. Arrancar la aplicación
            controlador.iniciar();
        });
    }
}