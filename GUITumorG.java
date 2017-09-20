/**
 * Class GUITumorG
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

public class GUITumorG{
  private JFrame frame;
  private JPanel panel;
  private CA2DTumorG TumorG = new CA2DTumorG();
  private int cores = Runtime.getRuntime().availableProcessors();

  private int nDim=655;
  private String seed="1 Central Cell";
  private double Ps=0.97, Pm=0.1, Pp=0.1, NP=1;

  private Thread t;
  private boolean stop=false;

  private JButton btnStop = new JButton("Stop"),
    btnSimulate = new JButton("Simulate");
  private JLabel lbtDimension = new JLabel("Dimension"),
    lbtSeed = new JLabel("Seed");
  private JSpinner spDim = new JSpinner();
  private JComboBox cbSeed = new JComboBox();

  public static void main(String[] args){
    EventQueue.invokeLater(new Runnable(){
      public void run(){
        try{
          GUITumorG window = new GUITumorG();
          window.frame.setVisible(true);
        }catch(Exception e){
          e.printStackTrace();
        }
      }
    });
  }

  public GUITumorG(){
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

    JLabel lbtTittle = new JLabel("Cellular Automaton 2D Simulator - Tumor Growth");
    lbtTittle.setVerticalAlignment(SwingConstants.TOP);
    lbtTittle.setHorizontalAlignment(SwingConstants.LEFT);
    lbtTittle.setFont(new Font("Dialog", Font.BOLD, 20));
    lbtTittle.setBounds(12, 12, 782, 38);
    frame.getContentPane().add(lbtTittle);

    JLabel label_Ps = new JLabel("Ps");
    label_Ps.setFont(new Font("Dialog", Font.BOLD, 10));
    label_Ps.setBounds(672, 55, 20, 20);
    label_Ps.setToolTipText("Probability of cell survival");
    frame.getContentPane().add(label_Ps);

		JSpinner spinner_Ps = new JSpinner();
    spinner_Ps.setFont(new Font("Dialog", Font.BOLD, 10));
    spinner_Ps.setBounds(690, 55, 42, 20);
    spinner_Ps.setModel(new SpinnerNumberModel(Ps, 0, 1, 0.01));
    frame.getContentPane().add(spinner_Ps);
    spinner_Ps.addChangeListener(new ChangeListener(){
      public void stateChanged(ChangeEvent e){
        TumorG.set_PsPd(Ps=(float)((double) spinner_Ps.getValue()));
        //System.out.println(Ps);
      }
    });

    JLabel label_Pm = new JLabel("Pm");
    label_Pm.setFont(new Font("Dialog", Font.BOLD, 10));
    label_Pm.setBounds(735, 55, 20, 20);
    label_Pm.setToolTipText("Probability of cell migration");
    frame.getContentPane().add(label_Pm);

    JSpinner spinner_Pm = new JSpinner();
    spinner_Pm.setFont(new Font("Dialog", Font.BOLD, 10));
    spinner_Pm.setBounds(753, 55, 42, 20);
    spinner_Pm.setModel(new SpinnerNumberModel(Pm, 0, 1, 0.01));
    frame.getContentPane().add(spinner_Pm);
    spinner_Pm.addChangeListener(new ChangeListener(){
      public void stateChanged(ChangeEvent e){
        TumorG.set_Pm(Pm=(float)((double) spinner_Pm.getValue()));
        //System.out.println(Pm);
      }
    });

    JLabel label_Pp = new JLabel("Pp");
    label_Pp.setFont(new Font("Dialog", Font.BOLD, 10));
    label_Pp.setBounds(672, 83, 20, 20);
    label_Pp.setToolTipText("Probability of cell proliferation");
    frame.getContentPane().add(label_Pp);

    JSpinner spinner_Pp = new JSpinner();
    spinner_Pp.setFont(new Font("Dialog", Font.BOLD, 10));
    spinner_Pp.setBounds(690, 83, 42, 20);
    spinner_Pp.setModel(new SpinnerNumberModel(Pp, 0, 1, 0.01));
    frame.getContentPane().add(spinner_Pp);
    spinner_Pp.addChangeListener(new ChangeListener(){
      public void stateChanged(ChangeEvent e){
        TumorG.set_Pp(Pp=(float)((double) spinner_Pp.getValue()));
        //System.out.println(Pp);
      }
    });

    JLabel label_NP = new JLabel("NP");
    label_NP.setFont(new Font("Dialog", Font.BOLD, 10));
    label_NP.setBounds(735, 83, 20, 20);
    label_NP.setToolTipText("Total PH needed to proliferate");
    frame.getContentPane().add(label_NP);

    JSpinner spinner_NP = new JSpinner();
    spinner_NP.setFont(new Font("Dialog", Font.BOLD, 10));
    spinner_NP.setBounds(753, 83, 42, 20);
    spinner_NP.setModel(new SpinnerNumberModel(NP, 0, 100, 01));
    frame.getContentPane().add(spinner_NP);
    spinner_NP.addChangeListener(new ChangeListener(){
      public void stateChanged(ChangeEvent e){
        TumorG.set_NP(NP=(float)((double) spinner_NP.getValue()));
        //System.out.println(NP);
      }
    });

    lbtDimension.setHorizontalAlignment(SwingConstants.CENTER);
    lbtDimension.setBounds(677, 131, 117, 20);
    frame.getContentPane().add(lbtDimension);

    spDim.setBounds(677, 154, 117, 20);
    spDim.setModel(new SpinnerNumberModel(nDim, 0, null, 1));
    frame.getContentPane().add(spDim);
    spDim.addChangeListener(new ChangeListener(){
      public void stateChanged(ChangeEvent e){
        nDim = (Integer)spDim.getValue(); //System.out.println(nDim);
      }
    });

    lbtSeed.setHorizontalAlignment(SwingConstants.CENTER);
    lbtSeed.setBounds(677, 188, 117, 20);
    frame.getContentPane().add(lbtSeed);

    cbSeed.setBounds(677, 211, 117, 24);
    cbSeed.setModel(new DefaultComboBoxModel(new String[] {"1 Central Cell", "2 Random Cells"}));
    frame.getContentPane().add(cbSeed);
    cbSeed.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
        JComboBox cb = (JComboBox)e.getSource();
        seed = (String)cb.getSelectedItem(); //System.out.println(seed);
      }
    });

    btnSimulate.setBounds(677, 260, 117, 55);
    frame.getContentPane().add(btnSimulate);
    btnSimulate.addActionListener(new ActionListener(){
        public void actionPerformed(ActionEvent e){
        lbtDimension.setVisible(false);
        spDim.setVisible(false);
        btnSimulate.setVisible(false);
        lbtSeed.setVisible(false);
        cbSeed.setVisible(false);
        btnStop.setVisible(true);
        stop=false;

        t = new Thread(new Runnable(){
          public void run(){ compute();}
        });

        t.start();
      }
		});

    btnStop.setBounds(677, 260, 117, 55);
    frame.getContentPane().add(btnStop);
    btnStop.setVisible(false);
    btnStop.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
        stop=true;

        try{ t.join();}catch(InterruptedException ex){}

        lbtDimension.setVisible(true);
        spDim.setVisible(true);
        btnSimulate.setVisible(true);
        lbtSeed.setVisible(true);
        cbSeed.setVisible(true);
        btnStop.setVisible(false);
      }
    });

  }

  public void setVisible(){
    frame.setVisible(true);
  }

  public void compute(){
    TumorG.set_nCells(nDim);
    TumorG.set_seed(seed);
    TumorG.set_PsPd(Ps);
    TumorG.set_Pm(Pm);
    TumorG.set_Pp(Pp);
    TumorG.set_NP(NP);
    TumorG.setup();

    while(!stop){
      ExecutorService ex = Executors.newFixedThreadPool(cores);
      int window = nDim/cores, lower=0, upper=window;
      for(int i=0; i<cores; i++){
        ex.execute(new CA2DTumorG(upper,lower,i));
        lower=upper;
        upper+=window;
        if((i==cores-2)&&(window*cores!=nDim)){ upper+=(nDim-window*cores);}
      }
      ex.shutdown();
      while(!ex.isTerminated()){}

      try { paint(panel.getGraphics(), TumorG.get_cellsP());
      }catch(InterruptedException exception){ System.out.println("EXCEPTION: "+exception);}
    }

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
