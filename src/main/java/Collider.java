package main.java;
import java.math.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javafx.scene.shape.Circle;
import javafx.util.Pair;
import javafx.scene.paint.Color;

// Collider mahdollistaa "puskurien" luomisen pelaajan ympärille

class Collider{
	
	String identifier;
	Actor actor;
	Double xOffset;
	Double yOffset;
	String orientation;
 
 
 Boolean collides = false;
 Double coillisionDistance = 35.0;
 
 
 //Luokan konstruktori
 public Collider(String identifier, Actor actor, Double xOffset, Double yOffset, String orientation ){
	 
	 this.identifier = identifier;
	 this.actor = actor;
	 this.xOffset = xOffset;
	 this.yOffset = yOffset;
	 this.orientation = orientation;
			 
	 
	 
 }
 
 public Pair<Double, Double> actorLocationInGame(){return actor.location.locationInGame();}
 
// Update-metodi tarkkailee colliderin tilaa
 public void update() {
  
   List<GameTile> nearbyTiles = actor.game.currentLevel.allTiles.stream()
		   .filter(tile -> tile.location.isNearCoord(actorLocationInGame(), 100));
   
   //Seinät 
  if (this.locations.stream().anyMatch(pos -> nearbyTiles.stream().anyMatch(tile -> tile.location.isNearCoord(pos, coillisionDistance)))){
   
    if (this.collides == false){ 
      actor.stop();
    }
    
    this.collides = true;
   
    
    
    //Tikkaat
  }else if(this.locations().stream().anyMatch(location -> nearbyTiles.filter(tile -> tile.isLadder()).stream().anyMatch(tile -> tile.location.isNearCoord(location, coillisionDistance + 10))){
  
    actor.isOnLadder = true;
    this.collides = false;
    
    
   //TriggerTiles
  }else if(this.actor instanceof Player && this.locations().stream().anyMatch(location -> nearbyTiles.stream().filter(tile -> tile instanceof TriggerTile ).stream().anyMatch(tile -> tile.location.isNearCoord(location, coillisionDistance + 10))){
  
    Tile nearbyTrigger = nearbyTiles.find(tile -> tile instanceof TriggerTile);
    if (nearbyTrigger.isPresent()) {
    	
    	TriggerTile near = (TriggerTile)nearbyTrigger.get();
    	near.trigger();
    }
    
    
    
    //Esineet
    }else if (actor.game.currentLevel.itemsInWorld.stream().anyMatch(exists(item -> item.isInWorld() && Helper.axisDistance(actor.location.locationInGame(), item.locationInWorld.get().locationInGame()).getKey() <=60 && Helper.axisDistance(actor.location.locationInGame(), item.locationInWorld.get().locationInGame()).getValue() <= 90 ))){
    
    Item nearbyItem = actor.game.currentLevel.itemsInWorld.find(item ->Helper.axisDistance(actor.location.locationInGame(), item.locationInWorld.get.locationInGame()).getKey()<=60 && Helper.axisDistance(actor.location.locationInGame(), item.locationInWorld.get.locationInGame).getValue()<=90);      
    
    if(nearbyItem.isPresent()) {
    	actor.pickUp(nearbyItem.get(), false);
    }
    		
  }else{
   
    this.collides = false;
    actor.isOnLadder = false;
  }
 }
  
  public Double x = actor.location.locationInGame().getKey() + xOffset;
  public Double y = actor.location.locationInGame().getValue() + yOffset;
  public Double imgX  = actor.location.locationInImage().getKey() + xOffset;
  public Double imgY = actor.location.locationInImage().getValue() + yOffset;
  
  //Määrittää sijainnit pelaajan ympärillä
  public ArrayList<Pair<Double, Double>> locations() {
    
	Double pHeight = (double) this.actor.height;
    Double pWidth = (double) this.actor.width;
    ArrayList<Pair<Double, Double>> done = new ArrayList<Pair<Double, Double>>();
    
    if (orientation == "horizontal") {
    	done.add(new Pair<Double, Double>(this.x-(pWidth/2) + 20 , this.y));
    	done.add(new Pair<Double, Double>(this.x-(pWidth/2) + 20 , this.y));
    	done.add(new Pair<Double, Double>(this.x-(pWidth/2) + 20 , this.y));
    }else{
    	done.add(new Pair<Double, Double>(this.x, this.y - (pHeight/2) + 36));
    	done.add(new Pair<Double, Double>(this.x, this.y));
    	done.add(new Pair<Double, Double>(this.x, this.y+(pHeight/2) -11));	
    }
    
    return done;
    
    }
  
    //Vastaavat sijainnit kuvan koordinaatistossa 
  
  public ArrayList<Pair<Double, Double>> locationsInImage() {
	    
		Double pHeight = (double) this.actor.height;
	    Double pWidth = (double) this.actor.width;
	    ArrayList<Pair<Double, Double>> done = new ArrayList<Pair<Double, Double>>();
	    
	    if (orientation == "horizontal") {
	    	done.add(new Pair<Double, Double>(this.imgX-(pWidth/2) + 20 , this.imgY));
	    	done.add(new Pair<Double, Double>(this.imgX-(pWidth/2) + 20 , this.imgY));
	    	done.add(new Pair<Double, Double>(this.imgX-(pWidth/2) + 20 , this.imgY));
	    }else{
	    	done.add(new Pair<Double, Double>(this.imgX, this.imgY - (pHeight/2) + 36));
	    	done.add(new Pair<Double, Double>(this.imgX, this.imgY));
	    	done.add(new Pair<Double, Double>(this.imgX, this.imgY+(pHeight/2) -11));	
	    }
	    
	    return done;
	    
	    }

  
  //Colliderian kuvat. Auttavat debuggauksessa
  public List<Circle> images() { 
	  
	 List<Circle> done = this.locationsInImage().stream()
			 .map(location -> new Circle(location.getKey(), location.getValue(), 1)).collect(Collectors.toList());
	 
	 done.stream().forEach(circle -> circle.setFill(Color.RED));
	 
	 return done;
  }
}