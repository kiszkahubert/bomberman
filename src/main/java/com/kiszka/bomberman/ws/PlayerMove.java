package com.kiszka.bomberman.ws;

import lombok.Data;

@Data
public class PlayerMove {
    private int playerId;
    private int x;
    private int y;
}
