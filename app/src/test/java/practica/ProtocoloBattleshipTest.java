package practica;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ProtocoloBattleshipTest {
    
    @Test
    void testConstruirMensajeDisparo() {
        // Debe construir un mensaje DISPARAR con coordenadas correctas
        String mensaje = ProtocoloBattleship.construirMensajeDisparo(3, 5);
        assertEquals("DISPARAR|3,5", mensaje);
        
        // Prueba con coordenadas en los bordes
        String mensaje2 = ProtocoloBattleship.construirMensajeDisparo(0, 9);
        assertEquals("DISPARAR|0,9", mensaje2);
    }

    @Test
    void testConstruirMensajeResultado() {
        // Construir mensaje de impacto con tipo de barco
        String mensaje = ProtocoloBattleship.construirMensajeResultado(
            ProtocoloBattleship.IMPACTO, 2, 7, "PORTAAVIONES");
        assertEquals("IMPACTO|2,7|PORTAAVIONES", mensaje);
        
        // Construir mensaje sin tipo de barco
        String mensaje2 = ProtocoloBattleship.construirMensajeResultado(
            ProtocoloBattleship.FALLO, 1, 1, null);
        assertEquals("FALLO|1,1", mensaje2);
    }

    @Test
    void testParsearMensajeDisparo() {
        // Parsear mensaje DISPARAR
        ProtocoloBattleship.Mensaje msg = ProtocoloBattleship.parsearMensaje("DISPARAR|4,6");
        assertEquals("DISPARAR", msg.comando);
        assertEquals(4, msg.x);
        assertEquals(6, msg.y);
        assertNull(msg.tipoBarco);
    }
    
    @Test
    void testParsearMensajeImpacto() {
        // Parsear mensaje IMPACTO con tipo de barco
        ProtocoloBattleship.Mensaje msg = ProtocoloBattleship.parsearMensaje("IMPACTO|3,5|CRUCERO");
        assertEquals("IMPACTO", msg.comando);
        assertEquals(3, msg.x);
        assertEquals(5, msg.y);
        assertEquals("CRUCERO", msg.tipoBarco);
    }
    
    @Test
    void testParsearMensajeFallo() {
        // Parsear mensaje FALLO sin tipo de barco
        ProtocoloBattleship.Mensaje msg = ProtocoloBattleship.parsearMensaje("FALLO|2,8");
        assertEquals("FALLO", msg.comando);
        assertEquals(2, msg.x);
        assertEquals(8, msg.y);
        assertNull(msg.tipoBarco);
    }
    
    @Test
    void testParsearMensajeHundido() {
        // Parsear mensaje HUNDIDO
        ProtocoloBattleship.Mensaje msg = ProtocoloBattleship.parsearMensaje("HUNDIDO|1,9|ACORAZADO");
        assertEquals("HUNDIDO", msg.comando);
        assertEquals(1, msg.x);
        assertEquals(9, msg.y);
        assertEquals("ACORAZADO", msg.tipoBarco);
    }
    
    @Test
    void testParsearMensajeComandoSimple() {
        // Parsear comando sin parámetros
        ProtocoloBattleship.Mensaje msg = ProtocoloBattleship.parsearMensaje("LISTO");
        assertEquals("LISTO", msg.comando);
        assertEquals(-1, msg.x);
        assertEquals(-1, msg.y);
        assertNull(msg.tipoBarco);
    }
    
    @Test
    void testParsearMensajeNulo() {
        // Debe lanzar excepción para mensaje nulo
        assertThrows(IllegalArgumentException.class, () -> {
            ProtocoloBattleship.parsearMensaje(null);
        });
    }
    
    @Test
    void testParsearMensajeVacio() {
        // Debe lanzar excepción para mensaje vacío
        assertThrows(IllegalArgumentException.class, () -> {
            ProtocoloBattleship.parsearMensaje("");
        });
    }
    
    @Test
    void testParsearMensajeFormatoInvalido() {
        // Debe lanzar excepción para formato inválido
        assertThrows(IllegalArgumentException.class, () -> {
            ProtocoloBattleship.parsearMensaje("DISPARAR|abc,def");
        });
    }
}
