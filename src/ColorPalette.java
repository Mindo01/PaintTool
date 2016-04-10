import javax.swing.*;
import java.awt.*;

/** ���� �����ϴ� �г�
 * ������ �׸��� ���̾ƿ� ���� ��ư�� ������ �޴� ���
 * */
public class ColorPalette extends JPanel{
	Color color;
	Color[] palet = {Color.RED, Color.ORANGE, Color.YELLOW, new Color(47, 157, 39), Color.BLUE, new Color(95, 0, 255), Color.BLACK, Color.WHITE,
			Color.PINK, new Color(204, 114, 61), new Color(135, 204, 0), new Color(0, 216, 255), Color.MAGENTA, new Color(255, 178, 245) ,Color.DARK_GRAY, Color.LIGHT_GRAY};
	JButton[] paletBtn = new JButton[16];
	/* ������ : �׸��� ���̾ƿ����� ���� �� ��ġ�ϰ�, ������ ����� ���� Ŭ�� �� �̺�Ʈ ���� */
	ColorPalette () {
		setLayout(new GridLayout(2, 8));
		for (int i = 0; i < paletBtn.length; i++)
		{
			paletBtn[i] = new JButton();
			paletBtn[i].setBackground(palet[i]);
			add(paletBtn[i]);
		}
		setSize(70, 280);
	}
	
}

