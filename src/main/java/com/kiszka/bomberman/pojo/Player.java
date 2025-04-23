package com.kiszka.bomberman.pojo;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class Player {
    private int x;
    private int y;
    private int bombsLeft;
    private boolean isAlive;
}
