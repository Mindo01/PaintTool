package com.mjpaint.view;
import java.awt.*;
import javax.swing.*;

/** 만든이 정보, 이미지 출처 정보를 띄워주는 커스텀 다이얼로그
 * 1. 만든이 정보 : 만든이 정보(이름, 학교 학번, 이메일 주소)
 * 2. 이미지 출처 정보 : iconfinder 내에서 받은 이미지 출처 작성
 *  */
public class MJDialog extends JDialog{
	JPanel panel = new JPanel();
	public MJDialog (JFrame frame, String title, int purpose) {
		super (frame, title);
		setLayout(null);
		/* 다이얼로그 목적에 따라 다르게 설정 : 1. 만든이 정보 / 2. 이미지 출처 */
		if (purpose == 1)
			copyrights();
		else
			imgCopyrights();
		/* 패널 배경색 설정, 위치 설정 */
		panel.setBackground(Color.WHITE);
		panel.setOpaque(true);
		panel.setLocation(0, 0);
		add(panel);
		/* 다이얼로그 띄워줄 윈도우상 위치 */
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation((screenSize.width-1000)/2, (screenSize.height-800)/2);
		/* 창 크기 고정 */
		setResizable(false);
	}
	/* 만든이 정보 다이얼로그 설정 : 맞는 내용의 라벨 등록 */
	private void copyrights()
	{
		/* 다이얼로그 내용 라벨로 나열 */
		panel.add(new JLabel("만든이"));
		panel.add(new JLabel("성공회대학교 글로컬IT학과"));
		panel.add(new JLabel("201334005 김민주"));
		panel.add(new JLabel("y229600@naver.com"));
		panel.setSize(200, 150);
		setSize(208, 150);
	}
	/* 이미지(아이콘) 출처 정보 다이얼로그 설정 : 맞는 내용의 라벨 등록 */
	private void imgCopyrights()
	{
		/* 다이얼로그 내용 라벨로 나열 */
		panel.add(new JLabel("              이미지 아이콘 출처                "));
		panel.add(new JLabel("Sergei Kokota"));
		panel.add(new JLabel("in IconFinder"));
		panel.add(new JLabel("https://www.iconfinder.com/Zerg"));
		panel.setSize(210, 120);
		setSize(218, 120);
	}
}
