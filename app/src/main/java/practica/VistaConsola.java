package practica;

import java.util.Map;
import java.util.Scanner;
import java.util.function.BiPredicate;

public class VistaConsola {
    
    private Scanner scanner;

    public VistaConsola() {
        this.scanner = new Scanner(System.in);
    }
    
    public void mostrarBienvenida() {
        System.out.println("=== BATTLESHIP P2P ===");
    }
    
    public String obtenerNombreJugador() {
        System.out.print("Ingresa tu nombre: ");
        return scanner.nextLine();
    }
    
    public int elegirModo() {
        while (true) {
            System.out.println("\nSelecciona modo:");
            System.out.println("1. Crear partida (Esperar conexión)");
            System.out.println("2. Unirse a partida (Conectar a otro jugador)");
            System.out.print("Opción: ");

            String opcion = scanner.nextLine();

            if ("1".equals(opcion)) {
                return 1;
            } else if ("2".equals(opcion)) {
                return 2;
            } else {
                mostrarError("Opción inválida. Intenta nuevamente.");
            }
        }
    }
    
    public String obtenerIPServidor() {
        System.out.print("\nIngresa la IP del otro jugador: ");
        return scanner.nextLine();
    }
    
    public int[] obtenerDisparo(BiPredicate<Integer, Integer> yaDisparado) {
        while (true) {
            try {
                System.out.print("Ingresa coordenadas para disparar (fila,columna 0-9): ");
                String entrada = scanner.nextLine();
                String[] coordenadas = entrada.split(",");

                if (coordenadas.length != 2) {
                    mostrarError("Formato inválido. Usa: fila,columna");
                    continue;
                }

                int fila = Integer.parseInt(coordenadas[0].trim());
                int columna = Integer.parseInt(coordenadas[1].trim());
                int tam = JuegoBattleship.TAMANIO_TABLERO;

                if (fila >= 0 && fila < tam && columna >= 0 && columna < tam) {
                    if (!yaDisparado.test(fila, columna)) {
                        return new int[] { fila, columna };
                    } else {
                        mostrarError("Ya disparaste en esa posición.");
                    }
                } else {
                    mostrarError("Coordenadas fuera de rango. Usa números del 0 al 9.");
                }
            } catch (NumberFormatException e) {
                mostrarError("Por favor ingresa números válidos.");
            }
        }
    }

    public void mostrarMensaje(String mensaje) {
        System.out.println(mensaje);
    }
    
    public void mostrarError(String error) {
        System.err.println(error);
    }
    
    public boolean preguntarReintento() {
        System.out.println("¿Deseas intentar nuevamente? (s/n)");
        String respuesta = scanner.nextLine();
        return respuesta.equalsIgnoreCase("s");
    }
    
    public void mostrarTablero(char[][] tablero, String titulo) {
        System.out.println("\n=== " + titulo + " ===");
        System.out.print("  ");
        for (int i = 0; i < JuegoBattleship.TAMANIO_TABLERO; i++) {
            System.out.print(i + " ");
        }
        System.out.println();
        
        for (int i = 0; i < JuegoBattleship.TAMANIO_TABLERO; i++) {
            System.out.print(i + " ");
            for (int j = 0; j < JuegoBattleship.TAMANIO_TABLERO; j++) {
                System.out.print(tablero[i][j] + " ");
            }
            System.out.println();
        }
    }
    
    public void mostrarEstadoBarcos(Map<String, Integer> barcos, Map<String, Integer> impactosPorBarco) {
        System.out.println("\nEstado de tus barcos:");
        for (String barco : barcos.keySet()) {
            int impactos = impactosPorBarco.getOrDefault(barco, 0);
            int tamanio = barcos.get(barco);
            String estado = (impactos >= tamanio) ? "HUNDIDO" : impactos + "/" + tamanio;
            System.out.println("  " + barco + ": " + estado);
        }
        System.out.println("\nLeyenda: ~=Agua, ?=Desconocido, X=Impacto, O=Fallo, Letras=Barcos");
    }

    public void cerrar() {
        scanner.close();
    }
}