package com.forthenight.wingsjourney.game;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.forthenight.wingsjourney.game.repository.GameRepository;

@CrossOrigin
@RestController
public class GameResource {
	
	private GameRepository gameRepository;

	public GameResource(GameRepository gameRepository) {
		this.gameRepository = gameRepository;
	}

	@GetMapping("games")
	public List<Game> getAllGames(){
		return gameRepository.findAll();
	}
	
}
