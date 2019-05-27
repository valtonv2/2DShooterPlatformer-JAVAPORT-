package main.java;

import javafx.scene.shape.Rectangle;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.text.Text;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

import javafx.scene.input.*;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.paint.Color.*;
import javafx.scene.media.AudioClip;
import javafx.scene.Group;
import javafx.scene.control.*;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import java.util.concurrent.Callable;



class Menus{
	
  static GameMenu MainMenu = new MainMenu();	
  static SettingsMenu SettingsMenu = new SettingsMenu();
  static GameMenu LevelSelectMenu = new LevelSelectMenu();
  static GameMenu LoadMenu = new LoadMenu();
  static GameMenu SaveMenu = new SaveMenu();
  static GameMenu Deathmenu = new DeathMenu();
  static GameMenu PauseMenu = new PauseMenu();
	
	
  static GameMenu currentMenu = MainMenu;  //Tämän perusteella GUI osaa kutsua oikean menun refresh-metodia
  static Boolean  fullScreenStatus = GameWindow.stage.isFullScreen();
  
}
  

  class MainMenu extends GameMenu {
	    
	    String name = "Main Menu";
	    private Text header = new Text(0,0,"");
	    private Text annotationText = new Text(0,0,"");
	    private Rectangle gameLogo = Helper.anySpriteFromImage("file:src/main/resources/Pictures/GameLogo.png", new Pair<Double, Double>(0.0,0.0), 600.0, 250.0);
	   
	        
	    private GameButton playButton = new AnimatedButton(
	      "Play Game", //Button text
	       new Pair<Double, Double>(0.0, -100.0), //Offset from center
	       new Pair<Double, Double>(200.0, 50.0),  //Dimensions
	       "file:src/main/resources/Pictures/GrayRectButtonNormal.png", //Normal image path
	       "file:src/main/resources/Pictures/GrayRectButtonHover.png", //Hover image path
	       "file:src/main/resources/Pictures/GrayRectButtonpressed.png",//Pressed image path
	       Optional.empty(), //Announcement
	       Callable<Void>actionjaa = () ->{    //Action
	      
	    	   GameWindow.menuClock.stop();
	           GameWindow.clock.start();
	           GameWindow.currentGame.fullImage.setCursor(Cursor.NONE);
	           if(!Menus.fullScreenStatus) { GameWindow.stage.setScene(GameWindow.currentGame.fullImage);}
	           else{GameWindow.stage.setScene(GameWindow.currentGame.fullImage); 
	           GameWindow.stage.setFullScreen(true);
	           }
	            
	        });
	    
	   
	    private GameButton settingsButton = new AnimatedButton(
	       "Settings", //Button text
	       new Pair<Double, Double>(0.0, -25.0), //Offset from center
	       new Pair<Double, Double>(200.0, 50.0),  //Dimensions
	       "file:src/main/resources/Pictures/GrayRectButtonNormal.png", //Normal image path
	       "file:src/main/resources/Pictures/GrayRectButtonHover.png", //Hover image path
	       "file:src/main/resources/Pictures/GrayRectButtonpressed.png",//Pressed image path
	       Optional.empty(), //Announcement
	       Callable<Void>action0 = () ->{    //Action
	    	  
	    	   if(!Menus.fullScreenStatus) { GameWindow.stage.setScene(Menus.SettingsMenu.scene);}
	    	   else{GameWindow.stage.setScene(Menus.SettingsMenu.scene); 
	    	        GameWindow.stage.setFullScreen(true); }
	    		       
	    	   Menus.currentMenu = Menus.SettingsMenu;
	    	   Menus.SettingsMenu.arrivedFrom = this;
	    	   
	    	        });
	    
	    
	    private GameButton levelSelectButton = new AnimatedButton(
	       "Select Level", //Button text
	       new Pair<Double, Double>(0.0, 50.0), //Offset from center
	       new Pair<Double, Double>(200.0, 50.0),  //Dimensions
	       "file:src/main/resources/Pictures/GrayRectButtonNormal.png", //Normal image path
	       "file:src/main/resources/Pictures/GrayRectButtonHover.png", //Hover image path
	       "file:src/main/resources/Pictures/GrayRectButtonpressed.png",//Pressed image path
	       Optional.empty(), //Announcement
	       Callable<Void>action1 = () ->{    //Action
	    	   
	    	   if(!Menus.fullScreenStatus) { GameWindow.stage.setScene(Menus.LevelSelectMenu.scene);}
	    	   else{
	    	     GameWindow.stage.setScene(Menus.LevelSelectMenu.scene); 
	    	     GameWindow.stage.setFullScreen(true);
	    	     }
	    	   Menus.currentMenu = Menus.LevelSelectMenu;
	    	   
	    	    	  });
	    	    
	    
	    
	    private GameButton loadButton = new AnimatedButton(
	       "Load Game", //Button text
	        new Pair<Double, Double>(0.0, 125.0), //Offset from center
	        new Pair<Double, Double>(200.0, 50.0),  //Dimensions
	    	"file:src/main/resources/Pictures/GrayRectButtonNormal.png", //Normal image path
	    	"file:src/main/resources/Pictures/GrayRectButtonHover.png", //Hover image path
	    	"file:src/main/resources/Pictures/GrayRectButtonpressed.png",//Pressed image path
	    	Optional.empty(), //Announcement
	    	Callable<Void>action2 = () ->{    //Action
	    	      
	    		
	    		if(!Menus.fullScreenStatus) { GameWindow.stage.setScene(Menus.LoadMenu.scene);}
		    	   else{
		    	     GameWindow.stage.setScene(Menus.LoadMenu.scene); 
		    	     GameWindow.stage.setFullScreen(true);
		    	     }
		    	   Menus.currentMenu = Menus.LoadMenu;
	    		
	    		
	    	    	   });
	    
	    private GameButton saveButton = new AnimatedButton(
	 	    "Save Game", //Button text
	 	     new Pair<Double, Double>(0.0, 200.0), //Offset from center
	 	     new Pair<Double, Double>(200.0, 50.0),  //Dimensions
	 	     "file:src/main/resources/Pictures/GrayRectButtonNormal.png", //Normal image path
	 	     "file:src/main/resources/Pictures/GrayRectButtonHover.png", //Hover image path
	 	     "file:src/main/resources/Pictures/GrayRectButtonpressed.png",//Pressed image path
	 	     Optional.empty(), //Announcement
	 	     Callable<Void>action3 = () ->{    //Action
	 	    	      
	 	    		
	 	   	if(!Menus.fullScreenStatus) { GameWindow.stage.setScene(Menus.SaveMenu.scene);}
	 	    else{
	 		     GameWindow.stage.setScene(Menus.SaveMenu.scene); 
	 		     GameWindow.stage.setFullScreen(true);
	 		    }
	 		Menus.currentMenu = Menus.SaveMenu;
	 	    		
	 	    		
	 	    	        });
	    	    
	    
	    private GameButton exitButton = new AnimatedButton(
		 	 "Exit Game", //Button text
		 	 new Pair<Double, Double>(0.0, 275.0), //Offset from center
		 	 new Pair<Double, Double>(200.0, 50.0),  //Dimensions
		 	 "file:src/main/resources/Pictures/GrayRectButtonNormal.png", //Normal image path
		 	 "file:src/main/resources/Pictures/GrayRectButtonHover.png", //Hover image path
		 	 "file:src/main/resources/Pictures/GrayRectButtonpressed.png",//Pressed image path
		 	 Optional.empty(), //Announcement
		 	 Callable<Void>action4 = () ->{    //Action
		 	    	      
		 		GameWindow.stage.close();
		        GameWindow.stopApp();
		 	   
		 	    	        });
	    

	    	    	    
	    
	    private ArrayList<GameButton> buttons = new ArrayList(); 
	    private Rectangle backGround = Helper.anySpriteFromImage("file:src/main/resources/Pictures/MainMenuBackGround.png", new Pair<Double, Double>(0.0,0.0), 2783.0 ,2484.0 );
	    public Optional<AudioClip> theme = Optional.empty();
	    private Group content = new Group();
	    public Scene scene = new Scene(content, 800.0, 800.0);
	    
	    
	    
	    public void refresh() {
	      
	      if(buttons.isEmpty()) {
	    	  buttons.add(playButton); 
	    	  buttons.add(levelSelectButton); 
	          buttons.add(settingsButton); 
	          buttons.add(loadButton); 
	          buttons.add(saveButton); 
	    	  buttons.add(exitButton);
	    	  } 
	      
	      if(content.getChildren().isEmpty()) {
	    	  content.getChildren().addAll(backGround, header, annotationText); 
	    	  buttons.stream().forEach(button -> content.getChildren().add(button.fullImage()));
	          
	      }
	     
	      
	      backGround.setHeight(GameWindow.stage.getHeight());
	      backGround.setWidth(GameWindow.stage.getWidth());
	      header.setX(GameWindow.stage.getWidth()/2 + 100);
	      header.setY(GameWindow.stage.getHeight()/2 - 250);
	      annotationText.setX(GameWindow.stage.getWidth()/2 - 50);
	      annotationText.setY(GameWindow.stage.getHeight()/2 + 250);
	      header.setScaleX(3);
	      header.setScaleY(3);
	      header.setFill(Color.WHITE); 
	      
	      annotationText.setScaleX(3); 
	      annotationText.setScaleY(3);
	      annotationText.setFill(Color.PURPLE);
	      
	      buttons.stream().forEach(button -> button.refreshLocation());
	      
	      if (buttons.stream().anyMatch(button -> button.buttonAnnouncement().isPresent())) { this.annotationText.setText(buttons.stream().filter(button -> button.buttonAnnouncement().isPresent()).findAny().get().buttonAnnouncement().get());}
	      else {this.annotationText.setText("");
	    }
	   }       
	}

 

 //----------------------------------------------------------------------------------------------------------------------------------------------------------------------
    
 class PauseMenu extends GameMenu {
	    
	    String name = "Pause Menu";
	    private Text header = new Text(0,0,"Game Paused");
	   
	    private Text annotationText = new Text(0,0,"");
	   
	        
	    private GameButton resumeButton = new AnimatedButton(
	      "", //Button text
	       new Pair<Double, Double>(-200.0, 0.0), //Offset from center
	       new Pair<Double, Double>(150.0, 150.0),  //Dimensions
	       "file:src/main/resources/Pictures/SquarePlayButton.png", //Normal image path
	       "file:src/main/resources/Pictures/SquarePlayButton.png", //Hover image path
	       "file:src/main/resources/Pictures/SquarePlayButton.png",//Pressed image path
	       Optional.of("Resume game"), //Announcement
	       Callable<Void>action1 = () ->{    //Action
	    	   GameWindow.clock.start();
	           GameWindow.menuClock.stop();
	           if(!Menus.fullScreenStatus) {
	        	   GameWindow.stage.setScene(GameWindow.currentGame.fullImage); 
	           }else{
	        	   GameWindow.stage.setScene(GameWindow.currentGame.fullImage); 
	           GameWindow.stage.setFullScreen(true); 
	           }
	        });
	    
	   
	    private GameButton settingsButton = new AnimatedButton(
	       "", //Button text
	       new Pair<Double, Double>(0.0, 0.0), //Offset from center
	       new Pair<Double, Double>(150.0, 150.0),  //Dimensions
	       "file:src/main/resources/Pictures/SquareSettingButton.png", //Normal image path
	       "file:src/main/resources/Pictures/SquareSettingButton.png", //Hover image path
	       "file:src/main/resources/Pictures/SquareSettingButton.png",//Pressed image path
	       Optional.of("Game Settings"), //Announcement
	       Callable<Void>action2 = () ->{    //Action
	    	   GameWindow.clock.stop();
	           GameWindow.menuClock.start();
	           if(!Menus.fullScreenStatus) {
	        	   GameWindow.stage.setScene(Menus.SettingsMenu.scene);
	           }else{
	        	   GameWindow.stage.setScene(Menus.SettingsMenu.scene);
	        	   GameWindow.stage.setFullScreen(true);
	        	   }
	           Menus.currentMenu = Menus.SettingsMenu;
	           Menus.SettingsMenu.arrivedFrom = this;
	    	        });
	    
	    
	    private GameButton exitButton = new AnimatedButton(
	    	 "", //Button text
	    	  new Pair<Double, Double>(200.0, 0.0), //Offset from center
	    	  new Pair<Double, Double>(150.0, 150.0),  //Dimensions
	    	  "file:src/main/resources/Pictures/SquareExitButton.png", //Normal image path
	    	  "file:src/main/resources/Pictures/SquareExitButton.png", //Hover image path
	    	  "file:src/main/resources/Pictures/SquareExitButton.png",//Pressed image path
	    	  Optional.of("Return to Main Menu"), //Announcement
	    	  Callable<Void>exit = () ->{    //Action
	    	   
	    		  if(!Menus.fullScreenStatus) { GameWindow.stage.setScene(Menus.MainMenu.scene); }
	    		  else{
	    			  GameWindow.stage.setScene(Menus.MainMenu.scene); 
	    			  GameWindow.stage.setFullScreen(true);
	    			  }
	    		
	    		 Menus.currentMenu = Menus.MainMenu;
	    		  
	    		  
	    	    	   });
	    	    	    
	    
	    private ArrayList<GameButton> buttons = new ArrayList(); 
	    private Rectangle backGround = Helper.anySpriteFromImage("file:src/main/resources/Pictures/MenuBackGround.png", new Pair<Double, Double>(0.0,0.0), 800.0 ,800.0 );
	    public Optional<AudioClip> theme = Optional.empty();
	    private Group content = new Group();
	    public Scene scene = new Scene(content, 800.0, 800.0);
	    
	    
	    
	    public void refresh() {
	      
	      if(buttons.isEmpty()) {
	    	  buttons.add(resumeButton); 
	    	  buttons.add(settingsButton); 
	    	  buttons.add(exitButton);
	    	  } 
	      
	      if(content.getChildren().isEmpty()) {
	    	  content.getChildren().addAll(backGround, header, annotationText); 
	    	  buttons.stream().forEach(button -> content.getChildren().add(button.fullImage()));
	          
	      }
	     
	      
	      backGround.setHeight(GameWindow.stage.getHeight());
	      backGround.setWidth(GameWindow.stage.getWidth());
	      header.setX(GameWindow.stage.getWidth()/2 + 100);
	      header.setY(GameWindow.stage.getHeight()/2 - 250);
	      annotationText.setX(GameWindow.stage.getWidth()/2 - 50);
	      annotationText.setY(GameWindow.stage.getHeight()/2 + 250);
	      header.setScaleX(3);
	      header.setScaleY(3);
	      header.setFill(Color.WHITE); 
	      
	      annotationText.setScaleX(3); 
	      annotationText.setScaleY(3);
	      annotationText.setFill(Color.PURPLE);
	      
	      buttons.stream().forEach(button -> button.refreshLocation());
	      
	      if (buttons.stream().anyMatch(button -> button.buttonAnnouncement().isPresent())) { this.annotationText.setText(buttons.stream().filter(button -> button.buttonAnnouncement().isPresent()).findAny().get().buttonAnnouncement().get());}
	      else {this.annotationText.setText("");
	    }
	   }       
	}

    
    
    
    
    
//------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------    
  
  class DeathMenu extends GameMenu {
	    
	    String name = "Death Menu";
	    private Text header = new Text(0,0,"You Died");
	   
	    private Text annotationText = new Text(0,0,"");
	   
	        
	    private GameButton restartButton = new AnimatedButton(
	      "", //Button text
	       new Pair<Double, Double>(-300.0, -150.0), //Offset from center
	       new Pair<Double, Double>(150.0, 150.0),  //Dimensions
	       "file:src/main/resources/Pictures/SquarePlayButton.png", //Normal image path
	       "file:src/main/resources/Pictures/SquarePlayButton.png", //Hover image path
	       "file:src/main/resources/Pictures/SquarePlayButton.png",//Pressed image path
	       Optional.of("Restart Game"), //Announcement
	       Callable<Void>action1 = () ->{    //Action
	       
	           GameWindow.menuClock.stop();
	           GameWindow.clock.start();
	           GameWindow.currentGame.reset();
	    
	           PlayerHUD.weaponHud.weaponBoxes.stream().forEach(box -> box.removeItem());
	           PlayerHUD.equipmentBox.box.removeItem();
	           PlayerHUD.equipmentBox.updateItems();
	           if(!Menus.fullScreenStatus) { GameWindow.stage.setScene(GameWindow.currentGame.fullImage); }
	           else{GameWindow.stage.setScene(GameWindow.currentGame.fullImage); 
	                GameWindow.stage.setFullScreen(true); 
	                }
	    	   
	    	   
	        });
	        
	    
	    private GameButton exitButton = new AnimatedButton(
	    	 "", //Button text
	    	  new Pair<Double, Double>(300.0, 150.0), //Offset from center
	    	  new Pair<Double, Double>(150.0, 150.0),  //Dimensions
	    	  "file:src/main/resources/Pictures/SquareExitButton.png", //Normal image path
	    	  "file:src/main/resources/Pictures/SquareExitButton.png", //Hover image path
	    	  "file:src/main/resources/Pictures/SquareExitButton.png",//Pressed image path
	    	  Optional.of("Return to Main Menu"), //Announcement
	    	  Callable<Void>exit = () ->{    //Action
	    	   
	    		    GameWindow.clock.stop();
	    	        GameWindow.menuClock.start();
	    	        GameWindow.currentGame.reset();
	    	        if(!Menus.fullScreenStatus) {
	    	        	GameWindow.stage.setScene(Menus.MainMenu.scene);
	    	        }else{
	    	        	GameWindow.stage.setScene(Menus.MainMenu.scene); 
	    	        	GameWindow.stage.setFullScreen(true);
	    	        	}
	    	        PlayerHUD.weaponHud.weaponBoxes.stream().forEach(box -> box.removeItem());
	    	        PlayerHUD.equipmentBox.box.removeItem();
	    	        PlayerHUD.equipmentBox.updateItems();
	    	        Menus.currentMenu = Menus.MainMenu;
	    		  
	    		  
	    	    	   });
	    	    	    
	    
	    private ArrayList<GameButton> buttons = new ArrayList(); 
	    private Rectangle backGround = Helper.anySpriteFromImage("file:src/main/resources/Pictures/DeathMenuBackGround.png", new Pair<Double, Double>(0.0,0.0), 800.0 ,800.0 );
	    public Optional<AudioClip> theme = Optional.of(new AudioClip("file:src/main/resources/sound/DWADeathMenuTheme.wav"));
	    private Group content = new Group();
	    public Scene scene = new Scene(content, 800.0, 800.0);
	    
	    
	    
	    public void refresh() {
	      
	      if(buttons.isEmpty()) {
	    	  buttons.add(restartButton);  
	    	  buttons.add(exitButton);
	    	  } 
	      
	      if(content.getChildren().isEmpty()) {
	    	  content.getChildren().addAll(backGround, header, annotationText); 
	    	  buttons.stream().forEach(button -> content.getChildren().add(button.fullImage()));
	          
	      }
	     
	      
	      backGround.setHeight(GameWindow.stage.getHeight());
	      backGround.setWidth(GameWindow.stage.getWidth());
	      header.setX(GameWindow.stage.getWidth()/2 + 100);
	      header.setY(GameWindow.stage.getHeight()/2 - 250);
	      annotationText.setX(GameWindow.stage.getWidth()/2 - 50);
	      annotationText.setY(GameWindow.stage.getHeight()/2 + 250);
	      header.setScaleX(3);
	      header.setScaleY(3);
	      header.setFill(Color.WHITE); 
	      
	      annotationText.setScaleX(3); 
	      annotationText.setScaleY(3);
	      annotationText.setFill(Color.PURPLE);
	      
	      buttons.stream().forEach(button -> button.refreshLocation());
	      
	      if (buttons.stream().anyMatch(button -> button.buttonAnnouncement().isPresent())) { this.annotationText.setText(buttons.stream().filter(button -> button.buttonAnnouncement().isPresent()).findAny().get().buttonAnnouncement().get());}
	      else {this.annotationText.setText("");
	    }
	   }       
	}

  
  
  
 //==================================================================================================================================================================================================== 
   
   class LevelSelectMenu extends GameMenu {
	    
	    String name = "Level Selection";
	    private Text header = new Text(0,0,"Level Selection");
	   
	    private Text annotationText = new Text(0,0,"");
	   
	        
	    private GameButton level1Button = new AnimatedButton(
	      "Level 1", //Button text
	       new Pair<Double, Double>(30.0, -245.0), //Offset from center
	       new Pair<Double, Double>(140.0, 95.0),  //Dimensions
	       "file:src/main/resources/Pictures/GrayRectButtonNormal.png", //Normal image path
	       "file:src/main/resources/Pictures/GrayRectButtonHover.png", //Hover image path
	       "file:src/main/resources/Pictures/GrayRectButtonpressed.png",//Pressed image path
	       Optional.of("large City"), //Announcement
	       Callable<Void>action1 = () ->{    //Action
	       
	    	   GameWindow.menuClock.stop();
	           GameWindow.clock.start();
	           GameWindow.currentGame.swapLevel(1);
	           PlayerHUD.weaponHud.weaponBoxes.stream().forEach(item -> item.removeItem());
	           PlayerHUD.equipmentBox.box.removeItem();
	           PlayerHUD.equipmentBox.updateItems();
	           if(!Menus.fullScreenStatus) {GameWindow.stage.setScene(GameWindow.currentGame.fullImage);}
	           else{GameWindow.stage.setScene(GameWindow.currentGame.fullImage); 
	                GameWindow.stage.setFullScreen(true); }
	           }
	        });
	    
	    private GameButton level2Button = new AnimatedButton(
	  	      "Level 2", //Button text
	  	       new Pair<Double, Double>(30.0, -100.0), //Offset from center
	  	       new Pair<Double, Double>(140.0, 95.0),  //Dimensions
	  	       "file:src/main/resources/Pictures/GrayRectButtonNormal.png", //Normal image path
	  	       "file:src/main/resources/Pictures/GrayRectButtonHover.png", //Hover image path
	  	       "file:src/main/resources/Pictures/GrayRectButtonpressed.png",//Pressed image path
	  	       Optional.of("large City"), //Announcement
	  	       Callable<Void>action2 = () ->{    //Action
	  	       
	  	    	   GameWindow.menuClock.stop();
	  	           GameWindow.clock.start();
	  	           GameWindow.currentGame.swapLevel(2);
	  	           PlayerHUD.weaponHud.weaponBoxes.stream().forEach(item -> item.removeItem());
	  	           PlayerHUD.equipmentBox.box.removeItem();
	  	           PlayerHUD.equipmentBox.updateItems();
	  	           if(!Menus.fullScreenStatus) {GameWindow.stage.setScene(GameWindow.currentGame.fullImage);}
	  	           else{GameWindow.stage.setScene(GameWindow.currentGame.fullImage); 
	  	                GameWindow.stage.setFullScreen(true); }
	  	           }
	  	        });
	    
	   
	    
	    private GameButton exitButton = new AnimatedButton(
	    	 "", //Button text
	    	  new Pair<Double, Double>(300.0, 150.0), //Offset from center
	    	  new Pair<Double, Double>(150.0, 150.0),  //Dimensions
	    	  "file:src/main/resources/Pictures/SquareExitButton.png", //Normal image path
	    	  "file:src/main/resources/Pictures/SquareExitButton.png", //Hover image path
	    	  "file:src/main/resources/Pictures/SquareExitButton.png",//Pressed image path
	    	  Optional.of("Return to Main Menu"), //Announcement
	    	  Callable<Void>exit = () ->{    //Action
	    	   
	    		  if(!Menus.fullScreenStatus) { GameWindow.stage.setScene(Menus.MainMenu.scene); }
	    		  else{
	    			  GameWindow.stage.setScene(Menus.MainMenu.scene); 
	    			  GameWindow.stage.setFullScreen(true);
	    			  }
	    		
	    		 Menus.currentMenu = Menus.MainMenu;
	    		  
	    	    	   });
	    
	    private Group scrollPart = new Group();
	    
	    EventHandler<ScrollEvent> scrollHandler = new EventHandler<ScrollEvent>() {
		  	public void handle(ScrollEvent event) {
		  		
		  		if(event.getDeltaY()>0 && scrollPart.getLayoutY() < 0){
		    	        scrollPart.setLayoutY(scrollPart.getLayoutY + 5);
		    	        lv1Button.changeOffset(0, 5);
		    	        lv2Button.changeOffset(0,5);
		    	      
		  		}else if(event.getDeltaY()<0 && scrollPart.getLayoutY() > GameWindow.stage.height() - 2500){
		    	        scrollPart.setLayoutY(scrollPart.layoutY - 5);
		    	        lv1Button.changeOffset(0, -5);
		    	        lv2Button.changeOffset(0, -5);
		    	      }

		  		
		  	}
	  };
	    
	    	    	    
	    
	    private ArrayList<GameButton> buttons = new ArrayList(); 
	    private Rectangle backGround = Helper.anySpriteFromImage("file:src/main/resources/Pictures/Lastembers.png", new Pair<Double, Double>(0.0,0.0), 800.0 ,800.0 );
	    private Rectangle scrollPartImg = Helper.anySpriteFromImage("file:src/main/resources/Pictures/LevelMenuScrollPart.png", new Pair<Double, Double>(0.0,0.0), 800.0, 2500.0);
	    public Optional<AudioClip> theme = Optional.empty();
	    private Group content = new Group();
	    public Scene scene = new Scene(content, 800.0, 800.0);
	    
	    
	    
	    public void refresh() {
	      
	      if(buttons.isEmpty()) {
	    	  buttons.add(level1Button); 
	    	  buttons.add(level2Button); 
	    	  buttons.add(exitButton);
	    	  } 
	      
	      if(content.getChildren().isEmpty()) {
	    	  content.getChildren().addAll(backGround, header, annotationText); 
	    	  buttons.stream().forEach(button -> content.getChildren().add(button.fullImage()));
	          
	      }
	      
	      if(scrollPart.getChildren().isEmpty()) {
	    	  scrollPart.getChildren().add(scrollPartImg);
	    	  }
	      
	      scrollPart.setLayoutX(GameWindow.stage.getWidth()/2 - 400);
	     
	      scrollPart.setOnScroll(scrollHandler);
	      
	      backGround.setHeight(GameWindow.stage.getHeight());
	      backGround.setWidth(GameWindow.stage.getWidth());
	      header.setX(GameWindow.stage.getWidth()/2 + 100);
	      header.setY(GameWindow.stage.getHeight()/2 - 250);
	      annotationText.setX(GameWindow.stage.getWidth()/2 - 50);
	      annotationText.setY(GameWindow.stage.getHeight()/2 + 250);
	      header.setScaleX(3);
	      header.setScaleY(3);
	      header.setFill(Color.WHITE); 
	      
	      annotationText.setScaleX(3); 
	      annotationText.setScaleY(3);
	      annotationText.setFill(Color.PURPLE);
	      
	      buttons.stream().forEach(button -> button.refreshLocation());
	      
	      if(GameWindow.currentGame.levelCompletionStatus.get(0) == false && !this.lv2Button.isLocked){
	          this.lv2Button.lock();

	        }
	      else if(GameWindow.currentGame.levelCompletionStatus(0) == true && this.lv2Button.isLocked){
	          this.lv2Button.unlock();
	         
	        }
	      
	      if (buttons.stream().anyMatch(button -> button.buttonAnnouncement().isPresent())) { this.annotationText.setText(buttons.stream().filter(button -> button.buttonAnnouncement().isPresent()).findAny().get().buttonAnnouncement().get());}
	      else {this.annotationText.setText("");
	    }
	   }       
	}

   
   

   
   
   class SettingsMenu extends GameMenu {
	    
	    String name = "Settings Menu";
	    private Text header = new Text(0,0,"Game Settings");
	    private Text annotationText = new Text(0,0,"");
	   
	    public GameMenu arrivedFrom = Menus.MainMenu;
	  
	    //Asetusmenun ohjainelementit
	    public GameSlider volumeSlider = new GameSlider("Volume",0.0,100.0,100.0, new Pair<Double, Double>(700.0, 10.0), new Pair<Double, Double>(-350.0, -200.0));
	    public GameCheckBox muteCheckBox = new GameCheckBox("Mute sound", new Pair<Double, Double>(-150.0, 50.0));
	    public GameCheckBox devModeCheckBox = new GameCheckBox("Dev Mode", new Pair<Double, Double>(-150.0, 130.0));
	    	    
	    private Rectangle checkBoxBackground = Helper.anySpriteFromImage("file:src/main/resources/Pictures/CBbackground.png", new Pair<Double, Double>(0.0,0.0), 400.0 ,1000.0 );
	    private GameButton exitButton = new AnimatedButton(
	    	 "", //Button text
	    	  new Pair<Double, Double>(300.0, 150.0), //Offset from center
	    	  new Pair<Double, Double>(150.0, 150.0),  //Dimensions
	    	  "file:src/main/resources/Pictures/SquareExitButton.png", //Normal image path
	    	  "file:src/main/resources/Pictures/SquareExitButton.png", //Hover image path
	    	  "file:src/main/resources/Pictures/SquareExitButton.png",//Pressed image path
	    	  Optional.of("Return to Main Menu"), //Announcement
	    	  Callable<Void>exit = () ->{    //Action
	    	   
	    		  if(!Menus.fullScreenStatus) { GameWindow.stage.setScene(arrivedFrom.scene); }
	    		  else{
	    			  GameWindow.stage.setScene(arrivedFrom.scene); 
	    			  GameWindow.stage.setFullScreen(true);
	    			  }
	    		
	    		 Menus.currentMenu = arrivedFrom;
	    		  
	    		  
	    	    	   });
	    	    	    
	    
	    private ArrayList<GameButton> buttons = new ArrayList(); 
	    private Rectangle backGround = Helper.anySpriteFromImage("file:src/main/resources/Pictures/SettingsMenuBG.png", new Pair<Double, Double>(0.0,0.0), 800.0 ,800.0 );
	    public Optional<AudioClip> theme = Optional.empty();
	    private Group content = new Group();
	    public Scene scene = new Scene(content, 800.0, 800.0);
	    
	    
	    
	    public void refresh() {
	      
	      if(buttons.isEmpty()) {
	    	  buttons.add(exitButton); 
	    	  buttons.add(muteCheckBox); 
	          buttons.add(devModeCheckBox); 
	    	  } 
	      
	      if(content.getChildren().isEmpty()) {
	    	  content.getChildren().addAll(backGround, header, annotationText, volumeSlider.image); 
	    	  buttons.stream().forEach(button -> content.getChildren().add(button.fullImage()));
	          
	      }
	     
	      
	      backGround.setHeight(GameWindow.stage.getHeight());
	      backGround.setWidth(GameWindow.stage.getWidth());
	      header.setX(GameWindow.stage.getWidth()/2 + 100);
	      header.setY(GameWindow.stage.getHeight()/2 - 250);
	      annotationText.setX(GameWindow.stage.getWidth()/2 - 50);
	      annotationText.setY(GameWindow.stage.getHeight()/2 + 250);
	      header.setScaleX(3);
	      header.setScaleY(3);
	      header.setFill(Color.BLACK); 
	      
	      annotationText.setScaleX(3); 
	      annotationText.setScaleY(3);
	      annotationText.setFill(Color.PURPLE);
	      
	      buttons.stream().forEach(button -> button.refreshLocation());
	      
	      if (buttons.stream().anyMatch(button -> button.buttonAnnouncement().isPresent())) { this.annotationText.setText(buttons.stream().filter(button -> button.buttonAnnouncement().isPresent()).findAny().get().buttonAnnouncement().get());}
	      else {this.annotationText.setText("");
	    }
	      
	      checkBoxBackground.setX( GameWindow.stage.getWidth()/2 -400); 
	      checkBoxBackground.setY(GameWindow.stage.getHeight()/2);

	      if(muteCheckBox.isSelected) { Settings.muteSound = true;}
	      else { Settings.muteSound = false;}
	    	      
	      if(devModeCheckBox.isSelected) { Settings.devMode=true;}
	      else { Settings.devMode = false;}
	    	      
	      volumeSlider.refresh();
	      
	   }       
	}

   

   
  class LoadMenu extends GameMenu {
	    
	    String name = "Load Menu";
	    private Text header = new Text(0,0,"Load Game");
	   
	    private Text annotationText = new Text(0,0,"");
	   
	        
	    private GameButton slot1Button = new AnimatedButton(
	      "Slot 1", //Button text
	       new Pair<Double, Double>(-100.0, -150.0), //Offset from center
	       new Pair<Double, Double>(200.0, 100.0),  //Dimensions
	       "file:src/main/resources/Pictures/GrayRectButtonNormal.png", //Normal image path
	       "file:src/main/resources/Pictures/GrayRectButtonHover.png", //Hover image path
	       "file:src/main/resources/Pictures/GrayRectButtonpressed.png",//Pressed image path
	       Optional.empty(), //Announcement
	       Callable<Void>action1 = () ->{    //Action
	       SaveHandler.loadGame("src/main/resources/SaveFiles/Save1.DWAsave");
	       this.annotationText.setText("Slot 1 loaded");
	        });
	    
	   
	    private GameButton slot2Button = new AnimatedButton(
	       "Slot 2", //Button text
	       new Pair<Double, Double>(-100.0, -25.0), //Offset from center
	       new Pair<Double, Double>(200.0, 100.0),  //Dimensions
	       "file:src/main/resources/Pictures/GrayRectButtonNormal.png", //Normal image path
	       "file:src/main/resources/Pictures/GrayRectButtonHover.png", //Hover image path
	       "file:src/main/resources/Pictures/GrayRectButtonpressed.png",//Pressed image path
	       Optional.empty(), //Announcement
	       Callable<Void>action2 = () ->{    //Action
	    	   SaveHandler.loadGame("src/main/resources/SaveFiles/Save2.DWAsave");
	    	   this.annotationText.setText("Slot 2 loaded");
	    	        });
	    
	    
	    private GameButton slot3Button = new AnimatedButton(
	       "Slot 3", //Button text
	       new Pair<Double, Double>(-100.0, -100.0), //Offset from center
	       new Pair<Double, Double>(200.0, 100.0),  //Dimensions
	       "file:src/main/resources/Pictures/GrayRectButtonNormal.png", //Normal image path
	       "file:src/main/resources/Pictures/GrayRectButtonHover.png", //Hover image path
	       "file:src/main/resources/Pictures/GrayRectButtonpressed.png",//Pressed image path
	       Optional.empty(), //Announcement
	       Callable<Void>action3 = () ->{    //Action
	    	     SaveHandler.loadGame("src/main/resources/SaveFiles/Save3.DWAsave");
	    	     this.annotationText.setText("Slot 3 loaded");
	    	    	  });
	    	    
	    
	    
	    private GameButton slot4Button = new AnimatedButton(
	       "Slot 4", //Button text
	        new Pair<Double, Double>(-100.0, 225.0), //Offset from center
	        new Pair<Double, Double>(200.0, 100.0),  //Dimensions
	    	"file:src/main/resources/Pictures/GrayRectButtonNormal.png", //Normal image path
	    	"file:src/main/resources/Pictures/GrayRectButtonHover.png", //Hover image path
	    	"file:src/main/resources/Pictures/GrayRectButtonpressed.png",//Pressed image path
	    	Optional.empty(), //Announcement
	    	Callable<Void>action4 = () ->{    //Action
	    	      SaveHandler.loadGame("src/main/resources/SaveFiles/Save4.DWAsave");
	    	      this.annotationText.setText("Slot 4 loaded");
	    	    	   });
	    	    
	    
	    private GameButton exitButton = new AnimatedButton(
	    	 "", //Button text
	    	  new Pair<Double, Double>(300.0, 150.0), //Offset from center
	    	  new Pair<Double, Double>(150.0, 150.0),  //Dimensions
	    	  "file:src/main/resources/Pictures/SquareExitButton.png", //Normal image path
	    	  "file:src/main/resources/Pictures/SquareExitButton.png", //Hover image path
	    	  "file:src/main/resources/Pictures/SquareExitButton.png",//Pressed image path
	    	  Optional.of("Return to Main Menu"), //Announcement
	    	  Callable<Void>exit = () ->{    //Action
	    	   
	    		  if(!Menus.fullScreenStatus) { GameWindow.stage.setScene(Menus.MainMenu.scene); }
	    		  else{
	    			  GameWindow.stage.setScene(Menus.MainMenu.scene); 
	    			  GameWindow.stage.setFullScreen(true);
	    			  }
	    		
	    		 Menus.currentMenu = MainMenu;
	    		  
	    		  
	    	    	   });
	    	    	    
	    
	    private ArrayList<GameButton> buttons = new ArrayList(); 
	    private Rectangle backGround = Helper.anySpriteFromImage("file:src/main/resources/Pictures/DeathMenuBackGround.png", new Pair<Double, Double>(0.0,0.0), 800.0 ,800.0 );
	    public Optional<AudioClip> theme = Optional.empty();
	    private Group content = new Group();
	    public Scene scene = new Scene(content, 800.0, 800.0);
	    
	    
	    
	    public void refresh() {
	      
	      if(buttons.isEmpty()) {
	    	  buttons.add(slot1Button); 
	    	  buttons.add(slot2Button); 
	          buttons.add(slot3Button); 
	          buttons.add(slot4Button); 
	    	  buttons.add(exitButton);
	    	  } 
	      
	      if(content.getChildren().isEmpty()) {
	    	  content.getChildren().addAll(backGround, header, annotationText); 
	    	  buttons.stream().forEach(button -> content.getChildren().add(button.fullImage()));
	          
	      }
	     
	      
	      backGround.setHeight(GameWindow.stage.getHeight());
	      backGround.setWidth(GameWindow.stage.getWidth());
	      header.setX(GameWindow.stage.getWidth()/2 + 100);
	      header.setY(GameWindow.stage.getHeight()/2 - 250);
	      annotationText.setX(GameWindow.stage.getWidth()/2 - 50);
	      annotationText.setY(GameWindow.stage.getHeight()/2 + 250);
	      header.setScaleX(3);
	      header.setScaleY(3);
	      header.setFill(Color.WHITE); 
	      
	      annotationText.setScaleX(3); 
	      annotationText.setScaleY(3);
	      annotationText.setFill(Color.PURPLE);
	      
	      buttons.stream().forEach(button -> button.refreshLocation());
	      
	      if (buttons.stream().anyMatch(button -> button.buttonAnnouncement().isPresent())) { this.annotationText.setText(buttons.stream().filter(button -> button.buttonAnnouncement().isPresent()).findAny().get().buttonAnnouncement().get());}
	      else {this.annotationText.setText("");
	    }
	   }       
	}

    
   
  class SaveMenu extends GameMenu {
    
    String name = "Save Menu";
    private Text header = new Text(0,0,"Save Game");
   
    private Text annotationText = new Text(0,0,"");
   
        
    private GameButton slot1Button = new AnimatedButton(
      "Slot 1", //Button text
       new Pair<Double, Double>(-100.0, -150.0), //Offset from center
       new Pair<Double, Double>(200.0, 100.0),  //Dimensions
       "file:src/main/resources/Pictures/GrayRectButtonNormal.png", //Normal image path
       "file:src/main/resources/Pictures/GrayRectButtonHover.png", //Hover image path
       "file:src/main/resources/Pictures/GrayRectButtonpressed.png",//Pressed image path
       Optional.empty(), //Announcement
       Callable<Void>action1 = () ->{    //Action
       SaveHandler.saveGame("src/main/resources/SaveFiles/Save1.DWAsave");
       this.annotationText.setText("Slot 1 saved");
        });
    
   
    private GameButton slot2Button = new AnimatedButton(
       "Slot 2", //Button text
       new Pair<Double, Double>(-100.0, -25.0), //Offset from center
       new Pair<Double, Double>(200.0, 100.0),  //Dimensions
       "file:src/main/resources/Pictures/GrayRectButtonNormal.png", //Normal image path
       "file:src/main/resources/Pictures/GrayRectButtonHover.png", //Hover image path
       "file:src/main/resources/Pictures/GrayRectButtonpressed.png",//Pressed image path
       Optional.empty(), //Announcement
       Callable<Void>action2 = () ->{    //Action
    	   SaveHandler.saveGame("src/main/resources/SaveFiles/Save2.DWAsave");
    	   this.annotationText.setText("Slot 2 saved");
    	        });
    
    
    private GameButton slot3Button = new AnimatedButton(
       "Slot 3", //Button text
       new Pair<Double, Double>(-100.0, -100.0), //Offset from center
       new Pair<Double, Double>(200.0, 100.0),  //Dimensions
       "file:src/main/resources/Pictures/GrayRectButtonNormal.png", //Normal image path
       "file:src/main/resources/Pictures/GrayRectButtonHover.png", //Hover image path
       "file:src/main/resources/Pictures/GrayRectButtonpressed.png",//Pressed image path
       Optional.empty(), //Announcement
       Callable<Void>action3 = () ->{    //Action
    	     SaveHandler.saveGame("src/main/resources/SaveFiles/Save3.DWAsave");
    	     this.annotationText.setText("Slot 3 saved");
    	    	  });
    	    
    
    
    private GameButton slot4Button = new AnimatedButton(
       "Slot 4", //Button text
        new Pair<Double, Double>(-100.0, 225.0), //Offset from center
        new Pair<Double, Double>(200.0, 100.0),  //Dimensions
    	"file:src/main/resources/Pictures/GrayRectButtonNormal.png", //Normal image path
    	"file:src/main/resources/Pictures/GrayRectButtonHover.png", //Hover image path
    	"file:src/main/resources/Pictures/GrayRectButtonpressed.png",//Pressed image path
    	Optional.empty(), //Announcement
    	Callable<Void>action4 = () ->{    //Action
    	      SaveHandler.saveGame("src/main/resources/SaveFiles/Save4.DWAsave");
    	      this.annotationText.setText("Slot 4 saved");
    	    	   });
    	    
    
    private GameButton exitButton = new AnimatedButton(
    	 "", //Button text
    	  new Pair<Double, Double>(300.0, 150.0), //Offset from center
    	  new Pair<Double, Double>(150.0, 150.0),  //Dimensions
    	  "file:src/main/resources/Pictures/SquareExitButton.png", //Normal image path
    	  "file:src/main/resources/Pictures/SquareExitButton.png", //Hover image path
    	  "file:src/main/resources/Pictures/SquareExitButton.png",//Pressed image path
    	  Optional.of("Return to Main Menu"), //Announcement
    	  Callable<Void>exit = () ->{    //Action
    	   
    		  if(!Menus.fullScreenStatus) { GameWindow.stage.setScene(Menus.MainMenu.scene); }
    		  else{
    			  GameWindow.stage.setScene(Menus.MainMenu.scene); 
    			  GameWindow.stage.setFullScreen(true);
    			  }
    		
    		 Menus.currentMenu = Menus.MainMenu;
    		  
    		  
    	    	   });
    	    	    
    
    private ArrayList<GameButton> buttons = new ArrayList(); 
    private Rectangle backGround = Helper.anySpriteFromImage("file:src/main/resources/Pictures/DeathMenuBackGround.png", new Pair<Double, Double>(0.0,0.0), 800.0 ,800.0 );
    public Optional<AudioClip> theme = Optional.empty();
    private Group content = new Group();
    public Scene scene = new Scene(content, 800.0, 800.0);
    
    
    
    public void refresh() {
      
      if(buttons.isEmpty()) {
    	  buttons.add(slot1Button); 
    	  buttons.add(slot2Button); 
          buttons.add(slot3Button); 
          buttons.add(slot4Button); 
    	  buttons.add(exitButton);
    	  } 
      
      if(content.getChildren().isEmpty()) {
    	  content.getChildren().addAll(backGround, header, annotationText); 
    	  buttons.stream().forEach(button -> content.getChildren().add(button.fullImage()));
          
      }
     
      
      backGround.setHeight(GameWindow.stage.getHeight());
      backGround.setWidth(GameWindow.stage.getWidth());
      header.setX(GameWindow.stage.getWidth()/2 + 100);
      header.setY(GameWindow.stage.getHeight()/2 - 250);
      annotationText.setX(GameWindow.stage.getWidth()/2 - 50);
      annotationText.setY(GameWindow.stage.getHeight()/2 + 250);
      header.setScaleX(3);
      header.setScaleY(3);
      header.setFill(Color.WHITE); 
      
      annotationText.setScaleX(3); 
      annotationText.setScaleY(3);
      annotationText.setFill(Color.PURPLE);
      
      buttons.stream().forEach(button -> button.refreshLocation());
      
      if (buttons.stream().anyMatch(button -> button.buttonAnnouncement().isPresent())) { this.annotationText.setText(buttons.stream().filter(button -> button.buttonAnnouncement().isPresent()).findAny().get().buttonAnnouncement().get());}
      else {this.annotationText.setText("");
    }
   }       
}

//##########################################################################################################################################################################################

//Luokka animatedButton tarjoaa helpon tavan luoda nappeja valikoihin. Action-parametri sisältää nappia painettaessas suoritettavan koodin.

class AnimatedButton extends GameButton{
  
	String textForButton;
	Pair<Double, Double> locationOffsetFromCenter;
	Pair<Double, Double>dimensions;
	String normalImgPath;
	String hoverImgPath;
	String pressedImgPath;
	Optional<String>announcement;
	Callable<Void>action;
	
	
	
  private Text buttonText = new Text(0,0, textForButton);
  
  Optional<String>buttonAnnouncement = Optional.empty();  //Teksti joka näkyy valikossa kun hiiri viedään napin päälle
  
  public Boolean isLocked = false;         //Jos nappi on lukittu sitä ei voi painaa. Käytetään pelin tasojen yhteydessä
  
  private Node normalImg = Helper.anySpriteFromImage(normalImgPath, new Pair<Double, Double>(0.0,0.0), dimensions.getKey(), dimensions.getValue());   //Kuvat kolmeen eri tilanteeseen
  private Node hoverImg = Helper.anySpriteFromImage(hoverImgPath, new Pair<Double, Double>(0.0,0.0), dimensions.getKey(), dimensions.getValue());
  private Node pressedImg = Helper.anySpriteFromImage(pressedImgPath, new Pair<Double, Double>(0.0,0.0), dimensions.getKey(), dimensions.getValue())
  
  Node currentImage = normalImg;
  
  //Konstruktori
  public AnimatedButton(String textForButton, Pair<Double, Double> locationOffsetFromCenter, Pair<Double, Double>dimensions, String normalImgPath, String hoverImgPath, String pressedImgPath, Optional<String>announcement, Callable<Void>action) {
	  
	  this.textForButton = textForButton;
	  this.locationOffsetFromCenter = locationOffsetFromCenter;
	  this.dimensions = dimensions;
	  this.normalImgPath = normalImgPath;
	  this.hoverImgPath = hoverImgPath;
	  this.pressedImgPath = pressedImgPath;
	  this.announcement = announcement;
	  this.action = action;
	  
	  buttonText.setScaleX(2.0);
	  buttonText.setScaleY(2.0);
	  buttonText.setMouseTransparent(true);
	  
	  
	  EventHandler<MouseEvent> mouseClickHandler = new EventHandler<MouseEvent>() {
		  	public void handle(MouseEvent event) {
		  		
		  		try{
		  		   if(isLocked){
		  		     buttonAnnouncement = Optional.of("Locked");
		  		   }else{
		  		    currentImage = pressedImg;
		  		    action.call();
		  		    reset();
		  		   }
		  		 }catch(Exception e){
		  		     GameWindow.exceptionScreen("Something is wrong. + \n" + e);
		  		   }
		  	}
	  };
	  
	  EventHandler<MouseEvent> mouseEnterHandler = new EventHandler<MouseEvent>() {
		  	public void handle(MouseEvent event) {
		  		
		  		 try{
		  		    currentImage = hoverImg;
		  		    buttonAnnouncement = announcement ;
		  		 }catch(Exception e){
		  		      GameWindow.exceptionScreen("Something is wrong. + \n" + e);
		  		    }
		  	}
	  };
	  
	  EventHandler<MouseEvent> mouseExitHandler = new EventHandler<MouseEvent>() {
		  	public void handle(MouseEvent event) {
		  		 
		  	    try{
		  	     currentImage = normalImg;
		  	     buttonAnnouncement = Optional.empty();
		  	    }catch(Exception e){
		  	      GameWindow.exceptionScreen("Something is wrong. + \n" + e);
		  	    }	
		  	}
	  };
	  
	  normalImg.setOnMouseClicked(mouseClickHandler);
	  normalImg.setOnMouseEntered(mouseEnterHandler);
	  normalImg.setOnMouseExited(mouseExitHandler);
	    
  }
  
  
  
  
  public Group fullImage() {
	  
	  Group done = new Group();
      done.getChildren().add(currentImage);
      done.getChildren().add(buttonText);
  }
  
  public void reset() { this.currentImage = normalImg; } //Nappi resetoidaan painalluksen jälkeen jotta se olisi valmis uudelleen käytettäväksi
  
  public void refreshLocation() {
    
    //Seuraavat rivit pitävät napin paikoillaan ikkunan koon muuttuessa
    this.currentImage.setLayoutX(GameWindow.stage.getWidth()/2 - dimensions.getKey()/2  + locationOffsetFromCenter.getKey()); 
    this.currentImage.setLayoutY(GameWindow.stage.getHeight()/2 - dimensions.getValue()/2 + locationOffsetFromCenter.getValue());
    this.buttonText.setX(GameWindow.stage.getWidth()/2 - dimensions.getKey()/2 + dimensions.getKey()/2-35 + locationOffsetFromCenter.getKey());
    this.buttonText.setY(GameWindow.stage.getHeight()/2 - dimensions.getValue()/2  + dimensions.getValue()/2 + locationOffsetFromCenter.getValue());
  }
  
  public void changeOffset(Double dx, Double dy) {
    
    Pair<Double, Double> orig = this.locationOffsetFromCenter;
    this.locationOffsetFromCenter = new Pair<Double, Double>(orig.getKey() + dx, orig.getValue() + dy);
    
  }
  
  public void lock() {
	  this.isLocked = true;
  }
  
  public void unlock() {
	  this.isLocked = false;
  }
  
  
}

//################################################################################################################################################################################################

class GameSlider{
  
	String header;
	Double min;
	Double max;
	Double value;
	Pair<Double, Double> dimensions;
	Pair<Double, Double> locationOffset;
	
	
 private Slider slider  = new Slider(min, max, value);
    
 private Text text = new Text(0,0,header);
  
 private Text percentage = new Text(0,0,slider.getValue() + "%");
 
 public Group image = new Group();
 
 //Konstruktori
 public GameSlider(String header, Double min, Double max, Double value, Pair<Double, Double> dimensions, Pair<Double, Double> locationOffset) {
	 
	 this.header = header;
	 this.min = min;
	 this.max = max; 
	 this.value = value;
	 this.dimensions = dimensions;
	 this.locationOffset = locationOffset;
	 
	 slider.setPrefWidth(dimensions.getKey());
	 slider.setPrefHeight(dimensions.getValue());
	 slider.getStylesheets().addAll("file:src/main/resources/StyleSheets/SliderStyle.css");
	 
	 image.getChildren().add(slider);
	 image.getChildren().add(text);
	 image.getChildren().add(percentage);
 }
    
  public void refresh() {
   slider.setLayoutX(GameWindow.stage.getWidth()/2 + locationOffset.getKey());
   slider.setLayoutY(GameWindow.stage.getHeight()/2 + locationOffset.getValue());
   text.setX(GameWindow.stage.getWidth()/2 + locationOffset.getKey() + 20);
   text.setY(GameWindow.stage.getHeight()/2 + locationOffset.getValue() - 20);
   percentage.setX(GameWindow.stage.getWidth()/2 + locationOffset.getKey() + 0.9*dimensions.getKey());
   percentage.setY(GameWindow.stage.getHeight()/2 + locationOffset.getValue() + 55);
   percentage.setText(slider.getValue() +"%");
  }
  
  
  public Double currentValue() { return slider.getValue();}
    
  public void changeValue(Double value) {
	  this.slider.adjustValue(value);
  }
  
}




//######################################################################################################################################################################################################

class GameCheckBox extends GameButton{
 
	String text;
	Pair<Double, Double> locationOffset;
	
	
	
 public Boolean isSelected = false;
  
 private Node buttonBase = Helper.anySpriteFromImage("file:src/main/resources/Pictures/CheckBoxBase.png", new Pair<Double, Double>(0.0,0.0), 50.0, 50.0);
 private Node buttonMark = Helper.anySpriteFromImage("file:src/main/resources/Pictures/CheckBoxmark.png", new Pair<Double, Double>(0.0,0.0), 30.0, 30.0);
 private Text labelText = new Text(0,0,text);
 
 
 //Konstruktori
 public GameCheckBox(String text, Pair<Double, Double> locationOffset) {
	 
	 this.text = text;
	 this.locationOffset = locationOffset;
	 
	 labelText.setFill(Color.WHITE);
	 
	  EventHandler<MouseEvent> mouseClickHandler = new EventHandler<MouseEvent>() {
		  	public void handle(MouseEvent event) {
		  		
		  		if(isSelected == false) {
		  		   isSelected = true;
		  		}
		  	    else {
		  	       isSelected = false;
		  	    }
	  };
	   
 };
	  
	  this.buttonBase.setOnMouseClicked(mouseClickHandler);
	  this.buttonMark.setOnMouseClicked(mouseClickHandler);
	  
	  
 }
  
  public Group fullImage(){
	  
	Group done = new Group();
	
	if(this.isSelected) {
		
		done.getChildren().add(buttonBase);
		done.getChildren().add(buttonMark);
		done.getChildren().add(labelText);
		
		return done;
		
	}else{
		
		done.getChildren().add(buttonBase);
		done.getChildren().add(labelText);
		
		return done;
	}
    
  } 
 
    
  public void refreshLocation(){
    
    buttonBase.setLayoutX(GameWindow.stage.getWidth()/2 + locationOffset.getKey());
    buttonBase.setLayoutY(GameWindow.stage.getHeight()/2 + locationOffset.getValue());
    buttonMark.setLayoutX(GameWindow.stage.getWidth()/2 + locationOffset.getKey() + 10);
    buttonMark.setLayoutY(GameWindow.stage.getHeight()/2 + locationOffset.getValue() + 10);
    labelText.setX(GameWindow.stage.getWidth()/2 + locationOffset.getKey() -120);
    labelText.setY(GameWindow.stage.getHeight()/2 + locationOffset.getValue() + 20);
    
  } 
  
  public Optional<String> buttonAnnouncement() {return Optional.empty();}
  
  
}
//################################################################################################################################################################


abstract class GameMenu{
  
  String name;
  Scene scene;
  public abstract void refresh();
  public Optional<AudioClip> theme;
  
}

abstract class GameButton{
  public abstract void refreshLocation();
  public abstract Group fullImage();
  public abstract Optional<String> buttonAnnouncement();

}