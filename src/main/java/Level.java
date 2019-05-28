package main.java;

import javafx.scene.shape.*;
import javafx.scene.paint.Color;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.paint.ImagePattern;
import javafx.util.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

class Level {
  
	
  String name;
  int levelNO;
  String layoutPath;
  String backGroundPath; 
  Game game;
  
  //Ladataan kaikki kenttään liittyvät kuvat muistiin
  private Image decorativeTileSprite = new Image("file:src/main/resources/Pictures/DecorativeTexture.png");
  private ImagePattern decorativeTilePattern = new ImagePattern(decorativeTileSprite, 0,0,1,1,true);
  private Image backWallImage = new Image("file:src/main/resources/Pictures/Tiilitekstuuri.jpg");
  private ImagePattern backWallPattern = new ImagePattern(backWallImage, 0,0,1,1,true);
  private Image floorImage = new Image("file:src/main/resources/Pictures/floorTexture.png");
  private ImagePattern floorPattern = new ImagePattern(floorImage, 0,0,1,1,true);
  private Image ladderImage = new Image("file:src/main/resources/Pictures/Ladder.png");
  private ImagePattern ladderPattern = new ImagePattern(ladderImage, 0,0,1,1,true);
  private Image goalImage = new Image("file:src/main/resources/Pictures/FailTexture.png");
  private ImagePattern goalPattern = new ImagePattern(goalImage, 0,0,1,1,true);
  
  private Image backGroundSprite;
  private ImagePattern backGroundPattern;
  
  //Seuraavia muuttujia käytetään taustan liikuttamiseen
  private Double bgX = -2000.0;
  private Double bgY = -4000.0;
  
  public Rectangle backGround() { return new Rectangle(8000, 8000, bgX, bgY);}
  
  //Metodi joka siirtää taustaa
  public void moveBackGround(Double dX, Double dY){
    
    this.bgX += dX;
    this.bgY += dY;
    
  }
    
  //Sisältää kentän kaikki tiilet. Jatkokäsittely ja kuvan luonti tapahtuvat GameCamerassa
  ArrayList<GameTile> allTiles = new ArrayList<GameTile>();
  
  List<GameTile> ladderTiles = allTiles.stream().filter(tile -> tile.isLadder).collect(Collectors.toList());
  
  //LevelCreator-metodi lisää tänne tiilten koordinaatit, joilla on "hitbox"
  //Hyödynnetään esim. Projectile-luokan coillisionDetection-metodissa
  List<GameTile> levelGeometryHitBox = allTiles.stream().filter(tile -> tile.hasCoillision).collect(Collectors.toList());
  
  public List<Circle> levelGeomHitboxDebug() { 
	  return levelGeometryHitBox.stream().map(tile -> new Circle(tile.location.locationInImage().getKey(), tile.location.locationInImage().getValue(), 2, Color.ORANGERED)).collect(Collectors.toList());
    }
  //Lista maailmassa vapaina olevista esineistä
  public ArrayList<Item> itemsInWorld = new ArrayList<Item>();
    
   private Image levelImage;
   private PixelReader pixelReader;
    

    
  //Metodi, joka luo kentän kuvan pikselien punaisten väriarvojen perusteella
    public void  levelCreator() {
    
    for(int  x = 0; x<levelImage.getWidth(); x++){
    for(int y = 0; y<levelImage.getHeight(); y++){
     
     
      if ((pixelReader.getColor(x, y).getRed() * 255) == 222){                // # = Koristetiili
        Double xPoint = (x*50.0);
        Double yPoint = (y*50.0);
        allTiles.add(new tile(xPoint, yPoint, false, false, decorativeTilePattern, 50.0, 50.0));
        
      }else if((pixelReader.getColor(x, y).getRed() * 255) == 174){           //Tiili jossa on törmäykset
        Double xPoint = (x*50.0);
        Double yPoint = (y*50.0);
        
        allTiles.add(new tile(xPoint, yPoint, true, false, floorPattern, 50.0, 50.0));
      
      }else if((pixelReader.getColor(x, y).getRed() * 255) == 255){        // Shooterenemy
        
         Double xPoint = (x*50.0);
         Double yPoint = (y*50.0);
         game.enemies.add(new ShooterEnemy("Mursunen", game, xPoint, yPoint));
         
      }else if((pixelReader.getColor(x, y).getRed() * 255) == 235){      // Followingenemy
        
         Double xPoint = (x*50.0);
         Double yPoint = (y*50.0);
         System.out.println("Spawning new enemy");
         game.enemies.add(new FollowingEnemy("Corrupted Moon Man", game, xPoint, yPoint));
        
      }else if((pixelReader.getColor(x, y).getRed() * 255) == 66){                // Rakennuksen taustatiili
        Double xPoint = (x*50.0);
        Double yPoint = (y*50.0);
        allTiles.add(new tile(xPoint, yPoint, false, false, backWallPattern, 50.0, 50.0));
      
      }else if((pixelReader.getColor(x, y).getRed() * 255) == 250.0){                // Tikkaat
        Double xPoint = (x*50.0);
        Double yPoint = (y*50.0);
        allTiles.add(new tile(xPoint, yPoint, false, true, ladderPattern, 50.0, 50.0));
        
      
      }else if((pixelReader.getColor(x, y).getRed() * 255) == 100){                // Health Pack
        Double xPoint = (x*50.0);
        Double yPoint = (y*50.0);
        HealthPack healthPack = new HealthPack(game, 1);
        itemsInWorld.add(healthPack);
        healthPack.isInWorld = true;
        healthPack.locationInWorld = Optional.ofNullable((new GamePos(new Pair<Double, Double>(xPoint, yPoint), false)));
       
       
      }else if((pixelReader.getColor(x, y).getRed() * 255) == 12){                // Energy Pack
        Double xPoint = (x*50.0);
        Double yPoint = (y*50.0);
        EnergyPack energyPack = new EnergyPack(game, 1);
        itemsInWorld.add(energyPack);
        energyPack.isInWorld = true;
        energyPack.locationInWorld = Optional.ofNullable(new GamePos(new Pair<Double, Double>(xPoint, yPoint), false));
      
    
      }else if((pixelReader.getColor(x, y).getRed() * 255) == 181){                // Slow Firing Weapon
        Double xPoint = (x*50.0);
        Double yPoint = (y*50.0);
        SlowFiringWeapon gun = new SlowFiringWeapon(game, Optional.empty());
        itemsInWorld.add(gun);
        gun.locationInWorld = Optional.ofNullable(new GamePos(new Pair<Double, Double>(xPoint, yPoint), false));
        gun.isInWorld = true;
        
      
      
      }else if((pixelReader.getColor(x, y).getRed() * 255) == 246){                //RapidFire Weapon
        Double xPoint = (x*50.0);
        Double yPoint = (y*50.0);
        RapidFireWeapon gun = new RapidFireWeapon(game, Optional.empty());
        itemsInWorld.add(gun);
        gun.locationInWorld = Optional.ofNullable(new GamePos(new Pair<Double, Double>(xPoint, yPoint), false));
        gun.isInWorld = true;
        
      }else if((pixelReader.getColor(x, y).getRed() * 255) == 3){                //Pelaajan spawnaus
       
        Double xPoint = (x*50.0);
        Double yPoint = (y*50.0);
        game.player.location.move(xPoint, yPoint);
      
      
      }else if((pixelReader.getColor(x, y).getRed() * 255) == 140){                //Tiili, jonka saavutettuaan pelaaja voittaa tason
       
        Double xPoint = (x*50.0);
        Double yPoint = (y*50.0);
        allTiles.add(new TriggerTile(xPoint, yPoint, false, false, goalPattern, 50.0, 50.0,  new Runnable() {
        	
        	@Override
        	public void run() {
          
          Boolean help = GameWindow.currentGame.levelCompletionStatus.get(GameWindow.currentGame.currentLevel.levelNO-1);
          help = true;
       
          GameWindow.menuClock.start();
          GameWindow.clock.stop();
          PlayerHUD.clearAll();                                      //Pelaajan HUD on bugien välttämiseksi tyhjennettävä
          GameWindow.stage.setScene(Menus.LevelSelectMenu.scene);
          Menus.currentMenu = Menus.LevelSelectMenu;
     
        	}
        }));
      }
     }
    }
    System.out.println("Luotu " + allTiles.size() + " tiiltä");
    }
       
  public void gravity(Double strength) {
    
    game.player.ySpeed += strength;
    
  }
      
  public void spawnItem(Item item, Pair<Double, Double>location) {
     
     item.isInWorld = true;
     item.locationInWorld = Optional.ofNullable(new GamePos(new Pair<Double, Double>(location.getKey(), location.getValue()), false));
     this.itemsInWorld.add(item);
     
   }
  
  public Pair<Double, Double> dimensions(){ return new Pair<Double, Double>(this.levelImage.getWidth()*50.0, this.levelImage.getHeight()*50.0);}
    
  //Konstruktori luokalle
  public Level(String name, int levelNO, String layoutPath, String backGroundPath, Game game) {
  
  this.name = name;
  this.levelNO = levelNO;
  this.layoutPath = layoutPath;
  this.backGroundPath = backGroundPath;
  this.game = game;
  backGroundSprite = new Image(backGroundPath);
  backGroundPattern = new ImagePattern(backGroundSprite, 0.5,0.9,1,1,true);
  levelImage = new Image(layoutPath);
  pixelReader = levelImage.getPixelReader();
  
  levelCreator(); //Luodaan taso
  this.levelGeometryHitBox = allTiles.stream().filter(tile -> tile.hasCoillision).collect(Collectors.toList()); 
  this.levelGeometryHitBox.stream().map(tile -> tile.locationForCollider).collect(Collectors.toList()); 
  game.currentLevelName = this.name;
  }

  
}
//################################################################################################################################################################
  //Yksinkertainen perusrakennuspalikka kentälle.
 
  abstract class GameTile {
   
	protected Double startX;
	protected Double startY;
	protected Boolean hasCoillision;
	protected Boolean isLadder;
	protected ImagePattern pattern;
	protected Double width2;
	protected Double height2;
    
    public GamePos location = new GamePos(new Pair<Double, Double>(startX, startY), false);
    public Pair<Double, Double>locationForCollider = new Pair<Double, Double>(startX + 25.0, startY + 25.0);
    
    public Rectangle tileImage = new Rectangle(width2, height2, location.locationInImage().getKey(), location.locationInImage().getValue());
   
 }
  
  class tile extends GameTile {
	  
	  public tile(Double startX, Double startY, Boolean hasCoillision, Boolean isLadder, ImagePattern pattern, Double width2, Double height2) {
		  
		  this.startX = startX;
		  this.startY = startY;
		  this.hasCoillision = hasCoillision;
		  this.isLadder = isLadder;
		  this.pattern = pattern;
		  this.width2 = width2;
		  this.height2 = height2;
		  
		  tileImage.setFill(pattern);
				  
	  }  
  }
 
  
  //Tiilen alatyyppi joka suorittaa parametrina annettavan funktion pelaajan koskettaessa sitä
  //Funktiokutsu tapahtuu colliderissa
  class TriggerTile extends GameTile{
    
	public Runnable function;  
	  
    public void trigger(){
    	
    	function.run();
    }
    
    public TriggerTile(Double startX, Double startY, Boolean hasCoillision, Boolean isLadder, ImagePattern pattern, Double width2, Double height2, Runnable function) {
		  
		  this.startX = startX;
		  this.startY = startY;
		  this.hasCoillision = hasCoillision;
		  this.isLadder = isLadder;
		  this.pattern = pattern;
		  this.width2 = width2;
		  this.height2 = height2;
		  this.function = function;
		  
		  tileImage.setFill(pattern);
				  
	  }  
}
  