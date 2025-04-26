package com.kiszka.bomberman.ws;

import com.kiszka.bomberman.GameStateRepository;
import com.kiszka.bomberman.pojo.*;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;

@Controller
public class WebSocketController {
    private final SimpMessagingTemplate messagingTemplate;
    private final GameStateRepository gameStateRepository;
    private final ConcurrentMap<String, ScheduledFuture<?>> bombTasks = new ConcurrentHashMap<>();
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(4);
    public WebSocketController(SimpMessagingTemplate messagingTemplate, GameStateRepository gameStateRepository){
        this.messagingTemplate = messagingTemplate;
        this.gameStateRepository = gameStateRepository;
    }
    @MessageMapping("/game/{gameId}/move")
    public void handlePlayerMove(@DestinationVariable String gameId, @RequestBody PlayerMove move){
        GameState gameState = gameStateRepository.findById(gameId);
        if(gameState == null) return;
        gameState.getPlayers().stream()
                .filter(p -> p.getId() == move.getPlayerId())
                .findFirst()
                .ifPresent(player -> {
                    if(!isValidMove(player,move,gameState) && player.isAlive()){
                        player.setX(move.getX());
                        player.setY(move.getY());
                        gameStateRepository.save(gameState,2, TimeUnit.HOURS);
                        messagingTemplate.convertAndSend("/topic/game/"+gameId,gameState);
                    }
                });
    }
    @MessageMapping("/game/{gameId}/placeBomb")
    public void handleBombPlacing(@DestinationVariable String gameId, @RequestBody BombPlacement placement){
        GameState gameState = gameStateRepository.findById(gameId);
        if(gameState == null) return;
        final int[] cords = new int[2];
        gameState.getPlayers().stream()
                .filter(p->p.getId() == placement.getPlayerId())
                .findFirst()
                .ifPresent(player -> {
                    if(player.getBombsLeft() > 0){
                        Bomb bomb = new Bomb(player.getX(), player.getY(), 3, player.getId());
                        if(!gameState.getBombs().contains(bomb)){
                            player.setBombsLeft(player.getBombsLeft()-1);
                            gameState.getBombs().add(bomb);
                            gameStateRepository.save(gameState,2,TimeUnit.HOURS);
                            messagingTemplate.convertAndSend("/topic/game/"+gameId, gameState);
                            ScheduledFuture<?> future = scheduler.schedule(() -> {
                                handleBombDetonation(gameId, bomb);
                                bombTasks.remove(gameId+":"+bomb.hashCode());
                            },3,TimeUnit.SECONDS);
                            bombTasks.put(gameId + ":" + bomb.hashCode(), future);
                        }
                    }
                });

    }
    private void handleBombDetonation(String gameId, Bomb bomb){
        GameState gameState = gameStateRepository.findById(gameId);
        if(gameState == null) return;
        synchronized (gameState){
            if(gameState.getBombs().remove(bomb)) {
                gameState.getPlayers().stream()
                        .filter(p -> p.getId() == bomb.getPlayerId())
                        .findFirst()
                        .ifPresent(player -> player.setBombsLeft(player.getBombsLeft() + 1));
                var gameMap = gameState.getGameMap().getGameMap();
                var bombX = bomb.getX();
                var bombY = bomb.getY();
                int explosionRadius = 3;
                List<Explosion> explosions = new ArrayList<>();
                long explosionEndTime = System.currentTimeMillis() + 1000;
                for (int direction = 0; direction < 4; direction++) {
                    for (int distance = 0; distance <= explosionRadius; distance++) {
                        int x = bombX;
                        int y = bombY;
                        switch (direction) {
                            case 0: x = bombX + distance * 60; break;
                            case 1: x = bombX - distance * 60; break;
                            case 2: y = bombY + distance * 60; break;
                            case 3: y = bombY - distance * 60; break;
                        }
                        Block currentBlock = new Block(x, y);
                        if (!gameMap.containsKey(currentBlock)) {
                            break;
                        }
                        explosions.add(new Explosion(x,y,explosionEndTime));
                        if (gameMap.get(currentBlock)) {
                            gameMap.put(currentBlock, false);
                        }
                    }
                }
                var players = gameState.getPlayers();
                for(var exp : explosions){
                    gameState.getExplosions().add(exp);
                    for(var p: players){
                        if(p.getX() == exp.getX() && p.getY() == exp.getY()){
                            p.setAlive(false);
                        }
                    }
                }
                gameStateRepository.save(gameState, 2, TimeUnit.HOURS);
                messagingTemplate.convertAndSend("/topic/game/" + gameId, gameState);
                scheduler.schedule(() -> {
                    GameState updatedState = gameStateRepository.findById(gameId);
                    if (updatedState != null) {
                        synchronized (updatedState) {
                            var explosionsToKeep = updatedState.getExplosions()
                                    .stream()
                                    .filter(e -> e.getEndTime() > System.currentTimeMillis())
                                    .toList();
                            updatedState.setExplosions(explosionsToKeep);
                            gameStateRepository.save(updatedState, 2, TimeUnit.HOURS);
                            messagingTemplate.convertAndSend("/topic/game/" + gameId, updatedState);
                        }
                    }
                }, 1, TimeUnit.SECONDS);
            }
        }
    }
    private boolean isValidMove(Player player, PlayerMove move, GameState gameState){
        int dx = Math.abs(player.getX() - move.getX());
        int dy = Math.abs(player.getY() - move.getY());
        if((dx != 60 && dx != 0) || (dy != 60 && dy != 0) || (dx == 60 && dy == 60)) return false;
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
