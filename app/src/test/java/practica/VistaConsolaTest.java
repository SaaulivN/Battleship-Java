package practica;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;

public class VistaConsolaTest {
    
    private PrintStream originalOut = System.out;
    private PrintStream originalErr = System.err;

    @Test
    void testCerrar() {
        // Debe cerrar sin lanzar excepciones
        VistaConsola vista = new VistaConsola();
        assertDoesNotThrow(() -> vista.cerrar());
    }

    @Test
    void testElegirModo() {
        // Simular entrada "1"
        String input = "1\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        
        VistaConsola vistaTest = new VistaConsola();
        int modo = vistaTest.elegirModo();
        assertEquals(1, modo);
        vistaTest.cerrar();
    }
    
    @Test
    void testElegirModoOpcion2() {
        // Simular entrada "2"
        String input = "2\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        
        VistaConsola vistaTest = new VistaConsola();
        int modo = vistaTest.elegirModo();
        assertEquals(2, modo);
        vistaTest.cerrar();
    }

    @Test
    void testMostrarBienvenida() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
        
        VistaConsola vista = new VistaConsola();
        vista.mostrarBienvenida();
        
        String output = outputStream.toString();
        System.setOut(originalOut);
        
        assertTrue(output.contains("BATTLESHIP"));
        vista.cerrar();
    }

    @Test
    void testMostrarError() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setErr(new PrintStream(outputStream));
        
        VistaConsola vista = new VistaConsola();
        vista.mostrarError("Error de prueba");
        
        String output = outputStream.toString();
        System.setErr(originalErr);
        
        assertTrue(output.contains("Error de prueba"));
        vista.cerrar();
    }

    @Test
    void testMostrarEstadoBarcos() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
        
        VistaConsola vista = new VistaConsola();
        Map<String, Integer> barcos = new HashMap<>();
        barcos.put("PORTAAVIONES", 5);
        barcos.put("DESTRUCTOR", 2);
        
        Map<String, Integer> impactos = new HashMap<>();
        impactos.put("PORTAAVIONES", 2);
        impactos.put("DESTRUCTOR", 2);
        
        vista.mostrarEstadoBarcos(barcos, impactos);
        
        String output = outputStream.toString();
        System.setOut(originalOut);
        
        assertTrue(output.contains("PORTAAVIONES") && output.contains("DESTRUCTOR"));
        vista.cerrar();
    }

    @Test
    void testMostrarMensaje() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
        
        VistaConsola vista = new VistaConsola();
        vista.mostrarMensaje("Mensaje de prueba");
        
        String output = outputStream.toString();
        System.setOut(originalOut);
        
        assertTrue(output.contains("Mensaje de prueba"));
        vista.cerrar();
    }

    @Test
    void testMostrarTablero() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
        
        VistaConsola vista = new VistaConsola();
        char[][] tablero = new char[10][10];
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                tablero[i][j] = '~';
            }
        }
        
        vista.mostrarTablero(tablero, "TABLERO PRUEBA");
        
        String output = outputStream.toString();
        System.setOut(originalOut);
        
        assertTrue(output.contains("TABLERO PRUEBA"));
        vista.cerrar();
    }

    @Test
    void testObtenerDisparo() {
        // Simular entrada "3,5"
        String input = "3,5\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        
        VistaConsola vistaTest = new VistaConsola();
        int[] disparo = vistaTest.obtenerDisparo((x, y) -> false); // No hay disparos previos
        assertEquals(3, disparo[0]);
        assertEquals(5, disparo[1]);
        vistaTest.cerrar();
    }
    
    @Test
    void testObtenerDisparoCoordenadasFuera() {
        // Simular entrada inválida primero, luego válida
        String input = "15,20\n2,2\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        
        VistaConsola vistaTest = new VistaConsola();
        int[] disparo = vistaTest.obtenerDisparo((x, y) -> false);
        assertEquals(2, disparo[0]);
        assertEquals(2, disparo[1]);
        vistaTest.cerrar();
    }
    
    @Test
    void testObtenerDisparoYaDisparado() {
        // Simular intento a posición ya disparada, luego válida
        String input = "1,1\n4,4\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        
        VistaConsola vistaTest = new VistaConsola();
        // BiPredicate devuelve true para (1,1) - ya disparado
        int[] disparo = vistaTest.obtenerDisparo((x, y) -> x == 1 && y == 1);
        assertEquals(4, disparo[0]);
        assertEquals(4, disparo[1]);
        vistaTest.cerrar();
    }

    @Test
    void testObtenerIPServidor() {
        String input = "192.168.1.100\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        
        VistaConsola vistaTest = new VistaConsola();
        String ip = vistaTest.obtenerIPServidor();
        assertEquals("192.168.1.100", ip);
        vistaTest.cerrar();
    }

    @Test
    void testObtenerNombreJugador() {
        String input = "JugadorTest\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        
        VistaConsola vistaTest = new VistaConsola();
        String nombre = vistaTest.obtenerNombreJugador();
        assertEquals("JugadorTest", nombre);
        vistaTest.cerrar();
    }

    @Test
    void testPreguntarReintento() {
        String input = "s\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        
        VistaConsola vistaTest = new VistaConsola();
        boolean reintento = vistaTest.preguntarReintento();
        assertTrue(reintento);
        vistaTest.cerrar();
    }
    
    @Test
    void testPreguntarReintentoNo() {
        String input = "n\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        
        VistaConsola vistaTest = new VistaConsola();
        boolean reintento = vistaTest.preguntarReintento();
        assertFalse(reintento);
        vistaTest.cerrar();
    }
}
