SELECT DISTINCT
Customer.Cust_LastName AS 'Last Name' , Customer.Cust_FirstName AS 'First Name',
Customer.Cust_MemType AS 'Membership Type', 
SUM(MR.Record_PtsEarned) AS 'Member Total Points'
 
FROM Customer 
JOIN Member_Record MR ON MR.Cust_Num = Customer.Cust_Num 

GROUP BY Customer.Cust_MemType, Customer.Cust_FirstName, Customer.Cust_LastName
HAVING SUM(MR.Record_PtsEarned) <= 60

ORDER BY SUM(MR.Record_PtsEarned) DESC; 