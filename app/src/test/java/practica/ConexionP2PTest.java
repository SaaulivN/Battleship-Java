package practica;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.io.*;
import java.net.*;

public class ConexionP2PTest {
    
    private static final int PUERTO_PRUEBA = 12346;
    
    @Test
    void testCerrar() {
        ConexionP2P conexion = new ConexionP2P();
        // Debe cerrar sin lanzar excepciones incluso sin estar conectado
        assertDoesNotThrow(() -> conexion.cerrar());
    }

    @Test
    void testConectar() throws IOException {
        // Crear servidor en hilo separado
        Thread servidorThread = new Thread(() -> {
            try {
                ConexionP2P servidor = new ConexionP2P();
                servidor.esperarConexion(PUERTO_PRUEBA);
                servidor.cerrar();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        servidorThread.start();
        
        // Dar tiempo al servidor para iniciar
        try { Thread.sleep(100); } catch (InterruptedException e) {}
        
        // Cliente conecta
        ConexionP2P cliente = new ConexionP2P();
        assertDoesNotThrow(() -> cliente.conectar("localhost", PUERTO_PRUEBA));
        cliente.cerrar();
        
        try { servidorThread.join(); } catch (InterruptedException e) {}
    }

    @Test
    void testEnviarMensaje() throws IOException {
        Thread servidorThread = new Thread(() -> {
            try {
                ConexionP2P servidor = new ConexionP2P();
                servidor.esperarConexion(PUERTO_PRUEBA + 1);
                
                String mensajeRecibido = servidor.leerMensaje();
                assertEquals("Hola desde cliente", mensajeRecibido);
                
                servidor.cerrar();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        servidorThread.start();
        
        try { Thread.sleep(100); } catch (InterruptedException e) {}
        
        ConexionP2P cliente = new ConexionP2P();
        cliente.conectar("localhost", PUERTO_PRUEBA + 1);
        cliente.enviarMensaje("Hola desde cliente");
        cliente.cerrar();
        
        try { servidorThread.join(); } catch (InterruptedException e) {}
    }

    @Test
    void testEsperarConexion() throws IOException {
        Thread servidorThread = new Thread(() -> {
            try {
                ConexionP2P servidor = new ConexionP2P();
                servidor.esperarConexion(PUERTO_PRUEBA + 2);
                servidor.cerrar();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        servidorThread.start();
        
        try { Thread.sleep(100); } catch (InterruptedException e) {}
        
        // Cliente conecta al servidor que estaba esperando
        ConexionP2P cliente = new ConexionP2P();
        assertDoesNotThrow(() -> cliente.conectar("localhost", PUERTO_PRUEBA + 2));
        cliente.cerrar();
        
        try { servidorThread.join(); } catch (InterruptedException e) {}
    }

    @Test
    void testGetDireccionRemota() throws IOException {
        Thread servidorThread = new Thread(() -> {
            try {
                ConexionP2P servidor = new ConexionP2P();
                servidor.esperarConexion(PUERTO_PRUEBA + 3);
                
                String direccion = servidor.getDireccionRemota();
                assertNotNull(direccion);
                assertFalse(direccion.isEmpty());
                
                servidor.cerrar();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        servidorThread.start();
        
        try { Thread.sleep(100); } catch (InterruptedException e) {}
        
        ConexionP2P cliente = new ConexionP2P();
        cliente.conectar("localhost", PUERTO_PRUEBA + 3);
        cliente.cerrar();
        
        try { servidorThread.join(); } catch (InterruptedException e) {}
    }

    @Test
    void testLeerMensaje() throws IOException {
        Thread servidorThread = new Thread(() -> {
            try {
                ConexionP2P servidor = new ConexionP2P();
                servidor.esperarConexion(PUERTO_PRUEBA + 4);
                servidor.enviarMensaje("Mensaje del servidor");
                servidor.cerrar();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        servidorThread.start();
        
        try { Thread.sleep(100); } catch (InterruptedException e) {}
        
        ConexionP2P cliente = new ConexionP2P();
        cliente.conectar("localhost", PUERTO_PRUEBA + 4);
        String mensaje = cliente.leerMensaje();
        assertEquals("Mensaje del servidor", mensaje);
        cliente.cerrar();
        
        try { servidorThread.join(); } catch (InterruptedException e) {}
    }
}
