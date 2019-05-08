package main.java;


import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.*;
import javafx.scene.transform.*;
import javafx.scene.media.*;
import java.math.*;
import javafx.scene.paint.*;
import javafx.scene.Node;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.util.converter.*;
import javafx.util.*;

import java.util.ArrayList;
import java.util.Optional;
import javafx.scene.transform.*;


public class Helper{
  
  
  //Metodi jonka avulla voi helposti lukea useita kuvatiedostoja kerralla
  public static ArrayList<Image> getSpritesFromFolder(String folderPath, String fileNameStart, Range fileNumberRange, String fileType)  {
    
    ArrayList<Image> fileList = ArrayList<Image>()
    		
    for (Int number : fileNumberRange){
      
      String filePath = folderPath + "/" + fileNameStart + number.toString() + fileType;
      fileList.add(Image(filePath));
      System.out.println(filePath);
      }
   
    return fileList;
  }
  
  //Metodi joka muuttaa kuvan teksturoiduksi suorakulmioksi. K�ytet��n erityisesti pelaajan ja vihollisen tapauksessa
    
  public static Rectangle spriteFromImage(Image image) {
        
       ImagePattern texture = new ImagePattern( image, -0.25,-0.1,1.5,1.5,true);
        
       Rectangle rect = new Rectangle(texture, 60, 90, 400 - 30, 400 - 45);
        
       return rect;
     
  }
    
    
  //Metodi joka muuttaa mink� tahansa kuvan teksturoiduksi suorakulmioksi
  public static Rectangle anySpriteFromImage(String imagePath, Pair<Int, Int> location, DOuble spriteWidth, Double spriteHeight){
    
	  Image image = new javafx.scene.image.Image(imagePath);
    
      ImagePattern texture = new ImagePattern( image, 1.0,1.0,1.0,1.0,true);
        
      Rectangle rect = new Rectangle(texture, spriteWidth, spriteHeight, location.getKey(), location.getValue());
    		  
      return rect;
   
  }
  
  def transformToNode[T <: Node](thing:T, transform:List[Transform]) = {
    
    thing.transforms = transform
    thing
    
  }
  
  //Metodi joka mahdollistaa helpon ��nitiedostojen k�yt�n
  public static ArrayList<AudioClip> getAudioFromFolder(String folderPath, String fileNameStart, Range fileNumberRange, String fileType) {
    
    ArrayList<AudioClip> fileList = ArrayList<AudioClip>();
    
    for (number : fileNumberRange){
      
      String filePath = folderPath + "/" + fileNameStart + number.toString() + fileType;
      fileList.add(Image(filePath));
      System.out.println(filePath);
      
      }
   
    return fileList;
  }
  
 //Apumetodi et�isyyksien laskemiseen. Palauttaa suoraviivaisen et�isyyden.
 public static Double absoluteDistance(Pair<Double, Double> a, Pair<Double, Double> b) {
    
    Double xDiff = abs(a.getKey() - b.getKey()).toDouble;
    Double yDiff = abs(a.getValue() - b.getValue()).toDouble;
    
    if(xDiff == 0 && yDiff>0) {return yDiff;}
    else if (yDiff == 0 && xDiff>0) {return xDiff;}
    else if (xDiff>=1 && yDiff>=1) {return sqrt(xDiff*xDiff+yDiff*yDiff);}
    else {return sqrt(xDiff*xDiff+yDiff*yDiff)}
  }
 
  //Apumetodi et�isyyksien laskemiseen. Erottelee x ja y akselit
  public static Pair<Double, Double> axisDistance(Pair<Double, Double> a, Pair<Double, Double> b)  {
    Double xDiff = abs(a.getKey() - b.getKey()).toDouble;
    Double yDiff = abs(a.getValue() - b.getValue()).toDouble;
    Pair<Double, Double> pair = Pair(xDiff, yDiff);
    return pair;
  } 
}
//#################################################################################################################################################################################

trait UsesGameSprite{
  
  var useMirror = false
  def locationForSprite:Option[(Double, Double)]
  def game:Game
  def lookDirectionForSprite:String
  
}

trait UsesAnimatedGameSprite extends UsesGameSprite{
 
  def locationForSprite:Option[(Double, Double)]
  def game:Game
  def lookDirectionForSprite:String
  def isMovingForSprite:Boolean
  
}
//GameSprite-luokka yksinkertaistaa pelin olioiden kuvien laadintaa, muokkaamista ja liikuttamista
//K�ytet��n aluksi esineiden kanssa ja my�hemmin jos aikaa riitt�� muuallakin
//Kuva peliss� saa automaattisesti k�ytt�j�ns� sijainnin joten sit� ei tarvitse grafiikkakomponentissa erikseen p�ivitt��

class GameSprite {
  
  //Parametrit
	String imagePath;
	Optional<Pair<Double, Double>> imageStartLocation;
	Pair<Double, Double> imageDimensions;
	UsesGameSprite user;
    Pair<Double, Double> locationOffset;
    Optional<Pair<Double, Double>> overrideLocation;
	
	
	
	
	
 public Double spriteWidth = imageDimensions.getKey();
 public Double spriteHeight = imageDimensions.getValue();
  
 private ImagePattern texture = new ImagePattern(new javafx.scene.image.Image(imagePath), 0,0,1,1,true);
 private ArrayList<Transform> transforms = ArrayList<Transform>();
  
  
 public Rectangle normalImage() {
  
  if(this.overrideLocation.isPresent) {
    
  return new Rectangle(this.overrideLocation.get.getKey() + locationOffset.getKey(), this.overrideLocation.get.getValue() + locationOffset.getValue(), this.spriteWidth, this.spriteHeight, this.texture );
    
 }else { 
   
  return new Rectangle(user.locationForSprite.get.getKey() + locationOffset.getKey(), user.locationForSprite.get.getValue() + locationOffset.getValue(), spriteWidth, spriteHeight, texture);
    
  }
 }
 
 
 private Rectangle mirrorImage() {
   Rectangle img = normalImage;
   Helper.transformToNode(img, this.mirrorRotate);
   return img;
 }
 
 public Rectangle image() {
	 
	 if(this.user.lookDirectionForSprite == "east") {
		 return this.normalImage;
	 }else {
		 return this.mirrorImage;
	 }

 }
 
 
 public void changeSize(Pair<Double, Double> newDimensions) {
   this.spriteWidth = newDimensions.getKey();
   this.spriteHeight= newDimensions.getValue();
   }
 
 public void rotate(Double amount, Pair<Double, Double> pivot) {
   
   this.image.transforms.add(new Rotate(amount, pivot.getKey(), pivot.getValue()));
   
 }
 
 private Pair<Double, Double> userSpriteLocation() {
	 
	 if(user.locationForSprite.isPresent) {
		 return user.locationForSprite.get;
	 }else {
		 Pair<Double, Double> done = Pair<Double, Double>(0.0, 0.0);
		 return done;
	 } 
 }
 
  private ArrayList<Rotate> mirrorRotate = ArrayList(new Rotate(180.0, userSpriteLocation.getKey(), userSpriteLocation.getValue() , 0, Rotate.YAxis));
  
  
  //Konstruktori luokalle
  public GameSprite(String imagePath, Optional<Pair<Double, Double>> imageStartLocation, Pair<Double, Double >imageDimensions, UsesGameSprite user, Pair<Double, Double> locationOffset, Optional<Pair<Double, Double>> overrideLocation) = {
		  
		  this.imagePath = imagePath;
		  this.imageStartLocation = imageStartLocation;
		  this.imageDimensions = imageDimensions;
		  this.user = user;
		  this.locationOffset = locationOffset;
		  this.overrideLocation = overrideLocation;
		  
  }
}

//##########################################################################################################################################################################################################

class AnimatedGameSprite(imageFolderPath:String, fileNameStart:String, fileNumberRange:Range, fileType:String, val imageStartLocation:Option[(Double, Double)], imageDimensions:(Double, Double), user:UsesAnimatedGameSprite, val locationOffset:(Double, Double), isAlwaysMoving:Boolean){
  
  private val images = Helper.getSpritesFromFolder(imageFolderPath, fileNameStart, fileNumberRange, fileType)
  private val textures = images.map(image =>new ImagePattern(image, 0,0,1,1,true) )
 

  private var time = 0
  private var spriteIndex = 0
  var spriteWidth = imageDimensions.getKey()
  var spriteHeight = imageDimensions.getValue()
  
  private def updateCurrentSpriteNumber = {
    
    if (this.time % 5 == 0 && spriteIndex < textures.size-1 && (this.user.isMovingForSprite || this.isAlwaysMoving)) {
      
      spriteIndex += 1
     
      
    }else if ((this.time % 5 == 0 && spriteIndex == textures.size-1) || (!this.user.isMovingForSprite && !this.isAlwaysMoving)){
      
      spriteIndex = 0
      
      }
   
    }
  
  def normalImage = new Rectangle{
    
    x = user.locationForSprite.get.getKey() + locationOffset.getKey()
    y = user.locationForSprite.get.getValue() + locationOffset.getValue()
    width = spriteWidth
    height = spriteHeight
    fill = textures(spriteIndex)  //Blue
    
  }
  
  
 private def mirrorImage = {
   
   val orig = this.normalImage
   Helper.transformToNode(orig, this.mirrorRotate)
   
  }
  
  def image = {
   
    time += 1 
    updateCurrentSpriteNumber
    user.lookDirectionForSprite match{
      
      case "east" => normalImage
      case "west" => mirrorImage
      
    }
   
    
  }
 
 
 def changeSize(newDimensions:(Double, Double)) = {
   this.spriteWidth = newDimensions.getKey()
   this.spriteHeight= newDimensions.getValue()
   }
 
  private def mirrorRotate = List(new Rotate(180.0, user.locationForSprite.get.getKey() ,user.locationForSprite.get.getValue(), 0, Rotate.YAxis))
  

  
}

//###########################################################################################################################################################################

//Actoreille saatavilla oleva k��ntyv� k�si. Huolehtii k�den ja aseen k��nn�st�. Toistaiseksi vain pelaajan k�yt�ss�
class RotatingArm(user:Actor,val direction:DirectionVector){
  
 private val armImage = new GameSprite("file:src/main/resources/Pictures/MoonmanHand.png", None, (40, 25), user, (-5, -13), None)
 private val armRotate = new Rotate(0.0, pivotPoint.getKey(), pivotPoint.getValue(), 400)
  
 private def pivotPoint = user.location.locationInImage
  
  def completeImage = {
   
    armRotate.angle = this.direction.angle * 50
    armRotate.pivotX = pivotPoint.getKey()
    armRotate.pivotY = pivotPoint.getValue()
   
    val group = user.equippedWeapon match{
    
    case Some(weapon) => new Group(armImage.image, weapon.sprites(2).image)
    case None => new Group(armImage.image)
    
  }
    
    
   user.lookDirectionForSprite match{
      
      case "east" => group.transforms.addAll(armRotate)
      case _ => group.transforms.addAll(armRotate)
      
      } 
      
      

    group
    
  } 
  
}

//#########################################################################################################################################################################

//Luokka DirectionVector tarjoaa yksinkertaisemman tavan k�sitell� suuntia esim ammusten tapauksessa
class DirectionVector(var originalStartPoint:(Double, Double), var originalEndPoint:(Double, Double)){
  
  var x = originalEndPoint.getKey() - originalStartPoint.getKey()
  var y = originalEndPoint.getValue() - originalStartPoint.getValue()
  
  def isTowardsLeft = this.x<0
  def isTowardsRight = this.x>0
  
  def length = {
    
    if(x != 0 && y != 0) sqrt(x*x + y*y)
    else if(x==0) y
    else x
    
  }
  
  def toUnitVect:DirectionVector = { //Metodi muuttaa vektorin yksikk�vektoriksi. Uuden vektorin alkupiste on vanhan alkupiste
    
    val length = this.length
    
    if(length == 1) this
    else{
      this.x = this.x/length
      this.y = this.y/length
      this
      
    }
  }
  
  def angle = {
    atan(y/x)
  }
    
  def opposite:DirectionVector = {
    
    new DirectionVector(this.originalEndPoint, this.originalStartPoint)
    
    }
  
  def sum(x:DirectionVector):DirectionVector = {
    
    new DirectionVector(this.originalStartPoint, x.originalEndPoint)
    
  }
  
  def scalarProduct(x:Double):DirectionVector = {
    
    new DirectionVector(this.originalStartPoint, (this.originalStartPoint.getKey() + x*this.x, this.originalStartPoint.getValue() +x*this.y))
    
    }
  
  def update(newStart:(Double, Double), newEnd:(Double, Double)){
    
    this.originalStartPoint = newStart
    this.originalEndPoint = newEnd
    
    x = originalEndPoint.getKey() - originalStartPoint.getKey()
    y = originalEndPoint.getValue() - originalStartPoint.getValue()
    
  }
  
  def copy = new DirectionVector(this.originalStartPoint, this.originalEndPoint)
  
  
}

//###########################################################################################################################################################################
  
  //Luokan GamePos tarkoitus on helpottaa pelin asioiden sijaintien k�sittely�. Sen avulla pelin varsinaisten koordinaattien ja kuvakoordinaattien v�lill� vaihtelu on helppoa.
  //Luokka otetaan k�ytt��n my�hemmin jos aikaa j��
  
class GamePos{

//Parametrit
 Pair<Double, Double> inGameCoordinates;
 Boolean isCenter;
 
 public Optional<GameCamera> center = GameWindow.gameCamera;

 private Double inGameX = inGameCoordinates.getKey();
 private Double inGameY = inGameCoordinates.getValue();
 private Double playerHeightOffset = -10;
  
 public Pair<Double, Double> locationInGame { return new Pair<Double,Double>(inGameX, inGameY); } 
 
 //Jos jonkin asian sijainti muuttuu peliss�, sen sijainti muuttuu kuvassa. Pelaaja on poikkeus.
 
 
 public Pair<Double, Double> locationInImage() {
		 
	if(center.isPresent) {
		
		 if (!this.isCenter){return Pair(inGameX-center.location.locationInGame.getKey()+center.location.locationInImage.getKey(), inGameY - center.location.locationInGame.getValue() + center.location.locationInImage.getValue() + playerHeightOffset);}
	     else { return Pair(GameWindow.stage.width.toDouble/2 ,GameWindow.stage.height.toDouble/2);}
		
		
	} else {
		
		return Pair(0.0, 0.0);
	
	}	 		 
 }
		 
		 

  
 public void move(Double dx, Double dy) {
   this.inGameX = this.inGameX + dx;
   this.inGameY = this.inGameY + dy;
 }
 
 public void teleport(Pair<Double, Double> newLoc) {
   this.inGameX = newLoc.getKey();
   this.inGameY = newLoc.getValue();
 }
 
 public void zero() {
   this.inGameX = 0;
   this.inGameY = 0;
   
 }
 
 //Konstruktori luokalle
 
 public GamePos(Pair<Double, Double> inGameCoord, Boolean isCenterOfAll) {
 	this.inGameCoordinates = inGameCoord;
    this.isCenter = isCenterOfAll;
 }
  
}
  