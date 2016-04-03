import java.awt.*;
import javax.swing.*;

class ImageButton extends JToggleButton {

  public ImageButton(String path) {
	  super(new ImageIcon(path));
	  /*ImageIcon icon = new ImageIcon(path);
	  Image img = icon.getImage();  //ImageIcon을 Image로 변환.
	  Image chimg = img.getScaledInstance(65, 65, java.awt.Image.SCALE_SMOOTH);
	  ImageIcon chicon = new ImageIcon(chimg); //Image로 ImageIcon 생성
	  setIcon(chicon);*/
  }

  public ImageButton(ImageIcon icon) {
    setIcon(icon);
    setMargin(new Insets(0, 0, 0, 0));
    setIconTextGap(0);
    setBorderPainted(false);
    setBorder(null);
    setText(null);
    setSize(icon.getImage().getWidth(null), icon.getImage().getHeight(null));
  }

}