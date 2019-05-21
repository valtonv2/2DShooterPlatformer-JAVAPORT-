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

  }
  
  
  
  def update:Unit = {
    
    if(!this.isActive && this.absDistToPlayer <= (GameWindow.stage.width.toDouble/2)+200) this.isActive = true //Aktiivisuuden säätely
    else if(this.absDistToPlayer > (GameWindow.stage.width.toDouble/2)+200) this.isActive == false
    
     //Aina suoritettavat toiminnot
    
    this.colliders.foreach(_.update)
    if(!this.southCollider.collides) this.ySpeed += 1
    this.arm.get.direction.update(this.location.locationInImage, this.game.player.location.locationInImage)
    if(this.isShielding) this.energy -= 5
    if(this.isShielding && !this.shieldSound.isPlaying) this.shieldSound.play()
    if(this.energy <= 0 ) this.isShielding = false
    if(this.location.locationInGame._2 > this.game.currentLevel.dimensions._2) this.takeDamage(9999) 
    
     //Esineiden pudottaminen
    try{
    if(this.isDead) this.drop(this.inventory.values.head)
    }catch{
      case x:NoSuchElementException => println("FollowingEnemy item drop is acting up.")
    }
    //Kun vihollinen on aktiivinen
    if(this.isActive){
      
      if(this.playerLocator._1 == "left" && !this.isStuck) this.xSpeed = -2
      if(this.playerLocator._1 == "right" && !this.isStuck) this.xSpeed = 2
      
      this.move
      
      if(this.energy >0 && game.projectiles.exists(bolt => Helper.absoluteDistance(this.location.locationInGame, bolt.location.locationInGame) < 300 && bolt.shooter != this)) this.shieldOn
      else this.shieldOff
      
    }
    
    //Kun vihollinen ei ole aktiivinen
    if(!this.isActive){
      this.stop
    }
    
   
  }

//Vihollisen toiminnot
//#########################################################################################################################################################
 
  
  
 private def playerLocator:(String, String) = {
    
    val xDiff = this.distToPlayer._1
    val yDiff = this.distToPlayer._2
    
    var xStatus = ""
    var yStatus = ""
    
    if(xDiff < -50)  xStatus = "left"
    else if(xDiff > 50) xStatus = "right"
    else xStatus = "nearby"
      
    if(yDiff < -5) yStatus = "above"
    else if(yDiff > 5) yStatus = "below"
    else yStatus = "onlevel"
    
    (xStatus, yStatus)
      
  }
  
 private def shoot = this.equippedWeapon match{
    
    case None => this.equipWeapon(Some(new RapidFireWeapon(this.game, Some(this))))
    case Some(gun) => if(this.game.time % 50 == 0) gun.fire
    
  }
  
 def stop = {
    this.xSpeed = 0
    this.ySpeed = 0
    this.isMovingForSprite = false
  } 
 
 
 //Move metodi hallitsee tämän vihollisen liikkumista playerlocatorin tietojen avulla
  private def move = {
    
    if(!this.isStuck && ((this.playerLocator._1 != "nearby") || ((this.playerLocator._2 != "onlevel") || !this.isOnLadder))){
      
      
      this.location.move(this.xSpeed, this.ySpeed)
     
      this.isMovingForSprite = true
      
    }else this.stop
    
    
    if(this.xSpeed<0) this.lookDirectionForSprite = "west"
    if(this.xSpeed>0) this.lookDirectionForSprite = "east"
    
    if(this.northCollider.collides) this.stop
    
    if(this.eastCollider.collides ^ this.westCollider.collides) this.jump(10)
    
    if(this.isOnLadder && this.playerLocator._2 == "above") this.ySpeed = -5
    else if(!this.isOnLadder && this.playerLocator._1 == "nearby" && this.playerLocator._2 == "above") this.jump(10)
    
    this.shoot
    
     if(this.isStuckBelow){
       
        location.move(0, -1)
       // Pelastetaan jumiutunut vihollinen
     }
    
    if(this.isStuckAbove){
      
      this.location.move(0, 7)
      
    }
  }
  
  private def jump(strength:Int) = {
    
   if(this.jumpCount < 3){
     this.ySpeed = -strength
     this.jumpCount += 1
   }else if(this.southCollider.collides){
     this.jumpCount = 0
     this.ySpeed = -strength
     this.jumpCount += 1
   }
  }
  
  private def shieldOn = if(this.energy > 0) this.isShielding = true
  private def shieldOff = this.isShielding = false
    
//#########################################################################################################################################################

  def image:Vector[Node] = if(!this.isShielding) Vector(body.image, arm.get.completeImage) else Vector(body.image, arm.get.completeImage, shield.image)
  
  def takeDamage(amount:Int):Unit = {
    
    this.HP -= amount
    this.jump(5)
    this.hurtSound.play()
    this.xSpeed -= 10
    
   
   
    
  }
  
  //Metodi, joka vähentää vihollisen energiaa. Käytetään pelin lataamisen yhteydessä
  def useEnergy(amount:Int):Unit = {
    this.energy -= amount
  }
  
 def isStuck = isStuckBelow || isStuckAbove
  
 private def isStuckBelow = this.eastCollider.collides && this.westCollider.collides && this.southCollider.collides
  
 private def isStuckAbove = this.eastCollider.collides && this.westCollider.collides && this.northCollider.collides
  
 private def distToPlayer = (this.game.player.location.locationInGame._1 - this.location.locationInGame._1, this.game.player.location.locationInGame._2 - this.location.locationInGame._2)
  
 private def absDistToPlayer = Helper.absoluteDistance(this.game.player.location.locationInGame, this.location.locationInGame)

 
 //Lisätään esine vihollisen tavaraluetteloon
 this.inventory += ("Health Pack" -> new HealthPack(this.game, 5))  
 
}