package com.company;

import javax.swing.*;
import java.awt.*;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Panel2 extends JPanel{

    FileWriter f = new FileWriter("plik.txt");
    JFrame okno = new JFrame("Okno2!");
    ArrayList<Kula> listaKul;

    class Kula
    {
        int x;
        int y;
        int size;

        Kula(int x,int y,int s)
        {
            this.x=x;
            this.y=y;
            this.size=s;
        }
    }


    void zapis(int tx,int ty,int ts,int kx,int ky,int ks) throws IOException {
        f.append(tx +" "+ ty +" "+ ts +" "+ kx +" "+ ky +" "+ ks +"\n");
    }

    void zamknij() throws IOException {
        f.close();
    }

    public Panel2() throws IOException {
    }

    void onko() throws IOException {
        okno.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        okno.setLocation(600,0);
        okno.setPreferredSize(new Dimension(600,600));
        okno.pack();
        okno.setVisible(true);

        FileReader file = new FileReader("plik.txt");
        Scanner scanner = new Scanner(file);

        while (scanner.hasNext())
        {
            String sx = scanner.next();
            String sy = scanner.next();
            String ss = scanner.next();

            int x = Integer.parseInt(sx);
            int y = Integer.parseInt(sy);
            int s = Integer.parseInt(ss);

            listaKul.add(new Kula(x,y,s));
        }
        scanner.close();
        file.close();


    }

    void zamknijOkno()
    {
        okno.setVisible(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        for(Kula k : this.listaKul)
        {
            g.setColor(Color.black);
            g.drawOval(k.x - (k.size / 2), k.y - (k.size / 2), k.size,k.size);
        }

    }

}
