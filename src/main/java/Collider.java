package main.java;
import java.math.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
		   .filter(tile -> tile.location.isNearCoordPair(actorLocationInGame(), 100.0))
		   .collect(Collectors.toList());
   
   List<GameTile> colliderTiles = nearbyTiles.stream()
		   .filter(tile -> tile.hasCoillision)
		   .collect(Collectors.toList());
   
   List<GameTile> ladderTiles = nearbyTiles.stream()
		   .filter(tile -> tile.isLadder)
		   .collect(Collectors.toList());
   
   List<GameTile> triggerTiles = nearbyTiles.stream()
		   .filter(tile -> tile instanceof TriggerTile)
		   .collect(Collectors.toList());
   
   List<Item> nearbyItems = actor.game.currentLevel.itemsInWorld.stream()
		   .filter(item -> Helper.axisDistance(actor.location.locationInGame(), item.locationInWorld.get().locationInGame()).getKey() <=60 && Helper.axisDistance(actor.location.locationInGame(), item.locationInWorld.get().locationInGame()).getValue() <= 90 )
		   .collect(Collectors.toList());
   
  
   
   //Seinät 
  if (this.locations().stream().anyMatch(pos -> tileIsInList(colliderTiles, pos, coillisionDistance))){
   
    if (this.collides == false){ 
      actor.stop();
    }
    
    this.collides = true;
   
    
    
    //Tikkaat
  }else if(this.locations().stream().anyMatch(pos -> tileIsInList(ladderTiles, pos, coillisionDistance + 10))){
  
    actor.isOnLadder = true;
    this.collides = false;
    
    
   //TriggerTiles
  }else if(this.actor instanceof Player && this.locations().stream().anyMatch(pos -> tileIsInList(triggerTiles, pos, coillisionDistance + 10)) ){
  
    Optional<GameTile> nearbyTrigger = triggerTiles.stream().findAny();
    
    if (nearbyTrigger.isPresent()) {
    	TriggerTile near = (TriggerTile)nearbyTrigger.get();
    	near.trigger();
    }
    
    
    
    //Esineet
    }else if (!nearbyItems.isEmpty()){
    
    Optional<Item> nearbyItem = nearbyItems.stream().findAny();      
    
    if(nearbyItem.isPresent()) {
    	actor.pickUp(nearbyItem.get(), false);
    }
    		
  }else{
   
    this.collides = false;
    actor.isOnLadder = false;
  }
 }
  
  //Määrittää sijainnit pelaajan ympärillä
  public ArrayList<Pair<Double, Double>> locations() {
	  
	  Double x = actor.location.locationInGame().getKey() + xOffset;
	  Double y = actor.location.locationInGame().getValue() + yOffset;
	 
	Double pHeight = (double) this.actor.height;
    Double pWidth = (double) this.actor.width;
    ArrayList<Pair<Double, Double>> done = new ArrayList<Pair<Double, Double>>();
    
    if (orientation == "horizontal") {
    	done.add(new Pair<Double, Double>(x-(pWidth/2) + 20 , y));
    	done.add(new Pair<Double, Double>(x-(pWidth/2) + 20 , y));
    	done.add(new Pair<Double, Double>(x-(pWidth/2) + 20 , y));
    }else{
    	done.add(new Pair<Double, Double>(x, y - (pHeight/2) + 36));
    	done.add(new Pair<Double, Double>(x, y));
    	done.add(new Pair<Double, Double>(x, y+(pHeight/2) -11));	
    }
    
    return done;
    
    }
  
    //Vastaavat sijainnit kuvan koordinaatistossa 
  
  public ArrayList<Pair<Double, Double>> locationsInImage() {
	    
		Double pHeight = (double) this.actor.height;
	    Double pWidth = (double) this.actor.width;
	    Double imgX  = actor.location.locationInImage().getKey() + xOffset;
		Double imgY = actor.location.locationInImage().getValue() + yOffset;
		
	    ArrayList<Pair<Double, Double>> done = new ArrayList<Pair<Double, Double>>();
	    
	    if (orientation == "horizontal") {
	    	done.add(new Pair<Double, Double>(imgX-(pWidth/2) + 20 , imgY));
	    	done.add(new Pair<Double, Double>(imgX-(pWidth/2) + 20 , imgY));
	    	done.add(new Pair<Double, Double>(imgX-(pWidth/2) + 20 ,imgY));
	    }else{
	    	done.add(new Pair<Double, Double>(imgX, imgY - (pHeight/2) + 36));
	    	done.add(new Pair<Double, Double>(imgX, imgY));
	    	done.add(new Pair<Double, Double>(imgX, imgY+(pHeight/2) -11));	
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
  
  public Boolean tileIsInList(List<GameTile>list, Pair<Double, Double> location, Double distance) {
	  
	  return list.stream().anyMatch(tile -> tile.locationForCollider.isNearCoordPair(location, distance));
	  
  }
}