/**
 * Class GUI
 * @author Carlos Gallardo Polanco
 */

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.SwingConstants;

public class GUI{
  private JFrame frame;

  public static void main(String[] args){
    EventQueue.invokeLater(new Runnable(){
      public void run(){
        try{
          GUI window = new GUI();
          window.frame.setVisible(true);
        }catch(Exception e){
          e.printStackTrace();
        }
      }
    });
  }

  public GUI(){
    initialize();
  }

  public void initialize(){
    frame = new JFrame();
    frame.setBounds(100, 100, 204, 300);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.getContentPane().setLayout(null);
    frame.setResizable(false);

    JButton btnCA1DSimulator = new JButton("CA1D Simulator");
    JButton btnCA2DSimulator = new JButton("CA2D Simulator");
    JButton btnMandelbrotSet = new JButton("Mandelbrot Set");
    JButton btnGameOfLife = new JButton("Game Of Life");
    JButton btnChemicalReaction = new JButton("BZ Reaction");
    JButton btnTumorGrowth = new JButton("Tumor Growth");
    JButton btnBack = new JButton("< Back");
    JLabel lbtca2d = new JLabel("Cellular Automaton 2D Simulator");

    btnCA1DSimulator.setBounds(10, 31, 184, 43);
    frame.getContentPane().add(btnCA1DSimulator);
    btnCA1DSimulator.setVisible(true);
    btnCA1DSimulator.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
        GUIca1ds g = new GUIca1ds();
        g.setVisible();
      }
    });

    btnCA2DSimulator.setBounds(10, 104, 184, 43);
    frame.getContentPane().add(btnCA2DSimulator);
    btnCA2DSimulator.setVisible(true);
    btnCA2DSimulator.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
        btnCA1DSimulator.setVisible(false);
        btnMandelbrotSet.setVisible(false);
        btnCA2DSimulator.setVisible(false);
        btnGameOfLife.setVisible(true);
        btnChemicalReaction.setVisible(true);
        btnTumorGrowth.setVisible(true);
        btnBack.setVisible(true);
        lbtca2d.setVisible(true);
      }
    });

    btnMandelbrotSet.setBounds(10, 177, 184, 43);
    frame.getContentPane().add(btnMandelbrotSet);
    btnMandelbrotSet.setVisible(true);
    btnMandelbrotSet.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
        GUIMandelbrot g = new GUIMandelbrot();
        g.setVisible();
      }
    });

    lbtca2d.setVerticalAlignment(SwingConstants.TOP);
    lbtca2d.setHorizontalAlignment(SwingConstants.LEFT);
    lbtca2d.setFont(new Font("Dialog", Font.BOLD, 10));
    lbtca2d.setBounds(10, 10, 184, 43);
    frame.getContentPane().add(lbtca2d);
    lbtca2d.setVisible(false);

    btnGameOfLife.setBounds(10, 31, 184, 43);
    frame.getContentPane().add(btnGameOfLife);
    btnGameOfLife.setVisible(false);
    btnGameOfLife.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
        GUIgol g = new GUIgol();
        g.setVisible();
      }
    });

    btnChemicalReaction.setBounds(10, 104, 184, 43);
    frame.getContentPane().add(btnChemicalReaction);
    btnChemicalReaction.setVisible(false);
    btnChemicalReaction.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
        GUIBelZab g = new GUIBelZab();
        g.setVisible();
      }
    });

    btnTumorGrowth.setBounds(10, 177, 184, 43);
    frame.getContentPane().add(btnTumorGrowth);
    btnTumorGrowth.setVisible(false);
    btnTumorGrowth.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
        GUITumorG g = new GUITumorG();
        g.setVisible();
      }
    });

    btnBack.setBounds(10, 240, 184, 20);
    frame.getContentPane().add(btnBack);
    btnBack.setVisible(false);
    btnBack.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
        btnCA1DSimulator.setVisible(true);
        btnMandelbrotSet.setVisible(true);
        btnCA2DSimulator.setVisible(true);
        btnGameOfLife.setVisible(false);
        btnChemicalReaction.setVisible(false);
        btnTumorGrowth.setVisible(false);
        btnBack.setVisible(false);
        lbtca2d.setVisible(false);
      }
    });

  }
}
