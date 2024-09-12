package crossydupe;
    import crossydupe.*;
    import Entity.Chicken;
    import Entity.Vehicle;
    import Tiles.tileManager;
    import java.awt.*;
    import java.io.IOException;
    import java.util.ArrayList;
    import java.util.Random;
    import javax.imageio.ImageIO;
    import javax.swing.*;

public class GamePanel extends JPanel implements Runnable {
    final int originalTileSize = 16;
    public final int scale = 3;
    public final int tileSize = originalTileSize * scale;
    
    public final int maxScreenCol = 20;
    public final int maxScreenRow = 15;
    public final int screenWidth = tileSize * maxScreenCol;
    public final int screenHeight = tileSize * maxScreenRow;
    
    public final int maxWorldCol = 50;
    public final int maxWorldRow = 50;
    public final int worldWidth = tileSize * maxWorldCol;
    public final int worldHeight = tileSize * maxWorldRow;
     // setting default size of pretty much everything to 16 such as the 
        //chicken killers then scaling so them bishes won't look tiny; proportionate to the pc resolution:3
        //Game screen settings:
    // FPS
    int FPS = 60; 
    
    tileManager tM = new tileManager(this);
    KeyHandler kH = new KeyHandler();
    Thread gameThread;
    private boolean running = false;
    public Chicken ch = new Chicken(this,kH);
    
    ArrayList<Vehicle> vehicles = new ArrayList<>();
    private Random random = new Random();
    
    private Image d1,d2,d3,d4,d5,d6;
    private boolean isAnimatingDeath = false;
    private long animationStartTime;
    
    private static final long ANIMATION_DURATION = 1000; 
    private boolean menuDisplayed = false;
    private DeathPointMenu menu;

    DoublyLinkedList deathPoints = new DoublyLinkedList();

    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(new Color(0, 153, 0)); 
        this.setDoubleBuffered(true);
        this.addKeyListener(kH);
        this.setFocusable(true);
        
        initVehicles();
        try {
            d1 = ImageIO.read(getClass().getResourceAsStream("/res/player/d1.gif"));
            d2 = ImageIO.read(getClass().getResourceAsStream("/res/player/d2.gif"));
            d3 = ImageIO.read(getClass().getResourceAsStream("/res/player/d1-2.png.png"));
            d4 = ImageIO.read(getClass().getResourceAsStream("/res/player/d1-3.png.png"));
            d5 = ImageIO.read(getClass().getResourceAsStream("/res/player/d1-4.png.png"));
            d6 = ImageIO.read(getClass().getResourceAsStream("/res/player/d1-5.png.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initVehicles() {
        vehicles.clear();  
        for (int i = 0; i < 3; i++) {
            vehicles.add(new Vehicle(this, 450, 250 + i * 250, "down", "car"));
            vehicles.add(new Vehicle(this, 550, 200 + i * 200, "down", "jeep"));
            vehicles.add(new Vehicle(this, 650, 200 + i * 200, "up", "car"));
            vehicles.add(new Vehicle(this, 120, 500 + i * 200, "up", "jeep"));
            vehicles.add(new Vehicle(this, 70, 100 + i * 200, "down", "jeep"));
        }
    }


    public void startGameThread() {
        stopGameThread();  
        running = true;
        gameThread = new Thread(this);
        gameThread.start();
    }

    public void stopGameThread() {
        running = false;
        if (gameThread != null) {
            try {
                gameThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            gameThread = null;
        }
    }

    @Override
    public void run() {
        double drawInterval = 1000000000 / FPS;
        double nextDrawTime = System.nanoTime() + drawInterval;

        while (running) {
            update();
            repaint();

            try {
                double remainingTime = nextDrawTime - System.nanoTime();
                remainingTime = remainingTime / 1000000;

                if (remainingTime < 0) {
                    remainingTime = 0;
                }

                Thread.sleep((long) remainingTime);
                nextDrawTime += drawInterval;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void checkCollision() {
        if (!ch.isAlive()) {
            return;
        }

        Rectangle chickenBounds = ch.getBounds(); 

        System.out.println("Chicken Bounds: " + chickenBounds);

        for (Vehicle vehicle : vehicles) {
            Rectangle vehicleBounds = vehicle.getBounds(); 
            System.out.println("Vehicle Bounds: " + vehicleBounds);

            if (chickenBounds.intersects(vehicleBounds)) {
                handleCollision();
                break;
            }
        }
    }

    public void handleCollision() {
        ch.setAlive(false);
        ch.setVisible(false);

        if (deathPoints.size() == 0) {
            Node firstDeathPoint = new Node(20, 300);
            deathPoints.add(firstDeathPoint);
        }

        deathPoints.add(new Node(ch.getX(), ch.getY()));
        startDeathAnimation();
    }

    public void startDeathAnimation() {
        isAnimatingDeath = true;
        animationStartTime = System.currentTimeMillis();
    }

    public void playDeathAnimation(Graphics2D g2) {
        long elapsedTime = System.currentTimeMillis() - animationStartTime;

        if (elapsedTime < 200) {
            g2.drawImage(d1, ch.worldX, ch.worldY, tileSize, tileSize, this);
        } else if (elapsedTime < 250){
            g2.drawImage(d3, ch.worldX, ch.worldY, tileSize, tileSize, this);
        } else if (elapsedTime < 450) {
            g2.drawImage(d4, ch.worldX, ch.worldY, tileSize, tileSize, this);
        } else if (elapsedTime < 650){
            g2.drawImage(d5, ch.worldX, ch.worldY, tileSize, tileSize, this);
        } else if (elapsedTime < 850){
            g2.drawImage(d6, ch.worldX, ch.worldY, tileSize, tileSize, this);
        } else{
            g2.drawImage(d2, ch.worldX, ch.worldY, tileSize, tileSize, this);

            if (elapsedTime > ANIMATION_DURATION && !menuDisplayed) {
                menuDisplayed = true;
                showDeathPointMenu();
            }
        }
    }

    public void showDeathPointMenu() {
        if (menu == null || !menu.isVisible()) {
            menu = new DeathPointMenu(
                (Frame) SwingUtilities.getWindowAncestor(this),
                deathPoints,
                this
            );

            menu.addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosed(java.awt.event.WindowEvent e) {
                    isAnimatingDeath = false;
                    ch.setVisible(true); 
                    menuDisplayed = false;
                    ch.setAlive(true);
                    startGameThread(); 
                }
            });

            menu.setVisible(true);
        }
    }

    public void update() {
        if (ch.isAlive()) {
            ch.update(); 

            for (Vehicle vehicle : vehicles) {
                vehicle.update(); 
            }

            checkCollision(); 
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;

        tM.draw(g2);

        if (!isAnimatingDeath && ch.isAlive()) {
            ch.draw(g2);
        }

        for (Vehicle vehicle : vehicles) {
            vehicle.draw(g2);

 
          // Rectangle bounds = vehicle.getBounds();
           //g2.setColor(Color.RED); 
           //g2.draw(bounds);
        }

        if (isAnimatingDeath) {
            playDeathAnimation(g2);
        }

        g2.dispose();
    }

    private void initializeVehicles(GamePanel gP) {
        int laneHeight = gP.tileSize; 
        int numLanes = 2; 
        
        for (int i = 0; i < numLanes; i++) {
            int yPos = i * laneHeight;
            for (int j = 0; j < gP.screenWidth; j += gP.tileSize * 5) { 
                int xPos = j;
                String direction = random.nextBoolean() ? "up" : "down";
                String vehicleType = random.nextBoolean() ? "car" : "jeep";
                Vehicle vehicle = new Vehicle(this, xPos, yPos, direction, vehicleType);
                vehicles.add(vehicle);
            }
        }
    }
}