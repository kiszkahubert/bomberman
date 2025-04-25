package com.kiszka.bomberman.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Bomb implements Serializable {
    private int x;
    private int y;
    private int ttl;
}
