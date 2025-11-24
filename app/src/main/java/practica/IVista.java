package practica;

// Esta interfaz define QUÉ debe hacer la vista, sin importar CÓMO lo haga (gráfica o texto)
public interface IVista {
    // Métodos para iniciar
    void mostrar();
    void cerrar();
    
    // Métodos para obtener datos iniciales
    String obtenerNombreJugador();
    int elegirModoJuego(); // 1 = Servidor, 2 = Cliente
    String obtenerIPServidor();
    
    // Métodos de juego
    void mostrarMensaje(String mensaje);
    void mostrarError(String error);
    
    // Actualización visual
    void actualizarTableroPropio(char[][] tablero);
    void actualizarTableroEnemigo(char[][] tablero); // El tablero enemigo serán botones
    
    // Enlazar con el controlador para que los botones funcionen
    void setControlador(BattleshipControlador controlador);
    
    // Habilitar/Deshabilitar interacción (ej. esperar turno)
    void setPuedeDisparar(boolean puede);
}