import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.*;
/**
 * 현재 선택된 색깔을 디스플레이 해주는 패널
 * */
public class NowColorPalette extends JPanel{
	Color color;
	JLabel label;
	JLabel palet;
	/* 생성자 : 패널 구성 : 위에 글씨 라벨 + 아래에 색상 라벨*/
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
	/** 색상 패널의 색 설정, 등록 */
	void setColor(Color chColor) {
		palet.setBackground(chColor);
	}
	/** 색상 패널의 색 가져와 반환 */
	Color getColor()
	{
		return color;
	}
}
