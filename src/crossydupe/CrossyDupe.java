/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package crossydupe;
 //Crossy Road dupe  with doubly  linked lists. 
 //Project of Aliamae Garcia and Hans Monjardin
 // Data Structure and Algorithm (Doubly Linked-list)
    import java.awt.*;
    import java.awt.event.ActionEvent;
    import java.awt.event.ActionListener;
    import java.awt.image.BufferedImage;
    import java.io.IOException;
    import javax.imageio.ImageIO;
    import javax.swing.*;

public class CrossyDupe {

    private static JFrame w;
    private static GamePanel gP;

    public static void main(String[] args) {
        new CrossyDupe().showStartPage();
    }

    public void showStartPage() {
        JFrame startFrame = new JFrame("KFCrossy Dupe");
        startFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        startFrame.setSize(700, 500);
        startFrame.setLocationRelativeTo(null);

        BufferedImage image = null;
        try {
            image = ImageIO.read(getClass().getResourceAsStream("/resTiles/KFC.jpg"));
        } catch (IOException e) {
            e.printStackTrace(); 
        }

        if (image != null) {
            Image scaledImage = image.getScaledInstance(700, 500, Image.SCALE_SMOOTH);

            BufferedImage resizedImage = new BufferedImage(700, 500, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = resizedImage.createGraphics();
            g2d.drawImage(scaledImage, 0, 0, null);
            g2d.dispose();

            JLabel backgroundLabel = new JLabel(new ImageIcon(resizedImage));
            backgroundLabel.setLayout(new BorderLayout());

            JPanel transparentPanel = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    drawShadowedText(g, "KFCrossy Dupe", 50, 100);
                }
            };
            transparentPanel.setOpaque(false);
            transparentPanel.setLayout(new BorderLayout());
            
            GridBagLayout gbl = new GridBagLayout();
            transparentPanel.setLayout(gbl);

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(350, 50, 50, 50); 
            JButton startButton = new JButton("Start Game");
            startButton.setBackground(new Color(0, 0, 0, 150)); 
            startButton.setFont(new Font("Comic Sans MS", Font.PLAIN, 18));
            startButton.setForeground(Color.WHITE);

            startButton.setBorderPainted(false);
            startButton.setFocusPainted(false);
            startButton.setContentAreaFilled(true);
            
            gbc.gridx = 0; 
            gbc.gridy = 0; 
            gbc.anchor = GridBagConstraints.CENTER; 

            transparentPanel.add(startButton, gbc);

            startButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    startFrame.setVisible(false); 
                    startGame(); 
                }
            });

            backgroundLabel.add(transparentPanel);
            startFrame.setContentPane(backgroundLabel);
            startFrame.setResizable(false);
            startFrame.setVisible(true); 
        }
    }

    public void startGame() {
        w = new JFrame();
        w.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        w.setResizable(false);
        w.setTitle("Crossy Dupe");

        gP = new GamePanel();
        w.add(gP);
        w.pack();
        w.setLocationRelativeTo(null);
        w.setVisible(true);
        gP.startGameThread();
    }

    public static void stopGameAndExit() {
        if (gP != null) {
            gP.stopGameThread();
        }
        if (w != null) {
            w.dispose();
        }
        System.exit(0);
    }

    private static void drawShadowedText(Graphics g, String text, int x, int y) {
        Graphics2D g2d = (Graphics2D) g;
        Font font = new Font("Comic Sans MS", Font.BOLD, 24);
        g2d.setFont(font);

        g2d.setColor(Color.BLACK);
        g2d.drawString(text, x + 2, y + 2);

        g2d.setColor(Color.WHITE);
        g2d.drawString(text, x, y);
    }
}
