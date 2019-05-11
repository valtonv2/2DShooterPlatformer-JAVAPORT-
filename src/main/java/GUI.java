package main.java;

import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.application.Application;

import java.util.Optional;

import javafx.animation.AnimationTimer;
import javafx.scene.*;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.*;
import javafx.scene.paint.ImagePattern;
import javafx.scene.text.*;
import javafx.stage.Stage;
import javafx.scene.paint.Color.*;


class GameWindow extends Application {
  
public static void main() {
		launch();
	}
	
public Game currentGame = new Game();
public GameCamera gameCamera;







public static AnimationTimer clock = new AnimationTimer(changeThings);
public static AnimationTimer mapClock = new AnimationTimer(changeMap);		 
public static AnimationTimer menuClock = AnimationTimer(changeMenus);

//Luodaan ikkuna
public void start(Stage primaryStage) {
		
	Optional<GameCamera> gameCamera = Optional.empty(); //GamePos-luokka laskee sijainnit kuvassa tämän suhteen. 
	
	primaryStage.setTitle("Don't Worry About Ammo (Java)");
	primaryStage.setWidth(800.0);
	primaryStage.setHeight(800.0);
	primaryStage.setMinWidth(800.0);
	primaryStage.setMinHeight(800.0);	
    primaryStage.setFullScreenExitHint("");
    primaryStage.setFullScreenExitKeyCombination(KeyCombination("Jee"));
    primaryStage.setScene(Menus.MainMenu.scene());
    

    menuClock.start();
    this.gameCamera = Some(currentGame.camera);
    
		
		
		
		
		
		
	}







//NEW GAME

  PlayerHUD;
  public Player player() { return currentGame.player;};

 
  //Tänne laitetaan jutut jotka tehdään joka tick
  def changeThings(time:Long):Unit = {
    
    try{
    
   GameWindow.currentGame.fullImage.cursor.value_=(Cursor.NONE)
  if(!currentGame.isOver){
    
    currentGame.camera.update
    player.updateState
    currentGame.fullImage.content = currentGame.camera.cameraImage // Muuttaa fullimagen sisältöä ja näin animoi asiat
    currentGame.cleanUp
    
    if (!currentGame.backGroundMusic.isPlaying() && !player.isSlowingTime){
      currentGame.backGroundMusic.play(Settings.musicVolume)
      currentGame.timeSlowAmbience.stop()
    }else if(currentGame.backGroundMusic.isPlaying) currentGame.backGroundMusic.volume = Settings.musicVolume
   
    if(player.isSlowingTime){
     currentGame.backGroundMusic.stop()
     if(!currentGame.timeSlowAmbience.isPlaying()) currentGame.timeSlowAmbience.play(Settings.musicVolume)
     if(currentGame.timeSlowAmbience.isPlaying) currentGame.timeSlowAmbience.volume = Settings.musicVolume
     }
   
    if (currentGame.time < 100000) currentGame.time += 1
    else currentGame.time = 0
    
    }else{
      
    this.clock.stop()
    this.menuClock.start()
    Menus.currentMenu = Menus.DeathMenu
    if(!Menus.fullScreenStatus) this.stage.scene = Menus.DeathMenu.scene 
    else{GameWindow.stage.scene = Menus.DeathMenu.scene; GameWindow.stage.setFullScreen(true) }
     }
   
    }catch{
 
      case e:Exception => exceptionScreen("Something is wrong. + \n" + e)
      case _ :Throwable => exceptionScreen("Something is wrong.")
      
    }
   
  }
  //Tämä metodi huolehtii menujen tilanpäivityksestä
  def changeMenus(time:Long) = {
    
    try{
    
    Menus.currentMenu.scene.cursor.value_=(Cursor.Default)
    Menus.currentMenu.refresh
    if(Menus.currentMenu.theme.isDefined && !Menus.currentMenu.theme.get.isPlaying()) Menus.currentMenu.theme.get.play(Settings.musicVolume)
    if(Menus.currentMenu.theme.isDefined && Menus.currentMenu.theme.get.isPlaying()){
      Menus.currentMenu.theme.get.setVolume(Settings.musicVolume) 
     
    }
   
    }catch{
      case e:Exception => exceptionScreen("Something is wrong. + \n" + e)
      case _ :Throwable => exceptionScreen("Something is wrong.")
    }
  }
  
  //Karttatilan päivitys
  def changeMap(time:Long) ={
    
    try{
    
    val cam = this.currentGame.camera
    cam.location.move(cam.xSpeed, cam.ySpeed)
    cam.zoomIn(cam.zInSpeed)
    cam.zoomOut(cam.zOutSpeed)
    
    }catch{
      
      case e:Exception => exceptionScreen("Something is wrong. + \n" + e)
      case _ :Throwable => exceptionScreen("Something is wrong.")
    }
      
    
    
  }
  
  //Näyttö joka näkyy jos peliä suoritettaessa tapahtuu poikkeus. Estää pelin jäätymisen.
  def exceptionScreen(msg:String):Unit = {
    println("Moving to exScreen")
    this.clock.stop()
    this.menuClock.stop()
    this.mapClock.stop()
    val scene = new Scene
    val text = new Text(GameWindow.stage.width.toDouble/2, GameWindow.stage.height.toDouble/2, msg)
    val backGround =  new Rectangle{
          
          fill = Gray
          width = 8000
          height = 8000
          x = -2000
          y = -2000
          
        }
    
    scene.content = (List[Node](backGround, text))
    text.setFill(Red)
    
    GameWindow.stage.scene = scene
    
  }
  
  //Luodaan kello jonka tikittäessä asioita muutetaan. Pelin main loop.
  //Ottaa parametrikseen funktion changeThings, joka sisältää muutokset.
  val clock = AnimationTimer(changeThings)
  
  val mapClock = AnimationTimer(changeMap)
 
  //Kuin ylempi clock mutta päivittää pelin menuja
  val menuClock = AnimationTimer(changeMenus)
  menuClock.start
  
}

