package com.zjw.view;


import com.zjw.dao.Encryption;
import com.zjw.resource.R;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.text.DecimalFormat;
import java.util.Timer;
import java.util.TimerTask;

public class MainFrame extends JFrame {

    private Timer encryptionTimer=new Timer();
    TopPanel topPanel=new TopPanel(this);
    private double theta=0;

    private int flag=0;
    DecimalFormat dFormat=new DecimalFormat("#.0");
    JButton encryption=new JButton(){
        @Override
        public void paint(Graphics g) {
            super.paint(g);
            if (flag==1 && Encryption.progress<100){
                Graphics2D g2d= (Graphics2D) g;
                g2d.rotate(theta,encryption.getWidth()/2,encryption.getHeight()/2);
                g2d.drawImage(R.load,0,0,null);
                g2d.rotate(-theta,encryption.getWidth()/2,encryption.getHeight()/2);
                g.setFont(new Font("Dialog",   1,   20));
                g.setColor(new Color(212,35,122));
                g.drawString(dFormat.format(Encryption.progress)+"%",36,70);
            }else if(flag==2 || flag==3){
                g.drawImage(R.complete,0,0,null);
                if(flag==2){
                    flag=3;
                    new Thread(){
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(500);
                                flag=0;
                                Encryption.progress=0;
                                encryption.repaint();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }.start();
                }
            }else {
                g.drawImage(R.encryption,0,0,null);
            }
        }
    };


    public MainFrame(){
        setSize(500,700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setUndecorated(true);
        setLayout(null);
        setVisible(true);
        initEncryption();
        addEncryptionListener();
        add(topPanel);
        add(encryption);
    }

    private void initEncryption() {
        encryptionTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (flag==1){
                    theta+=0.1;
                    if (theta>=2*Math.PI){
                        theta=0.1;
                    }
                    encryption.repaint();
                    if (Encryption.progress>=100){
                        flag=2;
                    }
                }else {
                    theta=0;
                }
            }
        },0,20);
        encryption.setSize(128,128);
        encryption.setLocation(186,310);
        encryption.setBackground(R.encryptionOrgColor);
        encryption.setBorder(null);//除去边框
        encryption.setOpaque(true);
    }

    private void addEncryptionListener(){
        encryption.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                encryption.setBackground(R.topActionColor);
                if (flag==0){
                    JFileChooser chooser = new JFileChooser();
                    chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                    int i = chooser.showDialog(new JLabel(), "选择");
                    File file = chooser.getSelectedFile();
                    if (i==JFileChooser.APPROVE_OPTION && file!=null && file.isFile()){
                        flag=1;
                        new Thread(){
                            @Override
                            public void run() {
                                try {
                                    Encryption.start(file);
                                } catch (FileNotFoundException e1) {
                                    e1.printStackTrace();
                                }
                            }
                        }.start();
                    }
                }


            }
        });
        encryption.addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {
                encryption.setBackground(R.encryptionOrgColor);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                encryption.setBackground(R.topActionColor);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                encryption.setBackground(R.encryptionOrgColor);

            }
        });
    }

    public static void main(String[] args){
        MainFrame mainFrame=new MainFrame();
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        topPanel.repaint();
        encryption.repaint();
    }
}
