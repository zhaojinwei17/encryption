package com.zjw.resource;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class R {

    public static Color encryptionOrgColor=Color.LIGHT_GRAY;
    public static Color topOrgColor=Color.DARK_GRAY;
    public static Color topActionColor=Color.GRAY;
    public static Color closePressedColor=Color.DARK_GRAY;

    public static BufferedImage close;
    public static BufferedImage encryption;
    public static BufferedImage load;
    public static BufferedImage complete;
    static {
        try {
            close= ImageIO.read(R.class.getResourceAsStream("close.png"));
            encryption= ImageIO.read(R.class.getResourceAsStream("encryption.png"));
            load= ImageIO.read(R.class.getResourceAsStream("load.png"));
            complete= ImageIO.read(R.class.getResourceAsStream("complete.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
