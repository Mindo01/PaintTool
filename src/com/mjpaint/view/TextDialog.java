package com.mjpaint.view;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/** 텍스트 필드와 버튼 하나를 가진 커스텀 다이얼로그
 * 1. 텍스트 필드 : 숫자만 입력 가능
 * 2. 확인 버튼 누르면 텍스트 필드의 값을 value 필드에 저장함
 * --> 둥근 사각형 정도 설정하는 메뉴를 위해 만든 다이얼로그 */
public class TextDialog extends JDialog {
	JTextField tf = new JTextField(3);		//텍스트 필드
	JButton okButton = new JButton("확인");	//확인 버튼
	
	public int value = 30;	// 둥근 정도 기본값
	/* 텍스트 다이얼로그 생성자 */
	public TextDialog(JFrame frame, String title) {
		super(frame, title);
		setLayout(new FlowLayout());
		add(tf);
		add(okButton);
		setSize(200, 100);

		/* 텍스트 필드에 숫자만 입력 가능하도록 설정 : 문자 입력 시 강제로 지워짐 */
		tf.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e){
				String str = "";
				JTextField s=(JTextField)e.getSource();
				try{
					if(!s.getText().isEmpty())
					Integer.parseInt(s.getText());
					str = s.getText();
				}
				catch(NumberFormatException nfe)
				{
					s.setText(str);
				}
			}
		});
		/* 확인 버튼 누르면, 텍스트 필드의 값을 value 필드에 저장함 */
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				/* 텍스트 필드 내용이 비었으면 그냥 종료 */
				if (tf.getText().isEmpty())
				{
					setVisible(false);
					return ;
				}
				value = Integer.valueOf(tf.getText());
				setVisible(false);
			}
		});
		/* 다이얼로그 띄워줄 윈도우상 위치 */
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation((screenSize.width-1000), (screenSize.height-600));
		/* 창 크기 고정 */
		setResizable(false);
	}
}
