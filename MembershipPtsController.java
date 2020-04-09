package GUI.controllor;

import GUI.Class.membershipPts;
import GUI.DBconnection;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.ToggleGroup;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MembershipPtsController implements Initializable {

    @FXML
    private Button cancel;

    @FXML
    private RadioButton above;

    @FXML
    private RadioButton below;

    @FXML
    private TextField int_value;

    @FXML
    private Button submit;

    @FXML
    private TableView<membershipPts> table_MemberInfo;

    @FXML
    private TableColumn<?, ?> table_MemberName;

    @FXML
    private TableColumn<?, ?> table_MemberType;

    @FXML
    private TableColumn<?, ?> table_MemberPts;

    @FXML
    private RadioButton highScore;

    @FXML
    private TextField input_value;

    @FXML
    private RadioButton lowScore;

    @FXML
    private void clear(ActionEvent event) {
        data.clear();

    }

    private Connection con = null;
    private PreparedStatement pst = null;
    private Statement stmnt = null;
    private ResultSet rs= null;
    private ObservableList<membershipPts> data;
    private String threshold;


    public void initialize(URL url, ResourceBundle rb) {
        firstfunction();
        con=GUI.DBconnection.dConnection();
        data = FXCollections.observableArrayList();

    }

    public void firstfunction(){
        //set up tableview
        table_MemberName.setCellValueFactory(new PropertyValueFactory<>("memberName"));
        table_MemberType.setCellValueFactory(new PropertyValueFactory<>("memberType"));
        table_MemberPts.setCellValueFactory(new PropertyValueFactory<>("memberPts"));

    }

    @FXML //CLICKING OKAY WILL POPULATE TABLEVIEW WITH DATA
    private void handleSubmit(ActionEvent event) throws IOException {

        threshold = input_value.getText(); //will be used to set value above or below


        //if selected above, the query 'Christian_MemberPtsGT_Mode' will run using the threshold input.
        if (above.isSelected()) {
            loadAbove();

        }

        //if below is selected, the query 'Rida_LowPointsMem_Mod' will run with threshold input
        else if (below.isSelected()) {
            loadBelow();


        }

        //if highScore (Top 5) is selected, the query 'Luis_HighScore_Mod' will run
        else if (highScore.isSelected()) {
            loadTop5();

        }

        //if lowScore (Bottom 5) is selected, the query 'Luis_LowScore_Mod' will run
        else if (lowScore.isSelected()) {
            loadBottom5();


        }




    }

    //this will run the query for top 5 members
    public void loadTop5(){
        data.clear();

        try {
            pst = con.prepareStatement("SELECT TOP 5\n" +
                    "CONCAT(Customer.Cust_FirstName, ' ', Customer.Cust_LastName) AS Member,\n" +
                    "M.Mem_Type AS 'Member Type',\n" +
                    "SUM(MR.Record_PtsEarned) AS 'Total Points'\n" +
                    "\n" +
                    "FROM Customer\n" +
                    "JOIN Member_Record MR ON Customer.Cust_Num = MR.Cust_Num\n" +
                    "JOIN Membership M ON Customer.Cust_MemType = M.Mem_Type\n" +
                    "\n" +
                    "GROUP BY M.Mem_Type, Customer.Cust_FirstName, Customer.Cust_LastName\n" +
                    "\n" +
                    "ORDER BY SUM(MR.Record_PtsEarned) DESC;");
            rs = pst.executeQuery();
            while(rs.next()){
                data.add(new membershipPts(rs.getString(1),rs.getString(2),rs.getString(3)));

            }

        }

        catch (SQLException ex) {
            Logger.getLogger(DBconnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        table_MemberInfo.setItems(data);
    }

    //this will run the sql query for bottom 5 members
    public void loadBottom5(){
        data.clear();

        try {
            pst = con.prepareStatement("SELECT TOP 5\n" +
                    "CONCAT(Customer.Cust_FirstName, ' ', Customer.Cust_LastName) AS Member,\n" +
                    "M.Mem_Type AS 'Member Type',\n" +
                    "SUM(MR.Record_PtsEarned) 'Total Points'\n" +
                    "\n" +
                    "FROM Customer\n" +
                    "JOIN Member_Record MR ON Customer.Cust_Num = MR.Cust_Num\n" +
                    "JOIN Membership M ON Customer.Cust_MemType = M.Mem_Type\n" +
                    "\n" +
                    "GROUP BY M.Mem_Type, Customer.Cust_FirstName, Customer.Cust_LastName\n" +
                    "\n" +
                    "ORDER BY 'Total Points' ASC;");
            rs = pst.executeQuery();
            while(rs.next()){
                data.add(new membershipPts(rs.getString(1),rs.getString(2),rs.getString(3)));

            }

        }

        catch (SQLException ex) {
            Logger.getLogger(DBconnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        table_MemberInfo.setItems(data);
    }

    //this will run the query for members above threshold
    public void loadAbove(){
        data.clear();

        try {
            pst = con.prepareStatement("SELECT DISTINCT\n" +
                    "CONCAT(C.Cust_FirstName, ' ', C.Cust_LastName) AS Member,\n" +
                    "C.Cust_MemType AS 'Member Type',\n" +
                    "SUM(MR.Record_PtsEarned) AS 'Total Points'\n" +
                    "\n" +
                    "FROM Customer C\n" +
                    "JOIN Member_Record MR ON MR.Cust_Num  = C.Cust_Num  \n" +
                    "\n" +
                    "GROUP BY C.Cust_MemType, C.Cust_FirstName, C.Cust_LastName\n" +
                    "HAVING SUM(MR.Record_PtsEarned) >= ?" +
                    " ORDER BY SUM(MR.Record_PtsEarned) DESC; ");
            pst.setString(1,threshold);
            rs = pst.executeQuery();
            while(rs.next()){
                data.add(new membershipPts(rs.getString(1),rs.getString(2),rs.getString(3)));

            }

        }

        catch (SQLException ex) {
            Logger.getLogger(DBconnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        table_MemberInfo.setItems(data);
    }

    //this will run the query for members below threshold
    public void loadBelow(){
        data.clear();

        try {
            pst = con.prepareStatement("SELECT DISTINCT\n" +
                    "CONCAT(C.Cust_FirstName, ' ', C.Cust_LastName) AS Member,\n" +
                    "C.Cust_MemType AS 'Member Type',\n" +
                    "SUM(MR.Record_PtsEarned) AS 'Total Points'\n" +
                    "\n" +
                    "FROM Customer C\n" +
                    "JOIN Member_Record MR ON MR.Cust_Num  = C.Cust_Num  \n" +
                    "\n" +
                    "GROUP BY C.Cust_MemType, C.Cust_FirstName, C.Cust_LastName\n" +
                    "HAVING SUM(MR.Record_PtsEarned) <= ?" +
                    " ORDER BY SUM(MR.Record_PtsEarned) DESC; ");
            pst.setString(1,threshold);
            rs = pst.executeQuery();
            while(rs.next()){
                data.add(new membershipPts(rs.getString(1),rs.getString(2),rs.getString(3)));

            }

        }

        catch (SQLException ex) {
            Logger.getLogger(DBconnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        table_MemberInfo.setItems(data);
    }
}
