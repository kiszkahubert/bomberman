package com.kiszka.bomberman.controllers;

import com.kiszka.bomberman.GameStateRepository;
import com.kiszka.bomberman.pojo.GameState;
import jakarta.servlet.http.HttpServletRequest;
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
    public String getGamePage(@PathVariable String gameId,
                              @PathVariable String playerId,
                              Model model,
                              HttpServletRequest request){
        GameState gameState = gameStateRepository.findById(gameId);
        if(gameState == null){
            return "redirect:/";
        }
        try{
            int pid = Integer.parseInt(playerId);
            if(pid < 1 || pid > 4){
                return "redirect:/";
            }
        } catch (Exception e){
            return "redirect:/";
        }
        model.addAttribute("gameState",gameState);
        model.addAttribute("playerId",playerId);
        model.addAttribute("gameId",gameId);
        request.getSession().setAttribute("playerId",playerId);
        return "index";
    }
}
