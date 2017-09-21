/**
 * Class GUIBelZab
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

public class GUIBelZab{
  private JFrame frame;
  private JPanel panel;
  private CA2DBelZab BelZab = new CA2DBelZab();
  private int cores = Runtime.getRuntime().availableProcessors();

  private int nDim=300;
  private float alpha=1.0f, beta=1.0f, gamma=1.0f;

  private Thread t;
  private boolean stop=false;

  private JButton btnStop = new JButton("Stop"),
    btnSimulate = new JButton("Simulate");
  private JLabel lbtDimension = new JLabel("Dimension");
  private JSpinner spDim = new JSpinner();

  public static void main(String[] args){
    EventQueue.invokeLater(new Runnable(){
      public void run(){
        try{
          GUIBelZab window = new GUIBelZab();
          window.frame.setVisible(true);
        }catch(Exception e){
          e.printStackTrace();
        }
      }
    });
  }

  public GUIBelZab(){
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

    JLabel lbtTittle = new JLabel("Cellular Automaton 2D Simulator - Belousov–Zhabotinsky Reaction");
    lbtTittle.setVerticalAlignment(SwingConstants.TOP);
    lbtTittle.setHorizontalAlignment(SwingConstants.LEFT);
    lbtTittle.setFont(new Font("Dialog", Font.BOLD, 20));
    lbtTittle.setBounds(12, 12, 782, 38);
    frame.getContentPane().add(lbtTittle);

    JLabel lbl_Alpha = new JLabel("\u03B1");
    lbl_Alpha.setBounds(672, 55, 17, 20);
    frame.getContentPane().add(lbl_Alpha);

		JSpinner spinner_Alpha = new JSpinner();
    spinner_Alpha.setBounds(685, 55, 42, 20);
    spinner_Alpha.setModel(new SpinnerNumberModel(alpha, 0, 2, 0.1));
    frame.getContentPane().add(spinner_Alpha);
    spinner_Alpha.addChangeListener(new ChangeListener(){
      public void stateChanged(ChangeEvent e){
        BelZab.set_alpha(alpha=(float)((double) spinner_Alpha.getValue()));
        //System.out.println(alpha);
      }
    });

    JLabel label_beta = new JLabel("\u03B2");
    label_beta.setBounds(735, 55, 17, 20);
    frame.getContentPane().add(label_beta);

    JSpinner spinner_Beta = new JSpinner();
    spinner_Beta.setBounds(748, 55, 42, 20);
    spinner_Beta.setModel(new SpinnerNumberModel(beta, 0, 2, 0.1));
    frame.getContentPane().add(spinner_Beta);
    spinner_Beta.addChangeListener(new ChangeListener(){
      public void stateChanged(ChangeEvent e){
        BelZab.set_beta(beta=(float)((double) spinner_Beta.getValue()));
        //System.out.println(beta);
      }
    });

    JLabel label_Gamma = new JLabel("\u03B3");
    label_Gamma.setBounds(701, 83, 17, 20);
    frame.getContentPane().add(label_Gamma);

    JSpinner spinner_Gamma = new JSpinner();
    spinner_Gamma.setBounds(714, 83, 42, 20);
    spinner_Gamma.setModel(new SpinnerNumberModel(gamma, 0, 2, 0.1));
    frame.getContentPane().add(spinner_Gamma);
    spinner_Gamma.addChangeListener(new ChangeListener(){
      public void stateChanged(ChangeEvent e){
        BelZab.set_gamma(gamma=(float)((double) spinner_Gamma.getValue()));
        //System.out.println(gamma);
      }
    });

    lbtDimension.setHorizontalAlignment(SwingConstants.CENTER);
    lbtDimension.setBounds(677, 118, 117, 20);
    frame.getContentPane().add(lbtDimension);

    spDim.setBounds(677, 141, 117, 20);
    spDim.setModel(new SpinnerNumberModel(nDim, 0, null, 1));
    frame.getContentPane().add(spDim);
    spDim.addChangeListener(new ChangeListener(){
      public void stateChanged(ChangeEvent e){
        nDim = (Integer)spDim.getValue(); //System.out.println(nDim);
      }
    });

    btnSimulate.setBounds(677, 176, 117, 55);
    frame.getContentPane().add(btnSimulate);
    btnSimulate.addActionListener(new ActionListener(){
        public void actionPerformed(ActionEvent e){
        lbtDimension.setVisible(false);
        spDim.setVisible(false);
        btnSimulate.setVisible(false);
        btnStop.setVisible(true);
        stop=false;

        t = new Thread(new Runnable(){
          public void run(){ compute();}
        });

        t.start();
      }
		});

    btnStop.setBounds(677, 176, 117, 55);
    frame.getContentPane().add(btnStop);
    btnStop.setVisible(false);
    btnStop.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
        stop=true;

        try{ t.join();}catch(InterruptedException ex){}

        lbtDimension.setVisible(true);
        spDim.setVisible(true);
        btnSimulate.setVisible(true);
        btnStop.setVisible(false);
      }
    });

  }

  public void setVisible(){
    frame.setVisible(true);
  }

  public void compute(){
    BelZab.set_alpha(alpha); BelZab.set_beta(beta); BelZab.set_gamma(gamma);
    BelZab.set_width(nDim); BelZab.set_height(nDim);
    BelZab.setup();

    while(!stop){
      ExecutorService ex = Executors.newFixedThreadPool(cores);
      int window = nDim/cores, lower=0, upper=window;
      for(int i=0; i<cores; i++){
        ex.execute(new CA2DBelZab(upper,lower,i));
        lower=upper;
        upper+=window;
        if((i==cores-2)&&(window*cores!=nDim)){ upper+=(nDim-window*cores);}
      }
      ex.shutdown();
      while(!ex.isTerminated()){}

      try { paint(panel.getGraphics(), BelZab.get_colorMatrix());
      }catch(InterruptedException exception){ System.out.println("EXCEPTION: "+exception);}
    }
  }

  public void paint(Graphics g, Color[][] m) throws InterruptedException{
    BufferedImage image=new BufferedImage(m[0].length, m.length, BufferedImage.TYPE_INT_RGB);
    for (int f=0;f<m.length ; ++f){
      for(int c=0; c<m[0].length; ++c){
        image.setRGB(f,c,m[f][c].getRGB());
      }
    }
    g.drawImage(image, 0, 0, panel.getHeight(),panel.getWidth(), panel);
  }

}
