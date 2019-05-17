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
	Integer xOffset;
	Integer yOffset;
	String orientation;
 
 
 Boolean collides = false;
 Double coillisionDistance = 35.0;
 
 //Luokan konstruktori
 public Collider(String identifier, Actor actor, Integer xOffset, Integer yOffset, String orientation ){
	 
	 this.identifier = identifier;
	 this.actor = actor;
	 this.xOffset = xOffset;
	 this.yOffset = yOffset;
	 this.orientation = orientation;
			 
	 
	 
 }
 
 public Pair<Double, Double> actorLocationInGame(){ return actor.location.locationInGame();}
 
// Update-metodi tarkkailee colliderin tilaa
 public void update() {
  
   List<GameTile> nearbyTiles = actor.game.currentLevel.allTiles.stream().filter(tile -> Helper.absoluteDistance(tile.location.locationInGame(), actorLocationInGame)<100).collect(Collectors.toList());
   
   //Seinät 
  if (this.locations.stream().anymatch(location -> nearbyTiles.stream().filter(tile -> tile.hasCoillision).map(tile -> tile.locationForCollider()).anyMatch(location2 -> Helper.axisDistance(location, location2).getKey()  <= coillisionDistance && Helper.axisDistance(location, location2).getValue() <= coillisionDistance))){
   
    if (this.collides == false){ 
      actor.stop
      println("Bang")
    }
    
    this.collides = true
   
    
    
    //Tikkaat
  }else if(this.locations.exists(location => nearbyTiles.filter(_.isLadder).map(tile => tile.locationForCollider).exists(location2 => Helper.axisDistance(location, location2)._1 <= coillisionDistance + 10 && Helper.axisDistance(location, location2)._2 <= coillisionDistance + 10))){
  
    actor.isOnLadder = true
    this.collides = false
    
    
   //TriggerTiles
  }else if(this.actor.isInstanceOf[Player] && this.locations.exists(location => nearbyTiles.filter(_.isInstanceOf[TriggerTile]).map(tile => tile.locationForCollider).exists(location2 => Helper.axisDistance(location, location2)._1 <= coillisionDistance + 10 && Helper.axisDistance(location, location2)._2 <= coillisionDistance + 10))){
  
    val nearbyTrigger = nearbyTiles.find(_.isInstanceOf[TriggerTile])
    if (nearbyTrigger.isDefined) nearbyTrigger.get.asInstanceOf[TriggerTile].trigger
    
    
    
    //Esineet
    }else if (actor.game.currentLevel.itemsInWorld.exists(item => item.isInWorld && Helper.axisDistance(actor.location.locationInGame, item.locationInWorld.get.locationInGame)._1 <=60 && Helper.axisDistance(actor.location.locationInGame, item.locationInWorld.get.locationInGame)._2 <=90 )){
    val nearbyItem = actor.game.currentLevel.itemsInWorld.find(item=>Helper.axisDistance(actor.location.locationInGame, item.locationInWorld.get.locationInGame)._1<=60 && Helper.axisDistance(actor.location.locationInGame, item.locationInWorld.get.locationInGame)._2<=90)      
    nearbyItem match {
      
      case Some(thing) => actor.pickUp(thing, false)
      case None =>
      }
    
  }else{
   
    this.collides = false
    actor.isOnLadder = false
    
  }
   
  }
  
  def x = actor.location.locationInGame._1 + xOffset
  def y = actor.location.locationInGame._2 + yOffset
  def imgX  = actor.location.locationInImage._1 + xOffset
  def imgY = actor.location.locationInImage._2 + yOffset
  
  //Määrittää sijainnit pelaajan ympärillä
  public Array<Optional<Pair<Double, Double>>> locations() {
    Double pHeight = (double) this.actor.height;
    Double pWidth = (double) this.actor.width;
    Array<Optional>
    if (orientation == "horizontal")
       (this.x-(pWidth/2) + 20 , this.y), (this.x, this.y), (this.x + (pWidth/2) - 20, this.y) )
    else
      Vector( (this.x, this.y - (pHeight/2) + 36 ), (this.x, this.y), (this.x, this.y+(pHeight/2) -11) )
    
    }
  
    //Vastaavat sijainnit kuvan koordinaatistossa 
    def imageLocations = {
    val pHeight = this.actor.height.toDouble
    val pWidth = this.actor.width.toDouble
    if (orientation == "horizontal")
      Vector( (this.imgX-(pWidth/2) + 20 , this.imgY), (this.imgX, this.imgY), (this.imgX + (pWidth/2) - 20, this.imgY) )
    else
      Vector( (this.imgX, this.imgY - (pHeight/2) + 36 ), (this.imgX, this.imgY), (this.imgX, this.imgY+(pHeight/2) - 11 ) )
    
    }
  
  //Colliderian kuvat. Auttavat debuggauksessa
  def images = this.imageLocations.map(location => new Circle{
    
    fill = Red
    centerX = location._1
    centerY = location._2
    radius = 1
    
  })
   
}