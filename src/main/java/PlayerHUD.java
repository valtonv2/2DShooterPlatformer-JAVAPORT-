package main.java;

import javafx.scene.shape.Rectangle;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;

import java.lang.reflect.Array;
import java.math.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javafx.scene.text.Text;
import javafx.util.Pair;
import javafx.scene.Node;
import java.util.stream.*;
import javafx.scene.Group;
import javafx.scene.paint.Color;

//PlayerHUD - olio kokoaa pelaajan HUDin osaset yhteen

class PlayerHUD{

  
  public static WeaponHud weaponHud = new WeaponHud();            //Asevalikko
  public static EquipmentBox equipmentBox = new EquipmentBox();   //Hyötytavaralaatikko
  public static GameBar healthBar = new GameBar("file:src/main/resources/StyleSheets/HealthBarStyle.css",new Pair<Double, Double>(1.0, 200.0), GameWindow.currentGame);
  public static GameBar energyBar = new GameBar("file:src/main/resources/StyleSheets/EnergyBarStyle.css",new Pair<Double, Double>(1.0, 240.0), GameWindow.currentGame);
  public static NotificationArea notificationArea = new NotificationArea(); //Ilmoitusalue
  
  public static Group image() {
    
	return new Group(notificationArea.image(), weaponHud.image(), equipmentBox.image, healthBar.image, energyBar.image);
   
  }
  
  public static void clearAll() {
    weaponHud.empty();
    equipmentBox.empty();
    notificationArea.clear();
  }

}

//###########################################################################################################################################################

// EquipmentBox näyttää pelaajalle mukaan poimitut hyötytavarat kuten HealthPackit ja mahdollistaa 
// niiden välillä valitsemisen

class EquipmentBox {
  
 
  public Pair<Double, Double> location = new Pair<Double, Double>(10.0, 80.0);
  public ItemBox box = new ItemBox(location, GameWindow.currentGame);
  private int selectedIndex = 0;
 
  private List<UtilityItem> possibleContents = player().inventory.values().stream().filter(item -> item instanceof UtilityItem ).map(item -> (UtilityItem)item).collect(Collectors.toList());
  
  
  public Player player() {return GameWindow.currentGame.player;}
  
  
  public void moveRight() {
    
    if(this.selectedIndex < this.possibleContents.size() - 1) {
    	
    	selectedIndex += 1;
    	this.updateItems();
    	
    }else {
    	
    	this.selectedIndex = 0;
    	this.updateItems();
    	
    }

  }
  
   public void moveLeft() {
    
	   if(this.selectedIndex > 0) {
		    	
		    selectedIndex =  selectedIndex - 1;
		    this.updateItems();
		    	
	   }else{
		    	
		    this.selectedIndex = this.possibleContents.size() - 1;
		    this.updateItems();
		    	
		    }
     
    } 
   
   public void updateItems(){
	   
	 Optional<Item> possibleItem = box.item();
     
     if(possibleItem.isPresent() && possibleItem.get() instanceof UtilityItem && ((UtilityItem) possibleItem.get()).isSpent()){
       this.box.removeItem();
       player().equippedUtilityItem = Optional.empty();
     }
     
     if (IntStream.range(0, possibleContents.size()-1).anyMatch(num -> num == selectedIndex)) selectedIndex = Math.max(this.possibleContents.size()-1,0);
       
     if(this.possibleContents.size() == 0) {System.out.println("Tried to update utilityitems but there were none in player inventory");} 
     else {
    	 UtilityItem item = possibleContents.get(selectedIndex);
         box.insertItem(item);
    	 player().equipUtilItem(item);
    	 
    	 
     } 
   }
   
  public void empty() {
     
     if (!this.box.isFree()) this.box.removeItem();
     
   }
   
  public Node[] image = box.fullImage();
     
}

//###########################################################################################################################################################

// WeaponHud näyttää pelaajalle mukaan poimitut aseet. Sen avulla aseiden välillä voi valita.

class WeaponHud {
  
  private Pair<Double, Double> location = new Pair<Double, Double>(10.0, 0.1);
  public List<ItemBox> weaponBoxes = IntStream.range(0, 5).map(num -> new ItemBox(new Pair<Double, Double>(location.getKey() + 70.0 * num, location.getValue() ), GameWindow.currentGame)).collect(Collectors.toList());
 
  private int selectedBoxNumber = 0;
  
  
  
  public Player player() { return GameWindow.player();}
  
  public List<String> itemsInBoxes = weaponBoxes.stream().map(box -> {
	  if(box.item.isPresent()) { return box.item.get().name;} else {return "Nothing";};
	  }).collect(Collectors.toList());
  
  public void selectBox(int boxNumber) {
	  
	Optional<ItemBox> possibleBox = Optional.of(weaponBoxes[boxNumber]);
	
	if(possibleBox.isPresent()) {
    
      weaponBoxes.get(boxNumber).select();
      player.equipWeapon(Optional.of( (Weapon)possibleBox.get().item()); //Jos asevalikon laatikossa on jotain se sisältää varmasti aseen
      
      if(selectedBoxNumber != boxNumber) {
    	  weaponBoxes.get(selectedBoxNumber).deselect();
      }
      selectedBoxNumber = boxNumber;
	}else {
      weaponBoxes.get(selectedBoxNumber).deselect();
      selectedBoxNumber = 0;
      weaponBoxes.get(selectedBoxNumber).select();
      player.equipWeapon(Optional.of( (Weapon)weaponBoxes.get(selectedBoxNumber).item().get());
      }
     }
  
  public void selectNext() {
    
    if (selectedBoxNumber == weaponBoxes.size() - 1){
       selectBox(0);
       }else{
       selectBox(selectedBoxNumber + 1);
    }
   }
  
  public void selectPrevious() {
    
    if (selectedBoxNumber == 0){
       selectBox(weaponBoxes.size() - 1);
       }else{
       selectBox(selectedBoxNumber - 1);
    }
    
  }
  
  public void updateItems() {
    
    List<Weapon> possibleContents = player().inventory.values().stream().filter(item -> item instanceof Weapon && !itemsInBoxes.contains(item)).map(item -> (Weapon)item).collect(Collectors.toList));
    
    int currentBox = 0;
   
    if(!possibleContents.isEmpty()){
   
      for (Weapon weapon:possibleContents){
        
       while(!weaponBoxes[currentBox].isFree() && currentBox < weaponBoxes.length()) {
         currentBox += 1;
       }
      
      if(currentBox <= weaponBoxes.size-1 && weaponBoxes[currentBox].isFree()){
      weaponBoxes[currentBox].insertItem((Weapon)weapon);
      }
      
     }
    
    }else {
   
    	System.out.println("Tried to update weapons but none were in player inventory.");
      
    }
   }
 
  public void empty() {
	  this.weaponBoxes.forEach(box -> box.removeItem());
  }
  
  public List<Node> image() {
	  weaponBoxes.stream().flatMap(box -> box.fullImage()).collect(Collectors.toList());
  }
  
}

//###########################################################################################################################################################

// Monet palaajan HUDin elementit rakentuvat ItemBox-olioista. 

class ItemBox extends UsesGameSprite{
	
  public Pair<Double, Double> location;
  public Game game;
  
  private Optional<Item> containedItem = Optional.empty();
  private Boolean isSelected = false;
  
  public GameSprite sprites[] = {
   new GameSprite("file:src/main/resources/Pictures/ItemBoxNotSelected.png", Optional.empty(), new Pair<Double, Double>(60.0, 60.0), this, new Pair<Double, Double>(0.0,0.0), Optional.empty()),  //Tyhjä
   new GameSprite("file:src/main/resources/Pictures/ItemBoxSelected.png", Optional.empty(), new Pair<Double, Double>(60.0, 60.0), this, new Pair(0.0,0.0), Optional.empty())  //Valittu tyhjä
  };
  
  //Konstruktori
  
  public ItemBox(Pair<Double, Double> location, Game game) {
	
	  this.location = location;
	  this.game = game;
	
}
  
  
 public Boolean isFree() {return this.containedItem.isPresent();}
 
 public void select()  {this.isSelected = true;}

 public void deselect() {this.isSelected = false;}
 
 public void insertItem(Item item)  {this.containedItem = Optional.of(item);} 
 
 public void removeItem() {this.containedItem = Optional.empty();} 
 
 private Node emptyImage() {
	 
	 if(isSelected) {
		 return sprites[1].image(); 
	 }else{
		 return sprites[0].image(); 
	 }
 }
  
 public Optional<Pair<Double, Double>> locationForSprite = Optional.of(this.location);
 
 public Node[] fullImage() {
		 
		 if(this.containedItem.isPresent() && this.containedItem.get() instanceof UtilityItem) {
			 
			 UtilityItem item = (UtilityItem)this.containedItem.get();
			 Text counter = new Text(this.location.getKey() + 40, this.location.getValue() + 55, item.amountOfUseTimes().toString());
			 
			 counter.setFill(javafx.scene.paint.Color.WHITE);
			 item.sprites[1].overrideLocation = Optional.of(this.location);
			 if(item.isSpent()) {this.removeItem();}
			 
			 Node done[] = {emptyImage(), item.sprites[1].image(), counter };
			 return done;
			 
			 
		 }else if(this.containedItem.isPresent()) {
			 
			 Item item = this.containedItem.get();

			 
			 item.sprites[1].overrideLocation = Optional.of(this.location);
			 Node done[] = {emptyImage(), item.sprites[1].image()};
			 return done;
			 
		 }else{
			 
			 Node done[] = {emptyImage()};
			 return done;
			 }
		 }
		 
		 
		 
	
 
 public Optional<Item> item() { return this.containedItem;}
 
 public String lookDirectionForSprite = "east";
 
 
 
   } 
//###########################################################################################################################################################

class GameBar{
	

	String styleSheetPath;
	Pair<Double, Double> location;
	Game game;
	
  private Pair<Double, Double> dimensions = new Pair<Double, Double>(150.0, 20.0);
  private ProgressBar bar1 = new ProgressBar();
 
  //Konstruktori
  public GameBar(String styleSheetPath, Pair<Double, Double> location, Game game) {
	  
	  this.game = game;
	  this.styleSheetPath = styleSheetPath;
	  this.location = location;
	  
	  //Säädetään palkin koko
	  bar1.prefWidth(dimensions.getKey());
	  bar1.prefHeight(dimensions.getValue());
	  
	  //Annetaan palkin sijainti
	  bar1.setLayoutX(location.getKey());
	  bar1.setLayoutY(location.getValue());
	  
	  //Palkin tyyli
	  
	  bar1.getStylesheets().add(styleSheetPath);
  }
    
  public void setValue(Double value) {
	  bar1.setProgress(value);
  }
  
  public Node image[] = {bar1};
   
  }

//##############################################################################################################################################

class NotificationArea {
  
  private int timer = 0;

  private Rectangle backGround = new Rectangle(location().getKey(), location().getValue(), 8000.0, 25.0);
  
  private String currentMessage = "";
		  
  private Pair<Double, Double> location() { return new Pair<Double, Double>(GameWindow.stage.width.toDouble - (GameWindow.stage.width.toDouble/2),0.1);}
  
  public void announce(String message)  {
    timer = 0;
    currentMessage = message;
    
  }
  
  public void clear() {currentMessage = "";}
  
  public Node[] image() {
	  
	 backGround.setFill(Color.DARKGREY);
   
    timer += 1;
    if(timer > 150) {clear();}
    
    Node done[] = {backGround, new Text(location().getKey() + 20, location().getValue() +15, currentMessage)};
   
    return done;
   
  }
  
  
  
  
}


