import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Objects;

public  class Type {
    /**Variables*/
    private final int ID;
    private final String name;
    private final BufferedImage icon;
    private final int iconEdge;

    /**
     * CONSTRUCTOR
     * @param n name of the Type
     */
    public Type(int id, String n) {
        ID = id;
        name = n;

        icon = loadIcon("files" + "/" + name + ".png");
        iconEdge = Objects.requireNonNull(icon).getHeight();
    } // end Type

    /**
     * @return the Icon of the Type
     */
    public Image getIcon() {
        return icon;
    } // end getIcon

    /**
     * @return the length of the Edge of the Type
     */
    public int getEdge() {
        return iconEdge;
    } // end getEdge()

    /**
     * @return the ID of the type
     */
    public int getID() {
        return ID;
    } // end getID()

//    /**
//     * @return the name for the Type
//     */
//    public String getName() {
//        return name;
//    } // end getName()

    /**
     * @param path address of the image
     * @return image from with path as its address
     */
    private BufferedImage loadIcon(String path) {
        try {
            return ImageIO.read(new File(path));
        } catch (IOException exc) {
            System.out.println("Error opening image file: " + exc.getMessage());
            return null;
        }
    } // end loadIcon()
} // end class Type