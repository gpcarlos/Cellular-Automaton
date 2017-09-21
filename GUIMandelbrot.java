/**
 * Class GUIMandelbrot
 * @author Carlos Gallardo Polanco
 */

import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JPanel;
import java.awt.Color;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.SwingConstants;
import javax.swing.JSpinner;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import javax.swing.JTextField;
import javax.swing.JTextPane;

import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;

public class GUIMandelbrot{
  private JFrame frame;
  private JPanel panel;
  private MandelbrotSet MandelSet = new MandelbrotSet();
  private int cores = Runtime.getRuntime().availableProcessors();

  private int nIter=100000;

  private Thread t;

  private JButton btnSimulate = new JButton("Simulate");
  private JLabel lbtIterations = new JLabel("Iterations"),
    lbtWorking = new JLabel("Please wait...");
  private JSpinner spIter = new JSpinner();


  public static void main(String[] args){
    EventQueue.invokeLater(new Runnable(){
      public void run(){
        try{
          GUIMandelbrot window = new GUIMandelbrot();
          window.frame.setVisible(true);
        }catch(Exception e){
          e.printStackTrace();
        }
      }
    });
  }

  public GUIMandelbrot(){
    initialize();
  }

  public void initialize(){
    frame = new JFrame();
    frame.setBounds(100, 100, 805, 740);
    frame.getContentPane().setLayout(null);
    frame.setResizable(false);

    panel = new JPanel();
    panel.setBackground(Color.WHITE);
    panel.setBounds(10, 42, 655, 655);
    frame.getContentPane().add(panel);

    JLabel lbtTittle = new JLabel("Cellular Automaton 2D Simulator - Mandelbrot Set");
    lbtTittle.setVerticalAlignment(SwingConstants.TOP);
    lbtTittle.setHorizontalAlignment(SwingConstants.LEFT);
    lbtTittle.setFont(new Font("Dialog", Font.BOLD, 20));
    lbtTittle.setBounds(12, 12, 782, 38);
    frame.getContentPane().add(lbtTittle);

    lbtIterations.setHorizontalAlignment(SwingConstants.CENTER);
    lbtIterations.setBounds(677, 45, 117, 20);
    frame.getContentPane().add(lbtIterations);

    spIter.setBounds(677, 68, 117, 20);
    spIter.setModel(new SpinnerNumberModel(nIter, 1, null, 1));
    frame.getContentPane().add(spIter);
    spIter.addChangeListener(new ChangeListener(){
      public void stateChanged(ChangeEvent e){
        nIter = (Integer)spIter.getValue(); //System.out.println(nIter);
      }
    });

    btnSimulate.setBounds(677, 110, 117, 55);
    frame.getContentPane().add(btnSimulate);
    btnSimulate.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){

        lbtIterations.setVisible(false);
        spIter.setVisible(false);
        btnSimulate.setVisible(false);
        lbtWorking.setVisible(true);

        t = new Thread(new Runnable(){
          public void run(){ compute();}
        });

        t.start();
      }
		});

    lbtWorking.setHorizontalAlignment(SwingConstants.CENTER);
    lbtWorking.setBounds(677, 110, 117, 55);
    frame.getContentPane().add(lbtWorking);

  }

  public void setVisible(){
    frame.setVisible(true);
  }

  public void compute(){

    MandelSet.setup(nIter, panel.getHeight());

    ExecutorService ex = Executors.newFixedThreadPool(cores);
    int window = panel.getHeight()/cores, lower=0, upper=window;
    for(int i=0; i<cores; i++){
      ex.execute(new MandelbrotSet(upper,lower));
      lower=upper;
      upper+=window;
      if((i==cores-2)&&(window*cores!=panel.getHeight())){ upper+=(panel.getHeight()-window*cores);}
    }
    ex.shutdown();
    while(!ex.isTerminated()){}

    try { paint(panel.getGraphics(), MandelSet.get_image());
    }catch(InterruptedException exception){ System.out.println("EXCEPTION: "+exception);}

    lbtIterations.setVisible(true);
    spIter.setVisible(true);
    btnSimulate.setVisible(true);
    lbtWorking.setVisible(false);
  }

  public void paint(Graphics g, BufferedImage m) throws InterruptedException{
    g.drawImage(m, 0, 0, panel.getHeight(), panel.getWidth(), panel);
  }

}
