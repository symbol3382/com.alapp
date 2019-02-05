package alapp;

import java.awt.Color;

public class UI {
	private static Color wallColor;
	private static Color textColor;
	private static Color componentColor;
	private static String setMode;
	private static Color themeColor;
	public static String changeButton() {
		if(setMode.equals("Dark Mode")) {
			UI.darkTheme();
		}else {
			UI.lightTheme();
		}
		return setMode;
	}
	
	public static void lightTheme() {
		wallColor = Color.WHITE;
		textColor = Color.DARK_GRAY;
		componentColor = Color.WHITE;
		setMode = "Dark Mode";
		themeColor = new Color(40, 180, 99);
	}
	public static void darkTheme() {
		wallColor = Color.BLACK;
		textColor = Color.WHITE;
		componentColor = Color.DARK_GRAY;
		setMode = "Light Mode";
	}
	public static Color getWallColor() {
		return wallColor;
	}
	public static Color getTextColor() {
		return textColor;
	}
	public static Color getComponentColor() {
		return componentColor;
	}
	public static String getSetMode() {
		return setMode;
	}
	public static Color getThemeColor() {
		return themeColor;
	}
}