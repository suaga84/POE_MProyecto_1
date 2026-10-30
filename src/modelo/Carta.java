package modelo;
public class Carta {
    //ATRIBUTOS
    private String nombre; //EJEMPLO: "As", "2", "Q"
    private String palo; //EJEMPLO: "CORAZONES", "DIAMANTES" 
    private int valorNumerico; //valor para sumar los puntajes

    //CONSTRUCTOR
    public Carta(String nombre, String palo, int valorNumerico) {
        this.nombre = nombre;
        this.palo = palo;
        this.valorNumerico = valorNumerico;
    }
    //METODOS   
    public String getNombre() {
        return nombre;
    }
    public String getPalo() {
        return palo;
    }
    public int getValorNumerico() {
        return valorNumerico;
    }
    // Metodo requerido por Mano.java para calcular los Ases
    public boolean esAs() {
        return this.nombre.equalsIgnoreCase("AS");
    }
    // Metodo que busca y retorna la imagen correcta automáticamente
    public String getNombreImagen() {
        return traducirNombreArchivo();
    }
    //CALCULA EL NOMBRE DEL ARCHIVO DE IMAGEN DE CADA CARTA
    private String traducirNombreArchivo() {
        int offsetPalo = 0;
        switch (palo.toUpperCase()) {
            case "CORAZONES": offsetPalo = 1; break;
            case "PICAS": offsetPalo = 15; break;
            case "DIAMANTES": offsetPalo = 29; break;
            case "TREBOLES": offsetPalo = 43; break;
        }
        //ASIGNA VALORES DEL 0 AL 12 SEGUN LA CARTA ("=" = "suma")
        //EJEMPLO: As=0, 2=1, 3=2, k=12
        int offsetValor = 0;
        switch (nombre.toUpperCase()) {
            case "AS": offsetValor = 0; break;
            case "2": offsetValor = 1; break;
            case "3": offsetValor = 2; break;
            case "4": offsetValor = 3; break;
            case "5": offsetValor = 4; break;
            case "6": offsetValor = 5; break;
            case "7": offsetValor = 6; break;
            case "8": offsetValor = 7; break;
            case "9": offsetValor = 8; break;
            case "10": offsetValor = 9; break;
            case "J": offsetValor = 10; break;
            case "Q": offsetValor = 11; break;
            case "K": offsetValor = 12; break;
        }
        //SUMA AMBOS VALORES PARA OBTENER EL NUMERO UNICO DE CADA CARTA 1-52
        int indexFinal = offsetPalo + offsetValor;
        return String.format("%02d_kerenel_Cards.png", indexFinal); //para crear el texto del archivo con 2 digitos
    }
}
