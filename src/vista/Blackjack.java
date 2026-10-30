package vista;
import controlador.Juego;
import controlador.Resultado;
import java.util.Optional;
import controlador.EstadoTurno;
import modelo.Mano;
import modelo.Nodo;
import modelo.Carta;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.scene.image.Image;    
import javafx.scene.image.ImageView;

public class Blackjack extends Application {
    private Juego juego;
    private Label lblSaldo;
    private Label lblApuesta;
    private Label lblBtnApuesta;
    private Label lblMensaje;
    private Label lblPuntajeJugador;
    private Label lblPuntajeComputador;
    private TextField txtApuesta;
    private Button btnApostar;
    private Button btnPedirCarta;
    private Button btnPlantarse;
    private Button btnReglas;
    private Button btnCerrarJuego;
    private HBox panelCartasJugador = new HBox(10);
    private HBox panelCartasComputador = new HBox(10);

    @Override
    public void start(Stage primaryStage) {
        juego = new Juego();

        // 1. PANEL SUPERIOR ----------------------------------------------------------------
        HBox panelSuperior = new HBox(15);
        panelSuperior.setPadding(new Insets(15));
        panelSuperior.setAlignment(Pos.CENTER);
        panelSuperior.setStyle("-fx-background-color: #2c3e50;");

        lblSaldo = new Label("Saldo: " + juego.getSaldo() + " fichas");
        lblSaldo.setStyle("-fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold;" + "-fx-background-color: #1a252f;" + "-fx-padding: 6px 12px;" + "-fx-background-radius: 5px;");

        lblApuesta = new Label("Apuesta actual: 0");
        lblApuesta.setStyle("-fx-text-fill: #f1c40f; -fx-font-size: 14px;" + "-fx-background-color: #1a252f;" + "-fx-padding: 6px 12px;" + "-fx-background-radius: 5px;");

        lblMensaje = new Label(juego.getMensaje());
        lblMensaje.setStyle("-fx-text-fill: #ecf0f1; -fx-font-size: 14px; -fx-font-style: italic;" + "-fx-background-color: #1a252f;" + "-fx-padding: 6px 12px;" + "-fx-background-radius: 5px;");

        btnReglas = new Button("Reglas");
        btnReglas.setStyle("-fx-background-color: #1f2c38; " + "-fx-text-fill: white; " + "-fx-font-weight: bold; " + "-fx-padding: 8px 15px; " + "-fx-background-radius: 5px;" + "-fx-border-color: #bdc3c7; " + "-fx-border-radius: 6px; " + "-fx-border-width: 1px;");
        btnReglas.setOnAction(e -> mostrarReglas());

        btnCerrarJuego = new Button("Cerrar");
        btnCerrarJuego.setStyle("-fx-background-color: #4b0d0d; " + "-fx-text-fill: white; " + "-fx-font-weight: bold; " + "-fx-padding: 8px 15px; " + "-fx-background-radius: 5px;" + "-fx-border-color: #bdc3c7; " + "-fx-border-radius: 6px; " + "-fx-border-width: 1px;");
        btnCerrarJuego.setOnAction(e -> confirmarCierreVentana());

        panelSuperior.getChildren().addAll(lblSaldo, lblApuesta, lblMensaje, btnReglas, btnCerrarJuego);

        // 2. PANEL CENTRAL ----------------------------------------------------------------
        VBox panelCentral = new VBox(20);
        panelCentral.setPadding(new Insets(20));
        panelCentral.setAlignment(Pos.CENTER);

        //FONDO VERDE CON DISEÑO
        panelCentral.setStyle("-fx-background-image: url('file:imagenes/fondo.jpg'); " + "-fx-background-size: cover; " + "-fx-background-position: center center;");
        //DATOS PARA EL COMPUTADOR
        VBox boxComputador = new VBox(5);
        boxComputador.setAlignment(Pos.CENTER);
        Label tituloComp = new Label("Mano del Computador (Banca):");
        tituloComp.setStyle("-fx-text-fill: #000000 ; -fx-font-size: 14px; -fx-font-weight: bold;");
        panelCartasComputador.setAlignment(Pos.CENTER);
        lblPuntajeComputador = new Label("Puntaje: 0");
        lblPuntajeComputador.setStyle("-fx-text-fill: #5c7aff; -fx-font-size: 14px; -fx-font-weight: bold;" + "-fx-background-color: #1a252f; " + "-fx-padding: 6px 12px; " + "-fx-background-radius: 5px;");
        boxComputador.getChildren().addAll(tituloComp, panelCartasComputador, lblPuntajeComputador);
        //DATOS PARA EL JUGADOR
        VBox boxJugador = new VBox(5);
        boxJugador.setAlignment(Pos.CENTER);
        Label tituloJugador = new Label("Tu Mano:");
        tituloJugador.setStyle("-fx-text-fill: #000000 ; -fx-font-size: 14px; -fx-font-weight: bold;");
        panelCartasJugador.setAlignment(Pos.CENTER);
        lblPuntajeJugador = new Label("Puntaje: 0");
        lblPuntajeJugador.setStyle("-fx-text-fill: #5c7aff; -fx-font-size: 14px; -fx-font-weight: bold;" + "-fx-background-color: #1a252f;" + "-fx-padding: 6px 12px; " + "-fx-background-radius: 5px;");
        boxJugador.getChildren().addAll(tituloJugador, panelCartasJugador, lblPuntajeJugador);

        panelCentral.getChildren().addAll(boxComputador, boxJugador);

        // 3. PANEL INFERIOR ----------------------------------------------------------------
        HBox panelControles = new HBox(10);
        panelControles.setPadding(new Insets(15));
        panelControles.setAlignment(Pos.CENTER);
        panelControles.setStyle("-fx-background-color: #2c3e50;");

        txtApuesta = new TextField();
        txtApuesta.setPromptText("Ingrese apuesta (mult. de 10)");

        btnApostar = new Button("Apostar / Iniciar Ronda");
        btnPedirCarta = new Button("Pedir Carta");
        btnPlantarse = new Button("Plantarse");

        //desactiva los botones para que el jugador no los pueda presionar
        btnPedirCarta.setDisable(true);
        btnPlantarse.setDisable(true);
        //mostrar
        panelControles.getChildren().addAll(lblBtnApuesta = new Label("Apuesta:"), txtApuesta, btnApostar, btnPedirCarta, btnPlantarse);
        lblBtnApuesta.setStyle("-fx-text-fill: #000000; -fx-font-size: 14px;");

        // EVENTOS
        btnApostar.setOnAction(e -> gestionarApuesta());
        btnPedirCarta.setOnAction(e -> gestionarPedirCarta());
        btnPlantarse.setOnAction(e -> gestionarPlantarse());
        //Organiza el panel principal en 3 secciones: superior, central, inferior
        BorderPane root = new BorderPane();
        root.setTop(panelSuperior);
        root.setCenter(panelCentral);
        root.setBottom(panelControles);

        Scene scene = new Scene(root, 650, 700);
        primaryStage.setTitle("Juego 21 Blackjack - MVC");
        
        confirmarCierreVentanaX(primaryStage);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    //METODOS -----------------------------------------------------------------------------

    //CODIGO PARA CONFIRMAR CIERRE DEL JUEGO. UNA ES PARA LA X DE LA VENTANA Y OTRA PARA EL BOTON
    private void confirmarCierreVentana(){
        Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
        alerta.setTitle("Salir del juego");
        alerta.setHeaderText(null);
        alerta.setContentText("¿Estás seguro de cerrar el juego?");

        Optional<ButtonType> resultado = alerta.showAndWait();
        if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
            Platform.exit();
            System.exit(0);
        }
    }
    private void confirmarCierreVentanaX(Stage primaryStage){
            primaryStage.setOnCloseRequest(event -> {
            event.consume(); // evita cierre de una vez

            Alert alerta = new Alert(Alert.AlertType.CONFIRMATION); //Creacion de la ventana de confirmacion
            alerta.setTitle("Salir del juego");
            alerta.setHeaderText(null);
            alerta.setContentText("¿Estás seguro de cerrar el juego?");

            Optional<ButtonType> resultado = alerta.showAndWait(); // decision del usuario
            if(resultado.isPresent() && resultado.get() == ButtonType.OK){ //"Si" cierra el juego
                Platform.exit();
                System.exit(0);
            }
        }); //------------------------------------------
    }
   // Método auxiliar adaptado a tu estructura de Nodos
   //INFORMACIÓN DE LAS CARTAS
    private void actualizarManoGrafica(HBox contenedor, Mano mano, boolean esComputador, boolean ocultarPrimeraCarta) {
        contenedor.getChildren().clear();

        Nodo actual = mano.getCabeza();
        int i = 0;
        while (actual != null) {
            Carta carta = actual.getCarta();

            VBox cartaConInfo = new VBox(5);
            cartaConInfo.setAlignment(Pos.CENTER);

            ImageView imageView = new ImageView();
            imageView.setFitWidth(100);
            imageView.setFitHeight(140);
            imageView.setPreserveRatio(true);

            if (esComputador && i == 0 && ocultarPrimeraCarta) {
                Image reverso = new Image("file:imagenes/reverso.png");
                imageView.setImage(reverso);
            
                Label lblDebug = new Label("Oculta");
                lblDebug.setStyle("-fx-text-fill: #f1c40f; -fx-font-size: 11px; -fx-font-weight: bold;");
                cartaConInfo.getChildren().addAll(lblDebug, imageView);
            } else {
            // ETIQUETA INFORMACION DE LA CARTA
                String info = carta.getNombre() + " de " + carta.getPalo(); //+ " V" + carta.getValorNumerico() + "";
                Label lblDebug = new Label(info);
                lblDebug.setStyle("-fx-text-fill: #f1c40f; -fx-font-size: 12px; -fx-font-weight: bold;");

            // Cargar la imagen real de la carta
                String rutaArchivo = "file:imagenes/" + carta.getNombreImagen();
                Image img = new Image(rutaArchivo);
                imageView.setImage(img);

                cartaConInfo.getChildren().addAll(lblDebug, imageView);
            }
        
        // unico contenedor que se añade al HBox principal
            contenedor.getChildren().add(cartaConInfo);

            actual = actual.getSiguiente();
            i++;
        }
    }

    private void gestionarApuesta() {
        int apuestaValida = juego.validarApuesta(txtApuesta.getText());
        if (apuestaValida != -1) {
            //reseteo de color y apartado de "GANASTE" o "PERDISTE"
            lblMensaje.setStyle("-fx-text-fill: #ffffff; -fx-font-size: 14px; -fx-font-style: italic; -fx-background-color: transparent;");
            //Color blanco para apuestas validas
            lblMensaje.setStyle("-fx-text-fill: #ffffff; -fx-font-size: 14px; -fx-font-style: italic;");
            juego.iniciarRonda(apuestaValida);
            actualizarVistaJuego();
            
            if (juego.getEstado() == EstadoTurno.TURNO_JUGADOR) {
                btnPedirCarta.setDisable(false);
                btnPlantarse.setDisable(false);
                btnApostar.setDisable(true);
                txtApuesta.setDisable(true);
                actualizarVistaJuego();
            } else {
                finalizarRondaUI();
            }
        } else {
            // Color rojo pal ingresar un dato incompatible con la apuesta
            lblMensaje.setStyle("-fx-text-fill: #d4200c; -fx-font-size: 14px; -fx-font-style: italic;");
            lblMensaje.setText(juego.getMensaje());
        }
    }

    private void gestionarPedirCarta() {
        juego.pedirCartaJugador();
        actualizarVistaJuego();

        if (juego.getEstado() == EstadoTurno.RONDA_TERMINADA) {
            finalizarRondaUI();
        }
    }

    private void gestionarPlantarse() {
        juego.plantarseJugador();
        actualizarVistaJuego();
        finalizarRondaUI();
    }
// error anterior de turnos con el computador
    private void actualizarVistaJuego() {
        lblSaldo.setText("Saldo: " + juego.getSaldo() + " fichas");
        lblApuesta.setText("Apuesta actual: " + juego.getApuesta());
        lblMensaje.setText(juego.getMensaje());
        lblPuntajeJugador.setText("Puntaje: " + juego.getManoJugador().calcularPuntaje());
        lblPuntajeComputador.setText("Puntaje: " + juego.getManoComputador().calcularPuntaje());
        /*if (juego.getEstado() == EstadoTurno.TURNO_JUGADOR) {         Esto es para que el computador no tenga cartas al inicio
            lblPuntajeComputador.setText("Puntaje: Oculto");
        } else {
            lblPuntajeComputador.setText("Puntaje: " + juego.getManoComputador().calcularPuntaje());
        }*/
        actualizarManoGrafica(panelCartasJugador, juego.getManoJugador(), false, false);
        actualizarManoGrafica(panelCartasComputador, juego.getManoComputador(), false, false); //juego.getEstado() == EstadoTurno.TURNO_JUGADOR);
    }
// APARTADO DE REGLAS
    private void mostrarReglas(){
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle("Reglas del juego");
        alerta.setHeaderText("Guia del juego");
        String textoReglas = "Objetivo: Acercarse lo más posible a 21 puntos sin pasarse, y superar la puntuación de la banca.\n\n" +
        "- Valores de las cartas: Las cartas numéricas valen su valor nominal. Las figuras (J, Q, K) valen 10 puntos.\n\n" +
        "- Los Ases: Valen 11 u 1 punto de manera flexible según convenga al jugador para evitar pasarse.\n\n" +
        "- Regla especial - Veinte y medio: Esta combinación especial sucede al tener 14 puntos.\n\n" +
        "- Apuestas y Pagos: Debes ingresar un monto válido múltiplo de las reglas antes de iniciar la ronda. Las victorias por Blackjack pagan con bonificación especial.\n\n" +
        "- Prioridad de Blackjack: Una mano inicial de Blackjack natural (As + Figura/10) tiene la máxima prioridad sobre cualquier otro 21 compuesto por más de dos cartas.\n\n" +
        "- Reglas del Computador (Banca): La banca está obligada a pedir cartas de forma automática hasta alcanzar o superar un puntaje mínimo de 17.";
        alerta.setContentText(textoReglas);
        alerta.showAndWait();
    }
// MANEJO DE APARTADOS EMERGENTES "GANASTE" O "PERDISTE"
    private void finalizarRondaUI() {
        btnPedirCarta.setDisable(true);
        btnPlantarse.setDisable(true);
        
        //APARTADO DE GANASTE O PERDISTE ---------------------
        Resultado resultado = juego.getResultado();
        
        if(resultado == Resultado.VICTORIA || resultado == Resultado.VICTORIA_BLACKJACK){
            lblMensaje.setStyle("-fx-text-fill: #2bc56b; " + "-fx-font-size: 16px; " + "-fx-font-weight: bold; " + "-fx-background-color: #1a252f; " + "-fx-padding: 10px 20px; " + "-fx-background-radius: 8px; " + "-fx-border-color: #2bc56b; " + "-fx-border-radius: 8px;");
        }
        else if(resultado == Resultado.DERROTA){ 
            lblMensaje.setStyle("-fx-text-fill: #da3f2d; " + "-fx-font-size: 16px; " + "-fx-font-weight: bold; " + "-fx-background-color: #1a252f; " + "-fx-padding: 10px 20px; " + "-fx-background-radius: 8px; " + "-fx-border-color: #da3f2d; " + "-fx-border-radius: 8px;");
        }
        else{ // empate
            lblMensaje.setStyle("-fx-text-fill: #e7bc10; " + "-fx-font-size: 16px; " + "-fx-font-weight: bold; " + "-fx-background-color: #1a252f; " + "-fx-padding: 10px 20px; " + "-fx-background-radius: 8px; " + "-fx-border-color: #e7bc10; " + "-fx-border-radius: 8px;");
        }
        //------------------------------------------------------
        if (juego.puedeApostar()) {
            btnApostar.setDisable(false);
            txtApuesta.setDisable(false);
            txtApuesta.clear();
        } else {
            lblMensaje.setText("¡Te has quedado sin saldo! Game Over.");
            lblMensaje.setStyle("-fx-text-fill: #ffffff; " + "-fx-font-size: 16px; " + "-fx-font-weight: bold; " + "-fx-background-color: #da3f2d; " + "-fx-padding: 10px 20px; " + "-fx-background-radius: 8px;");
            btnApostar.setDisable(true);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}