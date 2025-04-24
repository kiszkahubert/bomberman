package com.kiszka.bomberman.controllers;

import com.kiszka.bomberman.GameStateRepository;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class PageController {
    private final GameStateRepository gameStateRepository;
    public PageController(GameStateRepository gameStateRepository){
        this.gameStateRepository = gameStateRepository;
    }
    @GetMapping("/")
    public String getHomePage(){
        return "mainPage";
    }
    @GetMapping("/game/{gameId}/{playerId}")
    public String getGamePage(@PathVariable String gameId, @PathVariable String playerId, Model model){
        model.addAttribute("gameState", gameStateRepository.findById(gameId));
        model.addAttribute("playerId",playerId);
        model.addAttribute("gameId",gameId);
        return "index";
    }
}
