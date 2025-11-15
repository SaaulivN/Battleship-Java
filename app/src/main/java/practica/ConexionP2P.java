package practica;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class ConexionP2P {
    
    private Socket socket;
    private ServerSocket serverSocket;
    private PrintWriter salida;
    private BufferedReader entrada;
    
    public void esperarConexion(int puerto) throws IOException {
        serverSocket = new ServerSocket(puerto);
        socket = serverSocket.accept();
        configurarFlujos();
    }
    
    public void conectar(String ip, int puerto) throws IOException {
        socket = new Socket(ip, puerto);
        configurarFlujos();
    }
    
    private void configurarFlujos() throws IOException {
        salida = new PrintWriter(socket.getOutputStream(), true);
        entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }
    
    public String getDireccionRemota() {
        if (socket != null) {
            return socket.getInetAddress().toString();
        }
        return "N/A";
    }
    
    public void enviarMensaje(String mensaje) {
        if (salida != null) {
            salida.println(mensaje);
        }
    }
    
    public String leerMensaje() throws IOException {
        if (entrada != null) {
            return entrada.readLine();
        }
        throw new IOException("El flujo de entrada no está inicializado.");
    }
    
    public void cerrar() {
        try {
            if (entrada != null) entrada.close();
            if (salida != null) salida.close();
            if (socket != null) socket.close();
            if (serverSocket != null) serverSocket.close();
        } catch (IOException e) {
            System.err.println("Error al cerrar conexión: " + e.getMessage());
        }
    }
}