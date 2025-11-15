package practica;

import java.io.IOException;

public class BattleshipControlador {
    private static final int PUERTO = 12345;
    
    private JuegoBattleship juego;
    private VistaConsola vista;
    private ConexionP2P conexion;
    
    private String nombreJugador;
    private boolean esServidor;

    public BattleshipControlador() {
        this.juego = new JuegoBattleship();
        this.vista = new VistaConsola();
        this.conexion = new ConexionP2P();
    }

    public void iniciar() {
        vista.mostrarBienvenida();
        this.nombreJugador = vista.obtenerNombreJugador();

        if (elegirModoConexion()) {
            try {
                intercambiarNombres();
                iniciarJuego();
            } catch (IOException e) {
                vista.mostrarError("Error de conexión principal: " + e.getMessage());
            } finally {
                conexion.cerrar();
                vista.cerrar();
            }
        }
    }

    private boolean elegirModoConexion() {
        int modo = vista.elegirModo();
        esServidor = (modo == 1);
        
        while (true) {
            try {
                if (esServidor) {
                    vista.mostrarMensaje("\nIniciando servidor en puerto " + PUERTO + "...");
                    vista.mostrarMensaje("Esperando conexión de otro jugador...");
                    conexion.esperarConexion(PUERTO);
                    vista.mostrarMensaje("¡Jugador conectado desde: " + conexion.getDireccionRemota() + "!");
                } else {
                    String ip = vista.obtenerIPServidor();
                    vista.mostrarMensaje("Conectando a " + ip + ":" + PUERTO + "...");
                    conexion.conectar(ip, PUERTO);
                    vista.mostrarMensaje("¡Conectado exitosamente!");
                }
                return true; // Conexión exitosa
                
            } catch (IOException e) {
                vista.mostrarError("Error al conectar: " + e.getMessage());
                if (esServidor || !vista.preguntarReintento()) {
                    return false;
                }
            }
        }
    }

    private void intercambiarNombres() throws IOException {
        String nombreOponente;
        if (esServidor) {
            nombreOponente = conexion.leerMensaje();
            conexion.enviarMensaje(nombreJugador);
        } else {
            conexion.enviarMensaje(nombreJugador);
            nombreOponente = conexion.leerMensaje();
        }
        vista.mostrarMensaje("Jugando contra: " + nombreOponente);
    }

    private void iniciarJuego() throws IOException {
        vista.mostrarMensaje("\n=== INICIANDO JUEGO ===");

        juego.colocarBarcosAutomaticamente();
        vista.mostrarMensaje("Tus barcos han sido colocados automáticamente.");
        vista.mostrarTablero(juego.getTableroPropio(), "TU TABLERO");
        vista.mostrarEstadoBarcos(juego.getBarcos(), juego.getImpactosPorBarco());


        boolean juegoActivo = true;
        boolean miTurno = esServidor;

        conexion.enviarMensaje(ProtocoloBattleship.LISTO);
        String respuesta = conexion.leerMensaje();
        
        if (respuesta == null) {
            vista.mostrarError("El oponente se desconectó durante la inicialización.");
            return;
        }

        if (ProtocoloBattleship.LISTO.equals(respuesta)) {
            vista.mostrarMensaje("¡Ambos jugadores listos! El juego comienza.");
            
            if (miTurno) {
                vista.mostrarMensaje("\n¡Tú comienzas!");
            } else {
                vista.mostrarMensaje("\nEl oponente comienza...");
            }

            while (juegoActivo) {
                if (miTurno) {
                    juegoActivo = turnoLocal();
                } else {
                    juegoActivo = turnoRemoto();
                }
                
                if(juegoActivo) {
                    miTurno = !miTurno;
                }
            }
        }
    }

    private boolean turnoLocal() throws IOException {
        vista.mostrarMensaje("\n=== TU TURNO ===");
        vista.mostrarTablero(juego.getTableroEnemigo(), "TABLERO ENEMIGO");
        
        int[] disparo = vista.obtenerDisparo(juego::yaDisparado);
        
        conexion.enviarMensaje(ProtocoloBattleship.construirMensajeDisparo(disparo[0], disparo[1]));
        String respuesta = conexion.leerMensaje();
        
        if (respuesta == null) {
            vista.mostrarError("El oponente se desconectó.");
            return false;
        }

        try {
            ProtocoloBattleship.Mensaje mensaje = ProtocoloBattleship.parsearMensaje(respuesta);

            switch (mensaje.comando) {
                case ProtocoloBattleship.IMPACTO:
                    vista.mostrarMensaje("¡IMPACTO en (" + mensaje.x + "," + mensaje.y + ")!");
                    juego.registrarImpacto(mensaje.x, mensaje.y);
                    break;

                case ProtocoloBattleship.FALLO:
                    vista.mostrarMensaje("FALLO en (" + mensaje.x + "," + mensaje.y + ")");
                    juego.registrarFallo(mensaje.x, mensaje.y);
                    break;

                case ProtocoloBattleship.HUNDIDO:
                    vista.mostrarMensaje("¡HUNDIDO! " + mensaje.tipoBarco + " en (" + mensaje.x + "," + mensaje.y + ")");
                    juego.registrarImpacto(mensaje.x, mensaje.y);
                    break;

                case ProtocoloBattleship.JUEGO_TERMINADO:
                    vista.mostrarMensaje("¡FELICIDADES! ¡HAS GANADO!");
                    return false; // Juego terminado

                default:
                    vista.mostrarError("Respuesta inesperada: " + respuesta);
            }
            return true;
            
        } catch (Exception e) {
            vista.mostrarError("Error procesando respuesta: " + e.getMessage() + ". Respuesta: " + respuesta);
            return false;
        }
    }

   private boolean turnoRemoto() throws IOException {
        vista.mostrarMensaje("\n=== TURNO DEL OPONENTE ===");
        vista.mostrarMensaje("Esperando disparo del oponente...");
        
        String mensajeEntrante = conexion.leerMensaje();
        
        if (mensajeEntrante == null) {
            vista.mostrarError("El oponente se desconectó.");
            return false;
        }
        
        try {
            ProtocoloBattleship.Mensaje mensaje = ProtocoloBattleship.parsearMensaje(mensajeEntrante);
            
            if (ProtocoloBattleship.DISPARAR.equals(mensaje.comando)) {
                boolean impacto = juego.recibirDisparo(mensaje.x, mensaje.y);
                
                if (impacto) {
                    String tipoBarco = juego.obtenerTipoBarcoEn(mensaje.x, mensaje.y);
                    
                    if (!"DESCONOCIDO".equals(tipoBarco) && juego.estaBarcoHundido(tipoBarco)) {
                        
                        if (juego.todosBarcosHundidos()) {
                            conexion.enviarMensaje(ProtocoloBattleship.JUEGO_TERMINADO);
                            vista.mostrarError("El oponente hundió tu " + tipoBarco);
                            vista.mostrarError("¡HAS PERDIDO!");
                            return false;
                        } else {
                            conexion.enviarMensaje(ProtocoloBattleship.construirMensajeResultado(
                                ProtocoloBattleship.HUNDIDO, mensaje.x, mensaje.y, tipoBarco));
                            vista.mostrarMensaje("El oponente hundió tu " + tipoBarco + " en (" + mensaje.x + "," + mensaje.y + ")");
                        }
                    } else {
                        conexion.enviarMensaje(ProtocoloBattleship.construirMensajeResultado(
                            ProtocoloBattleship.IMPACTO, mensaje.x, mensaje.y, null));
                        vista.mostrarMensaje("El oponente impactó en (" + mensaje.x + "," + mensaje.y + ")");
                    }
                } else {
                    conexion.enviarMensaje(ProtocoloBattleship.construirMensajeResultado(
                        ProtocoloBattleship.FALLO, mensaje.x, mensaje.y, null));
                    vista.mostrarMensaje("El oponente falló en (" + mensaje.x + "," + mensaje.y + ")");
                }
            }
            
            vista.mostrarTablero(juego.getTableroPropio(), "TU TABLERO (ACTUALIZADO)");
            vista.mostrarEstadoBarcos(juego.getBarcos(), juego.getImpactosPorBarco());
            return true;
            
        } catch (Exception e) {
            vista.mostrarError("Error procesando mensaje del oponente: " + e.getMessage());
            return false;
        }
    }

    public static void main(String[] args) {
        BattleshipControlador controlador = new BattleshipControlador();
        controlador.iniciar();
    }
}