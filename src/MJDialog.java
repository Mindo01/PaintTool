import java.awt.*;
import javax.swing.*;

/* 만든이 정보 다이얼로그 */
public class MJDialog extends JDialog{
	public MJDialog (JFrame frame, String title) {
		super (frame, title);
		setLayout(new FlowLayout());
		/* 다이얼로그 내용 라벨로 나열 */
		add(new JLabel("만든이"));
		add(new JLabel("성공회대학교 글로컬IT학과"));
		add(new JLabel("201334005 김민주"));
		add(new JLabel("y229600@naver.com"));
		setBackground(Color.WHITE);
		setSize(200, 150);
		/* 다이얼로그 띄워줄 윈도우상 위치 */
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation((screenSize.width-320)/2, (screenSize.height-560)/2);
	}
}
