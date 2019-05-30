package main.java;

import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;

import javafx.scene.Node;
import javafx.util.Pair;

abstract class Actor extends UsesAnimatedGameSprite {
	  
	  String name;
	  ArrayList<Collider> colliders;
	  Game game;
	  Boolean isWalking = false;
	  Boolean isShielding = false;
	  Boolean isOnLadder = false;
	  Boolean hasDroppedItem = false;
	  int height = 90;
	  int width = 60;
	  
	 
	  //Tavaraluettelo
	  Map<String, Item> inventory;
	  Optional<UtilityItem> equippedUtilityItem = Optional.empty();
	  Optional<Weapon> equippedWeapon = Optional.empty();
	  GamePos location;

	  abstract void stop();
	  abstract void takeDamage(Double amount);
	  ArrayList<Node> image;
	  public Optional<RotatingArm> arm = Optional.empty();
	  
	  //Esineen poimiminen maailmasta
	  
	  public void pickUp(Item item, Boolean isCheatDrop) {
	    
	    
	    if(isCheatDrop){
	      this.inventory.put(item.name, item);
	      item.isInWorld = false;
	    }
	    
	    if(!this.inventory.containsKey(item.name) && !this.hasDroppedItem && game.currentLevel.itemsInWorld.contains(item) && !isCheatDrop) {  // Tiettyä esinettä voi tavaraluettelossa olla vain 1. UtilityItemien käyttökertojen määrä kasvaa poimittaessa niitä lisää.
	      
	      this.inventory.put(item.name, item);
	      item.isInWorld = false;
	      game.currentLevel.itemsInWorld.remove(item);
	      GameWindow.PlayerHUD.notificationArea.announce(this.name + " picked up " + item);
	   
	   }
	    
	    if(this.inventory.containsKey(item.name) && item instanceof UtilityItem && game.currentLevel.itemsInWorld.contains(item)) {
	     
	    	UtilityItem inventoryItem = (UtilityItem) this.inventory.get(item.name);
	    	UtilityItem pickUpItem = (UtilityItem) item;
	    	
	        inventoryItem.useTimes += pickUpItem.amountOfUseTimes();
	        pickUpItem.isInWorld = false;
	        game.currentLevel.itemsInWorld.remove(pickUpItem);
	      
	        GameWindow.PlayerHUD.notificationArea.announce(this.name + " picked up " + item);
	      
	      
	    }
	    
	    
	    if(item instanceof Weapon) GameWindow.PlayerHUD.weaponHud.updateItems();
	    else GameWindow.PlayerHUD.equipmentBox.updateItems();
	    
	  }
	  
	   public void equipWeapon(Optional<Weapon> gun) {
	     
		 this.equippedWeapon = gun;
	     
	     if(gun.isPresent()){
	       gun.get().user = Optional.of(this);
	       gun.get().sprites.get(2).user = this;
	     }  
	   }
	   
	   public void equipUtilItem(UtilityItem thing) {
	     this.equippedUtilityItem = Optional.of(thing);
	   }
	  
	   public void useUtilItem() {
		   
		   if(this.equippedUtilityItem.isPresent()) this.equippedUtilityItem.get().use();
		   else GameWindow.PlayerHUD.notificationArea.announce("You don't have a utility item equipped");
		  
	   }
	     
	    
	    
	   public void drop(Item item) {
	    
	    if (this.inventory.values().contains(item) && !this.hasDroppedItem){
	      
	      this.hasDroppedItem = true;
	      item.isInWorld = true;
	      item.locationForSprite = item.locationForSprite();
	      this.inventory.remove(item.name);
	      item.locationInWorld = Optional.of(this.location);
	      game.currentLevel.itemsInWorld.add(item);

	    }
	   }
	   
}