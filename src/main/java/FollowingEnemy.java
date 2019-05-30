package main.java;

import javafx.scene.shape.Rectangle;
import javafx.util.Pair;
import javafx.scene.image.*;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Color.*;
import javafx.scene.input.*;
import javafx.animation.*;
import javafx.event.*;
import java.math.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Random;
import javafx.scene.media.*;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.media.AudioClip;

class FollowingEnemy extends Enemy{
  
  
  Double locationX;
  Double locationY;

  private int jumpCount = 0;
  
  //Vihollisen kuva
  private AnimatedGameSprite body = new AnimatedGameSprite("file:src/main/resources/Pictures/CorruptedMoonmanWalk", "MoonmanWalk", 6, ".png", Optional.empty(), new Pair<Double, Double>(60.0,90.0), this, new Pair<Double, Double>(-30.0,-45.0), false);

  private AnimatedGameSprite shield = new AnimatedGameSprite("file:src/main/resources/Pictures/ShieldAnimated", "Shield", 5, ".png", Optional.empty(), new Pair<Double, Double>(60.0,90.0), this, new Pair<Double, Double>(-30.0,-45.0), true);

  
  //Vihollisen Colliderit
  private Collider northCollider;
  private Collider southCollider;
  private Collider eastCollider;
  private Collider westCollider;
  public ArrayList<Collider> colliders = new ArrayList<Collider>();
  
  //Audio
  private AudioClip shieldSound;
  private AudioClip hurtSound = new AudioClip("file:src/main/resources/sound/CorruptedHurt.wav");
  
  //Konstruktori
  public FollowingEnemy(String name, Game game,  Double locationX, Double locationY) {
	  
	  this.name = name;
	  this.game = game;
	  this.locationX = locationX;
	  this.locationY = locationY;
	  
	  HP = 500.0;
	  energy = 500.0;
	  isActive = false;
	  currentAction = "";
	  
	  lookDirectionForSprite = "east";
	  
	  this.inventory = new HashMap<String, Item>();
	  
	  location = new GamePos(new Pair<Double, Double>(locationX, locationY), false);
	  locationForSprite = Optional.of(location.locationInImage());
	  isMovingForSprite = false;
	  
	 
	  northCollider = new Collider("Enorth", this, 0.0,  -this.body.spriteHeight/2.0 +15, "horizontal");
	  southCollider = new Collider("Esouth", this, 0.0,  this.body.spriteHeight/2.0 -12, "horizontal");
	  eastCollider = new Collider("Eeast", this, this.body.spriteWidth/2.0, -30.0, "vertical");
	  westCollider = new Collider("Ewest", this, -this.body.spriteWidth/2.0, -30.0, "vertical");
	  
	  shieldSound = this.game.player.shieldSound;
	  
	  this.colliders.add(northCollider);
	  this.colliders.add(eastCollider);
	  this.colliders.add(southCollider);
	  this.colliders.add(westCollider);
	  

  }
  
  
  
  public void update() {

	  
	  if(!this.arm.isPresent()) this.arm = Optional.of(new RotatingArm(this, new DirectionVector(this.location.locationInImage(), GameWindow.currentGame.player.location.locationInImage())));
	//Lisätään esine vihollisen tavaraluetteloon
	if(this.inventory.isEmpty()) this.inventory.put ("Health Pack", new HealthPack(this.game, 5));
    
    if(!this.isActive && this.absDistToPlayer() <= (GameWindow.stage.getWidth()/2)+200) {
    	this.isActive = true; //Aktiivisuuden säätely
    }else if(this.absDistToPlayer() > (GameWindow.stage.getWidth()/2)+200) {
    	this.isActive = false;
    }
    
     //Aina suoritettavat toiminnot
    
    this.colliders.stream().forEach(collider -> collider.update());
    if(!this.southCollider.collides) { this.ySpeed += 1.0;}
    this.arm.get().direction.update(this.location.locationInImage(), this.game.player.location.locationInImage());
    if(this.isShielding) { this.energy = this.energy - 5.0;}
    if(this.isShielding && !this.shieldSound.isPlaying()) {this.shieldSound.play();}
    if(this.energy <= 0 ) { this.isShielding = false;}
    if(this.location.locationInGame().getValue() > this.game.currentLevel.dimensions().getValue()) { this.takeDamage(9999.0); }
    
    locationForSprite = Optional.of(location.locationInImage());
	
    if(this.xSpeed > 0) isMovingForSprite = true;
    else isMovingForSprite = false;
    
     //Esineiden pudottaminen
    try{
    if(this.isDead()) { this.drop((Item)this.inventory.values().toArray()[0]);}
    }catch(NoSuchElementException x){
      System.out.println("FollowingEnemy item drop is acting up.");
    }
    //Kun vihollinen on aktiivinen
    if(this.isActive){
      
      if(this.playerLocator().getKey() == "left" && !this.isStuck()) {this.xSpeed = -2.0;}
      if(this.playerLocator().getKey() == "right" && !this.isStuck()) {this.xSpeed = 2.0;}
      
      this.move();
      
      if(this.energy >0 && game.projectiles.stream().anyMatch(bolt -> Helper.absoluteDistance(this.location.locationInGame(), bolt.location.locationInGame()) < 300 && bolt.shooter != this)) {
    	  this.shieldOn();
      }else {
    	  this.shieldOff();
      }
      
    }
    
    //Kun vihollinen ei ole aktiivinen
    if(!this.isActive){
      this.stop();
    }
    
   
  }

//Vihollisen toiminnot
//#########################################################################################################################################################
 
  
  
 private Pair<String, String> playerLocator() {
    
    Double xDiff = this.distToPlayer().getKey();
    Double yDiff = this.distToPlayer().getValue();
    
    String xStatus = "";
    String yStatus = "";
    
    if(xDiff < -50) {xStatus = "left";}
    else if(xDiff > 50) {xStatus = "right";}
    else {xStatus = "nearby";}
      
    if(yDiff < -5) {yStatus = "above";}
    else if(yDiff > 5) {yStatus = "below";}
    else {yStatus = "onlevel";}
    
    return new Pair<String, String>(xStatus, yStatus);
      
  }
  
 private void shoot() {
	 
	 if(this.equippedWeapon.isPresent()) {
		 
		 if(this.game.time % 50 == 0) {this.equippedWeapon.get().fire();} 
	
	 }else{
		
		 this.equipWeapon(Optional.of(new RapidFireWeapon(this.game, Optional.of(this))));
	 }
  }
  
 public void stop()  {
    this.xSpeed = 0.0;
    this.ySpeed = 0.0;
    this.isMovingForSprite = false;
  } 
 
 
 //Move metodi hallitsee tämän vihollisen liikkumista playerlocatorin tietojen avulla
  private void move() {
    
    if(!this.isStuck() && ((this.playerLocator().getKey() != "nearby") || ((this.playerLocator().getValue() != "onlevel") || !this.isOnLadder))){
      
      
      this.location.move(this.xSpeed, this.ySpeed);
     
      this.isMovingForSprite = true;
      
    }else{
    	this.stop();
    }
    
    
    if(this.xSpeed<0) {this.lookDirectionForSprite = "west";}
    if(this.xSpeed>0) {this.lookDirectionForSprite = "east";}
    
    if(this.northCollider.collides) {this.stop();}
    
    if(this.eastCollider.collides ^ this.westCollider.collides) {this.jump(10.0);}
    
    if(this.isOnLadder && this.playerLocator().getValue() == "above") {this.ySpeed = -5.0;}
    else if(!this.isOnLadder && this.playerLocator().getKey() == "nearby" && this.playerLocator().getValue() == "above") {this.jump(10.0);}
    
    this.shoot();
    
     if(this.isStuckBelow()){
       
        location.move(0.0, -1.0);
       // Pelastetaan jumiutunut vihollinen
     }
    
    if(this.isStuckAbove()){
      
      this.location.move(0.0, 7.0);
      
    }
  }
  
  private void jump(Double strength) {
    
   if(this.jumpCount < 3){
     this.ySpeed = -strength;
     this.jumpCount += 1;
   }else if(this.southCollider.collides){
     this.jumpCount = 0;
     this.ySpeed = -strength;
     this.jumpCount += 1;
   }
  }
  
  private void shieldOn() { if(this.energy > 0) {this.isShielding = true;}}
  private void shieldOff() { {this.isShielding = false;} }
    
//#########################################################################################################################################################

  public Group image() {
	  
	  Group done = new Group(); 
	  if(!this.isShielding) done.getChildren().addAll(body.image(), arm.get().completeImage());
	  else done.getChildren().addAll(body.image(), arm.get().completeImage(), shield.image());
	  
	  return done;
  }
  
  public void takeDamage(Double amount) {
    
    this.HP = this.HP - amount;
    this.jump(5.0);
    this.hurtSound.play();
    this.xSpeed =  this.xSpeed - 10.0;
     
  }
  
  //Metodi, joka vähentää vihollisen energiaa. Käytetään pelin lataamisen yhteydessä
  public void useEnergy(Double amount) {
    this.energy = this.energy - amount;
  }
  
 public Boolean isStuck() { return isStuckBelow() || isStuckAbove() ;}
  
 private Boolean isStuckBelow() { return this.eastCollider.collides && this.westCollider.collides && this.southCollider.collides; }
  
 private Boolean isStuckAbove() { return this.eastCollider.collides && this.westCollider.collides && this.northCollider.collides; }
  
 private Pair<Double, Double> distToPlayer() { return new Pair<Double, Double>(this.game.player.location.locationInGame().getKey() - this.location.locationInGame().getKey(), this.game.player.location.locationInGame().getValue() - this.location.locationInGame().getValue());}
  
 private Double absDistToPlayer() {return Helper.absoluteDistance(this.game.player.location.locationInGame(), this.location.locationInGame());}

 
}