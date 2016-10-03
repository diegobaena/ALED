package es.upm.dit.aled.lab2;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class LaberintoMultiplesSoluciones extends Laberinto {
	
	// Descripción de las paredes que vamos a tirar para crear multiples soluciones
	private int ES=N*3;
    private int[] xES=new int[ES];
    private int[] yES=new int[ES];
    private boolean[] isNS=new boolean[ES];
    
    // Camino con menor numero de pasos
    private int mejorPasos=Integer.MAX_VALUE;
    private List<Integer> mejorX=new ArrayList<Integer>();
    private List<Integer> mejorY=new ArrayList<Integer>();
    private List<Integer> xactual=new ArrayList<Integer>();
    private List<Integer> yactual=new ArrayList<Integer>();
	public LaberintoMultiplesSoluciones(int n) {
		super(n);
		int s=0;
		int ss=0;
		while (ss++ < ES*3) {
			// Generamos una posición aletoria que no sea ninguno de los bordes del tablero p
			// ara evitar tirar paredes del tablero
			int x=(new Random()).nextInt(N-2)+2;
			int y=(new Random()).nextInt(N-2)+2;
			if (este[x][y]) { // si esta posición tiene pared en el este la tiramos
				este[x][y] = oeste[x+1][y] = false;
				xES[s]=x;
				yES[s]=y;
				isNS[s]=false;
				s++;
				if (s == ES) break;
			}
			if (norte[x][y]) { // si esta posición tiene pared en el este la tiramos
				norte[x][y] = sur[x][y+1] = false;
				xES[s]=x;
				yES[s]=y;
				isNS[s]=true;
				s++;
				if (s == ES) break;
			}
			if (sur[x][y]) {
				sur[x][y] = norte[x][y-1] = false;
				xES[s]=x;
				yES[s]=y-1;
				isNS[s]=true;
				s++;
				if (s == ES) break;
			}
			if (oeste[x][y]) {
				oeste[x][y] = este[x-1][y] = false;
				xES[s]=x-1;
				yES[s]=y;
				isNS[s]=false;
				s++;
				if (s == ES) break;
			}
		}
		ES=s;
	}

    /**
     * Metodo que resuelve el laberinto mediante un algoritmo recursivo.
     * @param x: posición x del laberinto.
     * @param y: posición y del laberinto.
     * @param pasos: pasos que llevo dados
     * @return solo sirve para volver al anterior, se podría obviar. 
     */
    private int resolver(int x, int y, int pasos) {
    	if(pasos>=mejorPasos || visitado[x][y]){
    		
    		return 0;
    	}
    	visitado[x][y]=true;
    	xactual.add(x); // hago una lista con el camino actual
    	yactual.add(y);
    	if(x==Math.round(N/2.0)&& y== Math.round(N/2.0)){// Si estoy en la posicion central cambio encontrado y lo pongo true.
    		mejorPasos=pasos;
    		mejorX.clear();// borro la lista anterior
    		mejorY.clear();
    		for(Integer p : xactual){//meto la lista actual como la lista perfecta 
    			mejorX.add(p);// no se puede poner mejorX = xactual porque pone REFERENCIAS (son objetos)
    		}
    		for(Integer asa: yactual){
    			mejorY.add(asa);
    		}
    	
    		
    	}
    	else{
    		
        if(!norte[x][y])resolver(x,y+1,pasos+1);// voy pal norte y vuelvo a empezar el metodo con un paso mas
    	if(!este[x][y])resolver(x+1,y,pasos+1);
    	if(!sur[x][y])resolver(x,y-1,pasos+1);
    	if(!oeste[x][y])resolver(x-1,y,pasos+1);
    	// en este punto no he podido ir a ningún lado 
    	}

		visitado[x][y] = false; // lo marco como no visitado para que no sea una "pared"
		xactual.remove(pasos-1); // quito este sitio de la lista y vuelvo al pnto anterior
		yactual.remove(pasos-1);
		return pasos;
	
	}

    @Override
    public int resolver() {
       
    	for (int x = 1; x <= N; x++)
            for (int y = 1; y <= N; y++)
                visitado[x][y] = false;
        encontrado = false;
        resolver(1, 1, 1);
        //(YO)
        for(Integer a: mejorX){ // imprimo en pantalla todos los puntos de las listas solución
     	   System.out.println("X"+a);
        }
        for(Integer b: mejorY){
      	   System.out.println("Y"+b);
         }
        System.out.println("largo" + mejorY.size());//longitud de la lista
        
    	//(NO YO)
        StdDraw.setPenColor(StdDraw.BLUE);
        for (int i=0; i < mejorX.size(); i++) {
			StdDraw.filledCircle(mejorX.get(i) + 0.5, mejorY.get(i) + 0.5, 0.25);
        }
		StdDraw.show(0);
		return mejorPasos;
    }
    
	@Override
    public void dibuja() {
		super.dibuja();
		// para ver las paredes que hemos tirado quitar el comentario. Apareceran en amarillo
		// pero esas paredes ya no existen en los arrays norte,sur,este,oeste
		/*
        StdDraw.setPenColor(StdDraw.YELLOW);
        StdDraw.setPenRadius(radio*10);
        for(int i=0; i < ES; i++)
        	if (isNS[i]) StdDraw.line(xES[i], yES[i] + 1, xES[i] + 1, yES[i] + 1);
        	else StdDraw.line(xES[i] + 1, yES[i], xES[i] + 1, yES[i] + 1);
        */
	}
	
    public static void main(String[] args) {
        int N = Integer.parseInt(args[0]);
        LaberintoMultiplesSoluciones laberinto = new LaberintoMultiplesSoluciones(N);
        StdDraw.show(0);
        laberinto.dibuja();
        laberinto.resolver();
        System.out.println("Pasos hasta solucion: "+laberinto.mejorPasos);
    }
}
