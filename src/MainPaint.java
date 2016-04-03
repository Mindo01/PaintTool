import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.net.URL;

import javax.swing.*;

/*
 * ���� ���α׷� : JFrame ���
 * UI �������� ���������� �̷������, �������� �����Ǵ� Ŭ����
 * */
public class MainPaint extends JFrame {
	Container contentP;
	JPanel mainP;
	PaintPanel drawP;
	String[] btnName = {"����", "���찳", "��", "�׸�", "����", "�ձٳ׸�"};
	String[] path = {"draw_pencil.png", "draw_eraser.png", "draw_line.png", "draw_rectangle.png", "draw_oval.png", "draw_roundedrec.png"};
	ImageButton[] Tbtn = new ImageButton[6];
	ButtonGroup bg = new ButtonGroup();	//��� ��ư�� �����ִ� ��ư �׷�
	/* ������ */
	public MainPaint() {
		setTitle("���� �׸���");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		/* ȭ�� �߾ӿ� ���� */
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation((screenSize.width-1100)/2, (screenSize.height-830)/2);
		contentP = getContentPane();
		mainP = new JPanel();
		mainP.setLayout(new BorderLayout());
		drawP = new PaintPanel();
		/* �޴� �� ���� */
		createMenu();
		/* ���̵� �޴� ���� */
		leftPanel();
		/* ���� �޴� ���� */
		colorPanel();
		mainP.add(drawP, BorderLayout.CENTER);
		contentP.add(mainP, BorderLayout.CENTER);
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
		/* ������ ���� ���̾�α� */
		MJDialog dialog = new MJDialog(this,"������ ����");
		/* ������ ���� Ŭ�� ��, ���̾�α� â���� �ȳ� */
		nameM.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dialog.setVisible(true);	// ���̾�α� â ����
			}
		});
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
		/* ���� �޴� �г� */
		JPanel leftP = new JPanel();
		/* �޴� �г� ���� ��ġ�� �г� */
		JPanel btnP = new JPanel();
		
		ImageButton fill = new ImageButton("draw_emp.png", false);
		leftP.setPreferredSize(new Dimension(160, 800));
		leftP.setBackground(Color.LIGHT_GRAY);
		leftP.setLayout(null);
		/* �׸��� ���� ��ư�� : �����, ���찳, ����, �׸�, ���׶��, �ձٳ׸� */
		btnP.setLayout(new GridLayout(3, 2));
		for (int i=0; i<btnName.length; i++)
		{
			Tbtn[i] = new ImageButton(path[i], i==0?true:false);	//�⺻ ������ ���������
			Tbtn[i].setSize(new Dimension(65, 65));
			Tbtn[i].addMouseListener(new BtnListener());
			btnP.add(Tbtn[i]);
			bg.add(Tbtn[i]);
		}
		btnP.setSize(130, 195);
		btnP.setLocation(15, 30);
		/* ä��� ��ư */
		//fill.setSelectedIcon(new ImageIcon("draw_fill.png"));
		fill.setSelectedIcon(new ImageIcon(getClass().getClassLoader().getResource("draw_fill.png")));
		fill.setSize(65, 65);
		fill.setLocation(47, 300);
		fill.addActionListener(new ActionListener() {		
			@Override	/* ä��� ��� ���� : true or false ����Ʈ�гο� �����ϱ� ���� fill �ʵ� ���� */
			public void actionPerformed(ActionEvent e) {
				drawP.shape.fill = !drawP.shape.fill;	// t->f / f->t
				System.out.println("fill : "+drawP.shape.fill);
			}
		});
		leftP.add(btnP);
		leftP.add(fill);
		mainP.add(leftP, BorderLayout.WEST);
	}
	/* ���� ���� ���� */
	void colorPanel() {
		JToolBar bar = new JToolBar("ColorMenu");
		JButton newBtn = new JButton("New");
		NowColorPalette nowColor = new NowColorPalette("���� ��");
		ColorPalette colorP = new ColorPalette();
		bar.setLayout(null);
		bar.setPreferredSize(new Dimension(800, 90));
		newBtn.setSize(60, 60);
		newBtn.setLocation(20, 15); // ���� : --, |
		newBtn.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				drawP.init();
			}
		});
		nowColor.setSize(60, 60);
		nowColor.setLocation(90, 15);
		colorP.setSize(240, 60);
		colorP.setLocation(170, 15);
		for (int i = 0; i < colorP.paletBtn.length; i++)
		{
			colorP.paletBtn[i].addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					JButton btn = (JButton)e.getSource();
					nowColor.setColor(btn.getBackground());
					drawP.shape.setColor(btn.getBackground());
				}
			});
		}
		bar.add(newBtn);
		bar.addSeparator();
		bar.add(nowColor);
		bar.addSeparator();
		bar.add(colorP);
		mainP.add(bar, BorderLayout.NORTH);
	}
	/* �׸��� ���� ���� ��ư�� ���� ������ */
	class BtnListener implements MouseListener {
		@Override
		public void mouseClicked(MouseEvent e) {
			ImageButton btn = (ImageButton)e.getSource();
			/* ���� ��ư ã�Ƽ� �׸��� ��� �����ϱ� */
			for (int i = 0; i < btnName.length; i++)
			{
				if(btn.equals(Tbtn[i]))
				{
					drawP.drawM = i + 1;	//�׸��� ��� ����
					break;
				}
			}
			//�׸��� ��� Ȯ�ο� �ܼ� ���
			System.out.println("�׸��� ��� : "+drawP.drawM);
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
