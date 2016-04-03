import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.*;
/*
 * 현재 선택된 색깔을 디스플레이 해주는 패널
 * */
public class NowColorPalette extends JPanel{
	Color color;
	JLabel label;
	JLabel palet;
	NowColorPalette(String str) {
		label = new JLabel(str);
		palet = new JLabel(" ");
		setLayout(new GridLayout(2, 1));
		label.setHorizontalAlignment(SwingConstants.CENTER);
		palet.setOpaque(true);
		palet.setBackground(Color.BLACK);
		add(label);
		add(palet);
		setSize(70, 70);
	}
	void setColor(Color chColor) {
		palet.setBackground(chColor);
	}
	Color getColor()
	{
		return color;
	}
}
