/**
* Class CA2DGoL that emulates the Conway's Game of Life
* @author Carlos Gallardo Polanco
*/

import java.util.Random;
import java.util.concurrent.CyclicBarrier;

public class CA2DGoL implements Runnable{
  Random rd = new Random();
  static int cores = Runtime.getRuntime().availableProcessors();
  static CyclicBarrier br = new CyclicBarrier(cores);

  static int[][] cells, sucessor; //(nDim)x(nDim)
  static int nDim;
  static String fGen, border;
  int ulimit, llimit, thread;

  /**
   * Constructor
   * @param  int ulimit        upper limit
   * @param  int llimit        lower limit
   * @param  int thread        thread number
   */
  public CA2DGoL(int ulimit, int llimit, int thread){
    this.ulimit=ulimit;
    this.llimit=llimit;
    this.thread=thread;
  }
  /**
   * No-argument constructor
   */
  public CA2DGoL(){}

  /**
   * run method
   */
  public void run(){
    caComputation();
    try{Thread.sleep(2); }catch(InterruptedException e){}
  }

  /**
   * initialize method inizializes the cellular automaton
   * @param int    d cellular automaton Dimension
   * @param String f type of the first generation
   * @param String b type of the border
   */
  public void inizialize(int d, String f, String b){
    nDim=d;
    fGen=f;
    border=b;
    cells = new int[nDim][nDim];
    sucessor = new int[nDim][nDim];
    firstgen();
  }

  /**
   * firstgen method inizializes the first generation
   */
  public void firstgen(){
    switch(fGen){
      case "Random":
        for(int i=0; i<nDim; ++i){
          for(int j=0; j<nDim; ++j){
            cells[i][j]=rd.nextInt(2);
          }
        }
      break;

      case "Center":
        for(int i=nDim/2-20; i<nDim/2+20; ++i){
          for(int j=nDim/2-20; j<nDim/2+20; ++j){
            cells[i][j]=rd.nextInt(2);
          }
        }
      break;

      case "Glider":
        cells[0][4]=1;  cells[0][5]=1;  cells[1][4]=1;  cells[1][5]=1;
        cells[10][4]=1; cells[10][5]=1; cells[10][6]=1; cells[11][3]=1;
        cells[11][7]=1; cells[12][2]=1; cells[12][8]=1; cells[13][2]=1;
        cells[13][8]=1; cells[14][5]=1; cells[15][3]=1; cells[15][7]=1;
        cells[16][4]=1; cells[16][5]=1; cells[16][6]=1; cells[17][5]=1;
        cells[20][2]=1; cells[20][3]=1; cells[20][4]=1; cells[21][2]=1;
        cells[21][3]=1; cells[21][4]=1; cells[22][1]=1; cells[22][5]=1;
        cells[24][0]=1; cells[24][1]=1; cells[24][5]=1; cells[24][6]=1;
        cells[34][2]=1; cells[34][3]=1; cells[35][2]=1; cells[35][3]=1;
      break;
      default: break;
    }
  }

  /**
   * get_cells method returns the cellular automaton
   * @return cellular automaton
   */
  public int[][] get_cells(){
    return cells;
  }

  /**
   * lCells counts how many live cells the cellular automaton has
   * @return How many live cells the cellular automaton has
   */
  public int lCells(){
      int cont=0;
      for(int i=0; i<nDim ; ++i){
          for(int j=0; j<nDim; ++j){
              cont+=cells[i][j];
          }
      }
      return cont;
  }

  /**
   * nextGen computes the next generation
   */
  public void nextGen(){
    for(int i=llimit; i<ulimit ; ++i){
      for(int j=0; j<nDim; ++j){
        if(cells[i][j]==1){
          if(neighbours(i,j)<2||3<neighbours(i,j)){
            sucessor[i][j]=0;
          }else{
            sucessor[i][j]=1;
          }
        }else{
          if(neighbours(i,j)==3){
            sucessor[i][j]=1;
          }
        }
      }
    }
    try{
      br.await();
    }catch(Exception e){}
  }

  /**
   * neighbours method count how many living neighbours the cell has
   * @param  int f             cell row
   * @param  int c             cell column
   * @return     How many living neighbours the cell has
   */
  public int neighbours(int f, int c){
    int cont=0;
    switch(border){
      case "Null":
        if(f==0){
          if(c==0){
            cont=cells[f][c+1]+cells[f+1][c]+cells[f+1][c+1];
          }
          else{
            if(c==nDim-1){
              cont=cells[f][c-1]+cells[f+1][c-1]+cells[f+1][c] ;
            }
            else{
              cont=cells[f+1][c-1]+cells[f+1][c]+cells[f+1][c+1]+cells[f][c-1]+cells[f][c+1];
            }
          }
        }else{
          if(f==nDim-1){
            if(c==0){
              cont=cells[f-1][c]+cells[f-1][c+1]+cells[f][c+1];
            }
            else{
              if(c==nDim-1){
                cont=cells[f-1][c-1]+cells[f-1][c]+cells[f][c-1];
              }
              else{
                cont=cells[f-1][c-1]+cells[f-1][c]+cells[f-1][c+1]+cells[f][c-1]+cells[f][c+1];
              }
            }
          }
          else{
            if(c==0){
              cont=cells[f-1][c+1]+cells[f][c+1]+cells[f+1][c+1]+cells[f-1][c]+cells[f+1][c];
            }
            else{
              if(c==nDim-1){
                cont=cells[f-1][c-1]+cells[f][c-1]+cells[f+1][c-1]+cells[f-1][c]+cells[f+1][c];
              }
              else{
                cont=cells[f-1][c-1]+cells[f-1][c]+cells[f-1][c+1]+cells[f][c-1]+cells[f][c+1]+cells[f+1][c-1]+cells[f+1][c] +cells[f+1][c+1];
              }
            }
          }
        }
      break;

      case "Toro":
        if(f==0){
          if(c==0){
            cont+=cells[nDim-1][nDim-1]+cells[nDim-1][c]+cells[nDim-1][c+1]+cells[f][nDim-1]+cells[f][c+1]+cells[f+1][nDim-1]+cells[f+1][c] +cells[f+1][c+1];
          }
          else{
            if(c==nDim-1){
              cont+=cells[nDim-1][c-1]+cells[nDim-1][c]+cells[nDim-1][0]+cells[f][c-1]+cells[f][0]+cells[f+1][c-1]+cells[f+1][c] +cells[f+1][0];
            }
            else{
              cont+=cells[nDim-1][c-1]+cells[nDim-1][c]+cells[nDim-1][c+1]+cells[f][c-1]+cells[f][c+1]+cells[f+1][c-1]+cells[f+1][c] +cells[f+1][c+1];
            }
          }
        }
        else{
          if(f==nDim-1){
            if(c==0){
              cont+=cells[f-1][nDim-1]+cells[nDim-1][c]+cells[nDim-1][c+1]+cells[f][nDim-1]+cells[f][c+1]+cells[0][nDim-1]+cells[0][c] +cells[0][c+1];
            }
            else{
              if(c==nDim-1){
                cont+=cells[f-1][c-1]+cells[f-1][c]+cells[f-1][0]+cells[f][c-1]+cells[f][0]+cells[0][c-1]+cells[0][c] +cells[0][0];
              }
              else{
                cont+=cells[f-1][c-1]+cells[f-1][c]+cells[f-1][c+1]+cells[f][c-1]+cells[f][c+1]+cells[0][c-1]+cells[0][c] +cells[0][c+1];
              }
            }
          }
          else{
            if(c==0){
              cont+=cells[f-1][nDim-1]+cells[f-1][c]+cells[f-1][c+1]+cells[f][nDim-1]+cells[f][c+1]+cells[f+1][nDim-1]+cells[f+1][c] +cells[f+1][c+1];
            }
            else{
              if(c==nDim-1){
                cont+=cells[f-1][c-1]+cells[f-1][c]+cells[f-1][0]+cells[f][c-1]+cells[f][0]+cells[f+1][c-1]+cells[f+1][c] +cells[f+1][0];
              }
              else{
                cont+=cells[f-1][c-1]+cells[f-1][c]+cells[f-1][c+1]+cells[f][c-1]+cells[f][c+1]+cells[f+1][c-1]+cells[f+1][c] +cells[f+1][c+1];
              }
            }
          }
        }
      break;

      default: break;
    }
    return cont;
  }

  /**
   * caComputation computes the cellular automaton
   */
  public void caComputation(){
    nextGen();
    if(thread==0){ //Thread 0 makes the copy
        for(int i=0; i<nDim; ++i){
            for(int j=0; j<nDim; ++j){
                cells[i][j]=sucessor[i][j];
            }
        }
    }
  }
}
