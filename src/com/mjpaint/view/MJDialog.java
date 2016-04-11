package com.mjpaint.view;
import java.awt.*;
import javax.swing.*;

/** ������ ����, �̹��� ��ó ������ ����ִ� Ŀ���� ���̾�α�
 * 1. ������ ���� : ������ ����(�̸�, �б� �й�, �̸��� �ּ�)
 * 2. �̹��� ��ó ���� : iconfinder ������ ���� �̹��� ��ó �ۼ�
 *  */
public class MJDialog extends JDialog{
	JPanel panel = new JPanel();
	public MJDialog (JFrame frame, String title, int purpose) {
		super (frame, title);
		setLayout(null);
		/* ���̾�α� ������ ���� �ٸ��� ���� : 1. ������ ���� / 2. �̹��� ��ó */
		if (purpose == 1)
			copyrights();
		else
			imgCopyrights();
		/* �г� ���� ����, ��ġ ���� */
		panel.setBackground(Color.WHITE);
		panel.setOpaque(true);
		panel.setLocation(0, 0);
		add(panel);
		/* ���̾�α� ����� ������� ��ġ */
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation((screenSize.width-1000)/2, (screenSize.height-800)/2);
		/* â ũ�� ���� */
		setResizable(false);
	}
	/* ������ ���� ���̾�α� ���� : �´� ������ �� ��� */
	private void copyrights()
	{
		/* ���̾�α� ���� �󺧷� ���� */
		panel.add(new JLabel("������"));
		panel.add(new JLabel("����ȸ���б� �۷���IT�а�"));
		panel.add(new JLabel("201334005 �����"));
		panel.add(new JLabel("y229600@naver.com"));
		panel.setSize(200, 150);
		setSize(208, 150);
	}
	/* �̹���(������) ��ó ���� ���̾�α� ���� : �´� ������ �� ��� */
	private void imgCopyrights()
	{
		/* ���̾�α� ���� �󺧷� ���� */
		panel.add(new JLabel("              �̹��� ������ ��ó                "));
		panel.add(new JLabel("Sergei Kokota"));
		panel.add(new JLabel("in IconFinder"));
		panel.add(new JLabel("https://www.iconfinder.com/Zerg"));
		panel.setSize(210, 120);
		setSize(218, 120);
	}
}
