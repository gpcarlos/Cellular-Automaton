/**
 * Class GUIca1ds
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
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;

import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;

public class GUIca1ds{
  private JFrame frame;
  private JPanel panel;
  private CA1DSimulator ca1D = new CA1DSimulator();
  private int cores = Runtime.getRuntime().availableProcessors();
  private int nGen=655, nCells=655, rule=0;

  public static void main(String[] args){
    EventQueue.invokeLater(new Runnable(){
      public void run(){
        try{
          GUIca1ds window = new GUIca1ds();
          window.frame.setVisible(true);
        }catch(Exception e){
          e.printStackTrace();
        }
      }
    });
  }

  public GUIca1ds(){
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

    JLabel lbtTittle = new JLabel("Cellular Automaton 1D Simulator");
    lbtTittle.setVerticalAlignment(SwingConstants.TOP);
    lbtTittle.setHorizontalAlignment(SwingConstants.LEFT);
    lbtTittle.setFont(new Font("Dialog", Font.BOLD, 20));
    lbtTittle.setBounds(12, 12, 492, 38);
    frame.getContentPane().add(lbtTittle);

    JLabel lbtRule = new JLabel("Rule");
    lbtRule.setHorizontalAlignment(SwingConstants.RIGHT);
    lbtRule.setFont(new Font("Dialog", Font.BOLD, 12));
    lbtRule.setBounds(646, 45, 61, 20);
    frame.getContentPane().add(lbtRule);

    JSpinner spRule = new JSpinner();
    spRule.setBounds(719, 45, 75, 20);
    spRule.setModel(new SpinnerNumberModel(rule, 0, 255, 1));
    frame.getContentPane().add(spRule);
    spRule.addChangeListener(new ChangeListener(){
      public void stateChanged(ChangeEvent e){
        rule = (Integer)spRule.getValue(); //System.out.println(rule);
        compute();
      }
    });

    JLabel lbtCells = new JLabel("Cells");
    lbtCells.setHorizontalAlignment(SwingConstants.RIGHT);
    lbtCells.setFont(new Font("Dialog", Font.BOLD, 12));
    lbtCells.setBounds(646, 82, 61, 20);
    frame.getContentPane().add(lbtCells);

    JSpinner spCells = new JSpinner();
    spCells.setBounds(719, 82, 75, 20);
    spCells.setModel(new SpinnerNumberModel(nCells, 1, null, 1));
    frame.getContentPane().add(spCells);
    spCells.addChangeListener(new ChangeListener(){
      public void stateChanged(ChangeEvent e){
        nCells = (Integer)spCells.getValue(); //System.out.println(nCells);
        compute();
      }
    });

    JLabel lbtGen = new JLabel("Gen");
    lbtGen.setHorizontalAlignment(SwingConstants.RIGHT);
    lbtGen.setFont(new Font("Dialog", Font.BOLD, 12));
    lbtGen.setBounds(646, 119, 61, 20);
    frame.getContentPane().add(lbtGen);

    JSpinner spGen = new JSpinner();
    spGen.setBounds(719, 119, 75, 20);
    spGen.setModel(new SpinnerNumberModel(nGen, 1, null, 1));
    frame.getContentPane().add(spGen);
    spGen.addChangeListener(new ChangeListener(){
      public void stateChanged(ChangeEvent e){
        nGen = (Integer)spGen.getValue(); //System.out.println(nGen);
        compute();
      }
    });

    JLabel lbtChange = new JLabel("Change the rule");
    lbtChange.setHorizontalAlignment(SwingConstants.CENTER);
    lbtChange.setFont(new Font("Dialog", Font.BOLD, 12));
    lbtChange.setBounds(677, 151, 117, 24);
    lbtChange.setToolTipText("Rules 18, 30, 57, 73, 161 or 225 ;)");
    frame.getContentPane().add(lbtChange);

  }

  public void setVisible(){
    frame.setVisible(true);
  }

  public void compute(){
    ca1D.inizialize(nGen,nCells,rule);

    ExecutorService ex = Executors.newFixedThreadPool(cores);
    int window = nCells/cores, lower=0, upper=window;
    for(int i=0; i<cores; i++){
      ex.execute(new CA1DSimulator(upper,lower));
      lower=upper;
      upper+=window;
      if((i==cores-2)&&(window*cores!=nCells)){ upper+=(nCells-window*cores);}
    }
    ex.shutdown();
    while(!ex.isTerminated()){}

    try { paint(panel.getGraphics(), ca1D.get_cells());
    }catch(InterruptedException exception){ System.out.println("EXCEPTION: "+exception);}
  }

  public void paint(Graphics g, int[][] m) throws InterruptedException{
    BufferedImage image=new BufferedImage(m[0].length, m.length, BufferedImage.TYPE_INT_RGB);
    for (int f=0;f<m.length ; ++f){
      for(int c=0; c<m[0].length; ++c){
        if(m[f][c]==0){
          Color color = Color.WHITE;
          image.setRGB(c,f,color.getRGB());
        }
        if(m[f][c]==1){
          Color color = Color.BLACK;
          image.setRGB(c,f,color.getRGB());
        }
      }
    }
    g.drawImage(image, 0, 0, panel.getWidth(),panel.getHeight(), panel);
  }

}
