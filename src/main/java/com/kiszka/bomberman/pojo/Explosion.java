package com.kiszka.bomberman.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Explosion implements Serializable {
    private int x;
    private int y;
    private long endTime;
}
