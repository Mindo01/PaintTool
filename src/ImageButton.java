import java.awt.*;
import javax.swing.*;

/* �̹��� ��ư Ŭ���� : �̹��� ������ ��� ��, jar���� ���� �Ŀ��� �����ǵ��� �����ڿ��� ���� 
 * 1. path(�̹��� ������ ����� ���)
 * 2. flag(ToggleButton�� ��� ���� true : Ŭ���� ���� / false : Ŭ�� �� �� ����)
 * 		- ó�� ���α׷� ���� �� �⺻ ������ ���õǾ� �ֵ��� �����ϱ� ���� ���
 * */
class ImageButton extends JToggleButton {

  public ImageButton(String path, boolean flag) {
	  super(new ImageIcon(path), flag);
	  setIcon(new ImageIcon(getClass().getClassLoader().getResource(path)));
  }

  /*public ImageButton(ImageIcon icon) {
    setIcon(icon);
    setMargin(new Insets(0, 0, 0, 0));
    setIconTextGap(0);
    setBorderPainted(false);
    setBorder(null);
    setText(null);
    setSize(icon.getImage().getWidth(null), icon.getImage().getHeight(null));
  }*/

}