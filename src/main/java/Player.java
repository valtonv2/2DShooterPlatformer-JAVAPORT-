package main.java;


import javafx.scene.shape.Rectangle;
import javafx.scene.Node;
import javafx.scene.image.*;
import javafx.scene.paint.ImagePattern;
import javafx.scene.transform.*;
import javafx.util.Pair;
import javafx.scene.paint.Color.*;
import javafx.scene.input.*;
import javafx.animation.*;
import javafx.event.*;
import java.math.*;
import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;
import java.io.File;
import javafx.scene.media.*;
import javafx.scene.Group;
import javafx.scene.Cursor;


abstract class Actor extends UsesGameSprite {
  
  String name;
  ArrayList<Collider> colliders;
  Game game;
  Boolean isWalking = false;
  Boolean isShielding = false;
  Boolean isOnLadder = false;
  Boolean hasDroppedItem = false;
  int height = 90;
  int width = 60;
  
  Optional<RotatingArm> arm;
  //Tavaraluettelo
  Map<String, Item> inventory;
  Optional<UtilityItem> equippedUtilityItem = Optional.empty();
  Optional<Weapon> equippedWeapon = Optional.empty()
  GamePos location;
  
  Optional<Pair<Double, Double>> locationForSprite = Optional.ofNullable(location.locationInImage());
  String lookDirectionForSprite;
  abstract void stop();
  abstract void takeDamage(int amount);
  ArrayList<Node> image;
  
  //Esineen poimiminen maailmasta
  /*
  public void pickUp(Item item, Boolean isCheatDrop) {
    
    
    if(isCheatDrop){
      this.inventory += (item.name, item);
      item.isInWorld = false;
    }
    
    if(!this.inventory.contains(item.name) && !this.hasDroppedItem && game.currentLevel.itemsInWorld.contains(item) && !isCheatDrop) {  // Tiettyä esinettä voi tavaraluettelossa olla vain 1. UtilityItemien käyttökertojen määrä kasvaa poimittaessa niitä lisää.
      
      this.inventory += (item.name -> item)
      item.isInWorld = false
      game.currentLevel.itemsInWorld -= item
      NotificationArea.announce(this.name + " picked up " + item)
   
   }
    
    if(this.inventory.contains(item.name) && item.isInstanceOf[UtilityItem] && game.currentLevel.itemsInWorld.contains(item)) {
      this.inventory(item.name).asInstanceOf[UtilityItem].amountOfUseTimes += item.asInstanceOf[UtilityItem].amountOfUseTimes
      item.isInWorld = false
      game.currentLevel.itemsInWorld -= item
      
      NotificationArea.announce(this.name + " picked up " + item)
      
      
    }
    
    
    println("Player inventory: " + this.inventory.keys.mkString(", "))
    if(item.isInstanceOf[Weapon]) PlayerHUD.weaponHud.updateItems
    else PlayerHUD.equipmentBox.updateItems
    
  }
  
   def equipWeapon(gun:Option[Weapon]) = {
     this.equippedWeapon = gun
     
     if(gun.isDefined){
       gun.get.user = Some(this)
       gun.get.sprites(2).user = this
     }
     
   }
   def equipUtilItem(thing:UtilityItem) = {
     this.equippedUtilityItem = Some(thing)
   }
   def useUtilItem = this.equippedUtilityItem match {
     
     case Some(item) => item.use
     case None => println("Tried to use item that did not exist")
     
   }
    
   def drop(item:Item) = {
    
    if (this.inventory.values.toArray.contains(item) && !this.hasDroppedItem){
      
      this.hasDroppedItem = true
      item.isInWorld = true
      this.inventory -= item.name
      item.locationInWorld = Some(this.location)
      game.currentLevel.itemsInWorld += item
    
      println(this.name + " dropped " + item.name)
    } else {
      
      println("Item specified did not exist in player inventory")
      
    }
    
  }*/
}