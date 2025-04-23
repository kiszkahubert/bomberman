package com.kiszka.bomberman.pojo;

import lombok.AllArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
public class Player implements Serializable {
    private int x;
    private int y;
    private int bombsLeft;
    private boolean isAlive;
}
