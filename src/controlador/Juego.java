package controlador;
import modelo.Carta;
import modelo.Mano;
import modelo.Mazo;

public class Juego {
    //ATRIBUTOS
    private Mazo mazo;
    private Mano manoJugador;
    private Mano manoComputador;
    private EstadoTurno estadoTurno;
    private Resultado resultado;
    private int saldo;  //
    private int apuesta;
    private String mensaje;
    //CONSTRUCTOR
    public Juego() {
        this.saldo = 1000;
        this.apuesta = 0;
        this.mazo = new Mazo();
        this.manoJugador = new Mano();
        this.manoComputador = new Mano();
        this.estadoTurno = EstadoTurno.ESPERANDO_APUESTA;
        this.mensaje = "Ingrese una apuesta válida para comenzar.";
    }
    //METODOS
    public int validarApuesta(String texto) { //CONDICIONES PARA VALIDAR UNA APUESTA
        try {
            if (texto == null || texto.trim().isEmpty()) {
                mensaje = "Ingrese el valor de la apuesta.";
                return -1;
            }
            int valor = Integer.parseInt(texto.trim()); 
            if (valor < 10) {
                mensaje = "La apuesta mínima es de 10 fichas.";
                return -1;
            }
            if (valor % 10 != 0) {
                mensaje = "La apuesta debe ser múltiplo de 10.";
                return -1;
            }
            if (valor > saldo) {
                mensaje = "No tienes saldo suficiente (" + saldo + ").";
                return -1;
            }
            return valor;
        } catch (NumberFormatException e) {
            mensaje = "Ingrese un valor entero válido.";
            return -1;
        }
    }
    // INICIO DE LA RONDA
    public void iniciarRonda(int apuestaValidada) {
    this.apuesta = apuestaValidada;
    this.saldo -= this.apuesta; //restamos el valor de la apuesta al saldo
    this.mazo = new Mazo();
    this.mazo.mezclar();
    this.manoJugador.limpiarMano();
    this.manoComputador.limpiarMano();

    //REPARTIR 2 CARTAS INICIALES
    //Repartir las 2 cartas iniciales al jugador
    this.manoJugador.agregarCarta(mazo.repartirCarta());
    this.manoJugador.agregarCarta(mazo.repartirCarta());
    // error de turnos:
    this.manoComputador.agregarCarta(mazo.repartirCarta());
    this.manoComputador.agregarCarta(mazo.repartirCarta());

    this.estadoTurno = EstadoTurno.TURNO_JUGADOR; //le da el turno al jugador despues de repartir las cartas
   
    //MENSAJE "TIENES BLACKJACK", "(20 y 1/2) 14 PUNTOS", "RONDA INICIADA"
    if (manoJugador.tieneBlackjack() || manoJugador.calcularPuntaje() == 21) {
        mensaje = "¡Tienes " + (manoJugador.tieneBlackjack() ? "Blackjack!" : "21 puntos!") + " ...";
        ejecutarTurnoComputador(); //le pasa el turno al computador
    } else if (manoJugador.esVeinteYMedio()) {
        mensaje = "¡Has obtenido Veinte y medio (14 puntos)!";
    } else {
        mensaje = "Ronda iniciada, puedes pedir carta o plantarte.";
    }
}

    public void pedirCartaJugador() {
        if (estadoTurno == EstadoTurno.TURNO_JUGADOR) {
            Carta nueva = mazo.repartirCarta();
            manoJugador.agregarCarta(nueva);
            
            if (manoJugador.esVeinteYMedio()) {
                mensaje = "¡Veinte y medio!";
            } else if (manoJugador.sePaso()) {
                estadoTurno = EstadoTurno.RONDA_TERMINADA;
                resultado = Resultado.DERROTA;
                mensaje = "Te has pasado de 21. ¡Perdiste!";
            } else if (manoJugador.calcularPuntaje() == 21) {
                mensaje = "Alcanzaste 21. Turno del rival.";
                ejecutarTurnoComputador();
            } else {
                mensaje = "Carta recibida. Puntaje actual: " + manoJugador.calcularPuntaje();
            }
        }
    }

    public void plantarseJugador() {
        if (estadoTurno == EstadoTurno.TURNO_JUGADOR) {
            mensaje = "Te has plantado. Turno del rival.";
            ejecutarTurnoComputador();
        }
    }

    public void ejecutarTurnoComputador() { 
        estadoTurno = EstadoTurno.TURNO_COMPUTADOR;
        //manoComputador.limpiarMano();
        manoComputador.agregarCarta(mazo.repartirCarta());
        manoComputador.agregarCarta(mazo.repartirCarta());
        
        while (manoComputador.calcularPuntaje() < 17) {
            manoComputador.agregarCarta(mazo.repartirCarta());
        }

        determinarResultado();
        liquidarApuesta();
        estadoTurno = EstadoTurno.RONDA_TERMINADA;
    }
//-------------
    public Resultado determinarResultado() { 
        boolean bjJugador = manoJugador.tieneBlackjack();
        boolean bjComputador = manoComputador.tieneBlackjack();
        int pJugador = manoJugador.calcularPuntaje();
        int pComputador = manoComputador.calcularPuntaje();

        if (manoJugador.sePaso()) {
            resultado = Resultado.DERROTA;
            mensaje = "Te pasaste de 21. Gana el rival.";
        } else if (bjJugador && bjComputador) {
            resultado = Resultado.EMPATE;
            mensaje = "Empate: ambos tienen blackjack.";
        } else if (bjJugador) {
            resultado = Resultado.VICTORIA_BLACKJACK;
            mensaje = "¡Victoria con Blackjack!";
        } else if (bjComputador) {
            resultado = Resultado.DERROTA;
            mensaje = "El computador tiene Blackjack. Gana la banca.";
        } else if (manoComputador.sePaso()) {
            resultado = Resultado.VICTORIA;
            mensaje = "El computador se pasó de 21. ¡Has ganado!";
        } else {
            boolean vmidJugador = manoJugador.esVeinteYMedio();
            boolean vmidComp = manoComputador.esVeinteYMedio();
            
            // DIFERENTES VICTORIAS
            if (vmidJugador && !vmidComp && pComputador < 21) {
                resultado = Resultado.VICTORIA;
                mensaje = "Ganas con Veinte y medio frente al rival.";
            } else if (!vmidJugador && vmidComp && pJugador < 21) {
                resultado = Resultado.DERROTA;
                mensaje = "El computador gana con Veinte y medio.";
            } else if (pJugador > pComputador) {
                resultado = Resultado.VICTORIA;
                mensaje = "Has ganado con " + pJugador + " puntos.";
            } else if (pJugador < pComputador) {
                resultado = Resultado.DERROTA;
                mensaje = "Gana el computador con " + pComputador + " puntos.";
            } else {
                resultado = Resultado.EMPATE;
                mensaje = "Empate: ambos tienen " + pJugador + " puntos.";
            }
        }
        return resultado;
    }
 //actualiza el saldo del usuario segun el resultado
    public void liquidarApuesta() { 
        switch (resultado) {
            case DERROTA:
                break;
            case EMPATE:
                saldo += apuesta;
                break;
                
            case VICTORIA:
                saldo += apuesta * 2;
                break;
            case VICTORIA_BLACKJACK:
                saldo += (int)(apuesta * 2.5);
                break;
        }
    }
    // puede apostar si tiene una cantidad mayor o igual a 10 fichas
    public boolean puedeApostar() { 
        return saldo >= 10;
    }
    //GETTERS
    public int getSaldo() { return saldo; }
    public int getApuesta() { return apuesta; }
    public EstadoTurno getEstado() { return estadoTurno; }
    public Resultado getResultado() { return resultado; }
    public Mano getManoJugador() { return manoJugador; }
    public Mano getManoComputador() { return manoComputador; }
    public String getMensaje() { return mensaje; }
}
