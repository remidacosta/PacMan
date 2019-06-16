package pacman.ViewController;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.media.AudioClip;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import pacman.ChoixTerrain;
import javafx.application.Application;

public class MenuJeu extends Application
{
	private Stage fenetre;
	private Theme theme;
		
	public void start(Stage primaryStage)
	{
		this.fenetre=primaryStage;
		primaryStage.setTitle("Pacman");
		primaryStage.getIcons().add(new Image("file:images/icon2.png"));
		theme=Theme.pacman;
		VueMenu();
		primaryStage.show();
	}
	
	public void VueMenu()
	{
		Scene vueMenu;
		VBox menu=new VBox(5);
		Text titre = new Text("Jeu Pacman");
		if(theme!=Theme.pacman)
		{
			titre.setText("Jeu Pac"+theme);
		}
		Image image = new Image("File:images/fond_menu.png");
		Button bt_jouer = new Button("Jouer");
		bt_jouer.setOnAction(e->VueModeDeJeu());
		Button bt_editer = new Button("Editeur");
		bt_editer.setOnAction(e->new Editeur(this).editer());
		Button bt_quitter = new Button("Quitter");
		bt_quitter.setOnAction(e->fenetre.close());
		Button bt_options = new Button("Options");
		bt_options.setOnAction(e->VueOptions());
		//feuille de style à faire...
		titre.setStyle("-fx-font-size: 32px;\r\n" + 
				"   -fx-font-family: \"Arial Black\";\r\n" + 
				"   -fx-fill: #818181;\r\n" + 
				"   -fx-effect: innershadow( three-pass-box , rgba(0,0,0,0.7) , 6, 0.0 , 0 , 2 );\r\n" + 
				"");
		//ajout des différents boutons 
		menu.setAlignment(Pos.CENTER);
		menu.getChildren().add(bt_jouer);
		menu.getChildren().add(bt_editer);
		menu.getChildren().add(bt_options);
		menu.getChildren().add(bt_quitter);
		
		//affichage de la view
		BorderPane border = new BorderPane();
		border.setTop(titre);
		BorderPane.setAlignment(titre, Pos.CENTER);
		border.setCenter(menu);
		border.setBackground(new Background(new BackgroundImage(image,BackgroundRepeat.REPEAT,
                BackgroundRepeat.REPEAT,
                BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT)));
		border.setBottom(new Text("By: Nassère Abdou-Rahimi & Rémi Da Costa"));
		vueMenu= new Scene(border,400,400);
		fenetre.setScene(vueMenu);
	}
	
	public void VueModeDeJeu()
	{
		Scene vueModeDeJeu;
		VBox menu=new VBox(5);
		Text titre = new Text("Choix du jeu");
		Image image = new Image("File:images/fond_menu.png");
		Button bt_jeuSimple = new Button("Partie classique");
		bt_jeuSimple.setOnAction(e->new WindowGraphic(this,ChoixTerrain.classique1joueur).jouer());
		Button bt_accueil = new Button("Retour à l'accueil");	
		bt_accueil.setOnAction(e->VueMenu());
		Button bt_aleatoire = new Button("Partie arcade");
		bt_aleatoire.setOnAction(e->new WindowGraphic(this,ChoixTerrain.aleatoire1joueur).jouer());
		Button bt_recupSauv = new Button("Charger un terrain");
		bt_recupSauv.setOnAction(e->new RecupTerrain(this).listeTerrains());
		//feuille de style à faire...
		titre.setStyle("-fx-font-size: 32px;\r\n" + 
				"   -fx-font-family: \"Arial Black\";\r\n" + 
				"   -fx-fill: #818181;\r\n" + 
				"   -fx-effect: innershadow( three-pass-box , rgba(0,0,0,0.7) , 6, 0.0 , 0 , 2 );\r\n" + 
				"");
		menu.setAlignment(Pos.CENTER);
		menu.getChildren().add(bt_jeuSimple);
		menu.getChildren().add(bt_aleatoire);
		menu.getChildren().add(bt_recupSauv);
		menu.getChildren().add(bt_accueil);
		
		BorderPane border = new BorderPane();
		border.setTop(titre);
		BorderPane.setAlignment(titre, Pos.CENTER);
		border.setCenter(menu);
		border.setBackground(new Background(new BackgroundImage(image,BackgroundRepeat.REPEAT,
                BackgroundRepeat.REPEAT,
                BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT)));
		
		vueModeDeJeu= new Scene(border,400,400);
		fenetre.setScene(vueModeDeJeu);
	}
	
	public void VueOptions()
	{
		Scene vueMenu;
		VBox menu=new VBox(5);
		Text titre = new Text("Options");
		Image image = new Image("File:images/fond_menu.png");
		Button bt_pacman = new Button("Pacman");
		bt_pacman.setOnAction(e->{ theme=Theme.pacman; VueMenu();} );
		Button bt_mario = new Button("Mario");
		bt_mario.setOnAction(e->{ theme=Theme.mario; VueMenu();});
		Button bt_zelda = new Button("Zelda");
		bt_zelda.setOnAction(e->{ theme=Theme.zelda; VueMenu();});
		Button bt_retour = new Button("Retour à l'accueil");
		bt_retour.setOnAction(e->VueMenu());
		titre.setStyle("-fx-font-size: 32px;\r\n" + 
				"   -fx-font-family: \"Arial Black\";\r\n" + 
				"   -fx-fill: #818181;\r\n" + 
				"   -fx-effect: innershadow( three-pass-box , rgba(0,0,0,0.7) , 6, 0.0 , 0 , 2 );\r\n" + 
				"");
		//ajout des différents boutons 
		menu.setAlignment(Pos.CENTER);
		Label lb_theme = new Label("Choix du theme:");
		lb_theme.setFont(Font.font("System", FontWeight.BOLD, 18));
		lb_theme.setTextFill(Color.STEELBLUE);
		menu.getChildren().add(lb_theme);
		menu.getChildren().addAll(bt_pacman,bt_mario,bt_zelda);
		menu.getChildren().add(bt_retour);
		
		//affichage de la view
		BorderPane border = new BorderPane();
		border.setTop(titre);
		BorderPane.setAlignment(titre, Pos.CENTER);
		border.setCenter(menu);
		border.setBackground(new Background(new BackgroundImage(image,BackgroundRepeat.REPEAT,
                BackgroundRepeat.REPEAT,
                BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT)));
		vueMenu= new Scene(border,400,400);
		fenetre.setScene(vueMenu);
	}
	
	public static void main(String[] args) 
	{
        launch(args);
    }	
	
	public void changeVue(Scene vue)
	{
		fenetre.setScene(vue);
	}
	
	public Theme getTheme()
	{
		return theme;
	}
}
