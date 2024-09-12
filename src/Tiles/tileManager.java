package Tiles;

    import crossydupe.GamePanel;
    import Tiles.tile;
    import java.awt.*;
    import java.io.*;
    import javax.imageio.*;

public class tileManager {
    GamePanel gP;
    tile[] tile;
    int mapTileNum[][];
    
    public tileManager(GamePanel gP){
        this.gP = gP;
        tile = new tile[10];
        mapTileNum = new int[gP.maxScreenCol][gP.maxScreenRow];
        
        getTileImage();
        loadMap();
    }
    public void getTileImage(){
        try{
            tile[0] = new tile();
            tile[0].image = ImageIO.read(getClass().getResourceAsStream("/resTiles/GRASS DETAIL 3 - DAY.png"));
            
            tile[1] = new tile();
            tile[1].image = ImageIO.read(getClass().getResourceAsStream("/resTiles/road.png"));
            
            tile[2] = new tile();
            tile[2].image = ImageIO.read(getClass().getResourceAsStream("/resTiles/GRASS DETAIL 2 - DAY.png"));
          
          
                       
            
        }catch(IOException e){
            e.printStackTrace();
        }
    }
    public void loadMap(){
        
        try{
            InputStream IS = getClass().getResourceAsStream("/res/maps/map01.txt");
            BufferedReader br = new BufferedReader(new InputStreamReader(IS));
            
            int col = 0;
            int row = 0;
            
            while(col < gP.maxScreenCol && row < gP.maxScreenRow){
                
                String line = br.readLine();
                
                while(col<gP.maxScreenCol){
                    String numbers[] = line.split(" ");
                    int num = Integer.parseInt(numbers[col]);
                    
                    mapTileNum[col][row] = num;
                    col++;
                    }
                    if(col == gP.maxScreenCol){
                        col = 0;
                        row++;
                    }
                }
                br.close();
            
        }catch(Exception e){         
        }
    }
    public void draw(Graphics2D g2){
        
        int col = 0;
        int row = 0;
        int x = 0;
        int y = 0;
        
        while(col<gP.maxScreenCol && row< gP.maxScreenRow){
            
            int tileNum = mapTileNum[col][row];
            
            g2.drawImage(tile[tileNum].image, x , y ,gP.tileSize,gP.tileSize,null);
            col++;
            x += gP.tileSize;
            
            if(col == gP.maxScreenCol){
                col = 0;
                x = 0;
                row++;
                y += gP.tileSize;
            }
        }
        
   
    }
}
