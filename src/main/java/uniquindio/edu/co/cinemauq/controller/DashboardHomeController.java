package uniquindio.edu.co.cinemauq.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import uniquindio.edu.co.cinemauq.model.Cinema;
import uniquindio.edu.co.cinemauq.model.Pelicula;

import java.net.URL;

public class DashboardHomeController {

    @FXML
    private StackPane heroContainer;

    @FXML
    private ImageView heroImageView;

    @FXML
    private HBox containerCartelera;

    private Cinema cinema;

    @FXML
    public void initialize() {
        // 1. Recorte perfecto de esquinas redondeadas en el contenedor principal
        if (heroContainer != null) {
            Rectangle clip = new Rectangle();
            clip.setArcWidth(48.0);
            clip.setArcHeight(48.0);
            clip.widthProperty().bind(heroContainer.widthProperty());
            clip.heightProperty().bind(heroContainer.heightProperty());
            heroContainer.setClip(clip);
        }

        // 2. Fundido suave modificando el canal ALFA del banner de Spiderman
        if (heroImageView != null && heroImageView.getImage() != null) {
            heroImageView.setImage(aplicarDesvanecimientoAlfa(heroImageView.getImage()));
        }
    }

    /**
     * Aplica una transición suave de transparencia alfa a los píxeles de la imagen.
     */
    private Image aplicarDesvanecimientoAlfa(Image originalImage) {
        int width = (int) originalImage.getWidth();
        int height = (int) originalImage.getHeight();

        WritableImage fadedImage = new WritableImage(width, height);
        PixelReader reader = originalImage.getPixelReader();
        PixelWriter writer = fadedImage.getPixelWriter();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Color color = reader.getColor(x, y);

                // Progreso de izquierda a derecha (0.0 = borde izquierdo, 1.0 = borde derecho)
                double factorX = (double) x / width;

                // Transición alfa: De 0% visible a 100% visible a partir del 50% del ancho
                double alpha = Math.pow(factorX, 2.2);

                writer.setColor(x, y, Color.color(color.getRed(), color.getGreen(), color.getBlue(), color.getOpacity() * alpha));
            }
        }
        return fadedImage;
    }

    public void initData(Cinema cinemaCompartido) {
        this.cinema = cinemaCompartido;
        cargarCarteleraDinamica();
    }

    // ==========================================
    // HANDLERS DEL BANNER DESTACADO
    // ==========================================

    @FXML
    private void handleComprarEntradas(ActionEvent event) {
        Node node = (Node) event.getSource();
        if (node != null && node.getScene() != null) {
            Node btnTaquilla = node.getScene().lookup("#btnTaquilla");
            if (btnTaquilla instanceof Button) {
                ((Button) btnTaquilla).fire();
            }
        }
    }

    @FXML
    private void handleVerTrailer(ActionEvent event) {
        try {
            URL videoUrl = getClass().getResource("/uniquindio/edu/co/cinemauq/trailers/hombre_arana.mp4");

            if (videoUrl == null) {
                mostrarAlerta("Archivo no encontrado", "No se encontró el tráiler en /uniquindio/edu/co/cinemauq/trailers/hombre_arana.mp4");
                return;
            }

            Media media = new Media(videoUrl.toExternalForm());
            MediaPlayer mediaPlayer = new MediaPlayer(media);
            MediaView mediaView = new MediaView(mediaPlayer);

            mediaView.setFitWidth(960);
            mediaView.setFitHeight(540);
            mediaView.setPreserveRatio(true);

            StackPane root = new StackPane(mediaView);
            root.setStyle("-fx-background-color: #000000;");

            Stage videoStage = new Stage();
            videoStage.setTitle("Tráiler - El Sorprendente Hombre Araña");
            videoStage.initModality(Modality.APPLICATION_MODAL);

            Node source = (Node) event.getSource();
            if (source != null && source.getScene() != null) {
                videoStage.initOwner(source.getScene().getWindow());
            }

            Scene scene = new Scene(root, 960, 540);
            videoStage.setScene(scene);
            videoStage.setOnCloseRequest(e -> mediaPlayer.stop());

            videoStage.show();
            mediaPlayer.play();

        } catch (Exception e) {
            mostrarAlerta("Error de reproducción", "No se pudo reproducir el video: " + e.getMessage());
        }
    }

    // ==========================================
    // RENDERIZADO DE CARTELERA DIVERSIFICADA
    // ==========================================

    public void cargarCarteleraDinamica() {
        if (containerCartelera == null || cinema == null) return;

        containerCartelera.getChildren().clear();

        for (Pelicula p : cinema.consultarPeliculas()) {
            VBox tarjeta = new VBox();
            tarjeta.setPrefWidth(500.0);
            tarjeta.setSpacing(15.0);
            tarjeta.getStyleClass().add("carousel-card");

            StackPane posterContainer = new StackPane();
            posterContainer.setPrefWidth(500.0);
            posterContainer.setPrefHeight(1050.0);
            posterContainer.setStyle("-fx-background-color: #0b0f19;");

            ImageView imageView = new ImageView();
            imageView.setFitWidth(500.0);
            imageView.setFitHeight(1050.0);
            imageView.setPreserveRatio(true);

            if (p.getImagen() != null && !p.getImagen().isEmpty()) {
                try {
                    String imagePath = p.getImagen().startsWith("file:/")
                            ? p.getImagen()
                            : getClass().getResource("/uniquindio/edu/co/cinemauq/images/" + p.getImagen()).toExternalForm();

                    imageView.setImage(new Image(imagePath, true));
                } catch (Exception e) {
                    System.out.println("No se pudo cargar la imagen para la película: " + p.getNombre());
                }
            }

            Rectangle clip = new Rectangle();
            clip.setArcWidth(24.0);
            clip.setArcHeight(24.0);
            clip.widthProperty().bind(posterContainer.widthProperty());
            clip.heightProperty().bind(posterContainer.heightProperty());
            posterContainer.setClip(clip);

            String colorClasificacion = "#38bdf8";
            if (p.getClasificacion() != null) {
                switch (p.getClasificacion()) {
                    case TODOS: colorClasificacion = "#34d399"; break;
                    case MAYORES_7: colorClasificacion = "#fbbf24"; break;
                    case MAYORES_15: colorClasificacion = "#c084fc"; break;
                    default: colorClasificacion = "#38bdf8"; break;
                }
            }

            Label lblClasificacion = new Label(p.getClasificacion() != null ? p.getClasificacion().toString() : "GENERAL");
            lblClasificacion.setStyle("-fx-text-fill: " + colorClasificacion + "; -fx-font-size: 15px; -fx-font-weight: bold; -fx-background-color: rgba(0,0,0,0.7); -fx-padding: 4 8; -fx-background-radius: 6;");

            StackPane.setAlignment(lblClasificacion, Pos.BOTTOM_LEFT);
            StackPane.setMargin(lblClasificacion, new Insets(20));

            posterContainer.getChildren().addAll(imageView, lblClasificacion);

            Label lblNombre = new Label(p.getNombre());
            lblNombre.setStyle("-fx-text-fill: #ffffff; -fx-font-weight: bold; -fx-font-size: 20px;");

            Label lblDetalle = new Label(p.getGenero() + " • " + p.getDuracion() + " min");
            lblDetalle.setStyle("-fx-text-fill: #94a3b8; -fx-font-size: 15px;");

            tarjeta.getChildren().addAll(posterContainer, lblNombre, lblDetalle);
            containerCartelera.getChildren().add(tarjeta);
        }
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}