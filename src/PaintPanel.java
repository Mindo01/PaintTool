import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
/*
 * ���������� �׸��� �׸��� �г�
 * ���콺 Ŭ��/����/�巡�� ���� �����ʸ� ����س��Ҵ�.
 * */
public class PaintPanel extends JPanel {
	private BufferedImage b1 = new BufferedImage(1000, 800, BufferedImage.TYPE_3BYTE_BGR);
	private Graphics g1 = b1.getGraphics(); // ���� �׷����� ����
	String thisPath = null;
	ShapeInfo shape = new ShapeInfo();
	int drawM = 1;	//�׸��� ���	// �̰� �Ǵ� shape���� type�ʵ� �� �� �ϳ� �����ϰ� ����� *******
	/* �׸��� ��忡 �����ϴ� ����� */
	final static int PENCIL = 1;	//���� �
	final static int BRUSH = 2;		//��
	final static int DASH = 3;		//����
	final static int ERASE = 4;		//�����
	final static int LINE = 5;		//����
	final static int REC = 6;		//�׸�
	final static int OVAL = 7;		//��(Ÿ��)
	final static int ROUNDREC = 8;	//�ձ� �׸�
	final static int TRI = 9;		//�ﰢ��
	final static int PENTA = 10;	//������
	final static int HEXA = 11;		//������
	final static int STAR = 12;		//��

	public PaintPanel (String path) {
		setBackground(Color.WHITE);
		g1.fillRect(0, 0, 1000, 800);
		g1.drawImage(new ImageIcon(path).getImage(), 0, 0, null);
		/* �⺻ �� ���� ���� */
		shape.setStroke(1);
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
			g1.setColor(shape.getColor());
			Rectangle rect = shape.getRect(sp, ep);
			switch (drawM)
			{
				case ERASE :
					/* ���찳�� ������ �������� 10�̵��� ���� */
					if (shape.getIntStroke() < 10)
						((Graphics2D) g1).setStroke(new BasicStroke(10));
					g1.setColor(Color.WHITE);
				case PENCIL :
					sp = shape.point.size() > 1 ? shape.point.get(shape.point.size() - 2) : shape.point.firstElement();
					g1.drawLine((int)sp.getX(), (int)sp.getY(), (int)ep.getX(), (int)ep.getY());
					break;
				case BRUSH :
					sp = shape.point.size() > 1 ? shape.point.get(shape.point.size() - 2) : shape.point.firstElement();
					((Graphics2D) g1).setStroke(new BasicStroke(shape.getIntStroke(), BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER));
					g1.drawLine((int)sp.getX(), (int)sp.getY(), (int)ep.getX(), (int)ep.getY());
					break;
				case DASH :
					float[] dash = new float[] { 10, 10, 10, 10 };
					((Graphics2D) g1).setStroke(new BasicStroke(shape.getIntStroke(), 0, BasicStroke.JOIN_MITER, 1.0f, dash, 0));
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
				case TRI :
					drawTriangle(g1, sp, ep, rect, shape.fill);
					break;
				case PENTA :
					drawPentagon(g1, sp, ep, rect, shape.fill);
					break;
				case HEXA :
					drawHexagon(g1, sp, ep, rect, shape.fill);
					break;
				case STAR :
					drawStar(g1, sp, ep, rect, shape.fill);
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
			if (drawM == PENCIL || drawM == BRUSH || drawM == ERASE )	// �����, ���찳�� ��
			{
				((Graphics2D) g1).setStroke(shape.stroke);
				g1.setColor(shape.getColor());
				if (drawM == ERASE)
					g1.setColor(Color.WHITE);
				sp = shape.point.size() > 1 ? shape.point.get(shape.point.size() - 2) : shape.point.firstElement();
				if (drawM == BRUSH)
				{
					((Graphics2D) g1).setStroke(new BasicStroke(shape.getIntStroke(), BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER));
					g1.drawLine((int)sp.getX(), (int)sp.getY(), (int)ep.getX(), (int)ep.getY());
				}
				else
				{
					g1.drawLine((int)sp.getX(), (int)sp.getY(), (int)ep.getX(), (int)ep.getY());
				}
				repaint();
			}
			else
			{
				Graphics g2 = getGraphics();
				Rectangle rect = shape.getRect(sp, ep);
				((Graphics2D) g2).setStroke(shape.stroke);
				g2.setColor(shape.getColor());
				switch (drawM)
				{
					case DASH : 
						float[] dash = new float[] { 10, 10, 10, 10 };
						((Graphics2D) g2).setStroke(new BasicStroke(shape.getIntStroke(), 0, BasicStroke.JOIN_MITER, 1.0f, dash, 0));
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
					case TRI : 
						drawTriangle(g2, sp, ep, rect, shape.fill);
						break;
					case PENTA :
						drawPentagon(g2, sp, ep, rect, shape.fill);
						break;
					case HEXA :
						drawHexagon(g2, sp, ep, rect, shape.fill);
						break;
					case STAR :
						drawStar(g2, sp, ep, rect, shape.fill);
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
	/* TRI : triangle : �ﰢ�� �׸��� �޼ҵ� */
	public void drawTriangle(Graphics g, Point sp, Point ep, Rectangle rect, boolean fill) {
		int r = rect.width;
		int[] x = new int[3];
		int[] y = new int[3];
		x[0] = (sp.x + ep.x)/2;
		y[0] = sp.y;
		x[1] = sp.x;
		y[1] = ep.y;
		x[2] = ep.x;
		y[2] = ep.y;
		/* ���� ä��� ���� */
		if (fill == false)
			g.drawPolygon(x, y, x.length);
		else
			g.fillPolygon(x, y, x.length);
	}
	/* PENTA : pentagon : ������ �׸��� �޼ҵ� */
	public void drawPentagon(Graphics g, Point sp, Point ep, Rectangle rect, boolean fill) {
		int r = rect.width;
		int[] x = new int[5];
		int[] y = new int[5];
		for (int i = 0; i < 5; i++) {
			x[i] = (int) (r * Math.cos(i * 2 * Math.PI / 5));
			y[i] = (int) (r * Math.sin(i * 2 * Math.PI / 5));
		}
		/* ���� ���� �巡���ϴ� ���� �� ������ �߰������� ���� */
		int standX = (sp.x + ep.x)/2;
		int standY = (sp.y + ep.y)/2;
		g.translate(standX, standY);
		/* ���� ä��� ���� */
		if (fill == false)
			g.drawPolygon(x, y, x.length);
		else
			g.fillPolygon(x, y, x.length);
		g.translate(-standX, -standY);
	}
	/* HEXA : hexagon : ������ �׸��� �޼ҵ� */
	public void drawHexagon(Graphics g, Point sp, Point ep, Rectangle rect, boolean fill) {
		int r = rect.width;
		int[] x = new int[6];
		int[] y = new int[6];
		boolean updown = false;
		/* 1. �� ����Ʈ�� y�� ��ǥ�� ������ �� ó�� : �巡�� ���������� ���� y��ǥ���� �� ���� �� */
		if (sp.y > ep.y)
			updown = true;
		else
			updown = false;
		x[0] = (sp.x + ep.x)/2;
		y[0] = sp.y;
		x[1] = sp.x;
		/* �� �ּ� 1���� ���� ���, ������ y��ǥ���� ���ִ� ������ ����. y[2], y[4], y[5] ��� �����ϰ� ó�� */
		y[1] = updown==false?sp.y + Math.abs(ep.y - sp.y)/4:sp.y - Math.abs(ep.y - sp.y)/4;
		x[2] = sp.x;
		y[2] = updown==false?sp.y + Math.abs(ep.y - sp.y)/4*3:sp.y - Math.abs(ep.y - sp.y)/4*3;
		x[3] = (sp.x + ep.x)/2;
		y[3] = ep.y;
		x[4] = ep.x;
		y[4] = updown==false?sp.y + Math.abs(ep.y - sp.y)/4*3:sp.y - Math.abs(ep.y - sp.y)/4*3;
		x[5] = ep.x;
		y[5] = updown==false?sp.y + Math.abs(ep.y - sp.y)/4:sp.y - Math.abs(ep.y - sp.y)/4;
		/* ���� ä��� ���� */
		if (fill == false)
			g.drawPolygon(x, y, x.length);
		else
			g.fillPolygon(x, y, x.length);
	}
	/* STAR : star : �� ��� �׸��� �޼ҵ� */
	public void drawStar(Graphics g, Point sp, Point ep, Rectangle rect, boolean fill) {
		int r = rect.width;
		int[] x = new int[10];
		int[] y = new int[10];
		int cx = r;
		int cy = rect.height;
		int r1 = Math.min(cx, cy) - 10;
		int r2 = Math.min(cx, cy) / 2;
		for (int i = 0; i < 10; i++)
		{
			x[i] = cx + (int)(r1 * Math.cos(i * Math.PI / 5 - Math.PI / 2));
			y[i] = cy + (int)(r1 * Math.sin(i * Math.PI / 5 - Math.PI / 2));
			i++;
			x[i] = cx + (int)(r2 * Math.cos((i+1) * Math.PI / 5 - Math.PI / 2));
			y[i] = cy + (int)(r2 * Math.sin((i+1) * Math.PI / 5 - Math.PI / 2));
		}
		/* ���� ���� �巡���ϴ� ���� �� ������ �߰������� ���� */
		g.translate(sp.x, sp.y);
		g.drawPolygon(x, y, x.length);
		g.translate(-sp.x, -sp.y);
	}
	
	
	/* �׸��� ���� �����ϱ� */
	public void init(String path) {
		g1.setColor(Color.WHITE);
		g1.fillRect(0, 0, 1000, 800);
		g1.drawImage(new ImageIcon(path).getImage(), 0, 0, null);
		repaint();
	}
	/* ���� ���̾�α� ����ϰ� ���� �̸� �ҷ����� �޼ҵ� */
	public String open() {
		/* JFileChooser ��ü ���� */
		JFileChooser chooser = new JFileChooser();
		/* ���� ���� ��ü ���� : png, jpg */
		FileNameExtensionFilter filter1 = new FileNameExtensionFilter("PNG(*.png)", "png");
		FileNameExtensionFilter filter2 = new FileNameExtensionFilter("JPG(*.jpg)", "jpg");
		/* chooser�� ���� ���� ���� */
		chooser.setFileFilter(filter1);
		chooser.setFileFilter(filter2);
		/* ���� ���̾�α� ��� */
		int ret = chooser.showOpenDialog(null);
		/* ���� ���� ���� �� ���â ���� */
		if (ret != JFileChooser.APPROVE_OPTION) {
			JOptionPane.showMessageDialog(null, "������ �������� �ʾҽ��ϴ�", "���", JOptionPane.WARNING_MESSAGE);
			return null;
		}
		/* ����ڰ� ������ ���� �̸� �˾Ƴ��� */
		String pathName = chooser.getSelectedFile().getPath();
		String fileName = chooser.getSelectedFile().getName();
		/* ���� ������ ������ ������ �׸� �г� �ʱ�ȭ */
		init(pathName);
		/* ���� �̸����� Ÿ��Ʋ ���� */
		return fileName;
	}
	/* ���� ���̾�α� ����ϰ� ���� �̸� �ҷ����� �޼ҵ� */
	public String saveAs() {
		/* JFileChooser ��ü ���� */
		JFileChooser chooser = new JFileChooser();
		/* ���� ���� ��ü ���� : png, jpg */
		FileNameExtensionFilter filter1 = new FileNameExtensionFilter("PNG(*.png)", "png");
		FileNameExtensionFilter filter2 = new FileNameExtensionFilter("JPG(*.jpg)", "jpg");
		/* chooser�� ���� ���� ���� */
		chooser.setFileFilter(filter1);
		chooser.setFileFilter(filter2);
		/* ���� ���̾�α� ��� */
		int ret = chooser.showSaveDialog(null);
		/* �ݱ� ��ư �Է� �� �׳� �ݱ� */
		if (ret == JFileChooser.CANCEL_OPTION) {
			return null;
		}

		File file = chooser.getSelectedFile();
		String fileName = file.getName();
		if (fileName.contains(".jpg") || fileName.contains(".JPG") || fileName.contains(".png") || fileName.contains(".PNG"))
			;
		else
			/* �ƹ� Ȯ���ڵ� �Է¾� �� ��, �⺻ Ȯ���ڴ� .png�� �ڵ� ���� */
			file = new File(chooser.getSelectedFile() + ".png");
		/* �̹� �����ϴ� ������ ��� */
		if (file.isFile())
		{
			JLabel message = new JLabel();
			message.setText("�̹� �����ϴ� �����Դϴ�. �ٲٽðڽ��ϱ�?");
			/* �ٲ��� �ʰڴٰ� Ŭ���ϸ� ���� */
			if (!(JOptionPane.showConfirmDialog(null, message, "Ȯ��", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE) == JOptionPane.YES_OPTION))
				return null;
		}
		
		/* �������� ���� ���� : ImageIO.write�� ����� �����̹��� b1�� �������·� ���� */
		try {
			/* ���õ� ���� ������ ���� ������ �޸��� */
			if (chooser.getFileFilter().equals(filter1))
				ImageIO.write(b1, "png", file);
			else
				ImageIO.write(b1, "jpg", file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		thisPath = file.getPath();
		return file.getName();
	}
	public String save() {
		/* �������.png�� ����. ���� ������ ���� ���� ������ ���, '�����̸����� ����'�ϴ� �ܰ�� ������ */
		if (thisPath == null)
		{
			return saveAs();
		}
		/* filechooser �θ� �� ����, �� ���Ͽ� �״�� ���� */
		File file = new File(thisPath);
		String fileName = file.getName();
		
		/* �������� ���� ���� : ImageIO.write�� ����� �����̹��� b1�� �������·� ���� */
		try {
			/* ���õ� ���� ������ ���� ������ �޸��� */
			if (fileName.contains(".png") || fileName.contains(".PNG"))
				ImageIO.write(b1, "png", file);
			else
				ImageIO.write(b1, "jpg", file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return fileName;
	}
	/* repaint() ȣ�� �� ����Ǵ� �޼ҵ� */
	public void paint(Graphics g) {
		g.drawImage(b1, 0, 0, null);
	}
}
