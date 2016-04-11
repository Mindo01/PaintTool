package com.mjpaint.model;
import javax.swing.*;

/** �̹��� ��ư Ŭ���� : �̹��� ������ ��� ��, jar���� ���� �Ŀ��� �����ǵ��� �����ڿ��� ���� 
 * 1. path(�̹��� ������ ����� ���)
 * 2. flag(ToggleButton�� ��� ���� true : Ŭ���� ���� / false : Ŭ�� �� �� ����)
 * 		- ó�� ���α׷� ���� �� �⺻ ������ ���õǾ� �ֵ��� �����ϱ� ���� ���
 * */
public class ImageButton extends JToggleButton {

  public ImageButton(String path, boolean flag) {
	  super(new ImageIcon(path), flag);
	  /* jar ���Ͽ� ���Եǵ��� ���� */
	  setIcon(new ImageIcon(getClass().getClassLoader().getResource(path)));
  }

}