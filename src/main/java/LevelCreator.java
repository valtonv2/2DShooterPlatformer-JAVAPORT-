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
import java.awt.Event;
import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;

import javafx.event.EventHandler;
import javafx.scene.*;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.util.Pair;
import javafx.scene.text.Text;



public class LevelCreator {
	
	CreatorCursor cursor = new CreatorCursor();
	
	LevelArea levelArea = new LevelArea(20, 20);
	
	BlockMenu blockMenu = new BlockMenu();
	
	//The container for the level creator elements. Is given to GameWindow.
	public Group window = new Group();
	
	private Group area = new Group();
	
	public Scene scene = new Scene(window, 800, 800);
	
	public LevelCreator() {
		
		levelArea.refresh();
		area.getChildren().add(levelArea.areaImage());
		window.getChildren().add(area);
		window.getChildren().add(blockMenu.content);
		
		
		
		 EventHandler<KeyEvent> movementHandler = new EventHandler<KeyEvent>() {
			  	public void handle(KeyEvent event) {
			  		
			  		if(event.getCode() == KeyCode.W) area.setLayoutY(area.getLayoutY()-8);
			  		if(event.getCode() == KeyCode.S) area.setLayoutY(area.getLayoutY()+8);
			  		if(event.getCode() == KeyCode.A) area.setLayoutX(area.getLayoutX()-8);
			  		if(event.getCode() == KeyCode.D) area.setLayoutX(area.getLayoutX()+8);
			  		
			  		if(event.getCode() == KeyCode.UP) {
			  			area.setScaleX(area.getScaleX()+0.1);
			  			area.setScaleY(area.getScaleY()+0.1);
			  		}
			  		
			  		if(event.getCode() == KeyCode.DOWN) {
			  			area.setScaleX(area.getScaleX()-0.1);
			  			area.setScaleY(area.getScaleY()-0.1);
			  		}
			  		
			  		
			  		
			  		
			  	}
		  };
		  
		  
		  EventHandler<MouseEvent> mouseHandler = new EventHandler<MouseEvent>() {
			  	public void handle(MouseEvent event) {
			  		
			  		cursor.placeBlock();
			  	
			  	}
		  };
		  
		  
		  
		  
		  scene.addEventFilter(KeyEvent.KEY_PRESSED, movementHandler);
		  scene.addEventFilter(MouseEvent.MOUSE_CLICKED, mouseHandler);
		
		
	}
	
	public void refresh() {
		
		levelArea.refresh();
		blockMenu.refreshContent();
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
	
	public Optional<GridTile> selectedTile() {
		
		return Optional.ofNullable(this.area.stream().filter(tile -> tile.isSelected).collect(Collectors.toList()).get(0));
		
	}
	
}


class GridTile{
	
   GamePos location;
   Optional<GameTile> content = Optional.ofNullable(null);
   Boolean isSelected = false;
   
   Group selectedImage() {
	   
	   Optional<GameTile> cursorTile = GameWindow.levelCreator.cursor.heldBlock;
	   Rectangle normal = new Rectangle(50, 50, Color.DIMGRAY);
	   normal.setOpacity(0.5);
	   
	   if(cursorTile.isPresent()) {
		   
		   Rectangle cursorTileImage = cursorTile.get().tileImage;
		   cursorTileImage.setX(0.0);
		   cursorTileImage.setY(0.0);
		   
		   Group group = new Group();
		   group.getChildren().add(cursorTileImage);
		   group.getChildren().add(normal);
		   group.setLayoutX(this.location.locationInImage().getKey());
		   group.setLayoutY(this.location.locationInImage().getValue());

		   return group;
	   }else { 
		   Group group = new Group();
		   group.getChildren().add(normal);
		   group.setLayoutX(this.location.locationInImage().getKey());
		   group.setLayoutY(this.location.locationInImage().getValue());
		   return group; 
	   }
	   
   } 
   Rectangle idleImage = new Rectangle(50, 50, Color.GRAY);
   Rectangle sensor = new Rectangle(50, 50, Color.TRANSPARENT);
   
   public GridTile(GamePos location) {
	   
	   this.location = location;
	   
	   
	   this.idleImage.setX(this.location.locationInImage().getKey());
	   this.idleImage.setY(this.location.locationInImage().getValue());
	   this.idleImage.setOpacity(0.25);
	   
	   this.sensor.setX(this.location.locationInImage().getKey());
	   this.sensor.setY(this.location.locationInImage().getValue());

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
		   
		   if(this.isSelected) group.getChildren().add(selectedImage());   
		   else group.getChildren().add(idleImage);
		   
		   return group;
		   
	   }else {
		   Group group = new Group();
		  
		   if(this.isSelected) group.getChildren().add(selectedImage());   
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


class CreatorCursor{
	
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
	
	  private tile decTile = new tile(0.0, 0.0, false, false, decorativeTilePattern, 50.0, 50.0);
	  private tile floorTile = new tile(0.0, 0.0, true, false, floorPattern, 50.0, 50.0);
	  
	
	
	public Optional<GameTile> heldBlock = Optional.of(decTile); 
	
	
	
	public void switchBlock(GameTile tile) {
		
		this.heldBlock = Optional.of(tile);
		
	}

	
	public void placeBlock() {
		
		if(hoverLocation().isPresent() && heldBlock.isPresent()) {
			
			GameTile copy = heldBlock.get().copy();
			copy.location = hoverLocation().get().location;
			hoverLocation().get().addContent(copy);
		}
		else System.out.println("Could not place block");
		
	}
	
	
	public Optional<GridTile> hoverLocation() {
		
		return GameWindow.levelCreator.levelArea.selectedTile();
		
	}
	
	public void rotateRight() {
		
		if(this.heldBlock.isPresent()) this.heldBlock.get().rotate(90.0);
		else System.out.println("No block present for rotation");
		
	}
	
    public void rotateLeft() {
		
		if(this.heldBlock.isPresent()) this.heldBlock.get().rotate(-90.0);
		else System.out.println("No block present for rotation");
		
	}
	
	
}
	
	
class BlockMenu extends MovableMenu {
	
	private AnimatedButton decorativeTileButton = new AnimatedButton(
	         this,
	    	 "", //Button text
	    	  new Pair<Double, Double>(0.0, 0.0), //Offset from center
	    	  new Pair<Double, Double>(50.0, 50.0),  //Dimensions
	    	  "file:src/main/resources/Pictures/DecorativeTexture.png", //Normal image path
	    	  "file:src/main/resources/Pictures/DecorativeTexture.png", //Hover image path
	    	  "file:src/main/resources/Pictures/DecorativeTexture.png",//Pressed image path
	    	  Optional.empty(), //Announcement
	    	  new Runnable(){    //Action
	    	      
		    	  @Override
		    	  public void run() {
		    	  
		    		  System.out.println("Click");
	    	   
	    		  
		    	  }
	    		  
	    	    	   });
	
	public BlockMenu() {
		
		backGround.setFill(Color.DARKSLATEGREY);
		content.getChildren().add(backGround);
		buttons.add(decorativeTileButton);
		content.addEventFilter(MouseEvent.MOUSE_DRAGGED, dragHandler);
		
	}

	@Override
	public void refresh() {
		
		
	}
	

	
	
	
	
	
	
}
	
//Uusi parempi nappiluokka joka ei ole riippuvainen menusta

	class MultiPurposeButton{
		
		Pair<Double, Double> location = new Pair<Double, Double>(0.0,0.0);
		
		Pair<Double, Double> dimensions = new Pair<Double, Double>(25.0,25.0);
		
		Runnable action;
		
		Boolean isLocked = false;
		
		Boolean staysDown = false;
		
		Rectangle hitBox = new Rectangle(dimensions.getKey(), dimensions.getValue());
		Rectangle idleImage = new Rectangle(dimensions.getKey(), dimensions.getValue());
		Rectangle hoverImage = new Rectangle(dimensions.getKey(), dimensions.getValue());
		Rectangle pressedImage = new Rectangle(dimensions.getKey(), dimensions.getValue());
		Text text = new Text("X");
		
		//Simple constructor
		public MultiPurposeButton(String textForButton, Double locationX, Double locationY, Runnable action) {
			
			this.text = new Text(textForButton);
			this.location = new Pair<Double, Double>(locationX, locationY);
			
			this.idleImage.setFill(new ImagePattern( new javafx.scene.image.Image("file:src/main/resources/Pictures/GrayRectButtonNormal.png"), 1.0,1.0,1.0,1.0,true));
			this.hoverImage.setFill(new ImagePattern( new javafx.scene.image.Image("file:src/main/resources/Pictures/GrayRectButtonHover.png"), 1.0,1.0,1.0,1.0,true));
			this.pressedImage.setFill(new ImagePattern( new javafx.scene.image.Image("file:src/main/resources/Pictures/GrayRectButtonPressed.png"), 1.0,1.0,1.0,1.0,true));
			
			this.action = action;
			
			 EventHandler<MouseEvent> mouseClickHandler = new EventHandler<MouseEvent>() {
				  	public void handle(MouseEvent event) {
				  		
				  		try{
				  		
				  		   if(!lockStatus()){
				  			   
				  		    action.run();
			
				  		   }
				  		   
				  		 }catch(Exception e){
				  		     GameWindow.exceptionScreen(e);
				  		   }
				  	}
			  };
			  
			  hitBox.setOnMousePressed(mouseClickHandler);
		}
		
		
		
		Group image() {
				
			Group container = new Group();
			
			container.getChildren().add(idleImage);
			container.getChildren().add(text);
			container.getChildren().add(hitBox);
			
			container.setLayoutX(location.getKey());
			container.setLayoutY(location.getValue());
			
			return container;
				
		}
		
		void changeLocation(Pair<Double, Double> location) {
			
			this.location = location;
			
		}
		
	
	void lock(){this.isLocked = true;}
	
	void unlock(){this.isLocked = false;}
	
	Boolean lockStatus() {return this.isLocked;}
		
		
	}
