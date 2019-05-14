
package main.java;

class Settings{
  
 // def settingMenu = Menus.SettingsMenu
  
  
  //��niasetukset
  public static boolean muteSound = false;
  
  public static Double musicVolume() { 
	 
	  if(!muteSound) {
		 return settingMenu.volumeSlider.currentValue/100.0;
	  }else {
		  
		return 0.0;
		  
	  }
	  
  }
  
  
  
  
  public static void toggleMute() {
  
	  if(muteSound == false) {
    	muteSound = true;
    }
    else muteSound = false;
  }
  
  //Kehitt�j�tila(Loputon HP, energia ja hypyt)
  
  public static boolean devMode = false;
  
  
  
  
  
  
  
}