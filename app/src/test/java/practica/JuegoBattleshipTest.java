package practica;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class JuegoBattleshipTest {
    
    private JuegoBattleship juego;
    
    @BeforeEach
    void setUp() {
        juego = new JuegoBattleship();
    }
    
    @Test
    void testColocarBarcosAutomaticamente() {
        juego.colocarBarcosAutomaticamente();
        
        char[][] tablero = obtenerTableroPropio();
        int conteoP = 0, conteoA = 0, conteoC = 0, conteoS = 0, conteoD = 0;
        
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                switch (tablero[i][j]) {
                    case 'P': conteoP++; break;
                    case 'A': conteoA++; break;
                    case 'C': conteoC++; break;
                    case 'S': conteoS++; break;
                    case 'D': conteoD++; break;
                }
            }
        }
        
        assertEquals(5, conteoP, "Debe haber 5 casillas de PORTAAVIONES");
        assertEquals(4, conteoA, "Debe haber 4 casillas de ACORAZADO");
        assertEquals(3, conteoC, "Debe haber 3 casillas de CRUCERO");
        assertEquals(3, conteoS, "Debe haber 3 casillas de SUBMARINO");
        assertEquals(2, conteoD, "Debe haber 2 casillas de DESTRUCTOR");
    }

    @Test
    void testRecibirDisparo() {
        juego.colocarBarcosAutomaticamente();
        
        boolean disparoEnAgua = false;
        for (int i = 0; i < 10 && !disparoEnAgua; i++) {
            for (int j = 0; j < 10 && !disparoEnAgua; j++) {
                boolean resultado = juego.recibirDisparo(i, j);
                if (!resultado) {
                    disparoEnAgua = true;
                }
            }
        }
        
        assertTrue(disparoEnAgua, "Al menos uno de los disparos debe fallar en agua");
    }

    @Test
    void testRecibirDisparoEnBarco() {
        juego.colocarBarcosAutomaticamente();
        
        boolean disparoEnBarco = false;
        for (int i = 0; i < 10 && !disparoEnBarco; i++) {
            for (int j = 0; j < 10 && !disparoEnBarco; j++) {
                boolean resultado = juego.recibirDisparo(i, j);
                if (resultado) {
                    disparoEnBarco = true;
                }
            }
        }
        
        assertTrue(disparoEnBarco, "Al menos un disparo debe impactar en un barco");
    }

    @Test
    void testRegistrarImpacto() {
        juego.registrarImpacto(3, 5);
        
        assertTrue(juego.yaDisparado(3, 5), 
            "La posición debe quedar registrada como disparada después de impacto");
    }

    @Test
    void testRegistrarFallo() {
        juego.registrarFallo(2, 4);
        
        assertTrue(juego.yaDisparado(2, 4), 
            "La posición debe quedar registrada como disparada después de fallo");
    }

    @Test
    void testYaDisparado() {
        juego.registrarImpacto(1, 1);
        juego.registrarFallo(2, 2);
        
        assertTrue(juego.yaDisparado(1, 1), "Posición de impacto debe estar registrada");
        assertTrue(juego.yaDisparado(2, 2), "Posición de fallo debe estar registrada");
        assertFalse(juego.yaDisparado(3, 3), "Posición no disparada no debe estar registrada");
    }

    @Test
    void testEstaBarcoHundido() {
        juego.colocarBarcosAutomaticamente();
        
        assertFalse(juego.estaBarcoHundido("DESTRUCTOR"), 
            "DESTRUCTOR no debe estar hundido al inicio");
        assertFalse(juego.estaBarcoHundido("PORTAAVIONES"), 
            "PORTAAVIONES no debe estar hundido al inicio");
    }

    @Test
    void testTodosBarcosHundidos() {
        juego.colocarBarcosAutomaticamente();
        
        assertFalse(juego.todosBarcosHundidos(), 
            "No todos los barcos deben estar hundidos al inicio");
    }

    @Test
    void testObtenerTipoBarcoEn() {
        juego.colocarBarcosAutomaticamente();
        
        String tipoBarco = juego.obtenerTipoBarcoEn(0, 0);
        
        assertNotNull(tipoBarco, "Siempre debe retornar un tipo de barco");
        assertTrue(tipoBarco.equals("DESCONOCIDO") || 
                   tipoBarco.equals("PORTAAVIONES") || 
                   tipoBarco.equals("ACORAZADO") ||
                   tipoBarco.equals("CRUCERO") ||
                   tipoBarco.equals("SUBMARINO") ||
                   tipoBarco.equals("DESTRUCTOR"),
                   "El tipo de barco debe ser válido");
    }

    @Test
    void testMostrarTableroPropio() {
        juego.colocarBarcosAutomaticamente();
        
        assertDoesNotThrow(() -> juego.mostrarTableroPropio(), 
            "mostrarTableroPropio no debe lanzar excepciones");
    }

    @Test
    void testMostrarTableroEnemigo() {
        assertDoesNotThrow(() -> juego.mostrarTableroEnemigo(), 
            "mostrarTableroEnemigo no debe lanzar excepciones");
    }

    private char[][] obtenerTableroPropio() {
        try {
            java.lang.reflect.Field campo = JuegoBattleship.class.getDeclaredField("tableroPropio");
            campo.setAccessible(true);
            return (char[][]) campo.get(juego);
        } catch (Exception e) {
            fail("No se pudo acceder al tablero propio: " + e.getMessage());
            return null;
        }
    }
}
