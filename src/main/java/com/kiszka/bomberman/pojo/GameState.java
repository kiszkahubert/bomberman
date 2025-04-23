package com.kiszka.bomberman.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class GameState {
    private String gameId;
    private List<Player> players;

}
