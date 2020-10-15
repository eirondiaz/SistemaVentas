package screensframework;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javax.swing.JOptionPane;
import screensframework.DBConnect.DBConnection;
import org.apache.commons.codec.digest.DigestUtils;

public class RegistroController implements Initializable, ControlledScreen {
    
    ScreensController controlador;
    private Validaciones validation = new Validaciones();
    private ControlesBasicos controlesBasicos = new ControlesBasicos();
    public TextField tfAddNombre;
    public TextField tfAddApellido;
    public TextField tfAddCorreo;
    public TextField tfAddUser;
    public TextField tfAddPass;
    public PasswordField tfConfirmar;
    public ComboBox cbAddsex;
    private Connection conexion;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ObservableList<String> options = FXCollections.observableArrayList(
                "Hombre",
                "Mujer"
                );
        cbAddsex.setItems(options);
        
        // Escuchador para comprobar si pierdo el foco
        tfAddUser.focusedProperty().addListener(new ChangeListener<Boolean>() {
                @Override
                public void changed(ObservableValue<? extends Boolean> arg0, Boolean arg1, Boolean arg2) {
                //Controlamos cuando tf pierde el foco
                    if(!arg2){
                        if (!tfAddUser.getText().equals("")) {
                            try {
                                
                                conexion = DBConnection.connect();
                                
                                String sql = "SELECT usuario FROM "
                                        + "usuarios WHERE "
                                        + "usuario = '"+tfAddUser.getText()+"'";
                                
                                ResultSet resultado = conexion.createStatement().executeQuery(sql);
                                
                                boolean existeUsuario = resultado.next();

                                if (existeUsuario) {
                                    JOptionPane.showMessageDialog(null, "Error! ya existe el usuario "+tfAddUser.getText());
                                    tfAddUser.setStyle("-fx-border-color: #B80000;");
                                    return;
                                }

                                tfAddUser.setStyle("-fx-border-color: #3399CC;");
                                resultado.close();
                                
                            } catch (Exception e) {
                                System.out.println("error de conexion "+e);
                            }
                        }
                    }
                }
         });
    }    

    @Override
    public void setScreenParent(ScreensController pantallaPadre) {
        controlador = pantallaPadre;
    }
    
    @FXML
    private void registroUsuario(ActionEvent event){
        
        //______________________________________________________________
        // VALIDACIONES
        if (!validation.validarVacios(tfAddUser.getText(), "USUARIO")) {
            return;
        }
        
        if (!validation.validarVacios(tfAddNombre.getText(), "NOMBRE")) {
            return;
        }
        
        if (!validation.validarVacios(tfAddApellido.getText(), "APELLIDO")) {
            return;
        }
        
        if (cbAddsex.getValue() == null) {
            JOptionPane.showMessageDialog(null, "Selecciona el sexo");
            return;
        }
        
        if (!validation.validarVacios(tfAddCorreo.getText(), "CORREO")) {
            return;
        }
        
        if (!validation.validarCorreo(tfAddCorreo.getText())) {
            return;
        }
        
        if (!validation.validarVacios(tfAddPass.getText(), "CONTRASEÑA")) {
            return;
        }
        
        if (!validation.validaPassword(tfAddPass.getText(), tfConfirmar.getText())) {
            return;
        }
       
        //______________________________________________________________
        // PREPARAMOS LA SENTENCIA PARA INSERTAR LOS DATOS
        try {
            conexion = DBConnection.connect();
            String sql = "INSERT INTO usuarios "
                    + "(nombre, apellido, sexo, correo, usuario, pass) "
                    + "VALUES (?, ?, ?, ?, ?, ?)";
            
            PreparedStatement estado = conexion.prepareStatement(sql);
            estado.setString(1, tfAddNombre.getText());
            estado.setString(2, tfAddApellido.getText());
            estado.setString(3, cbAddsex.getValue().toString());
            estado.setString(4, tfAddCorreo.getText());
            estado.setString(5, tfAddUser.getText());
            estado.setString(6, DigestUtils.sha1Hex(tfAddPass.getText()));
            
            tfAddNombre.setText("");
            tfAddApellido.setText("");
            tfAddCorreo.setText("");
            tfAddUser.setText("");
            tfAddPass.setText("");
            tfConfirmar.setText("");
            cbAddsex.setValue("");
            
            int n = estado.executeUpdate();
            
            if (n == 0) {
                JOptionPane.showMessageDialog(null, "Fallo el registro");
            } 
            
            estado.close();
            
        } catch (SQLException e) {
            
            JOptionPane.showMessageDialog(null, "Fallo el registro "+e);
            
        }
    }
    
    @FXML
    private void regresarPrincipal(ActionEvent event) {
        tfAddNombre.setText("");
        tfAddApellido.setText("");
        tfAddCorreo.setText("");
        tfAddUser.setText("");
        tfAddPass.setText("");
        tfConfirmar.setText("");
        cbAddsex.setValue("");
        
        tfAddUser.setStyle("-fx-border-color: #3399CC;");
        
        controlador.setScreen(ScreensFramework.loginID);
    }
    
    @FXML
    private void salir(ActionEvent event) {
        this.controlesBasicos.salirSistema();
    }
}
