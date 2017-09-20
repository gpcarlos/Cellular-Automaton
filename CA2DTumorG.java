/**
* Class CA2DTumorG that emultes a tumor growth
* @author Carlos Gallardo Polanco
*/

import java.awt.Color;
import java.util.Random;
import java.util.concurrent.CyclicBarrier;

public class CA2DTumorG implements Runnable{
  static Random rd = new Random();
  static int cores = Runtime.getRuntime().availableProcessors();
  static CyclicBarrier br = new CyclicBarrier(cores);

  static int[][][] cells;
  static int[][] cellsP;
  static int p=0, q=1;
  static String seed;
  static int nCells;
  static double Ps, Pd, Pm, Pp, NP;
  int ulimit, llimit, thread;

  public void set_nCells(int cel){ nCells=cel;}
  public void set_PsPd(double ps){ Ps=ps; Pd=1-ps;}
  public void set_Pm(double pm){ Pm=pm;}
  public void set_Pp(double pp){ Pp=pp;}
  public void set_NP(double np){ NP=np;}
  public void set_seed(String s){ seed=s;}
  public int[][] get_cellsP(){return cellsP;}

  /**
   * Constructor
   * @param  int ulimit        upper limit
   * @param  int llimit        lower limit
   * @param  int thread        thread number
   */
  public CA2DTumorG(int ulimit, int llimit, int thread){
    this.thread=thread;
    this.llimit=llimit;
    this.ulimit=ulimit;
  }

  /**
   * No-argument constructor
   */
  public CA2DTumorG(){}

  /**
   * run method
   */
  public void run(){
    compute();
      if(thread==0){
        if(p==0){ p=1; q=0;}
        else{ p=0; q=1;}
      }
  }

  /**
   * setup method
   */
  public void setup(){
    cells = new int[nCells][nCells][2];
    cellsP = new int[nCells][nCells];
    switch(seed){
      case "1 Central Cell":
        cells[nCells/2][nCells/2][p] = 1;
      break;
      case "2 Random Cells":
        int x1 = rd.nextInt(nCells);
        int y1 = rd.nextInt(nCells);
        int x2 = rd.nextInt(nCells);
        int y2 = rd.nextInt(nCells);
        cells[x1][y1][p] = 1;
        cells[x2][y2][p] = 1;
      break;
    }
  }

  /**
   * compute method
   */
  public void compute(){
    for(int x=llimit; x<ulimit; ++x){
      for(int y=0; y<nCells ; ++y){
        cells[x][y][q]=cells[x][y][p];
      }
    }
    for(int x=llimit; x<ulimit; ++x){
      for(int y=0; y<nCells ; ++y){
        if(cells[x][y][p]==1){
          double rr = rd.nextDouble();
          if(rr<Ps){ //Survive?
            int PH=0; boolean proliferate=true;
            double rpo = rd.nextDouble();
            double[] P = probabilities(x, y);
            int i=x, j=y;
            if(0<=rpo && rpo<=P[0] ){ i=x-1; j=y;}else{
              if(P[0]<rpo && rpo<=P[0]+P[1]){ i=x+1; j=y;}else{
                if(P[0]+P[1]<rpo && rpo<=P[0]+P[1]+P[2]){ i=x; j=y-1;}else{
                  if(P[0]+P[1]+P[2]<rpo && rpo<=1){ i=x; j=y+1;}
                }
              }
            }
            while(PH!=NP && proliferate){
              double rrp = rd.nextDouble();
              if(rrp>=Pp){ //Proliferate?
                proliferate=false;
                double rrm = rd.nextDouble();
                if(rrm<Pm){ //Migrate?
                  cells[x][y][q]=0;
                  if(i>=0&&nCells>i&&j>=0&&nCells>j){ cells[i][j][q]=1;}
                  else{ cells[x][y][q]=1;}
                }else{ cells[x][y][q]=1;}
              }else{PH++;}
            }
            if(proliferate){
              cells[x][y][q]=1;
              if(i<0||nCells-1<i||j<0||nCells-1<j){}
              else{ cells[i][j][q]=1;}
            }
          }else{cells[x][y][q]=0;}
        }
        cellsP[x][y]=cells[x][y][q];
      }
    }
    try{
      br.await();
    }catch(Exception e){}
  }

  /**
   * probabilities method calcules the probabilities of the cell
   * @param  int x             cell row
   * @param  int y             cell column
   * @return     Probabilities of the cell
   */
  public double[] probabilities(int x, int y){
    double P1, P2, P3, P4, denominator;
    if(x==0){
      if(y==0){
        denominator=4-cells[x+1][y][p]-cells[x][y+1][p];
        P1=(1)/denominator;
        P2=(1-cells[x+1][y][p])/denominator;
        P3=(1)/denominator;
        P4=(1-cells[x][y+1][p])/denominator;
      }else{
        if(y==nCells-1){
          denominator=4-cells[x+1][y][p]-cells[x][y-1][p];
          P1=(1)/denominator;
          P2=(1-cells[x+1][y][p])/denominator;
          P3=(1-cells[x][y-1][p])/denominator;
          P4=(1)/denominator;
        }else{
        denominator=4-cells[x+1][y][p]-cells[x][y+1][p]-cells[x][y-1][p];
          P1=(1)/denominator;
          P2=(1-cells[x+1][y][p])/denominator;
          P3=(1-cells[x][y-1][p])/denominator;
          P4=(1-cells[x][y+1][p])/denominator;
        }
      }
    }else{
      if(x==nCells-1){
        if(y==0){
          denominator=4-cells[x-1][y][p]-cells[x][y+1][p];
          P1=(1-cells[x-1][y][p])/denominator;
          P2=(1)/denominator;
          P3=(1)/denominator;
          P4=(1-cells[x][y+1][p])/denominator;
        }else{
          if(y==nCells-1){
            denominator=4-cells[x-1][y][p]-cells[x][y-1][p];
            P1=(1-cells[x-1][y][p])/denominator;
            P2=(1)/denominator;
            P3=(1-cells[x][y-1][p])/denominator;
            P4=(1)/denominator;
          }else{
            denominator=4-cells[x-1][y][p]-cells[x][y+1][p]-cells[x][y-1][p];
            P1=(1-cells[x-1][y][p])/denominator;
            P2=(1)/denominator;
            P3=(1-cells[x][y-1][p])/denominator;
            P4=(1-cells[x][y+1][p])/denominator;
          }
        }
      }else{
        if(y==0){
          denominator=4-cells[x+1][y][p]-cells[x-1][y][p]-cells[x][y+1][p];
          P1=(1-cells[x-1][y][p])/denominator;
          P2=(1-cells[x+1][y][p])/denominator;
          P3=(1)/denominator;
          P4=(1-cells[x][y+1][p])/denominator;
        }else{
          if(y==nCells-1){
            denominator=4-cells[x+1][y][p]-cells[x-1][y][p]-cells[x][y-1][p];
            P1=(1-cells[x-1][y][p])/denominator;
            P2=(1-cells[x+1][y][p])/denominator;
            P3=(1-cells[x][y-1][p])/denominator;
            P4=(1)/denominator;
          }else{
            denominator=4-cells[x+1][y][p]-cells[x-1][y][p]-cells[x][y+1][p]-cells[x][y-1][p];
            if(denominator!=0){
              P1=(1-cells[x-1][y][p])/denominator;
              P2=(1-cells[x+1][y][p])/denominator;
              P3=(1-cells[x][y-1][p])/denominator;
              P4=(1-cells[x][y+1][p])/denominator;
            }else{P1=0.25;P2=0.25;P3=0.25;P4=0.25;}
          }
        }
      }
    }
    double[] P = {P1,P2,P3,P4};
    return P;
  }
}
