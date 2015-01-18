package com.evstudio.thefirstlottery.mobile.common;

import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

/**
 * Author  : 简洋
 * Date    : 2011-9-22
 *
 * Note    : 
 */
public class GlobalUtils 
{
	private static GlobalUtils instance;
	
	private int screenHeight;
	private int screenWidth;
    private WindowManager winmanager;
	
	public GlobalUtils(WindowManager windowManager)
	{
		instance = this;
        winmanager = windowManager;
		Display display = windowManager.getDefaultDisplay();
//		this.screenHeight = display.getHeight(); //屏幕高度
//		this.screenWidth = display.getWidth();  //屏幕宽度

        DisplayMetrics dm = new DisplayMetrics();
        display.getMetrics(dm);
        this.screenWidth = dm.widthPixels;//宽度
        this.screenHeight = dm.heightPixels ;//高度
        System.out.println( "screenWidth" + this.screenWidth );
        System.out.println( "screenHeight" + this.screenHeight );
	}
	public static GlobalUtils getInstance()
	{
		return instance;
	}
	
	
	/**
	 * 获得屏幕的高度。
	 * @return
	 */
	public int getScreenHeight() {
		return screenHeight;
	}
	
	/**
	 * 获得屏幕的宽度。
	 * @return
	 */
	public int getScreenWidth() {
		return screenWidth;
	}


}
