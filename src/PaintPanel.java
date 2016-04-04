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
	int drawM = 1;	//그리기 모드	// 이거 또는 shape내의 type필드 둘 중 하나 선택하고 지우기 *******
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
		shape.setStroke(5);
		addMouseListener( new PaintListener());
		addMouseMotionListener( new PaintListener());
	}
	
	class PaintListener implements MouseListener, MouseMotionListener {
		public void mousePressed(MouseEvent e) {
			// 첫 시작점 저장
			shape.add(e.getPoint());
			((Graphics2D)g1).setStroke(shape.getStroke());
		}
		public void mouseReleased(MouseEvent e) {
			shape.add(e.getPoint());
			Point sp = shape.get().firstElement();
			Point ep = shape.get().lastElement();
			((Graphics2D) g1).setStroke(shape.getStroke());
			g1.setColor(shape.getColor());
			Rectangle rect = shape.getRect(sp, ep);
			switch (drawM)
			{
				case ERASE :
					g1.setColor(Color.WHITE);
				case PENCIL :
					sp = shape.point.size() > 1 ? shape.point.get(shape.point.size() - 2) : shape.point.firstElement();
					g1.drawLine((int)sp.getX(), (int)sp.getY(), (int)ep.getX(), (int)ep.getY());
					break;
				case LINE : 
					g1.drawLine((int)sp.getX(), (int)sp.getY(), (int)ep.getX(), (int)ep.getY());
					break;
				case REC :	
					if (shape.fill == false)
						g1.drawRect(rect.x, rect.y, rect.width, rect.height);
					else
						g1.fillRect(rect.x, rect.y, rect.width, rect.height);
					break;
				case OVAL :
					if (shape.fill == false)
						g1.drawOval(rect.x, rect.y, rect.width, rect.height);
					else
						g1.fillOval(rect.x, rect.y, rect.width, rect.height);
					break;
				case ROUNDREC :
					if (shape.fill == false)
						g1.drawRoundRect(rect.x, rect.y, rect.width, rect.height, 50, 50);	//뒤에 두 인자는 둥근 정도 수치
					else
						g1.fillRoundRect(rect.x, rect.y, rect.width, rect.height, 50, 50);
					break;
			}
			repaint();
			/* 저장된 포인트 모두 삭제 */
			shape.get().clear();
		}
		public void mouseDragged(MouseEvent e) {
			shape.add(e.getPoint());
			Point sp = shape.get().firstElement();
			Point ep = shape.get().lastElement();
			/* 그리기 모드에 따라 다른 상황으로
			 * 자유곡선, 지우기는 드래그도 그대로 그림판에 입력
			 * 나머지 기능은 드래그 과정은 그냥 보여주기만 (실질적 입력이 아님 - 지역 객체 g2 사용)
			 */
			if (drawM == PENCIL || drawM == ERASE)	// 자유곡선, 지우개일 때
			{
				((Graphics2D) g1).setStroke(shape.stroke);
				g1.setColor(shape.getColor());
				if (drawM == ERASE)
					g1.setColor(Color.WHITE);
				sp = shape.point.size() > 1 ? shape.point.get(shape.point.size() - 2) : shape.point.firstElement();
				g1.drawLine((int)sp.getX(), (int)sp.getY(), (int)ep.getX(), (int)ep.getY());
				
				repaint();
			}
			else
			{
				Graphics g2 = getGraphics();
				Rectangle rect = shape.getRect(sp, ep);;
				((Graphics2D) g2).setStroke(shape.stroke);
				g2.setColor(shape.getColor());
				switch (drawM)
				{
					case LINE : 
						g2.drawLine((int)sp.getX(), (int)sp.getY(), (int)ep.getX(), (int)ep.getY());
						break;
					case REC :
						if (shape.fill == false)
							g2.drawRect(rect.x, rect.y, rect.width, rect.height);
						else
							g2.fillRect(rect.x, rect.y, rect.width, rect.height);
						break;
					case OVAL :
						if (shape.fill == false)
							g2.drawOval(rect.x, rect.y, rect.width, rect.height);
						else
							g2.fillOval(rect.x, rect.y, rect.width, rect.height);
						break;
					case ROUNDREC :
						if (shape.fill == false)
							g2.drawRoundRect(rect.x, rect.y, rect.width, rect.height, 50, 50);	//뒤에 두 인자는 둥근 정도 수치
						else
							g2.fillRoundRect(rect.x, rect.y, rect.width, rect.height, 50, 50);
						break;
				}
				getParent().repaint();
			}
		}
		/* 사용하지 않는 메소드들 : 사용하지 않아도 implements 했기 때문에 빈 상태로 오버라이드 */
		public void mouseMoved(MouseEvent e) {}
		public void mouseClicked(MouseEvent e) {}
		public void mouseEntered(MouseEvent e) {}
		public void mouseExited(MouseEvent e) {}
	}

	/* 그림판 새로 시작하기 */
	public void init() {
		g1.setColor(Color.WHITE);
		g1.fillRect(0, 0, 1000, 800);
		g1.drawImage(new ImageIcon().getImage(), 0, 0, null);
		repaint();
	}
	/* repaint() 호출 시 실행되는 메소드 */
	public void paint(Graphics g) {
		g.drawImage(b1, 0, 0, null);
	}
}
