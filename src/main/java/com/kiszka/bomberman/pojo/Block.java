package com.kiszka.bomberman.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.Objects;

@Data
@AllArgsConstructor
public class Block implements Serializable {
    int x;
    int y;
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Block block = (Block) o;
        return x == block.x && y == block.y;
    }
    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
    @Override
    public String toString(){
        return x + "," +y;
    }
    public static Block fromString(String str) {
        String[] parts = str.split(",");
        return new Block(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]));
    }
}