package com.zjw.view;


import com.zjw.resource.R;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Timer;
import java.util.TimerTask;

public class TopPanel extends JPanel {

    private JFrame frame;
    private int x;
    private int y;
    private int orgX;
    private int orgY;
    private JButton close;
    private double centerX=40;
    private double centerY=40;
    private double theta;
    // 获取屏幕的边界
    Insets screenInsets ;

    private Timer inTimer=new Timer();
    private Timer outTimer=new Timer();
    private boolean inFlag=false;
    private boolean outFlag=false;

    public TopPanel(JFrame frame){
        this.frame=frame;
        setSize(500,80);
        setBackground(R.topOrgColor);

        JLabel title=new JLabel("加密小程序！");
        title.setForeground(new Color(212,35,122));
        title.setFont(new   java.awt.Font("Dialog",   1,   20));
        title.setSize(120,80);
        title.setLocation(190,0);

        add(title);
        addCloseBtn();
        addMoveListener();
        addCloseListener();
        screenInsets= Toolkit.getDefaultToolkit().getScreenInsets(frame.getGraphicsConfiguration());
    }

    private void addCloseBtn() {
        close=new JButton(){
            @Override
            public void paint(Graphics g) {
                super.paint(g);
                Graphics2D g2d= (Graphics2D) g;
                g2d.rotate(theta,centerX,centerY);
                g2d.drawImage(R.close,8,8,null);
                g2d.rotate(-theta,centerX,centerY);
            }
        };
        close.setSize(80,80);
        close.setLocation(0,0);
        close.setBackground(R.topOrgColor);
        close.setBorder(null);//除去边框
        close.setOpaque(true);
        addCloseListener();
        add(close);
    }

    private void addCloseListener() {
        inTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (inFlag && theta<=2*Math.PI){
                    theta+=0.1;
                    close.repaint();
                }
            }
        },0,10);
        outTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (outFlag && theta>=0){
                    theta-=0.1;
                    close.repaint();
                }else {
                    outFlag=false;
                }
            }
        },0,10);
        close.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        close.addMouseListener(new MouseAdapter() {


            @Override
            public void mousePressed(MouseEvent e) {
                close.setBackground(R.closePressedColor);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                TopPanel.this.setBackground(R.topActionColor);
                close.setBackground(R.topActionColor);
                inFlag=true;
                outFlag=false;
            }

            @Override
            public void mouseExited(MouseEvent e) {
                TopPanel.this.setBackground(R.topOrgColor);
                close.setBackground(R.topOrgColor);
                inFlag=false;
                outFlag=true;

            }
        });
    }
    private void addMoveListener(){
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                x=e.getXOnScreen();
                y=e.getYOnScreen();
                orgX=frame.getX();
                orgY=frame.getY();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (frame.getY()<screenInsets.top){
                    frame.setLocation(frame.getX(),screenInsets.top);
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                setBackground(R.topActionColor);
                close.setBackground(R.topActionColor);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                setBackground(R.topOrgColor);
                close.setBackground(R.topOrgColor);
            }
        });
        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                int tempX=e.getXOnScreen()-x+orgX;
                int tempY=e.getYOnScreen()-y+orgY;
                frame.setLocation(tempX,tempY);
            }
        });
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        close.repaint();
    }
}
