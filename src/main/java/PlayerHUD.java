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

public class PlayerHUD{

  
  public WeaponHud weaponHud = new WeaponHud();            //Asevalikko
  public EquipmentBox equipmentBox = new EquipmentBox();   //Hyötytavaralaatikko
  public GameBar healthBar = new GameBar("file:src/main/resources/StyleSheets/HealthBarStyle.css",new Pair<Double, Double>(1.0, 200.0), GameWindow.currentGame);
  public GameBar energyBar = new GameBar("file:src/main/resources/StyleSheets/EnergyBarStyle.css",new Pair<Double, Double>(1.0, 240.0), GameWindow.currentGame);
  public  NotificationArea notificationArea = new NotificationArea(); //Ilmoitusalue
  
  public Group image() {
    
	  Group done = new Group();
	  done.getChildren().addAll(notificationArea.image(), weaponHud.image(), equipmentBox.image(), healthBar.image(), energyBar.image());
	  return done;
   
  }
  
  public void clearAll() {
    weaponHud.empty();
    equipmentBox.empty();
    notificationArea.clear();
  }



//###########################################################################################################################################################

// EquipmentBox näyttää pelaajalle mukaan poimitut hyötytavarat kuten HealthPackit ja mahdollistaa 
// niiden välillä valitsemisen

class EquipmentBox {
  
 
  public Pair<Double, Double> location = new Pair<Double, Double>(10.0, 80.0);
  public ItemBox box = new ItemBox(location, GameWindow.currentGame);
  private int selectedIndex = 0;
 
  private List<UtilityItem> possibleContents() {return player().inventory.values().stream().filter(item -> item instanceof UtilityItem ).map(item -> (UtilityItem)item).collect(Collectors.toList());}
  
  
  public Player player() {return GameWindow.currentGame.player;}
  
  
  public void moveRight() {
    
    if(this.selectedIndex < this.possibleContents().size() - 1) {
    	
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
		    	
		    this.selectedIndex = this.possibleContents().size() - 1;
		    this.updateItems();
		    	
		    }
     
    } 
   
   public void updateItems(){
	   
	 Optional<Item> possibleItem = box.item();
     
     if(possibleItem.isPresent() && possibleItem.get() instanceof UtilityItem && ((UtilityItem) possibleItem.get()).isSpent()){
       this.box.removeItem();
       player().equippedUtilityItem = Optional.empty();
     }
     
     if (IntStream.range(0, possibleContents().size()-1).anyMatch(num -> num == selectedIndex)) selectedIndex = Math.max(this.possibleContents().size()-1,0);
       
     if(this.possibleContents().size() == 0) {System.out.println("Tried to update utilityitems but there were none in player inventory");} 
     else {
    	 UtilityItem item = possibleContents().get(selectedIndex);
         box.insertItem(item);
    	 player().equipUtilItem(item);
    	 
    	 
     } 
   }
   
  public void empty() {
     
     if (!this.box.isFree()) this.box.removeItem();
     
   }
   
  public Group image() { return box.fullImage();}
     
}

//###########################################################################################################################################################

// WeaponHud näyttää pelaajalle mukaan poimitut aseet. Sen avulla aseiden välillä voi valita.

class WeaponHud {
  
  private Pair<Double, Double> location = new Pair<Double, Double>(10.0, 0.1);
  public ArrayList<ItemBox> weaponBoxes = new ArrayList<ItemBox>(); 
 
  private int selectedBoxNumber = 0;
  
  
  //Konstruktori
  public WeaponHud() {
	
	  this.weaponBoxes.add(new ItemBox(new Pair<Double, Double>(location.getKey() + 70.0 * 0, location.getValue() ), GameWindow.currentGame));
	  this.weaponBoxes.add(new ItemBox(new Pair<Double, Double>(location.getKey() + 70.0 * 1, location.getValue() ), GameWindow.currentGame));
	  this.weaponBoxes.add(new ItemBox(new Pair<Double, Double>(location.getKey() + 70.0 * 2, location.getValue() ), GameWindow.currentGame));
	  this.weaponBoxes.add(new ItemBox(new Pair<Double, Double>(location.getKey() + 70.0 * 3, location.getValue() ), GameWindow.currentGame));
	  this.weaponBoxes.add(new ItemBox(new Pair<Double, Double>(location.getKey() + 70.0 * 4, location.getValue() ), GameWindow.currentGame));
	  this.weaponBoxes.add(new ItemBox(new Pair<Double, Double>(location.getKey() + 70.0 * 5, location.getValue() ), GameWindow.currentGame));
  
  
  
  }
  
  
  public Player player() { return GameWindow.player();}
  
  public List<String> itemsInBoxes() { 

	  return weaponBoxes.stream().map(box -> getBoxItemName(box)).collect(Collectors.toList());
  }
  
  public void selectBox(int boxNumber) {
	
	 
	Optional<ItemBox> possibleBox = Optional.of(weaponBoxes.get(boxNumber));
	
	if(possibleBox.isPresent()) {
      weaponBoxes.stream().forEach(box -> box.deselect());
      weaponBoxes.get(boxNumber).select();
      player().equippedWeapon = Optional.empty();
      player().equipWeapon(Optional.of( (Weapon)possibleBox.get().item().get())); //Jos asevalikon laatikossa on jotain se sisältää varmasti aseen
      
      if(selectedBoxNumber != boxNumber) {
    	  weaponBoxes.get(selectedBoxNumber).deselect();
      }
      selectedBoxNumber = boxNumber;
	}else {
      weaponBoxes.get(selectedBoxNumber).deselect();
      selectedBoxNumber = 0;
      weaponBoxes.get(selectedBoxNumber).select();
      player().equippedWeapon = Optional.empty();
      player().equipWeapon(Optional.of( (Weapon)weaponBoxes.get(selectedBoxNumber).item().get()));
      }
     }
  
  public void selectNext() {
    
	  weaponBoxes.stream().forEach(box -> box.deselect());
	  
    if (selectedBoxNumber == weaponBoxes.size() - 1){
       selectBox(0);
       selectedBoxNumber = 0;
       }else{
       selectBox(selectedBoxNumber + 1);
    }
   }
  
  public void selectPrevious() {
	  
	  weaponBoxes.stream().forEach(box -> box.deselect());
    
    if (selectedBoxNumber == 0){
       selectBox(weaponBoxes.size() - 1);
       }else{
       selectBox(selectedBoxNumber - 1);
    }
    
  }
  
  public void updateItems() {
    
	List<Weapon> possibleContents = player().inventory.values().stream().filter(item -> item instanceof Weapon && !itemsInBoxes().contains(item)).map(item -> (Weapon)item ).collect(Collectors.toList());
   // List<Weapon> possibleContents = player().inventory.values().stream().filter(item -> item instanceof Weapon && !itemsInBoxes.contains(item))).collect(Collectors.toList()));
   
    int currentBox = 0;
   
    if(!possibleContents.isEmpty()){
   
      for (Weapon weapon:possibleContents){
        
       while(!weaponBoxes.get(currentBox).isFree() && currentBox < weaponBoxes.size()-1) {
         currentBox += 1;
       }
      
      if(weaponBoxes.get(currentBox).isFree()){
      weaponBoxes.get(currentBox).insertItem((Weapon)weapon);
      System.out.println("Weapon inserted to box " + currentBox);
      }
      
     }
    System.out.println("Player inventory: " + GameWindow.currentGame.player.inventory);
    System.out.println("Possible Contents: " + possibleContents);
    System.out.println("WeaponBoxes: " + this.itemsInBoxes());
    
    }else {
   
    	System.out.println("Tried to update weapons but none were in player inventory.");
      
    }
   }
 
  public void empty() {
	  this.weaponBoxes.forEach(box -> box.removeItem());
  }
  
  public Group image() {
	  
      Group done = new Group();
      List<Node> images = weaponBoxes.stream().map(box -> box.fullImage()).collect(Collectors.toList());
	  images.forEach(img -> done.getChildren().add(img));
	  return done;
  }
  
  private String getBoxItemName(ItemBox possibleBox) {
	  
	  if(possibleBox.item().isPresent()) return possibleBox.item().get().name;
	  else return "Nothing";
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
	  lookDirectionForSprite = "east";
	
}
  
  
 public Boolean isFree() {return !this.containedItem.isPresent();}
 
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
  
 
 public Group fullImage() {
	 
	     Group done = new Group();
		 
		 if(this.containedItem.isPresent() && this.containedItem.get() instanceof UtilityItem) {
			 
			 UtilityItem item = (UtilityItem)this.containedItem.get();
			 Text counter = new Text(this.location.getKey() + 40, this.location.getValue() + 55, item.amountOfUseTimes().toString());
			 
			 counter.setFill(javafx.scene.paint.Color.WHITE);
			 item.sprites.get(1).overrideLocation = Optional.of(this.location);
			 if(item.isSpent()) {this.removeItem();}
			 
			 
			 
			 done.getChildren().addAll(emptyImage(), item.sprites.get(1).image(), counter);
			 return done;
			 
			 
		 }else if(this.containedItem.isPresent()) {
			 
			 Item item = this.containedItem.get();

			 
			 item.sprites.get(1).overrideLocation = Optional.of(this.location);
			 done.getChildren().addAll(emptyImage(), item.sprites.get(1).image());
			 return done;
			 
		 }else{
			 
			 done.getChildren().addAll(emptyImage());
			 return done;
			 }
		 }
		 
		 
		 
	
 
 public Optional<Item> item() { return this.containedItem;}
 
 
 public Optional<Pair<Double, Double>> locationForSprite(){return Optional.ofNullable(this.location);}
 
 
 
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
  
  public Group image() {
	  Group done = new Group();
	  done.getChildren().add(bar1);
	  return done;
  }
   
  }

//##############################################################################################################################################

class NotificationArea {
  
  private int timer = 0;

  private Rectangle backGround = new Rectangle(location().getKey(), location().getValue(), 8000.0, 25.0);
  
  private String currentMessage = "";
		  
  private Pair<Double, Double> location() { return new Pair<Double, Double>(GameWindow.stage.getWidth() - (GameWindow.stage.getWidth()/2),0.1);}
  
  public void announce(String message)  {
    timer = 0;
    currentMessage = message;
    
  }
  
  public void clear() {currentMessage = "";}
  
  public Group image() {
	  
	 backGround.setFill(Color.DARKGREY);
   
    timer += 1;
    if(timer > 150) {clear();}
    
    Group done = new Group();
    done.getChildren().addAll(backGround, new Text(location().getKey() + 20, location().getValue() +15, currentMessage));
   
    return done;
   
  }
  
  
  
  
}
}

