package com.forthenight.wingsjourney.game;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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

	@PostMapping("games")
	public ResponseEntity<Integer> uploadGame(@RequestBody Game gameData){
		Game game = new Game(
			0, 
			gameData.getName(), 
			gameData.getCompany(), 
			"", 
			gameData.getGenre(), 
			gameData.getDescription(), 
			0, 
			0
		);
		gameRepository.save(game);
		return ResponseEntity.ok(gameRepository.findByName(game.getName()).getId());
	}

	@PostMapping(value = "games/image/{id}")
	public ResponseEntity<String> uploadGameImage(@PathVariable Integer id, @RequestBody MultipartFile image) 
	throws IllegalStateException, IOException{
		Game game = gameRepository.findById(id).get();

        Path dir = Paths.get("images");
        String fileName = image.getOriginalFilename();
        Path imagePath = dir.resolve(fileName);
        Files.copy(image.getInputStream(), imagePath);

        game.setImage("http://localhost:8081/images/"+fileName);
        gameRepository.save(game);

		return ResponseEntity.ok("Game image uploaded successfully."+imagePath);
	}

	@GetMapping("games/id/{id}")
	public Game getGameById(@PathVariable Integer id){
		return gameRepository.findById(id).get();
	}
	
}
