package com.mjpaint.controller;
import java.awt.*;
import java.awt.event.*;
import java.net.URL;

import javax.swing.*;

import com.mjpaint.model.ImageButton;
import com.mjpaint.view.ColorPalette;
import com.mjpaint.view.MJDialog;
import com.mjpaint.view.NowColorPalette;
import com.mjpaint.view.PaintPanel;

/**
 * ���� ���α׷� : JFrame ���
 * UI �������� ���������� �̷������, �������� �����Ǵ� Ŭ����
 * 1. ������ ���� �� ������Ʈ ����
 * 2. ���� �г�, ����, �޴��� ����
 * 3. PaintPanel Ŭ������ ��ü�� drawP ����
 * 4. ���� �����ϴ� ��ư �̺�Ʈ ���
 * */
public class MainPaint extends JFrame {
	Container contentP;	// ���� �����̳ʸ� �޾� ���⿡ ������Ʈ ����
	JPanel mainP;		// ���� �г�(��ü ������Ʈ�� ���⿡ �ٴ� ���� ���� �г�)
	PaintPanel drawP;	// ��ο� �г�(�׸��� �׷����� �г�)
	
	/* �׸��� ���� ��ư �迭 : �� 12���� ��ư�� �ݺ����� ����� ���� ����, ����� �� �ֵ��� �Ѵ� */
	ImageButton[] Tbtn = new ImageButton[12];
	ButtonGroup bg = new ButtonGroup();	//(���� Tbtn[]��)��� ��ư�� �����ִ� ��ư �׷� : �� �� �� ���� Ŭ���� �ǵ��� �ڵ� ������
	/* �׸��� ���� ��ư �̸��� �迭 */
	String[] btnName = {"����", "��", "������", "���찳", "��", "�簢��", "��", "�ձٻ簢��", "�ﰢ��", "������", "������", "��"};
	/* �׸��� ���� ��ư�� �̹��� ��� �迭 */
	String[] path = {"draw_pencil.png", "draw_brush.png", "draw_highlight.png", "draw_eraser.png", "draw_line.png", "draw_rectangle.png", "draw_oval.png", "draw_roundedrec.png", "draw_triangle.png", "draw_penta.png", "draw_hexa.png", "draw_star.png"};
	/* �� ���� ����, �������� ��Ÿ���� �迭 */
	String[] strSize = {"�� ���� 1", "�� ���� 5", "�� ���� 10", "�� ���� 25", "�� ���� 40", "�� ���� 60"};
	int[] strValue = {1, 5, 10, 25, 40, 60};
	/* �� ���� ������ ��Ÿ���� �迭 */
	String[] strType = {"�Ǽ�", "����"};
	
	/* PaintPanel ������ �����ϰ� �Ǵ� ��ü�� -> static ���� ������ ���ٰ����ϵ��� �Ѵ� */
	public static JComboBox strokeValue;		// �� ���⸦ ��Ÿ���� JComboBox ��ü
	public static NowColorPalette nowColor;		// ���� ���� ��Ÿ���� ��ü(JPanel�� Ŀ���Ұ�ü)
	public static NowColorPalette backColor;	// ��� ���� ��Ÿ���� ��ü(JPanel�� Ŀ���Ұ�ü)
	public static ImageButton spoidBtn;			// ���ٿ� ��ϵ� �����̵� ��ư ��ü
	/* ������ */
	public MainPaint() {
		setTitle("���� �׸��� - ���� ����.png");	// �⺻ Ÿ��Ʋ
		/* ����ǥ���� �ݱ� ��ư�� default�� �ƹ��۵� ���ϴ� ������ �����ϰ�,
		 * windowListener�� windowClosing�� �̿���, 
		 * ���� ������ ���� ��� �����ϱ� ���̾�α׸� ���� ������� ������.
		 * (�׳� �ݱ� ��ư�̳� ���� ���̾�α� ���¿��� ��Ҹ� ���� ��, */
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				JFrame frame = (JFrame)e.getWindow();
				/* ���� ������ ���� ���� Ȯ�� â�� ����� */
				if (drawP.changedFile == true)
				{
					/* ���� ���� ������ ���� ����� */
					JLabel message = new JLabel();
					message.setText("���� ������ �����Ͻðڽ��ϱ�?");
					int ret = JOptionPane.showConfirmDialog(null, message, "���� �� Ȯ��", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
					/* 1. ���� YES(Ȯ��) ���� ��� */
					if (ret == JOptionPane.YES_OPTION)
					{
						String str = drawP.saveAs();
						if (str == null)
							return ;
					}
					/* 2. ���� CANCEL(���) �Ǵ� â ���� ��ư ���� ��� */
					if (ret == JOptionPane.CANCEL_OPTION || ret == JOptionPane.CLOSED_OPTION)
					{
						return ;
					}
				}
				/* ���α׷� ���� ���� */
				System.exit(0);
			}
		});
		/* Ÿ��Ʋ�� ������ ���� */
		URL imageURL = MainPaint.class.getClassLoader().getResource("draw_colors.png");
		ImageIcon img = new ImageIcon(imageURL);
		this.setIconImage(img.getImage());
		/* �������� �����Ǵ� ��ġ ���� : ȭ�� �߾ӿ� ���� */
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation((screenSize.width-1100)/2, (screenSize.height-830)/2);
		/* ����Ʈ��, �����г�, ��ο� �г� ��ü ����*/
		contentP = getContentPane();
		mainP = new JPanel();
		mainP.setLayout(new BorderLayout());	//���̾ƿ� ����
		drawP = new PaintPanel(null);
		/* �޴� �� ���� */
		createMenu();
		/* ���̵� �޴� ���� */
		leftPanel();
		/* ���� �޴� ���� */
		toolBarPanel();
		/* paintPanel �߰� : ���������� �׸� �׸��� ���� */
		mainP.add(drawP, BorderLayout.CENTER);
		contentP.add(mainP, BorderLayout.CENTER);
		/* ������ ����� ���� (�⺻�� �ڹ�) */
		try {
		    UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch(Exception e) { 
		}
		/* ������ ũ�� ���� */
		setSize(1100, 830);
		setVisible(true);
	}
	/* ���ʿ� ��ġ�� �޴� �����ϴ� �޼ҵ� */
	void createMenu() {
		/* �޴�, �޴���, �޴������� */
		JMenuBar mb;
		JMenu fileMenu, editMenu, helpMenu;	// �޴��ٿ� �����Ǵ� �޴��� : ����, ����, ����
		JMenuItem newFile, save, saveAs, open;	// ���θ����, ����, �ٸ� �̸����� ����, ����
		JMenuItem select, copy, cut, paste;		// ���� ���, ����, �ڸ���, �ٿ��ֱ�
		JMenuItem nameM, iconM;		// ���� �� ����, ������ ��ó ����
		/* �޴��� */
		mb = new JMenuBar();	// �޴� ��
		mb.setPreferredSize(new Dimension(320, 45));
		mb.setLocation(0, 0);
		mb.setBackground(Color.DARK_GRAY);
		/* �޴��ٿ� �����Ǵ� �޴� ��ü�� ���� */
		fileMenu = new JMenu("  ����");	// ���� �޴�
		editMenu = new JMenu("  ����");	// ���� �޴�
		helpMenu = new JMenu("  ����");	// ��Ÿ �޴�
		/* ũ��, �۾� �� ���� */
		fileMenu.setForeground(Color.WHITE);
		fileMenu.setPreferredSize(new Dimension(50, 45));
		editMenu.setForeground(Color.WHITE);
		editMenu.setPreferredSize(new Dimension(50, 45));
		helpMenu.setForeground(Color.WHITE);
		helpMenu.setPreferredSize(new Dimension(50, 45));
		/* �޴� �����۵� ���� */
		// ���� ���� ������
		newFile = new JMenuItem("���� �����");
		save = new JMenuItem("����");
		saveAs = new JMenuItem("�ٸ� �̸����� ����");
		open = new JMenuItem("����");
		// ���� ���� ������ ����Ű ����
		newFile.setAccelerator(KeyStroke.getKeyStroke('N', InputEvent.CTRL_MASK));
		save.setAccelerator(KeyStroke.getKeyStroke('S', InputEvent.CTRL_MASK));
		saveAs.setAccelerator(KeyStroke.getKeyStroke('S', InputEvent.SHIFT_MASK + InputEvent.CTRL_MASK));
		open.setAccelerator(KeyStroke.getKeyStroke('G', InputEvent.CTRL_MASK));
		// ���� ���� ������
		select = new JMenuItem("����");
		copy = new JMenuItem("����");
		cut = new JMenuItem("�ڸ���");
		paste = new JMenuItem("�ٿ��ֱ�");
		// ���� ���� ������ ����Ű ����
		select.setAccelerator(KeyStroke.getKeyStroke("S"));
		copy.setAccelerator(KeyStroke.getKeyStroke('C', InputEvent.CTRL_MASK));
		cut.setAccelerator(KeyStroke.getKeyStroke('X', InputEvent.CTRL_MASK));
		paste.setAccelerator(KeyStroke.getKeyStroke('V', InputEvent.CTRL_MASK));
		// ���� ���� ������
		nameM = new JMenuItem("������ ����");
		iconM = new JMenuItem("������ ��ó");
		/* �޴� �����ۿ� ���� ������ ��� */
		// 1) ���� �����
		newFile.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				/* ���� �ִ� �̹��� �����ϴ��� Ȯ�� */
				// ���̾�α�.... �߰��ϱ�
				/* PaintPanel Ŭ���� ���� init() �޼ҵ带 ȣ�� */
				drawP.init(null);
				setTitle("���� �׸��� - ���� ����.png");
			}
		});
		// 2) ����
		save.addActionListener(new ActionListener() {		
			@Override
			public void actionPerformed(ActionEvent e) {
				String title = drawP.save();
				/* Ÿ��Ʋ�� ��� ������ ���� �̸� ���� */
				if (title != null)
					setTitle("���� �׸��� - "+title);
			}
		});
		// 3) �ٸ��̸����� ����
		saveAs.addActionListener(new ActionListener() {		
			@Override
			public void actionPerformed(ActionEvent e) {
				String title = drawP.saveAs();
				/* Ÿ��Ʋ�� ��� ������ ���� �̸� ���� */
				if (title != null)
					setTitle("���� �׸��� - "+title);
			}
		});
		// 4) ����
		open.addActionListener(new ActionListener() {		
			@Override
			public void actionPerformed(ActionEvent e) {
				String title = drawP.open();
				/* Ÿ��Ʋ�� ��� �� ���� �̸� ���� */
				if (title == null)
					title = "���� ����.png";
				setTitle("���� �׸��� - "+title);
				
			}
		});
		/* ���� �޴� �����۵� ������ ��� */
		// 1) ���� ���
		select.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				drawP.drawM = 0;	//���� ���� ����
			}
		});
		// 2) �����ϱ�
		copy.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				drawP.copy();	//���� ��忡�� ������ ���� ����
			}
		});
		// 3) �ڸ���
		cut.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				drawP.cut();	//���� ��忡�� ������ ���� �ڸ���
			}
		});
		// 4) �ٿ��ֱ�
		paste.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				drawP.paste();	//���� ��忡�� ������ ������ �ٿ��ֱ�
			}
		});
		/* ������ ���� ���̾�α� */
		MJDialog myCopy = new MJDialog(this,"������ ����", 1);
		MJDialog iconCopy = new MJDialog(this, "������ ��ó", 2);
		/* ������ ���� Ŭ�� ��, ���̾�α� â���� �ȳ� */
		nameM.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				myCopy.setVisible(true);	// ���̾�α� â ����
			}
		});
		iconM.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				iconCopy.setVisible(true);
			}
		});
		/* �޴��ٿ� �޴� ������ ��� */
		fileMenu.add(newFile);
		fileMenu.addSeparator();	// �޴� ������ ���м�
		fileMenu.add(open);
		fileMenu.addSeparator();	// �޴� ������ ���м�
		fileMenu.add(save);
		fileMenu.add(saveAs);
		editMenu.add(select);
		editMenu.add(copy);
		editMenu.add(cut);
		editMenu.add(paste);
		helpMenu.add(nameM);
		helpMenu.add(iconM);
		// �޴� �ٿ� �޴� ���
		mb.add(fileMenu);
		mb.add(editMenu);
		mb.add(helpMenu);
		// ����Ʈ �ҿ� �޴� �� ���
		contentP.add(mb, BorderLayout.NORTH);
	}
	/* ���� �޴� �г� ���� */
	void leftPanel() {
		/* ���� �޴� �г� */
		JPanel leftP = new JPanel();
		/* �޴� �г� ���� ��ġ�� �г� */
		JPanel btnP = new JPanel();
		JLabel fillLabel = new JLabel("ä���");
		ImageButton fill = new ImageButton("draw_emp.png", false);
		leftP.setPreferredSize(new Dimension(160, 800));
		leftP.setBackground(Color.LIGHT_GRAY);
		leftP.setLayout(null);
		/* �׸��� ���� ��ư�� : �����, ���찳, ����, �׸�, ���׶��, �ձٳ׸� */
		/* ��ư �г��� �׸��� ���̾ƿ����� �����ϰ� ���� ��ư���� �� �гο� ��� */
		btnP.setLayout(new GridLayout(6, 2));
		for (int i=0; i<btnName.length; i++)
		{
			Tbtn[i] = new ImageButton(path[i], i==0?true:false);	//�⺻ ������ �����(PENCIL)����
			Tbtn[i].setSize(new Dimension(65, 65));					//�� ��ư ������ ����
			Tbtn[i].addMouseListener(new BtnListener());			//��ư�� ���� ������ ���
			btnP.add(Tbtn[i]);	//�гο� ��ư ������Ʈ ����
			bg.add(Tbtn[i]);	//��ư �׷쿡 ���
		}
		btnP.setSize(130, 390);
		btnP.setLocation(15, 30);
		/* ä��� ��ư */
		fillLabel.setSize(50, 50);
		fillLabel.setLocation(54, 430);
		fillLabel.setHorizontalAlignment(SwingConstants.CENTER);	// ä��� �۾��� ��� ��������
		// ä��� ��ư ������ ��� : jar���Ͽ����� ��밡���ϵ��� classLoader ���
		fill.setSelectedIcon(new ImageIcon(getClass().getClassLoader().getResource("draw_fills.png")));
		fill.setSize(65, 65);
		fill.setLocation(47, 465);
		fill.addActionListener(new ActionListener() {	
			/* ä��� ��� ���� : true or false ����Ʈ�гο� �����ϱ� ���� fill �ʵ� ���� */
			@Override	
			public void actionPerformed(ActionEvent e) {
				drawP.shape.fill = !drawP.shape.fill;	// t->f / f->t
				System.out.println("fill : "+drawP.shape.fill);
			}
		});
		/* leftP �гο� ���������� ������Ʈ ��� */
		leftP.add(btnP);
		leftP.add(fillLabel);
		leftP.add(fill);
		/* leftP�� mainP�� ���ʿ� ���� */
		mainP.add(leftP, BorderLayout.WEST);
	}
	/* �޴� �Ʒ��� �پ��ִ� ���� ���� : ���θ����, ���� ����, �� ���� ���� ��. */
	void toolBarPanel() {
		/* ���ٿ� ������ ������Ʈ�� ��ü ���� (���ʴ��)
		 * 1. ���� ����	newBtn
		 * 2. �� ����		strokeValue
		 * 3. �� ����		strokeType
		 * 4. ���� ��		nowColor
		 * 5. ��� ��		backColor
		 * 6. ���� 10�� ���� ������ ColorPalette Ŀ���� ��ü	colorP
		 * 7. ColorChooser �����ִ� �̹��� ��ư			moreColor
		 * 8. ���콺�� �÷��� ���� �ȳ��ϴ� �����̵� ��ư 			spoidBtn
		 * */
		JToolBar bar = new JToolBar("ColorMenu");
		JButton newBtn = new JButton("New");
		strokeValue = new JComboBox();
		JComboBox strokeType = new JComboBox();
		nowColor = new NowColorPalette("���� ��", Color.BLACK);
		backColor = new NowColorPalette("��� ��", Color.WHITE);
		ColorPalette colorP = new ColorPalette();
		JButton moreColor = new JButton(new ImageIcon(getClass().getClassLoader().getResource("draw_colors.png")));
		spoidBtn = new ImageButton("draw_spoid.png", false);
		
		/* 0. ��ġ������ ���� ��, ���� ���� ������Ʈ ���� ��ġ�� ��ġ�ϱ� */
		bar.setLayout(null);
		bar.setPreferredSize(new Dimension(800, 90));
		/* 1. ���� ����� ��ư : �׸��� �г� �ʱ�ȭ*/
		newBtn.setSize(60, 60);
		newBtn.setLocation(20, 15); // ���� : --, |
		newBtn.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				/* PaintPanel Ŭ������ init �޼ҵ� ����� �г� ���� */
				drawP.init(null);
				setTitle("���� �׸��� - ���� ����.png");
			}
		});
		/* 2. �� ���� ���� */
		strokeValue.setSize(90, 35);
		strokeValue.setLocation(100, 27);
		for (int i = 0; i < strSize.length; i++)
			strokeValue.addItem(strSize[i]);
		strokeValue.addActionListener(new ActionListener() {		
			@Override
			public void actionPerformed(ActionEvent e) {
				JComboBox cb = (JComboBox)e.getSource();
				String item = (String)cb.getSelectedItem();
				for (int i = 0; i < strSize.length; i++)
					if (item.equals(strSize[i]))
							drawP.shape.setStroke(strValue[i], drawP.drawM);
				System.out.println("stroke : "+drawP.shape.getIntStroke(drawP.drawM));
			}
		});
		/* 3. �� ���� ���� */
		strokeType.setSize(70, 35);
		strokeType.setLocation(200, 27);
		for (int i = 0; i < strType.length; i++)
			strokeType.addItem(strType[i]);
		strokeType.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JComboBox cb = (JComboBox)e.getSource();
				String item = (String)cb.getSelectedItem();
				for (int i = 0; i < strType.length; i++)
					if (item.equals(strType[i]))
							drawP.shape.setStrokeType(strType[i]);
				System.out.println("stroke : "+drawP.shape.getStrokeType());
			}
		});
		/* 4. ���� ������ �����ִ� �� */
		nowColor.setSize(45, 60);
		nowColor.setLocation(290, 11);
		/* 5. ��� ������ �����ִ� �� */
		backColor.setSize(45, 60);
		backColor.setLocation(345, 11);
		/* 6. �⺻ ����ǥ 10�� : �г� (ColorPalette Ŭ������ ��ü) */
		colorP.setSize(240, 60);
		colorP.setLocation(410, 15);
		for (int i = 0; i < colorP.paletBtn.length; i++)
		{
			colorP.paletBtn[i].addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					JButton btn = (JButton)e.getSource();
					if (e.isMetaDown() == false)	// ���� ���콺 Ŭ��
					{
						nowColor.setColor(btn.getBackground());
						drawP.shape.setColor(btn.getBackground());
					}
					else	// ������ ���콺 Ŭ��
					{
						drawP.bgColor = btn.getBackground();
						backColor.setColor(drawP.bgColor);
					}
				}
			});
		}
		/* 7. JColorChooser�̿��� �پ��� ������ ����� ���� ������ ��ư */
		moreColor.setSize(50, 50);
		moreColor.setLocation(670, 20);
		moreColor.addActionListener(new ActionListener() {	
			@Override
			public void actionPerformed(ActionEvent e) {
				JColorChooser chooser = new JColorChooser();
				Color selectedColor = chooser.showDialog(null, "���� ����", drawP.shape.getColor());
				if (selectedColor == null)
					return ;
				nowColor.setColor(selectedColor);
				drawP.shape.setColor(selectedColor);
			}
		});
		/* 8. �����̵� ���� : ���콺�� �÷����� ������Ʈ�� ������ ��ȯ�� �������ִ� ��� */
		spoidBtn.setSize(50, 50);
		spoidBtn.setLocation(730, 20);
		spoidBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				drawP.drawM = 13;	//�����̵� ���� ����
				/* �����̵� ��ư ������ ���̷��� ����� �����ڸ��� ��ҵǾ�� �� */
				spoidBtn.setSelected(false);
				//TODO : �̰� �ȵſ�...�ФФ�
				/* �����̵� ��ư ������ leftPanel �� �ִ� ��� ��ư�� ��� ��ҵ� ���·� �ٲٱ� */
				for (int i = 0; i < 12 ; i++)
					Tbtn[i].setSelected(false);
			}
		});
		/* ��� ������Ʈ�� ���ٿ� ���� */
		bar.add(newBtn);
		bar.add(strokeValue);
		bar.add(strokeType);
		bar.add(nowColor);
		bar.add(backColor);
		bar.add(colorP);
		bar.add(moreColor);
		bar.add(spoidBtn);
		/* ���ٸ� �⺻������ mainP�� ���ʿ� ���� */
		mainP.add(bar, BorderLayout.NORTH);
	}
	/* �׸��� ���� ���� ��ư�� ���� ������ */
	class BtnListener implements MouseListener {
		@Override
		public void mousePressed(MouseEvent e) {
			ImageButton btn = (ImageButton)e.getSource();
			/* ���� ��ư ã�Ƽ� �׸��� ��� �����ϱ� */
			for (int i = 0; i < btnName.length; i++)
			{
				if(btn.equals(Tbtn[i]))
				{
					drawP.drawM = i + 1;	//�׸��� ��� ���� 1~12
					break;
				}
			}
			/* ���� ���� ������ ���� �� ���� �޺��ڽ��� �ٸ��� ���õǾ� �ֵ��� ���� */
			strokeValue.setSelectedIndex(drawP.shape.strokeIndex(drawP.shape.getIntStroke(drawP.drawM)));
			//�׸��� ��� Ȯ�ο� �ܼ� ���
			System.out.println("�׸��� ��� : "+drawP.drawM);
		}
		/* ������� �ʴ� �޼ҵ�� : ������� �ʾƵ� implements �߱� ������ �� ���·� �������̵� */
		public void mouseClicked(MouseEvent e) { }
		public void mouseReleased(MouseEvent e) { }
		public void mouseEntered(MouseEvent e) { }
		public void mouseExited(MouseEvent e) {	}
	}

	/* ���� : ������ ���� �� â ����! */
	public static void main(String[] args) {
		new MainPaint();
	}

}
