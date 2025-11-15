package practica;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Map;

public class JuegoBattleshipTest {
    
    private JuegoBattleship juego;
    
    @BeforeEach
    void setUp() {
        juego = new JuegoBattleship();
    }

    @Test
    void testColocarBarcosAutomaticamente() {
        // Los barcos no deben estar colocados inicialmente
        char[][] tablero = juego.getTableroPropio();
        int contadorBarcos = 0;
        
        // Contar posiciones sin agua antes de colocar
        for (int i = 0; i < JuegoBattleship.TAMANIO_TABLERO; i++) {
            for (int j = 0; j < JuegoBattleship.TAMANIO_TABLERO; j++) {
                if (tablero[i][j] != '~') contadorBarcos++;
            }
        }
        assertEquals(0, contadorBarcos);
        
        // Colocar barcos
        juego.colocarBarcosAutomaticamente();
        
        // Ahora debe haber barcos (5+4+3+3+2 = 17 casillas ocupadas)
        contadorBarcos = 0;
        tablero = juego.getTableroPropio();
        for (int i = 0; i < JuegoBattleship.TAMANIO_TABLERO; i++) {
            for (int j = 0; j < JuegoBattleship.TAMANIO_TABLERO; j++) {
                if (tablero[i][j] != '~') contadorBarcos++;
            }
        }
        assertEquals(17, contadorBarcos);
    }

    @Test
    void testEstaBarcoHundido() {
        juego.colocarBarcosAutomaticamente();
        
        // Inicialmente ningún barco está hundido
        assertFalse(juego.estaBarcoHundido("PORTAAVIONES"));
        
        // Simular impactos para hundir un barco de 2 casillas
        // Incrementar impactos manualmente (de forma privada, usamos recibirDisparo)
        // Para esto es mejor usar el mapa de impactos indirectamente
        assertFalse(juego.estaBarcoHundido("DESTRUCTOR"));
    }

    @Test
    void testGetBarcos() {
        Map<String, Integer> barcos = juego.getBarcos();
        
        // Verificar que existen todos los barcos
        assertEquals(5, barcos.get("PORTAAVIONES"));
        assertEquals(4, barcos.get("ACORAZADO"));
        assertEquals(3, barcos.get("CRUCERO"));
        assertEquals(3, barcos.get("SUBMARINO"));
        assertEquals(2, barcos.get("DESTRUCTOR"));
    }

    @Test
    void testGetImpactosPorBarco() {
        Map<String, Integer> impactos = juego.getImpactosPorBarco();
        
        // Inicialmente todos los barcos tienen 0 impactos
        assertEquals(0, impactos.get("PORTAAVIONES"));
        assertEquals(0, impactos.get("ACORAZADO"));
        assertEquals(0, impactos.get("CRUCERO"));
        assertEquals(0, impactos.get("SUBMARINO"));
        assertEquals(0, impactos.get("DESTRUCTOR"));
    }

    @Test
    void testGetTableroEnemigo() {
        char[][] tableroEnemigo = juego.getTableroEnemigo();
        
        // Inicialmente todo debe ser desconocido
        assertEquals(JuegoBattleship.TAMANIO_TABLERO, tableroEnemigo.length);
        for (int i = 0; i < JuegoBattleship.TAMANIO_TABLERO; i++) {
            for (int j = 0; j < JuegoBattleship.TAMANIO_TABLERO; j++) {
                assertEquals('?', tableroEnemigo[i][j]);
            }
        }
    }

    @Test
    void testGetTableroPropio() {
        char[][] tableroPropio = juego.getTableroPropio();
        
        // Inicialmente todo debe ser agua
        assertEquals(JuegoBattleship.TAMANIO_TABLERO, tableroPropio.length);
        for (int i = 0; i < JuegoBattleship.TAMANIO_TABLERO; i++) {
            for (int j = 0; j < JuegoBattleship.TAMANIO_TABLERO; j++) {
                assertEquals('~', tableroPropio[i][j]);
            }
        }
    }

    @Test
    void testObtenerTipoBarcoEn() {
        // Después de colocar barcos, verificar tipos
        juego.colocarBarcosAutomaticamente();
        
        // Este test es débil porque los barcos se colocan aleatoriamente
        // Pero podemos verificar que devuelve un tipo válido
        char[][] tablero = juego.getTableroPropio();
        String tipoBarco = juego.obtenerTipoBarcoEn(0, 0);
        
        // Puede ser un tipo válido o DESCONOCIDO si no hay barco en (0,0)
        assertTrue(tipoBarco.equals("PORTAAVIONES") || 
                   tipoBarco.equals("ACORAZADO") ||
                   tipoBarco.equals("CRUCERO") ||
                   tipoBarco.equals("SUBMARINO") ||
                   tipoBarco.equals("DESTRUCTOR") ||
                   tipoBarco.equals("DESCONOCIDO"));
    }

    @Test
    void testRecibirDisparo() {
        juego.colocarBarcosAutomaticamente();
        
        // Disparar a (9,9) que casi siempre será agua
        // Si falla es agua, debe devolver false y marcar 'O'
        // Si es barco, devuelve true y marca 'X'
        boolean resultado = juego.recibirDisparo(9, 9);
        char celda = juego.getTableroPropio()[9][9];
        
        if (resultado) {
            // Fue barco
            assertEquals('X', celda);
        } else {
            // Fue agua
            assertEquals('O', celda);
        }
        
        // Disparar a la misma posición nuevamente debe devolver false
        boolean resultado2 = juego.recibirDisparo(9, 9);
        assertFalse(resultado2);
    }

    @Test
    void testRegistrarFallo() {
        juego.registrarFallo(2, 3);
        
        // Verificar que la posición se marcó como fallo en tablero enemigo
        assertEquals('O', juego.getTableroEnemigo()[2][3]);
        
        // Verificar que la posición se registró como disparada
        assertTrue(juego.yaDisparado(2, 3));
    }

    @Test
    void testRegistrarImpacto() {
        juego.registrarImpacto(5, 5);
        
        // Verificar que la posición se marcó como impacto en tablero enemigo
        assertEquals('X', juego.getTableroEnemigo()[5][5]);
        
        // Verificar que la posición se registró como disparada
        assertTrue(juego.yaDisparado(5, 5));
    }

    @Test
    void testTodosBarcosHundidos() {
        juego.colocarBarcosAutomaticamente();
        
        // Inicialmente no todos están hundidos
        assertFalse(juego.todosBarcosHundidos());
    }

    @Test
    void testYaDisparado() {
        // Inicialmente no se ha disparado a ninguna posición
        assertFalse(juego.yaDisparado(0, 0));
        assertFalse(juego.yaDisparado(9, 9));
        
        // Registrar un disparo
        juego.registrarFallo(3, 4);
        
        // Verificar que la posición se recordó
        assertTrue(juego.yaDisparado(3, 4));
        
        // Otras posiciones no deben estar disparadas
        assertFalse(juego.yaDisparado(3, 5));
    }
}
