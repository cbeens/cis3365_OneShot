
SELECT TOP 5
CONCAT(Customer.Cust_FirstName, ' ', Customer.Cust_LastName) AS Customer,
M.Mem_Type AS 'Member Status',
SUM(MR.Record_PtsEarned) AS 'Total Points',
FORMAT(MAX(MR.Record_Date), 'MM/dd/yyyy') 'Last Visit'

FROM Customer
JOIN Member_Record MR ON Customer.Cust_Num = MR.Cust_Num
JOIN Membership M ON Customer.Cust_MemType = M.Mem_Type

GROUP BY M.Mem_Type, Customer.Cust_FirstName, Customer.Cust_LastName

ORDER BY SUM(MR.Record_PtsEarned) DESC;