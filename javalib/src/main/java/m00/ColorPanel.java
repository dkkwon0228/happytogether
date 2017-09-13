/*

2008mar18

color menu: 'each color represents a different label'

*/

package m00;

import javax.swing.*;
import java.awt.*;

////////////////////////////////////////////////////////////////////////////
public class ColorPanel extends JPanel {
    private JButton currentColor, red, green, blue, cyan, magenta, yellow,
             pink, orange, brown, purple, lightblue, lightgreen;

    public ColorPanel() {
        super();
        initComponents();
        this.currentColor = red;//magenta;
    }
    public ColorPanel(String m) {
        super();
        initComponents2();
        this.currentColor = red;
    }

    public Color getCurrentColor() {
        return currentColor.getBackground();
    } 
    private void initComponents2() {
        this.setLayout(new GridBagLayout());
        GridBagConstraints gb;

        red = new JButton();
        red.setBackground(Color.red);
        red.setPreferredSize(new Dimension(16, 16));
        red.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                currentColor = red;
                System.out.println("red");
            }
        });
        gb = new GridBagConstraints(); gb.gridx = 0; gb.gridy = 0;
        this.add(red, gb);

        
        blue = new JButton();
        blue.setBackground(Color.blue);
        blue.setPreferredSize(new Dimension(16, 16));
        blue.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                currentColor = blue;
                System.out.println("blue");
            }
        });
        gb = new GridBagConstraints(); gb.gridx = 1; gb.gridy = 0;
        this.add(blue, gb);
    }    

    private void initComponents() {
        this.setLayout(new GridBagLayout());
        GridBagConstraints gb;

        red = new JButton();
        red.setBackground(Color.red);
        red.setPreferredSize(new Dimension(16, 16));
        red.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                currentColor = red;
                System.out.println("red");
            }
        });
        gb = new GridBagConstraints(); gb.gridx = 0; gb.gridy = 0;
        this.add(red, gb);

        green = new JButton();
        green.setBackground(Color.green);
        green.setPreferredSize(new Dimension(16, 16));
        green.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                currentColor = green;
                System.out.println("green");
            }
        });
        gb = new GridBagConstraints(); gb.gridx = 1; gb.gridy = 0;
        this.add(green, gb);
        
        blue = new JButton();
        blue.setBackground(Color.blue);
        blue.setPreferredSize(new Dimension(16, 16));
        blue.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                currentColor = blue;
                System.out.println("blue");
            }
        });
        gb = new GridBagConstraints(); gb.gridx = 2; gb.gridy = 0;
        this.add(blue, gb);
    
        cyan = new JButton();
        cyan.setBackground(Color.cyan);
        cyan.setPreferredSize(new Dimension(16, 16));
        cyan.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                currentColor = cyan;
                System.out.println("cyan");
            }
        });
        gb = new GridBagConstraints(); gb.gridx = 0; gb.gridy = 1;
        this.add(cyan, gb);
        
        magenta = new JButton();
        magenta.setBackground(Color.magenta);
        magenta.setPreferredSize(new Dimension(16, 16));
        magenta.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                currentColor = magenta;
                System.out.println("magenta");
            }
        });
        gb = new GridBagConstraints(); gb.gridx = 1; gb.gridy = 1;
        this.add(magenta, gb);
        
        yellow = new JButton();
        yellow.setBackground(Color.yellow);
        yellow.setPreferredSize(new Dimension(16, 16));
        yellow.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                currentColor = yellow;
                System.out.println("yellow");
            }
        });
        gb = new GridBagConstraints(); gb.gridx = 2; gb.gridy = 1;
        this.add(yellow, gb);
        
        pink = new JButton();
        pink.setBackground(Color.pink);
        pink.setPreferredSize(new Dimension(16, 16));
        pink.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                currentColor = pink;
                System.out.println("pink");
            }
        });
        gb = new GridBagConstraints(); gb.gridx = 3; gb.gridy = 0;
        this.add(pink, gb);
    
        orange = new JButton();
        orange.setBackground(new Color(255,128,0));
        orange.setPreferredSize(new Dimension(16, 16));
        orange.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                currentColor = orange;
                System.out.println("orange");
            }
        });
        gb = new GridBagConstraints(); gb.gridx = 4; gb.gridy = 0;
        this.add(orange, gb);
    
        brown = new JButton();
        brown.setBackground(new Color(143, 0, 0));
        brown.setPreferredSize(new Dimension(16, 16));
        brown.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                currentColor = brown;
                System.out.println("brown");
            }
        });
        gb = new GridBagConstraints(); gb.gridx = 5; gb.gridy = 0;
        this.add(brown, gb);
    
        purple = new JButton();
        purple.setBackground(new Color(128,0,255));
        purple.setPreferredSize(new Dimension(16, 16));
        purple.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                currentColor = purple;
                System.out.println("purple");
            }
        });
        gb = new GridBagConstraints(); gb.gridx = 3; gb.gridy = 1;
        this.add(purple, gb);
    
        lightblue = new JButton();
        lightblue.setBackground(new Color(128,128,255));
        lightblue.setPreferredSize(new Dimension(16, 16));
        lightblue.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                currentColor = lightblue;
                System.out.println("lightblue");
            }
        });
        gb = new GridBagConstraints(); gb.gridx = 4; gb.gridy = 1;
        this.add(lightblue, gb);
    
        lightgreen = new JButton();
        lightgreen.setBackground(new Color(128, 255, 128));
        lightgreen.setPreferredSize(new Dimension(16, 16));
        lightgreen.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                currentColor = lightgreen;
                System.out.println("lightgreen");
            }
        });
        gb = new GridBagConstraints(); gb.gridx = 5; gb.gridy = 1;
        this.add(lightgreen, gb);
    }    
}

