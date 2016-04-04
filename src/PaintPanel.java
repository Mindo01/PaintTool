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
	int drawM = 1;	//�׸��� ���	// �̰� �Ǵ� shape���� type�ʵ� �� �� �ϳ� �����ϰ� ����� *******
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
		shape.setStroke(5);
		addMouseListener( new PaintListener());
		addMouseMotionListener( new PaintListener());
	}
	
	class PaintListener implements MouseListener, MouseMotionListener {
		public void mousePressed(MouseEvent e) {
			// ù ������ ����
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
						g1.drawRoundRect(rect.x, rect.y, rect.width, rect.height, 50, 50);	//�ڿ� �� ���ڴ� �ձ� ���� ��ġ
					else
						g1.fillRoundRect(rect.x, rect.y, rect.width, rect.height, 50, 50);
					break;
			}
			repaint();
			/* ����� ����Ʈ ��� ���� */
			shape.get().clear();
		}
		public void mouseDragged(MouseEvent e) {
			shape.add(e.getPoint());
			Point sp = shape.get().firstElement();
			Point ep = shape.get().lastElement();
			/* �׸��� ��忡 ���� �ٸ� ��Ȳ����
			 * �����, ������ �巡�׵� �״�� �׸��ǿ� �Է�
			 * ������ ����� �巡�� ������ �׳� �����ֱ⸸ (������ �Է��� �ƴ� - ���� ��ü g2 ���)
			 */
			if (drawM == PENCIL || drawM == ERASE)	// �����, ���찳�� ��
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
							g2.drawRoundRect(rect.x, rect.y, rect.width, rect.height, 50, 50);	//�ڿ� �� ���ڴ� �ձ� ���� ��ġ
						else
							g2.fillRoundRect(rect.x, rect.y, rect.width, rect.height, 50, 50);
						break;
				}
				getParent().repaint();
			}
		}
		/* ������� �ʴ� �޼ҵ�� : ������� �ʾƵ� implements �߱� ������ �� ���·� �������̵� */
		public void mouseMoved(MouseEvent e) {}
		public void mouseClicked(MouseEvent e) {}
		public void mouseEntered(MouseEvent e) {}
		public void mouseExited(MouseEvent e) {}
	}

	/* �׸��� ���� �����ϱ� */
	public void init() {
		g1.setColor(Color.WHITE);
		g1.fillRect(0, 0, 1000, 800);
		g1.drawImage(new ImageIcon().getImage(), 0, 0, null);
		repaint();
	}
	/* repaint() ȣ�� �� ����Ǵ� �޼ҵ� */
	public void paint(Graphics g) {
		g.drawImage(b1, 0, 0, null);
	}
}
