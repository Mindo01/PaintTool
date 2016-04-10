import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.*;
/**
 * ���� ���õ� ������ ���÷��� ���ִ� �г�
 * */
public class NowColorPalette extends JPanel{
	Color color;
	JLabel label;
	JLabel palet;
	/* ������ : �г� ���� : ���� �۾� �� + �Ʒ��� ���� ��*/
	NowColorPalette(String str, Color basic) {
		label = new JLabel(str);
		palet = new JLabel(" ");
		setLayout(new GridLayout(2, 1));
		label.setHorizontalAlignment(SwingConstants.CENTER);
		palet.setOpaque(true);
		palet.setBackground(basic);
		add(label);
		add(palet);
		setSize(60, 60);
	}
	/** ���� �г��� �� ����, ��� */
	void setColor(Color chColor) {
		palet.setBackground(chColor);
	}
	/** ���� �г��� �� ������ ��ȯ */
	Color getColor()
	{
		return color;
	}
}
