import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.net.URL;

import javax.swing.*;

/*
 * 메인 프로그램 : JFrame 상속
 * UI 디자인이 실질적으로 이루어지고, 프레임이 생성되는 클래스
 * */
public class MainPaint extends JFrame {
	Container contentP;
	JPanel mainP;
	PaintPanel drawP;
	String[] btnName = {"자유", "지우개", "선", "네모", "동글", "둥근네모"};
	String[] path = {"draw_pencil.png", "draw_eraser.png", "draw_line.png", "draw_rectangle.png", "draw_oval.png", "draw_roundedrec.png"};
	ImageButton[] Tbtn = new ImageButton[6];
	ButtonGroup bg = new ButtonGroup();	//토글 버튼들 묶어주는 버튼 그룹
	/* 생성자 */
	public MainPaint() {
		setTitle("민주 그림판");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		/* 화면 중앙에 생성 */
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation((screenSize.width-1100)/2, (screenSize.height-830)/2);
		contentP = getContentPane();
		mainP = new JPanel();
		mainP.setLayout(new BorderLayout());
		drawP = new PaintPanel();
		/* 메뉴 바 생성 */
		createMenu();
		/* 사이드 메뉴 생성 */
		leftPanel();
		/* 툴바 메뉴 생성 */
		colorPanel();
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
	
	void createMenu() {
		/* 메뉴, 메뉴바, 메뉴아이템 */
		JMenuBar mb;
		JMenu fileMenu, nameMenu;
		JMenuItem newFile, save, saveAs, open;	// 새로만들기, 저장, 다른 이름으로 저장, 열기
		JMenuItem nameM;	// 만든 이 정보
		/* 메뉴바 */
		mb = new JMenuBar();	// 메뉴 바
		mb.setPreferredSize(new Dimension(320, 40));
		mb.setLocation(0, 0);
		mb.setBackground(Color.DARK_GRAY);
		fileMenu = new JMenu("파일");	// 메뉴 바의 메뉴
		nameMenu = new JMenu("도움");	// 기타 메뉴
		fileMenu.setForeground(Color.WHITE);
		nameMenu.setForeground(Color.WHITE);
		/* 메뉴 아이템들 */
		newFile = new JMenuItem("새로 만들기");
		save = new JMenuItem("저장");
		saveAs = new JMenuItem("다른 이름으로 저장");
		open = new JMenuItem("열기");
		nameM = new JMenuItem("만든이 정보");
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
		nameMenu.add(nameM);
		// 메뉴 바에 메뉴 등록
		mb.add(fileMenu);
		mb.add(nameMenu);
		// 컨탠트 팬에 메뉴 바 등록
		contentP.add(mb, BorderLayout.NORTH);
	}
	/* 왼쪽 메뉴 */
	void leftPanel() {
		/* 왼쪽 메뉴 패널 */
		JPanel leftP = new JPanel();
		/* 메뉴 패널 내에 배치할 패널 */
		JPanel btnP = new JPanel();
		
		ImageButton fill = new ImageButton("draw_emp.png", false);
		leftP.setPreferredSize(new Dimension(160, 800));
		leftP.setBackground(Color.LIGHT_GRAY);
		leftP.setLayout(null);
		/* 그리기 도구 버튼들 : 자유곡선, 지우개, 직선, 네모, 동그라미, 둥근네모 */
		btnP.setLayout(new GridLayout(3, 2));
		for (int i=0; i<btnName.length; i++)
		{
			Tbtn[i] = new ImageButton(path[i], i==0?true:false);	//기본 설정을 자유곡선으로
			Tbtn[i].setSize(new Dimension(65, 65));
			Tbtn[i].addMouseListener(new BtnListener());
			btnP.add(Tbtn[i]);
			bg.add(Tbtn[i]);
		}
		btnP.setSize(130, 195);
		btnP.setLocation(15, 30);
		/* 채우기 버튼 */
		//fill.setSelectedIcon(new ImageIcon("draw_fill.png"));
		fill.setSelectedIcon(new ImageIcon(getClass().getClassLoader().getResource("draw_fill.png")));
		fill.setSize(65, 65);
		fill.setLocation(47, 300);
		fill.addActionListener(new ActionListener() {		
			@Override	/* 채우기 토글 여부 : true or false 페인트패널에 전달하기 위해 fill 필드 변경 */
			public void actionPerformed(ActionEvent e) {
				drawP.shape.fill = !drawP.shape.fill;	// t->f / f->t
				System.out.println("fill : "+drawP.shape.fill);
			}
		});
		leftP.add(btnP);
		leftP.add(fill);
		mainP.add(leftP, BorderLayout.WEST);
	}
	/* 색상 선택 툴바 */
	void colorPanel() {
		JToolBar bar = new JToolBar("ColorMenu");
		JButton newBtn = new JButton("New");
		NowColorPalette nowColor = new NowColorPalette("현재 색");
		ColorPalette colorP = new ColorPalette();
		bar.setLayout(null);
		bar.setPreferredSize(new Dimension(800, 90));
		newBtn.setSize(60, 60);
		newBtn.setLocation(20, 15); // 인자 : --, |
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
	/* 그리기 도형 선택 버튼에 대한 리스너 */
	class BtnListener implements MouseListener {
		@Override
		public void mouseClicked(MouseEvent e) {
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
			//그리기 모드 확인용 콘솔 출력
			System.out.println("그리기 모드 : "+drawP.drawM);
		}
		/* 사용하지 않는 메소드들 : 사용하지 않아도 implements 했기 때문에 빈 상태로 오버라이드 */
		public void mousePressed(MouseEvent e) { }
		public void mouseReleased(MouseEvent e) { }
		public void mouseEntered(MouseEvent e) { }
		public void mouseExited(MouseEvent e) {	}
	}
	
	/* 메인 : 프레임 생성 및 창 생성! */
	public static void main(String[] args) {
		new MainPaint();

	}

}
