package Entity;

    import crossydupe.*;
    import java.awt.*;
    import java.awt.image.*;
    import java.io.IOException;
    import javax.imageio.ImageIO;

public class Chicken extends Entity {
    
    private BufferedImage  left,right,up,down;
    GamePanel gP;
    KeyHandler kH;
    public boolean isAlive = true;
    private boolean isVisible = true;
    public int worldX, worldY; 


    public Chicken(GamePanel gP,KeyHandler kH) {
        super(gP);
        this.gP = gP;
        this.kH = kH;

        setDefaultValues();
        getPlayerImage();
    }

    public void setDefaultValues() {
        worldX = 20;
        worldY = 300;
        speed = 2;
        direction = "right";
    }

    public void getPlayerImage() {
        try {
            //Suicidal bish profile for each direction
            right = ImageIO.read(getClass().getResourceAsStream("/res/player/right.png"));
            left = ImageIO.read(getClass().getResourceAsStream("/res/player/left.png"));
            down = ImageIO.read(getClass().getResourceAsStream("/res/player/down.png"));
            up = ImageIO.read(getClass().getResourceAsStream("/res/player/up.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setVisible(boolean isVisible) { 
        this.isVisible = isVisible;
    }

    public boolean isVisible() { 
        return isVisible;
    }

    public int getX() {
        return worldX;
    }

    public void setX(int worldX) {
        this.worldX = worldX;
    }

    public int getY() {
        return worldY;
    }

    public void setY(int worldY) {
        this.worldY = worldY;
    }

    public boolean isAlive() {
        return isAlive;
    }

    public void setAlive(boolean isAlive) {
        this.isAlive = isAlive;
    }

    private BufferedImage getImageForDirection() {
        if (direction.equals("up")) {
            return up;
        } else if (direction.equals("down")) {
            return down;
        } else if (direction.equals("left")) {
            return left;
        } else if (direction.equals("right")) {
            return right;
        }
        return null;
    }
    public Rectangle getBounds() {
        BufferedImage image = getImageForDirection();
        int imageWidth = gP.tileSize;
        int imageHeight = gP.tileSize;

        double scaleFactor = 0.5; 

        int boundsWidth = (int) (imageWidth * scaleFactor);
        int boundsHeight = (int) (imageHeight * scaleFactor);

        int boundsX = worldX + (imageWidth - boundsWidth) / 2;
        int boundsY = worldY + (imageHeight - boundsHeight) / 2;

        return new Rectangle(boundsX, boundsY, boundsWidth, boundsHeight);
    }



    public int getWidth() {
        BufferedImage image = getImageForDirection();
        return image.getWidth() * gP.scale;
    }

    public int getHeight() {
        BufferedImage image = getImageForDirection();
        return image.getHeight() * gP.scale;
    }

    public void update() {
        if (kH.upPressed) {
            direction = "up";
            worldY -= speed;
        } else if (kH.downPressed) {
            direction = "down";
            worldY += speed;
        } else if (kH.rightPressed) {
            direction = "right";
            worldX += speed;
        } else if (kH.leftPressed) {
            direction = "left";
            worldX -= speed;
        }

        // Boundary checking
        if (worldX < 0) {
            worldX = 0;
        }
        if (worldX > gP.screenWidth - gP.tileSize) {
            worldX = gP.screenWidth - gP.tileSize;
        }
        if (worldY < 0) {
            worldY = 0;
        }
        if (worldY > gP.screenHeight - gP.tileSize) {
            worldY = gP.screenHeight - gP.tileSize;
        }
    }

    public void draw(Graphics2D g2) {
        BufferedImage image = null;

        switch (direction) {
            case "up":
                image = up;
                break;
            case "down":
                image = down;
                break;
            case "left":
                image = left;
                break;
            case "right":
                image = right;
                break;           
        }
        int imageWidth = gP.tileSize ;
        int imageHeight = gP.tileSize;
        
        g2.drawImage(image, worldX, worldY, imageWidth, imageHeight, null);
        Rectangle bounds = getBounds();
        //g2.setColor(Color.RED); 
        //g2.draw(bounds);
    }
}
