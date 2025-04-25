package com.kiszka.bomberman.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GameState implements Serializable {
    private String gameId;
    private List<Player> players;
    private GameMap gameMap;
    private List<Bomb> bombs;
    private List<Explosion> explosions;
}
