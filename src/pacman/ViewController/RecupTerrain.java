package pacman.ViewController;
import java.util.ArrayList;

import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Pagination;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import lib.ListeFichiers;
import pacman.*;

public class RecupTerrain {
	
	//private Terrain terrain;
	private MenuJeu menuJeu;
	ArrayList<String>TabSauvegardes;
	ListeFichiers model_fichiers;
	
	public RecupTerrain(MenuJeu menuJeu)
	{
	//	terrain = new Terrain();
		this.menuJeu=menuJeu;
		model_fichiers= new ListeFichiers();
		TabSauvegardes= new ArrayList<String>();
		TabSauvegardes = model_fichiers.getListeNomImages("images/terrains/sauvegardes/modeles");
	}
	
	public void listeTerrains()
	{
		Image image_fond = new Image("File:images/fond_menu.png");
		BorderPane bP_affichage= new BorderPane();
		Text titre = new Text("Sélectionner un terrain");
		BorderPane.setAlignment(titre, Pos.CENTER);
		titre.setStyle("-fx-font-size: 32px;\r\n" + 
				"   -fx-font-family: \"Arial Black\";\r\n" + 
				"   -fx-fill: #818181;\r\n" + 
				"   -fx-effect: innershadow( three-pass-box , rgba(0,0,0,0.7) , 6, 0.0 , 0 , 2 );\r\n" + 
				"");
		Button bt_retour = new Button("Retour");
		bt_retour.setOnAction(e->menuJeu.VueModeDeJeu());
		VBox vB_haut=new VBox();
		vB_haut.setAlignment(Pos.CENTER);
		vB_haut.getChildren().addAll(titre,bt_retour);
		bP_affichage.setTop(vB_haut);
		
		bP_affichage.setBackground(new Background(new BackgroundImage(image_fond,BackgroundRepeat.REPEAT,
                BackgroundRepeat.REPEAT,
                BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT)));
		
		//Test de la pagination
		int nbPages = (int) Math.ceil(((double)TabSauvegardes.size())/4);
		if(nbPages==0) nbPages=1;
		Pagination root= new Pagination(nbPages, 0);
		root.setPageFactory(index-> {return createPage(index, 4);});
		bP_affichage.setCenter(root);
		
		Scene scene = new Scene(bP_affichage,400,400,Color.LIGHTBLUE); 
		menuJeu.changeVue(scene);
	}
	
	private Node createPage(int pageIndex, int nbPerPage)
	{
		VBox pane= new VBox(10);
		Label lblTitle= new Label("Sauvegardes ("+(pageIndex+1)+")");
		pane.setPadding(new Insets(20));
		pane.setAlignment(Pos.CENTER);
		lblTitle.setFont(Font.font("System", FontWeight.BOLD, 18));
		lblTitle.setTextFill(Color.STEELBLUE);
		pane.getChildren().add(lblTitle);
		int offset= pageIndex* nbPerPage;
		for(int i=offset; i<(offset+nbPerPage); i++) 
		{
			if(i<TabSauvegardes.size())
			{
				//Affichage des differents boutons + images associées aux sauvegardes
				Tooltip tool = new Tooltip();
				Image img_sauvegarde= new Image("File:images/terrains/sauvegardes/rendus/"+TabSauvegardes.get(i));
				tool.setGraphic(new ImageView(img_sauvegarde));
				Button bt_sauvegarde = new Button(TabSauvegardes.get(i).substring(0,TabSauvegardes.get(i).length()-4));
				//bt_sauvegarde.setTooltip(tool);
				bt_sauvegarde.setOnMouseEntered(new EventHandler<MouseEvent>(){
				      
				      public void handle(MouseEvent event) {
			    	  Point2D p = bt_sauvegarde.localToScreen(bt_sauvegarde.getLayoutBounds().getMaxX(), bt_sauvegarde.getLayoutBounds().getMaxY());
			          tool.show(bt_sauvegarde, p.getX(), p.getY());
				      }
				});
				
				bt_sauvegarde.setOnMouseExited(e->tool.hide());
				final int t=i;
				bt_sauvegarde.setOnMouseClicked(e->new WindowGraphic(menuJeu,"images/terrains/sauvegardes/modeles/"+TabSauvegardes.get(t)).jouer());
				pane.getChildren().add(bt_sauvegarde);
			}
		} 
		return pane;
	}
}
