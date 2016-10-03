package es.upm.dit.aled.lab2;

import java.awt.Font;


public class Laberinto {
    protected int N; // Tamano alto y ancho del laberinto
    // norte, sur este y oeste nos dicen cuando hay paredes o no alrededor de una posicion
    protected boolean[][] norte;
    protected boolean[][] este;
    protected boolean[][] sur;
    protected boolean[][] oeste;
    // visitado nos dice cuando hemos pasado por una posicion
    protected boolean[][] visitado;

    // encontrado: hemos llegado al punto central
    protected boolean encontrado = false;
    protected double radio=StdDraw.getPenRadius();
    private int total=0; // para saber los pasos que hemos dado.

    public Laberinto(int N) {
        this.N = N;
        StdDraw.setCanvasSize(700,700);
        StdDraw.setXscale(0, N+2);
        StdDraw.setYscale(0, N+2);
        init();
        generar();
    }

    private void init() {
        // todas las posiciones del marco exterior marcadas como visitadas
        visitado = new boolean[N+2][N+2];
        for (int x = 0; x < N+2; x++) visitado[x][0] = visitado[x][N+1] = true;
        for (int y = 0; y < N+2; y++) visitado[0][y] = visitado[N+1][y] = true;


        // todas las paredes levantadas
        norte = new boolean[N+2][N+2];
        este  = new boolean[N+2][N+2];
        sur = new boolean[N+2][N+2];
        oeste  = new boolean[N+2][N+2];
        for (int x = 0; x < N+2; x++)
            for (int y = 0; y < N+2; y++)
                norte[x][y] = este[x][y] = sur[x][y] = oeste[x][y] = true;
    }

    // generar el laberinto
    private void generar(int x, int y) {
        visitado[x][y] = true;

        // mientras tengamos una posicion junto a esta que no este visitada
        while (!visitado[x][y+1] || !visitado[x+1][y] || !visitado[x][y-1] || !visitado[x-1][y]) {

            // elegimos una posicion cualquiera
            while (true) {
                double r = Math.random();
                if (r < 0.25 && !visitado[x][y+1]) {
                    norte[x][y] = sur[x][y+1] = false;
                    generar(x, y + 1);
                    break;
                }
                else if (r >= 0.25 && r < 0.50 && !visitado[x+1][y]) {
                    este[x][y] = oeste[x+1][y] = false;
                    generar(x+1, y);
                    break;
                }
                else if (r >= 0.5 && r < 0.75 && !visitado[x][y-1]) {
                    sur[x][y] = norte[x][y-1] = false;
                    generar(x, y-1);
                    break;
                }
                else if (r >= 0.75 && r < 1.00 && !visitado[x-1][y]) {
                    oeste[x][y] = este[x-1][y] = false;
                    generar(x-1, y);
                    break;
                }
            }
        }
    }

    // empezamos a generar por la posicion izquierda y abajo
    protected void generar() {
        generar(1, 1);
    }

    // resolvemos buscado la primera solucion que encontremos
    /**
     *Metodo que resuelve el laberinto 
     * @param x: coordenada x.
     * @param y: coordenada y.
     * @param pasos: pasos que llevamos dados
     * @return nada porque es inutil
     */
    private int resolver(int x, int y, int pasos) {
    
    	if(visitado[x][y]){ // si ya he estado ahi vuelvo (como si fuera una pared el sitio en el que ya he estado)
    		return 0;
    	}
    	visitado[x][y]=true; // marcamos el sitio como visitado y lo pintamos de azul
    	StdDraw.setPenColor(StdDraw.BLUE);
    	StdDraw.filledCircle(x+0.5, y+0.5, 0.25);
    	StdDraw.show(0);
    	if(x==Math.round(N/2.0)&& y== Math.round(N/2.0)){// Si estoy en la posicion central cambio encontrado y lo pongo true.
    		total=pasos;
    		encontrado=true;
    		return total;
    	}
    	if(encontrado)return total;
    	
    	double random = Math.random()*11;//creo un numero aleatorio
    	
    	if(random<3){
    	if(!norte[x][y])resolver(x,y+1,pasos+1);// voy pal norte y vuelvo a empezar el metodo con un paso mas
    	if(!este[x][y])resolver(x+1,y,pasos+1);
    	if(!sur[x][y])resolver(x,y-1,pasos+1);
    	if(!oeste[x][y])resolver(x-1,y,pasos+1);
    	}
    	if (random >=3 && random<6){
    		if(!este[x][y])resolver(x+1,y,pasos+1);
        	if(!sur[x][y])resolver(x,y-1,pasos+1);
        	if(!oeste[x][y])resolver(x-1,y,pasos+1);
        	if(!norte[x][y])resolver(x,y+1,pasos+1);
    	}
    	if(random>=6 && random<9){
    		if(!sur[x][y])resolver(x,y-1,pasos+1);
        	if(!oeste[x][y])resolver(x-1,y,pasos+1);
        	if(!norte[x][y])resolver(x,y+1,pasos+1);
        	if(!este[x][y])resolver(x+1,y,pasos+1);
    	}
    	if(random >=9){
    		if(!oeste[x][y])resolver(x-1,y,pasos+1);
        	if(!norte[x][y])resolver(x,y+1,pasos+1);
        	if(!este[x][y])resolver(x+1,y,pasos+1);
        	if(!sur[x][y])resolver(x,y-1,pasos+1);
    	}
    	
    	if(encontrado){
    		return total;
    	}
    	StdDraw.setPenColor(StdDraw.GRAY);// si no puedo ir a ningún lado lo pinto de gris 
    	StdDraw.filledCircle(x+0.5, y+0.5, 0.25);
    	StdDraw.show(0);
    	return 0;// vuelvo al punto anterior 
    
    }

    // resolver empezando en la primera posicion: abajo a la izquierda
    /**
     * Resolución final del laberinto con una solución
     * @return número de pasos dados
     */
    public int resolver() {
    	long t=System.currentTimeMillis(); // segundos actuales
    	for (int x = 1; x <= N; x++)
            for (int y = 1; y <= N; y++)
                visitado[x][y] = false;
        encontrado = false;
        int pasos=resolver(1, 1, 1); // Inútil totalmente
        StdDraw.show(0);
        System.out.println("Tiempo total "+(System.currentTimeMillis()-t));// imprime lo que tardamos
        return total;
    }

    // dibuja el laberinto
    public void dibuja() {
    	StdDraw.clear();
    	StdDraw.setPenRadius(radio);
        StdDraw.setPenColor(StdDraw.RED);
        StdDraw.filledCircle(Math.round(N/2.0) + 0.5, Math.round(N/2.0) + 0.5, 0.375);
        StdDraw.filledCircle(1.5, 1.5, 0.375);

        StdDraw.setPenColor(StdDraw.BLACK);
        Font f = StdDraw.getFont();
        int size=300/N;
        StdDraw.setFont(new Font(null,0, size > 12 ? 16 : size));
        for (int x = 1; x <= N; x++)
        	StdDraw.text(x+0.5, 0, new Integer(x).toString());
        for (int y = 1; y <= N; y++)
        	StdDraw.text(0, y+0.5, new Integer(y).toString());
        StdDraw.setFont(f);
        for (int x = 1; x <= N; x++) {
            for (int y = 1; y <= N; y++) {
                if (sur[x][y]) StdDraw.line(x, y, x + 1, y);
                if (norte[x][y]) StdDraw.line(x, y + 1, x + 1, y + 1);
                if (oeste[x][y])  StdDraw.line(x, y, x, y + 1);
                if (este[x][y])  StdDraw.line(x + 1, y, x + 1, y + 1);
            }
        }
        StdDraw.show(0);
    }

    // una prueba
    public static void main(String[] args) {
        int N = Integer.parseInt(args[0]);
        Laberinto laberinto = new Laberinto(N);
        StdDraw.show(0);
        laberinto.dibuja();
        System.out.println("Pasos hasta solucion: "+laberinto.resolver());
    }

}

