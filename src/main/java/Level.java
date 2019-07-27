package main.java;

import javafx.scene.shape.*;
import javafx.scene.transform.Rotate;
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
  
  private Rectangle bgRect = new Rectangle(bgX, bgY, 8000, 8000);
  
  public ArrayList<Effect> effects = new ArrayList<Effect>();
  
  public Rectangle backGround() { 
	  bgRect.setX(bgX);
	  bgRect.setY(bgY);
	  bgRect.setFill(backGroundPattern);
	  
	  return bgRect;
	  }
  
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
    public void levelCreator() {
    
    for(int  x = 0; x<levelImage.getWidth(); x++){
    for(int y = 0; y<levelImage.getHeight(); y++){
     
     
      if (Math.floor(pixelReader.getColor(x, y).getRed() * 255.0) == 222.0){                // # = Koristetiili
        Double xPoint = (x*50.0);
        Double yPoint = (y*50.0);
        allTiles.add(new tile(xPoint, yPoint, false, false, decorativeTilePattern, 50.0, 50.0));
        
      }else if(Math.floor(pixelReader.getColor(x, y).getRed() * 255.0) == 174.0){           //Tiili jossa on törmäykset
        Double xPoint = (x*50.0);
        Double yPoint = (y*50.0);
        
        allTiles.add(new tile(xPoint, yPoint, true, false, floorPattern, 50.0, 50.0));
      
      }else if(Math.floor(pixelReader.getColor(x, y).getRed() * 255.0) == 255.0){        // Shooterenemy
        
         Double xPoint = (x*50.0);
         Double yPoint = (y*50.0);
         game.enemies.add(new ShooterEnemy("Mursunen", game, xPoint, yPoint));
         
      }else if(Math.floor(pixelReader.getColor(x, y).getRed() * 255.0) == 235.0){      // Followingenemy
        
         Double xPoint = (x*50.0);
         Double yPoint = (y*50.0);
         System.out.println("Spawning new enemy");
         game.enemies.add(new FollowingEnemy("Corrupted Moon Man", game, xPoint, yPoint));
        
      }else if(Math.floor(pixelReader.getColor(x, y).getRed() * 255.0) == 66.0){                // Rakennuksen taustatiili
        Double xPoint = (x*50.0);
        Double yPoint = (y*50.0);
        allTiles.add(new tile(xPoint, yPoint, false, false, backWallPattern, 50.0, 50.0));
      
      }else if(Math.floor(pixelReader.getColor(x, y).getRed() * 255.0) == 250.0){                // Tikkaat
        Double xPoint = (x*50.0);
        Double yPoint = (y*50.0);
        allTiles.add(new tile(xPoint, yPoint, false, true, ladderPattern, 50.0, 50.0));
        
      
      }else if(Math.floor(pixelReader.getColor(x, y).getRed() * 255.0) == 100.0){                // Health Pack
        Double xPoint = (x*50.0);
        Double yPoint = (y*50.0);
        HealthPack healthPack = new HealthPack(game, 1);
        itemsInWorld.add(healthPack);
        healthPack.isInWorld = true;
        healthPack.locationInWorld = Optional.ofNullable((new GamePos(new Pair<Double, Double>(xPoint, yPoint), false)));
       
       
      }else if(Math.floor(pixelReader.getColor(x, y).getRed() * 255.0) == 12.0){                // Energy Pack
        Double xPoint = (x*50.0);
        Double yPoint = (y*50.0);
        EnergyPack energyPack = new EnergyPack(game, 1);
       
        itemsInWorld.add(energyPack);
        energyPack.isInWorld = true;
        energyPack.locationInWorld = Optional.ofNullable(new GamePos(new Pair<Double, Double>(xPoint, yPoint), false));
      
    
      }else if(Math.floor(pixelReader.getColor(x, y).getRed() * 255.0) == 181.0){                // Slow Firing Weapon
        Double xPoint = (x*50.0);
        Double yPoint = (y*50.0);
        SlowFiringWeapon gun = new SlowFiringWeapon(game, Optional.empty());
        itemsInWorld.add(gun);
        gun.locationInWorld = Optional.ofNullable(new GamePos(new Pair<Double, Double>(xPoint, yPoint), false));
        gun.isInWorld = true;
        
      
      
      }else if(Math.floor(pixelReader.getColor(x, y).getRed() * 255.0) == 246.0){                //RapidFire Weapon
        Double xPoint = (x*50.0);
        Double yPoint = (y*50.0);
        RapidFireWeapon gun = new RapidFireWeapon(game, Optional.empty());
        itemsInWorld.add(gun);
        gun.locationInWorld = Optional.ofNullable(new GamePos(new Pair<Double, Double>(xPoint, yPoint), false));
        gun.isInWorld = true;
        
      }else if(Math.floor(pixelReader.getColor(x, y).getRed() * 255.0) == 3.0){                //Pelaajan spawnaus
       
        Double xPoint = (x*50.0);
        Double yPoint = (y*50.0);
        game.player.location.move(xPoint, yPoint);
      
      
      }else if(Math.floor(pixelReader.getColor(x, y).getRed() * 255.0) == 140.0){                //Tiili, jonka saavutettuaan pelaaja voittaa tason
       
        Double xPoint = (x*50.0);
        Double yPoint = (y*50.0);
        allTiles.add(new TriggerTile(xPoint, yPoint, false, false, goalPattern, 50.0, 50.0,  new Runnable() {
        	
        	@Override
        	public void run() {
          
          Boolean help = GameWindow.currentGame.levelCompletionStatus.get(GameWindow.currentGame.currentLevel.levelNO-1);
          help = true;
       
          GameWindow.menuClock.start();
          GameWindow.clock.stop();
          GameWindow.PlayerHUD.clearAll();                                      //Pelaajan HUD on bugien välttämiseksi tyhjennettävä
          GameWindow.Menus.LevelSelectMenu.level2Button.unlock();
          GameWindow.stage.setScene(GameWindow.Menus.LevelSelectMenu.scene);
          GameWindow.Menus.currentMenu = GameWindow.Menus.LevelSelectMenu;
     
        	}
        }));
      }
     }
    }
    System.out.println("Luotu " + allTiles.size() + " tiiltä" + ": :" + itemsInWorld.size() + " esinettä" );
    
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
    
    public GamePos location;
    public GamePos locationForCollider;
    
    public Rectangle tileImage;
    abstract Rectangle image(); 
    abstract GameTile copy();
    
    
    public void rotate(Double deg) {
    	
    	Rotate transform = new Rotate(deg, location.locationInImage().getKey()+25, location.locationInImage().getValue()+25);
    	tileImage.getTransforms().add(transform);
    	
    }
    	

   
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
		  location = new GamePos(new Pair<Double, Double>(startX, startY), false);
		  locationForCollider = new GamePos(new Pair<Double, Double>(startX + 25.0, startY + 25.0), false);
		  tileImage = new Rectangle(location.locationInImage().getKey(), location.locationInImage().getValue(), width2, height2);
		  
		  tileImage.setFill(pattern);
				  
	  }  
	  
	  public Rectangle image() {
		  
		  this.tileImage.setX(location.locationInImage().getKey());
		  this.tileImage.setY(location.locationInImage().getValue());
		  return this.tileImage;
		  
	  }
	  
	   protected tile copy() {
		  
		  return new tile(this.startX, this.startY, this.hasCoillision, this.isLadder, this.pattern, this.width2, this.height2);
		  
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
		  location = new GamePos(new Pair<Double, Double>(startX, startY), false);
		  locationForCollider = new GamePos( new Pair<Double, Double>(startX + 25.0, startY + 25.0), false);
		  tileImage = new Rectangle(location.locationInImage().getKey(), location.locationInImage().getValue(), width2, height2);
		  
		  
		  tileImage.setFill(pattern);
				  
	  }  
    
    public Rectangle image() {
		  
		  this.tileImage.setX(location.locationInImage().getKey());
		  this.tileImage.setY(location.locationInImage().getValue());
		  return this.tileImage;
		  
	  }
    
    protected TriggerTile copy() {
		  
		  return new TriggerTile(this.startX, this.startY, this.hasCoillision, this.isLadder, this.pattern, this.width2, this.height2, this.function);
		  
	  }
}
  