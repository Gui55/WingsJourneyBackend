package com.forthenight.wingsjourney.game.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.forthenight.wingsjourney.game.Game;

public interface GameRepository extends JpaRepository<Game, Integer>{
    public Game findByName(String name)
;}
