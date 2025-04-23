package com.kiszka.bomberman.controllers;

import com.kiszka.bomberman.GameStateRepository;
import com.kiszka.bomberman.pojo.GameMap;
import com.kiszka.bomberman.pojo.GameState;
import com.kiszka.bomberman.pojo.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@RestController("/api")
public class ApiController {
    private final GameStateRepository gameStateRepository;
    public ApiController(GameStateRepository gameStateRepository) {
        this.gameStateRepository = gameStateRepository;
    }

    @PostMapping("/game/new")
    public ResponseEntity<GameState> createNewGameSession(){
        String gameId = UUID.randomUUID().toString();
        GameMap gameMap = new GameMap();
        List<Player> players = new ArrayList<>();
        players.add(new Player(60,60,3,true));
        players.add(new Player(780,60,3,true));
        players.add(new Player(60,780,3,true));
        players.add(new Player(780,780,3,true));
        GameState gameState = new GameState(gameId,players,gameMap);
        gameStateRepository.save(gameState,2, TimeUnit.HOURS);
        return ResponseEntity.ok(gameState);
    }
    @GetMapping("/game/{gameId}")
    public ResponseEntity<GameState> getExistingGameSession(@PathVariable String gameId){
        GameState gameState = gameStateRepository.findById(gameId);
        if(gameState == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(gameState);
    }
}
