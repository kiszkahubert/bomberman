package com.kiszka.bomberman.pojo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Player implements Serializable {
    private int id;
    private int x;
    private int y;
    private int bombsLeft;
    private boolean isAlive;
    private String color;
}
