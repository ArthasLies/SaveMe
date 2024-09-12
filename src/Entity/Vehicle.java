package Entity;
    import crossydupe.GamePanel;
    import java.awt.*;
    import java.awt.image.*;
    import java.io.*;
    import javax.imageio.ImageIO;

public class Vehicle extends Entity {

    private BufferedImage up, down;
    private String vehicleType;

    public Vehicle(GamePanel gP, int x, int y, String direction, String vehicleType) {
        super(gP);
        this.worldX = x;
        this.worldY = y;
        this.direction = direction;
        this.vehicleType = vehicleType;
        this.speed = 3;
        loadVehicleImage(vehicleType);
    }

    public void loadVehicleImage(String vehicleType) {
        try {
            switch (vehicleType) {
                case "car":
                    up = ImageIO.read(getClass().getResourceAsStream("/res/ChickenKiller/carUp.png"));
                    down = ImageIO.read(getClass().getResourceAsStream("/res/ChickenKiller/carDown.png"));
                    break;
                case "jeep":
                    up = ImageIO.read(getClass().getResourceAsStream("/res/ChickenKiller/jeepUp.png"));
                    down = ImageIO.read(getClass().getResourceAsStream("/res/ChickenKiller/jeepDown.png"));
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void update() {
        switch (direction) {
            case "up":
                worldY -= speed;
                if (worldY < -gP.tileSize) {
                    worldY = gP.screenHeight; 
                }
                break;
            case "down":
                worldY += speed;
                if (worldY > gP.screenHeight) {
                    worldY = -gP.tileSize; 
                }
                break;
        }
    }

    public void draw(Graphics2D g2) {
        BufferedImage image = getImageForDirection();

        if (image == null) {
            throw new IllegalStateException("Image not loaded for direction: " + direction);
        }

        int imageWidth = gP.tileSize * 3;
        int imageHeight = gP.tileSize * 3;

        g2.drawImage(image, worldX, worldY, imageWidth, imageHeight, null);
    }

    public int getWidth() {
        BufferedImage image = getImageForDirection();
        return image.getWidth() * 3;
    }

    public int getHeight() {
        BufferedImage image = getImageForDirection();
        return image.getHeight() * 3;
    }

    private BufferedImage getImageForDirection() {
        if (direction.equals("up")) {
            return up;
        } else if (direction.equals("down")) {
            return down;
        }
        return null;
    }

    public Rectangle getBounds() {
        BufferedImage image = getImageForDirection();

        int imageWidth = image.getWidth() *3;
        int imageHeight = image.getHeight()*3;

        double scaleFactor = 0.5; 
        int boundsWidth = (int) (imageWidth * scaleFactor);
        int boundsHeight = (int) (imageHeight * scaleFactor);

        int boundsX = worldX + (imageWidth + boundsWidth)/2;
        int boundsY = worldY + (imageHeight+ boundsHeight)/2;

        if (direction.equals("up") && vehicleType.equals("jeep")) {
            boundsX = worldX + (imageWidth - boundsWidth) + 35;
            boundsY = worldY + (imageHeight+ boundsHeight)/2 + 10;
            boundsWidth = (int) (boundsWidth * 2); 
            boundsHeight = (int) (boundsHeight * 3); 
        } else if (direction.equals("up") && vehicleType.equals("car")) {
            boundsY = worldY + (imageHeight+ boundsHeight)/2 - 32;
            boundsWidth = (int) (boundsWidth * 3); 
            boundsHeight = (int) (boundsHeight *5); 
        } else if (direction.equals("down") &&vehicleType.equals("car")) {
             boundsY = worldY + (imageHeight+ boundsHeight)/2 - 10;
            boundsWidth = (int) (boundsWidth * 3); 
            boundsHeight = (int) (boundsHeight *5); 
        }else if(direction.equals("down") && vehicleType.equals("jeep")) {
            boundsY = worldY + (imageHeight+ boundsHeight)/2 - 10;
            boundsWidth = (int) (boundsWidth * 2); 
            boundsHeight = (int) (boundsHeight * 3); 
        }

        return new Rectangle(boundsX, boundsY, boundsWidth, boundsHeight);
    }

}
