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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.io.File;
import java.lang.reflect.Array;

import javafx.scene.media.*;
import javafx.scene.Group;
import javafx.scene.Cursor;

  
 public class Player extends Actor {
	  
	  
	  Double startX;
	  Double startY;
	  
	  Double maxHP = 1000.0;
	  Double maxEnergy = 1000.0;
	  Double ySpeed = 0.0;
	  Double xSpeed = 0.0;
	  Double HP = maxHP;
	  Double energy = maxEnergy;
	  Boolean isDead = false;
	  Boolean isSlowingTime = false;
	  Double timeSlowCoefficient = 0.1;
	 
	  
	 private Integer jumpCounter = 0;
	  	  
	  //Pelaajan kuvat
	  
	  private AnimatedGameSprite body = new AnimatedGameSprite("file:src/main/resources/Pictures/MoonmanWalk", "MoonmanWalk", 4, ".png", Optional.empty(), new Pair<Double, Double>(60.0,90.0), this, new Pair<Double, Double>(-30.0,-45.0), false);
	
	  private AnimatedGameSprite shield = new AnimatedGameSprite("file:src/main/resources/Pictures/ShieldAnimated", "Shield", 5, ".png", Optional.empty(), new Pair<Double, Double>(60.0,90.0), this, new Pair<Double, Double>(-30.0,-45.0), true);

	  
	  //Audio
	  private AudioClip playerHurtSound = new AudioClip("file:src/main/resources/sound/PlayerHurt.wav");
	  AudioClip shieldSound = new AudioClip("file:src/main/resources/sound/ShieldSound.wav");
	  AudioClip shieldBounceSound = new AudioClip("file:src/main/resources/sound/ShieldBounce.wav");
	  
	  /* 
	   * Seuraavat Colliderit muodostavat pelaajan sijainnin ympärille "puskurit" jotka tunnistavat
	   * niihin kohdistuvat törmäykset. Kukin collider ottaa parametrikseen nimen, pelaajan, etäisyyden 
	   * pelaajan sijainnista x ja y - suunnissa ja stringin, joka kertoo onko puskuri vaakasuora vai pystysuora.
	   * Kukin collider tarkkailee kolmea sijaintia, jotka ovat pienen välimatkan päässä toisistaan.
	   * Kolmea sijaintia käytetään, jotta pelaaja ei tietyin välimatkoin pääsisi "putoamaan" maan sisään.
	  */
	   
	 private Collider northCollider = new Collider("north", this, 0.0,  -this.body.spriteHeight/2+15, "horizontal");
	 Collider southCollider = new Collider("south", this, 0.0,  this.body.spriteHeight/2-12, "horizontal");
	 private Collider eastCollider = new Collider("east", this, this.body.spriteWidth/2 - 20, -30.0, "vertical");
	 private Collider westCollider = new Collider("west", this, -this.body.spriteWidth/2 + 20, -30.0, "vertical");
	  
	 public ArrayList<Collider> colliders = new ArrayList<Collider>(); 
	  
	 
	//Konstruktori luokalle
	  
	  public Player(Double startX, Double startY, Game game) {
		 
		  name = "Moon Man";
		  this.startX = startX;
		  this.startY = startY;
		  this.game = game;
		  location = new GamePos(new Pair<Double, Double>(startX, startY), false);
		  arm = Optional.of(new RotatingArm(this, new DirectionVector(this.location.locationInImage(), new Pair<Double, Double>(0.0,0.0))));
		  this.inventory = new HashMap<String, Item>();
		  
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
		  	      if(GameWindow.Menus.fullScreenStatus() == false) { GameWindow.stage.setScene(GameWindow.Menus.PauseMenu.scene); }
		  	      else{GameWindow.stage.setScene(GameWindow.Menus.PauseMenu.scene); GameWindow.stage.setFullScreen(true); }
		  	      GameWindow.Menus.currentMenu = GameWindow.Menus.PauseMenu;
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
			  	     GameWindow.PlayerHUD.equipmentBox.moveLeft();
			  	     }
			  	     
			  	     if(event.getCode() == KeyCode.C){
			  	     GameWindow.PlayerHUD.equipmentBox.moveRight();
			  	     }
			  	     
			  	     if(event.getCode() == KeyCode.DIGIT1){
			  	     GameWindow.PlayerHUD.weaponHud.selectBox(0);
			  	     }
			  	    
			  	     if(event.getCode() == KeyCode.DIGIT2){
			  	     GameWindow.PlayerHUD.weaponHud.selectBox(1);
			  	     }
			  	       
			  	     if(event.getCode() == KeyCode.DIGIT3){
			  	     GameWindow.PlayerHUD.weaponHud.selectBox(2);
			  	     }
			  	        
			  	     if(event.getCode() == KeyCode.DIGIT4){
			  	     GameWindow.PlayerHUD.weaponHud.selectBox(3);
			  	     }
			  	         
			  	     if(event.getCode() == KeyCode.DIGIT5){
			  	     GameWindow.PlayerHUD.weaponHud.selectBox(4);
			  	     }
			  	          
			  	     if(event.getCode() == KeyCode.DIGIT6){
			  	     GameWindow.PlayerHUD.weaponHud.selectBox(5);
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
			  		if(event.getDeltaY() > 0) { GameWindow.PlayerHUD.weaponHud.selectNext();}
			 	    else {GameWindow.PlayerHUD.weaponHud.selectPrevious();}
			  		
			  	}
		  };
				  
		  
		  this.game.fullImage.addEventFilter(KeyEvent.KEY_PRESSED, playerKeyPressedHandler);
		  this.game.fullImage.addEventFilter(KeyEvent.KEY_RELEASED, playerKeyRaisedHandler);
		  this.game.fullImage.addEventFilter(KeyEvent.KEY_PRESSED, playerKeyPressedHandler);
		  this.game.fullImage.addEventFilter(MouseEvent.MOUSE_CLICKED, playerMouseClickHandler);
		  this.game.fullImage.addEventFilter(ScrollEvent.SCROLL, playerScrollHandler);
		  
		  this.colliders.add(northCollider);
		  this.colliders.add(eastCollider);
		  this.colliders.add(southCollider);
		  this.colliders.add(westCollider);
	  }
	  
	  
	  
    public void expMove() { location.move(this.xSpeed, this.ySpeed); }
	  
	  
	  
	  //Metodi joka muuttaa pelaajan tilaa. Kutsutaan joka tick
	  
	  
	 public void updateState() {
	     
		lookDirectionForSprite = this.lookDirection;
		
		isMovingForSprite = this.isWalking;
		 
	    this.expMove();
	    
	    if(!this.arm.isPresent()) this.arm = Optional.of(new RotatingArm(this, new DirectionVector(this.location.locationInImage(), GameWindow.currentGame.mouseCursor.location)));
	    
	    if (this.game.mouseCursor.isOnLeft) {this.lookDirection = "west";}
	    else {this.lookDirection = "east";}
	    
	    //Päivittää pelaajan collidereja ja mahdollistaa törmäykset
	    this.colliders.forEach(collider -> collider.update());
	    
	    if((this.eastCollider.collides || this.westCollider.collides) && this.game.time % 5 == 0) {this.isWalking = false;}
	    
	    if (this.game.time % 5 == 0 && this.xSpeed == 0 ) {this.isWalking = false;}
	     
	     //Pelaajaan vaikuttaa painovoima, jos se ei kosketa maata jaloillaan
	    if(!this.southCollider.collides && !this.isOnLadder) { this.game.currentLevel.gravity(0.2);}
	    
	    if(this.isShielding && !this.shieldSound.isPlaying() && Settings.muteSound == false){
	       this.shieldSound.play(Settings.musicVolume());
	        }
	    
	    if(this.isShielding && !Settings.devMode) {this.energy = Math.max(0, this.energy-1);}
	    
	    if (this.isSlowingTime && !Settings.devMode) { this.energy = Math.max(0, this.energy-1);}
	    
	    if(this.isSlowingTime && this.energy == 0) {this.toggleSlowTime();}
	    
	    if(this.isShielding && this.energy == 0) {this.isShielding = false;}
	    
	    if (this.isDead) { game.isOver = true;}
	   
	    this.arm.get().direction.update(this.location.locationInImage(), this.game.mouseCursor.location);
	      
	    GameWindow.PlayerHUD.healthBar.setValue(this.HP / this.maxHP);
	    
	    GameWindow.PlayerHUD.energyBar.setValue(this.energy / this.maxEnergy);
	    
	    if(this.location.locationInGame().getValue() > this.game.currentLevel.dimensions().getValue()){

	      this.takeDamage(9999.0);
	    }
	  }
	   
	  //Luodaan scalafx:n mukainen kuva
	 public Group image() { 
	  
		 Group done = new Group();
		 
	      if (!this.isShielding) {
	    	  
	    	  done.getChildren().add(body.image());
	    	  done.getChildren().add(arm.get().completeImage());
	    	  return done;
	      }
	      else {
	    	  done.getChildren().add(body.image());
	    	  done.getChildren().add(arm.get().completeImage());
	    	  done.getChildren().add(shield.image());
	    	  return done;
	      }
	      
	  }
	  
	 
	 //Seuraava shoot-metodi laukaisee aseen.
	  public void shoot() { 
		  
		  if(this.equippedWeapon.isPresent()) {
			  
			  if(!this.game.isInmapMode) {this.equippedWeapon.get().fire();}
			  
		  }else {
			  
			  GameWindow.PlayerHUD.notificationArea.announce("You have not equipped a weapon");
			  
		  }
		  
	 }
	  
	  //Pelaajan vahingoittaminen
	  public void takeDamage(Double amount) {
	   
		  if(Settings.muteSound == false) {playerHurtSound.play(Settings.musicVolume());}
	    
	      if(Settings.devMode == false) { this.HP -= amount;}
	     
	      if (this.HP <= 0) {this.isDead = true;}
	  }
	  
	  
	  public void stop() {
	    
	    this.xSpeed = 0.0;
	    this.ySpeed = 0.0;
	  }
	  
	  //Pelaajan parantaminen
	  public void heal(double d) {
	    this.HP += d;
	    GameWindow.PlayerHUD.notificationArea.announce("Healed. Current HP: " + this.HP);
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
	 
	 public Optional<Pair<Double, Double>> locationForSprite(){return Optional.ofNullable(this.location.locationInImage());}
	  
	 public String toString() {return this.name + " now at (" + this.location.locationInGame().getKey() +" ; "+ this.location.locationInGame().getValue() + ")";}
	}
