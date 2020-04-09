SELECT DISTINCT
CONCAT(C.Cust_FirstName, ' ', C.Cust_LastName) AS Member,
C.Cust_MemType AS 'Membership Type',
SUM(MR.Record_PtsEarned) AS 'Total Points'

FROM Customer C
JOIN Member_Record MR ON MR.Cust_Num  = C.Cust_Num  

GROUP BY C.Cust_MemType, C.Cust_FirstName, C.Cust_LastName
HAVING SUM(MR.Record_PtsEarned) >= 61
ORDER BY SUM(MR.Record_PtsEarned) DESC;