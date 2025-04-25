package com.kiszka.bomberman.ws;

import lombok.Data;

@Data
public class BombPlacement {
    private int playerId;
    private int x;
    private int y;
}
