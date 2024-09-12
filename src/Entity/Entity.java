/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entity;

import crossydupe.GamePanel;
import java.awt.*;
import java.awt.image.*;

    public class Entity {
        GamePanel gP;
        public int worldX,worldY;
        public int speed;
        
        public BufferedImage up,down,left,right;
        public String direction;
                      
        public Rectangle solidArea = new Rectangle(0,0,48,48);
        public int solidAreaDefaultX, solidAreaDefaultY;
        public boolean collisionOn = false;
        
        public Entity(GamePanel gP){
            this.gP = gP;
        }
    }
