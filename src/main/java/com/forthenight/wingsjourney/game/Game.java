package com.forthenight.wingsjourney.game;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;

@Entity
public class Game {
	@Id
	@GeneratedValue
	private Integer id;
	private String name;
	private String company;
	private String image;
	private String genre;
    @Lob
    @Column(length = 10000)
	private String description;
	private Integer likes;
	private Integer dislikes;

	public Game() {
		
	}
	
	public Game(Integer id, String name, String company, String image, String genre, String description, Integer likes, Integer dislikes) {
		this.id = id;
		this.name = name;
		this.company = company;
		this.image = image;
		this.genre = genre;
		this.description = description;
		this.likes = likes;
		this.dislikes = dislikes;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getGenre() {
		return genre;
	}

	public void setGenre(String genre) {
		this.genre = genre;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getLikes() {
		return likes;
	}

	public void setLikes(Integer likes) {
		this.likes = likes;
	}

	public Integer getDislikes() {
		return dislikes;
	}

	public void setDislikes(Integer dislikes) {
		this.dislikes = dislikes;
	}

	@Override
	public String toString() {
		return "Game [id=" + id + ", name=" + name + ", company=" + company + ", image=" + image + ", genre=" + genre
				+ ", description=" + description + ", likes=" + likes + ", dislikes=" + dislikes + "]";
	}
	
}
