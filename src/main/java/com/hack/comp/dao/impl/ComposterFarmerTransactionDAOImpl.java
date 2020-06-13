package com.hack.comp.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.hack.comp.connection.Connections;
import com.hack.comp.dao.schema.ComposterFarmerTransactionDAO;
import com.hack.comp.model.composterFarmerTransaction.ComposterFarmerTransactionModel;

@Service
public class ComposterFarmerTransactionDAOImpl implements ComposterFarmerTransactionDAO
{

	@Override
	public Integer addComposterFarmerTransaction(Long composter_init_id, Long composter_id, Long farmer_id,Timestamp date_time) throws SQLException, ClassNotFoundException 
	{
		Connection c=Connections.setConnection();
		PreparedStatement stmt=c.prepareCall(
				"INSERT INTO public.composter_farmer_transaction(\r\n" + 
				"										composter_compost_init_id,\r\n" + 
				"										composter_id, \r\n" + 
				"										farmer_id, \r\n" + 
				"										farmer_name, \r\n" + 
				"										farmer_contact,\r\n" + 
				"										date_time)\r\n" + 
				"	VALUES (\r\n" + 
				"		?, \r\n" + 
				"		?, \r\n" + 
				"		?,\r\n" + 
				"		(SELECT farmer.farmer_info.farmer_name FROM farmer.farmer_info WHERE farmer.farmer_info.id=? AND \"deleteIndex\"=FALSE), \r\n" + 
				"		(SELECT farmer.farmer_info.farmer_contact_number FROM farmer.farmer_info WHERE farmer.farmer_info.id=? AND \"deleteIndex\"=FALSE),\r\n" + 
				"		?);");
		stmt.setLong(1, composter_init_id);
		stmt.setLong(2, composter_id);
		stmt.setLong(3, farmer_id);
		stmt.setLong(4, farmer_id);
		stmt.setLong(5, farmer_id);
		stmt.setTimestamp(6, date_time);
		Integer rs=stmt.executeUpdate();
		c.commit();
		stmt.close();
		c.close();
		return rs;
	}

	@Override
	public List<ComposterFarmerTransactionModel> selectComposterFarmerTransaction(Long init_id)throws SQLException, ClassNotFoundException 
	{
		Connection c=Connections.setConnection();
		PreparedStatement stmt=c.prepareStatement(
				"SELECT\r\n" + 
				"	public.composter_farmer_transaction.inc_id,\r\n" + 
				"	public.composter_farmer_transaction.composter_compost_init_id,\r\n" + 
				"	public.composter_farmer_transaction.composter_id,\r\n" + 
				"	public.composter_farmer_transaction.farmer_id,\r\n" + 
				"	public.composter_farmer_transaction.farmer_name,\r\n" + 
				"	public.composter_farmer_transaction.farmer_contact,\r\n" + 
				"	public.composter_farmer_transaction.date_time\r\n" + 
				"FROM\r\n" + 
				"	public.composter_farmer_transaction\r\n" + 
				"WHERE \r\n" + 
				"	public.composter_farmer_transaction.composter_compost_init_id=?;");
		stmt.setLong(1, init_id);
		ResultSet rs=stmt.executeQuery();
		List<ComposterFarmerTransactionModel> cf=new ArrayList<ComposterFarmerTransactionModel>();
		if(rs.next())
		{
			cf.add(new ComposterFarmerTransactionModel(
					rs.getLong("inc_id"),
					rs.getLong("composter_compost_init_id"),
					rs.getLong("composter_id"),
					rs.getLong("farmer_id"),
					rs.getString("farmer_name"),
					rs.getString("farmer_contact"),
					rs.getTimestamp("date_time")
					));
		}
		rs.close();
		stmt.close();
		c.close();
		return cf;
	}

}