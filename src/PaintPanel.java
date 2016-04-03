import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.Vector;
import javax.swing.*;
/*
 * 실질적으로 그림을 그리는 패널
 * 마우스 클릭/해제/드래그 때의 리스너를 등록해놓았다.
 * */
public class PaintPanel extends JPanel {
	private BufferedImage b1 = new BufferedImage(1000, 800, BufferedImage.TYPE_3BYTE_BGR);
	private Graphics g1 = b1.getGraphics(); // 실제 그려지는 영역
	
	ShapeInfo shape = new ShapeInfo();
	int drawM = 1;	//그리기 모드	
	/* 그리기 모드에 대응하는 상수들 */
	final static int PENCIL = 1;	//자유 곡선
	final static int ERASE = 2;		//지우기
	final static int LINE = 3;		//직선
	final static int REC = 4;		//네모
	final static int OVAL = 5;		//원(타원)
	final static int ROUNDREC = 6;	//둥근 네모

	public PaintPanel () {
		setBackground(Color.WHITE);
		g1.fillRect(0, 0, 1000, 800);
		g1.drawImage(new ImageIcon().getImage(), 0, 0, null);
		/* 기본 선 굵기 설정 */
		shape.setStroke(3);
		addMouseListener( new PaintListener());
		addMouseMotionListener( new PaintListener());
	}
	
	class PaintListener implements MouseListener, MouseMotionListener {
		public void mousePressed(MouseEvent e) {
			// 첫 시작점 저장
			shape.add(e.getPoint());
			System.out.println("눌렸따!");
			
		}
		public void mouseReleased(MouseEvent e) {
			shape.add(e.getPoint());
			Point sp = shape.get().firstElement();
			Point ep = shape.get().lastElement();
			((Graphics2D) g1).setStroke(shape.stroke);
			g1.setColor(Color.BLUE);
			g1.drawLine((int)sp.getX(), (int)sp.getY(), (int)ep.getX(), (int)ep.getY());
			System.out.println("풀렸따!");
			repaint();
			/* 저장된 포인트 모두 삭제 */
			shape.get().clear();
		}
		public void mouseDragged(MouseEvent e) {
			shape.add(e.getPoint());
			Point sp = shape.get().firstElement();
			Point ep = shape.get().lastElement();
			/* 그리기 모드에 따라 다른 상황으로 */
			Graphics g2 = getGraphics();
			((Graphics2D) g2).setStroke(shape.stroke);
			g2.setColor(Color.BLUE);
			g2.drawLine((int)sp.getX(), (int)sp.getY(), (int)ep.getX(), (int)ep.getY());
			
			
			
			getParent().repaint();
		}
		/* 사용하지 않는 메소드들 : 사용하지 않아도 implements 했기 때문에 빈 상태로 오버라이드 */
		public void mouseMoved(MouseEvent e) {}
		public void mouseClicked(MouseEvent e) {}
		public void mouseEntered(MouseEvent e) {}
		public void mouseExited(MouseEvent e) {}
	}

	public void paint(Graphics g) {
		g.drawImage(b1, 0, 0, null);
	}
}
