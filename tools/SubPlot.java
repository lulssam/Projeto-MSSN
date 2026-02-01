package tools;

/**
 * Classe utilitária para conversão entre coordenadas de "mundo" e coordenadas em píxeis.
 *
 * Um SubPlot define uma janela no mundo (window) e uma zona do ecrã
 * (viewport) onde essa janela será desenhada, permitindo:
 *  - converter pontos do mundo -> píxeis (para desenhar)
 *  - converter píxeis -> mundo (para input/colisões em coordenadas lógicas)
 *  - obter dimensões em píxeis a partir de dimensões no mundo
 *
 * A janela (window) segue o formato: [xmin, xmax, ymin, ymax].
 * O viewport (viewport) segue o formato: [x, y, w, h] em frações do ecrã (0..1).
 *
 * A conversão em y inverte o eixo vertical (o ecrã cresce para baixo),
 * pelo que a escala my é negativa.
 */

public class SubPlot {
    private double[] window;  //formato: [xmin, xmax, ymin, ymax]
    private float[] viewport;  //formato: [x, y, w, h] em fracoes do ecrã (0..1)
    private double mx;
    private double bx;
    private double my;
    private double by;

    public SubPlot(double[] window, float[] viewport, float fullwidth, float fullheight) {
        this.window = window;
        this.viewport = viewport;
        
        //mx/bx: escala e offset para converter x do mundo -> pixel
        mx = viewport[2] * fullwidth / (window[1] - window[0]);
        bx = viewport[0] * fullwidth;
        
        //my/by: escala e offset para converter y do mundo -> pixel
        //my é negativo porque o eixo y em pixel cresce para baixo
        my = -viewport[3] * fullheight / (window[3] - window[2]);
        by = (1 - viewport[1]) * fullheight;
    }

    public float[] getPixelCoord(double x, double y) {
        float[] coord = new float[2];
        
        //mundo -> pixel
        coord[0] = (float) (bx + mx * (x - window[0]));
        coord[1] = (float) (by + my * (y - window[2]));

        return coord;
    }

    public float[] getPixelCoord(double[] xy) {
        return getPixelCoord(xy[0], xy[1]);
    }

    public double[] getWorldCoord(float xx, float yy) {
        double[] coord = new double[2];
        
        //pixel -> mundo
        coord[0] = window[0] + (xx - bx) / mx;
        coord[1] = window[2] + (yy - by) / my;

        return coord;
    }

    public double[] getPixelCoord(float[] xy) {
        return getWorldCoord(xy[0], xy[1]);
    }
    
    //verifica se um ponto em pixel esta dentro da janela do mundo
    public boolean isInside(float xx, float yy) {
        double[] c = getWorldCoord(xx, yy);
        return (c[0] >= window[0] && c[0] <= window[1] && c[1] >= window[2] && c[1] <= window[3]);
    }

    public boolean isInside(float[] xy) {
        return isInside(xy[0], xy[1]);
    }

    //bounding box em pixel correspondente a janela completa
    public float[] getBoundingBox() {
    	
        float[] c1 = getPixelCoord(window[0], window[2]);
        float[] c2 = getPixelCoord(window[1], window[3]);
        float[] box = {c1[0], c2[1], c2[0] - c1[0], c1[1] - c2[1]};

        return box;
    }
    
    //caixa em pixel a partir de uma origem no mundo + dimensoes no mundo
    public float[] getBox(double cx, double cy, double dimx, double dimy) {
        float[] c1 = getPixelCoord(cx, cy);
        float[] c2 = getPixelCoord(cx + dimx, cy + dimy);
        float[] box = {c1[0], c2[1], c2[0] - c1[0], c1[1] - c2[1]};

        return box;
    }

    public float[] getBox(double[] b) {
        return getBox(b[0], b[1], b[2], b[3]);
    }
    
    //converte dimensoes do mundo para dimensoes em pixel
    public float[] getDimInPixel(double dx, double dy) {
        float[] v = new float[2];
        v[0] = (float) (dx * mx);
        v[1] = (float) (dy * my);

        return v;
    }

    public float[] getDimInPixel(double[] dxdy) {
        return getDimInPixel(dxdy[0], dxdy[1]);
    }

    public float[] getViewport() {
        return viewport;
    }

    public void setViewport(float[] viewport) {
        this.viewport = viewport;
    }

    public double[] getWindow() {
        return window;
    }

    public void setWindow(double[] window) {
        this.window = window;
    }
    
    //converte um vetor do mundo para pixel
    //sinal invertido em y para manter direcao consistente no ecrã
    public float[] getVectorCoord(double dx, double dy) {
        float[] v = new float[2];
        v[0] = (float) (dx * mx);
        v[1] = (float) (-dy * my);
        return v;
    }

}