import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.Vector;
import javax.swing.*;
/*
 * ���������� �׸��� �׸��� �г�
 * ���콺 Ŭ��/����/�巡�� ���� �����ʸ� ����س��Ҵ�.
 * */
public class PaintPanel extends JPanel {
	private BufferedImage b1 = new BufferedImage(1000, 800, BufferedImage.TYPE_3BYTE_BGR);
	private Graphics g1 = b1.getGraphics(); // ���� �׷����� ����
	
	ShapeInfo shape = new ShapeInfo();
	int drawM = 1;	//�׸��� ���	
	/* �׸��� ��忡 �����ϴ� ����� */
	final static int PENCIL = 1;	//���� �
	final static int ERASE = 2;		//�����
	final static int LINE = 3;		//����
	final static int REC = 4;		//�׸�
	final static int OVAL = 5;		//��(Ÿ��)
	final static int ROUNDREC = 6;	//�ձ� �׸�

	public PaintPanel () {
		setBackground(Color.WHITE);
		g1.fillRect(0, 0, 1000, 800);
		g1.drawImage(new ImageIcon().getImage(), 0, 0, null);
		/* �⺻ �� ���� ���� */
		shape.setStroke(3);
		addMouseListener( new PaintListener());
		addMouseMotionListener( new PaintListener());
	}
	
	class PaintListener implements MouseListener, MouseMotionListener {
		public void mousePressed(MouseEvent e) {
			// ù ������ ����
			shape.add(e.getPoint());
			System.out.println("���ȵ�!");
			
		}
		public void mouseReleased(MouseEvent e) {
			shape.add(e.getPoint());
			Point sp = shape.get().firstElement();
			Point ep = shape.get().lastElement();
			((Graphics2D) g1).setStroke(shape.stroke);
			g1.setColor(Color.BLUE);
			g1.drawLine((int)sp.getX(), (int)sp.getY(), (int)ep.getX(), (int)ep.getY());
			System.out.println("Ǯ�ȵ�!");
			repaint();
			/* ����� ����Ʈ ��� ���� */
			shape.get().clear();
		}
		public void mouseDragged(MouseEvent e) {
			shape.add(e.getPoint());
			Point sp = shape.get().firstElement();
			Point ep = shape.get().lastElement();
			/* �׸��� ��忡 ���� �ٸ� ��Ȳ���� */
			Graphics g2 = getGraphics();
			((Graphics2D) g2).setStroke(shape.stroke);
			g2.setColor(Color.BLUE);
			g2.drawLine((int)sp.getX(), (int)sp.getY(), (int)ep.getX(), (int)ep.getY());
			
			
			
			getParent().repaint();
		}
		/* ������� �ʴ� �޼ҵ�� : ������� �ʾƵ� implements �߱� ������ �� ���·� �������̵� */
		public void mouseMoved(MouseEvent e) {}
		public void mouseClicked(MouseEvent e) {}
		public void mouseEntered(MouseEvent e) {}
		public void mouseExited(MouseEvent e) {}
	}

	public void paint(Graphics g) {
		g.drawImage(b1, 0, 0, null);
	}
}
