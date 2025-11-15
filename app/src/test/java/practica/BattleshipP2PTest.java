package practica;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class BattleshipP2PTest {
    
    @Test
    void testIniciar() {
        assertDoesNotThrow(() -> {
            BattleshipP2P juego = new BattleshipP2P();
            assertNotNull(juego, "No debe ser nulo");
        }, "No debe lanzar excepciones al crear una instancia");
    }

    @Test
    void testMain() {
        assertDoesNotThrow(() -> {
            BattleshipP2P juego = new BattleshipP2P();
            assertNotNull(juego);
        }, "La clase debe instanciarse correctamente");
    }
    
    @Test
    void testConstructorCreaBattleshipP2P() {
        BattleshipP2P juego = new BattleshipP2P();
        
        assertNotNull(juego, "El objeto BattleshipP2P no debe ser nulo");
    }
    
    @Test
    void testConstructorInicializaJuego() {
        BattleshipP2P juego = new BattleshipP2P();
        
        try {
            java.lang.reflect.Field campo = BattleshipP2P.class.getDeclaredField("juego");
            campo.setAccessible(true);
            JuegoBattleship juegoInterno = (JuegoBattleship) campo.get(juego);
            assertNotNull(juegoInterno, "El juego interno debe haberse inicializado");
        } catch (Exception e) {
            fail("No se pudo acceder al campo juego: " + e.getMessage());
        }
    }
}
