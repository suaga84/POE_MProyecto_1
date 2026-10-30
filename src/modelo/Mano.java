package modelo;
public class Mano {
    //ATRIBUTOS
    private Nodo cabeza;
    private int tamano;
    //CONSTRUCTOR
    public Mano() {
        this.cabeza = null;
        this.tamano = 0;
    }
    //METODOS
    public void agregarCarta(Carta carta) {
        Nodo nuevoNodo = new Nodo(carta); 
        if (cabeza == null) {
            cabeza = nuevoNodo;
        } else {
            Nodo actual = cabeza;
            while (actual.getSiguiente() != null) {
                actual = actual.getSiguiente();
            }
            actual.setSiguiente(nuevoNodo);
        }
        tamano++;
    }

    public void limpiarMano() {
        cabeza = null;
        tamano = 0;
    }

    public int calcularPuntaje() {
        int suma = 0;
        int cantidadAses = 0; 
        Nodo actual = cabeza;

        while (actual != null) {
            Carta c = actual.getCarta();
            suma += c.getValorNumerico();
            if (c.esAs()) {
                cantidadAses++;
            }
            actual = actual.getSiguiente();
        }

        while (suma > 21 && cantidadAses > 0) {
            suma -= 10;
            cantidadAses--;
        }
        return suma;
    }
    //ASEGURA UN TOTAL DE 14, UN 4 Y UN  J, K, Q
    public boolean esVeinteYMedio() {
        if (tamano == 2) {
            Nodo c1 = cabeza;
            Nodo c2 = cabeza.getSiguiente();
            if (c1 != null && c2 != null) {
                String n1 = c1.getCarta().getNombre();
                String n2 = c2.getCarta().getNombre();
                boolean tieneCuatro = n1.equals("4") || n2.equals("4");
                boolean tieneFigura = n1.equals("J") || n1.equals("Q") || n1.equals("K") ||
                                      n2.equals("J") || n2.equals("Q") || n2.equals("K");
                return tieneCuatro && tieneFigura;
            }
        }
        return false;
    }
    //ASEGURA QUE UNA CARTA SEA UN As Y OTRA UN VALOR DE 10
    public boolean tieneBlackjack() {
        if (tamano == 2) {
            Nodo c1 = cabeza;
            Nodo c2 = cabeza.getSiguiente();
            if (c1 != null && c2 != null) {
                boolean esAs1 = c1.getCarta().esAs();
                boolean esAs2 = c2.getCarta().esAs();
                int v1 = c1.getCarta().getValorNumerico();
                int v2 = c2.getCarta().getValorNumerico();
                return (esAs1 && v2 == 10) || (esAs2 && v1 == 10);
            }
        }
        return false;
    }

    public boolean sePaso() {
        return calcularPuntaje() > 21;
    }
    public Nodo getCabeza() {
        return cabeza;
    }
    public int getTamano() {
        return tamano;
    }
    //ASEGURA QUE EL TOTAL DE CARTAS SUMEN 21 PERO QUE NO SEA UN BLACKJACK
    public boolean esVeintiuno(){
        return calcularPuntaje() == 21 && !tieneBlackjack();
    }
}
