package pacman.ViewController;

import java.util.Observable;
import java.util.Observer;
import java.util.Optional;

import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import lib.Position;
import pacman.Chemin;
import pacman.Pastille;
import pacman.SuperPastille;
import pacman.TerrainEditable;

public class Editeur 
{
	private MenuJeu menuJeu;
	private TerrainEditable terrain;
	
	public Editeur(MenuJeu menuJeu)
	{
		terrain = new TerrainEditable();
		this.menuJeu=menuJeu;
	}
	
	public void editer()
	{
		Theme theme = menuJeu.getTheme();
		final int TAILLE_SPRITE=20;
		Image img_FantomeBleu = new Image("File:images/themes/"+theme+"/personnages/fantomebleu.png");
		Image img_Teleportation = new Image("File:images/themes/"+theme+"/environnement/teleportation.png");
		Image img_Pacman1 = new Image("File:images/themes/"+theme+"/personnages/pacman_1.png");		// image 1 du pacman 
		Image img_Pacman2 = new Image("File:images/themes/"+theme+"/personnages/pacman_2.png");		// image 2 du pacman
		Image img_FantomeRouge = new Image("File:images/themes/"+theme+"/personnages/fantomerouge.png");
		Image img_Pastille = new Image("File:images/themes/"+theme+"/pastilles/pastille.png");
		Image img_SuperPastille = new Image("File:images/themes/"+theme+"/pastilles/superpastille.png");
		Image img_Mur= new Image("File:images/themes/"+theme+"/environnement/mur.png");
		Image img_Chemin= new Image("File:images/themes/"+theme+"/environnement/chemin.png");
		Image img_Fuir= new Image("File:images/themes/"+theme+"/personnages/fuir.png");
        ImageView tab[][] = new ImageView[terrain.getTerrain().getDimX()][terrain.getTerrain().getDimY()];
        // Affichage de la page

        // gestion du placement (permet de placer le champ Text affichage en haut, et GridPane gPane au centre)
        BorderPane bP_fenetre = new BorderPane();
        // permet de placer les diffrents boutons dans une grille
        GridPane gP_terrain = new GridPane();
 		String couleur_fond;
        switch(theme)
 		{
 			case pacman:  couleur_fond="black";
 			break;
 			case mario: couleur_fond="#31ac39";
 			break;
 			case zelda: couleur_fond="#e8931f"; // a modif
 			break;
 			default:
 				couleur_fond="black";
 		}
        gP_terrain.setStyle("-fx-background-color: "+couleur_fond);
        //	gPane.setGridLinesVisible(true);
        bP_fenetre.setCenter(gP_terrain);
        gP_terrain.setAlignment(Pos.CENTER);
        HBox barre = new HBox();
        Button bt_quitter = new Button("Quitter sans sauvegarder");
        Button bt_sauvegarde =new Button("Sauvegarder");
        TextInputDialog inDialog= new TextInputDialog("Nouveau terrain");
        inDialog.setTitle("Sauvegarde");
        inDialog.setHeaderText("Veuillez saisir un nom de sauvegarde");
        inDialog.setContentText("Nom de sauvegarde:");
        
        bt_sauvegarde.setOnAction(ae->{  
        	Optional<String> textIn= inDialog.showAndWait();
        	if(textIn.isPresent())
        	{
        		terrain.setNomFichier(textIn.get());
        		terrain.sauvegarder();
        	}
        	menuJeu.VueMenu();
        	
        	} );

        bt_quitter.setOnAction(e->menuJeu.VueMenu());
        barre.getChildren().addAll(bt_sauvegarde,bt_quitter); //A modif
        barre.setAlignment(Pos.CENTER);
        bP_fenetre.setBottom(barre);
        StackPane contenu = new StackPane();
        contenu.getChildren().addAll(bP_fenetre);
        VBox page = new VBox ();
        page.getChildren().addAll(contenu);
        page.setStyle("-fx-background-color: LIGHTBLUE");

        Scene scene = new Scene(page, Color.LIGHTBLUE);       
        
   		//Affichage initial du terrain
   		for (int i=0;i<terrain.getTerrain().getDimX();i++) 
   		{
   			for(int j=0;j<terrain.getTerrain().getDimY();j++)
   			{
   				switch(terrain.getTerrain().getCase(new Position(i,j)).getClass().getSimpleName())
   				{
   				case "Chemin":
   					Chemin tmp = (Chemin)terrain.getTerrain().getCase(new Position(i,j));
   					if(tmp.getObjet() instanceof SuperPastille)
   					{
   						tab[i][j]= new ImageView(img_SuperPastille);
   					}
   					else if(tmp.getObjet() instanceof Pastille)
   					{
   						tab[i][j]= new ImageView(img_Pastille);
   					}
   					else
   					{														
   						tab[i][j]= new ImageView(img_Chemin);
   					}
   					break;
   				case "Mur":
   					tab[i][j]= new ImageView(img_Mur);
   					break;
   				case "Teleporteur":
   					tab[i][j]= new ImageView(img_Teleportation);
   					break;
   				default:
   					tab[i][j]= new ImageView(img_Chemin);
   					break;
   				}
   				BorderPane p = new BorderPane();
   				p.setCenter(tab[i][j]);
   				final int x=i;
   				final int y=j;
   				//controlleur, modification du model
   				p.setOnMouseClicked(new EventHandler<MouseEvent>() {
   	               
   	               public void handle(MouseEvent event) {
   	        	   	
   	        	   		if(event.getButton()==MouseButton.PRIMARY)
   		        		{
   		        			 terrain.setType(x,y); 
   		        		}
   		        		else if(event.getButton()==MouseButton.SECONDARY)
   		        		{
   		        			terrain.setPastille(x, y);
   		        		}   	            	   
   	               }
   	           });
   				gP_terrain.add(p,j,i);
   			}
   		}
   		   		
   		//Update de la vue
        
        terrain.addObserver(new Observer() {
			public void update(Observable o, Object arg) {
				Position derniere = terrain.getDerniere();
				int i=derniere.getX();
				int j=derniere.getY();
				switch(terrain.getTerrain().getCase(new Position(i,j)).getClass().getSimpleName())
   				{
   				case "Chemin":
   					Chemin tmp = (Chemin)terrain.getTerrain().getCase(new Position(i,j));
   					if(tmp.getObjet() instanceof SuperPastille)
   					{
   						tab[i][j].setImage(img_SuperPastille);
   					}
   					else if(tmp.getObjet() instanceof Pastille)
   					{
   						tab[i][j].setImage(img_Pastille);
   					}
   					else
   					{														
   						tab[i][j].setImage(img_Chemin);
   					}
   					break;
   				case "Mur":
   					tab[i][j].setImage(img_Mur);
   					break;
   				case "Teleporteur":
   					tab[i][j].setImage(img_Teleportation);
   					break;
   				default:
   					tab[i][j].setImage(img_Chemin);
   					break;
   				}
			}
			
        });
   		
   		
   		menuJeu.changeVue(scene);
	}
}
