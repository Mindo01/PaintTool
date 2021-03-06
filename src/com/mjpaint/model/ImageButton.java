package com.mjpaint.model;
import javax.swing.*;

/** 이미지 버튼 클래스 : 이미지 아이콘 등록 시, jar파일 생성 후에도 구동되도록 생성자에서 설정 
 * 1. path(이미지 파일이 저장된 경로)
 * 2. flag(ToggleButton의 토글 여부 true : 클릭된 상태 / false : 클릭 안 된 상태)
 * 		- 처음 프로그램 실행 시 기본 도구가 선택되어 있도록 설정하기 위해 사용
 * */
public class ImageButton extends JToggleButton {

  public ImageButton(String path, boolean flag) {
	  super(new ImageIcon(path), flag);
	  /* jar 파일에 포함되도록 설정 */
	  setIcon(new ImageIcon(getClass().getClassLoader().getResource(path)));
  }

}