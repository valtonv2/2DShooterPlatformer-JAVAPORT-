package main.java;

import main.java.UsesGameSprite;
import javafx.scene.shape.Rectangle;
import javafx.scene.Node;
import javafx.scene.image.*;
import javafx.scene.paint.ImagePattern;
import javafx.scene.transform.*;
import javafx.util.Pair;
import javafx.scene.paint.Color.*;
import javafx.scene.input.*;
import javafx.animation.*;
import javafx.event.*;
import java.math.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.io.File;
import java.lang.reflect.Array;

import javafx.scene.media.*;
import javafx.scene.Group;
import javafx.scene.Cursor;


abstract class Actor extends UsesAnimatedGameSprite {
  
  String name;
  ArrayList<Collider> colliders;
  Game game;
  Boolean isWalking = false;
  Boolean isShielding = false;
  Boolean isOnLadder = false;
  Boolean hasDroppedItem = false;
  int height = 90;
  int width = 60;
  
  Optional<RotatingArm> arm;
  //Tavaraluettelo
  Map<String, Item> inventory;
  Optional<UtilityItem> equippedUtilityItem = Optional.empty();
  Optional<Weapon> equippedWeapon = Optional.empty();
  GamePos location;
  
  Optional<Pair<Double, Double>> locationForSprite = Optional.ofNullable(location.locationInImage());
  String lookDirectionForSprite;
  abstract void stop();
  abstract void takeDamage(Double amount);
  ArrayList<Node> image;
  
  //Esineen poimiminen maailmasta
  /*
  public void pickUp(Item item, Boolean isCheatDrop) {
    
    
    if(isCheatDrop){
      this.inventory += (item.name, item);
      item.isInWorld = false;
    }
    
    if(!this.inventory.contains(item.name) && !this.hasDroppedItem && game.currentLevel.itemsInWorld.contains(item) && !isCheatDrop) {  // Tiettyä esinettä voi tavaraluettelossa olla vain 1. UtilityItemien käyttökertojen määrä kasvaa poimittaessa niitä lisää.
      
      this.inventory += (item.name -> item)
      item.isInWorld = false
      game.currentLevel.itemsInWorld -= item
      NotificationArea.announce(this.name + " picked up " + item)
   
   }
    
    if(this.inventory.contains(item.name) && item.isInstanceOf[UtilityItem] && game.currentLevel.itemsInWorld.contains(item)) {
      this.inventory(item.name).asInstanceOf[UtilityItem].amountOfUseTimes += item.asInstanceOf[UtilityItem].amountOfUseTimes
      item.isInWorld = false
      game.currentLevel.itemsInWorld -= item
      
      NotificationArea.announce(this.name + " picked up " + item)
      
      
    }
    
    
    println("Player inventory: " + this.inventory.keys.mkString(", "))
    if(item.isInstanceOf[Weapon]) PlayerHUD.weaponHud.updateItems
    else PlayerHUD.equipmentBox.updateItems
    
  }
  
   def equipWeapon(gun:Option[Weapon]) = {
     this.equippedWeapon = gun
     
     if(gun.isDefined){
       gun.get.user = Some(this)
       gun.get.sprites(2).user = this
     }
     
   }
   def equipUtilItem(thing:UtilityItem) = {
     this.equippedUtilityItem = Some(thing)
   }
   def useUtilItem = this.equippedUtilityItem match {
     
     case Some(item) => item.use
     case None => println("Tried to use item that did not exist")
     
   }
    
   def drop(item:Item) = {
    
    if (this.inventory.values.toArray.contains(item) && !this.hasDroppedItem){
      
      this.hasDroppedItem = true
      item.isInWorld = true
      this.inventory -= item.name
      item.locationInWorld = Some(this.location)
      game.currentLevel.itemsInWorld += item
    
      println(this.name + " dropped " + item.name)
    } else {
      
      println("Item specified did not exist in player inventory")
      
    }
    */
  }
  
  class Player extends Actor {
	  
	  
	  Double startX;
	  Double startY;
	  Game game;
	  
	  
	  String name = "Moon Man";
	  Double maxHP = 1000.0;
	  Double maxEnergy = 1000.0;
	  Double ySpeed = 0.0;
	  Double xSpeed = 0.0;
	  Double HP = maxHP;
	  Double energy = maxEnergy;
	  Boolean isDead = false;
	  Boolean isSlowingTime = false;
	  Double timeSlowCoefficient = 0.1;
	  String lookDirection = "east";
	  
	 private Integer jumpCounter = 0;
	  
	  GamePos location = new GamePos(new Pair<Double, Double>(startX, startY), false);
	  
	  
	  //Pelaajan kuvat
	  
	  private AnimatedGameSprite body = new AnimatedGameSprite("file:src/main/resources/Pictures/MoonmanWalk", "MoonmanWalk", 4, ".png", Optional.empty(), new Pair<Double, Double>(60.0,90.0), this, new Pair<Double, Double>(-30.0,-45.0), false);
	  public Optional<RotatingArm> arm = Optional.of(new RotatingArm(this, new DirectionVector(this.location.locationInImage(), new Pair<Double, Double>(0.0,0.0))));
	  private AnimatedGameSprite shield = new AnimatedGameSprite("file:src/main/resources/Pictures/ShieldAnimated", "Shield", 5, ".png", Optional.empty(), new Pair<Double, Double>(60.0,90.0), this, new Pair<Double, Double>(-30.0,-45.0), true);

	  
	  //Audio
	  private AudioClip playerHurtSound = new AudioClip("file:src/main/resources/sound/PlayerHurt.wav");
	  AudioClip shieldSound = new AudioClip("file:src/main/resources/sound/ShieldSound.wav");
	  private AudioClip shieldBounceSound = new AudioClip("file:src/main/resources/sound/ShieldBounce.wav");
	  
	  /* 
	   * Seuraavat Colliderit muodostavat pelaajan sijainnin ympärille "puskurit" jotka tunnistavat
	   * niihin kohdistuvat törmäykset. Kukin collider ottaa parametrikseen nimen, pelaajan, etäisyyden 
	   * pelaajan sijainnista x ja y - suunnissa ja stringin, joka kertoo onko puskuri vaakasuora vai pystysuora.
	   * Kukin collider tarkkailee kolmea sijaintia, jotka ovat pienen välimatkan päässä toisistaan.
	   * Kolmea sijaintia käytetään, jotta pelaaja ei tietyin välimatkoin pääsisi "putoamaan" maan sisään.
	  */
	   
	 private Collider northCollider = new Collider("north", this, 0.0,  -this.body.spriteHeight/2+15, "horizontal");
	 private Collider southCollider = new Collider("south", this, 0.0,  this.body.spriteHeight/2-12, "horizontal");
	 private Collider eastCollider = new Collider("east", this, this.body.spriteWidth/2 - 20, -30.0, "vertical");
	 private Collider westCollider = new Collider("west", this, -this.body.spriteWidth/2 + 20, -30.0, "vertical");
	  
	 public Collider colliders[] =  {northCollider, eastCollider, southCollider, westCollider};
	  
	 
	//Konstruktori luokalle
	  
	  public Player(Double startX, Double startY, Game game) {
		  
		  this.startX = startX;
		  this.startY = startY;
		  this.game = game;
		  
	  EventHandler<KeyEvent> playerKeyPressedHandler = new EventHandler<KeyEvent>() {
		  	public void handle(KeyEvent event) {
		  		 if(!eastCollider.collides && event.getCode() == KeyCode.D && xSpeed<=10 && !game.isInmapMode){
		  	       xSpeed = 10.0;
		  	       isWalking = true;
		  	    
		  	     }else if(eastCollider.collides && event.getCode() == KeyCode.D && xSpeed<=10 ){
		  	       xSpeed = 0.0;

		  	     }
		  	     
		  	     
		  	     if(!westCollider.collides && event.getCode() == KeyCode.A && xSpeed >= -10 && !game.isInmapMode){
		  	      xSpeed = -10.0;
		  	      isWalking = true;

		  	     }else if(westCollider.collides && event.getCode() == KeyCode.A && xSpeed<=10){
		  	       xSpeed = 0.0;
		  	 
		  	     }
		  	     
		  	     if(!northCollider.collides && event.getCode() == KeyCode.W && ySpeed >= -10 && !game.isInmapMode ){
		  	         
		  	       if(isOnLadder) { ySpeed = -10.0;}
		  	       else {jump();}
		  	       
		  	     }else if(northCollider.collides && event.getCode() == KeyCode.W && ySpeed<=10){
		  	       xSpeed = 0.0;
		  	     }
		  	     
		  	     if(!southCollider.collides && event.getCode() == KeyCode.S && ySpeed <= 10 && !game.isInmapMode){
		  	     ySpeed = 10.0;
		  	     }else if(southCollider.collides && event.getCode() == KeyCode.S && ySpeed<=10){
		  	       xSpeed = 0.0;
		  	     }
		  	     
		  	     //Suojakenttä
		  	     if(event.getCode() == KeyCode.E){
		  	     useShield();
		  	     }
		  	     
		  	     //Ajan hidastus
		  	     if(event.getCode() == KeyCode.Q){
		  	     toggleSlowTime();
		  	     }
		  	     
		  	     
		  	     //Pause-screen
		  	      if(event.getCode() == KeyCode.ESCAPE){
		  	      GameWindow.clock.stop();
		  	      GameWindow.menuClock.start();
		  	      if(Menus.fullScreenStatus == false) { GameWindow.stage.scene = Menus.PauseMenu.scene; }
		  	      else{GameWindow.stage.scene = Menus.PauseMenu.scene; GameWindow.stage.setFullScreen(true); }
		  	      Menus.currentMenu = Menus.PauseMenu;
		  	    }
		  	     
		  	      //Kamera
		  	      
		  	     if(event.getCode() == KeyCode.M){
		  	     game.toggleMap();
		  	     }
		  	     
		  	      if(event.getCode() == KeyCode.L && game.isInmapMode){
		  	       game.camera.xSpeed = 50.0;
		  	       }
		  	     
		  	     if(event.getCode() == KeyCode.J && game.isInmapMode){
		  	       game.camera.xSpeed = -50.0;
		  	       }
		  	     
		  	     if(event.getCode() == KeyCode.I && game.isInmapMode){
		  	       game.camera.ySpeed = -50.0;
		  	       }
		  	     
		  	     if(event.getCode() == KeyCode.K && game.isInmapMode){
		  	       game.camera.ySpeed = 50.0;
		  	       }
		  	     
		  	      if(event.getCode() == KeyCode.UP && game.isInmapMode){
		  	     game.camera.zInSpeed = 0.05;
		  	   
		  	     }

		  	     if(event.getCode() == KeyCode.DOWN && game.isInmapMode){
		  	     game.camera.zOutSpeed = 0.05;
		  	    
		  	     } 
		  	      
		  	   }
			  
		  };
		  
		  
		  EventHandler<KeyEvent> playerKeyRaisedHandler = new EventHandler<KeyEvent>() {
			  	public void handle(KeyEvent event) {
			  		
			  		
			  	     if(event.getCode() == KeyCode.D && xSpeed>0){
			  	       xSpeed = 0.0;
			  	       isWalking = false;
			  	       }
			  	     
			  	     if(event.getCode() == KeyCode.A && xSpeed<0){
			  	       xSpeed = 0.0;
			  	       isWalking = false;
			  	       }
			  	     
			  	     if(event.getCode() == KeyCode.W && isOnLadder){
			  	       ySpeed = 0.0;
			  	      
			  	       }
			  	     
			  	     if(event.getCode() == KeyCode.D && isOnLadder){
			  	       ySpeed = 0.0;
			  	       }
			  	     
			  	     if(event.getCode() == KeyCode.E){
			  	     isShielding = false;
			  	     }
			  	     
			  	     if(event.getCode() == KeyCode.X){
			  	     PlayerHUD.equipmentBox.moveLeft();
			  	     }
			  	     
			  	     if(event.getCode() == KeyCode.C){
			  	     PlayerHUD.equipmentBox.moveRight();
			  	     }
			  	     
			  	     if(event.getCode() == KeyCode.DIGIT1){
			  	     PlayerHUD.weaponHud.selectBox(0);
			  	     }
			  	    
			  	     if(event.getCode() == KeyCode.DIGIT2){
			  	     PlayerHUD.weaponHud.selectBox(1);
			  	     }
			  	       
			  	     if(event.getCode() == KeyCode.DIGIT3){
			  	     PlayerHUD.weaponHud.selectBox(2);
			  	     }
			  	        
			  	     if(event.getCode() == KeyCode.DIGIT4){
			  	     PlayerHUD.weaponHud.selectBox(3);
			  	     }
			  	         
			  	     if(event.getCode() == KeyCode.DIGIT5){
			  	     PlayerHUD.weaponHud.selectBox(4);
			  	     }
			  	          
			  	     if(event.getCode() == KeyCode.DIGIT6){
			  	     PlayerHUD.weaponHud.selectBox(5);
			  	     }
			  	     
			  	     if(event.getCode() == KeyCode.H){
			  	     useUtilItem();
			  	     }
			  	     
			  	     
			  	     if(event.getCode() == KeyCode.UP){
			  	     game.camera.zInSpeed = 0.0;
			  	    
			  	     }

			  	     if(event.getCode() == KeyCode.DOWN){
			  	     game.camera.zOutSpeed = 0.0;
			  	    
			  	     }
			  	    
			  	      if(event.getCode() == KeyCode.L && game.isInmapMode){
			  	       game.camera.xSpeed = 0.0;
			  	       }
			  	     
			  	     if(event.getCode() == KeyCode.J && game.isInmapMode){
			  	       game.camera.xSpeed = 0.0;
			  	       }
			  	     
			  	     if(event.getCode() == KeyCode.I && game.isInmapMode){
			  	       game.camera.ySpeed = 0.0;
			  	      
			  	       }
			  	     
			  	     if(event.getCode() == KeyCode.K && game.isInmapMode){
			  	       game.camera.ySpeed = 0.0;
			  	       }
			  	   
			  	}
		  };
		  
		  
		  EventHandler<MouseEvent> playerMouseClickHandler = new EventHandler<MouseEvent>() {
			  	public void handle(MouseEvent event) {
			  		shoot();
			  		
			  	}
		  };
		  
		  EventHandler<ScrollEvent> playerScrollHandler = new EventHandler<ScrollEvent>() {
			  	public void handle(ScrollEvent event) {
			  		if(event.getDeltaY() > 0) { PlayerHUD.weaponHud.selectNext();}
			 	    else {PlayerHUD.weaponHud.selectPrevious();}
			  		
			  	}
		  };
				  
		  
		  this.player.game.fullImage().addEventFilter(KeyEvent.KEY_PRESSED, playerKeyPressedHandler);
		  this.player.game.fullImage().addEventFilter(KeyEvent.KEY_RELEASED, playerKeyRaisedHandler);
		  this.player.game.fullImage().addEventFilter(KeyEvent.KEY_PRESSED, playerKeyPressedHandler);
		  this.player.game.fullImage().addEventFilter(MouseEvent.MOUSE_CLICKED, playerMouseClickHandler);
		  this.player.game.fullImage().addEventFilter(ScrollEvent.SCROLL, playerScrollHandler);
	  }
	  
	  
	  
	  
	  
	  
	  
	  
	  
	  
	  
	  
	  
	  
    public void expMove() { location.move(this.xSpeed, this.ySpeed); }
	  
	  
	  
	  //Metodi joka muuttaa pelaajan tilaa. Kutsutaan joka tick
	  
	  
	 public void updateState() {
	     
	    this.expMove;
	    
	    
	    if (this.game.mouseCursor.isOnLeft) {this.lookDirection = "west";}
	    else {this.lookDirection = "east";}
	    
	    //Päivittää pelaajan collidereja ja mahdollistaa törmäykset
	    this.colliders.foreach(_.update);
	    
	    if((this.eastCollider.collides || this.westCollider.collides) && this.game.time % 5 == 0) {this.isWalking = false;}
	    
	    if (this.game.time % 5 == 0 && this.xSpeed == 0 ) {this.isWalking = false;}
	     
	     //Pelaajaan vaikuttaa painovoima, jos se ei kosketa maata jaloillaan
	    if(!this.southCollider.collides && !this.isOnLadder) { this.game.currentLevel.gravity(0.2);}
	    
	    if(this.isShielding && !this.shieldSound.isPlaying() && Settings.muteSound == false){
	       this.shieldSound.play(Settings.musicVolume)
	        }
	    
	    if(this.isShielding && !Settings.devMode) {this.energy = math.max(0, this.energy-1);}
	    
	    if (this.isSlowingTime && !Settings.devMode) this.energy = {math.max(0, this.energy-1);}
	    
	    if(this.isSlowingTime && this.energy == 0) {this.toggleSlowTime();}
	    
	    if(this.isShielding && this.energy == 0) {this.isShielding = false;}
	    
	    if (this.isDead) { game.isOver = true;}
	    
	    this.arm.get().direction.update(this.location.locationInImage(), this.game.mouseCursor.location);
	      
	    PlayerHUD.healthBar.setValue(this.HP / this.maxHP);
	    
	    PlayerHUD.energyBar.setValue(this.energy / this.maxEnergy);
	    
	    if(this.location.locationInGame.getValue() > this.game.currentLevel.dimensions.getValue()){
	      println("You fell off map")
	      this.takeDamage(9999);
	    }
	  }
	   
	  //Luodaan scalafx:n mukainen kuva
	 public Array image() { 
	  
	      if (!this.isShielding) Array<Node>(body.image, arm.get.completeImage);
	      else Array<Node>(body.image, arm.get.completeImage, shield.image);
	      
	  }
	  
	 
	 //Seuraava shoot-metodi laukaisee aseen.
	  public void shoot() { switch(this.equippedWeapon){
	    
	    case Optional<Weapon>: 
	    	if(!this.game.isInmapMode) {this.equippedWeapon.get().fire;}
	        break;
	    case Optional.empty():
	    	PlayerHUD.notificationArea.announce("You have not equipped a weapon");
	        break;
	  
	  }
	 }
	  
	  //Pelaajan vahingoittaminen
	  public void takeDamage(int amount) {
	   
		  if(Settings.muteSound == false) {playerHurtSound.play(Settings.musicVolume);}
	    
	      if(Settings.devMode == false) { this.HP -= amount;}
	     
	      if (this.HP <= 0) {this.isDead = true;}
	  }
	  
	  
	  public void stop() {
	    
	    this.xSpeed = 0;
	    this.ySpeed = 0;
	  }
	  
	  //Pelaajan parantaminen
	  public void heal(double d) {
	    this.HP += d;
	    PlayerHUD.notificationArea.announce("Healed. Current HP: " + this.HP);
	  }
	  
	  
	  //Ajan hidastaminen. Tämä pienentää liikkuvien asioiden nopeutta.
	  //Sallii myös ammusten päällä hyppimisen
	  
	  private void toggleSlowTime() {
	    
	    
	    
	    if(this.isSlowingTime == false && this.energy>0){
	    
	    this.isSlowingTime = true;
	    
	    this.game.projectiles.forEach(projectile -> projectile.addSpeedModifier(timeSlowCoefficient));
	    this.game.enemies.forEach(enemy -> enemy.addSpeedModifier(timeSlowCoefficient));
	    
	    
	    }else{
	      
	    
	      this.isSlowingTime = false;
	      this.game.projectiles.forEach(projectile -> projectile.addSpeedModifier(1/timeSlowCoefficient));
	      this.game.enemies.forEach(enemy -> enemy.addSpeedModifier(1/timeSlowCoefficient));
	      
	      
	    }
	    
	  }
	  
	  private void useShield() {
	    
	    if (this.energy>0) {this.isShielding = true;}
	    
	  }
	  
	  //Pelaajan hyppy
	  private void jump() {
	    
	    if (this.jumpCounter <= 1){
	      this.ySpeed = -8.0;
	      this.jumpCounter += 1;
	    }
	    
	    if (this.southCollider.collides || Settings.devMode){
	      
	      this.jumpCounter = 0;
	    } 
	  }
	 
	  public String lookDirectionForSprite() { 
		  return this.lookDirection;
		  }

	  
	 public Boolean isMovingForSprite() {return this.isWalking;}
	  
	 public String toString() {this.name + " now at (" + this.location.locationInGame._1 +" ; "+ this.location.locationInGame._2 + ")"}
	}
