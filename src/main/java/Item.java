package main.java;
import java.math.*;
import java.util.ArrayList;
import java.util.Optional;

import javafx.scene.shape.Rectangle;
import javafx.util.Pair;
import javafx.scene.media.*;

//Item on pelin esineitä kuvaava piirreluokka joka sisältää kaikille esineille yhteiset ominaisuudet

abstract class Item extends UsesGameSprite{
  
  String name;
  Game game;
	
	
  public Player player = game.player;
  
  public String ID;
    
  public Boolean isInWorld = false;
    
  //Kun esine on pelaajan tai vihollisen tavaraluettelossa sijaintia maailmassa ei ole
  //Sijainnille annetaan arvo kun esine on vapaana maailmassa
  public Optional<GamePos> locationInWorld = Optional.empty();
     
  public ArrayList<GameSprite> sprites(); //Sisältää esineen kuvat. 0 = World image 1 = Inventory image
  
  public Optional<Pair<Double, Double>> locationForSprite() {
    
     GamePos pos = this.locationInWorld.orElseGet(new GamePos(new Pair<Double, Double>(0.0,0.0),false));
     Pair <Double, Double >value = pos.locationInImage();
     return Optional.of(value);
    
  }
  
  public String toString() { return this.name;}
  
}

//################################################################################################################################################################################

// UtilityItem toimii lajittelun apuna. Sitä hyödynnetään kun maailmasta poimittua esinettä ollaan 
// sijoittamassa tavaraluetteloon.

abstract class UtilityItem extends Item {
  
  public String name;
  public Game game;
  public Integer useTimes;
  public Integer strength;
  
  
  public Integer amountOfUseTimes() {return useTimes;}
  
  public Boolean isSpent() {return (this.useTimes == 0);}
 
  public void use();
  
  public ArrayList<GameSprite> sprites();
  
  public String lookDirectionForSprite = "east";
  
}

//#################################################################################################################################################################################

//Weapon kuvaa erilaisia aseita.

abstract class Weapon extends Item{
	
  String name;
  Optional<Actor> user;
  Game game;
  
  private MouseCursor cursor() {return this.game.mouseCursor;}
  
  private Pair<Double, Double> equippedLocation = new Pair<Double, Double>(game.player.location.locationInGame().getKey(), game.player.location.locationInGame().getValue());
  
  public Boolean isEquipped = false;
  
  public void fire();
  
  public ArrayList<GameSprite> sprites();
  
  protected Integer currentTime = game.time;
  
  }
 
//##################################################################################################################################################################################

//HealthPack parantaa pelaajaa pelaajan käyttäessä sitä.

class HealthPack extends UtilityItem{
	
  public Game game;
  public Integer useTimes;
  public String  name = "Health Pack";
  
  public String ID = "HP" + this.amountOfUseTimes(); // Hyödynnetään tallentamisessa
  
  public Double strength = 500.0; //Parannuksen voimakkuus
  
  
  public void use() { 
    
    player.heal(Math.min(strength, (player.maxHP-player.HP))); //Pelaajan elinvoiman ei anneta kasvaa yli maksimaalisen määrän
    this.useTimes -= 1.0;
    
    PlayerHUD.equipmentBox.updateItems();
  }
  
  ArrayList<GameSprite> sprites = new ArrayList<GameSprite>();
  
  //Konstruktori
  
  public HealthPack(Game game, Integer useTimes) {
	  
	  this.game = game;
	  this.useTimes = useTimes;
			  
	  
	  sprites.add( new GameSprite("file:src/main/resources/Pictures/HealthPack.png", Optional.empty(), new Pair<Double, Double>(45.0,45.0), this, new Pair<Double, Double>(0.0,0.0), Optional.empty())); //World image
	  sprites.add( new GameSprite("file:src/main/resources/Pictures/HealthPack.png", Optional.empty(), new Pair<Double, Double>(25.0,25.0), this, new Pair<Double, Double>(15.0,15.0), Optional.ofNullable(EquipmentBox.location)));  //Inventory image
	    
  }
       
 }
 
//####################################################################################################################################################################################

//EnergyPack kasvattaa pelaajan energian määrää pelaajan käyttäessä sitä.


class EnergyPack extends UtilityItem{
	
	  public Game game;
	  public Integer useTimes;
	  public String  name = "Energy Pack";
	  
	  public String ID = "EP" + this.amountOfUseTimes(); // Hyödynnetään tallentamisessa
	  
	  public Double strength = 500.0; //Parannuksen voimakkuus
	  
	  
	  public void use() { 
	    
		  player.energy += Math.min(strength, player.maxEnergy-player.energy);//Pelaajan energian ei anneta kasvaa yli maksimaalisen määrän
		  this.useTimes -= 1.0; 		    
		  PlayerHUD.notificationArea.announce("Used energy pack. Current energy: " + player.energy); 
		  PlayerHUD.equipmentBox.updateItems;
	  }
	  
	  ArrayList<GameSprite> sprites = new ArrayList<GameSprite>();
	  
	  //Konstruktori
	  
	  public EnergyPack(Game game, Integer useTimes) {
		  
		  this.game = game;
		  this.useTimes = useTimes;
				  
		  
		  sprites.add( new GameSprite("file:src/main/resources/Pictures/HealthPack.png", Optional.empty(), new Pair<Double, Double>(45.0,45.0), this, new Pair<Double, Double>(0.0,0.0), Optional.empty())); //World image
		  sprites.add( new GameSprite("file:src/main/resources/Pictures/HealthPack.png", Optional.empty(), new Pair<Double, Double>(25.0,25.0), this, new Pair<Double, Double>(15.0,15.0), Optional.ofNullable(EquipmentBox.location)));  //Inventory image
		    
	  }
	       
	 }
	 

 
//####################################################################################################################################################################################
  
class SlowFiringWeapon(game:Game, var actor:Option[Actor]) extends Weapon("Kitten 5000", actor, game){
  
  val ID = "SFW"
  
  private val cooloffTime = 100
  private val laserSound = new AudioClip("file:src/main/resources/sound/Pew.wav")
  private var lastShotTime = 0
  
  private def projectileSpeed = if(player.isSlowingTime) player.timeSlowCoefficient * 15 else 15 
    
  //Hitaasti ampuva ase voi ampua vain tietyin aikavälein
  def fire = {
    
    val shotTime = currentTime
    if( (shotTime - lastShotTime) >= cooloffTime){
      
      if(this.user.isDefined && this.user.get.arm.isDefined) new Projectile(this.game, user.get.arm.get.direction, projectileSpeed, 0, -20, this.user.get )
      else new Projectile(this.game, new DirectionVector(this.game.player.location.locationInImage, this.game.mouseCursor.location), projectileSpeed, 0, -20, this.user.get )
      
      if(Settings.muteSound == false) this.laserSound.play()
      this.lastShotTime = shotTime
      }
    
  }
  
  lazy val sprites = Array(
      
      new GameSprite("file:src/main/resources/Pictures/SlowFIreWeapon.png", None, (45.0,45.0), this, (0,0), None), //World image
      new GameSprite("file:src/main/resources/Pictures/SlowFIreWeapon.png", None, (25.0,25.0), this, (15,15), None), //Inventory image
      new GameSprite("file:src/main/resources/Pictures/SlowFIreWeapon.png", None, (30.0, 30.0), game.player, (18,-18), None)  //Equipped image
  
  )
  
  def lookDirectionForSprite = "east"
}

//##############################################################################################################################################################################

class RapidFireWeapon(game:Game, var actor:Option[Actor]) extends Weapon("Plasma Bolter", actor, game){
  
  val ID = "RFW"
  
  private val coolOffTime = 2
  private val resetTime = 75
  private val accuracyModifiers = Vector(25, -0.2, 0.15, -0.1, 0)
  private var modifierIndex = 0
  private var lastShotTime = 0
  private val laserSound = new AudioClip("file:src/main/resources/sound/RFWsound.wav")
  
  private def projectileSpeed = if(player.isSlowingTime) player.timeSlowCoefficient * 15 else 15 
  
  //Rapidfireweapon ampuu nopeasti, mutta ensimmäiset laukaukset ovat epätarkkoja
  def fire = {
    
    if(currentTime-lastShotTime >= resetTime) modifierIndex = 0
    
    if (currentTime-lastShotTime >= coolOffTime) {
      
      if(this.user.isDefined) new Projectile(this.game, this.user.get.arm.get.direction, projectileSpeed, 0, -20, this.user.get )
      
      if(Settings.muteSound == false) this.laserSound.play()
      
      lastShotTime = currentTime
      if(modifierIndex < accuracyModifiers.size) modifierIndex += 1
      
       }
    
  }
  
 lazy val sprites = Array(
      
      new GameSprite("file:src/main/resources/Pictures/RapidFireWeapon.png", None, (45.0,45.0), this, (0,0), None), //World image
      new GameSprite("file:src/main/resources/Pictures/RapidFireWeapon.png", None, (35.0,35.0), this, (15,15), None), //Inventory image
      new GameSprite("file:src/main/resources/Pictures/RapidFireWeapon.png", None, (40.0, 40.0), this.game.player, (18,-18), None)  //Equipped image
  
  )
  
  def lookDirectionForSprite = "east"
}