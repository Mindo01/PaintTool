package com.mjpaint.controller;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.mjpaint.model.ShapeInfo;
/**
 * ���������� �׸��� �׸��� �г�
 * ���콺 Ŭ��/����/�巡�� ���� �����ʸ� ����س��Ҵ�.
 * 1. PaintPanel Ŭ���� ���� : ShapeInfo�� shape ��ü�� ���� ���� ��� 
 * 						/ �������� �׸� �׸��� �г��� paint() �޼ҵ� ����
 * 2. save / open / saveAs �޼ҵ� ���� : ����, ����, �ٸ� �̸����� ����
 * 3. drawTriangle/Pentagon/Hexagon/Star �޼ҵ� �������� Ư������ �׸��� ����
 * 4. init() �޼ҵ�� �׸��� ���� �����ϴ� �޼ҵ� ����
 * 5. �׸��� ��忡 �����ϴ� ����� ���� :
 * 		PENCIL BRUSH HIGHLIGHT ERASE LINE REC OVAL ROUNDREC TRI PENTA HEXA STAR
 * */
public class PaintPanel extends JPanel {
	/* ���� �̹��� ��ü 2�� 
	 * - b1 : ���������� �׸��� ������ �����̹���
	 * - b2 : copy, cut, paste �� ����� �ӽ� ���� �����̹��� 
	 * �̿� �����ϴ� �� �׷��� ��ü : g1, g2 */
	private BufferedImage b1 = new BufferedImage(1000, 800, BufferedImage.TYPE_3BYTE_BGR);
	private BufferedImage b2 = new BufferedImage(1000, 800, BufferedImage.TYPE_3BYTE_BGR);
	private Graphics g1 = b1.getGraphics(); // ���� �׷����� ����
	private Graphics g2 = b2.getGraphics(); // ���� �س��� ����
	/* ���� ������ ��θ� �����ϴ� ���� */
	public String thisPath = null;
	public ShapeInfo shape;		// shape ������ ������ �����ϴ� Ŭ������ ��ü
	public Color bgColor;			// ���� ����
	public int drawM = 1;			// �׸��� ���
	public boolean changedFile = false;	//������� ���ο� ���� ����
	Rectangle selectArea;	// ���� ���� �����ϴ� �׸�
	/* �׸��� ��忡 �����ϴ� ����� */
	final static int SELECT = 0;	//���ø��
	final static int PENCIL = 1;	//���� �
	final static int BRUSH = 2;		//��
	final static int HIGHLIGHT = 3;		//����
	final static int ERASE = 4;		//�����
	final static int LINE = 5;		//����
	final static int REC = 6;		//�׸�
	final static int OVAL = 7;		//��(Ÿ��)
	final static int ROUNDREC = 8;	//�ձ� �׸�
	final static int TRI = 9;		//�ﰢ��
	final static int PENTA = 10;	//������
	final static int HEXA = 11;		//������
	final static int STAR = 12;		//��
	final static int SPOID = 13;	//�����̵�

	/* ������ : �г� �⺻ ����, ���� ����, Ŀ�� ���� */
	public PaintPanel (String path) {
		/* �⺻ ���� �׸��� */
		g1.fillRect(0, 0, 1000, 800);
		g1.drawImage(new ImageIcon(path).getImage(), 0, 0, null);
		/* �⺻ ���� �Ͼ������ ���� */
		bgColor = Color.WHITE;
		/* ���� ������ ��� ��ü ���� */
		shape = new ShapeInfo();
		/* ���� ������ �����ִ� �簢�� ��ü ���� */
		selectArea = new Rectangle(-1, 0, 0, 0);
		/* PaintPanel �����ϸ�, ���콺 Ŀ�� ��� ���� */
		setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
		/* �гο� ������ ���콺�� ���� ������ ��� */
		addMouseListener( new PaintListener());
		addMouseMotionListener( new PaintListener());
	}
	
	/** ���õ� ������ ������ �δ� �޼ҵ�
	 */
	public void copy() {
		g2.drawImage(b1, 0, 0, 1000, 800, selectArea.x, selectArea.y, selectArea.x + selectArea.width, selectArea.y + selectArea.height, null);
	}

	/** ������ ������ �����Ѱų� �߶󳽰��� ���̴� �޼ҵ�, ���ÿ����� ũ�� �巡���ϸ� Ȯ���ؼ�, �۰��ϸ� ����ؼ�, �����ϸ� �Ȱ���
	 * �ٿ��־���
	 * */
	public void paste() {
		g1.drawImage(b2, selectArea.x, selectArea.y, selectArea.x + selectArea.width, selectArea.y + selectArea.height, 0, 0, 1000, 800, null);
		repaint();
	}

	/** ������ ������ �߶󳻴� �޼ҵ�(�߶��� ������ ������� ����)
	 */
	public void cut() {
		g2.drawImage(b1, 0, 0, 1000, 800, selectArea.x, selectArea.y, selectArea.x + selectArea.width, selectArea.y + selectArea.height, null);
		g1.setColor(Color.white);
		g1.fillRect(selectArea.x, selectArea.y, selectArea.width, selectArea.height);
		repaint();
	}
	
	
	/* ���콺 Ŭ���� Ŭ������, �巡�׿� ���� �ٸ��� �����ϴ� ������ */
	class PaintListener implements MouseListener, MouseMotionListener {
		/* 1. ���콺 Ŭ���Ǿ��� ���� �̺�Ʈ */
		public void mousePressed(MouseEvent e) {
			/* ���� ����� �� */
			if (drawM == SELECT) 
			{
				selectArea.x = -1; // �巡�� ���� ��ġ
			}
			/* �����̵� �� ��, �ƹ� �۵� ���� */
			if (drawM == SPOID)
				return ;
			/* ������ �� ��, ������ ���� / �ƴϸ� �ٽ� ������ 255(�ִ밪)���� ���� */
			if (drawM == HIGHLIGHT)
				shape.setOpacity(shape.getColor(), 10);
			else
				shape.setOpacity(shape.getColor(), 255);
			/* ù ������ shape ��ü �� ����Ʈ ���Ϳ� ���� 
			 * (�� ���¿����� ����Ʈ ���� ���� �ƹ� ����Ʈ�� ���� ����) */
			shape.add(e.getPoint());
			((Graphics2D)g1).setStroke(shape.getStroke(drawM));	// �� ���� ����
			g1.setColor(shape.getColor());					// �� �� / ä��� �� ����
			/* ���� ���� ���� true�� ���� */
			changedFile = true;
		}
		/* 2. ���콺 Ŭ���� �������� ���� �̺�Ʈ
		 * ���� �׸���� �� �� ���������� �гο� �׷��� (g1 ���) */
		public void mouseReleased(MouseEvent e) {
			/* ���� ����� �� */
			if (drawM == SELECT)
				return ;
			/* �����̵� �� ��, 
			 * ���콺 Ŭ�� ������ ������ �ȼ���(mouseMoved�� ���� spoidBtn�� ������ �Ǿ� ����)
			 * ���� ���� ��, ��� �� ���� */
			if (drawM == SPOID)
			{
				if (e.isMetaDown() == false) // ���� Ŭ�� : ���� �� ����
				{
					MainPaint.nowColor.setColor(MainPaint.spoidBtn.getBackground());
					shape.setColor(MainPaint.spoidBtn.getBackground());
				}
				else	// ������ Ŭ�� : ��� �� ����
				{
					MainPaint.backColor.setColor(MainPaint.spoidBtn.getBackground());
					bgColor = MainPaint.spoidBtn.getBackground();
				}
				return ;
			}
			// ����Ʈ shape ��ü �� ���Ϳ� ���� (���� ����)
			shape.add(e.getPoint());
			/* ����Ʈ shape ��ü �� ���Ϳ� ����Ǿ� �ִ� �������� ���� �޾ƿ��� */
			Point sp = shape.get().firstElement();	// ������
			Point ep = shape.get().lastElement();	// ����
			Rectangle rect = shape.getRect(sp, ep);
			/* �׸��� ��忡 ���� �ٸ��� �׸��� */
			switch (drawM)
			{
				case ERASE :
					g1.setColor(bgColor);	// ���찳�� ���� �������� ��ĥ��
				case HIGHLIGHT :// �������� ���� �ձ۰� �ϴ� �� �Ӽ���, pressed���� �������� ������ ���¿��� �׷���
				case BRUSH :	// ���� ���� �ձ۰� �ϴ� stroke �Ӽ����� �����ϰ�, �׸��� �κ��� pencil�� ����
					((Graphics2D) g1).setStroke(new BasicStroke(shape.getIntStroke(drawM), BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER));
				case PENCIL :
					sp = shape.get().size() > 1 ? shape.get().get(shape.get().size() - 2) : shape.get().firstElement();
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
						g1.drawRoundRect(rect.x, rect.y, rect.width, rect.height, shape.getRoundRec(), shape.getRoundRec());	//�ڿ� �� ���ڴ� �ձ� ���� ��ġ
					else
						g1.fillRoundRect(rect.x, rect.y, rect.width, rect.height, shape.getRoundRec(), shape.getRoundRec());
					break;
				/* TRI, PENTA, HEXA, STAR ���� ���� ���� �޼ҵ带 �̿��� ������ ������ (drawPolygon()���) */
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
		
		/* 3. ���콺 Ŭ�� �� �巡�� �� ��, �ӽ÷� �������� ������ �̺�Ʈ ���� 
		 * ��, PENCIL, BRUSH, ERASE�� �巡���� ���� ������ �׷��� : g1 �׷��� ��ü ���
		 * ������ �������� �ӽ÷θ� ���������� ���� ��ü�� g2 �׷��� ��ü�� �׸� */
		public void mouseDragged(MouseEvent e) {
			/* �����̵� �� ��, �ƹ��͵� ���ϰ� ���� */
			if (drawM == SPOID)
				return ;
			/* ���� ����� �� */
			if (drawM == SELECT)
			{
				if (selectArea.x == -1) {	//�ʱⰪ ����
					selectArea.setBounds(e.getX(), e.getY(), 0, 0);
				}
				/* �巡�� �Ǵ� ����Ʈ ��ǥ�� �������� ����, �ʺ� ���� */
				selectArea.width = e.getX() - selectArea.x;
				selectArea.height = e.getY() - selectArea.y;
				repaint();
				return ;
			}
			shape.add(e.getPoint());
			Point sp = shape.get().firstElement();
			Point ep = shape.get().lastElement();
			/* �׸��� ��忡 ���� �ٸ� ��Ȳ����
			 * �����, ��, ���찳, �������� �巡�׵� �״�� �׸��ǿ� �Է�
			 * ������ ����� �巡�� ������ �׳� �����ֱ⸸ (������ �Է��� �ƴ� - ���� ��ü g2 ���)
			 */
			if (drawM == PENCIL || drawM == BRUSH || drawM == ERASE || drawM == HIGHLIGHT ) //�����, ��, ���찳, ������
			{
				((Graphics2D) g1).setStroke(shape.getStroke(drawM));
				if (drawM == ERASE)	// ���찳�� ��, �� ���� / �� ����
				{
					/* ��� ���� ���찳 �������� ���� */
					g1.setColor(bgColor);
					/* ���찳�� ������ �������� 10�̵��� ���� */
					if (shape.getIntStroke(drawM) < 10)
					{
						((Graphics2D) g1).setStroke(new BasicStroke(10));
						MainPaint.strokeValue.setSelectedIndex(2);	// �� ���� �޺��ڽ� ������ ������ ������ 10���� ����
					}
					((Graphics2D) g1).setStroke(shape.getStroke(drawM));
				}
				/* �� �׸� ��, ���� ����� ����ǵ���, �ٷ� ������ ����Ʈ�� ����ؼ� ���� */
				sp = shape.get().size() > 1 ? shape.get().get(shape.get().size() - 2) : shape.get().firstElement();
				/* ��, �������� ��� ���� �ձ� ������ stroke�Ӽ� ���� */
				if (drawM == BRUSH || drawM == HIGHLIGHT)
				{
					((Graphics2D) g1).setStroke(new BasicStroke(shape.getIntStroke(drawM), BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER));
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
				/* ���� �� ������ : �ӽ÷� �׸����� Graphics ��ü�� g2�� �ӽ������� �޾� �׸��� 
				 * �׷��� ��ü�� g2�� �͸� ����, released�� ���� */
				Graphics g2 = getGraphics();				//�ӽ� �׷��� ��ü �޾ƿ���
				Rectangle rect = shape.getRect(sp, ep);		//������ ���� �������� �簢�� �޾ƿ��� (���� �׸��⿡ ����)
				setStrokeType(g2, shape.getStrokeType());	//�ӽ� ��ü, g1 ��ü �� ���� �����ϱ�
				g2.setColor(shape.getColor());				//�ӽ� ��ü �� �� �����ϱ�
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
							g2.drawRoundRect(rect.x, rect.y, rect.width, rect.height, shape.getRoundRec(), shape.getRoundRec());	//�ڿ� �� ���ڴ� �ձ� ���� ��ġ
						else
							g2.fillRoundRect(rect.x, rect.y, rect.width, rect.height, shape.getRoundRec(), shape.getRoundRec());
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
				/* g2 ��ü�� �׸� �� �Ź� �����ִ� �κ� : �θ� �ҷ��� repaint�� �ؼ� ��� �׸� ���� ������ */
				getParent().repaint();
			}
		}
		/* �����̵� ��� �����ϱ� ���� ������ mouseMoved 
		 * ���콺�� ������ �� �� ��ǥ�� RGB������ color ��ü�� ������ �����̵� �������� �����ش� */
		public void mouseMoved(MouseEvent e) {
			if (drawM != SPOID)
			{
				MainPaint.spoidBtn.setOpaque(false);
				return ;
			}
			Color color = new Color(b1.getRGB(e.getPoint().x, e.getPoint().y));
			MainPaint.spoidBtn.setBackground(color);
			MainPaint.spoidBtn.setOpaque(true);
			//System.out.println("("+e.getPoint().x+", "+e.getPoint().y+") RGB�� : "+color.getRed()+", "+color.getGreen()+", "+color.getBlue());
		}
		/* ������� �ʴ� �޼ҵ�� : ������� �ʾƵ� implements �߱� ������ �� ���·� �������̵� */
		public void mouseClicked(MouseEvent e) {}
		public void mouseEntered(MouseEvent e) {}
		public void mouseExited(MouseEvent e) {}
	}
	/** �� ���� �������ִ� �޼ҵ� */
	public void setStrokeType(Graphics g, int type)
	{
		float[] dash;
		switch (type)
		{
			case 1 : // �Ǽ� : �ƹ��͵� ������
				((Graphics2D) g).setStroke(shape.getStroke(drawM));
				((Graphics2D) g1).setStroke(shape.getStroke(drawM));
				break;	
			case 2 : // ���� : �� ���� �ٲ��ֱ�
				dash = new float[] { 5, 5, 5, 5 };
				((Graphics2D) g).setStroke(new BasicStroke(shape.getIntStroke(drawM), 0, BasicStroke.JOIN_MITER, 1.0f, dash, 0));
				((Graphics2D) g1).setStroke(new BasicStroke(shape.getIntStroke(drawM), 0, BasicStroke.JOIN_MITER, 1.0f, dash, 0));
				break;
			case 3 : // ���� : �� ���� �ٲ��ֱ�
				dash = new float[] { 10, 10, 10, 10 };
				((Graphics2D) g).setStroke(new BasicStroke(shape.getIntStroke(drawM), 0, BasicStroke.JOIN_MITER, 1.0f, dash, 0));
				((Graphics2D) g1).setStroke(new BasicStroke(shape.getIntStroke(drawM), 0, BasicStroke.JOIN_MITER, 1.0f, dash, 0));
				break;
			case 4 : // ���� : �� ���� �ٲ��ֱ�
				dash = new float[] { 10, 10, 10, 10 };
				((Graphics2D) g).setStroke(new BasicStroke(shape.getIntStroke(drawM), BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER, 1.0f, dash, 0));
				((Graphics2D) g1).setStroke(new BasicStroke(shape.getIntStroke(drawM), BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER, 1.0f, dash, 0));
				break;
		}
	}
	/** TRI : triangle : �ﰢ�� �׸��� �޼ҵ� */
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
	/** PENTA : pentagon : ������ �׸��� �޼ҵ� */
	public void drawPentagon(Graphics g, Point sp, Point ep, Rectangle rect, boolean fill) {
		int rw = rect.width;
		int rh = rect.height;
        int[] x = new int[5];
        int[] y = new int[5];
        x[0] = (sp.x+ep.x)/2;
        y[0] = sp.y;
        x[1] = sp.x;
        /* �� ����Ʈ�� x�� �Ǵ� y�� ��ǥ�� ������ �� ó�� : �巡�� ���������� ���� x �Ǵ� y ��ǥ���� �� ���� �� */
        y[1] = sp.y < ep.y? sp.y+rh*5/14 : sp.y-rh*5/14;
        x[2] = sp.x < ep.x? sp.x+rw*1/5 : sp.x-rw*1/5;
        y[2] = ep.y;
        x[3] = sp.x < ep.x? sp.x+rw*29/36 : sp.x-rw*29/36;
        y[3] = ep.y;
        x[4] = ep.x;
        y[4] = sp.y < ep.y? sp.y+rh*5/14 : sp.y-rh*5/14;

		/* ���� ä��� ���� */
		if (fill == false)
			g.drawPolygon(x, y, x.length);
		else
			g.fillPolygon(x, y, x.length);
	}
	/** HEXA : hexagon : ������ �׸��� �޼ҵ� */
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
	/** STAR : star : �� ��� �׸��� �޼ҵ� */
	public void drawStar(Graphics g, Point sp, Point ep, Rectangle rect, boolean fill) {
		int rw = rect.width;
		int rh = rect.height;
        // cx��� ������ �� x��ǥ�� �߰���
        int cx=(sp.x+ep.x)/2;
        // ù ��° x��ǥ�� ���� �����ʿ� ��ġ�ϴ� x��ǥ
        int x1=ep.x>sp.x?sp.x:ep.x;
        // �� ��° x��ǥ�� ���� ���ʿ� ��ġ�ϴ� x��ǥ
        int x2=ep.x>sp.x?ep.x:sp.x;
        // ù ���� y��ǥ�� ���� ���� ��ġ�� y��ǥ (������������ y�� ���ΰ��� Ŀ������ �ڹٿ����� �۾���)
        int y1=ep.y>sp.y?ep.y:sp.y;
        // �� ���� y��ǥ�� ���� �Ʒ��� ��ġ�� y��ǥ (������������ y�� ���ΰ��� Ŀ������ �ڹٿ����� �۾���)
        int y2=ep.y>sp.y?sp.y:ep.y;
		int x[]={cx, x1+rw*8/13, x2, x1+rw*25/35, x1+rw*29/36, cx,x1+rw*1/5, x1+rw*10/35, x1, x1+rw*5/13};
        int y[]={y2, y2+rh*5/14, y2+rh*5/14, y2+rh*19/31, y1, y2+rh*23/30, y1, y2+rh*19/31, y2+rh*5/14, y2+rh*5/14};
		/* ���� ���� �巡���ϴ� ���� �� ������ �߰������� ���� */
        if (fill == false)
        	g.drawPolygon(x, y, x.length);
        else
        	g.fillPolygon(x, y, x.length);
	}
	
	
	/** �׸��� ���� �����ϱ� */
	public void init(String path) {
		if (changedFile == true)
		{
			/* ���� ���� ������ ���� ����� */
			JLabel message = new JLabel();
			message.setText("���� ������ �����Ͻðڽ��ϱ�?");
			int ret = JOptionPane.showConfirmDialog(null, message, "Ȯ��", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
			/* 1. ���� YES(Ȯ��) ���� ��� */
			if (ret == JOptionPane.YES_OPTION)	
			{
				String str = saveAs();
				if (str == null)
					return ;
			}
			/* 2. ���� CANCEL(���) �Ǵ� â ���� ��ư ���� ��� */
			if (ret == JOptionPane.CANCEL_OPTION || ret == JOptionPane.CLOSED_OPTION)
			{
				return ;
			}
		}
		g1.setColor(Color.WHITE);
		g1.fillRect(0, 0, 1000, 800);
		g1.drawImage(new ImageIcon(path).getImage(), 0, 0, null);
		repaint();
		// ���泻�� �÷��� �ٽ� �������
		changedFile = false;
	}
	/** ���� ���̾�α� ����ϰ� ���� �̸� �ҷ����� �޼ҵ� */
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
		thisPath = pathName;
		/* ���� �̸����� Ÿ��Ʋ ���� */
		return fileName;
		
	}
	/** ���� ���̾�α� ����ϰ� ���� �̸� �ҷ����� �޼ҵ� */
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
		if (ret == JFileChooser.CANCEL_OPTION || ret == JOptionPane.CLOSED_OPTION) {
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
		/* ���� ���� ��θ� ��Ÿ���� ������ �������ֱ� */
		thisPath = file.getPath();
		return file.getName();
	}
	/** �̹��� ���Ϸ� �����ϴ� �޼ҵ� */
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
		super.paint(g);
		g.drawImage(b1, 0, 0, this);
		
		if (drawM == SELECT) { // ���ø���� ��
			if (selectArea.x != -1) {
				/* ���� ��� �� ���, Color.CYAN ���� + ª�� �������� ���������� ����! */
				Graphics g2 = g;
				float[] dash = new float[] { 5, 5, 5, 5 };
				((Graphics2D) g2).setStroke(new BasicStroke(2, 0, BasicStroke.JOIN_MITER, 1.0f, dash, 0));
				g2.setColor(Color.CYAN);
				g2.drawRect(selectArea.x, selectArea.y, selectArea.width, selectArea.height);
			}
		}
	}
}