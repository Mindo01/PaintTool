import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.*;

/*
 * 메인 프로그램 : JFrame 상속
 * UI 디자인이 실질적으로 이루어지고, 프레임이 생성되는 클래스
 * */
public class MainPaint extends JFrame {
	Container contentP;
	PaintPanel mainP;
	String[] btnName = {"자유", "지우개", "선", "네모", "동글", "둥근네모"};
	String[] path = {"resources/draw_pencil.png", "resources/draw_eraser.png", "resources/draw_line.png", "resources/draw_rectangle.png", "resources/draw_oval.png", "resources/draw_roundedrec.png"};
	ImageButton[] Tbtn = new ImageButton[6];
	ButtonGroup bg = new ButtonGroup();	//토글 버튼들 묶어주는 버튼 그룹
	/* 생성자 */
	public MainPaint() {
		setTitle("민주 그림판");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		contentP = getContentPane();
		mainP = new PaintPanel();
		contentP.add(mainP, BorderLayout.CENTER);
		/* 메뉴 바 생성 */
		createMenu();
		/* 사이드 메뉴 생성 */
		leftPanel();
		
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
			/* 눌린 버튼 찾아서 그리기 모드 설정하기 */
			for (int i = 0; i < btnName.length; i++)
			{
				if(btn.equals(Tbtn[i]))
				{
					mainP.drawM = i + 1;	//그리기 모드 설정
					break;
				}
			}
			//그리기 모드 확인용 콘솔 출력
			System.out.println("그리기 모드 : "+mainP.drawM);
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
