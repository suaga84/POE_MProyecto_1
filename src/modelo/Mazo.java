package modelo;
import java.util.Random;

public class Mazo {
    //ATRIBUTOS
    private Carta[] cartas; 
    private int indiceActual;
    //CONSTRUCTOR
    public Mazo() {
        cartas = new Carta[52];
        indiceActual = 0; //marcador que guarda cual es la siguiente carta que se va a repartir del mazo
        inicializarMazo();
        mezclar();
    }
    //METODOS
    public void inicializarMazo() {
        String[] palos = {"CORAZONES", "DIAMANTES", "PICAS", "TREBOLES"};
        String[] nombres = {"AS", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K"};
        
        int index = 0; //contador temporal para recorrer las 52 posiciones del arreglo mientras los bucles van creando las cartas
        for (String palo : palos) {
            for (int i = 0; i < nombres.length; i++) {
                String nombre = nombres[i];
                int valor;
                
                if (nombre.equals("AS")) {
                    valor = 11;
                } else if (nombre.equals("J") || nombre.equals("Q") || nombre.equals("K")) {
                    valor = 10;
                } else {
                    valor = Integer.parseInt(nombre); 
                }
                
                cartas[index] = new Carta(nombre, palo, valor);
                index++;
            }
        }
        indiceActual = 0; 
    }
    //MEZCLA LAS CARTAS INICIANDO DESDE ATRAS HACIA ADELANTE
    public void mezclar() {
        Random rand = new Random();
        for (int i = cartas.length - 1; i > 0; i--) {
            int j = rand.nextInt(i + 1); 
            Carta temp = cartas[i];    
            cartas[i] = cartas[j];
            cartas[j] = temp;
        }
        indiceActual = 0;
    }

    public Carta repartirCarta() {
        if (indiceActual < cartas.length) { 
            return cartas[indiceActual++];
        }
        return null;
    }
}
