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
import java.util.Optional;


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
  
  //Metodi joka muuttaa kuvan teksturoiduksi suorakulmioksi. Käytetään erityisesti pelaajan ja vihollisen tapauksessa
    
  public static Rectangle spriteFromImage(Image image) {
        
       ImagePattern texture = new ImagePattern( image, -0.25,-0.1,1.5,1.5,true);
        
       Rectangle rect = new Rectangle(texture, 60, 90, 400 - 30, 400 - 45);
        
       return rect;
     
  }
    
    
  //Metodi joka muuttaa minkä tahansa kuvan teksturoiduksi suorakulmioksi
  public static Rectangle anySpriteFromImage(String imagePath, Pair<Int, Int> location, DOuble spriteWidth, Double spriteHeight){
    
	  Image image = new javafx.scene.image.Image(imagePath);
    
      ImagePattern texture = new ImagePattern( image, 1.0,1.0,1.0,1.0,true);
        
      Rectangle rect = new Rectangle(texture, spriteWidth, spriteHeight, location._1, location._2);
    		  
      return rect;
   
  }
  
  def transformToNode[T <: Node](thing:T, transform:List[Transform]) = {
    
    thing.transforms = transform
    thing
    
  }
  
  //Metodi joka mahdollistaa helpon äänitiedostojen käytön
  public static ArrayList<AudioClip> getAudioFromFolder(String folderPath, String fileNameStart, Range fileNumberRange, String fileType) {
    
    ArrayList<AudioClip> fileList = ArrayList<AudioClip>();
    
    for (number : fileNumberRange){
      
      String filePath = folderPath + "/" + fileNameStart + number.toString() + fileType;
      fileList.add(Image(filePath));
      System.out.println(filePath);
      
      }
   
    return fileList;
  }
  
 //Apumetodi etäisyyksien laskemiseen. Palauttaa suoraviivaisen etäisyyden.
 public static Double absoluteDistance(Pair<Double, Double> a, Pair<Double, Double> b) {
    
    Double xDiff = abs(a._1 - b._1).toDouble;
    Double yDiff = abs(a._2 - b._2).toDouble;
    
    if(xDiff == 0 && yDiff>0) {return yDiff;}
    else if (yDiff == 0 && xDiff>0) {return xDiff;}
    else if (xDiff>=1 && yDiff>=1) {return sqrt(xDiff*xDiff+yDiff*yDiff);}
    else {return sqrt(xDiff*xDiff+yDiff*yDiff)}
  }
 
  //Apumetodi etäisyyksien laskemiseen. Erottelee x ja y akselit
  public static Pair<Double, Double> axisDistance(Pair<Double, Double> a, Pair<Double, Double> b)  {
    Double xDiff = abs(a._1 - b._1).toDouble;
    Double yDiff = abs(a._2 - b._2).toDouble;
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
//Käytetään aluksi esineiden kanssa ja myöhemmin jos aikaa riittää muuallakin
//Kuva pelissä saa automaattisesti käyttäjänsä sijainnin joten sitä ei tarvitse grafiikkakomponentissa erikseen päivittää

class GameSprite(imagePath:String, var imageStartLocation:Option[(Double, Double)], imageDimensions:(Double, Double), var user:UsesGameSprite, val locationOffset:(Double, Double), var overrideLocation:Option[(Double, Double)]){
  
  
 var spriteWidth = imageDimensions._1
 var spriteHeight = imageDimensions._2
  
 private val texture = new ImagePattern(new scalafx.scene.image.Image(imagePath), 0,0,1,1,true)
 private val transforms = List[Transform]()
  
  
  def normalImage = overrideLocation match{
   
   case Some(location) =>  new Rectangle{
    
    x = location._1 + locationOffset._1
    y = location._2 + locationOffset._2
    width = spriteWidth
    height = spriteHeight
    fill = texture
    
  }
   
   case None  =>  new Rectangle{
    
    x = user.locationForSprite.get._1 + locationOffset._1
    y = user.locationForSprite.get._2 + locationOffset._2
    width = spriteWidth
    height = spriteHeight
    fill = texture
    
  }
 }
 
 private def mirrorImage = {
   val img = normalImage
   Helper.transformToNode(img, this.mirrorRotate)
   img
 }
 
 def image = this.user.lookDirectionForSprite match {
   case "east" => normalImage
   case _ => {
     this.mirrorImage
   }
 }
 
 
 def changeSize(newDimensions:(Double, Double)) = {
   this.spriteWidth = newDimensions._1
   this.spriteHeight= newDimensions._2
   }
 
 def rotate(amount:Double, pivot:(Double, Double)) = {
   
   this.image.transforms.add(new Rotate(amount, pivot._1, pivot._2))
   
 }
 
 private def userSpriteLocation = user.locationForSprite.getOrElse((0.0,0.0))
 
  private def mirrorRotate = List(new Rotate(180.0, userSpriteLocation._1, userSpriteLocation._2 , 0, Rotate.YAxis))
}

//##########################################################################################################################################################################################################

class AnimatedGameSprite(imageFolderPath:String, fileNameStart:String, fileNumberRange:Range, fileType:String, val imageStartLocation:Option[(Double, Double)], imageDimensions:(Double, Double), user:UsesAnimatedGameSprite, val locationOffset:(Double, Double), isAlwaysMoving:Boolean){
  
  private val images = Helper.getSpritesFromFolder(imageFolderPath, fileNameStart, fileNumberRange, fileType)
  private val textures = images.map(image =>new ImagePattern(image, 0,0,1,1,true) )
 

  private var time = 0
  private var spriteIndex = 0
  var spriteWidth = imageDimensions._1
  var spriteHeight = imageDimensions._2
  
  private def updateCurrentSpriteNumber = {
    
    if (this.time % 5 == 0 && spriteIndex < textures.size-1 && (this.user.isMovingForSprite || this.isAlwaysMoving)) {
      
      spriteIndex += 1
     
      
    }else if ((this.time % 5 == 0 && spriteIndex == textures.size-1) || (!this.user.isMovingForSprite && !this.isAlwaysMoving)){
      
      spriteIndex = 0
      
      }
   
    }
  
  def normalImage = new Rectangle{
    
    x = user.locationForSprite.get._1 + locationOffset._1
    y = user.locationForSprite.get._2 + locationOffset._2
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
   this.spriteWidth = newDimensions._1
   this.spriteHeight= newDimensions._2
   }
 
  private def mirrorRotate = List(new Rotate(180.0, user.locationForSprite.get._1 ,user.locationForSprite.get._2, 0, Rotate.YAxis))
  

  
}

//###########################################################################################################################################################################

//Actoreille saatavilla oleva kääntyvä käsi. Huolehtii käden ja aseen käännöstä. Toistaiseksi vain pelaajan käytössä
class RotatingArm(user:Actor,val direction:DirectionVector){
  
 private val armImage = new GameSprite("file:src/main/resources/Pictures/MoonmanHand.png", None, (40, 25), user, (-5, -13), None)
 private val armRotate = new Rotate(0.0, pivotPoint._1, pivotPoint._2, 400)
  
 private def pivotPoint = user.location.locationInImage
  
  def completeImage = {
   
    armRotate.angle = this.direction.angle * 50
    armRotate.pivotX = pivotPoint._1
    armRotate.pivotY = pivotPoint._2
   
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

//Luokka DirectionVector tarjoaa yksinkertaisemman tavan käsitellä suuntia esim ammusten tapauksessa
class DirectionVector(var originalStartPoint:(Double, Double), var originalEndPoint:(Double, Double)){
  
  var x = originalEndPoint._1 - originalStartPoint._1
  var y = originalEndPoint._2 - originalStartPoint._2
  
  def isTowardsLeft = this.x<0
  def isTowardsRight = this.x>0
  
  def length = {
    
    if(x != 0 && y != 0) sqrt(x*x + y*y)
    else if(x==0) y
    else x
    
  }
  
  def toUnitVect:DirectionVector = { //Metodi muuttaa vektorin yksikkövektoriksi. Uuden vektorin alkupiste on vanhan alkupiste
    
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
    
    new DirectionVector(this.originalStartPoint, (this.originalStartPoint._1 + x*this.x, this.originalStartPoint._2 +x*this.y))
    
    }
  
  def update(newStart:(Double, Double), newEnd:(Double, Double)){
    
    this.originalStartPoint = newStart
    this.originalEndPoint = newEnd
    
    x = originalEndPoint._1 - originalStartPoint._1
    y = originalEndPoint._2 - originalStartPoint._2
    
  }
  
  def copy = new DirectionVector(this.originalStartPoint, this.originalEndPoint)
  
  
}

//###########################################################################################################################################################################
  
  //Luokan GamePos tarkoitus on helpottaa pelin asioiden sijaintien käsittelyä. Sen avulla pelin varsinaisten koordinaattien ja kuvakoordinaattien välillä vaihtelu on helppoa.
  //Luokka otetaan käyttöön myöhemmin jos aikaa jää
  
class GamePos{

//Parametrit
 Pair<Double, Double> inGameCoordinates;
 Boolean isCenter;
 
 public Optional<GameCamera> center = GameWindow.gameCamera;

 private Double inGameX = inGameCoordinates._1;
 private Double inGameY = inGameCoordinates._2;
 private Double playerHeightOffset = -10;
  
 public Pair<Double, Double> locationInGame = (inGameX, inGameY); 
 
 //Jos jonkin asian sijainti muuttuu pelissä, sen sijainti muuttuu kuvassa. Pelaaja on poikkeus.
 
 
 public Pair<Double, Double> locationInImage = {
		 
	if(center.isPresent) {
		
		 if (!this.isCenter){return Pair(inGameX-center.location.locationInGame._1+center.location.locationInImage._1, inGameY - center.location.locationInGame._2 + center.location.locationInImage._2 + playerHeightOffset);}
	     else { return Pair(GameWindow.stage.width.toDouble/2 ,GameWindow.stage.height.toDouble/2);}
		
		
	} else {
		
		return Pair(0.0, 0.0);
	
	}	 		 
 }
		 
		 

  
 public void move(Double dx, Double dy) = {
   this.inGameX = this.inGameX + dx;
   this.inGameY = this.inGameY + dy;
 }
 
 public void teleport(Pair<Double, Double> newLoc) = {
   this.inGameX = newLoc._1;
   this.inGameY = newLoc._2;
 }
 
 public void zero = {
   this.inGameX = 0;
   this.inGameY = 0;
   
 }
 
 //Konstruktori luokalle
 
 public GamePos(Pair<Double, Double> inGameCoord, Boolean isCenterOfAll) {
 	this.inGameCoordinates = inGameCoord;
    this.isCenter = isCenterOfAll;
 }
  
}
  