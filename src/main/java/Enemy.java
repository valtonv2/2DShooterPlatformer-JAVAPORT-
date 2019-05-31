package main.java;

import java.util.Optional;


abstract class Enemy extends Actor{
  

Double HP;
Double energy = 0.0;
String currentAction;
Boolean isActive;
Double ySpeed = 0.0;
Double xSpeed = 0.0;
Boolean isDead() { return this.HP<=0;}
public abstract void update();

public void addSpeedModifier(Double modifier) {
    
     this.xSpeed = modifier * this.xSpeed;
     this.ySpeed = modifier * this.ySpeed;
  }
  
}

