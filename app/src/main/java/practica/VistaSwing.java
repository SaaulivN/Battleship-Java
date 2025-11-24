package practica;

import javax.swing.*;
import java.awt.*;

public class VistaSwing extends JFrame implements IVista {
    private BattleshipControlador controlador;
    private JButton[][] botonesEnemigos;
    private JLabel[][] etiquetasPropias;
    private JTextArea areaLog;
    private boolean puedeDisparar = false;

    public VistaSwing() {
        super("Battleship P2P - Java Swing");
        configurarVentana();
        inicializarComponentes();
    }

    private void configurarVentana() {
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null); // Centrar
    }

    private void inicializarComponentes() {
        // Panel central con los dos tableros
        JPanel panelTableros = new JPanel(new GridLayout(1, 2, 20, 0));
        
        // Tablero Propio (Izquierda - Son JLabels o Paneles de colores)
        JPanel panelPropio = new JPanel(new GridLayout(10, 10));
        panelPropio.setBorder(BorderFactory.createTitledBorder("Tu Flota"));
        etiquetasPropias = new JLabel[10][10];
        
        // Tablero Enemigo (Derecha - Son JButtons para disparar)
        JPanel panelEnemigo = new JPanel(new GridLayout(10, 10));
        panelEnemigo.setBorder(BorderFactory.createTitledBorder("Radar Enemigo (Click para atacar)"));
        botonesEnemigos = new JButton[10][10];

        // Llenar tableros
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                // Configurar tablero propio
                JLabel celdaPropia = new JLabel("", SwingConstants.CENTER);
                celdaPropia.setBorder(BorderFactory.createLineBorder(Color.GRAY));
                etiquetasPropias[i][j] = celdaPropia;
                panelPropio.add(celdaPropia);

                // Configurar tablero enemigo
                JButton boton = new JButton();
                int finalI = i; // Necesario para la lambda
                int finalJ = j;
                boton.addActionListener(e -> manejarClic(finalI, finalJ));
                botonesEnemigos[i][j] = boton;
                panelEnemigo.add(boton);
            }
        }

        panelTableros.add(panelPropio);
        panelTableros.add(panelEnemigo);
        add(panelTableros, BorderLayout.CENTER);

        // Panel inferior para logs/mensajes
        areaLog = new JTextArea(5, 50);
        areaLog.setEditable(false);
        add(new JScrollPane(areaLog), BorderLayout.SOUTH);
    }

    private void manejarClic(int fila, int col) {
        if (puedeDisparar && controlador != null) {
            // Desactivar botón para no volver a disparar ahí
            botonesEnemigos[fila][col].setEnabled(false);
            controlador.procesarDisparo(fila, col);
        }
    }

    // --- IMPLEMENTACIÓN DE LA INTERFAZ IVISTA ---

    @Override
    public void mostrar() {
        setVisible(true);
    }

    @Override
    public void cerrar() {
        dispose();
    }

    @Override
    public String obtenerNombreJugador() {
        return JOptionPane.showInputDialog(this, "Ingresa tu nombre:");
    }

    @Override
    public int elegirModoJuego() {
        Object[] options = {"Crear Partida (Servidor)", "Unirse (Cliente)"};
        int n = JOptionPane.showOptionDialog(this,
                "¿Cómo deseas jugar?",
                "Selección de Modo",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]);
        return (n == 0) ? 1 : 2;
    }

    @Override
    public String obtenerIPServidor() {
        return JOptionPane.showInputDialog(this, "Ingresa IP del Host:", "localhost");
    }

    @Override
    public void mostrarMensaje(String mensaje) {
        areaLog.append(mensaje + "\n");
        areaLog.setCaretPosition(areaLog.getDocument().getLength()); // Auto-scroll
    }

    @Override
    public void mostrarError(String error) {
        JOptionPane.showMessageDialog(this, error, "Error", JOptionPane.ERROR_MESSAGE);
    }

    @Override
    public void actualizarTableroPropio(char[][] tablero) {
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                char c = tablero[i][j];
                etiquetasPropias[i][j].setText(String.valueOf(c));
                // Colorear según estado
                if (c == '~') etiquetasPropias[i][j].setBackground(Color.CYAN);
                else if (c == 'X') etiquetasPropias[i][j].setBackground(Color.RED);
                else if (c == 'O') etiquetasPropias[i][j].setBackground(Color.BLUE);
                else etiquetasPropias[i][j].setBackground(Color.GRAY); // Barco
                etiquetasPropias[i][j].setOpaque(true);
            }
        }
    }

    @Override
    public void actualizarTableroEnemigo(char[][] tablero) {
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                char c = tablero[i][j];
                if (c == 'X') {
                    botonesEnemigos[i][j].setBackground(Color.RED);
                    botonesEnemigos[i][j].setText("X");
                } else if (c == 'O') {
                    botonesEnemigos[i][j].setBackground(Color.BLUE);
                    botonesEnemigos[i][j].setText("O");
                }
            }
        }
    }

    @Override
    public void setControlador(BattleshipControlador controlador) {
        this.controlador = controlador;
    }

    @Override
    public void setPuedeDisparar(boolean puede) {
        this.puedeDisparar = puede;
        mostrarMensaje(puede ? ">>> ES TU TURNO: Selecciona una casilla en el radar enemigo." : ">>> TURNO RIVAL: Esperando disparo...");
    }
}