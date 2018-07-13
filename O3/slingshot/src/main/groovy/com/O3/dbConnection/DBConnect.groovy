package com.O3.dbConnection

import com.O3.model.FailureOutcome
import com.O3.model.SuccessOutcome
import com.O3.web.WebForm
import com.O3.web.WebPage

import com.O3.tools.Browser

import com.O3.app.pages.O3CommonPage
import  java.sql.Connection;
import  java.sql.Statement;
import  java.sql.ResultSet;
import  java.sql.DriverManager;
import  java.sql.SQLException;



final class DBConnect extends WebPage {

	def static returnQuery(String query1){
		//Connection URL Syntax: "jdbc:mysql://ipaddress:portnumber/db_name"
		String dbUrl = "jdbc:mysql://175.100.151.226:3306/o3_backoffice";

		//Database Username
		String username = "root";

		//Database Password
		String password = "root";

		//Query to Execute
		String query = query1;
		println "query "+query

		//Load mysql jdbc driver
		Class.forName("com.mysql.jdbc.Driver");

		//Create Connection to DB
		Connection con = DriverManager.getConnection(dbUrl,username,password);

		//Create Statement Object
		Statement stmt = con.createStatement();
		ResultSet rs= null;
		def list = []
		// Execute the SQL Query. Store results in ResultSet
		if(query.contains("select")){
			rs= stmt.executeQuery(query);
			int i = 1;
			//println "Size : "+rs.size()
			while (rs.next()){
				def test = rs.getString(i)
				list.add(test)
				i++;
			}
			println "Result : "+list
		}else{
			stmt.executeUpdate(query);
			
		}
		// closing DB Connection
		con.close();
		return list;
	}
}
