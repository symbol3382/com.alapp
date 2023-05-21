package alapp.service;

import java.awt.Font;
import java.awt.FontFormatException;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;

public class AppService {

	public static Font appFont;

	public static Font getAppFont(float size, int fontType) {
		try {
			if (appFont == null) {
				String path = "assets/fonts/Helvetica.ttf";
				AppService.appFont = Font.createFont(Font.TRUETYPE_FONT,
						new BufferedInputStream(new FileInputStream(path)));
			}
			return appFont.deriveFont(fontType, size);
		} catch (FontFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println(e);
			e.printStackTrace();
		}
		return null;
	}
	
	public static Font getAppFont(int fontType) {
		return AppService.getAppFont(12f, fontType);
	}
	
	public static Font getAppFont(float size) {
		return AppService.getAppFont(size, Font.PLAIN);
	}
	
	public static Font getAppFont() {
		return AppService.getAppFont(12f, Font.PLAIN);
	}
}
