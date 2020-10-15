/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package screensframework;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javax.swing.JOptionPane;

/**
 * FXML Controller class
 *
 * @author Wil
 */
public class ContenidoController implements Initializable, ControlledScreen {
    
    ScreensController controlador;
    
    private ControlesBasicos controlesBasicos = new ControlesBasicos();
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @Override
    public void setScreenParent(ScreensController pantallaPadre) {
        controlador = pantallaPadre; 
    }
    
    @FXML
    private void irMantenimientoProducto(ActionEvent  event) {
       
       controlador.setScreen(ScreensFramework.mantenimientoProductoID);
    }
    
    @FXML
    private void salir(ActionEvent event) {
        this.controlesBasicos.salirSistema();
    }
    
    @FXML
    private void cerrarSesion(ActionEvent event) {
        controlador.setScreen(ScreensFramework.loginID);
    }
}
