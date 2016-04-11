import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * 메인 프로그램 : JFrame 상속
 * UI 디자인이 실질적으로 이루어지고, 프레임이 생성되는 클래스
 * 1. 프레임 생성 및 컴포넌트 부착
 * 2. 왼쪽 패널, 툴바, 메뉴바 부착
 * 3. PaintPanel 클래스의 객체인 drawP 부착
 * 4. 도형 선택하는 버튼 이벤트 등록
 * */
public class MainPaint extends JFrame {
	Container contentP;	// 현재 컨테이너를 받아 여기에 컴포넌트 부착
	JPanel mainP;
	PaintPanel drawP;
	String[] btnName = {"자유", "붓", "점선", "지우개", "선", "네모", "동글", "둥근네모", "세모", "오각형", "육각형", "별"};
	String[] path = {"draw_pencil.png", "draw_brush.png", "draw_highlight.png", "draw_eraser.png", "draw_line.png", "draw_rectangle.png", "draw_oval.png", "draw_roundedrec.png", "draw_triangle.png", "draw_penta.png", "draw_hexa.png", "draw_star.png"};
	ImageButton[] Tbtn = new ImageButton[12];
	ButtonGroup bg = new ButtonGroup();	//토글 버튼들 묶어주는 버튼 그룹
	String[] strSize = {"선 굵기 1", "선 굵기 5", "선 굵기 10", "선 굵기 25", "선 굵기 40", "선 굵기 60"};
	String[] strType = {"실선", "점선"};
	int[] strValue = {1, 5, 10, 25, 40, 60};
	static JComboBox strokeValue;
	static NowColorPalette nowColor;
	static NowColorPalette backColor;
	static ImageButton spoidBtn;
	/* 생성자 */
	public MainPaint() {
		setTitle("민주 그림판 - 제목 없음.png");
		/* 제목표시줄 닫기 버튼의 default를 아무작동 안하는 것으로 설정하고,
		 * windowListener의 windowClosing을 이용해, 
		 * 변경 사항이 있을 경우 저장하기 다이얼로그를 띄우는 방식으로 설정함.
		 * (그냥 닫기 버튼이나 저장 다이얼로그 상태에서 취소를 누를 때, */
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				JFrame frame = (JFrame)e.getWindow();
				if (drawP.changedFile == true)
				{
					/* 변경 사항 저장할 건지 물어보기 */
					JLabel message = new JLabel();
					message.setText("변경 사항을 저장하시겠습니까?");
					int ret = JOptionPane.showConfirmDialog(null, message, "종료 전 확인", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
					/* 1. 저장 YES(확인) 누를 경우 */
					if (ret == JOptionPane.YES_OPTION)
					{
						String str = drawP.saveAs();
						if (str == null)
							return ;
					}
					/* 2. 저장 CANCEL(취소) 또는 창 종료 버튼 누를 경우 */
					if (ret == JOptionPane.CANCEL_OPTION || ret == JOptionPane.CLOSED_OPTION)
					{
						return ;
					}
				}
				System.exit(0);
			}
		});
		/* 화면 중앙에 생성 */
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation((screenSize.width-1100)/2, (screenSize.height-830)/2);
		contentP = getContentPane();
		mainP = new JPanel();
		mainP.setLayout(new BorderLayout());
		drawP = new PaintPanel(null);
		/* 메뉴 바 생성 */
		createMenu();
		/* 사이드 메뉴 생성 */
		leftPanel();
		/* 툴바 메뉴 생성 */
		toolBarPanel();
		/* paintPanel 추가 : 실질적으로 그림 그리는 공간 */
		mainP.add(drawP, BorderLayout.CENTER);
		contentP.add(mainP, BorderLayout.CENTER);
		/* 윈도우 룩앤필 설정 (기본은 자바) */
		try {
		    UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch(Exception e) { 
		}
		
		setSize(1100, 830);
		setVisible(true);
	}
	/* 위쪽에 위치한 메뉴 생성하는 메소드 */
	void createMenu() {
		/* 메뉴, 메뉴바, 메뉴아이템 */
		JMenuBar mb;
		JMenu fileMenu, editMenu, nameMenu;
		JMenuItem newFile, save, saveAs, open;	// 새로만들기, 저장, 다른 이름으로 저장, 열기
		JMenuItem select, copy, cut, paste;
		JMenuItem nameM;	// 만든 이 정보
		/* 메뉴바 */
		mb = new JMenuBar();	// 메뉴 바
		mb.setPreferredSize(new Dimension(320, 45));
		mb.setLocation(0, 0);
		mb.setBackground(Color.DARK_GRAY);
		fileMenu = new JMenu("  파일");	// 파일 메뉴
		editMenu = new JMenu("  도구");	// 편집 메뉴
		nameMenu = new JMenu("  도움");	// 기타 메뉴
		fileMenu.setForeground(Color.WHITE);
		fileMenu.setPreferredSize(new Dimension(50, 45));
		editMenu.setForeground(Color.WHITE);
		editMenu.setPreferredSize(new Dimension(50, 45));
		nameMenu.setForeground(Color.WHITE);
		nameMenu.setPreferredSize(new Dimension(50, 45));
		/* 메뉴 아이템들 생성 */
		// 파일 하위 아이템
		newFile = new JMenuItem("새로 만들기");
		save = new JMenuItem("저장");
		saveAs = new JMenuItem("다른 이름으로 저장");
		open = new JMenuItem("열기");
		// 파일 하위 아이템 단축키 설정
		newFile.setAccelerator(KeyStroke.getKeyStroke('N', InputEvent.CTRL_MASK));
		save.setAccelerator(KeyStroke.getKeyStroke('S', InputEvent.CTRL_MASK));
		saveAs.setAccelerator(KeyStroke.getKeyStroke('S', InputEvent.SHIFT_MASK + InputEvent.CTRL_MASK));
		open.setAccelerator(KeyStroke.getKeyStroke('G', InputEvent.CTRL_MASK));
		// 도구 하위 아이템
		select = new JMenuItem("선택");
		copy = new JMenuItem("복사");
		cut = new JMenuItem("자르기");
		paste = new JMenuItem("붙여넣기");
		// 도구 하위 아이템 단축키 설정
		select.setAccelerator(KeyStroke.getKeyStroke("S"));
		copy.setAccelerator(KeyStroke.getKeyStroke('C', InputEvent.CTRL_MASK));
		cut.setAccelerator(KeyStroke.getKeyStroke('X', InputEvent.CTRL_MASK));
		paste.setAccelerator(KeyStroke.getKeyStroke('V', InputEvent.CTRL_MASK));
		// 도움 하위 아이템
		nameM = new JMenuItem("만든이 정보");
		/* 메뉴 아이템에 대한 리스너 등록 */
		// 1) 새로 만들기
		newFile.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				/* 기존 있던 이미지 저장하는지 확인 */
				// 다이얼로그.... 추가하기
				/* PaintPanel 클래스 내의 init() 메소드를 호출 */
				drawP.init(null);
				setTitle("민주 그림판 - 제목 없음.png");
			}
		});
		// 2) 저장
		save.addActionListener(new ActionListener() {		
			@Override
			public void actionPerformed(ActionEvent e) {
				String title = drawP.save();
				/* 타이틀에 방금 저장한 파일 이름 띄우기 */
				if (title != null)
					setTitle("민주 그림판 - "+title);
			}
		});
		// 3) 다른이름으로 저장
		saveAs.addActionListener(new ActionListener() {		
			@Override
			public void actionPerformed(ActionEvent e) {
				String title = drawP.saveAs();
				/* 타이틀에 방금 저장한 파일 이름 띄우기 */
				if (title != null)
					setTitle("민주 그림판 - "+title);
			}
		});
		// 4) 열기
		open.addActionListener(new ActionListener() {		
			@Override
			public void actionPerformed(ActionEvent e) {
				String title = drawP.open();
				/* 타이틀에 방금 연 파일 이름 띄우기 */
				if (title == null)
					title = "제목 없음.png";
				setTitle("민주 그림판 - "+title);
				
			}
		});
		/* 도구 메뉴 아이템들 리스너 등록 */
		// 1) 선택 모드
		select.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				drawP.drawM = 0;	//선택 모드로 설정
			}
		});
		// 2) 복사하기
		copy.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				drawP.copy();	//선택 모드에서 지정된 영역 복사
			}
		});
		// 3) 자르기
		cut.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				drawP.cut();	//선택 모드에서 지정된 영역 자르기
			}
		});
		// 4) 붙여넣기
		paste.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				drawP.paste();	//선택 모드에서 지정된 영역에 붙여넣기
			}
		});
		/* 만든이 정보 다이얼로그 */
		MJDialog dialog = new MJDialog(this,"만든이 정보");
		/* 만든이 정보 클릭 시, 다이얼로그 창으로 안내 */
		nameM.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dialog.setVisible(true);	// 다이얼로그 창 띄우기
			}
		});
		/* 메뉴바에 메뉴 아이템 등록 */
		fileMenu.add(newFile);
		fileMenu.addSeparator();	// 메뉴 아이템 구분선
		fileMenu.add(open);
		fileMenu.addSeparator();	// 메뉴 아이템 구분선
		fileMenu.add(save);
		fileMenu.add(saveAs);
		editMenu.add(select);
		editMenu.add(copy);
		editMenu.add(cut);
		editMenu.add(paste);
		nameMenu.add(nameM);
		// 메뉴 바에 메뉴 등록
		mb.add(fileMenu);
		mb.add(editMenu);
		mb.add(nameMenu);
		// 컨탠트 팬에 메뉴 바 등록
		contentP.add(mb, BorderLayout.NORTH);
	}
	/* 왼쪽 메뉴 패널 구성 */
	void leftPanel() {
		/* 왼쪽 메뉴 패널 */
		JPanel leftP = new JPanel();
		/* 메뉴 패널 내에 배치할 패널 */
		JPanel btnP = new JPanel();
		JLabel fillLabel = new JLabel("채우기");
		ImageButton fill = new ImageButton("draw_emp.png", false);
		leftP.setPreferredSize(new Dimension(160, 800));
		leftP.setBackground(Color.LIGHT_GRAY);
		leftP.setLayout(null);
		/* 그리기 도구 버튼들 : 자유곡선, 지우개, 직선, 네모, 동그라미, 둥근네모 */
		btnP.setLayout(new GridLayout(6, 2));
		for (int i=0; i<btnName.length; i++)
		{
			Tbtn[i] = new ImageButton(path[i], i==0?true:false);	//기본 설정을 자유곡선으로
			Tbtn[i].setSize(new Dimension(65, 65));
			Tbtn[i].addMouseListener(new BtnListener());
			btnP.add(Tbtn[i]);
			bg.add(Tbtn[i]);
		}
		btnP.setSize(130, 390);
		btnP.setLocation(15, 30);
		/* 채우기 버튼 */
		fillLabel.setSize(50, 50);
		fillLabel.setLocation(54, 430);
		fillLabel.setHorizontalAlignment(SwingConstants.CENTER);
		//fill.setSelectedIcon(new ImageIcon("draw_fill.png"));
		fill.setSelectedIcon(new ImageIcon(getClass().getClassLoader().getResource("draw_fills.png")));
		fill.setSize(65, 65);
		fill.setLocation(47, 465);
		fill.addActionListener(new ActionListener() {		
			@Override	/* 채우기 토글 여부 : true or false 페인트패널에 전달하기 위해 fill 필드 변경 */
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
	/* 메뉴 아래에 붙어있는 툴바 구성 : 새로만들기, 색상 선택, 선 굵기 조절 등. */
	void toolBarPanel() {
		JToolBar bar = new JToolBar("ColorMenu");
		JButton newBtn = new JButton("New");
		JButton moreColor = new JButton(new ImageIcon(getClass().getClassLoader().getResource("draw_colors.png")));
		nowColor = new NowColorPalette("현재 색", Color.BLACK);
		backColor = new NowColorPalette("배경 색", Color.WHITE);
		ColorPalette colorP = new ColorPalette();
		strokeValue = new JComboBox();
		JComboBox strokeType = new JComboBox();
		spoidBtn = new ImageButton("draw_spoid.png", false);
		
		/* 0. 배치관리자 제거 후, 툴바 내에 컴포넌트 절대 위치에 배치하기 */
		bar.setLayout(null);
		bar.setPreferredSize(new Dimension(800, 90));
		/* 1. 새로 만들기 버튼 : 그리는 패널 초기화*/
		newBtn.setSize(60, 60);
		newBtn.setLocation(20, 15); // 인자 : --, |
		newBtn.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				drawP.init(null);
				setTitle("민주 그림판 - 제목 없음.png");
			}
		});
		/* 1. 선 굵기 설정 */
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
		/* 2. 선 유형 설정 */
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
		/* 3. 현재 색상을 보여주는 라벨 */
		nowColor.setSize(45, 60);
		nowColor.setLocation(290, 11);
		/* 3. 배경 색상을 보여주는 라벨 */
		backColor.setSize(45, 60);
		backColor.setLocation(345, 11);
		/* 4. 기본 색상표 10개 : 패널 (ColorPalette 클래스의 객체) */
		colorP.setSize(240, 60);
		colorP.setLocation(410, 15);
		for (int i = 0; i < colorP.paletBtn.length; i++)
		{
			colorP.paletBtn[i].addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					JButton btn = (JButton)e.getSource();
					if (e.isMetaDown() == false)	// 왼쪽 마우스 클릭
					{
						colorP.color = btn.getBackground();
						nowColor.setColor(colorP.color);
						drawP.shape.setColor(colorP.color);
					}
					else	// 오른쪽 마우스 클릭
					{
						drawP.bgColor = btn.getBackground();
						backColor.setColor(drawP.bgColor);
					}
				}
			});
		}
		/* 5. JColorChooser이용해 다양한 색상을 사용자 설정 가능한 버튼 */
		moreColor.setSize(50, 50);
		moreColor.setLocation(670, 20);
		moreColor.addActionListener(new ActionListener() {	
			@Override
			public void actionPerformed(ActionEvent e) {
				JColorChooser chooser = new JColorChooser();
				Color selectedColor = chooser.showDialog(null, "색상 선택", drawP.shape.getColor());
				if (selectedColor == null)
					return ;
				nowColor.setColor(selectedColor);
				drawP.shape.setColor(selectedColor);
			}
		});
		/* 6. 스포이드 구현 : 마우스를 올려놓은 컴포넌트의 색상을 반환해 설정해주는 기능 */
		spoidBtn.setSize(50, 50);
		spoidBtn.setLocation(730, 20);
		spoidBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				drawP.drawM = 13;	//스포이드 모드로 설정
				/* 스포이드 버튼 색상이 보이려면 토글이 눌리자마자 취소되어야 함 */
				spoidBtn.setSelected(false);
				//TODO : 이거 안돼요...ㅠㅠㅠ
				/* 스포이드 버튼 누르면 leftPanel 에 있는 토글 버튼들 모두 취소된 상태로 바꾸기 */
				for (int i = 0; i < 12 ; i++)
					Tbtn[i].setSelected(false);
			}
		});
		//TODO : 다른 도구 버튼은 false 되도록 (leftPanel의 도구 토글버튼들)
		
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
	/* 그리기 도형 선택 버튼에 대한 리스너 */
	class BtnListener implements MouseListener {
		@Override
		public void mousePressed(MouseEvent e) {
			ImageButton btn = (ImageButton)e.getSource();
			/* 눌린 버튼 찾아서 그리기 모드 설정하기 */
			for (int i = 0; i < btnName.length; i++)
			{
				if(btn.equals(Tbtn[i]))
				{
					drawP.drawM = i + 1;	//그리기 모드 설정
					break;
				}
			}
			/* 현재 도형 유형에 따라 선 굵기 콤보박스에 다르게 선택되어 있도록 설정 */
			strokeValue.setSelectedIndex(drawP.shape.strokeIndex(drawP.shape.getIntStroke(drawP.drawM)));
			//그리기 모드 확인용 콘솔 출력
			System.out.println("그리기 모드 : "+drawP.drawM);
		}
		/* 사용하지 않는 메소드들 : 사용하지 않아도 implements 했기 때문에 빈 상태로 오버라이드 */
		public void mouseClicked(MouseEvent e) { }
		public void mouseReleased(MouseEvent e) { }
		public void mouseEntered(MouseEvent e) { }
		public void mouseExited(MouseEvent e) {	}
	}

	/* 메인 : 프레임 생성 및 창 생성! */
	public static void main(String[] args) {
		new MainPaint();

	}

}
