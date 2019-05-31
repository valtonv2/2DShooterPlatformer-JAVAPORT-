package main.java;

import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.application.Application;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Optional;
import java.util.concurrent.Callable;

import javafx.animation.AnimationTimer;
import javafx.scene.*;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.*;
import javafx.scene.paint.ImagePattern;
import javafx.scene.text.*;
import javafx.stage.Stage;
import javafx.scene.paint.Color.*;


public class GameWindow extends Application {
	
  
public static void main(String args[]) {
		Application.launch(args);
	}

public static Stage stage;

public static Menus Menus;
public static Optional<GameCamera> gameCamera = Optional.empty(); //GamePos-luokka laskee sijainnit kuvassa tämän suhteen. 
public static Game currentGame;
public static PlayerHUD PlayerHUD;

public static AnimationTimer clock = new AnimationTimer() {
	@Override 
	public void handle(long time) {changeThings();}
};

public static AnimationTimer menuClock = new AnimationTimer() {
	@Override 
	public void handle(long time) {changeMenus();}
};

public static AnimationTimer mapClock = new AnimationTimer() {
	@Override 
	public void handle(long time) {changeMap();}
};

//Luodaan ikkuna
public void start(Stage primaryStage) {
		

	
	primaryStage.setTitle("Don't Worry About Ammo (Java)");
	primaryStage.setWidth(800.0);
	primaryStage.setHeight(800.0);
	primaryStage.setMinWidth(800.0);
	primaryStage.setMinHeight(800.0);	
    primaryStage.setFullScreenExitHint("");
    primaryStage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
  
    
    GameWindow.stage = primaryStage;
    
    Menus = new Menus();
    
    currentGame = new Game();
    gameCamera = Optional.ofNullable(currentGame.camera);
    
    PlayerHUD = new PlayerHUD();
    
    primaryStage.setScene(Menus.mainMenu.scene);
    
    menuClock.start();
    
    primaryStage.show();
    System.out.println("Päästiin start-metodin loppuun");
	}


  public static Player player() { return currentGame.player;}

  //Tänne laitetaan jutut jotka tehdään joka tick
  public static void changeThings() {
    
    try{
    
  GameWindow.currentGame.fullImage.setCursor(Cursor.NONE);
  
  if(!currentGame.isOver){
    currentGame.imageContent.getChildren().clear();
    currentGame.camera.update();
    player().updateState();
    currentGame.imageContent.getChildren().addAll(currentGame.camera.cameraImage()); // Muuttaa fullimagen sisältöä ja näin animoi asiat
    currentGame.cleanUp();
    
    if (!currentGame.backGroundMusic.isPlaying() && !player().isSlowingTime){
      currentGame.backGroundMusic.play(Settings.musicVolume());
      currentGame.timeSlowAmbience.stop();
    }else if(currentGame.backGroundMusic.isPlaying()) currentGame.backGroundMusic.setVolume(Settings.musicVolume());
   
    if(player().isSlowingTime){
     currentGame.backGroundMusic.stop();
     if(!currentGame.timeSlowAmbience.isPlaying()) currentGame.timeSlowAmbience.play(Settings.musicVolume());
     if(currentGame.timeSlowAmbience.isPlaying()) currentGame.timeSlowAmbience.setVolume(Settings.musicVolume()); 
     }
   
    if (currentGame.time < 100000) currentGame.time += 1;
    else currentGame.time = 0;
    
    }else{
      
    clock.stop();
    menuClock.start();
    Menus.currentMenu = Menus.DeathMenu;
    if(!Menus.fullScreenStatus()) GameWindow.stage.setScene(Menus.DeathMenu.scene); 
    else{GameWindow.stage.setScene(Menus.DeathMenu.scene); GameWindow.stage.setFullScreen(true); }
     }
   
    }catch(Exception e){
 
      exceptionScreen(e);
      
    }
   
  }
  
  //Tämä metodi huolehtii menujen tilanpäivityksestä
  public static void changeMenus() {
    
    try{
    
    Menus.currentMenu.scene.setCursor(Cursor.DEFAULT);
    Menus.currentMenu.refresh();
    if(Menus.currentMenu.theme.isPresent() && !Menus.currentMenu.theme.get().isPlaying()) Menus.currentMenu.theme.get().play(Settings.musicVolume());
    if(Menus.currentMenu.theme.isPresent() && Menus.currentMenu.theme.get().isPlaying()){
      Menus.currentMenu.theme.get().setVolume(Settings.musicVolume());
     
    }
   
    }catch(Exception e){
      
     exceptionScreen(e);
     
    }
  }
  
  //Karttatilan päivitys
  public static void changeMap() {
    
    try{
    
    GameCamera cam = currentGame.camera;
    cam.location.move(cam.xSpeed, cam.ySpeed);
    cam.zoomIn(cam.zInSpeed);
    cam.zoomOut(cam.zOutSpeed);
    
    }catch(Exception e){
      
     exceptionScreen(e);

    }
       
  }
  
  //Näyttö joka näkyy jos peliä suoritettaessa tapahtuu poikkeus. Estää pelin jäätymisen.
  public static void exceptionScreen(Exception e) {

    GameWindow.clock.stop();
    GameWindow.menuClock.stop();
    GameWindow.mapClock.stop();
    
    Group windowContent = new Group();
    Scene scene = new Scene(windowContent, 800, 800);
    
    StringWriter writer = new StringWriter();
    PrintWriter pwriter = new PrintWriter(writer);
    
    e.printStackTrace(pwriter);
    
    String msg = "Something is wrong.  " + e + writer.toString();
    
    Text text = new Text(GameWindow.stage.getWidth()/2, GameWindow.stage.getHeight()/2, msg);
    Rectangle backGround =  new Rectangle(-2000, -2000, 8000, 8000);
    
    windowContent.getChildren().add(backGround);
    windowContent.getChildren().add(text);
    
    
    text.setFill(Color.RED);
    
    GameWindow.stage.setScene(scene);
    
  }
  
  
}



