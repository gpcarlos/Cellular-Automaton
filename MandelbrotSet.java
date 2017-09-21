/**
* Class MandelbrotSet that emultes the Mandelbrot set
* @author Carlos Gallardo Polanco
*/

import java.awt.image.BufferedImage;

public class MandelbrotSet implements Runnable{
  private static int MAX_ITER, nDim;
  private final double ZOOM = 250;
  static BufferedImage image;
  private double zx, zy, cX, cY, tmp;
  int ulimit, llimit;

  /**
   * Constructor
   * @param  int ulimit        upper limit
   * @param  int llimit        lower limit
   */
  public MandelbrotSet(int ulimit, int llimit){
      this.ulimit=ulimit;
      this.llimit=llimit;
  }

  /**
   * No-argument constructor
   */
  public MandelbrotSet(){}

  /**
   * run method
   */
  public void run(){
    compute();
  }

  /**
   * setup method
   * @param int iter Iterations
   * @param int n    Dimension
   */
  public void setup(int iter, int n){
    MAX_ITER=iter;
    nDim=n;
    image = new BufferedImage(nDim,nDim, BufferedImage.TYPE_INT_RGB);
  }

  /**
   * get_image method
   * @return image of the Mandelbrot set
   */
  public BufferedImage get_image(){
    return image;
  }

  /**
   * compute method
   */
  public void compute(){
    for(int y=llimit; y<ulimit; ++y){
      for(int x=0; x<nDim ; ++x){
        zx = zy = 0;
        cX = (x - 400) / ZOOM;
        cY = (y - 300) / ZOOM;
        int iter = MAX_ITER;
        while (zx * zx + zy * zy < 4 && iter > 0) {
          tmp = zx * zx - zy * zy + cX;
          zy = 2.0 * zx * zy + cY;
          zx = tmp;
          iter--;
        }
        image.setRGB(x, y, iter | (iter << 8));
      }
    }
  }
}
