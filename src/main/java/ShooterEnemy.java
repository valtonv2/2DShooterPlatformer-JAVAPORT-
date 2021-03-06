package main.java;

import javafx.scene.shape.Rectangle;
import javafx.util.Pair;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.image.*;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Color.*;
import javafx.scene.input.*;
import javafx.animation.*;
import javafx.event.*;
import java.math.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;
import java.util.Random;
import javafx.scene.media.*;

class ShooterEnemy extends Enemy {
	  

	Double locationX;
	Double locationY;
	
  private int actionCounter = 0;
  private int actionNumber = 0;
  private String lookDirection = "east";
  private Boolean isReadyForNextAction = true;
  private Boolean moves = false;
  private Boolean idles = false;
  public Player player;
  private Random rand = new Random();
  private int itemDropIndex = 0;

  private GameSprite newImage = new GameSprite("file:src/main/resources/Pictures/Enemy.png", Optional.empty(), new Pair<Double, Double>(60.0, 90.0), this, new Pair<Double, Double>(-30.0, -45.0), Optional.empty());
  private String currentAction = "idling";
  
 
  //Audio
   public AudioClip enemyHurtSound = new AudioClip("file:src/main/resources/sound/EnemyHurt.wav");
  //Seuraavat Colliderit huolehtivat vihollisen törmäyksistä
  private Collider northCollider;
  private Collider southCollider;
  private Collider eastCollider;
  private Collider westCollider;
  
  public ArrayList<Collider> colliders = new ArrayList<Collider>();

   
 
  //Konstruktori luokalle
  public ShooterEnemy(String name, Game game, Double locationX, Double locationY) {
	  
	  this.name = name;
	  this.game = game;
	  this.locationX = locationX;
	  this.locationY = locationY;	  
	  location = new GamePos(new Pair<Double, Double>(locationX, locationY), false);
	  HP = 200.0;
	  this.lookDirectionForSprite = "east";
	  
	  
	  this.inventory = new HashMap<String, Item>();
	  
	  player = game.player;
	  
	  isActive = false; //Monet raskaat toiminnot suoritetaan vain kun tämä on true. Vaikutus suorituskykyyn on huima
	  
	  northCollider = new Collider("Enorth", this, 0.0,  -45.0, "horizontal");
	  southCollider = new Collider("Esouth", this, 0.0,  45.0, "horizontal");
	  eastCollider = new Collider("Eeast", this, 30.0, 0.0, "vertical");
	  westCollider = new Collider("Ewest", this, -30.0, 0.0, "vertical");
	  
	  this.colliders.add(northCollider);
	  this.colliders.add(eastCollider);
	  this.colliders.add(southCollider);
	  this.colliders.add(westCollider);
  }
  
  private Double projectileSpeed() { 
  	if(player.isSlowingTime) {
  	return player.timeSlowCoefficient * 10.0;}
  	else {
      return 10.0;
  	}
  }
  
  public Optional<Object> itemDrop() {
	  System.out.println("Metodin osa suorittuu");
	  return Optional.ofNullable(this.inventory.values().toArray()[itemDropIndex]);
	  }
  
  public Group image(){

	  Group done = new Group(); 
	  done.getChildren().add(this.newImage.image());
	  return done;
  }
  
  //Seuraava metodi hallitsee vihollisen ampumista laskemalla suunnan kohti pelaajaa ja luomala ammuksen
  public void shoot() {
    
    DirectionVector aimDirection = new DirectionVector(this.location.locationInGame(), player.location.locationInGame());
    new Projectile(this.game, aimDirection, projectileSpeed(), -15.0, 0.0, this );
  }
  
    
  //update-metodi muodostaa vihollisen "aivot" ja päivittää vihollisen tilaa
  public void update() {
	  
	  if(this.inventory.isEmpty()) {
		  
		  //Täytetään vihollisen tavaraluettelo esineillä
		  this.inventory.put("Health Pack", new HealthPack(this.game, 1));
		  this.inventory.put("Energy Pack", new EnergyPack(this.game, 1));
		  this.inventory.put("Kitten 5000", new SlowFiringWeapon(this.game, Optional.empty()));
		  this.inventory.put("Heat Bolter", new RapidFireWeapon(this.game, Optional.empty()));
		  
	  }
   
    //Säädellään aktiivisuutta tehokkuussyistä. Jos vihollinen ei ole aktiivinen se ei ammu
    if (Helper.absoluteDistance(this.location.locationInGame(), this.game.player.location.locationInGame()) <= (GameWindow.stage.getWidth()/2)+200) {
    	this.isActive = true;
    
    }else{
    	this.isActive = false; 
        this.stop();
    }
    
    if(this.xSpeed == 0 && this.ySpeed == 0) {
    	this.moves = false;
    }else{
    	this.moves = true;
    }
    
    if(this.isDead()) {
    	
    	if(this.itemDrop().isPresent()) {this.drop((Item)this.itemDrop().get());}
    	else {System.out.println(this.name + " vanished but did not drop any items this time");}
    	
    }
    	
   
   if(this.isActive && this.moves) {
	   this.colliders.forEach(collider -> collider.update());  //Colliderin päivitys törmäyksien havaitsemista varten
   }
    
    if(this.northCollider.collides) {
       this.ySpeed = 0.0;
    }
    
    if(this.southCollider.collides) { 
      this.ySpeed = 0.0; 
    }
    
    if (game.time % 100 == 0 && this.isActive) {
    	this.shoot(); //Ampuminen. Tapahtuu tietyin aikavälein kun vihollinen on aktiivinen
    }
    
   
    if (this.isActive && !this.southCollider.collides && !player.isSlowingTime) {
    	this.ySpeed += 1.0; //Painovoima
    }else if(this.isActive && !this.southCollider.collides && player.isSlowingTime) {
    	this.ySpeed += 0.1;
    }
    
    if(this.location.locationInGame().getValue() > this.game.currentLevel.dimensions().getValue()) {
    	this.takeDamage(9999.0); //Vihollinen joka putoaa kentän rajojen yli kuolee 
    }
    
    //Vihollisen toiminnon valinta. Valitaan satunnaisesti kun aikaisempi toiminto valmistuu
    if (this.isReadyForNextAction && this.isActive) {
      
        int chooser = rand.nextInt(2);
      
        if(chooser == 0){
          this.isReadyForNextAction = false;
          this.actionNumber = 0;
  
          }else if(chooser == 1){
            this.isReadyForNextAction = false;
            this.actionNumber = 1;
          }
      
    //Toiminnon suorittaminen 
     }else if(this.actionNumber == 0) {
       this.idle();
     }else if(this.actionNumber == 1){
       this.move();
     }
    
    
    this.lookDirectionForSprite = this.lookDirection;
  }
  
 // Idle-toiminto pitää vihollisen paikoillaan 15 tickin ajan
  private void idle() {
      
      this.actionCounter += 1;
      this.xSpeed = 0.0;
      this.ySpeed = 0.0;
      
      if (this.actionCounter == 15) {
        this.idles = false;
        this.isReadyForNextAction = true;
        this.actionCounter = 0;
        this.xSpeed = 5.0;
      }   
 }  
  
  //Move- toiminto saa vihollisen liikkeelle
 private void move() {
    

    if(this.eastCollider.collides && this.lookDirection == "east") {
      this.xSpeed = this.xSpeed * -1.0; 
      this.lookDirection = "west";
    }
    
    if(this.westCollider.collides && this.lookDirection == "west") { 
      this.xSpeed = this.xSpeed * -1.0;
      this.lookDirection = "east";
    }
      
    if (this.actionCounter == 15) {
      this.isReadyForNextAction = true;
      this.actionCounter = 0;
      
    this.actionCounter += 1.0;
    }
    
    this.location.move(this.xSpeed, this.ySpeed);
    
  }
  
  public void takeDamage(Double amount)  {
    this.HP = this.HP - amount;
    this.enemyHurtSound.play();
    this.ySpeed += 10;
    this.xSpeed += 5;
  } 
  
  public void stop() {
    this.xSpeed = 0.0;
    this.ySpeed = 0.0;
  } 
  
  public Optional<Pair<Double, Double>> locationForSprite(){return Optional.ofNullable(this.location.locationInImage());}
  

  
 
  
}