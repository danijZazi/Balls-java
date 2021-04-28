package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.ArrayList;

class Panel extends JPanel {

    private ArrayList<Kula> listaKul;
    private int size = 20;
    private Timer timer;
    private final int DELAY = 16;
    Panel2 panel2;

    {
        try {
            panel2 = new Panel2();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Panel() {
        listaKul = new ArrayList<>();
        setBackground(Color.BLACK);

        addMouseListener(new Listener());
        addMouseMotionListener(new Listener());
        addMouseWheelListener(new Listener());

        timer = new Timer(DELAY, new Listener());
        timer.start();
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        for (Kula k : listaKul) {

            g.setColor(k.kolor);
            g.fillOval(k.x - k.size / 2, k.y - k.size / 2, k.size, k.size);

        }
        //licznik kul
        g.setColor(Color.YELLOW);
        g.drawString(Integer.toString(listaKul.size()), 40, 40);

    }

    private class Listener implements MouseListener, MouseMotionListener, MouseWheelListener, ActionListener {

        @Override
        public void mouseClicked(MouseEvent mouseEvent) {

        }

        @Override
        public void mousePressed(MouseEvent mouseEvent) {
            boolean moznaStworzyc=true;
            for(Kula k:listaKul) {
                double sumaPromieni=(size/2)+(k.size/2);
                double odleglosc=Math.sqrt(((mouseEvent.getX()-k.x)*(mouseEvent.getX()-k.x))+((mouseEvent.getY()-k.y)*(mouseEvent.getY()-k.y)));
                if(odleglosc<=sumaPromieni)
                {
                    moznaStworzyc=false;
                }
            }

            if(moznaStworzyc) {
                listaKul.add(new Kula(mouseEvent.getX(), mouseEvent.getY(), size));
                repaint();
            }
            
        }

        @Override
        public void mouseReleased(MouseEvent mouseEvent) {

        }

        @Override
        public void mouseEntered(MouseEvent mouseEvent) {
            panel2.zamknijOkno();
        }

        @Override
        public void mouseExited(MouseEvent mouseEvent) {
            try {
                panel2.zamknij();
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                panel2.onko();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            for (Kula k : listaKul) {

                k.update();
            }
            repaint();
        }

        @Override
        public void mouseDragged(MouseEvent mouseEvent) {
            boolean moznaStworzyc=true;
            for(Kula k:listaKul) {
                double sumaPromieni=(size/2)+(k.size/2);
                double odleglosc=Math.sqrt(((mouseEvent.getX()-k.x)*(mouseEvent.getX()-k.x))+((mouseEvent.getY()-k.y)*(mouseEvent.getY()-k.y)));
                if(odleglosc<=sumaPromieni)
                {
                    moznaStworzyc=false;
                }
            }

            if(moznaStworzyc) {
                listaKul.add(new Kula(mouseEvent.getX(), mouseEvent.getY(), size));
                repaint();
            }
        }

        @Override
        public void mouseMoved(MouseEvent mouseEvent) {

        }

        @Override
        public void mouseWheelMoved(MouseWheelEvent mouseWheelEvent) {
            if(mouseWheelEvent.getWheelRotation()>0 && size>6){size=size-2;}
            if(mouseWheelEvent.getWheelRotation()<0){size=size+2;}
        }
    }

    private class Kula {

        public int x, y, size, xspeed = 0, yspeed = 0;
        public boolean xc = false, yc = false;
        public Color kolor;
        private final int MAX_SPEED = 5;

        public Kula(int x, int y, int size) {

            this.x = x;
            this.y = y;
            this.size = size;

            // składowe RGB
            kolor = new Color((float) Math.random(), (float) Math.random(), (float) Math.random());


            xspeed = (int) (Math.random() * MAX_SPEED * 2 - MAX_SPEED);

            yspeed = (int) (Math.random() * MAX_SPEED * 2 - MAX_SPEED);


            while(xspeed==0 && yspeed==0) {
                xspeed = (int) (Math.random() * MAX_SPEED * 2 - MAX_SPEED);
                yspeed = (int) (Math.random() * MAX_SPEED * 2 - MAX_SPEED);
            }
            
        }

        public void update() {
            x += xspeed;
            y += yspeed;

            if (x - size / 2 <= 0 || x + (size / 2) >= getWidth()) {

                xspeed = -xspeed;
            }

            if (y - size / 2 <= 0 || y + size / 2 >= getHeight())
                yspeed = -yspeed;


            //kolizje
            for (Kula k:listaKul)
            {
                if (this.czyKolizja(k))
                {
                    this.odbicie(k);
                }
            }


        }

        private boolean czyKolizja(Kula kula) {
            double sumaPromieni=(this.size/2)+(kula.size/2);
            double odleglosc=Math.sqrt(((this.x-kula.x)*(this.x-kula.x))+((this.y-kula.y)*(this.y-kula.y)));
            if(odleglosc<=sumaPromieni && kula!=this) {
                System.out.println("Odleglosc = "+odleglosc+" Promien = "+sumaPromieni);
                try {
                    panel2.zapis(this.x,this.y,this.size,kula.x,kula.y,kula.size);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return true;
            }
            else return false;

        }

        private void odbicie(Kula kula) {
            double thisYSpeed=(this.yspeed*(this.size-kula.size)+(2*this.size*kula.yspeed))/(this.size+kula.size);
            double thisXSpeed=(this.xspeed*(this.size-kula.size)+(2*this.size*kula.xspeed))/(this.size+kula.size);
            double kulaXSpeed=(kula.xspeed*(kula.size-this.size)+(2*kula.size*this.xspeed))/(kula.size+this.size);
            double kulaYSpeed=(kula.yspeed*(kula.size-this.size)+(2*kula.size*this.yspeed))/(kula.size+this.size);

            this.yspeed= (int) Math.round(thisYSpeed);
            this.xspeed= (int) Math.round(thisXSpeed);
            kula.xspeed= (int) Math.round(kulaXSpeed);
            kula.yspeed= (int) Math.round(kulaYSpeed);
        }

    }
}