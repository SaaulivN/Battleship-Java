package practica;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ProtocoloBattleshipTest {
    
    @Test
    void testConstruirMensajeDisparo() {
        String mensaje = ProtocoloBattleship.construirMensajeDisparo(3, 5);
        
        assertEquals("DISPARAR|3,5", mensaje, 
            "El mensaje de disparo debe tener el formato correcto");
    }
    
    @Test
    void testConstruirMensajeDisparoConCerosCeros() {
        String mensaje = ProtocoloBattleship.construirMensajeDisparo(0, 0);
        
        assertEquals("DISPARAR|0,0", mensaje, 
            "El mensaje debe funcionar con coordenadas 0,0");
    }
    
    @Test
    void testConstruirMensajeDisparoEnEsquinaSuperior() {
        String mensaje = ProtocoloBattleship.construirMensajeDisparo(9, 9);
        
        assertEquals("DISPARAR|9,9", mensaje, 
            "El mensaje debe funcionar con coordenadas máximas");
    }

    @Test
    void testConstruirMensajeResultado() {
        String mensaje = ProtocoloBattleship.construirMensajeResultado(
            ProtocoloBattleship.IMPACTO, 2, 4, null);
        
        assertEquals("IMPACTO|2,4", mensaje, 
            "El mensaje de resultado sin tipo debe tener el formato correcto");
    }
    
    @Test
    void testConstruirMensajeResultadoConTipoBarco() {
        String mensaje = ProtocoloBattleship.construirMensajeResultado(
            ProtocoloBattleship.HUNDIDO, 1, 3, "PORTAAVIONES");
        
        assertEquals("HUNDIDO|1,3|PORTAAVIONES", mensaje, 
            "El mensaje de resultado con tipo debe incluir el nombre del barco");
    }
    
    @Test
    void testConstruirMensajeResultadoFallo() {
        String mensaje = ProtocoloBattleship.construirMensajeResultado(
            ProtocoloBattleship.FALLO, 7, 8, null);
        
        assertEquals("FALLO|7,8", mensaje, 
            "El mensaje de fallo debe tener el formato correcto");
    }

    @Test
    void testParsearMensajeDisparo() {
        String mensaje = "DISPARAR|3,5";
        ProtocoloBattleship.Mensaje resultado = ProtocoloBattleship.parsearMensaje(mensaje);
        
        assertEquals(ProtocoloBattleship.DISPARAR, resultado.comando, 
            "El comando debe ser DISPARAR");
        assertEquals(3, resultado.x, "La coordenada x debe ser 3");
        assertEquals(5, resultado.y, "La coordenada y debe ser 5");
        assertNull(resultado.tipoBarco, "No debe haber tipo de barco en un disparo");
    }
    
    @Test
    void testParsearMensajeImpacto() {
        String mensaje = "IMPACTO|2,4";
        ProtocoloBattleship.Mensaje resultado = ProtocoloBattleship.parsearMensaje(mensaje);
        
        assertEquals(ProtocoloBattleship.IMPACTO, resultado.comando, 
            "El comando debe ser IMPACTO");
        assertEquals(2, resultado.x, "La coordenada x debe ser 2");
        assertEquals(4, resultado.y, "La coordenada y debe ser 4");
    }
    
    @Test
    void testParsearMensajeHundido() {
        String mensaje = "HUNDIDO|1,3|PORTAAVIONES";
        ProtocoloBattleship.Mensaje resultado = ProtocoloBattleship.parsearMensaje(mensaje);
        
        assertEquals(ProtocoloBattleship.HUNDIDO, resultado.comando, 
            "El comando debe ser HUNDIDO");
        assertEquals(1, resultado.x, "La coordenada x debe ser 1");
        assertEquals(3, resultado.y, "La coordenada y debe ser 3");
        assertEquals("PORTAAVIONES", resultado.tipoBarco, 
            "Debe extraer el tipo de barco correctamente");
    }
    
    @Test
    void testParsearMensajeFallo() {
        String mensaje = "FALLO|7,8";
        ProtocoloBattleship.Mensaje resultado = ProtocoloBattleship.parsearMensaje(mensaje);
        
        assertEquals(ProtocoloBattleship.FALLO, resultado.comando, 
            "El comando debe ser FALLO");
        assertEquals(7, resultado.x, "La coordenada x debe ser 7");
        assertEquals(8, resultado.y, "La coordenada y debe ser 8");
    }
    
    @Test
    void testParsearMensajeSimple() {
        String mensaje = "LISTO";
        ProtocoloBattleship.Mensaje resultado = ProtocoloBattleship.parsearMensaje(mensaje);
        
        assertEquals(ProtocoloBattleship.LISTO, resultado.comando, 
            "Debe parsear comandos simples sin coordenadas");
    }
    
    @Test
    void testParsearMensajeNulo_LanzaExcepcion() {
        assertThrows(IllegalArgumentException.class, () -> {
            ProtocoloBattleship.parsearMensaje(null);
        }, "No debe permitir mensajes nulos");
    }
    
    @Test
    void testParsearMensajeVacio_LanzaExcepcion() {
        assertThrows(IllegalArgumentException.class, () -> {
            ProtocoloBattleship.parsearMensaje("");
        }, "No debe permitir mensajes vacíos");
    }
    
    @Test
    void testParsearMensajeConFormatoInvalido_LanzaExcepcion() {
        assertThrows(IllegalArgumentException.class, () -> {
            ProtocoloBattleship.parsearMensaje("DISPARAR|abc,def");
        }, "No debe permitir coordenadas no numéricas");
    }
    
    @Test
    void testParsearMensajeConCoordenadaIncompleta_LanzaExcepcion() {
        assertThrows(IllegalArgumentException.class, () -> {
            ProtocoloBattleship.parsearMensaje("DISPARAR|3");
        }, "No debe permitir coordenadas incompletas");
    }
    
    @Test
    void testRedondaCompletoMensajeDisparo() {
        String mensajeOriginal = ProtocoloBattleship.construirMensajeDisparo(4, 6);
        ProtocoloBattleship.Mensaje resultado = ProtocoloBattleship.parsearMensaje(mensajeOriginal);
        
        assertEquals(ProtocoloBattleship.DISPARAR, resultado.comando);
        assertEquals(4, resultado.x);
        assertEquals(6, resultado.y);
    }
    
    @Test
    void testRedondaCompletoMensajeResultado() {
        String mensajeOriginal = ProtocoloBattleship.construirMensajeResultado(
            ProtocoloBattleship.HUNDIDO, 2, 5, "DESTRUCTOR");
        ProtocoloBattleship.Mensaje resultado = ProtocoloBattleship.parsearMensaje(mensajeOriginal);
        
        assertEquals(ProtocoloBattleship.HUNDIDO, resultado.comando);
        assertEquals(2, resultado.x);
        assertEquals(5, resultado.y);
        assertEquals("DESTRUCTOR", resultado.tipoBarco);
    }
}
