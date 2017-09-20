/**
* Class CA1DSimulator that emulates a cellular automata in 1D
* @author Carlos Gallardo Polanco
*/

import java.util.Random;
import java.util.concurrent.CyclicBarrier;

public class CA1DSimulator implements Runnable{
  Random rd = new Random();
  static int cores = Runtime.getRuntime().availableProcessors();
  static CyclicBarrier br = new CyclicBarrier(cores);

  static int[][] cells; //(nGen+1)x(nCells)
  int[] sucessor; //next generation
  static int nGen, nCells;
  static int[] ruleBit = new int[8];
  int ulimit, llimit, gen, cel;

  /**
   * Constructor
   * @param  int ulimit        upper limit
   * @param  int llimit        lower limit
   */
	public CA1DSimulator(int ulimit, int llimit){
		this.ulimit=ulimit;
		this.llimit=llimit;
		this.sucessor = new int[nCells];
	}
  /**
   * No-argument constructor
   */
  public CA1DSimulator(){}

  /**
   * run method
   */
  public void run(){
    for(gen=0; gen<nGen; gen++){ caComputation();}
    try{Thread.sleep(2); }catch(InterruptedException e){}
  }
  /**
   * initialize method inizializes the cellular automaton
   * @param int g number of generations
   * @param int c number of cells
   * @param int r rule
   */
  public void inizialize(int g, int c, int r){
    nGen=g;
    nCells=c;
    toBinary(r);
    cells = new int[nGen+1][nCells];
    cells[0][nCells/2]=1; //inizializes the first generation
  }

  /**
   * get_cells method returns the cellular automaton
   * @return cellular automaton
   */
  public int[][] get_cells(){
    return cells;
  }

  /**
   * toBinary method transforms a decimal number to binary
   * @param int rule
   */
  public void toBinary(int rule) {
    final boolean[] ret = new boolean[8];

    for (int i = 0; i < 8; i++){
      ret[8 - 1 - i] = (1 << i & rule) != 0;
    }

    for (int i = 0; i < 8; i++){
      if(ret[i]==true){ ruleBit[i]=1;}
      else{ ruleBit[i]=0;}
    }
  }

  /**
   * nextGen computes the next generation
   */
  public void nextGen(){
    int zero=0;
    for(int i=llimit; i<ulimit; i++){
      if(i==0){
        if(zero==1&&cells[gen][i]==1&&cells[gen][i+1]==1){sucessor[i]=ruleBit[0];}
          else{if(zero==1&&cells[gen][i]==1&&cells[gen][i+1]==0){sucessor[i]=ruleBit[1];}
            else{if(zero==1&&cells[gen][i]==0&&cells[gen][i+1]==1){sucessor[i]=ruleBit[2];}
              else{if(zero==1&&cells[gen][i]==0&&cells[gen][i+1]==0){sucessor[i]=ruleBit[3];}
                else{if(zero==0&&cells[gen][i]==1&&cells[gen][i+1]==1){sucessor[i]=ruleBit[4];}
                  else{if(zero==0&&cells[gen][i]==1&&cells[gen][i+1]==0){sucessor[i]=ruleBit[5];}
                    else{if(zero==0&&cells[gen][i]==0&&cells[gen][i+1]==1){sucessor[i]=ruleBit[6];}
                      else{if(zero==0&&cells[gen][i]==0&&cells[gen][i+1]==0){sucessor[i]=ruleBit[7];}
                    }
                  }
                }
              }
            }
          }
        }
      }else{
        if(i==(nCells-1)){
          if(cells[gen][i-1]==1&&cells[gen][i]==1&&zero==1){sucessor[i]=ruleBit[0];}
            else{if(cells[gen][i-1]==1&&cells[gen][i]==1&&zero==0){sucessor[i]=ruleBit[1];}
              else{if(cells[gen][i-1]==1&&cells[gen][i]==0&&zero==1){sucessor[i]=ruleBit[2];}
                else{if(cells[gen][i-1]==1&&cells[gen][i]==0&&zero==0){sucessor[i]=ruleBit[3];}
                  else{if(cells[gen][i-1]==0&&cells[gen][i]==1&&zero==1){sucessor[i]=ruleBit[4];}
                    else{if(cells[gen][i-1]==0&&cells[gen][i]==1&&zero==0){sucessor[i]=ruleBit[5];}
                      else{if(cells[gen][i-1]==0&&cells[gen][i]==0&&zero==1){sucessor[i]=ruleBit[6];}
                        else{if(cells[gen][i-1]==0&&cells[gen][i]==0&&zero==0){sucessor[i]=ruleBit[7];}
                      }
                    }
                  }
                }
              }
            }
          }
        }else{
          if(cells[gen][i-1]==1&&cells[gen][i]==1&&cells[gen][i+1]==1){sucessor[i]=ruleBit[0];}
            else{if(cells[gen][i-1]==1&&cells[gen][i]==1&&cells[gen][i+1]==0){sucessor[i]=ruleBit[1];}
              else{if(cells[gen][i-1]==1&&cells[gen][i]==0&&cells[gen][i+1]==1){sucessor[i]=ruleBit[2];}
                else{if(cells[gen][i-1]==1&&cells[gen][i]==0&&cells[gen][i+1]==0){sucessor[i]=ruleBit[3];}
                  else{if(cells[gen][i-1]==0&&cells[gen][i]==1&&cells[gen][i+1]==1){sucessor[i]=ruleBit[4];}
                    else{if(cells[gen][i-1]==0&&cells[gen][i]==1&&cells[gen][i+1]==0){sucessor[i]=ruleBit[5];}
                      else{if(cells[gen][i-1]==0&&cells[gen][i]==0&&cells[gen][i+1]==1){sucessor[i]=ruleBit[6];}
                        else{if(cells[gen][i-1]==0&&cells[gen][i]==0&&cells[gen][i+1]==0){sucessor[i]=ruleBit[7];}
                      }
                    }
                  }
                }
              }
            }
          }
        }
      }
    }
  }

  /**
   * caComputation computes the cellular automaton
   */
  public void caComputation(){
    nextGen();
    for(int i=llimit; i<ulimit ;i++){
      cells[gen+1][i]=sucessor[i];
    }
    try{
      br.await();
    }catch(Exception e){}
  }

}
