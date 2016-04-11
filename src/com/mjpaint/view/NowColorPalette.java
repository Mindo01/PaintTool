package com.mjpaint.view;
import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.*;
/**
 * 현재 선택된 색깔을 디스플레이 해주는 패널
 * */
public class NowColorPalette extends JPanel{
	Color color;
	JLabel label;
	JLabel palet;
	/* 생성자 : 패널 구성 : 위에 글씨 라벨 + 아래에 색상 라벨*/
	public NowColorPalette(String str, Color basic) {
		label = new JLabel(str);	// 글씨 라벨
		palet = new JLabel(" ");	// 색상 라벨
		setLayout(new GridLayout(2, 1));
		label.setHorizontalAlignment(SwingConstants.CENTER);	// 글씨 가운데 정렬
		palet.setOpaque(true);		// 색상 라벨 배경색 설정이 가능하도록 설정
		palet.setBackground(basic);	// 생성자에서 받아온 기본 색상으로 색상라벨 배경 색 설정
		/* 팔렛트 패널에 글씨, 색상 라벨 등록 */
		add(label);
		add(palet);
		setSize(60, 60);
	}
	/** 색상 패널의 색 설정, 등록 */
	public void setColor(Color chColor) {
		palet.setBackground(chColor);
	}
	/** 색상 패널의 색 가져와 반환 */
	public Color getColor() {
		return color;
	}
}
