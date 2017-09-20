/**
 * Class GUIgol
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

public class GUIgol{
  private JFrame frame;
  private JPanel panel;
  private CA2DGoL GoL = new CA2DGoL();
  private int cores = Runtime.getRuntime().availableProcessors();

  private int nGen=5000, nDim=200;
  private String fGen="Random", border="Null";

  private Thread t;
  private boolean stop=false;

  private JTextPane textPane_gen = new JTextPane(),
    textPane_dead = new JTextPane(), textPane_alive = new JTextPane();
  private JButton btnStop = new JButton("Stop"),
    btnSimulate = new JButton("Simulate");
  private JLabel lbtDimension = new JLabel("Dimension"),
    lbtGenerations = new JLabel("Generations"),
    lbtFirstGen = new JLabel("First Generation"),
    lbtBorder = new JLabel("Border");
  private JSpinner spDim = new JSpinner(), spGen = new JSpinner();
  private JComboBox cbFirstGen = new JComboBox(), cbBorder = new JComboBox();


  public static void main(String[] args){
    EventQueue.invokeLater(new Runnable(){
      public void run(){
        try{
          GUIgol window = new GUIgol();
          window.frame.setVisible(true);
        }catch(Exception e){
          e.printStackTrace();
        }
      }
    });
  }

  public GUIgol(){
    initialize();
  }

  public void initialize(){
    frame = new JFrame();
    frame.setBounds(100, 100, 805, 740);
    frame.getContentPane().setLayout(null);

    panel = new JPanel();
    panel.setBackground(Color.WHITE);
    panel.setBounds(10, 42, 655, 655);
    frame.getContentPane().add(panel);

    JLabel lbtTittle = new JLabel("Cellular Automaton 2D Simulator - Conway's Game of Life");
    lbtTittle.setVerticalAlignment(SwingConstants.TOP);
    lbtTittle.setHorizontalAlignment(SwingConstants.LEFT);
    lbtTittle.setFont(new Font("Dialog", Font.BOLD, 20));
    lbtTittle.setBounds(12, 12, 782, 38);
    frame.getContentPane().add(lbtTittle);

    lbtDimension.setHorizontalAlignment(SwingConstants.CENTER);
    lbtDimension.setBounds(677, 45, 117, 20);
    frame.getContentPane().add(lbtDimension);

    spDim.setBounds(677, 68, 117, 20);
    spDim.setModel(new SpinnerNumberModel(nDim, 40, null, 1));
    frame.getContentPane().add(spDim);
    spDim.addChangeListener(new ChangeListener(){
      public void stateChanged(ChangeEvent e){
        nDim = (Integer)spDim.getValue(); //System.out.println(nDim);
      }
    });

    lbtGenerations.setHorizontalAlignment(SwingConstants.CENTER);
    lbtGenerations.setBounds(677, 90, 117, 20);
    frame.getContentPane().add(lbtGenerations);

    spGen.setBounds(677, 113, 117, 20);
    spGen.setModel(new SpinnerNumberModel(nGen, 1, null, 1));
    frame.getContentPane().add(spGen);
    spGen.addChangeListener(new ChangeListener(){
      public void stateChanged(ChangeEvent e){
        nGen = (Integer)spGen.getValue(); //System.out.println(nGen);
      }
    });

    lbtFirstGen.setHorizontalAlignment(SwingConstants.CENTER);
    lbtFirstGen.setBounds(677, 135, 117, 20);
    frame.getContentPane().add(lbtFirstGen);

    cbFirstGen.setModel(new DefaultComboBoxModel(new String[] {"Random", "Center", "Glider"}));
    cbFirstGen.setBounds(677, 158, 117, 24);
    frame.getContentPane().add(cbFirstGen);
    cbFirstGen.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
        JComboBox cb = (JComboBox)e.getSource();
        fGen = (String)cb.getSelectedItem(); //System.out.println(fGen);
      }
    });

    lbtBorder.setHorizontalAlignment(SwingConstants.CENTER);
    lbtBorder.setBounds(677, 184, 117, 20);
    frame.getContentPane().add(lbtBorder);

    cbBorder.setModel(new DefaultComboBoxModel(new String[] {"Null", "Toro"}));
    cbBorder.setBounds(677, 207, 117, 24);
    frame.getContentPane().add(cbBorder);
    cbBorder.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
        JComboBox cb = (JComboBox)e.getSource();
        border = (String)cb.getSelectedItem(); //System.out.println(border);
      }
    });


    btnSimulate.setBounds(677, 243, 117, 55);
    frame.getContentPane().add(btnSimulate);
    btnSimulate.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
        btnSimulate.setVisible(false);
        btnStop.setVisible(true);
        lbtDimension.setVisible(false);
        spDim.setVisible(false);
        lbtGenerations.setVisible(false);
        spGen.setVisible(false);
        lbtFirstGen.setVisible(false);
        cbFirstGen.setVisible(false);
        lbtBorder.setVisible(false);
        cbBorder.setVisible(false);
        stop=false;

        t = new Thread(new Runnable(){
          public void run(){ compute();}
        });

        t.start();
      }
		});

    btnStop.setBounds(677, 243, 117, 55);
    frame.getContentPane().add(btnStop);
    btnStop.setVisible(false);
    btnStop.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
        stop=true;

        try{ t.join();}catch(InterruptedException ex){}

        btnSimulate.setVisible(true);
        btnStop.setVisible(false);
        lbtDimension.setVisible(true);
        spDim.setVisible(true);
        lbtGenerations.setVisible(true);
        spGen.setVisible(true);
        lbtFirstGen.setVisible(true);
        cbFirstGen.setVisible(true);
        lbtBorder.setVisible(true);
        cbBorder.setVisible(true);
      }
    });

    JLabel lbtGen = new JLabel("Gen");
    lbtGen.setBounds(677, 330, 50, 24);
    frame.getContentPane().add(lbtGen);

    textPane_gen.setBounds(727, 330, 66, 24);
    frame.getContentPane().add(textPane_gen);

    JLabel lbtDead = new JLabel("Dead");
    lbtDead.setBounds(677, 366, 50, 24);
    frame.getContentPane().add(lbtDead);

    textPane_dead.setBounds(727, 366, 66, 24);
    frame.getContentPane().add(textPane_dead);

    JLabel lbtAlive = new JLabel("Alive");
    lbtAlive.setBounds(677, 402, 50, 24);
    frame.getContentPane().add(lbtAlive);

    textPane_alive.setBounds(727, 402, 66, 24);
    frame.getContentPane().add(textPane_alive);
  }

  public void setVisible(){
    frame.setVisible(true);
  }

  public void compute(){
    GoL.inizialize(nDim,fGen,border);
    try { paint(panel.getGraphics(), GoL.get_cells());
    }catch(InterruptedException exception){ System.out.println("EXCEPTION: "+exception);}

    for(int j=0; j<nGen && !stop; j++){
      textPane_gen.setText(String.valueOf(j+1));
      ExecutorService ex = Executors.newFixedThreadPool(cores);
      int window = nDim/cores, lower=0, upper=window;
      for(int i=0; i<cores; i++){
        ex.execute(new CA2DGoL(upper,lower,i));
        lower=upper;
        upper+=window;
        if((i==cores-2)&&(window*cores!=nDim)){ upper+=(nDim-window*cores);}
      }
      ex.shutdown();
      while(!ex.isTerminated()){}

      textPane_alive.setText(String.valueOf(GoL.lCells()));
      textPane_dead.setText(String.valueOf(nDim*nDim-GoL.lCells()));
      try { paint(panel.getGraphics(), GoL.get_cells());
      }catch(InterruptedException exception){ System.out.println("EXCEPTION: "+exception);}
    }

    btnSimulate.setVisible(true);
    btnStop.setVisible(false);
    lbtDimension.setVisible(true);
    spDim.setVisible(true);
    lbtGenerations.setVisible(true);
    spGen.setVisible(true);
    lbtFirstGen.setVisible(true);
    cbFirstGen.setVisible(true);
    lbtBorder.setVisible(true);
    cbBorder.setVisible(true);
  }

  public void paint(Graphics g, int[][] m) throws InterruptedException{
    BufferedImage image=new BufferedImage(m.length, m[0].length, BufferedImage.TYPE_INT_RGB);
    for (int f=0;f<m.length ; ++f){
      for(int c=0; c<m[0].length; ++c){
        if(m[f][c]==0){
          Color color = Color.WHITE;
          image.setRGB(f,c,color.getRGB());
        }
        if(m[f][c]==1){
          Color color = Color.BLACK;
          image.setRGB(f,c,color.getRGB());
        }
      }
    }
    g.drawImage(image, 0, 0, panel.getHeight(),panel.getWidth(), panel);
  }

}
