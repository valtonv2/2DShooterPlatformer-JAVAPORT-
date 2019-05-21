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
import java.util.Optional;
import java.util.Random;
import javafx.scene.media.*;
import javafx.scene.Node;
import javafx.scene.media.AudioClip;

class FollowingEnemy extends Enemy{
  String name; 
  Game game;
  Double locationX;
  Double locationY;

  public GamePos location = new GamePos(new Pair<Double, Double>(locationX, locationY), false);
  public Double HP = 500.0;
  public Double energy = 500.0;
  public Boolean isActive = false;
  private String currentAction = "";
  private int jumpCount = 0;
  
  //Vihollisen kuva
  private AnimatedGameSprite body = new AnimatedGameSprite("file:src/main/resources/Pictures/CorruptedMoonmanWalk", "MoonmanWalk", 6, ".png", Optional.empty(), new Pair<Double, Double>(60.0,90.0), this, new Pair<Double, Double>(-30.0,-45.0), false);
  public Optional<RotatingArm> arm = Optional.of(new RotatingArm(this, new DirectionVector(this.location.locationInImage(), this.game.player.location.locationInImage())));
  private AnimatedGameSprite shield = new AnimatedGameSprite("file:src/main/resources/Pictures/ShieldAnimated", "Shield", 5, ".png", Optional.empty(), new Pair<Double, Double>(60.0,90.0), this, new Pair<Double, Double>(-30.0,-45.0), true);
  public String lookDirectionForSprite = "east";
  public Boolean isMovingForSprite = false;
  
  //Vihollisen Colliderit
  private Collider northCollider = new Collider("Enorth", this, 0.0,  -this.body.spriteHeight/2.0 +15, "horizontal");
  private Collider southCollider = new Collider("Esouth", this, 0.0,  this.body.spriteHeight/2.0 -12, "horizontal");
  private Collider eastCollider = new Collider("Eeast", this, this.body.spriteWidth/2.0, -30.0, "vertical");
  private Collider westCollider = new Collider("Ewest", this, -this.body.spriteWidth/2.0, -30.0, "vertical");
  public Collider colliders[] = {northCollider, eastCollider, southCollider, westCollider};
  
  //Audio
  private AudioClip shieldSound = this.game.player.shieldSound;
  private AudioClip hurtSound = new AudioClip("file:src/main/resources/sound/CorruptedHurt.wav");
  
  //Konstruktori
  public FollowingEnemy(String name, Game game,  Double locationX, Double locationY) {
	  
	  this.name = name;
	  this.game = game;
	  this.locationX = locationX;
	  this.locationY = locationY;
	  
	  //Lisätään esine vihollisen tavaraluetteloon
	  this.inventory.put ("Health Pack", new HealthPack(this.game, 5));

  }
  
  
  
  public void update() {
    
    if(!this.isActive && this.absDistToPlayer() <= (GameWindow.stage.width.toDouble/2)+200) {
    	this.isActive = true; //Aktiivisuuden säätely
    }else if(this.absDistToPlayer() > (GameWindow.stage.width.toDouble/2)+200) {
    	this.isActive = false;
    }
    
     //Aina suoritettavat toiminnot
    
    this.colliders.forEach(collider -> collider.update);
    if(!this.southCollider.collides) { this.ySpeed += 1.0;}
    this.arm.get().direction.update(this.location.locationInImage, this.game.player.location.locationInImage);
    if(this.isShielding) { this.energy = this.energy - 5.0;}
    if(this.isShielding && !this.shieldSound.isPlaying) {this.shieldSound.play();}
    if(this.energy <= 0 ) { this.isShielding = false;}
    if(this.location.locationInGame().getValue() > this.game.currentLevel.dimensions.getValue()) { this.takeDamage(9999); }
    
     //Esineiden pudottaminen
    try{
    if(this.isDead) { this.drop(this.inventory.values.head);}
    }catch(NoSuchElementException x){
      System.out.println("FollowingEnemy item drop is acting up.");
    }
    //Kun vihollinen on aktiivinen
    if(this.isActive){
      
      if(this.playerLocator._1 == "left" && !this.isStuck) {this.xSpeed = -2.0;}
      if(this.playerLocator._1 == "right" && !this.isStuck) {this.xSpeed = 2.0;}
      
      this.move();
      
      if(this.energy >0 && game.projectiles.exists(bolt => Helper.absoluteDistance(this.location.locationInGame, bolt.location.locationInGame) < 300 && bolt.shooter != this)) {
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
    
    Double xDiff = this.distToPlayer.getKey();
    Double yDiff = this.distToPlayer.getValue();
    
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

  def image:Vector[Node] = if(!this.isShielding) Vector(body.image, arm.get.completeImage) else Vector(body.image, arm.get.completeImage, shield.image)
  
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