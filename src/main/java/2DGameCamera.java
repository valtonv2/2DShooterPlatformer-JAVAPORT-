package main.java;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
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

//2DGameCamera piirtää pelin kuvan ja liikuttaa piirrettävää ympäristöä

class GameCamera{
	
	public Player followee;
  
  public GamePos location = new GamePos(new Pair<Double, Double>(followee.location.locationInGame().getKey()+400.0,followee.location.locationInGame().getValue()+200), true);
  private Double zoomCoefficient = 1.0;    // Zoomauskerroin
  private Scale zoomTransform = new Scale(zoomCoefficient, zoomCoefficient, GameWindow.stage.width.toDouble/2, GameWindow.stage.height.toDouble/2);
  private Boolean followPlayer = true;
  public int drawDistance = GameWindow.stage.width.toDouble/2+200;
  
  public Double xSpeed = 0.0;
  public Double ySpeed = 0.0;
  public Double zInSpeed = 0.0;
  public Double zOutSpeed = 0.0;
  
  Game game = this.followee.game;
  
  //Konstruktori luokalle
  
  public GameCamera(Player followee) {
	  
	  this.followee = followee;
	  
	  mapModeHelpText.setFill(Color.WHITE);
	    
  }
  
  
  
  
  public Level level() {return this.game.currentLevel;}
  
  public ArrayList<GameTile> entireEnvironment() {
    return level().allTiles;
  }
  
  public List colliderImages() { followee.colliders.stream().flatMap(collider -> collider.images()).collect(Collectors.toList());}
 
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
  private List<Enemy> activeEnemies = game.enemies.stream().filter(enemy -> enemy.isActive == true).collect(Collectors.toList());
  private List<GameTile> drawnEnvironment = entireEnvironment().stream().filter(tile -> tile.location.isNearOther(this.location, drawDistance) || activeEnemies.stream().anyMatch(enemy -> Helper.absoluteDistance(enemy.location.locationInGame(), tile.location.locationInGame())<100 )).collect(Collectors.toList());
  
  //Luo pelin kuvan joka välitetään gamelle ja sen jälkeen GUI:lle
  public Group cameraImage()  {
   
    Node moonMan = followee.image();
    Node arm = followee.arm.get().completeImage();
    List<Node> projectiles = game.projectiles.stream().map(projectile -> projectile.sprite.image()).collect(Collectors.toList());
    List<Node> tiles = drawnEnvironment.stream().map(tile -> tile.tileImage).collect(Collectors.toList());
    List<Node>items = level.itemsInWorld.map( item -> item.sprites(0).image()).collect(Collectors.toList());
   
    List<Node>enemies = game.enemies.stream().filter(enemy -> Helper.absoluteDistance(enemy.location.locationInGame(), this.location.locationInGame()) <= drawDistance ).flatMap(enemy -> enemy.image()).collect(Collectors.toList());
    Node cursor = game.mouseCursor.image();
    
    List<Node> worldObjects = tiles + items + moonMan +  enemies + projectiles;
    Group guiElements = new Group(PlayerHUD.image, cursor);
    
    val zoomables = new Group();
    zoomables.children.addAll(worldObjects);
    zoomables.transforms_=(List(zoomTransform));
    
    Group all = new Group(level.backGround, zoomables);
    if (mapModeSkin.isPresent()) {
    	all.children.addAll(mapOverLay, mapModeCross, mapModeHelpText);
    }
    
    all.children.addAll(guiElements);
    return all;
  }
  
  //Kameran kuvan päivitys
  public void update() {
                     
    game.projectiles.forEach(projectile -> projectile.updateState());           //Ammusten tilanpäivitys
    game.enemies.forEach(enemy -> enemy.update());                              //Vihollisten tilan päivitys
    level().moveBackGround(-0.1 * game.player.xSpeed, -0.1 * game.player.ySpeed);  //Taustan siirto
    if(followPlayer) { location.teleport(followee.location.locationInGame()); }       //Pelaajan seuraaminen
    if(zoomCoefficient < 1){
    drawDistance = GameWindow.stage.width.toDouble/2+200 + (1/zoomCoefficient)*(GameWindow.stage.width.toDouble/2)+500;      //Piirtoetäisyyden päivitys
    }else{
      drawDistance = GameWindow.stage.width.toDouble/2+200;
    }
    
    this.mapModeCross.setLayoutX(this.location.locationInImage().getKey()-350);           //Karttamoodin pitäminen koossa
    this.mapModeCross.setLayoutY(this.location.locationInImage().getValue()-350);
    this.mapModeHelpText.setLayoutX((GameWindow.stage.width.toDouble/2) -300);
    this.mapModeHelpText.setLayoutY(GameWindow.stage.height.toDouble -50);
  }
  
  //Kameran zoomaus
  public void changeZoom(Double newLevel) {
    this.zoomCoefficient = newLevel;
  }
  
  public void zoomIn(Double amount) {
	  
	  if(this.zoomCoefficient<5) {
		  this.zoomCoefficient += amount;
	  }else{ 
		  this.zoomCoefficient = 5.0;
	  }
  }
  
  public void zoomOut(Double amount) {
	  
	  if(this.zoomCoefficient>0.1) { 
		  this.zoomCoefficient =  this.zoomCoefficient - amount; 
	  }else{ 
		  this.zoomCoefficient = 0.1;
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
