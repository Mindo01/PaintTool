import java.awt.*;
import javax.swing.*;

/* ������ ���� ���̾�α� */
public class MJDialog extends JDialog{
	public MJDialog (JFrame frame, String title) {
		super (frame, title);
		setLayout(new FlowLayout());
		/* ���̾�α� ���� �󺧷� ���� */
		add(new JLabel("������"));
		add(new JLabel("����ȸ���б� �۷���IT�а�"));
		add(new JLabel("201334005 �����"));
		add(new JLabel("y229600@naver.com"));
		setBackground(Color.WHITE);
		setSize(200, 150);
		/* ���̾�α� ����� ������� ��ġ */
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation((screenSize.width-320)/2, (screenSize.height-560)/2);
	}
}
