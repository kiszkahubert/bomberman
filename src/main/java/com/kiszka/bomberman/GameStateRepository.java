package com.kiszka.bomberman;

import com.kiszka.bomberman.pojo.GameState;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.concurrent.TimeUnit;

@Repository
public class GameStateRepository {
    private static final String KEY_PREFIX = "game:";
    private final RedisTemplate<String, Object> redisTemplate;
    public GameStateRepository(RedisTemplate<String,Object> redisTemplate){
        this.redisTemplate = redisTemplate;
    }
    public void save(GameState gameState, long ttl, TimeUnit timeUnit){
        redisTemplate.opsForValue().set(
                KEY_PREFIX + gameState.getGameId(),
                gameState,
                ttl,
                timeUnit
        );
    }
    public GameState findById(String gameId){
        return (GameState) redisTemplate.opsForValue().get(KEY_PREFIX + gameId);
    }
    public void delete(String gameId){
        redisTemplate.delete(KEY_PREFIX + gameId);
    }
}
