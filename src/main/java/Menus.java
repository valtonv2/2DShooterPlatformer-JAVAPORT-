package main.java;

import javafx.scene.shape.Rectangle;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.text.Text;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Optional;
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



public class Menus{
	
	

public MainMenu mainMenu = new MainMenu();	
public SettingsMenu SettingsMenu = new SettingsMenu();
public LevelSelectMenu LevelSelectMenu = new LevelSelectMenu();
public LoadMenu LoadMenu = new LoadMenu();
public SaveMenu SaveMenu = new SaveMenu();
public DeathMenu DeathMenu = new DeathMenu();
public PauseMenu PauseMenu = new PauseMenu();
	
	
public GameMenu currentMenu = mainMenu;  //Tämän perusteella GUI osaa kutsua oikean menun refresh-metodia

	
  
  public Boolean fullScreenStatus() { return GameWindow.stage.isFullScreen();}
  public void menuStatus() {System.out.println(this.mainMenu.name);}
  

  

 public class MainMenu extends GameMenu {
	    
	    private Text header = new Text(0,0,"");
	    private Text annotationText = new Text(0,0,"");
	    private Rectangle gameLogo = Helper.anySpriteFromImage("file:src/main/resources/Pictures/GameLogo.png", new Pair<Double, Double>(0.0,0.0), 600.0, 250.0);
	   
	    private AnimatedButton playButton = new AnimatedButton(
	    		this,
	      "Play Game", //Button text
	       new Pair<Double, Double>(0.0, -100.0), //Offset from center
	       new Pair<Double, Double>(200.0, 50.0),  //Dimensions
	       "file:src/main/resources/Pictures/GrayRectButtonNormal.png", //Normal image path
	       "file:src/main/resources/Pictures/GrayRectButtonHover.png", //Hover image path
	       "file:src/main/resources/Pictures/GrayRectButtonpressed.png",//Pressed image path
	       Optional.empty(), //Announcement
	        new Runnable(){    //Action
	      
	    	  @Override
	    	  public void run() {
	    	  
	    	   GameWindow.menuClock.stop();
	           GameWindow.clock.start();
	           GameWindow.currentGame.fullImage.setCursor(Cursor.NONE);
	           if(!GameWindow.Menus.fullScreenStatus()) { GameWindow.stage.setScene(GameWindow.currentGame.fullImage);}
	           else{GameWindow.stage.setScene(GameWindow.currentGame.fullImage); 
	           GameWindow.stage.setFullScreen(true);
	           }
	           }
	            
	        });
	    
	   
	    private AnimatedButton settingsButton = new AnimatedButton(
	    		this,
	       "Settings", //Button text
	       new Pair<Double, Double>(0.0, -25.0), //Offset from center
	       new Pair<Double, Double>(200.0, 50.0),  //Dimensions
	       "file:src/main/resources/Pictures/GrayRectButtonNormal.png", //Normal image path
	       "file:src/main/resources/Pictures/GrayRectButtonHover.png", //Hover image path
	       "file:src/main/resources/Pictures/GrayRectButtonpressed.png",//Pressed image path
	       Optional.empty(), //Announcement
	       new Runnable(){    //Action
	 	      
		    	  @Override
		    	  public void run() {
		    	  
	    	  
	    	   if(!GameWindow.Menus.fullScreenStatus()) { GameWindow.stage.setScene(GameWindow.Menus.SettingsMenu.scene);}
	    	   else{GameWindow.stage.setScene(GameWindow.Menus.SettingsMenu.scene); 
	    	        GameWindow.stage.setFullScreen(true); }
	    		       
	    	   GameWindow.Menus.currentMenu = GameWindow.Menus.SettingsMenu;
	    	   GameWindow.Menus.SettingsMenu.arrivedFrom = GameWindow.Menus.mainMenu;
		    	  }
	    	   
	    	        });
	    
	    
	    private AnimatedButton levelSelectButton = new AnimatedButton(
	    		this,
	       "Select Level", //Button text
	       new Pair<Double, Double>(0.0, 50.0), //Offset from center
	       new Pair<Double, Double>(200.0, 50.0),  //Dimensions
	       "file:src/main/resources/Pictures/GrayRectButtonNormal.png", //Normal image path
	       "file:src/main/resources/Pictures/GrayRectButtonHover.png", //Hover image path
	       "file:src/main/resources/Pictures/GrayRectButtonpressed.png",//Pressed image path
	       Optional.empty(), //Announcement
	       new Runnable(){    //Action
	 	      
		    	  @Override
		    	  public void run() {
		    	  
	    	   
	    	   if(!GameWindow.Menus.fullScreenStatus()) { GameWindow.stage.setScene(GameWindow.Menus.LevelSelectMenu.scene);}
	    	   else{
	    	     GameWindow.stage.setScene(GameWindow.Menus.LevelSelectMenu.scene); 
	    	     GameWindow.stage.setFullScreen(true);
	    	     }
	    	   GameWindow.Menus.currentMenu = GameWindow.Menus.LevelSelectMenu;
		    	  }
	    	   
	    	    	  });
	    	    
	    
	    
	    private AnimatedButton loadButton = new AnimatedButton(
	    		this,
	       "Load Game", //Button text
	        new Pair<Double, Double>(0.0, 125.0), //Offset from center
	        new Pair<Double, Double>(200.0, 50.0),  //Dimensions
	    	"file:src/main/resources/Pictures/GrayRectButtonNormal.png", //Normal image path
	    	"file:src/main/resources/Pictures/GrayRectButtonHover.png", //Hover image path
	    	"file:src/main/resources/Pictures/GrayRectButtonpressed.png",//Pressed image path
	    	Optional.empty(), //Announcement
	    	new Runnable(){    //Action
	  	      
		    	  @Override
		    	  public void run() {
		    	  
	    	      
	    		
	    		if(!GameWindow.Menus.fullScreenStatus()) { GameWindow.stage.setScene(GameWindow.Menus.LoadMenu.scene);}
		    	   else{
		    	     GameWindow.stage.setScene(GameWindow.Menus.LoadMenu.scene); 
		    	     GameWindow.stage.setFullScreen(true);
		    	     }
		    	   GameWindow.Menus.currentMenu = GameWindow.Menus.LoadMenu;
		    	  }
	    		
	    	    	   });
	    
	    private AnimatedButton saveButton = new AnimatedButton(
	    		this,
	 	    "Save Game", //Button text
	 	     new Pair<Double, Double>(0.0, 200.0), //Offset from center
	 	     new Pair<Double, Double>(200.0, 50.0),  //Dimensions
	 	     "file:src/main/resources/Pictures/GrayRectButtonNormal.png", //Normal image path
	 	     "file:src/main/resources/Pictures/GrayRectButtonHover.png", //Hover image path
	 	     "file:src/main/resources/Pictures/GrayRectButtonpressed.png",//Pressed image path
	 	     Optional.empty(), //Announcement
	 	    new Runnable(){    //Action
	 		      
		    	  @Override
		    	  public void run() {
		    	  
	 	    	      
	 	    		
	 	   	if(!GameWindow.Menus.fullScreenStatus()) { GameWindow.stage.setScene(GameWindow.Menus.SaveMenu.scene);}
	 	    else{
	 		     GameWindow.stage.setScene(GameWindow.Menus.SaveMenu.scene); 
	 		     GameWindow.stage.setFullScreen(true);
	 		    }
	 		GameWindow.Menus.currentMenu = GameWindow.Menus.SaveMenu;
		    	  }
	 	    		
	 	    	        });
	    	    
	    
	    private AnimatedButton exitButton = new AnimatedButton(
	    		this,
		 	 "Exit Game", //Button text
		 	 new Pair<Double, Double>(0.0, 275.0), //Offset from center
		 	 new Pair<Double, Double>(200.0, 50.0),  //Dimensions
		 	 "file:src/main/resources/Pictures/GrayRectButtonNormal.png", //Normal image path
		 	 "file:src/main/resources/Pictures/GrayRectButtonHover.png", //Hover image path
		 	 "file:src/main/resources/Pictures/GrayRectButtonpressed.png",//Pressed image path
		 	 Optional.empty(), //Announcement
		 	new Runnable(){    //Action
			      
		    	  @Override
		    	  public void run() {
		    	  
		 	    	      
		 		GameWindow.stage.close();
	
		    	  }
		 	   
		 	    	        });
	    

	    	    	    
	    
	    private ArrayList<AnimatedButton> buttons = new ArrayList<AnimatedButton>(); 
	    private Rectangle backGround = Helper.anySpriteFromImage("file:src/main/resources/Pictures/MainMenuBackGround.png", new Pair<Double, Double>(0.0,0.0), 2783.0 ,2484.0 );
	    public Optional<AudioClip> theme = Optional.empty();
	    
	    
	    public MainMenu() {
	    	content = new Group();
	    	scene = new Scene(content, 800.0, 800.0);
	    	name = "Main Menu";
	    	
	    }
	    
	    
	    
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
	      content.getChildren().addAll(backGround, header, annotationText, gameLogo); 
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
	      
	      if (buttons.stream().anyMatch(button -> button.buttonAnnouncement.isPresent())) { this.annotationText.setText(buttons.stream().filter(button -> button.buttonAnnouncement.isPresent()).findAny().get().buttonAnnouncement.get());}
	      else {this.annotationText.setText("");
	    }
	   } 
	    
	    public void refreshContent() {
		 	  
	    	
		  	  content.getChildren().clear();
	    	
		  	   
	    }
	    
	}

 

 //----------------------------------------------------------------------------------------------------------------------------------------------------------------------
    
  class PauseMenu extends GameMenu {
	    
	   
	    private Text header = new Text(0,0,"Game Paused");
	   
	    private Text annotationText = new Text(0,0,"");
	   
	        
	    private AnimatedButton resumeButton = new AnimatedButton(
	    		this,
	      "", //Button text
	       new Pair<Double, Double>(-200.0, 0.0), //Offset from center
	       new Pair<Double, Double>(150.0, 150.0),  //Dimensions
	       "file:src/main/resources/Pictures/SquarePlayButton.png", //Normal image path
	       "file:src/main/resources/Pictures/SquarePlayButton.png", //Hover image path
	       "file:src/main/resources/Pictures/SquarePlayButton.png",//Pressed image path
	       Optional.of("Resume game"), //Announcement
	       new Runnable(){    //Action
	 	      
		    	  @Override
		    	  public void run() {
		    	  
	    	   GameWindow.clock.start();
	           GameWindow.menuClock.stop();
	           if(!GameWindow.Menus.fullScreenStatus()) {
	        	   GameWindow.stage.setScene(GameWindow.currentGame.fullImage); 
	           }else{
	        	   GameWindow.stage.setScene(GameWindow.currentGame.fullImage); 
	           GameWindow.stage.setFullScreen(true); 
	           }
	           }
	        });
	    
	   
	    private AnimatedButton settingsButton = new AnimatedButton(
	    		this,
	       "", //Button text
	       new Pair<Double, Double>(0.0, 0.0), //Offset from center
	       new Pair<Double, Double>(150.0, 150.0),  //Dimensions
	       "file:src/main/resources/Pictures/SquareSettingButton.png", //Normal image path
	       "file:src/main/resources/Pictures/SquareSettingButton.png", //Hover image path
	       "file:src/main/resources/Pictures/SquareSettingButton.png",//Pressed image path
	       Optional.of("Game Settings"), //Announcement
	       new Runnable(){    //Action
	 	      
		    	  @Override
		    	  public void run() {
		    	  
	    	   GameWindow.clock.stop();
	           GameWindow.menuClock.start();
	           if(!GameWindow.Menus.fullScreenStatus()) {
	        	   GameWindow.stage.setScene(GameWindow.Menus.SettingsMenu.scene);
	           }else{
	        	   GameWindow.stage.setScene(GameWindow.Menus.SettingsMenu.scene);
	        	   GameWindow.stage.setFullScreen(true);
	        	   }
	           GameWindow.Menus.currentMenu = GameWindow.Menus.SettingsMenu;
	           GameWindow.Menus.SettingsMenu.arrivedFrom = GameWindow.Menus.PauseMenu;
		    	  }
	    	        });
	    
	    
	    private AnimatedButton exitButton = new AnimatedButton(
	    		this,
	    	 "", //Button text
	    	  new Pair<Double, Double>(200.0, 0.0), //Offset from center
	    	  new Pair<Double, Double>(150.0, 150.0),  //Dimensions
	    	  "file:src/main/resources/Pictures/SquareExitButton.png", //Normal image path
	    	  "file:src/main/resources/Pictures/SquareExitButton.png", //Hover image path
	    	  "file:src/main/resources/Pictures/SquareExitButton.png",//Pressed image path
	    	  Optional.of("Return to Main Menu"), //Announcement
	    	  new Runnable(){    //Action
	    	      
		    	  @Override
		    	  public void run() {
		    	  
	    	   
	    		  if(!GameWindow.Menus.fullScreenStatus()) { GameWindow.stage.setScene(GameWindow.Menus.mainMenu.scene); }
	    		  else{
	    			  GameWindow.stage.setScene(GameWindow.Menus.mainMenu.scene); 
	    			  GameWindow.stage.setFullScreen(true);
	    			  }
	    		
	    		 GameWindow.Menus.currentMenu = GameWindow.Menus.mainMenu;
		    	  }
	    		  
	    	    	   });
	    	    	    
	    
	    private ArrayList<AnimatedButton> buttons = new ArrayList<AnimatedButton>(); 
	    private Rectangle backGround = Helper.anySpriteFromImage("file:src/main/resources/Pictures/MenuBackGround.png", new Pair<Double, Double>(0.0,0.0), 800.0 ,800.0 );
	    public Optional<AudioClip> theme = Optional.empty();
	   
	    
	    
	    
	    public PauseMenu() {
	    	content = new Group();
	    	scene = new Scene(content, 800.0, 800.0);
	    	name = "Pause Menu";
	    	
	    }
	    
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
	      
	      if (buttons.stream().anyMatch(button -> button.buttonAnnouncement.isPresent())) { this.annotationText.setText(buttons.stream().filter(button -> button.buttonAnnouncement.isPresent()).findAny().get().buttonAnnouncement.get());}
	      else {this.annotationText.setText("");
	    }
	   }   
	    
	    public void refreshContent() {
		 	   
		  	  content.getChildren().clear();
		         	  	   
		     }
	    
	    
	}

    
    
    
    
    
//------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------    
  
  class DeathMenu extends GameMenu {
	    
	    private Text header = new Text(0,0,"You Died");
	   
	    private Text annotationText = new Text(0,0,"");
	   
	        
	    private AnimatedButton restartButton = new AnimatedButton(
	    		this,
	      "", //Button text
	       new Pair<Double, Double>(-300.0, -150.0), //Offset from center
	       new Pair<Double, Double>(150.0, 150.0),  //Dimensions
	       "file:src/main/resources/Pictures/SquarePlayButton.png", //Normal image path
	       "file:src/main/resources/Pictures/SquarePlayButton.png", //Hover image path
	       "file:src/main/resources/Pictures/SquarePlayButton.png",//Pressed image path
	       Optional.of("Restart Game"), //Announcement
	       new Runnable(){    //Action
	 	      
		    	  @Override
		    	  public void run() {
		    	  
	       
	           GameWindow.menuClock.stop();
	           GameWindow.clock.start();
	           GameWindow.currentGame.reset();
	    
	           PlayerHUD.weaponHud.weaponBoxes.stream().forEach(box -> box.removeItem());
	           PlayerHUD.equipmentBox.box.removeItem();
	           PlayerHUD.equipmentBox.updateItems();
	           if(!GameWindow.Menus.fullScreenStatus()) { GameWindow.stage.setScene(GameWindow.currentGame.fullImage); }
	           else{GameWindow.stage.setScene(GameWindow.currentGame.fullImage); 
	                GameWindow.stage.setFullScreen(true); 
	                }
	    	   
		    	  }
	        });
	        
	    
	    private AnimatedButton exitButton = new AnimatedButton(
	    		this,
	    	 "", //Button text
	    	  new Pair<Double, Double>(300.0, 150.0), //Offset from center
	    	  new Pair<Double, Double>(150.0, 150.0),  //Dimensions
	    	  "file:src/main/resources/Pictures/SquareExitButton.png", //Normal image path
	    	  "file:src/main/resources/Pictures/SquareExitButton.png", //Hover image path
	    	  "file:src/main/resources/Pictures/SquareExitButton.png",//Pressed image path
	    	  Optional.of("Return to Main Menu"), //Announcement
	    	  new Runnable(){    //Action
	    	      
		    	  @Override
		    	  public void run() {
		    	  
	    	   
	    		    GameWindow.clock.stop();
	    	        GameWindow.menuClock.start();
	    	        GameWindow.currentGame.reset();
	    	        if(!GameWindow.Menus.fullScreenStatus()) {
	    	        	GameWindow.stage.setScene(GameWindow.Menus.mainMenu.scene);
	    	        }else{
	    	        	GameWindow.stage.setScene(GameWindow.Menus.mainMenu.scene); 
	    	        	GameWindow.stage.setFullScreen(true);
	    	        	}
	    	        PlayerHUD.weaponHud.weaponBoxes.stream().forEach(box -> box.removeItem());
	    	        PlayerHUD.equipmentBox.box.removeItem();
	    	        PlayerHUD.equipmentBox.updateItems();
	    	        GameWindow.Menus.currentMenu = GameWindow.Menus.mainMenu;
	    		  
		    	  }
	    	    	   });
	    	    	    
	    
	    private ArrayList<AnimatedButton> buttons = new ArrayList<AnimatedButton>(); 
	    private Rectangle backGround = Helper.anySpriteFromImage("file:src/main/resources/Pictures/DeathMenuBackGround.png", new Pair<Double, Double>(0.0,0.0), 800.0 ,800.0 );
	    public Optional<AudioClip> theme = Optional.of(new AudioClip("file:src/main/resources/sound/DWADeathMenuTheme.wav"));
	    
	    public DeathMenu() {
	    	content = new Group();
	    	scene = new Scene(content, 800.0, 800.0);
	    	name = "Death Menu";
	    	
	    }
	    
	    
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
	      
	      if (buttons.stream().anyMatch(button -> button.buttonAnnouncement.isPresent())) { this.annotationText.setText(buttons.stream().filter(button -> button.buttonAnnouncement.isPresent()).findAny().get().buttonAnnouncement.get());}
	      else {this.annotationText.setText("");
	    }
	   }       
	    
	    public void refreshContent() {
		 	   
		  	  content.getChildren().clear();
		   	  content.getChildren().addAll(backGround, header, annotationText); 
		   	  buttons.stream().forEach(button -> content.getChildren().add(button.fullImage()));
		         
		  	   
		     }
	    
	    
	    
	}

  
  
  
 //==================================================================================================================================================================================================== 
   
  class LevelSelectMenu extends GameMenu {
	    
	    
	    private Text header = new Text(0,0,"Level Selection");
	   
	    private Text annotationText = new Text(0,0,"");
	   
	        
	    private AnimatedButton level1Button = new AnimatedButton(
	    		this,
	      "Level 1", //Button text
	       new Pair<Double, Double>(30.0, -245.0), //Offset from center
	       new Pair<Double, Double>(140.0, 95.0),  //Dimensions
	       "file:src/main/resources/Pictures/GrayRectButtonNormal.png", //Normal image path
	       "file:src/main/resources/Pictures/GrayRectButtonHover.png", //Hover image path
	       "file:src/main/resources/Pictures/GrayRectButtonpressed.png",//Pressed image path
	       Optional.of("Large City"), //Announcement
	       new Runnable(){    //Action
	 	      
		    	  @Override
		    	  public void run() {
		    	  
	       
	    	   GameWindow.menuClock.stop();
	           GameWindow.clock.start();
	           GameWindow.currentGame.swapLevel(1);
	           PlayerHUD.weaponHud.weaponBoxes.stream().forEach(item -> item.removeItem());
	           PlayerHUD.equipmentBox.box.removeItem();
	           PlayerHUD.equipmentBox.updateItems();
	           if(!GameWindow.Menus.fullScreenStatus()) {GameWindow.stage.setScene(GameWindow.currentGame.fullImage);}
	           else{GameWindow.stage.setScene(GameWindow.currentGame.fullImage); 
	                GameWindow.stage.setFullScreen(true); }
	           }
	      
	        });
	    
	    private AnimatedButton level2Button = new AnimatedButton(
	    		this,
	  	      "Level 2", //Button text
	  	       new Pair<Double, Double>(30.0, -100.0), //Offset from center
	  	       new Pair<Double, Double>(140.0, 95.0),  //Dimensions
	  	       "file:src/main/resources/Pictures/GrayRectButtonNormal.png", //Normal image path
	  	       "file:src/main/resources/Pictures/GrayRectButtonHover.png", //Hover image path
	  	       "file:src/main/resources/Pictures/GrayRectButtonpressed.png",//Pressed image path
	  	       Optional.of("Floating Boxes"), //Announcement
	  	     new Runnable(){    //Action
	  		      
	 	    	  @Override
	 	    	  public void run() {
	 	    	  
	  	       
	  	    	   GameWindow.menuClock.stop();
	  	           GameWindow.clock.start();
	  	           GameWindow.currentGame.swapLevel(2);
	  	           PlayerHUD.weaponHud.weaponBoxes.stream().forEach(item -> item.removeItem());
	  	           PlayerHUD.equipmentBox.box.removeItem();
	  	           PlayerHUD.equipmentBox.updateItems();
	  	           if(!GameWindow.Menus.fullScreenStatus()) {GameWindow.stage.setScene(GameWindow.currentGame.fullImage);}
	  	           else{GameWindow.stage.setScene(GameWindow.currentGame.fullImage); 
	  	                GameWindow.stage.setFullScreen(true); }
	  	           }
	  	     
	  	        });
	    
	   
	    
	    private AnimatedButton exitButton = new AnimatedButton(
	    		this,
	    	 "", //Button text
	    	  new Pair<Double, Double>(300.0, 150.0), //Offset from center
	    	  new Pair<Double, Double>(150.0, 150.0),  //Dimensions
	    	  "file:src/main/resources/Pictures/SquareExitButton.png", //Normal image path
	    	  "file:src/main/resources/Pictures/SquareExitButton.png", //Hover image path
	    	  "file:src/main/resources/Pictures/SquareExitButton.png",//Pressed image path
	    	  Optional.empty(), //Announcement
	    	  new Runnable(){    //Action
	    	      
		    	  @Override
		    	  public void run() {
		    	  
	    	   
	    		  if(!GameWindow.Menus.fullScreenStatus()) { GameWindow.stage.setScene(GameWindow.Menus.mainMenu.scene); }
	    		  else{
	    			  GameWindow.stage.setScene(GameWindow.Menus.mainMenu.scene); 
	    			  GameWindow.stage.setFullScreen(true);
	    			  }
	    		
	    		 GameWindow.Menus.currentMenu = GameWindow.Menus.mainMenu;
		    	  }
	    		  
	    	    	   });
	    
	    private Group scrollPart = new Group();
	    
	    EventHandler<ScrollEvent> scrollHandler = new EventHandler<ScrollEvent>() {
		  	public void handle(ScrollEvent event) {
		  		
		  		if(event.getDeltaY()>0 && scrollPart.getLayoutY() < 0){
		    	        scrollPart.setLayoutY(scrollPart.getLayoutY() + 5);
		    	        level1Button.changeOffset(0.0, 5.0);
		    	        level2Button.changeOffset(0.0,5.0);
		    	      
		  		}else if(event.getDeltaY()<0 && scrollPart.getLayoutY() > GameWindow.stage.getHeight() - 2500){
		    	        scrollPart.setLayoutY(scrollPart.getLayoutY() - 5);
		    	        level1Button.changeOffset(0.0, -5.0);
		    	        level2Button.changeOffset(0.0, -5.0);
		    	      }

		  		
		  	}
	  };
	    
	    	    	    
	    
	    private ArrayList<AnimatedButton> buttons = new ArrayList<AnimatedButton>(); 
	    private Rectangle backGround = Helper.anySpriteFromImage("file:src/main/resources/Pictures/Lastembers.png", new Pair<Double, Double>(0.0,0.0), 800.0 ,800.0 );
	    private Rectangle scrollPartImg = Helper.anySpriteFromImage("file:src/main/resources/Pictures/LevelMenuScrollPart.png", new Pair<Double, Double>(0.0,0.0), 800.0, 2500.0);
	    public Optional<AudioClip> theme = Optional.empty();
	   
	    public LevelSelectMenu() {
	    	content = new Group();
	    	scene = new Scene(content, 800.0, 800.0);
	    	name = "Level Select Menu";
	    	
	    }
	    
	    
	    
	    public void refresh() {
	      
	      if(buttons.isEmpty()) {
	    	  buttons.add(level1Button); 
	    	  buttons.add(level2Button); 
	    	  buttons.add(exitButton);
	    	  } 
	      
	      if(content.getChildren().isEmpty()) {
	      content.getChildren().addAll(backGround, scrollPart); 
	      buttons.stream().forEach(button -> content.getChildren().add(button.fullImage()));
	      content.getChildren().addAll(header, annotationText); 
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
	      
	      if(GameWindow.currentGame.levelCompletionStatus.get(0) == false && !this.level2Button.isLocked){
	          this.level2Button.lock();

	        }
	      else if(GameWindow.currentGame.levelCompletionStatus.get(0) == true && this.level2Button.isLocked){
	          this.level2Button.unlock();
	         
	        }
	      
	      if (buttons.stream().anyMatch(button -> button.buttonAnnouncement.isPresent())) { this.annotationText.setText(buttons.stream().filter(button -> button.buttonAnnouncement.isPresent()).findAny().get().buttonAnnouncement.get());}
	      else {this.annotationText.setText("");
	    }
	   }       
	    
	    public void refreshContent() {
		 	   
		  	  content.getChildren().clear();
		   	   
		     }
	    
	    
	    
	}

   
   

   
   
  class SettingsMenu extends GameMenu {
	    
	    
	    private Text header = new Text(0,0,"Game Settings");
	    private Text annotationText = new Text(0,0,"");
	   
	    public GameMenu arrivedFrom;
	  
	    //Asetusmenun ohjainelementit
	    public GameSlider volumeSlider = new GameSlider("Volume",0.0,100.0,100.0, new Pair<Double, Double>(700.0, 10.0), new Pair<Double, Double>(-350.0, -200.0));
	    public GameCheckBox muteCheckBox = new GameCheckBox(this, "Mute sound", new Pair<Double, Double>(-150.0, 50.0));
	    public GameCheckBox devModeCheckBox = new GameCheckBox(this, "Dev Mode", new Pair<Double, Double>(-150.0, 130.0));
	    	    
	    private Rectangle checkBoxBackground = Helper.anySpriteFromImage("file:src/main/resources/Pictures/CBbackground.png", new Pair<Double, Double>(0.0,0.0), 400.0 ,1000.0 );
	    private AnimatedButton exitButton = new AnimatedButton(this,
	    	 "", //Button text
	    	  new Pair<Double, Double>(300.0, 150.0), //Offset from center
	    	  new Pair<Double, Double>(150.0, 150.0),  //Dimensions
	    	  "file:src/main/resources/Pictures/SquareExitButton.png", //Normal image path
	    	  "file:src/main/resources/Pictures/SquareExitButton.png", //Hover image path
	    	  "file:src/main/resources/Pictures/SquareExitButton.png",//Pressed image path
	    	  Optional.of("Return to Main Menu"), //Announcement
	    	  new Runnable(){    //Action
	    	      
		    	  @Override
		    	  public void run() {
		    	  
	    	   
	    		  if(!GameWindow.Menus.fullScreenStatus()) { GameWindow.stage.setScene(arrivedFrom.scene); }
	    		  else{
	    			  GameWindow.stage.setScene(arrivedFrom.scene); 
	    			  GameWindow.stage.setFullScreen(true);
	    			  }
	    		
	    		 GameWindow.Menus.currentMenu = arrivedFrom;
	    		  
		    	  }
	    	    	   });
	    	    	    
	    
	    private ArrayList<GameButton> buttons = new ArrayList<GameButton>(); 
	    private Rectangle backGround = Helper.anySpriteFromImage("file:src/main/resources/Pictures/SettingsMenuBG.png", new Pair<Double, Double>(0.0,0.0), 800.0 ,800.0 );
	    public Optional<AudioClip> theme = Optional.empty();
	    
	    public SettingsMenu() {
	    	content = new Group();
	    	scene = new Scene(content, 800.0, 800.0);
	    	name = "Settings Menu";
	    	
	    }
	    
	    
	    
	    public void refresh() {

	      if(buttons.isEmpty()) {
	    	  buttons.add(exitButton); 
	    	  buttons.add(muteCheckBox); 
	          buttons.add(devModeCheckBox); 
	    	  } 
	      
	      if(content.getChildren().isEmpty()) {
	    	  content.getChildren().addAll(backGround, header,checkBoxBackground, annotationText, volumeSlider.image); 
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
	      
	      if (buttons.stream().anyMatch(button -> button.buttonAnnouncement.isPresent())) { this.annotationText.setText(buttons.stream().filter(button -> button.buttonAnnouncement.isPresent()).findAny().get().buttonAnnouncement.get());}
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
	    
	    public void refreshContent() {
	 	   
	  	  content.getChildren().clear();
	  	   
	     }
	    
	    
	    
	    
	}

   

   
 class LoadMenu extends GameMenu {
	    
	  
	    private Text header = new Text(0,0,"Load Game");
	   
	    private Text annotationText = new Text(0,0,"");
	   
	        
	    private AnimatedButton slot1Button = new AnimatedButton(
	    		this,
	      "Slot 1", //Button text
	       new Pair<Double, Double>(-100.0, -150.0), //Offset from center
	       new Pair<Double, Double>(200.0, 100.0),  //Dimensions
	       "file:src/main/resources/Pictures/GrayRectButtonNormal.png", //Normal image path
	       "file:src/main/resources/Pictures/GrayRectButtonHover.png", //Hover image path
	       "file:src/main/resources/Pictures/GrayRectButtonpressed.png",//Pressed image path
	       Optional.empty(), //Announcement
	       new Runnable(){    //Action
	 	      
		    	  @Override
		    	  public void run() {
		    	  
	       SaveHandler.loadGame("src/main/resources/SaveFiles/Save1.DWAsave");
	       annotationText.setText("Slot 1 loaded");
		    	  }
	        });
	    
	   
	    private AnimatedButton slot2Button = new AnimatedButton(
	    		this,
	       "Slot 2", //Button text
	       new Pair<Double, Double>(-100.0, -25.0), //Offset from center
	       new Pair<Double, Double>(200.0, 100.0),  //Dimensions
	       "file:src/main/resources/Pictures/GrayRectButtonNormal.png", //Normal image path
	       "file:src/main/resources/Pictures/GrayRectButtonHover.png", //Hover image path
	       "file:src/main/resources/Pictures/GrayRectButtonpressed.png",//Pressed image path
	       Optional.empty(), //Announcement
	       new Runnable(){    //Action
	 	      
		    	  @Override
		    	  public void run() {
		    	  
	    	   SaveHandler.loadGame("src/main/resources/SaveFiles/Save2.DWAsave");
	    	   annotationText.setText("Slot 2 loaded");
		    	  }
	    	        });
	    
	    
	    private AnimatedButton slot3Button = new AnimatedButton(
	    		this,
	       "Slot 3", //Button text
	       new Pair<Double, Double>(-100.0, -100.0), //Offset from center
	       new Pair<Double, Double>(200.0, 100.0),  //Dimensions
	       "file:src/main/resources/Pictures/GrayRectButtonNormal.png", //Normal image path
	       "file:src/main/resources/Pictures/GrayRectButtonHover.png", //Hover image path
	       "file:src/main/resources/Pictures/GrayRectButtonpressed.png",//Pressed image path
	       Optional.empty(), //Announcement
	       new Runnable(){    //Action
	 	      
		    	  @Override
		    	  public void run() {
		    	  
	    	     SaveHandler.loadGame("src/main/resources/SaveFiles/Save3.DWAsave");
	    	     annotationText.setText("Slot 3 loaded");
		    	  }
	    	    	  });
	    	    
	    
	    
	    private AnimatedButton slot4Button = new AnimatedButton(
	    		this,
	       "Slot 4", //Button text
	        new Pair<Double, Double>(-100.0, 225.0), //Offset from center
	        new Pair<Double, Double>(200.0, 100.0),  //Dimensions
	    	"file:src/main/resources/Pictures/GrayRectButtonNormal.png", //Normal image path
	    	"file:src/main/resources/Pictures/GrayRectButtonHover.png", //Hover image path
	    	"file:src/main/resources/Pictures/GrayRectButtonpressed.png",//Pressed image path
	    	Optional.empty(), //Announcement
	    	new Runnable(){    //Action
	  	      
		    	  @Override
		    	  public void run() {
		    	  
	    	      SaveHandler.loadGame("src/main/resources/SaveFiles/Save4.DWAsave");
	    	      annotationText.setText("Slot 4 loaded");
		    	  }
	    	    	   });
	    	    
	    
	    private AnimatedButton exitButton = new AnimatedButton(
	    		this,
	    	 "", //Button text
	    	  new Pair<Double, Double>(300.0, 150.0), //Offset from center
	    	  new Pair<Double, Double>(150.0, 150.0),  //Dimensions
	    	  "file:src/main/resources/Pictures/SquareExitButton.png", //Normal image path
	    	  "file:src/main/resources/Pictures/SquareExitButton.png", //Hover image path
	    	  "file:src/main/resources/Pictures/SquareExitButton.png",//Pressed image path
	    	  Optional.of("Return to Main Menu"), //Announcement
	    	  new Runnable(){    //Action
	    	      
		    	  @Override
		    	  public void run() {
		    	  
	    	   
	    		  if(!GameWindow.Menus.fullScreenStatus()) { GameWindow.stage.setScene(GameWindow.Menus.mainMenu.scene); }
	    		  else{
	    			  GameWindow.stage.setScene(GameWindow.Menus.mainMenu.scene); 
	    			  GameWindow.stage.setFullScreen(true);
	    			  }
	    		
	    		 GameWindow.Menus.currentMenu = GameWindow.Menus.mainMenu;
		    	  }
	    		  
	    	    	   });
	    	    	    
	    
	    private ArrayList<AnimatedButton> buttons = new ArrayList<AnimatedButton>(); 
	    private Rectangle backGround = Helper.anySpriteFromImage("file:src/main/resources/Pictures/DeathMenuBackGround.png", new Pair<Double, Double>(0.0,0.0), 800.0 ,800.0 );
	    public Optional<AudioClip> theme = Optional.empty();
	    
	    public LoadMenu() {
	    	content = new Group();
	    	scene = new Scene(content, 800.0, 800.0);
	    	name = "Load Menu";
	    	
	    }
	    
	    
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
	      
	      if (buttons.stream().anyMatch(button -> button.buttonAnnouncement.isPresent())) { this.annotationText.setText(buttons.stream().filter(button -> button.buttonAnnouncement.isPresent()).findAny().get().buttonAnnouncement.get());}
	      else {this.annotationText.setText("");
	    }
	   }      
	    
	    public void refreshContent() {
	 	   
	  	  content.getChildren().clear();
	   	   
	     }
	    
	    
	}

    
   
 class SaveMenu extends GameMenu {
    
    
    private Text header = new Text(0,0,"Save Game");
   
    private Text annotationText = new Text(0,0,"");
   
        
    private AnimatedButton slot1Button = new AnimatedButton(
    		this,
      "Slot 1", //Button text
       new Pair<Double, Double>(-100.0, -150.0), //Offset from center
       new Pair<Double, Double>(200.0, 100.0),  //Dimensions
       "file:src/main/resources/Pictures/GrayRectButtonNormal.png", //Normal image path
       "file:src/main/resources/Pictures/GrayRectButtonHover.png", //Hover image path
       "file:src/main/resources/Pictures/GrayRectButtonpressed.png",//Pressed image path
       Optional.empty(), //Announcement
       new Runnable(){    //Action
 	      
	    	  @Override
	    	  public void run() {
	    	  
       SaveHandler.saveGame("src/main/resources/SaveFiles/Save1.DWAsave");
       annotationText.setText("Slot 1 saved");
	    	  }
        });
    
   
    private AnimatedButton slot2Button = new AnimatedButton(
    		this,
       "Slot 2", //Button text
       new Pair<Double, Double>(-100.0, -25.0), //Offset from center
       new Pair<Double, Double>(200.0, 100.0),  //Dimensions
       "file:src/main/resources/Pictures/GrayRectButtonNormal.png", //Normal image path
       "file:src/main/resources/Pictures/GrayRectButtonHover.png", //Hover image path
       "file:src/main/resources/Pictures/GrayRectButtonpressed.png",//Pressed image path
       Optional.empty(), //Announcement
       new Runnable(){    //Action
 	      
	    	  @Override
	    	  public void run() {
	    	  
    	   SaveHandler.saveGame("src/main/resources/SaveFiles/Save2.DWAsave");
    	   annotationText.setText("Slot 2 saved");
	    	  }
    	        });
    
    
    private AnimatedButton slot3Button = new AnimatedButton(
    		this,
       "Slot 3", //Button text
       new Pair<Double, Double>(-100.0, -100.0), //Offset from center
       new Pair<Double, Double>(200.0, 100.0),  //Dimensions
       "file:src/main/resources/Pictures/GrayRectButtonNormal.png", //Normal image path
       "file:src/main/resources/Pictures/GrayRectButtonHover.png", //Hover image path
       "file:src/main/resources/Pictures/GrayRectButtonpressed.png",//Pressed image path
       Optional.empty(), //Announcement
       new Runnable(){    //Action
 	      
	    	  @Override
	    	  public void run() {
	    	  
    	     SaveHandler.saveGame("src/main/resources/SaveFiles/Save3.DWAsave");
    	     annotationText.setText("Slot 3 saved");
	    	  }
    	    	  });
    	    
    
    
    private AnimatedButton slot4Button = new AnimatedButton(
    	this,
       "Slot 4", //Button text
        new Pair<Double, Double>(-100.0, 225.0), //Offset from center
        new Pair<Double, Double>(200.0, 100.0),  //Dimensions
    	"file:src/main/resources/Pictures/GrayRectButtonNormal.png", //Normal image path
    	"file:src/main/resources/Pictures/GrayRectButtonHover.png", //Hover image path
    	"file:src/main/resources/Pictures/GrayRectButtonpressed.png",//Pressed image path
    	Optional.empty(), //Announcement
    	new Runnable(){    //Action
  	      
	    	  @Override
	    	  public void run() {
	    	  
    	      SaveHandler.saveGame("src/main/resources/SaveFiles/Save4.DWAsave");
    	      annotationText.setText("Slot 4 saved");
	    	  }
    	    	   });
    	    
    
    private AnimatedButton exitButton = new AnimatedButton(
         this,
    	 "", //Button text
    	  new Pair<Double, Double>(300.0, 150.0), //Offset from center
    	  new Pair<Double, Double>(150.0, 150.0),  //Dimensions
    	  "file:src/main/resources/Pictures/SquareExitButton.png", //Normal image path
    	  "file:src/main/resources/Pictures/SquareExitButton.png", //Hover image path
    	  "file:src/main/resources/Pictures/SquareExitButton.png",//Pressed image path
    	  Optional.of("Return to Main Menu"), //Announcement
    	  new Runnable(){    //Action
    	      
	    	  @Override
	    	  public void run() {
	    	  
    	   
    		  if(!GameWindow.Menus.fullScreenStatus()) { GameWindow.stage.setScene(GameWindow.Menus.mainMenu.scene); }
    		  else{
    			  GameWindow.stage.setScene(GameWindow.Menus.mainMenu.scene); 
    			  GameWindow.stage.setFullScreen(true);
    			  }
    		
    		 GameWindow.Menus.currentMenu = GameWindow.Menus.mainMenu;
	    	  }
    		  
    	    	   });
    	    	    
    
    private ArrayList<AnimatedButton> buttons = new ArrayList<AnimatedButton>(); 
    private Rectangle backGround = Helper.anySpriteFromImage("file:src/main/resources/Pictures/DeathMenuBackGround.png", new Pair<Double, Double>(0.0,0.0), 800.0 ,800.0 );
    public Optional<AudioClip> theme = Optional.empty();
    
    public SaveMenu() {
    	content = new Group();
    	scene = new Scene(content, 800.0, 800.0);
    	name = "Save Menu";
    	
    }
    
    
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
      
      if (buttons.stream().anyMatch(button -> button.buttonAnnouncement.isPresent())) { this.annotationText.setText(buttons.stream().filter(button -> button.buttonAnnouncement.isPresent()).findAny().get().buttonAnnouncement.get());}
      else {this.annotationText.setText("");
    }
   }      
    
   public void refreshContent() {
	   
	  content.getChildren().clear();

	   
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
	
	Runnable action;
	
	
	
  private Text buttonText;
   
  public Boolean isLocked = false;         //Jos nappi on lukittu sitä ei voi painaa. Käytetään pelin tasojen yhteydessä
  
  private Node normalImg;
  private Node hoverImg;
  private Node pressedImg;
  
  Node currentImage;
  
  //Konstruktori
  public AnimatedButton(GameMenu menu, String textForButton, Pair<Double, Double> locationOffsetFromCenter, Pair<Double, Double>dimensions, String normalImgPath, String hoverImgPath, String pressedImgPath, Optional<String>announcement, Runnable action) {
	  
	  this.textForButton = textForButton;
	  this.locationOffsetFromCenter = locationOffsetFromCenter;
	  this.dimensions = dimensions;
	  this.normalImgPath = normalImgPath;
	  this.hoverImgPath = hoverImgPath;
	  this.pressedImgPath = pressedImgPath;
	  this.buttonAnnouncement = announcement;
	  this.action = action;
	  this.menu = menu;
	  
	  buttonText = new Text(0,0, textForButton);
	  buttonText.setScaleX(2.0);
	  buttonText.setScaleY(2.0);
	  buttonText.setMouseTransparent(true);
	  
	  normalImg = Helper.anySpriteFromImage(normalImgPath, new Pair<Double, Double>(0.0,0.0), dimensions.getKey(), dimensions.getValue());   //Kuvat kolmeen eri tilanteeseen
	  hoverImg = Helper.anySpriteFromImage(hoverImgPath, new Pair<Double, Double>(0.0,0.0), dimensions.getKey(), dimensions.getValue());
	  pressedImg = Helper.anySpriteFromImage(pressedImgPath, new Pair<Double, Double>(0.0,0.0), dimensions.getKey(), dimensions.getValue());
	  
	  currentImage = normalImg;
	  
	  EventHandler<MouseEvent> mouseClickHandler = new EventHandler<MouseEvent>() {
		  	public void handle(MouseEvent event) {
		  		
		  		try{
		  		
		  		   if(isLocked){
		  		     buttonAnnouncement = Optional.of("Locked");
		  		   }else{
		  		    currentImage = pressedImg;
		  		    action.run();
		  		    reset();
		  		   }
		  		 }catch(Exception e){
		  		     GameWindow.exceptionScreen(e);
		  		   }
		  	}
	  };
	  
	  EventHandler<MouseEvent> mouseEnterHandler = new EventHandler<MouseEvent>() {
		  	public void handle(MouseEvent event) {
		  		
		  		 try{
		  			 menu.refreshContent();
		  		    currentImage = hoverImg;
		  		    buttonAnnouncement = announcement ;
		  		 }catch(Exception e){
		  		      GameWindow.exceptionScreen(e);
		  		    }
		  	}
	  };
	  
	  EventHandler<MouseEvent> mouseExitHandler = new EventHandler<MouseEvent>() {
		  	public void handle(MouseEvent event) {
		  		 
		  	    try{
		  	    	menu.refreshContent();
		  	     currentImage = normalImg;
		  	     buttonAnnouncement = Optional.empty();
		  	    }catch(Exception e){
		  	      GameWindow.exceptionScreen(e);
		  	    }	
		  	}
	  };
	  
	  hoverImg.setOnMousePressed(mouseClickHandler);
	  normalImg.setOnMouseEntered(mouseEnterHandler);
	  hoverImg.setOnMouseExited(mouseExitHandler);
	    
  }
  
  
  
  
  public Group fullImage() {
	  
	  Group done = new Group();
      done.getChildren().add(currentImage);
      done.getChildren().add(buttonText);
      return done;
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
	
	
 private Slider slider;
    
 private Text text;
  
 private Text percentage;
 
 public Group image = new Group();
 
 //Konstruktori
 public GameSlider(String header, Double min, Double max, Double value, Pair<Double, Double> dimensions, Pair<Double, Double> locationOffset) {
	 
	 this.header = header;
	 this.min = min;
	 this.max = max; 
	 this.value = value;
	 this.dimensions = dimensions;
	 this.locationOffset = locationOffset;
	 
	 slider = new Slider(min, max, value);
	 slider.setPrefWidth(dimensions.getKey());
	 slider.setPrefHeight(dimensions.getValue());
	 slider.getStylesheets().addAll("file:src/main/resources/StyleSheets/SliderStyle.css");
	 
	 text = new Text(0,0,header);
	 percentage = new Text(0,0,slider.getValue() + "%");
	 
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
 private Text labelText;
 
 EventHandler<MouseEvent> mouseClickHandler = new EventHandler<MouseEvent>() {
	  	public void handle(MouseEvent event) {
	  	  
	  	  menu.refreshContent();
	  	  System.out.println("Game checkbox status: " + isSelected);
	  		if(isSelected == false) {
	  		   isSelected = true;
	  		}
	  	    else {
	  	       isSelected = false;
	  	    }
};
};
 
 //Konstruktori
 public GameCheckBox(GameMenu menu, String text, Pair<Double, Double> locationOffset) {
	 
	 this.text = text;
	 this.locationOffset = locationOffset;
	 this.menu = menu;
	 
	 labelText = new Text(0,0,text);
	 labelText.setFill(Color.WHITE);
	 
	  
	  this.buttonBase.setOnMousePressed(mouseClickHandler);
	  this.buttonMark.setOnMousePressed(mouseClickHandler);
	
	  
	  
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
	  
	 /* if(this.isSelected && !menu.content.getChildren().contains(buttonMark)) {
		  menu.content.getChildren().add(buttonMark);
	  }else if (!this.isSelected && menu.content.getChildren().contains(buttonMark)) {
		  menu.content.getChildren().remove(buttonMark); 
	  }*/
    
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
  
  public String name;
  Scene scene;
  protected Group content;
  public abstract void refresh();
  public Optional<AudioClip> theme = Optional.empty();
  
  public abstract void refreshContent();
  
}

private abstract class GameButton{
  GameMenu menu;
  public abstract void refreshLocation();
  public abstract Group fullImage();
  public  Optional<String> buttonAnnouncement = Optional.empty();
  


}

}