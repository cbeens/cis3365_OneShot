package GUI.controllor;

import GUI.Class.*;
import GUI.DBconnection;
import com.sun.corba.se.impl.orb.PrefixParserAction;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MembershipQueryController implements Initializable {

    @FXML
    private TableView<ActiveMember> table_ActiveMembers;

    @FXML
    private TableColumn<?, ?> table_MemberName;

    @FXML
    private TableColumn<?, ?> table_MemberType;

    @FXML
    private TableColumn<?, ?> table_MemberSince;

    @FXML
    private TableView<FrequentMember> table_FrequentMembers;

    @FXML
    private TableColumn<?, ?> table_FrequentMember;

    @FXML
    private TableColumn<?, ?> table_FrequentType;

    @FXML
    private TableColumn<?, ?> table_FrequentVisits;

    @FXML
    private TableView<NewMember> table_NewMembers;

    @FXML
    private TableColumn<?, ?> table_NewMemberships;

    @FXML
    private TableColumn<?, ?> table_MembershipsSince;

    @FXML
    private TableView<PopularMember> table_PopularMember;

    @FXML
    private TableColumn<?, ?> table_PopularType;

    @FXML
    private TableColumn<?, ?> table_PopularCount;

    @FXML
    private TableColumn<?, ?> table_PopularVisits;

    @FXML
    private TableView<CountMember> table_MemberCount;

    @FXML
    private TableColumn<?, ?> table_MemberLocation;

    @FXML
    private TableColumn<?, ?> table_LocationCount;

    @FXML
    private DatePicker datePicker;

    @FXML
    private Button clear;

    @FXML
    private RadioButton above;

    @FXML
    private RadioButton below;

    @FXML
    private Button submit;

    @FXML
    private TableView<membershipPts> table_MemberInfo;

    @FXML
    private TableColumn<?, ?> table_MemberName1;

    @FXML
    private TableColumn<?, ?> table_MemberType1;

    @FXML
    private TableColumn<?, ?> table_MemberPts;

    @FXML
    private RadioButton highScore;

    @FXML
    private TextField input_value;

    @FXML
    private RadioButton lowScore;


    //variables for db and query
    private Connection con = null;

    //prepared statements and result sets for SQL..
    //active members
    private PreparedStatement pst_active = null;
    private ResultSet rs_active = null;

    //frequent members
    private PreparedStatement pst_frequent = null;
    private ResultSet rs_frequent = null;

    //new members
    private PreparedStatement pst_new = null;
    private ResultSet rs_new = null;
    private Date sqlDate;

    //popular members
    private PreparedStatement pst_popular = null;
    private ResultSet rs_popular = null;

    //member counts
    private PreparedStatement pst_count = null;
    private ResultSet rs_count = null;

    //member points
    private PreparedStatement pst_points = null;
    private ResultSet rs_points= null;
    @FXML
    private ObservableList<membershipPts> points;
    private String threshold;


    //observable lists for tables
    private ObservableList<ActiveMember> active;
    private ObservableList<FrequentMember> frequent;
    private ObservableList<PopularMember> popular;
    private ObservableList<NewMember> newMem;
    private ObservableList<CountMember> count;
    private ObservableList<membershipPts> data;

    public void initialize(URL url, ResourceBundle rb) {

        //set up tableviews for the display of query objects
        setActiveMembers();
        setFrequentMembers();
        setNewMembers();
        setPopularMembers();
        setMemberCounts();
        setMemberPts();

        //connect to db
        con=GUI.DBconnection.dConnection();

        //observable lists in the GUI
        active = FXCollections.observableArrayList(); //active members table
        frequent = FXCollections.observableArrayList(); //frequent members table
        popular = FXCollections.observableArrayList(); //popular member type table
        newMem = FXCollections.observableArrayList(); //new members table
        count = FXCollections.observableArrayList(); //member counts
        data = FXCollections.observableArrayList(); //member points

        //load sql data into object tables
        loadActiveMembers();
        loadFrequentMembers();
        loadPopularMembers();
        loadMemberCounts();

    }

    //set up active members table
    private void setActiveMembers(){
        table_MemberName.setCellValueFactory(new PropertyValueFactory<>("memberName"));
        table_MemberType.setCellValueFactory(new PropertyValueFactory<>("memberType"));
        table_MemberSince.setCellValueFactory(new PropertyValueFactory<>("memberSince"));
    }

    //set up up frequent members table
    private void setFrequentMembers(){
        table_FrequentMember.setCellValueFactory(new PropertyValueFactory<>("memberName"));
        table_FrequentType.setCellValueFactory(new PropertyValueFactory<>("memberType"));
        table_FrequentVisits.setCellValueFactory(new PropertyValueFactory<>("memberVisits"));
    }

    //set up new members table
    private void setNewMembers(){
        table_NewMemberships.setCellValueFactory(new PropertyValueFactory<>("memberCount"));
        table_MembershipsSince.setCellValueFactory(new PropertyValueFactory<>("memberSince"));
    }

    //set up popular member type table
    private void setPopularMembers(){
        table_PopularType.setCellValueFactory(new PropertyValueFactory<>("memberType"));
        table_PopularCount.setCellValueFactory(new PropertyValueFactory<>("memberCount"));
        table_PopularVisits.setCellValueFactory(new PropertyValueFactory<>("memberVisits"));
    }

    //set up member counts tables
    private void setMemberCounts(){
        table_MemberLocation.setCellValueFactory(new PropertyValueFactory<>("memberLocation"));
        table_LocationCount.setCellValueFactory(new PropertyValueFactory<>("memberCount"));
    }

    //set up member points
    private void setMemberPts(){
        table_MemberName1.setCellValueFactory(new PropertyValueFactory<>("memberName"));
        table_MemberType1.setCellValueFactory(new PropertyValueFactory<>("memberType"));
        table_MemberPts.setCellValueFactory(new PropertyValueFactory<>("memberPts"));
    }


    //this loads the sql query Tolu_MemberStatus
    private void loadActiveMembers(){

        try {
            pst_active = con.prepareStatement("SELECT \n" +
                    " \n" +
                    "CONCAT(Customer.Cust_Firstname, ' ' , Customer.Cust_Lastname) AS Customer,\n" +
                    "Customer.Cust_MemType AS 'Member Type', \n" +
                    "FORMAT(Customer.Cust_MemSince, 'MM/dd/yyyy') AS 'Member Since'\n" +
                    " \n" +
                    "FROM Customer\n" +
                    " \n" +
                    "WHERE Customer.Cust_MemType IS NOT NULL \n" +
                    "AND Cust_MemExpire <= GETDATE();");
            rs_active = pst_active.executeQuery();
            while(rs_active.next()){
                active.add(new ActiveMember(rs_active.getString(1),rs_active.getString(2),rs_active.getString(3)));

            }

        } catch (SQLException ex) {
            Logger.getLogger(DBconnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        table_ActiveMembers.setItems(active);


    }

    //this loads the sql query Rida_FreqMember
    private void loadFrequentMembers(){

        try {
            pst_frequent = con.prepareStatement("SELECT \n" +
                    "CONCAT(Customer.Cust_FirstName, ' ', Customer.Cust_LastName) AS 'Customer', \n" +
                    "Customer.Cust_MemType AS 'Membership Type', \n" +
                    "COUNT(Appointment.Appt_Num) AS 'Visits'\n" +
                    "\n" +
                    " \n" +
                    "FROM Customer\n" +
                    "LEFT JOIN Appointment ON Customer.Cust_Num = Appointment.Cust_Num \n" +
                    "LEFT JOIN Member_Record MR ON Appointment.Appt_Date = MR.Record_Date\n" +
                    "LEFT JOIN Location ON Appointment.Loc_Num = Location.Loc_Num \n" +
                    "\n" +
                    "WHERE Customer.Cust_MemType IS NOT NULL\n" +
                    "\n" +
                    "GROUP BY Customer.Cust_MemType, Customer.Cust_FirstName, Customer.Cust_LastName\n" +
                    "\n" +
                    "HAVING COUNT(Appointment.Appt_Num) > 1\n" +
                    "\n" +
                    "ORDER BY COUNT(Appointment.Appt_Date) DESC;");
            rs_frequent = pst_frequent.executeQuery();
            while(rs_frequent.next()){
                frequent.add(new FrequentMember(rs_frequent.getString(1),rs_frequent.getString(2),rs_frequent.getString(3)));

            }

        } catch (SQLException ex) {
            Logger.getLogger(DBconnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        table_FrequentMembers.setItems(frequent);


    }

    //this loads the sql query Min_PopularType
    private void loadPopularMembers(){

        try {
            pst_popular = con.prepareStatement("SELECT \n" +
                    "Customer.Cust_MemType AS 'Membership Type',\n" +
                    "COUNT(DISTINCT Customer.Cust_Num) AS 'Member Count',\n" +
                    "COUNT(Appointment.Appt_Date) AS Visits\n" +
                    "\n" +
                    "\n" +
                    "FROM Customer\n" +
                    "LEFT OUTER JOIN Appointment ON Appointment.Cust_Num = Customer.Cust_Num\n" +
                    "\n" +
                    "WHERE Customer.Cust_MemType IS NOT NULL\n" +
                    "\n" +
                    "GROUP BY Customer.Cust_MemType\n" +
                    "\n" +
                    "ORDER BY Visits DESC;");
            rs_popular = pst_popular.executeQuery();
            while(rs_popular.next()){
                popular.add(new PopularMember(rs_popular.getString(1),rs_popular.getString(2),rs_popular.getString(3)));

            }

        } catch (SQLException ex) {
            Logger.getLogger(DBconnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        table_PopularMember.setItems(popular);


    }

    @FXML
    //this allows the date to be used for New Members
    void submitDate(ActionEvent event) {

        sqlDate = java.sql.Date.valueOf(datePicker.getValue());
        loadNewMembers();

    }

    //this loads the sql query Tolu_NewMembers
    private void loadNewMembers(){

        try {
            pst_new = con.prepareStatement("SELECT\n" +
                    "\n" +
                    "COUNT(Cust_MemSince) as 'New Memberships',\n" +
                    "FORMAT(?, 'MM/dd/yyyy') AS Since\n" +
                    "\n" +
                    "FROM Customer\n" +
                    "\n" +
                    "WHERE Cust_MemSince >= ?;");
            pst_new.setDate(1,sqlDate);
            pst_new.setDate(2,sqlDate);
            rs_new = pst_new.executeQuery();
            while(rs_new.next()){
                newMem.add(new NewMember(rs_new.getString(1),rs_new.getString(2)));

            }

        } catch (SQLException ex) {
            Logger.getLogger(DBconnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        table_NewMembers.setItems(newMem);


    }

    //this loads the sql query Tolu_NewMembers
    private void loadMemberCounts(){

        try {
            pst_count = con.prepareStatement("Select Location.Loc_Street as Location, \n" +
                    "COUNT(DISTINCT Customer.Cust_Num) AS MemberCount\n" +
                    "\n" +
                    "From Location\n" +
                    "LEFT JOIN Appointment ON Location.Loc_Num = Appointment.Loc_Num\n" +
                    "LEFT JOIN Customer ON Customer.Cust_Num = Appointment.Cust_Num\n" +
                    "LEFT JOIN Member_Record MR ON Appointment.Appt_Date = MR.Record_Date\n" +
                    "\n" +
                    "WHERE Customer.Cust_MemType IS NOT NULL\n" +
                    "\n" +
                    "GROUP BY Location.Loc_Street\n" +
                    "\n" +
                    "ORDER BY COUNT(DISTINCT Customer.Cust_Num) DESC;");
            rs_count = pst_count.executeQuery();
            while(rs_count.next()){
                count.add(new CountMember(rs_count.getString(1),rs_count.getString(2)));

            }

        } catch (SQLException ex) {
            Logger.getLogger(DBconnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        table_MemberCount.setItems(count);


    }


    /************
     * The remaining functions are for the Member Points tab
     * @param event
     * @throws IOException
     */

    @FXML //this handles the okay button
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

    @FXML //this handles clear button
    private void clear(ActionEvent event) {
        points.clear();
        input_value.clear();

    }

    //this will run the query for top 5 members
    private void loadTop5(){
        data.clear();

        try {
            pst_points = con.prepareStatement("SELECT TOP 5\n" +
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
            rs_points = pst_points.executeQuery();
            while(rs_points.next()){
                data.add(new membershipPts(rs_points.getString(1),rs_points.getString(2),rs_points.getString(3)));

            }

        }

        catch (SQLException ex) {
            Logger.getLogger(DBconnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        table_MemberInfo.setItems(data);
    }

    //this will run the sql query for bottom 5 members
    private void loadBottom5(){
        data.clear();

        try {
            pst_points = con.prepareStatement("SELECT TOP 5\n" +
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
            rs_points = pst_points.executeQuery();
            while(rs_points.next()){
                data.add(new membershipPts(rs_points.getString(1),rs_points.getString(2),rs_points.getString(3)));

            }

        }

        catch (SQLException ex) {
            Logger.getLogger(DBconnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        table_MemberInfo.setItems(data);
    }

    //this will run the query for members above threshold
    private void loadAbove(){
        data.clear();

        try {
            pst_points = con.prepareStatement("SELECT DISTINCT\n" +
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
            pst_points.setString(1,threshold);
            rs_points = pst_points.executeQuery();
            while(rs_points.next()){
                data.add(new membershipPts(rs_points.getString(1),rs_points.getString(2),rs_points.getString(3)));

            }

        }

        catch (SQLException ex) {
            Logger.getLogger(DBconnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        table_MemberInfo.setItems(data);
    }

    //this will run the query for members below threshold
    private void loadBelow(){
        data.clear();

        try {
            pst_points = con.prepareStatement("SELECT DISTINCT\n" +
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

            pst_points.setString(1,threshold);
            rs_points = pst_points.executeQuery();
            while(rs_points.next()){
                data.add(new membershipPts(rs_points.getString(1),rs_points.getString(2),rs_points.getString(3)));

            }

        }

        catch (SQLException ex) {
            Logger.getLogger(DBconnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        table_MemberInfo.setItems(data);
    }
}

