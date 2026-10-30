package modelo;
public class Nodo {
    //ATRIBUTOS
    private Carta carta;
    private Nodo siguiente;
    //CONSTRUCTOR
    public Nodo(Carta carta){
        this.carta = carta;
        this.siguiente = null;
    }
    //METODOS
    public Carta getCarta(){
        return carta;
    }
    public Nodo getSiguiente(){
        return siguiente;
    }
    public void setSiguiente(Nodo siguiente){
        this.siguiente = siguiente;
    }
}
