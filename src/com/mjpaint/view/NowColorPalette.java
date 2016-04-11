package com.mjpaint.view;
import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.*;
/**
 * ���� ���õ� ������ ���÷��� ���ִ� �г�
 * */
public class NowColorPalette extends JPanel{
	Color color;
	JLabel label;
	JLabel palet;
	/* ������ : �г� ���� : ���� �۾� �� + �Ʒ��� ���� ��*/
	public NowColorPalette(String str, Color basic) {
		label = new JLabel(str);	// �۾� ��
		palet = new JLabel(" ");	// ���� ��
		setLayout(new GridLayout(2, 1));
		label.setHorizontalAlignment(SwingConstants.CENTER);	// �۾� ��� ����
		palet.setOpaque(true);		// ���� �� ���� ������ �����ϵ��� ����
		palet.setBackground(basic);	// �����ڿ��� �޾ƿ� �⺻ �������� ����� ��� �� ����
		/* �ȷ�Ʈ �гο� �۾�, ���� �� ��� */
		add(label);
		add(palet);
		setSize(60, 60);
	}
	/** ���� �г��� �� ����, ��� */
	public void setColor(Color chColor) {
		palet.setBackground(chColor);
	}
	/** ���� �г��� �� ������ ��ȯ */
	public Color getColor() {
		return color;
	}
}
