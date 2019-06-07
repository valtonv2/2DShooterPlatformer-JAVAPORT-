package main.java;

import java.util.Optional;

import javafx.util.Pair;

/*Seuraava luokka kuvaa pelin taustalla nähtävää liikkuvaa efektiä.
 * 
 * Efekti on yksinkertainen liikkuva kuva.
*/

class Effect extends UsesGameSprite{
	
	GamePos startLocation = new GamePos(new Pair<Double, Double>(0.0,0.0), false);
	GamePos location = new GamePos(new Pair<Double, Double>(0.0,0.0), false);
	Double limitDistance;
	GameSprite image;
	Double xSpeed = 0.0;
	Double ySpeed = 0.0;
	
	
	//Konstruktori 1
	
	public Effect(String imgPath,Double xDim, Double yDim, GamePos startLocation, Double xSpeed, Double ySpeed, Double limitDistance) {
		
		this.startLocation = startLocation;
		this.location = startLocation;
		this.image = new GameSprite(
				imgPath, 
				Optional.of(startLocation.locationInImage()), 
				new Pair<Double, Double>(xDim, yDim), 
				this, 
				new Pair<Double, Double>(0.0, 0.0),
				Optional.empty()
				);
		this.xSpeed = xSpeed;
		this.ySpeed = ySpeed;
		this.limitDistance = limitDistance;
		
	}
	
	//Konstruktori 2
	
	public Effect(String imgPath,Double xDim, Double yDim, Double xSpeed, Double ySpeed, Double limitDistance) {
	
		this.image = new GameSprite(
				imgPath, 
				Optional.of(startLocation.locationInImage()), 
				new Pair<Double, Double>(xDim, yDim), 
				this, 
				new Pair<Double, Double>(0.0, 0.0),
				Optional.empty()
				);
		this.xSpeed = xSpeed;
		this.ySpeed = ySpeed;
		this.limitDistance = limitDistance;
		
	}
	
	
	public void move() {
		
		if(this.location.distance(this.startLocation) > this.limitDistance) {
			
			this.location.teleport(this.startLocation.locationInGame());
		
		}else {
			
			this.location.move(xSpeed, ySpeed);
		
		}	
	}
	
	public Optional<Pair<Double, Double>> locationForSprite(){
		return Optional.of(location.locationInImage());
	}
	
	
	
	
	
	
	
	
	
}