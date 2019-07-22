/* This file contains the code for this game's level creator.
 * 
 * The level creator shows different menus, the level area and the creator cursor.
 * 
 * Menus:
 * On the right side corner of the window, there is a selection of blocks. The blocks are clickable.
 * When a block is clicked, the creator cursor shows a translucent image of the block. The menu is hidable.
 * 
 * Level Area: 
 * The level area is a grid on which blocks can be placed. The area is scalable and movable.
 * The background shows the background image of the level.
 * 
 * Creator Cursor:
 * The player's mouse cursor. Shows a cross and the translucent image of the selected block.
 * On left mouse click a block is placed. the cursor allows for 90 degree rotations of the held block.
 * On right mouse click, the block is removed.
 * 
 * Ability to save created levels:
 * Block placements are saved in text format. One block could look like this ;TYPE:X:Y:COILLISION;
 */
package main.java;
import java.util.ArrayList;
import java.util.Optional;

import javafx.event.EventHandler;
import javafx.scene.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Pair;


public class LevelCreator {
	
	LevelArea levelArea = new LevelArea(20, 20);
	
	//The container for the level creator elements. Is given to GameWindow.
	public Group window = new Group();
	
	public Scene scene = new Scene(window, 800, 800);
	
	public LevelCreator() {
		
		levelArea.refresh();
		window.getChildren().add(levelArea.areaImage());
		
		 EventHandler<KeyEvent> movementHandler = new EventHandler<KeyEvent>() {
			  	public void handle(KeyEvent event) {
			  		
			  		if(event.getCode() == KeyCode.W) window.setLayoutY(window.getLayoutY()-8);
			  		if(event.getCode() == KeyCode.S) window.setLayoutY(window.getLayoutY()+8);
			  		if(event.getCode() == KeyCode.A) window.setLayoutX(window.getLayoutX()-8);
			  		if(event.getCode() == KeyCode.D) window.setLayoutX(window.getLayoutX()+8);
			  		
			  		if(event.getCode() == KeyCode.UP) {
			  			window.setScaleX(window.getScaleX()+0.1);
			  			window.setScaleY(window.getScaleY()+0.1);
			  		}
			  		
			  		if(event.getCode() == KeyCode.DOWN) {
			  			window.setScaleX(window.getScaleX()-0.1);
			  			window.setScaleY(window.getScaleY()-0.1);
			  		}
			  		
			  		
			  		
			  		
			  	}
		  };
		  
		  scene.addEventFilter(KeyEvent.KEY_PRESSED, movementHandler);
		
		
	}
	
	public void refresh() {
		
		levelArea.refresh();
		//window.getChildren().clear();
		//window.getChildren().add(levelArea.areaImage);
		System.out.println("Refreshing");
	}
	
}


class LevelArea {
	
	int xBlocks; //Width of area in blocks
	int yBlocks; //Height of area in blocks
	
	ArrayList<GridTile> area = new ArrayList<GridTile>();
	Group tileImages = new Group();
	Group tileSensors = new Group();
	
	
	public LevelArea(int xBlocks, int yBlocks) {
		
		this.xBlocks = xBlocks;
		this.yBlocks = yBlocks;
		
		for(int y = 0; y<yBlocks; y++) {
			
			for(int x = 0; x<xBlocks; x++) {
				
				this.area.add(new GridTile(new GamePos(new Pair<Double, Double>(x*50.0, y*50.0), false)));
				System.out.println("(" + x*50 + " ; " + y*50 + ")");
			}
		}
		area.stream().forEach(tile -> tileSensors.getChildren().add(tile.sensor));
		
	}
	
	
	public void refresh() {
		tileImages.getChildren().clear();
		area.stream().forEach(tile -> tileImages.getChildren().add(tile.image()));
	}
	
	public Group areaImage() {
		
		Group group = new Group();
		group.getChildren().add(tileImages);
		group.getChildren().add(tileSensors);
		return group;
		
		
	}
	
}


class GridTile{
	
   GamePos location;
   Optional<GameTile> content = Optional.ofNullable(null);
   Boolean isSelected = false;
   
   Rectangle selectedImage = new Rectangle(50, 50, Color.BLACK);
   Rectangle idleImage = new Rectangle(50, 50, Color.ORANGERED);
   Rectangle sensor = new Rectangle(50, 50, Color.TRANSPARENT);
   
   public GridTile(GamePos location) {
	   
	   this.location = location;
	   
	   this.selectedImage.setX(this.location.locationInGame().getKey());
	   this.selectedImage.setY(this.location.locationInGame().getValue());
	   
	   this.idleImage.setX(this.location.locationInGame().getKey());
	   this.idleImage.setY(this.location.locationInGame().getValue());
	   
	   this.sensor.setX(this.location.locationInGame().getKey());
	   this.sensor.setY(this.location.locationInGame().getValue());

	   EventHandler<MouseEvent> mouseEnterHandler = new EventHandler<MouseEvent>() {
		  	public void handle(MouseEvent event) {
		  		isSelected = true;
		  	}
	  };
	  
	   EventHandler<MouseEvent> mouseExitHandler = new EventHandler<MouseEvent>() {
		  	public void handle(MouseEvent event) {
		  		isSelected = false;
		  	}
	  };
	  
	  sensor.addEventFilter(MouseEvent.MOUSE_ENTERED, mouseEnterHandler);
	  sensor.addEventFilter(MouseEvent.MOUSE_EXITED, mouseExitHandler);

   }
   
   public Group image() {
	   
	   if(this.content.isPresent()) {
		   Group group = new Group();
		   group.getChildren().add(content.get().image());
		   
		   if(this.isSelected) group.getChildren().add(selectedImage);   
		   else group.getChildren().add(idleImage);
		   
		   return group;
		   
	   }else {
		   Group group = new Group();
		  
		   if(this.isSelected) group.getChildren().add(selectedImage);   
		   else group.getChildren().add(idleImage);
		   
		   return group;
		   
	   } 
   }
   
   
   public void addContent(GameTile content) {
	   this.content = Optional.of(content);
   }
   
   public void removeContent() {
	   this.content = Optional.empty();
   }
   
	
}