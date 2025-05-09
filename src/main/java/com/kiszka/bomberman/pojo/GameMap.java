package com.kiszka.bomberman.pojo;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Assumption is made that there will always be 4 players for game to start
 * Each player will spawn in each corner of the map and each player will have random amount of free space in their neighbourhood amounting to at least 2 blocks in X and Y axis
 */
@Data
public class GameMap implements Serializable {
    private static final int BOARD_WIDTH = 900;
    private static final int BOARD_HEIGHT = 900;
    private static final int CELL_SIZE = 60;
    private static final int INNER_GRID_OFFSET = 180;
    private static final int INNER_GRID_SPACING = 120;
    @JsonIgnore
    private final transient Random rand = new Random();
    /**
     * Map<Block, Boolean>
     * First param holds top left corner position of a block and second parameter holds the information if the block has been filled or not
     */
    private final Map<Block, Boolean> gameMap = new HashMap<>();
    public GameMap(){
        generateBaselineGrid();
        fillGridWithBlocks();
    }
    private void generateBaselineGrid(){
        //top and bottom
        for(int i=0; i<BOARD_WIDTH/CELL_SIZE-2; i++){
            gameMap.put(new Block(CELL_SIZE+CELL_SIZE*i, CELL_SIZE), false);
            gameMap.put(new Block(CELL_SIZE+CELL_SIZE*i, BOARD_HEIGHT - CELL_SIZE*2), false);
        }
        //left and right
        for(int i=1;i<BOARD_HEIGHT/CELL_SIZE-2;i++){
            gameMap.put(new Block(CELL_SIZE, CELL_SIZE+CELL_SIZE*i),false);
            gameMap.put(new Block(BOARD_WIDTH-CELL_SIZE*2, CELL_SIZE+CELL_SIZE*i),false);
        }
        for(int i=0; i<6; i++){
            for(int j=0; j<5;j++){
                int x = INNER_GRID_OFFSET + (INNER_GRID_SPACING*j);
                int y = INNER_GRID_SPACING + (INNER_GRID_SPACING*i);
                gameMap.put(new Block(x, y),false);
            }
        }
        for(int i=0; i<5; i++){
            for(int j=0; j<11; j++){
                int x = INNER_GRID_SPACING + (60*j);
                int y = INNER_GRID_OFFSET + (INNER_GRID_SPACING*i);
                gameMap.put(new Block(x, y),false);
            }
        }
    }
    private void fillGridWithBlocks(){
        for (Map.Entry<Block, Boolean> entry : gameMap.entrySet()) {
            Block block = entry.getKey();
            boolean isPlayerSpawn = false;
            //Player1 spawn
            if((block.x == 60 && (block.y == 60 || block.y == 120)) || (block.x == 120 && block.y == 60)){
                isPlayerSpawn = true;
            }
            //Player2 spawn
            if((block.x == 780 && (block.y == 60 || block.y == 120)) || (block.x == 720 && block.y == 60)){
                isPlayerSpawn = true;
            }
            //Player3 spawn
            if((block.x == 60 && (block.y == 720 || block.y == 780)) || (block.x == 120 && block.y == 780)){
                isPlayerSpawn = true;
            }
            //Player4 spawn
            if((block.x == 780 && (block.y == 720 || block.y == 780)) || (block.x == 720 && block.y == 780)){
                isPlayerSpawn = true;
            }
            if(!isPlayerSpawn){
                entry.setValue(rand.nextInt(10) < 7);
            }
        }
    }
}

