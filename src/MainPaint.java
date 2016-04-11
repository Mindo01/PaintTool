import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

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
	JPanel mainP;
	PaintPanel drawP;
	String[] btnName = {"����", "��", "����", "���찳", "��", "�׸�", "����", "�ձٳ׸�", "����", "������", "������", "��"};
	String[] path = {"draw_pencil.png", "draw_brush.png", "draw_highlight.png", "draw_eraser.png", "draw_line.png", "draw_rectangle.png", "draw_oval.png", "draw_roundedrec.png", "draw_triangle.png", "draw_penta.png", "draw_hexa.png", "draw_star.png"};
	ImageButton[] Tbtn = new ImageButton[12];
	ButtonGroup bg = new ButtonGroup();	//��� ��ư�� �����ִ� ��ư �׷�
	String[] strSize = {"�� ���� 1", "�� ���� 5", "�� ���� 10", "�� ���� 25", "�� ���� 40", "�� ���� 60"};
	String[] strType = {"�Ǽ�", "����"};
	int[] strValue = {1, 5, 10, 25, 40, 60};
	static JComboBox strokeValue;
	static NowColorPalette nowColor;
	static NowColorPalette backColor;
	static ImageButton spoidBtn;
	/* ������ */
	public MainPaint() {
		setTitle("���� �׸��� - ���� ����.png");
		/* ����ǥ���� �ݱ� ��ư�� default�� �ƹ��۵� ���ϴ� ������ �����ϰ�,
		 * windowListener�� windowClosing�� �̿���, 
		 * ���� ������ ���� ��� �����ϱ� ���̾�α׸� ���� ������� ������.
		 * (�׳� �ݱ� ��ư�̳� ���� ���̾�α� ���¿��� ��Ҹ� ���� ��, */
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				JFrame frame = (JFrame)e.getWindow();
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
				System.exit(0);
			}
		});
		/* ȭ�� �߾ӿ� ���� */
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation((screenSize.width-1100)/2, (screenSize.height-830)/2);
		contentP = getContentPane();
		mainP = new JPanel();
		mainP.setLayout(new BorderLayout());
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
		
		setSize(1100, 830);
		setVisible(true);
	}
	/* ���ʿ� ��ġ�� �޴� �����ϴ� �޼ҵ� */
	void createMenu() {
		/* �޴�, �޴���, �޴������� */
		JMenuBar mb;
		JMenu fileMenu, editMenu, nameMenu;
		JMenuItem newFile, save, saveAs, open;	// ���θ����, ����, �ٸ� �̸����� ����, ����
		JMenuItem select, copy, cut, paste;
		JMenuItem nameM;	// ���� �� ����
		/* �޴��� */
		mb = new JMenuBar();	// �޴� ��
		mb.setPreferredSize(new Dimension(320, 45));
		mb.setLocation(0, 0);
		mb.setBackground(Color.DARK_GRAY);
		fileMenu = new JMenu("  ����");	// ���� �޴�
		editMenu = new JMenu("  ����");	// ���� �޴�
		nameMenu = new JMenu("  ����");	// ��Ÿ �޴�
		fileMenu.setForeground(Color.WHITE);
		fileMenu.setPreferredSize(new Dimension(50, 45));
		editMenu.setForeground(Color.WHITE);
		editMenu.setPreferredSize(new Dimension(50, 45));
		nameMenu.setForeground(Color.WHITE);
		nameMenu.setPreferredSize(new Dimension(50, 45));
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
		editMenu.add(select);
		editMenu.add(copy);
		editMenu.add(cut);
		editMenu.add(paste);
		nameMenu.add(nameM);
		// �޴� �ٿ� �޴� ���
		mb.add(fileMenu);
		mb.add(editMenu);
		mb.add(nameMenu);
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
		btnP.setLayout(new GridLayout(6, 2));
		for (int i=0; i<btnName.length; i++)
		{
			Tbtn[i] = new ImageButton(path[i], i==0?true:false);	//�⺻ ������ ���������
			Tbtn[i].setSize(new Dimension(65, 65));
			Tbtn[i].addMouseListener(new BtnListener());
			btnP.add(Tbtn[i]);
			bg.add(Tbtn[i]);
		}
		btnP.setSize(130, 390);
		btnP.setLocation(15, 30);
		/* ä��� ��ư */
		fillLabel.setSize(50, 50);
		fillLabel.setLocation(54, 430);
		fillLabel.setHorizontalAlignment(SwingConstants.CENTER);
		//fill.setSelectedIcon(new ImageIcon("draw_fill.png"));
		fill.setSelectedIcon(new ImageIcon(getClass().getClassLoader().getResource("draw_fills.png")));
		fill.setSize(65, 65);
		fill.setLocation(47, 465);
		fill.addActionListener(new ActionListener() {		
			@Override	/* ä��� ��� ���� : true or false ����Ʈ�гο� �����ϱ� ���� fill �ʵ� ���� */
			public void actionPerformed(ActionEvent e) {
				drawP.shape.fill = !drawP.shape.fill;	// t->f / f->t
				System.out.println("fill : "+drawP.shape.fill);
			}
		});
		leftP.add(btnP);
		leftP.add(fillLabel);
		leftP.add(fill);
		mainP.add(leftP, BorderLayout.WEST);
	}
	/* �޴� �Ʒ��� �پ��ִ� ���� ���� : ���θ����, ���� ����, �� ���� ���� ��. */
	void toolBarPanel() {
		JToolBar bar = new JToolBar("ColorMenu");
		JButton newBtn = new JButton("New");
		JButton moreColor = new JButton(new ImageIcon(getClass().getClassLoader().getResource("draw_colors.png")));
		nowColor = new NowColorPalette("���� ��", Color.BLACK);
		backColor = new NowColorPalette("��� ��", Color.WHITE);
		ColorPalette colorP = new ColorPalette();
		strokeValue = new JComboBox();
		JComboBox strokeType = new JComboBox();
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
				drawP.init(null);
				setTitle("���� �׸��� - ���� ����.png");
			}
		});
		/* 1. �� ���� ���� */
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
		/* 2. �� ���� ���� */
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
		/* 3. ���� ������ �����ִ� �� */
		nowColor.setSize(45, 60);
		nowColor.setLocation(290, 11);
		/* 3. ��� ������ �����ִ� �� */
		backColor.setSize(45, 60);
		backColor.setLocation(345, 11);
		/* 4. �⺻ ����ǥ 10�� : �г� (ColorPalette Ŭ������ ��ü) */
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
						colorP.color = btn.getBackground();
						nowColor.setColor(colorP.color);
						drawP.shape.setColor(colorP.color);
					}
					else	// ������ ���콺 Ŭ��
					{
						drawP.bgColor = btn.getBackground();
						backColor.setColor(drawP.bgColor);
					}
				}
			});
		}
		/* 5. JColorChooser�̿��� �پ��� ������ ����� ���� ������ ��ư */
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
		/* 6. �����̵� ���� : ���콺�� �÷����� ������Ʈ�� ������ ��ȯ�� �������ִ� ��� */
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
		//TODO : �ٸ� ���� ��ư�� false �ǵ��� (leftPanel�� ���� ��۹�ư��)
		
		bar.add(newBtn);
		bar.addSeparator();
		bar.add(strokeValue);
		bar.add(strokeType);
		bar.addSeparator();
		bar.add(nowColor);
		bar.add(backColor);
		bar.add(colorP);
		bar.add(moreColor);
		bar.add(spoidBtn);
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
					drawP.drawM = i + 1;	//�׸��� ��� ����
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
