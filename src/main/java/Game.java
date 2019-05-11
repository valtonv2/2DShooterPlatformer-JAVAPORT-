
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.*;
import javafx.scene.shape.*;
import javafx.util.Pair;
import javafx.scene.media.*;


public class Game {
  
  public boolean isOver = false;
  public boolean isInmapMode = false;
  public int time = 0;
  public Group imageContent = new Group();
  public Scene fullImage = new Scene(imageContent);

  
  AudioClip backGroundMusic = new AudioClip("file:src/main/resources/sound/BackGroundBeat.wav");
  AudioClip timeSlowAmbience = new AudioClip("file:src/main/resources/sound/TimeStopAmbience.wav");
  ArrayList<AudioClip> skyWalkSounds = Helper.getAudioFromFolder("file:src/main/resources/sound/ProjectileBlips", "Blip", 6, ".wav");
  
  public Player player = new Player(0, 0, this); //Sijainnin oltava t�ss� (0,0) jotta levelin spawn-location menee oikein
  
  public ArrayList<Enemy> enemies = new ArrayList<Enemy>();
 
  public Level currentLevel = firstLevel();
  public String currentLevelName = "";
  
  private Level firstLevel() {return new Level("Large City", 1,"file:src/main/resources/Pictures/LevelImageLarge.png", "file:src/main/resources/Pictures/DarkClouds.png", this);}
  private Level secondLevel(){return new Level("Boxes", 2, "file:src/main/resources/Pictures/LevelFloatingBoxes.png", "file:src/main/resources/Pictures/Earth.jpg", this);}
  
  public ArrayList<Boolean> levelCompletionStatus = new ArrayList<Boolean>();
	
  
  
  
  
  public MouseCursor mouseCursor = new MouseCursor(player);
  GameCamera camera = new GameCamera(player);
  public List<Projectile> projectiles = new ArrayList<Projectile>();
  
  public void cleanUp(){
    
    this.projectiles = this.projectiles.stream().filter(projectile -> projectile.hasCollided == false).collect(Collectors.toList());
    this.enemies = this.enemies.stream().filter(enemy -> enemy.isDead == false).collect(Collectors.toList());
    
  }
  
  
  public void reset() {
    
    this.isOver = false;
    player.HP = player.maxHP;
    player.energy = player.maxEnergy;
    player.isDead = false;
    player.xSpeed = 0;
    player.ySpeed = 0;
    player.inventory.clear();
    Pair<Double, Double> pos = player.location.locationInGame();
    player.location.move(-pos.getKey(), -pos.getValue());    // Pelaajan sijainti on nollattava
    this.enemies.clear();
    this.currentLevel = firstLevel();
     
  }
  
  public void swapLevel(Int levelNo){
     player.HP = player.maxHP;
     player.energy = player.maxEnergy;
     player.inventory.clear();
     player.equippedUtilityItem = Optional.empty();
     player.equippedWeapon = Optional.empty();
     Pair<Double, Double> pos = player.location.locationInGame();
     player.location.move(-pos.getKey(), -pos.getValue());   // Pelaajan sijainti on nollattava
     player.xSpeed = 0;
     player.ySpeed = 0;
    
     this.enemies.clear();
     
     switch (levelNo){
       case 1: 
    	   this.currentLevel = firstLevel();
    	   break;
       case 2:
    	   this.currentLevel = secondLevel();
    	   break;
       default:
    	    System.out.println("Tried to switch to level that doesn't exist");
     }
  }
  
  public void toggleMap() {
    
    if(!this.isInmapMode){
     this.isInmapMode = true;
     
     camera.changeZoom(0.5);
     camera.toggleFreeCamera;
     GameWindow.mapClock.start();
    }else{
      this.isInmapMode = false;
     GameWindow.mapClock.stop();
      camera.changeZoom(1);
      camera.toggleFreeCamera;
    }
    
    
    
  }
  
  
  //Konstruktori luokalle
  
  public Game() {
	  this.levelCompletionStatus.add(false);
	  this.levelCompletionStatus.add(false);
	  this.imageContent.getChildren().addAll(camera.cameraImage);
  }
  
  //T�ss� tehd��n kuva joka v�ltet��n GUI:lle
  
  
  
  
 
}
