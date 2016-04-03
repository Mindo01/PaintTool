import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.*;

/*
 * ���� ���α׷� : JFrame ���
 * UI �������� ���������� �̷������, �������� �����Ǵ� Ŭ����
 * */
public class MainPaint extends JFrame {
	Container contentP;
	PaintPanel mainP;
	String[] btnName = {"����", "���찳", "��", "�׸�", "����", "�ձٳ׸�"};
	String[] path = {"resources/draw_pencil.png", "resources/draw_eraser.png", "resources/draw_line.png", "resources/draw_rectangle.png", "resources/draw_oval.png", "resources/draw_roundedrec.png"};
	ImageButton[] Tbtn = new ImageButton[6];
	ButtonGroup bg = new ButtonGroup();	//��� ��ư�� �����ִ� ��ư �׷�
	/* ������ */
	public MainPaint() {
		setTitle("���� �׸���");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		contentP = getContentPane();
		mainP = new PaintPanel();
		contentP.add(mainP, BorderLayout.CENTER);
		/* �޴� �� ���� */
		createMenu();
		/* ���̵� �޴� ���� */
		leftPanel();
		
		/* ������ ����� ���� (�⺻�� �ڹ�) */
		try {
		    UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch(Exception e) { 
		}
		
		setSize(1100, 830);
		setVisible(true);
	}
	
	void createMenu() {
		/* �޴�, �޴���, �޴������� */
		JMenuBar mb;
		JMenu fileMenu, nameMenu;
		JMenuItem newFile, save, saveAs, open;	// ���θ����, ����, �ٸ� �̸����� ����, ����
		JMenuItem nameM;	// ���� �� ����
		/* �޴��� */
		mb = new JMenuBar();	// �޴� ��
		mb.setPreferredSize(new Dimension(320, 40));
		mb.setLocation(0, 0);
		mb.setBackground(Color.DARK_GRAY);
		fileMenu = new JMenu("����");	// �޴� ���� �޴�
		nameMenu = new JMenu("����");	// ��Ÿ �޴�
		fileMenu.setForeground(Color.WHITE);
		nameMenu.setForeground(Color.WHITE);
		/* �޴� �����۵� */
		newFile = new JMenuItem("���� �����");
		save = new JMenuItem("����");
		saveAs = new JMenuItem("�ٸ� �̸����� ����");
		open = new JMenuItem("����");
		nameM = new JMenuItem("������ ����");
		/* �޴��ٿ� �޴� ������ ��� */
		fileMenu.add(newFile);
		fileMenu.addSeparator();	// �޴� ������ ���м�
		fileMenu.add(open);
		fileMenu.addSeparator();	// �޴� ������ ���м�
		fileMenu.add(save);
		fileMenu.add(saveAs);
		nameMenu.add(nameM);
		// �޴� �ٿ� �޴� ���
		mb.add(fileMenu);
		mb.add(nameMenu);
		// ����Ʈ �ҿ� �޴� �� ���
		contentP.add(mb, BorderLayout.NORTH);
	}
	/* ���� �޴� */
	void leftPanel() {
		JPanel leftP = new JPanel();
		JPanel btnP = new JPanel();
		leftP.setPreferredSize(new Dimension(160, 800));
		leftP.setBackground(Color.LIGHT_GRAY);
		leftP.setLayout(null);
		btnP.setLayout(new GridLayout(3, 2));
		for (int i=0; i<btnName.length; i++)
		{
			Tbtn[i] = new ImageButton(new ImageIcon(path[i], btnName[i]));
			Tbtn[i].setIcon(new ImageIcon(path[i]));
			Tbtn[i].setSize(new Dimension(65, 65));
			Tbtn[i].addMouseListener(new BtnListener());
			btnP.add(Tbtn[i]);
			bg.add(Tbtn[i]);
		}
		btnP.setSize(130, 195);
		btnP.setLocation(10, 30);
		leftP.add(btnP);
		contentP.add(leftP, BorderLayout.WEST);
	}
	
	class BtnListener implements MouseListener {
		@Override
		public void mouseClicked(MouseEvent e) {
			ImageButton btn = (ImageButton)e.getSource();
			/* ���� ��ư ã�Ƽ� �׸��� ��� �����ϱ� */
			for (int i = 0; i < btnName.length; i++)
			{
				if(btn.equals(Tbtn[i]))
				{
					mainP.drawM = i + 1;	//�׸��� ��� ����
					break;
				}
			}
			//�׸��� ��� Ȯ�ο� �ܼ� ���
			System.out.println("�׸��� ��� : "+mainP.drawM);
		}
		/* ������� �ʴ� �޼ҵ�� : ������� �ʾƵ� implements �߱� ������ �� ���·� �������̵� */
		public void mousePressed(MouseEvent e) { }
		public void mouseReleased(MouseEvent e) { }
		public void mouseEntered(MouseEvent e) { }
		public void mouseExited(MouseEvent e) {	}
	}
	
	/* ���� : ������ ���� �� â ����! */
	public static void main(String[] args) {
		new MainPaint();

	}

}
