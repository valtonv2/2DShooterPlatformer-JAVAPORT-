package main.java;

import javafx.scene.shape.Rectangle;
import javafx.util.Pair;
import javafx.scene.image.*;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Color.*;
import javafx.scene.input.*;
import javafx.animation.*;
import javafx.event.*;


class MouseCursor{
	
  private Player player;
  
  private Double xCoordinate = 0.0;
  private Double yCoordinate = 0.0;
  public Boolean isOnLeft = false;
  public Boolean isOnRight = false;
  
  //Ladataan tähtäinkuva muistiin
  private Image cursorImage = new Image("file:src/main/resources/Pictures/Cursor.png");
  private ImagePattern pattern = new ImagePattern(cursorImage, 0,0,1,1,true);
  
  //Luodaan kuva
  public Rectangle image() {
	  Rectangle rect = new Rectangle(30, 30, xCoordinate-15, yCoordinate -15);
	  rect.setFill(pattern);
	  return rect;  
	  
  }
  
  

  private Double playerXDiff = this.xCoordinate - player.location.locationInImage.getkey();
  private Double playerYDiff = this.yCoordinate - player.location.locationInImage.getValue();
        
  public Pair<Double, Double> location = new Pair<Double, Double>(this.xCoordinate, this.yCoordinate);
  
  //Konstruktori luokalle
  
  public MouseCursor(Player player) {
	  
	  this.player = player;
	  
	  //Tapahtumankäsittelijä joka huolehtii kursorin seurannasta
	  
	  EventHandler<MouseEvent> followCursor = new EventHandler<MouseEvent>() {
		  
		  public void handle(MouseEvent event) {
			  
			  xCoordinate = event.getSceneX();
			  yCoordinate = event.getSceneY();
				      
		      Double dX = playerXDiff;
			  Double dY = playerYDiff;
				      
				 if(dX <= 0){
				    isOnLeft = true;
				    isOnRight = false;
				 }
				     
				 if(dX > 0){
				    isOnLeft = false;
				    isOnRight = true;
				 }
			  
		  }
		  
	  };
	  
	  this.player.game.fullImage().addEventFilter(MouseEvent.MOUSE_MOVED, followCursor);
	  
  }
  
}