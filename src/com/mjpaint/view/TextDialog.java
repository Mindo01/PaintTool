package com.mjpaint.view;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/** �ؽ�Ʈ �ʵ�� ��ư �ϳ��� ���� Ŀ���� ���̾�α�
 * 1. �ؽ�Ʈ �ʵ� : ���ڸ� �Է� ����
 * 2. Ȯ�� ��ư ������ �ؽ�Ʈ �ʵ��� ���� value �ʵ忡 ������
 * --> �ձ� �簢�� ���� �����ϴ� �޴��� ���� ���� ���̾�α� */
public class TextDialog extends JDialog {
	JTextField tf = new JTextField(3);		//�ؽ�Ʈ �ʵ�
	JButton okButton = new JButton("Ȯ��");	//Ȯ�� ��ư
	
	public int value = 30;	// �ձ� ���� �⺻��
	/* �ؽ�Ʈ ���̾�α� ������ */
	public TextDialog(JFrame frame, String title) {
		super(frame, title);
		setLayout(new FlowLayout());
		add(tf);
		add(okButton);
		setSize(200, 100);

		/* �ؽ�Ʈ �ʵ忡 ���ڸ� �Է� �����ϵ��� ���� : ���� �Է� �� ������ ������ */
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
		/* Ȯ�� ��ư ������, �ؽ�Ʈ �ʵ��� ���� value �ʵ忡 ������ */
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				/* �ؽ�Ʈ �ʵ� ������ ������� �׳� ���� */
				if (tf.getText().isEmpty())
				{
					setVisible(false);
					return ;
				}
				value = Integer.valueOf(tf.getText());
				setVisible(false);
			}
		});
		/* ���̾�α� ����� ������� ��ġ */
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation((screenSize.width-1000), (screenSize.height-600));
		/* â ũ�� ���� */
		setResizable(false);
	}
}
