package main.java;
import java.lang.reflect.Array;
import java.math.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

import javafx.scene.shape.Rectangle;
import javafx.util.Pair;
import javafx.scene.media.*;

//Item on pelin esineitä kuvaava piirreluokka joka sisältää kaikille esineille yhteiset ominaisuudet

abstract class Item extends UsesGameSprite{
  
  String name;
  Game game;
	
	
  public Player player;
  
  public String ID;
    
  public Boolean isInWorld = false;
    
  //Kun esine on pelaajan tai vihollisen tavaraluettelossa sijaintia maailmassa ei ole
  //Sijainnille annetaan arvo kun esine on vapaana maailmassa
  public Optional<GamePos> locationInWorld = Optional.empty();
     
  public ArrayList<GameSprite> sprites; //Sisältää esineen kuvat. 0 = World image 1 = Inventory image
  
  public Optional<Pair<Double, Double>> locationForSprite() {
    
     GamePos pos = this.locationInWorld.orElse(new GamePos(new Pair<Double, Double>(0.0,0.0),false));
     Pair <Double, Double >value = pos.locationInImage();
     return Optional.of(value);
    
  }
  
  
}

//################################################################################################################################################################################

// UtilityItem toimii lajittelun apuna. Sitä hyödynnetään kun maailmasta poimittua esinettä ollaan 
// sijoittamassa tavaraluetteloon.

abstract class UtilityItem extends Item {
  
  public Integer useTimes;
  public double strength;
  
  
  public Integer amountOfUseTimes() {return useTimes;}
  
  public Boolean isSpent() {return (this.useTimes == 0);}
 
  abstract public void use();
  


  
}

//#################################################################################################################################################################################

//Weapon kuvaa erilaisia aseita.

abstract class Weapon extends Item{
	
  
  Optional<Actor> user;

  
  private MouseCursor cursor() {return this.game.mouseCursor;}
  
  public Boolean isEquipped = false;
  
  abstract public void fire();
  
  protected Integer currentTime() {return game.time;}
  
  }
 
//##################################################################################################################################################################################

//HealthPack parantaa pelaajaa pelaajan käyttäessä sitä.

class HealthPack extends UtilityItem{
	
  
  public void use() { 
    
    player.heal(Math.min(strength, (player.maxHP-player.HP))); //Pelaajan elinvoiman ei anneta kasvaa yli maksimaalisen määrän
    this.useTimes =  this.useTimes - 1;
    
    GameWindow.PlayerHUD.equipmentBox.updateItems();
  }
  
  public ArrayList<GameSprite> sprites = new ArrayList<GameSprite>();
  //Konstruktori
  
  public HealthPack(Game game, Integer useTimes) {
	  
	  name = "Health Pack";
	  this.game = game;
	  this.useTimes = useTimes;
	  ID = "HP" + this.amountOfUseTimes(); // Hyödynnetään tallentamisessa
	  player = game.player;
	  strength = 500.0; //Parannuksen voimakkuus
	  this.locationForSprite = locationForSprite();
	  
	  this.sprites.add(new GameSprite("file:src/main/resources/Pictures/HealthPack.png", Optional.empty(), new Pair<Double, Double>(45.0,45.0), this, new Pair<Double, Double>(0.0,0.0), Optional.empty()));
	  this.sprites.add(new GameSprite("file:src/main/resources/Pictures/HealthPack.png", Optional.empty(), new Pair<Double, Double>(25.0,25.0), this, new Pair<Double, Double>(15.0,15.0), Optional.empty()));
			  
  }
  
  public String toString() { return this.name;}
       
 }
 
//####################################################################################################################################################################################

//EnergyPack kasvattaa pelaajan energian määrää pelaajan käyttäessä sitä.


class EnergyPack extends UtilityItem{
	  
	  public void use() { 
	    
		  player.energy += Math.min(strength, player.maxEnergy-player.energy);//Pelaajan energian ei anneta kasvaa yli maksimaalisen määrän
		  this.useTimes = this.useTimes - 1; 	
		  
		  GameWindow.PlayerHUD.notificationArea.announce("Used energy pack. Current energy: " + player.energy); 
		  GameWindow.PlayerHUD.equipmentBox.updateItems();
	  }
	  
	  public ArrayList<GameSprite> sprites = new ArrayList<GameSprite>();
			  
			
	  
	  //Konstruktori
	  
	  public EnergyPack(Game game, Integer useTimes) {
		  name = "Energy Pack";
		  this.game = game;
		  this.useTimes = useTimes;
		  ID = "EP" + this.amountOfUseTimes();
		  player = game.player;
		  strength = 500.0; //Parannuksen voimakkuus
		  
		  this.sprites.add(new GameSprite("file:src/main/resources/Pictures/HealthPack.png", Optional.empty(), new Pair<Double, Double>(45.0,45.0), this, new Pair<Double, Double>(0.0,0.0), Optional.empty()));
		  this.sprites.add( new GameSprite("file:src/main/resources/Pictures/HealthPack.png", Optional.empty(), new Pair<Double, Double>(25.0,25.0), this, new Pair<Double, Double>(15.0,15.0), Optional.empty()));	  
		
	  }
	  
	  public String toString() { return this.name;}
	       
	 }
	 

 
//####################################################################################################################################################################################
  
class SlowFiringWeapon extends Weapon{
  
  
  private Integer cooloffTime = 100;
  private AudioClip laserSound = new AudioClip("file:src/main/resources/sound/Pew.wav");
  private Integer lastShotTime = 0;
  
  public SlowFiringWeapon(Game game, Optional<Actor> actor) {
	  name = "Kitten 5000";
	  this.game = game;
	  this.user = actor;
	  player = game.player;
	  ID = "SFW";
	  
	  this.sprites.add(new GameSprite("file:src/main/resources/Pictures/SlowFIreWeapon.png", Optional.empty(), new Pair<Double, Double>(45.0,45.0), this, new Pair<Double, Double>(0.0,0.0), Optional.empty())); //World image
	  this.sprites.add(new GameSprite("file:src/main/resources/Pictures/SlowFIreWeapon.png", Optional.empty(), new Pair<Double, Double>(25.0,25.0), this, new Pair<Double, Double>(15.0,15.0), Optional.empty())); //Inventory image
	  this.sprites.add(new GameSprite("file:src/main/resources/Pictures/SlowFIreWeapon.png", Optional.empty(), new Pair<Double, Double>(30.0, 30.0), game.player, new Pair<Double, Double>(18.0,-18.0), Optional.empty()));  //Equipped image 
  }
  
  private Double projectileSpeed() { 
	  if(player.isSlowingTime) { return player.timeSlowCoefficient * 15.0;} else {return 15.0;} 
	  }
    
  //Hitaasti ampuva ase voi ampua vain tietyin aikavälein
  public void fire() {
    
    Integer shotTime = currentTime();
    
    if( (shotTime - lastShotTime) >= cooloffTime){
      
      if(this.user.isPresent() && this.user.get().arm.isPresent()) { new Projectile(this.game, user.get().arm.get().direction, projectileSpeed(), 0.0, -20.0, this.user.get() );}
      else { new Projectile(this.game, new DirectionVector(this.game.player.location.locationInImage(), this.game.mouseCursor.location), projectileSpeed(), 0.0, -20.0, this.user.get() );}
      
      if(Settings.muteSound == false) { this.laserSound.play();}
     
      this.lastShotTime = shotTime;
      }
    
  }
  
  public ArrayList<GameSprite> sprites = new ArrayList<GameSprite>();
		  
	  
  public String lookDirectionForSprite = "east";
  
  public String toString() { return this.name;}
}

//##############################################################################################################################################################################

class RapidFireWeapon extends Weapon{
   
  private Double coolOffTime = 2.0;
  private Double resetTime = 75.0;
  private Double accuracyModifiers[] = {25.0, -0.2, 0.15, -0.1, 0.0};
  private int modifierIndex = 0;
  private int lastShotTime = 0;
  private AudioClip laserSound = new AudioClip("file:src/main/resources/sound/RFWsound.wav");
  
 
 public RapidFireWeapon(Game game, Optional<Actor> actor) {
	  
	  this.game = game;
	  this.user = actor;
	  player = game.player;
	  name = "Plasma Bolter";
	  ID = "RFW";
	  
	  this.sprites.add(new GameSprite("file:src/main/resources/Pictures/SlowFIreWeapon.png", Optional.empty(), new Pair<Double, Double>(45.0,45.0), this, new Pair<Double, Double>(0.0,0.0), Optional.empty())); //World image
	  this.sprites.add(new GameSprite("file:src/main/resources/Pictures/SlowFIreWeapon.png", Optional.empty(), new Pair<Double, Double>(25.0,25.0), this, new Pair<Double, Double>(15.0,15.0), Optional.empty())); //Inventory image
	  this.sprites.add(new GameSprite("file:src/main/resources/Pictures/SlowFIreWeapon.png", Optional.empty(), new Pair<Double, Double>(30.0, 30.0), game.player, new Pair<Double, Double>(18.0,-18.0), Optional.empty()));  //Equipped image
	  

  } 
  
  private Double projectileSpeed() {
		  if(player.isSlowingTime) {return player.timeSlowCoefficient * 15.0;}
		  else {return 15.0;} 
		  }
  
  //Rapidfireweapon ampuu nopeasti, mutta ensimmäiset laukaukset ovat epätarkkoja
  public void fire() {
    
    if(currentTime()-lastShotTime >= resetTime) {modifierIndex = 0;}
    
    if (currentTime()-lastShotTime >= coolOffTime) {
      
      if(this.user.isPresent()) { new Projectile(this.game, this.user.get().arm.get().direction, projectileSpeed(), 0.0, -20.0, this.user.get() );}
      
      if(Settings.muteSound == false) { this.laserSound.play();}
      
      lastShotTime = currentTime();
      if(modifierIndex < accuracyModifiers.length) { modifierIndex += 1.0;}
      
       }
    
  }
  
  public ArrayList<GameSprite> sprites = new ArrayList<GameSprite>();
	
  public String lookDirectionForSprite = "east";
  
  public String toString() { return this.name;}
}