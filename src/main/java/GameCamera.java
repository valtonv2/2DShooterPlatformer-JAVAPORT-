package main.java;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;
import javafx.scene.paint.Color.*;
import java.math.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;



import javafx.scene.transform.*;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.transform.Scale;
import javafx.util.Pair;
import javafx.scene.input.*;
import javafx.scene.text.Text;

class GameCamera {
	
  public Player followee;
  public Game game;
  
  public GamePos location;
  private Double zoomCoefficient = 1.0;    // Zoomauskerroin
  private Scale zoomTransform = new Scale(zoomCoefficient, zoomCoefficient, 400, 400);
  private Boolean followPlayer = true;
  public Double drawDistance = 600.0;
  
  public Double xSpeed = 0.0;
  public Double ySpeed = 0.0;
  public Double zInSpeed = 0.0;
  public Double zOutSpeed = 0.0;
  
  //Konstruktori luokalle
  
  public GameCamera(Player followee) {
	  
	  this.followee = followee;
	  
	  location = new GamePos(new Pair<Double, Double>(followee.location.locationInGame().getKey()+400.0,followee.location.locationInGame().getValue()+200), true);
	  game = this.followee.game;
	  
	  mapModeHelpText.setFill(Color.WHITE);
	    
  }
  
  public Level level() {return this.game.currentLevel;}
  
  public ArrayList<GameTile> entireEnvironment() {
    return level().allTiles;
  }
  
 // public List<Node> colliderImages() { return followee.colliders.stream().flatMap(collider -> collider.images()).collect(Collectors.toList());}
 
  //Karttatilan kuvat
  
  private Rectangle mapOverLay = Helper.anySpriteFromImage("file:src/main/resources/Pictures/MapModeOverLay.png", new Pair<Double, Double>(0.0,0.0), 4000.0, 4000.0);
  private Rectangle mapModeCross = Helper.anySpriteFromImage("file:src/main/resources/Pictures/MapModeCross.png", new Pair<Double, Double>(300.0,300.0), 100.0, 100.0);
  private Text mapModeHelpText = new Text("UP [I] | DOWN [K] | LEFT [J] | RIGHT [L] | ZOOMIN [UP] | ZOOMOUT [DOWN] | RETURN [M] ");
 
  private Optional<Node>mapModeSkin() { 
	  
	  if(this.game.isInmapMode) {
	     return Optional.of(mapOverLay);
	  }else{ 
		 return Optional.empty();
	  }
  }
 
  //Suodattaa koko kentän sisällöstä ne tiilet, jotka näkyvät kullakin hetkellä, ja jotka piirretään
  private List<Enemy> activeEnemies() {return game.enemies.stream().filter(enemy -> enemy.isActive == true).collect(Collectors.toList());}
  private List<GameTile> drawnEnvironment() {return entireEnvironment().stream().filter(tile -> tile.location.isNearOther(this.location, drawDistance) || activeEnemies().stream().anyMatch(enemy -> Helper.absoluteDistance(enemy.location.locationInGame(), tile.location.locationInGame())<100 )).collect(Collectors.toList());}
   
  
  //Luo pelin kuvan joka välitetään gamelle ja sen jälkeen GUI:lle
  public Group cameraImage()  {
  
    Group moonMan = new Group(); 
    moonMan.getChildren().add(followee.image());
    List<Node> projectiles = game.projectiles.stream().map(projectile -> projectile.sprite.image()).collect(Collectors.toList());
    List<Node> tiles = drawnEnvironment().stream().map(tile -> tile.image()).collect(Collectors.toList());
    
   
    List<Node>items = GameWindow.currentGame.currentLevel.itemsInWorld.stream().map( item -> item.sprites.get(0).image()).collect(Collectors.toList());
    
    
    List<Group>enemies = game.enemies.stream().map(enemy -> enemy.image()).collect(Collectors.toList());
    
    ArrayList<Node>effects = new ArrayList<Node>();
    
    if(!level().effects.isEmpty()) {
    	level().effects.stream().map(effect -> effect.image.image()).forEach(image -> effects.add(image));
    }
  
    		
    		//.filter(enemy -> Helper.absoluteDistance(enemy.location.locationInGame(), this.location.locationInGame()) <= drawDistance ).flatMap(enemy -> enemy.image).collect(Collectors.toList());
    Node cursor = game.mouseCursor.image();
    
    ArrayList<Node> worldObjects = new ArrayList<Node>(); 
    worldObjects.add(GameWindow.currentGame.currentLevel.backGround());
    worldObjects.addAll(effects);
    worldObjects.addAll(tiles);
    worldObjects.addAll(items);
    worldObjects.add(moonMan);
    worldObjects.addAll(enemies);
    worldObjects.addAll(projectiles);
    		
    Group guiElements = new Group(GameWindow.PlayerHUD.image(), cursor);

    
    
    Group zoomables = new Group();
    zoomables.getChildren().addAll(worldObjects);
    zoomables.getTransforms().add(zoomTransform);
    
    Group all = new Group(level().backGround(), zoomables);
    if (mapModeSkin().isPresent()) {
    	all.getChildren().addAll(mapOverLay, mapModeCross, mapModeHelpText);
    }
    
    all.getChildren().addAll(guiElements);
    return all;
  }
  
  //Kameran kuvan päivitys
  public void update() {
    
	zoomTransform.setPivotX(GameWindow.stage.getWidth()/2);
	zoomTransform.setPivotY(GameWindow.stage.getHeight()/2);
	
	this.drawDistance = GameWindow.stage.getWidth()/2+200;
    game.projectiles.forEach(projectile -> projectile.updateState());           //Ammusten tilanpäivitys
    game.enemies.forEach(enemy -> enemy.update());                              //Vihollisten tilan päivitys
    level().moveBackGround(-0.1 * game.player.xSpeed, -0.1 * game.player.ySpeed);  //Taustan siirto
    level().effects.stream().forEach(effect -> effect.move());
    if(followPlayer) { location.teleport(followee.location.locationInGame()); }       //Pelaajan seuraaminen
    if(zoomCoefficient < 1){
    drawDistance = GameWindow.stage.getWidth()/2+200 + (1/zoomCoefficient)*(GameWindow.stage.getWidth()/2)+500;      //Piirtoetäisyyden päivitys
    }else{
      drawDistance = GameWindow.stage.getWidth()/2+200;
    }
    
    this.mapModeCross.setLayoutX(this.location.locationInImage().getKey()-350);           //Karttamoodin pitäminen koossa
    this.mapModeCross.setLayoutY(this.location.locationInImage().getValue()-350);
    this.mapModeHelpText.setLayoutX((GameWindow.stage.getWidth()/2) -300);
    this.mapModeHelpText.setLayoutY(GameWindow.stage.getHeight() -50);
  }
  
  //Kameran zoomaus
  public void changeZoom(Double newLevel) {
    this.zoomCoefficient = newLevel;
    zoomTransform.setX(zoomCoefficient);
	zoomTransform.setY(zoomCoefficient);

  }
  
  public void zoomIn(Double amount) {
	  
	  if(this.zoomCoefficient<5) {
		  this.zoomCoefficient += amount;
		 
		  zoomTransform.setX(zoomCoefficient);
		  zoomTransform.setY(zoomCoefficient);
	
	  }else{ 
		  this.zoomCoefficient = 5.0;
		  
		  zoomTransform.setX(zoomCoefficient);
		  zoomTransform.setY(zoomCoefficient);

	  }
  }
  
  public void zoomOut(Double amount) {
	  
	  if(this.zoomCoefficient>0.1) { 
		  this.zoomCoefficient =  this.zoomCoefficient - amount; 
		  
		  zoomTransform.setX(zoomCoefficient);
		  zoomTransform.setY(zoomCoefficient);
	  }else{ 
		  this.zoomCoefficient = 0.1;
		  zoomTransform.setX(zoomCoefficient);
		  zoomTransform.setY(zoomCoefficient);
	  }
  }
  
  public void toggleFreeCamera() { 
  
  if(this.followPlayer == false){
    this.followPlayer = true;
  }else{
    this.followPlayer = false;
  }
 }
  
}
