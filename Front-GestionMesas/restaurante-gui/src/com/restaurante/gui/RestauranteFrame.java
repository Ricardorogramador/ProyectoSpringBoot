package com.restaurante.gui;

import com.restaurante.gui.model.Mesa;
import com.restaurante.gui.model.MesaApiClient;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;
import java.io.IOException;
import javax.imageio.ImageIO;

public class RestauranteFrame extends JFrame {
    private JPanel panelMesas;
    private JComboBox<String> comboPreset;
    private int cantidadMesasActual = 6; // Valor por defecto
    private Image mesaIconImg;

    public RestauranteFrame() {
        setTitle("Restaurante");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        cargarIconoMesa();
        initUI();
    }

    private void cargarIconoMesa() {
        try {
            var is = getClass().getResourceAsStream("/com/restaurante/gui/Icons/mesa-de-comedor.png");
            if (is == null) throw new IOException("No se encontró el icono");
            mesaIconImg = ImageIO.read(is);
        } catch (IOException e) {
            // Si falla, usa un cuadrado simple
            mesaIconImg = new BufferedImage(64, 64, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = ((BufferedImage) mesaIconImg).createGraphics();
            g2.setColor(Color.LIGHT_GRAY);
            g2.fillRect(8, 8, 48, 48);
            g2.dispose();
        }
    }

    private void initUI() {
        JPanel panelSuperior = new JPanel(new BorderLayout());
        panelSuperior.setBackground(new Color(70, 130, 180)); // Azul acero elegante
        panelSuperior.setPreferredSize(new Dimension(900, 80));
        panelSuperior.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));

        JLabel titulo = new JLabel("Restaurante - gestión de mesas", SwingConstants.LEFT);
        titulo.setFont(new Font("Arial", Font.BOLD, 26));
        titulo.setForeground(Color.WHITE);

        // Panel vertical para controles (preset + info)
        JPanel controlesVertical = new JPanel();
        controlesVertical.setLayout(new BoxLayout(controlesVertical, BoxLayout.Y_AXIS));
        controlesVertical.setOpaque(false);

        // Panel horizontal para preset y botones
        JPanel controles = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        controles.setOpaque(false);

        String[] presets = {"6", "12"};
        comboPreset = new JComboBox<>(presets);
        controles.add(new JLabel("Preset de mesas:") {{
            setForeground(Color.WHITE);
            setFont(new Font("Arial", Font.PLAIN, 16));
        }});
        controles.add(comboPreset);

        JButton btnPreset = new JButton("Aplicar preset");
        btnPreset.addActionListener(e -> {
            int cantidad = Integer.parseInt((String) comboPreset.getSelectedItem());
            cantidadMesasActual = cantidad;
            MesaApiClient.presetMesas(cantidad);
            actualizarMesas();
            ajustarTamañoVentana(cantidad);
        });
        controles.add(btnPreset);

        JButton btnActualizar = new JButton("Actualizar");
        btnActualizar.addActionListener(e -> actualizarMesas());
        controles.add(btnActualizar);

        controlesVertical.add(controles);

        // Botón de información de mesas
        JButton btnInfoMesas = new JButton("Información de mesas");
        btnInfoMesas.setAlignmentX(Component.RIGHT_ALIGNMENT);
        btnInfoMesas.addActionListener(e -> mostrarInformacionMesas());
        JPanel panelBtnInfo = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        panelBtnInfo.setOpaque(false);
        panelBtnInfo.add(btnInfoMesas);
        controlesVertical.add(panelBtnInfo);

        panelSuperior.add(titulo, BorderLayout.WEST);
        panelSuperior.add(controlesVertical, BorderLayout.EAST);

        panelMesas = new JPanel();
        setGridLayoutParaMesas(cantidadMesasActual);
        panelMesas.setBackground(Color.WHITE);
        panelMesas.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(panelSuperior, BorderLayout.NORTH);
        getContentPane().add(panelMesas, BorderLayout.CENTER);

        actualizarMesas();
    }

    private void mostrarInformacionMesas() {
        List<Mesa> mesas = MesaApiClient.obtenerMesas();
        String[] columnas = {"ID", "Número", "Estado"};
        String[][] datos = new String[mesas.size()][3];

        for (int i = 0; i < mesas.size(); i++) {
            Mesa m = mesas.get(i);
            datos[i][0] = String.valueOf(m.getId());
            datos[i][1] = String.valueOf(m.getNumero());
            if (m.isLimpieza()) {
                datos[i][2] = "Limpieza";
            } else if (m.isOcupada()) {
                datos[i][2] = "Ocupada";
            } else {
                datos[i][2] = "Libre";
            }
        }

        JTable tabla = new JTable(datos, columnas);
        tabla.setEnabled(false);
        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setPreferredSize(new Dimension(350, 200));

        JOptionPane.showMessageDialog(this, scroll, "Información de Mesas", JOptionPane.INFORMATION_MESSAGE);
    }

    private void setGridLayoutParaMesas(int cantidad) {
        if (cantidad <= 6) {
            panelMesas.setLayout(new GridLayout(2, 3, 20, 20));
        } else if (cantidad <= 12) {
            panelMesas.setLayout(new GridLayout(2, 6, 20, 20));
        } else {
            int filas = (int) Math.ceil(cantidad / 6.0);
            panelMesas.setLayout(new GridLayout(filas, 6, 20, 20));
        }
    }

    private void actualizarMesas() {
        panelMesas.removeAll();
        setGridLayoutParaMesas(cantidadMesasActual);
        List<Mesa> mesas = MesaApiClient.obtenerMesas();

        for (Mesa mesaObj : mesas) {
            Color estadoColor;
            if (mesaObj.isLimpieza()) {
                estadoColor = new Color(135, 206, 235); // Azul cielo
            } else if (mesaObj.isOcupada()) {
                estadoColor = Color.RED;
            } else {
                estadoColor = Color.GREEN;
            }
            MesaIconPanel mesaPanel = new MesaIconPanel(mesaObj, mesaIconImg, estadoColor);
            mesaPanel.setComponentPopupMenu(crearPopupMenu(mesaObj));
            panelMesas.add(mesaPanel);
        }
        panelMesas.revalidate();
        panelMesas.repaint();
    }

    private JPopupMenu crearPopupMenu(Mesa mesaObj) {
        JPopupMenu popup = new JPopupMenu();

        JMenuItem libre = new JMenuItem("Libre");
        libre.addActionListener(e -> {
            MesaApiClient.cambiarEstadoMesa(mesaObj.getId(), "libre");
            actualizarMesas();
        });
        popup.add(libre);

        JMenuItem ocupado = new JMenuItem("Ocupado");
        ocupado.addActionListener(e -> {
            MesaApiClient.cambiarEstadoMesa(mesaObj.getId(), "ocupado");
            actualizarMesas();
        });
        popup.add(ocupado);

        JMenuItem limpieza = new JMenuItem("Limpieza");
        limpieza.addActionListener(e -> {
            MesaApiClient.cambiarEstadoMesa(mesaObj.getId(), "limpieza");
            actualizarMesas();
        });
        popup.add(limpieza);

        return popup;
    }

    private void ajustarTamañoVentana(int cantidad) {
        if (cantidad <= 6) {
            setSize(900, 600);
        } else if (cantidad <= 12) {
            setSize(1200, 600);
        } else {
            setSize(1400, 800);
        }
        setLocationRelativeTo(null);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            RestauranteFrame frame = new RestauranteFrame();
            frame.setVisible(true);
        });
    }

    // Panel personalizado para la mesa, SOLO el icono con borde/sombra
    static class MesaIconPanel extends JPanel {
        private final Mesa mesaObj;
        private final Image mesaIcon;
        private final Color estadoColor;

        public MesaIconPanel(Mesa mesaObj, Image mesaIcon, Color estadoColor) {
            this.mesaObj = mesaObj;
            this.mesaIcon = mesaIcon;
            this.estadoColor = estadoColor;
            setOpaque(false);
            setPreferredSize(new Dimension(100, 100));
            setToolTipText("Mesa " + mesaObj.getNumero());
            // Popup menu con click derecho
            addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mousePressed(java.awt.event.MouseEvent evt) {
                    if (evt.isPopupTrigger())
                        showMenu(evt);
                }
                @Override
                public void mouseReleased(java.awt.event.MouseEvent evt) {
                    if (evt.isPopupTrigger())
                        showMenu(evt);
                }
                private void showMenu(java.awt.event.MouseEvent evt) {
                    if (getComponentPopupMenu() != null)
                        getComponentPopupMenu().show(MesaIconPanel.this, evt.getX(), evt.getY());
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();

            int margen = 8; // margen para la sombra y el borde

            // Sombra
            g2.setColor(new Color(estadoColor.getRed(), estadoColor.getGreen(), estadoColor.getBlue(), 60));
            g2.fillRoundRect(margen, margen, getWidth() - 2*margen, getHeight() - 2*margen, 40, 40);

            // Fondo blanco
            g2.setColor(Color.WHITE);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 40, 40);

            // Borde grueso de color de estado
            int borde = 5;
            g2.setStroke(new BasicStroke(borde));
            g2.setColor(estadoColor);
            g2.drawRoundRect(margen/2, margen/2, getWidth() - margen, getHeight() - margen, 40, 40);

            // ICONO: ajusta al máximo tamaño posible
            int iconW = getWidth() - 2*(margen + borde);
            int iconH = getHeight() - 2*(margen + borde);
            int x = (getWidth() - iconW) / 2;
            int y = (getHeight() - iconH) / 2;
            g2.drawImage(mesaIcon, x, y, iconW, iconH, null);

            g2.dispose();
        }
    }
}