package com.kiszka.bomberman.ws;

import com.kiszka.bomberman.GameStateRepository;
import com.kiszka.bomberman.pojo.GameState;
import com.kiszka.bomberman.pojo.Player;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;

import java.security.Principal;
import java.util.concurrent.TimeUnit;

@Controller
public class WebSocketController {
    private final SimpMessagingTemplate messagingTemplate;
    private final GameStateRepository gameStateRepository;
    public WebSocketController(SimpMessagingTemplate messagingTemplate, GameStateRepository gameStateRepository){
        this.messagingTemplate = messagingTemplate;
        this.gameStateRepository = gameStateRepository;
    }
    @MessageMapping("/game/{gameId}/move")
    public void handlePlayerMove(@DestinationVariable String gameId, @RequestBody PlayerMove move, Principal principal){
        GameState gameState = gameStateRepository.findById(gameId);
        if(gameState == null){
            return;
        }
        gameState.getPlayers().stream()
                .filter(p -> p.getId() == move.getPlayerId())
                .findFirst()
                .ifPresent(player -> {
                    if(!isValidMove(player,move,gameState)){
                        player.setX(move.getX());
                        player.setY(move.getY());
                        gameStateRepository.save(gameState,2, TimeUnit.HOURS);
                        messagingTemplate.convertAndSend("/topic/game/"+gameId,gameState);
                    }
                });
    }
    private boolean isValidMove(Player player, PlayerMove move, GameState gameState){
        int dx = Math.abs(player.getX() - move.getX());
        int dy = Math.abs(player.getY() - move.getY());
        if((dx != 60 && dx != 0) || (dy != 60 && dy != 0) || (dx == 60 && dy == 60)){
            return false;
        }
        return !checkCollision(move.getX(),move.getY(),gameState);
    }
    private boolean checkCollision(int x, int y, GameState gameState){
        var map = gameState.getGameMap().getGameMap();
        for(var entry: map.entrySet()){
            int mapX = entry.getKey().getX();
            int mapY = entry.getKey().getY();
            if(x == mapX && y == mapY && entry.getValue()){
                return false;
            } else if (x == mapX && y == mapY) {
                return true;
            }
        }
        return true;
    }
}
