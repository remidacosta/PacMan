package pacman.ViewController;

import java.util.Observable;
import java.util.Observer;

import javafx.animation.Animation;
import javafx.animation.Animation.Status;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.MenuBar;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.AudioClip;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;
import lib.Case;
import lib.Direction;
import lib.Entite;
import lib.Position;
import pacman.*;

public class WindowGraphic
{
	private Partie jeu;
	private Text affichage;
	private MenuJeu menuJeu;
	int Decompte = 5;
	
	public WindowGraphic(MenuJeu menu,ChoixTerrain choix)
	{
		this.menuJeu=menu;
		jeu = new Partie(choix);
	}
	
	public WindowGraphic(MenuJeu menu,String chemin)
	{
		this.menuJeu=menu;
		jeu = new Partie(chemin);
	}
	
	public void jouer() 
	{
		Theme theme = menuJeu.getTheme();
		//Chargement des images
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
		Image img_Finpredateur= new Image("File:images/themes/"+theme+"/personnages/finpredateur.png");

        ImageView tab[][] = new ImageView[jeu.getTerrain().getDimX()][jeu.getTerrain().getDimY()];
		
        // Sons
		AudioClip intro = new AudioClip("file:data/intro.wav");
		
        // Thread du jeu
		Thread partie=new Thread (jeu);
		
		 // Gestion du temps 
			//Genere un pouvoir (envoie une info au controleur qui modifie le modele)
        Timeline time_objet = new Timeline(new KeyFrame(Duration.seconds(20),
				ae-> jeu.getTerrain().genererPouvoir()));
		time_objet.setCycleCount(Timeline.INDEFINITE);
			//Gestion du délai du mode prédateur (envoie une info au controleur qui modifie le modele)
        Timeline time_predateur = new Timeline(new KeyFrame(Duration.seconds(10),
				ae-> jeu.getTerrain().finPredateur()));
		time_predateur.setCycleCount(1);
			//Decompte du lancement de la partie 
		Timeline time_decompte = new Timeline();
		time_decompte.setCycleCount(Timeline.INDEFINITE);
		time_decompte.getKeyFrames().add(new KeyFrame(Duration.seconds(1),
				e-> { 
					Decompte--; 
					affichage.setText(Integer.toString(Decompte));
					if (Decompte <= 0) 
					{
						time_decompte.stop();
						affichage.setVisible(false);
						partie.setDaemon(true);
						partie.start();
						time_objet.play();
					}
				}));
		
		// Barre de menu
		MenuBar menuBar = new MenuBar();
        Menu accueil = new Menu("Accueil");
        MenuItem retour = new MenuItem("Retour à l'accueil");
        retour.setOnAction(e->{	
        	intro.stop();
        	time_decompte.stop();
        	time_objet.stop();
        	jeu.stop(); menuJeu.VueMenu();
        });
        
        MenuItem nouvellepartie = new MenuItem("Nouvelle partie");
        nouvellepartie.setOnAction(e->{
        	intro.stop();
    		time_decompte.stop();
    		time_objet.stop();
    		jeu.stop();
        	if(jeu.getChoixTerrain()!=ChoixTerrain.sauvegarde)
        	new WindowGraphic(menuJeu,jeu.getChoixTerrain()).jouer();
        	else
        		new WindowGraphic(menuJeu,jeu.getTerrain().getChemin()).jouer();
        	
        });
 		HBox hb_score = new HBox();
        MenuItem pause = new MenuItem("Pause");
        pause.setOnAction(e->{	
        	if(!jeu.getPause())
	        {	
	        	time_objet.pause();
	        	time_predateur.pause();
	        	jeu.pause();
	        	affichage.setText("PAUSE !");
	        	affichage.setVisible(true);
	        	hb_score.setVisible(false);
        	}
        	else
        	{
	        	time_objet.play();
	        	time_predateur.play();
        		jeu.pause();
        		affichage.setVisible(false);
	        	hb_score.setVisible(true);
        	}
        });
        accueil.getItems().addAll(retour, nouvellepartie,pause);
      //  Menu pause = new Menu("Pause");
        Menu aide = new Menu("Aide");
        menuBar.getMenus().addAll(accueil, aide);
        
        // Affichage de la page
        
     		BorderPane bP_fenetre = new BorderPane();
     		GridPane gP_terrain = new GridPane();
     		affichage = new Text(Integer.toString(Decompte));
     		affichage.setFont(Font.font ("Verdana", 20));
     		affichage.setFill(Color.RED);
     		affichage.setTextAlignment(TextAlignment.CENTER);
     		Text nb_score = new Text("");
     		nb_score.setFont(Font.font ("Verdana", 20));
     		nb_score.setFill(Color.RED);
     		hb_score.getChildren().add(nb_score);
     		hb_score.setAlignment(Pos.CENTER);
     		HBox hb_vie = new HBox();
     		int nb_vie = ((Perso)jeu.getTerrain().getEntites().get(0)).getVie();
     		Text txt_vie = new Text(Integer.toString(nb_vie));
     		for(int i=0;i<nb_vie;i++)
     		{
     			hb_vie.getChildren().add(new ImageView(img_Pacman1));
     		}
     		hb_score.getChildren().add(hb_vie);
     		hb_vie.setVisible(false);
     		StackPane sp_haut = new StackPane();
     		sp_haut.getChildren().addAll(affichage,hb_score);
     		sp_haut.setAlignment(Pos.CENTER);
     		bP_fenetre.setTop(sp_haut);
     		BorderPane.setAlignment(affichage,Pos.CENTER);
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
    		HBox hB_pouvoir= new HBox(2);
    		String pouvoir_actif="Pouvoir activé !		[Espace] pour le désactiver !";
    		String pouvoir_desact="Appuyez sur [Espace] pour activer votre pouvoir !";
    		String pouvoir_OFF="Vous n'avez aucun pouvoir !";
    		Text txt_pouvoir = new Text (pouvoir_OFF);
    		Image img_pouvoir = new Image("File:images/themes/"+theme+"/objets/objet.png");
    		Image img_pouvoir_passemur = new Image("File:images/themes/pacman/objets/passemur.png");
    		Image img_pouvoir_portalgun = new Image("File:images/themes/pacman/objets/portalgun.png");
    		ImageView iV_pouvoir = new ImageView(img_pouvoir);
    		hB_pouvoir.getChildren().addAll(iV_pouvoir,txt_pouvoir);
    		bP_fenetre.setBottom(hB_pouvoir);
    		
    		VBox vB_FinPartie=new VBox(5);
			vB_FinPartie.setStyle("-fx-background-color: grey; -fx-background-radius: 10 10 10 10 ;");
			vB_FinPartie.getChildren().add(new Text("Fin de jeu"));
			Button bt_rejouer = new Button ("Nouvelle Partie");
			Button bt_accueil = new Button ("Menu principal");
			bt_rejouer.setOnAction(e->{
				if(!(jeu.getChoixTerrain()==ChoixTerrain.sauvegarde))
				new WindowGraphic(menuJeu,jeu.getChoixTerrain()).jouer();
				else new WindowGraphic(menuJeu,jeu.getTerrain().getChemin()).jouer();
					});
			bt_accueil.setOnAction(e->menuJeu.VueMenu());
			vB_FinPartie.getChildren().add(bt_rejouer);
			vB_FinPartie.getChildren().add(bt_accueil);
			vB_FinPartie.setAlignment(Pos.CENTER);
			vB_FinPartie.setMaxWidth(200);
			vB_FinPartie.setMaxHeight(200);
			vB_FinPartie.setVisible(false);
			
    		StackPane contenu = new StackPane();
    		contenu.getChildren().addAll(bP_fenetre,vB_FinPartie);
    		VBox page = new VBox ();
    		page.getChildren().addAll(menuBar,contenu);
    		page.setStyle("-fx-background-color: LIGHTBLUE");
    		menuBar.setStyle("-fx-background-color: lightgrey");
    		    		
    		Scene scene = new Scene(page, Color.LIGHTBLUE);
		
		//Affichage initial du terrain
		for (int i=0;i<jeu.getTerrain().getDimX();i++) 
		{
			for(int j=0;j<jeu.getTerrain().getDimY();j++)
			{
				switch(jeu.getTerrain().getCase(new Position(i,j)).getClass().getSimpleName())
				{
				case "Chemin":
					Chemin tmp = (Chemin)jeu.getTerrain().getCase(new Position(i,j));
					if(tmp.getObjet() instanceof SuperPastille)
					{
						tab[i][j]= new ImageView(img_SuperPastille);
					}
					else if(tmp.getObjet() instanceof Pastille)
					{
						tab[i][j]= new ImageView(img_Pastille);
					}
					else if(tmp.getObjet() instanceof Pouvoir)
					{
						tab[i][j]= new ImageView(img_pouvoir);
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
				gP_terrain.add(tab[i][j],j,i);
			}
		}
		
		//Affichage initial des personnages
		for(Entite personnage: jeu.getTerrain().getEntites() )
		{

			int x=personnage.getPosPerso().getX();
			int y=personnage.getPosPerso().getY();	
			switch(personnage.getClass().getSimpleName())
			{
			case "FantomeRouge":
				tab[x][y].setImage(img_FantomeRouge);
				break;
			case "FantomeBleu":
				tab[x][y].setImage(img_FantomeBleu);
				break;
			case "Pacman":
				if(((Pacman)personnage).compt.getValeur() == 1)
				{
					tab[x][y].setImage(img_Pacman1);
				}
				else if(((Pacman)personnage).compt.getValeur() == 2)
				{
					tab[x][y].setImage(img_Pacman2);
				}
				break;
			} 
		}	
		// mise à jour de l'affichage d'un objet

		jeu.getTerrain().addObserver(new Observer() {
			
			public void update(Observable o, Object arg) {
				Case c =jeu.getTerrain().getCaseChange();
				if(c instanceof Teleporteur)
				{
					tab[c.getPosition().getX()][c.getPosition().getY()].setImage(img_Teleportation);
					tab[c.getPosition().getX()][c.getPosition().getY()].setRotate(0);
				}
				if(c instanceof Chemin)
				{
					if(((Chemin)c).getObjet() instanceof Pouvoir)
					{
						tab[c.getPosition().getX()][c.getPosition().getY()].setImage(img_pouvoir);
						tab[c.getPosition().getX()][c.getPosition().getY()].setRotate(0); 
					}
				}
				
			}
		});
		
		// mise à jour de l'affichage des personnages

		jeu.addObserver(new Observer() {

				public void update(Observable o, Object arg) {
					if(!hb_vie.isVisible()) hb_vie.setVisible(true);
					//affichage de la fin de partie
					if(jeu.partiePerdue())
					{
						menuBar.setDisable(true);
						hb_score.setVisible(false);
						vB_FinPartie.setVisible(true);
						intro.stop();
			    		time_decompte.stop();
			    		time_objet.stop();
						jeu.stop();
					}
					// Fin du niveau
					if(jeu.partieGagnee())
					{
						jeu.niveauSuivant();
						/*
						 switch à mettre dans une fonction (pour éviter la duplication du code),
						 implique que les images soient des variables de la classe
						 */
						for (int i=0;i<jeu.getTerrain().getDimX();i++) 
						{
							for(int j=0;j<jeu.getTerrain().getDimY();j++)
							{
								switch(jeu.getTerrain().getCase(new Position(i,j)).getClass().getSimpleName())
								{
								case "Chemin":
									Chemin tmp = (Chemin)jeu.getTerrain().getCase(new Position(i,j));
									if(tmp.getObjet() instanceof SuperPastille)
									{
										tab[i][j].setImage(img_SuperPastille);
										tab[i][j].setRotate(0);
									}
									else if(tmp.getObjet() instanceof Pastille)
									{
										tab[i][j].setImage(img_Pastille);
										tab[i][j].setRotate(0);
									}
									else if(tmp.getObjet() instanceof Pouvoir)
									{
										tab[i][j].setImage(img_pouvoir);
										tab[i][j].setRotate(0);
									}
									else
									{														
										tab[i][j].setImage(img_Chemin);
										tab[i][j].setRotate(0);
									}
									break;
								case "Mur":
									tab[i][j].setImage(img_Mur);
									tab[i][j].setRotate(0);
									break;
								case "Teleporteur":
									tab[i][j].setImage(img_Teleportation);
									tab[i][j].setRotate(0);
									break;
								default:
									tab[i][j].setImage(img_Chemin);
									tab[i][j].setRotate(0);
									break;
								}
								
							}
							try {
								//partie.suspend();
								Thread.sleep(100);
								//partie.resume();
							}
							catch(Exception e)
							{
								e.printStackTrace();
							}
						}
						// Ajout de l'observeur, car le terrain n'a pas supprimé et non pas vidé 
						jeu.getTerrain().addObserver(new Observer() {
							public void update(Observable o, Object arg) {
								Case c =jeu.getTerrain().getCaseChange();
								if(c instanceof Teleporteur)
								{
									tab[c.getPosition().getX()][c.getPosition().getY()].setImage(img_Teleportation);
									tab[c.getPosition().getX()][c.getPosition().getY()].setRotate(0);
								}
								if(c instanceof Chemin)
								{
									if(((Chemin)c).getObjet() instanceof Pouvoir)
									{
										tab[c.getPosition().getX()][c.getPosition().getY()].setImage(img_pouvoir);
										tab[c.getPosition().getX()][c.getPosition().getY()].setRotate(0); 
									}
								}
							}
						});
					}
					// Remet à zero le compteur de time_predateur
					if(((Perso)jeu.getTerrain().getEntites().get(0)).getSiPredateur())
					{
						time_predateur.playFromStart();
					}
					// Met à jour l'affichage du pouvoir en bas de la fenetre
					if(((Pacman)(jeu.getTerrain().getEntites().get(0))).getPouvoir()!=null)
					{
						if(((Pacman)(jeu.getTerrain().getEntites().get(0))).getPouvoir().getActif())
						{
							txt_pouvoir.setText(pouvoir_actif);
						}
						else txt_pouvoir.setText(pouvoir_desact);
						
						if(((Pacman)(jeu.getTerrain().getEntites().get(0))).getPouvoir() instanceof PasseMur)
						{
							iV_pouvoir.setImage(img_pouvoir_passemur);
						}
						if(((Pacman)(jeu.getTerrain().getEntites().get(0))).getPouvoir() instanceof PortalGun)
						{
							iV_pouvoir.setImage(img_pouvoir_portalgun);
						}
					} 
					else 
					{
						txt_pouvoir.setText(pouvoir_OFF);
						iV_pouvoir.setImage(img_pouvoir);
					}
					
					//mise à jour de l'affichage du score et des vies
						int a=((Pacman)(jeu.getTerrain().getEntites().get(0))).getScore(); 
						int v=((Pacman)(jeu.getTerrain().getEntites().get(0))).getVie(); 
						
						nb_score.setText("Score : " +Integer.toString(a)+" Vie : ");

					if(!txt_vie.getText().equals(Integer.toString(v)))
					{
						hb_vie.getChildren().get(v).setVisible(false);
						txt_vie.setText(Integer.toString(v));
					}
					
					/*
					 switch à mettre dans une fonction (pour éviter la duplication du code),
					 implique que les images soient des variables de la classe
					 */
					
					for(Entite p: jeu.getTerrain().getEntites())
					{
						// mise à jour de l'affichage de l'ancienne position du personnage
						Position anciennePosition=p.getAnciennePosPerso();
						int x=anciennePosition.getX();
						int y=anciennePosition.getY();
						switch(jeu.getTerrain().getCase(anciennePosition).getClass().getSimpleName())
						{
						case "Chemin":
							Chemin tmp = (Chemin)jeu.getTerrain().getCase(anciennePosition);
							if(tmp.getObjet() instanceof SuperPastille)
							{
								tab[x][y].setImage(img_SuperPastille);
							}
							else if(tmp.getObjet() instanceof Pastille)
							{
								tab[x][y].setImage(img_Pastille);
							}
							else if(tmp.getObjet() instanceof Pouvoir)
							{
								tab[x][y].setImage(img_pouvoir);
							}
							else
							{
								tab[x][y].setImage(img_Chemin);
							}

							break;
						case "Mur":
							tab[x][y].setImage(img_Mur);
							break;
						case "Teleporteur":
							tab[x][y].setImage(img_Teleportation);
							break;
						default:
							tab[x][y].setImage(img_Chemin);		
							break;
						}	
					}
					
					for(Entite p: jeu.getTerrain().getEntites())
					{
						// mise à jour de l'affichage de la position actuelle du personnage	
						int x=p.getPosPerso().getX();
						int y=p.getPosPerso().getY();
						switch(p.getClass().getSimpleName())
						{
						case "FantomeRouge":
							if(((Perso)p).getSiPredateur())	tab[x][y].setImage(img_FantomeRouge);
							else 
							{	
								tab[x][y].setImage(img_Fuir);
								if(time_predateur.getStatus().equals(Status.RUNNING)
								&&time_predateur.getCurrentTime().greaterThanOrEqualTo(Duration.seconds(6))
								&&(((int)time_predateur.getCurrentTime().toMillis())/200)%2==0
								&&!tab[x][y].getImage().equals(img_Finpredateur))
								{
									tab[x][y].setImage(img_Finpredateur);
								}
							}
							tab[x][y].setRotate(0);
							break;
						case "FantomeBleu":
							if(((Perso)p).getSiPredateur())	tab[x][y].setImage(img_FantomeBleu);
							else 
							{	
								tab[x][y].setImage(img_Fuir);
								if(time_predateur.getStatus().equals(Status.RUNNING)
								&&time_predateur.getCurrentTime().greaterThanOrEqualTo(Duration.seconds(6))
								&&(((int)time_predateur.getCurrentTime().toMillis())/200)%2==0
								&&!tab[x][y].getImage().equals(img_Finpredateur))
								{
									tab[x][y].setImage(img_Finpredateur);
								}
							}
							tab[x][y].setRotate(0);
							break;
						case "Pacman":
							if(((Pacman)p).compt.getValeur() == 1)
							{
								tab[x][y].setImage(img_Pacman1);
							}
							else if(((Pacman)p).compt.getValeur() == 2)
							{
								tab[x][y].setImage(img_Pacman2);
							}
							if(p.getDirection()==Direction.bas) tab[x][y].setRotate(90); // a modifier
							if(p.getDirection()==Direction.gauche)
							{
								if(theme==Theme.pacman)
								{
									tab[x][y].setRotate(180);
								}
								else tab[x][y].setRotate(0);
							}
							if(p.getDirection()==Direction.haut) tab[x][y].setRotate(270);
							if(p.getDirection()==Direction.droite) tab[x][y].setRotate(0); 
							break;
						default:
							break;
						}
					}
				} 
			});
		
		//Controleur des actions du clavier pour le pacman
		scene.setOnKeyPressed(new EventHandler<KeyEvent> (){

			public void handle(KeyEvent toucheclavier){
				if(!jeu.getPause())
				{
					// mise à jour l'affichage du score  
					if(toucheclavier.getCode()==KeyCode.UP ||toucheclavier.getCode()==KeyCode.DOWN||
							toucheclavier.getCode()==KeyCode.RIGHT||toucheclavier.getCode()==KeyCode.LEFT )
					{
					/*	if(!gPane.getChildren().contains(iv1))
						gPane.getChildren().remove(iv1); */
						Direction suivante;
						switch(toucheclavier.getCode())
						{
						case UP:
							suivante=Direction.haut;
							break;
						case DOWN:
							suivante=Direction.bas;
							break;
						case RIGHT:
							suivante=Direction.droite;
							break;
						case LEFT:
							suivante=Direction.gauche;
							break;
							default:
							suivante=((Pacman)(jeu.getTerrain().getEntites().get(0))).getDirection();
							break;
						}
						jeu.ActionPacman(suivante);
					}
					
					if(toucheclavier.getCode()==KeyCode.SPACE)
					{
						jeu.ActionPouvoir();
					}
				}
			}
		});
		
		menuJeu.changeVue(scene);
		intro.play();
		time_decompte.play();
	}
	
}
