import javax.swing.*;
import java.awt.*;

/** 색상 선택하는 패널
 * 색상을 그리드 레이아웃 내의 버튼에 설정해 받는 방식
 * */
public class ColorPalette extends JPanel{
	Color color;
	Color[] palet = {Color.RED, Color.ORANGE, Color.YELLOW, new Color(47, 157, 39), Color.BLUE, new Color(95, 0, 255), Color.BLACK, Color.WHITE,
			Color.PINK, new Color(204, 114, 61), new Color(135, 204, 0), new Color(0, 216, 255), Color.MAGENTA, new Color(255, 178, 245) ,Color.DARK_GRAY, Color.LIGHT_GRAY};
	JButton[] paletBtn = new JButton[16];
	/* 생성자 : 그리드 레이아웃으로 색상 라벨 배치하고, 리스너 등록해 색상 클릭 시 이벤트 설정 */
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

