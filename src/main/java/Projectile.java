package main.java;
import javafx.scene.shape.Circle;
import javafx.util.Pair;
import javafx.scene.media.AudioClip;
import java.util.Optional;
import java.util.Random;

class Projectile extends UsesGameSprite {
  
	
	
  Game game;
  DirectionVector direction;
  Double speed;
  Double locationModifierX;
  Double locationModifierY;
  Actor shooter;
  
  private Player player;
  private MouseCursor cursor;
  private Level level;
  private int projectileRadius = 15;
  private DirectionVector setDir; 
  private int range = 1500;
  private Random randomizer = new Random(9001);

  Boolean hasCollided = false;
  
  public GamePos location;
  
  GameSprite sprite = new GameSprite("file:src/main/resources/Pictures/Projectile.png", Optional.empty(), new Pair<Double, Double>(30.0, 30.0), this, new Pair<Double, Double>(0.0,7.0), Optional.empty());
  //Konstruktori luokalle
  public Projectile(Game game, DirectionVector direction, Double speed, Double locationModifierX, Double locationModifierY, Actor shooter) {
	  
	  this.game = game;
	  this.direction = direction;
	  this.speed = speed;
	  this.locationModifierX = locationModifierX;
	  this.locationModifierY = locationModifierY;
	  this.shooter = shooter;
	  
	  player = game.player;
	  cursor = game.mouseCursor;
	  level = game.currentLevel;
	  setDir = direction.copy();
	  location = new GamePos(new Pair<Double, Double>(xCoordinate(), yCoordinate()), false);
	  
	  lookDirectionForSprite = "east"; 
	  
	  //Lisätään ammus pelin ammusten listaan. Sen avulla kaikkia ammuksia on helppo hallita
	  game.projectiles.add(this);
	    
 }
  
  public Double xCoordinate() { return shooter.location.locationInGame().getKey() + locationModifierX; };
  public Double yCoordinate() { return shooter.location.locationInGame().getValue() + locationModifierY; };
  
  
  public void move() {
    
    DirectionVector dir = setDir.toUnitVect().scalarProduct(speed);
     
      this.location.move(dir.x, dir.y);

  }
   
  public Circle debugLoc() { return new Circle(10, location.locationInGame().getKey(), location.locationInGame().getValue());}
  
  
  public void addSpeedModifier(Double modifier){
    
   this.speed = modifier * this.speed;
    
  }
        
  
  //Huolehtii törmäyksistä
  public void coillisionDetection()  {
    //Ammus ja seinä
    if(level.levelGeometryHitBox.stream().map(tile -> tile.locationForCollider).anyMatch(coordPair -> axisDistance(coordPair, this.location.locationInGame()).getKey() <= projectileRadius + 25  && axisDistance(coordPair, this.location.locationInGame()).getValue() <= projectileRadius + 25)){
      this.hasCollided = true;
      
     }
    
    //Ammus ja vihollinen
    if (game.enemies.stream().anyMatch(enemy -> axisDistance(enemy.location.locationInGame(), location.locationInGame()).getKey()<=30 && axisDistance(enemy.location.locationInGame(), location.locationInGame()).getValue()<=30 && this.shooter != enemy && !enemy.isShielding)){
      this.hasCollided = true;
      game.enemies.stream().filter(enemy -> axisDistance(enemy.location.locationInGame(), location.locationInGame()).getKey()<=30 && axisDistance(enemy.location.locationInGame(), location.locationInGame()).getValue()<=30).forEach(enemy -> enemy.takeDamage(100.0));
    
    }else if(game.enemies.stream().anyMatch(enemy -> axisDistance(enemy.location.locationInGame(), location.locationInGame()).getKey()<=30 && axisDistance(enemy.location.locationInGame(), location.locationInGame()).getValue()<=30 && this.shooter != enemy && enemy.isShielding)){
      
      this.setDir = this.setDir.opposite();
      this.shooter = this.game.enemies.get(0);
      this.game.player.shieldBounceSound.play(Settings.musicVolume());
      
    }
   
  
  //Ammus ja pelaaja
  if (axisDistance(player.location.locationInGame(), this.location.locationInGame()).getKey() < 30 && axisDistance(player.location.locationInGame(), this.location.locationInGame()).getValue() < 45 && player.isShielding == false && this.shooter != player && !player.isSlowingTime){
    player.takeDamage(333.0);
    this.hasCollided = true;
    
  }else if(axisDistance(player.location.locationInGame(), this.location.locationInGame()).getKey() < 30 && axisDistance(player.location.locationInGame(), this.location.locationInGame()).getValue() < 45 && player.isShielding  && this.shooter != player && !player.isSlowingTime){
    //Kimpoaminen takaisin
    this.setDir = this.setDir.opposite();
    this.game.player.shieldBounceSound.play(Settings.musicVolume());
    this.shooter = player;
    
   }
  
   else if(player.southCollider.locations().stream().anyMatch(location -> axisDistance(location, this.location.locationInGame()).getKey() < 15 && axisDistance(location, this.location.locationInGame()).getValue() < 15) && player.isSlowingTime && this.shooter != player && player.isShielding){
    //Kävely ammusten päällä kun suojakenttä on käytössä
     player.ySpeed = -3.0;
     this.setDir = this.setDir.opposite();
     this.shooter = player;
   }
  
  else if(player.southCollider.locations().stream().anyMatch(location ->axisDistance(location, this.location.locationInGame()).getKey() < 15 && axisDistance(location, this.location.locationInGame()).getValue() < 15) && player.isSlowingTime && this.shooter != player){
    //Kävely ammusten päällä
     player.ySpeed = -3.0;
     
     AudioClip blip = game.skyWalkSounds.get(randomizer.nextInt(5));
     if (!blip.isPlaying()){
       blip.play(Settings.musicVolume());
     }
   }
  
   else if(axisDistance(player.location.locationInGame(), this.location.locationInGame()).getKey() < 30 && axisDistance(player.location.locationInGame(), this.location.locationInGame()).getValue() < 45 && player.isShielding  && this.shooter != player && player.isSlowingTime){
    this.setDir = this.setDir.opposite();
    this.game.player.shieldBounceSound.play(Settings.musicVolume());
    this.shooter = player;
   }
  
  else if (axisDistance(player.location.locationInGame(), this.location.locationInGame()).getKey() < 30 && axisDistance(player.location.locationInGame(), this.location.locationInGame()).getValue() < 45 && player.isShielding == false && this.shooter != player){
    player.takeDamage(100.0);
    this.hasCollided = true;
    
  }
  
  
}
  
  
  //Metodi, joka päivittää ammuksen tilaa. Kutsutaan joka tick
  public void updateState() {
    if (this.game.time%100 == 0 && Helper.absoluteDistance(player.location.locationInGame(), this.location.locationInGame())>this.range) {
    	this.hasCollided  = false;
    }
    this.coillisionDetection();
    this.move();
  
  }
  
  
   private Pair<Double, Double> axisDistance(Pair<Double, Double> a, Pair<Double, Double> b) {return Helper.axisDistance(a, b); }
   
   public Optional<Pair<Double, Double>> locationForSprite(){return Optional.ofNullable(this.location.locationInImage());}
 
}