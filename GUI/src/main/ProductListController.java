package GUI.controllor;

import GUI.Class.ProductList;
import GUI.DBconnection;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;

import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ProductListController implements Initializable
{


    @FXML
    private  Button btn_submit;

    @FXML
    private Label label;

    @FXML
    private TextField txt_product;

    @FXML
    private TextField txt_productdescription;

    @FXML
    private TextField txt_usecount;

    @FXML
    private TextField txt_lastused;

    @FXML
    private TextField txt_search;

    @FXML
    private TableView<ProductList> tableProduct;

    @FXML
    private TableColumn<?, ?> columnProduct;

    @FXML
    private TableColumn<?, ?> columnProductDescription;

    @FXML
    private TableColumn<?, ?> columnUseCount;

    @FXML
    private TableColumn<?, ?> columnLastUsed;

    @FXML
    private Label error_product;

    @FXML
    private Label error_productdescription;

    @FXML
    private Label error_usecount;

    @FXML
    private Label error_lastused;

    @FXML
    private Button btn_okay;

    @FXML
    private Button btn_clear;

    @FXML
    private Text txt_title;

    private Connection con = null;
    private PreparedStatement pst = null;
    private ResultSet rs = null;
    private ObservableList<ProductList> data;

    //use this for getting the products from a certain date
    private String inputDate;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setCellTable();
        con=GUI.DBconnection.dConnection();
        data = FXCollections.observableArrayList();

    }



    public void setCellTable() {
        columnProduct.setCellValueFactory(new PropertyValueFactory<>("product"));
        columnProductDescription.setCellValueFactory(new PropertyValueFactory<>("productDescription"));
        columnUseCount.setCellValueFactory(new PropertyValueFactory<>("useCount"));
        columnLastUsed.setCellValueFactory(new PropertyValueFactory<>("lastUsed"));
        //the names in the "" have are case sensitive to the ProductList class setters and getters.
    }

    @FXML
    //when you click okay, the data will load using the input text.
    private void handleSubmit(ActionEvent event) throws IOException {

        inputDate = txt_lastused.getText();
        loadDataFromDatabase();
        txt_title.setText("Popular Products \nSince " + inputDate);

    }

    private void loadDataFromDatabase(){
        try {
            //Made some small changes to the sql query
            pst = con.prepareStatement("SELECT \n" +
                    "Product.Prod_Name AS 'Product',\n" +
                    "Product.Prod_Description AS 'Product Description',\n" +
                    "COUNT(Appt_Svc.Svc_Product) AS 'Use Count',\n" +
                    "FORMAT(MAX(A.Appt_Date), 'MM/dd/yyyy') 'Last Used'\n" +
                    "\n" +
                    "FROM Product\n" +
                    "JOIN Appt_Svc ON Product.Prod_Num = Appt_Svc.Svc_Product\n" +
                    "INNER JOIN Appointment A ON Appt_Svc.Appt_Num = A.Appt_Num\n" +
                    "INNER JOIN Week_Day ON A.Appt_Date = Week_Day.Day_Date\n" +
                    "JOIN Work_Week W ON W.Week_Num = Week_Day.Week_Num\n" +
                    "\n" +
                    "WHERE W.Week_End >= ?" + //the question mark is variable, based on GUI input
                    "\n" +
                    "GROUP By Product.Prod_Name, Product.Prod_Description\n" +
                    "\n" +
                    "ORDER BY 'Use Count' DESC;");

            //this refers to the ? in the SQL query, allowing us to use a variable date in the GUI
            pst.setString(1,inputDate);

            rs = pst.executeQuery();
            while (rs.next()) {
                //the column names were not right, there's 4 columns for this query.
                data.add(new ProductList(rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4)));
            }

        } catch (SQLException ex) {
            Logger.getLogger(DBconnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        tableProduct.setItems(data);
    }

    @FXML
    //the clear button will clear the data, and the input field.
    private void clearTextField(ActionEvent event){
        txt_lastused.clear(); //clears input field
        data.clear(); //clears data
    }


    //I don't know what this was for...
    /*
    private void setCellValueFromTableToTextField(){
        tableProduct.setOnMouseClicked(e -> {
            ProductList pl = tableProduct.getItems().get(tableProduct.getSelectionModel().getSelectedIndex());
            txt_product.setText(pl.getProduct());
            txt_productdescription.setText(pl.getProductDescription());
            txt_usecount.setText(pl.getUseCount());
            txt_lastused.setText(pl.getLastUsed());
        });
    }
    */


    private void searchProduct()
    {
        txt_search.setOnKeyReleased(e -> {
            if (txt_search.getText().equals("")) {
                loadDataFromDatabase();
            }
            else {
                data.clear();
                String sql = "Select*From Product Where Prod_Name LIKE '%" + txt_search.getText() + "%'"
                        + "UNION Select * From Product Where Prod_Description LIKE '%"+txt_search.getText()+"%'"
                        + "UNION Select * From Appt_Svc Where Svc_Product LIKE '&"+txt_search.getText()+"%'"
                        + "UNION Select * From Appointment Where Appt_Date LIKE '%"+txt_search.getText()+"%'";
                try {
                    pst = con.prepareStatement(sql);
                    rs = pst.executeQuery();
                    while (rs.next()) {
                        System.out.println("" + rs.getString(2));
                        data.add(new ProductList(rs.getString(2), rs.getString(3),rs.getString(4),rs.getString(5)));
                    }
                    tableProduct.setItems(data);
                } catch (SQLException ex) {
                    Logger.getLogger(DBconnection.class.getName()).log(Level.SEVERE,null,ex);
                }
            }
        });

    }




}
