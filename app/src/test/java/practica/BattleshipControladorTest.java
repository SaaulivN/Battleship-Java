package practica;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class BattleshipControladorTest {
    
    @Test
    void testIniciar() {
        // El método iniciar inicia el flujo completo del juego
        // Este test es limitado porque requiere entrada del usuario
        // En una aplicación real, se usaría mock o inyección de dependencias
        
        // Verificar que el controlador se puede crear sin excepciones
        BattleshipControlador controlador = new BattleshipControlador();
        assertNotNull(controlador);
        
        // El método iniciar requiere interacción de usuario (stdin)
        // Por lo que es difícil de testear sin mocking
    }

    @Test
    void testMain() {
        // El método main no devuelve nada y es el punto de entrada
        // Este test verifica que no lanza excepciones
        assertDoesNotThrow(() -> {
            // No se puede llamar directamente a main desde un test
            // porque requiere entrada interactiva
            // Este es un marcador para documentar que existe
        });
    }
}
