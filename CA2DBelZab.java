/**
* Class CA2DBelZab that emultes the Belousov–Zhabotinsky reaction
* @author Carlos Gallardo Polanco
*/

import java.awt.Color;
import java.util.Random;
import java.util.concurrent.CyclicBarrier;

public class CA2DBelZab implements Runnable{
  Random rd = new Random();
  static int cores = Runtime.getRuntime().availableProcessors();
  static CyclicBarrier br = new CyclicBarrier(cores);

  static float[][][] a, b, c;
  static int p=0, q=1;
  static int width, height;
  static float alpha, beta, gamma;
  static Color[][] colorMatrix;
  int ulimit, llimit, thread;

  /**
   * Constructor
   * @param  int ulimit        upper limit
   * @param  int llimit        lower limit
   * @param  int thread        thread number
   */
  public CA2DBelZab(int ulimit, int llimit, int thread){
    this.ulimit=ulimit;
    this.llimit=llimit;
    this.thread=thread;
  }

  /**
   * No-argument constructor
   */
  public CA2DBelZab(){}

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

  public void set_width(int w){ width=w;}
  public void set_height(int h){ height=h;}
  public void set_alpha(float a){ alpha=a;}
  public void set_beta(float b){ beta=b;}
  public void set_gamma(float g){ gamma=g;}

  public Color[][] get_colorMatrix(){ return colorMatrix;}

  /**
   * setup method
   */
  public void setup(){
    a = new float[width][height][2];
    b = new float[width][height][2];
    c = new float[width][height][2];
    colorMatrix = new Color[width][height];
    for(int x=0; x<width; ++x){
      for(int y=0; y<height ; ++y){
        a[x][y][p] = rd.nextFloat();
        b[x][y][p] = rd.nextFloat();
        c[x][y][p] = rd.nextFloat();
      }
    }
  }

  /**
   * compute method
   */
  public void compute(){
    for(int x=llimit; x<ulimit; ++x){
      for(int y=0; y<height ; ++y){
        float c_a=0.0f, c_b=0.0f, c_c=0.0f;
        for(int i=x-1; i<=x+1; ++i){
          for(int j=y-1; j<=y+1 ; ++j){
            c_a+=a[(i+width)%width][(j+height)%height][p];
            c_b+=b[(i+width)%width][(j+height)%height][p];
            c_c+=c[(i+width)%width][(j+height)%height][p];
          }
        }
        c_a/=9.0; c_b/=9.0; c_c/=9.0;
        a[x][y][q] = constrain(c_a+c_a*(alpha*c_b-gamma*c_c));
        b[x][y][q] = constrain(c_b+c_b*(beta*c_c-alpha*c_a));
        c[x][y][q] = constrain(c_c+c_c*(gamma*c_a-beta*c_b));
        colorMatrix[x][y] = new Color(a[x][y][q], b[x][y][q], c[x][y][q]);
      }
    }
    try{
      br.await();
    }catch(Exception e){}
  }

  /**
   * constrain method
   */
  public float constrain(float i){
    if(i<0){return 0;}
    else{
      if(i>1){return 1;}
      else{ return i;}
    }
  }

}
