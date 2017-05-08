insert ignore into security_group(groupname, groupdescription) values('CUSTOMERS', 'This group contains customers.');
insert ignore into security_group(groupname, groupdescription) values('MANAGERS', 'This group contains managers.');

insert ignore into security_user(username, password) values('customerlogin1',SHA2('customer1',256));
insert ignore into security_user(username, password) values('customerlogin2',SHA2('customer2',256));
insert ignore into security_user(username, password) values('customerlogin3',SHA2('customer3',256));
insert ignore into security_user(username, password) values('managerlogin1',SHA2('manager1',256));
insert ignore into security_user(username, password) values('managerlogin2',SHA2('manager2',256));
insert ignore into security_user(username, password) values('managerlogin3',SHA2('manager3',256));

insert ignore into sec_user_groups(groupname, username) values('CUSTOMERS','customerlogin1');
insert ignore into sec_user_groups(groupname, username) values('CUSTOMERS','customerlogin2');
insert ignore into sec_user_groups(groupname, username) values('CUSTOMERS','customerlogin3');
insert ignore into sec_user_groups(groupname, username) values('MANAGERS','managerlogin1');
insert ignore into sec_user_groups(groupname, username) values('MANAGERS','managerlogin2');
insert ignore into sec_user_groups(groupname, username) values('MANAGERS','managerlogin3');
insert ignore into sec_user_groups(groupname, username) values('CUSTOMERS','managerlogin3');

insert into customer(customerid, customeremail, customername, dateofbirth, username) values (101, 'ravi@gmail.com', 'Ravi Kumar', '1989-09-30', 'customerlogin1');
insert into customer(customerid, customeremail, customername, dateofbirth, username) values (102, 'sunil@gmail.com', 'Sunil Kumar', '1984-08-18', 'customerlogin2');
insert into customer(customerid, customeremail, customername, dateofbirth, username) values (103, 'vijay@gmail.com', 'Vijay Hazare', '1987-02-15', 'customerlogin3');
insert into customer(customerid, customeremail, customername, dateofbirth, username) values (104, 'rahul@gmail.com', 'Rahul Malhotra', '1988-05-16', 'managerlogin3');

insert into manager(managerid, dateofbirth, manageremail, managername, username) values (501, '1964-06-21', 'manager1@gmail.com', 'Manager 1', 'managerlogin1');
insert into manager(managerid, dateofbirth, manageremail, managername, username) values (502, '1965-07-11', 'manager2@gmail.com', 'Manager 2', 'managerlogin2');
insert into manager(managerid, dateofbirth, manageremail, managername, username) values (503, '1966-08-01', 'manager3@gmail.com', 'Manager 3', 'managerlogin3');

insert into menuitem(itemid, itemname, cost, category, dateadded) values (1, 'Veg Samosa', 5.00, 'Appetizers', '2017-01-22');
insert into menuitem(itemid, itemname, cost, category, dateadded) values (2, 'Sambar', 10.00, 'Soup', '2017-02-23');
insert into menuitem(itemid, itemname, cost, category, dateadded) values (3, 'Chicken Biryani', 11.00, 'Rice', '2017-03-05');

insert into lineitem(orderid, tableno, quantity, orderdate, item_id, item_name, orderamount, cust_id, status, transid) values (201, 'Table 1', 1, '2017-01-19', 1, 'Veg Samosa', 5.00, 101, 'BILLED', 1);
insert into lineitem(orderid, tableno, quantity, orderdate, item_id, item_name, orderamount, cust_id, status, transid) values (202, 'Table 2', 2, '2017-02-03', 2, 'Sambar', 10.00, 102, 'BILLED', 2);
insert into lineitem(orderid, tableno, quantity, orderdate, item_id, item_name, orderamount, cust_id, status, transid) values (203, 'Table 3', 3, '2017-03-09', 3, 'Chicken Biryani', 33.00, 103, 'BILLED', 3);

insert into cheque(billid, billamount, billstatus, billgenerateddate, manager_id, customer_id, transactionid) values (301, 123.2, 'INACTIVE', '2017-02-13', 501, 101, 1);
insert into cheque(billid, billamount, billstatus, billgenerateddate, manager_id, customer_id, transactionid) values (302, 221.1, 'INACTIVE', '2017-02-14', 502, 102, 2);
insert into cheque(billid, billamount, billstatus, billgenerateddate, manager_id, customer_id, transactionid) values (303, 101.11, 'INACTIVE', '2017-02-15', 503, 103, 3);
