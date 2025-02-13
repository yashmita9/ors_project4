package com.rays.pro4.Model;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.rays.pro4.Bean.TradeBean;
import com.rays.pro4.Exception.ApplicationException;
import com.rays.pro4.Exception.DatabaseException;
import com.rays.pro4.Exception.DuplicateRecordException;
import com.rays.pro4.Util.JDBCDataSource;

public class TradeModel {

	private static Logger log = Logger.getLogger(TradeModel.class);

	public Integer nextPK() throws DatabaseException {
		log.debug("Modal nextPK Started");
		Connection conn = null;
		int pk = 0;
		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement("SELECT MAX(ID) FROM trade");
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				pk = rs.getInt(1);
			}
			rs.close();
		} catch (Exception e) {
			log.error("Database Exception", e);
			throw new DatabaseException("Exceptio :Exception in getting PK");

		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		log.debug("Model nextPK End");
		return pk + 1;
	}
	public long add(TradeBean bean) throws ApplicationException,DuplicateRecordException{
		log.debug("Model add Started");
		Connection conn=null;
		int pk=0;
		
		try{
			conn=JDBCDataSource.getConnection();
			pk=nextPK();
			conn.setAutoCommit(false);
			PreparedStatement pstmt=conn.prepareStatement("INSERT INTO trade VALUES(?,?,?,?,?)");
			pstmt.setInt(1, pk);
			pstmt.setString(2, bean.getUserId());
			pstmt.setDate(3, new java.sql.Date(bean.getStartDate().getTime()));
			pstmt.setDate(4, new java.sql.Date(bean.getEndDate().getTime()));
			pstmt.setInt(5, bean.getTransactionType());
			pstmt.executeUpdate();
			conn.commit();
			pstmt.close();
		}catch(Exception e){
			log.error("Database Exception",e);
			try{
				conn.rollback();
			}catch(Exception ex){
				ex.printStackTrace();
				throw new ApplicationException("Exception : add rollback exception"+ex.getMessage());
			}
			throw new ApplicationException("Exception : Exception in add trade");
		}finally{
			JDBCDataSource.closeConnection(conn);
		}
		log.debug("Model add End");
		return pk;
	}

	public void delete(TradeBean bean) throws ApplicationException {
		log.debug("Model delete Started");
		Connection conn = null;
		try {
			conn = JDBCDataSource.getConnection();
			conn.setAutoCommit(false);
			PreparedStatement pstmt = conn.prepareStatement("DELETE FROM trade WHERE ID=?");
			pstmt.setLong(1, bean.getId());
			pstmt.executeUpdate();
			conn.commit();
			pstmt.close();
		} catch (Exception e) {
			log.error("Database Exception ", e);
			try {
				conn.rollback();
			} catch (Exception ex) {
				throw new ApplicationException("Exception :Delete rollback exception" + ex.getMessage());
			}
			throw new ApplicationException("Exception : Exception in delete trade");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		log.debug("Modal delete End");
	}


	public TradeBean findByPK(long pk) throws ApplicationException {
		log.debug("Model Find BY Pk Stsrted");
		StringBuffer sql = new StringBuffer("SELECT*FROM trade WHERE id=?");
		TradeBean bean = null;
		Connection conn = null;
		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql.toString());
			pstmt.setLong(1, pk);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				bean = new TradeBean();
				bean.setId(rs.getLong(1));
				bean.setUserId(rs.getString(2));
				bean.setStartDate(rs.getDate(3));
				bean.setEndDate(rs.getDate(4));
				bean.setTransactionType(rs.getInt(5));
				}
			rs.close();
		} catch (Exception e) {
			log.error("Database Exception ", e);
			throw new ApplicationException("Exception : Exception is getting trade byPK");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		log.debug("Find By PK End");
		return bean;
	}

	public void update(TradeBean bean) throws ApplicationException, DuplicateRecordException {
		log.debug("Model update Started");
		Connection conn = null;

		try {

			conn = JDBCDataSource.getConnection();

			conn.setAutoCommit(false); // Begin transaction
			PreparedStatement pstmt = conn.prepareStatement(
					"UPDATE trade SET user_id=?,start_date=?,end_date=?,transaction_type=? WHERE ID=?");
			pstmt.setString(1, bean.getUserId());
			pstmt.setDate(2, new java.sql.Date(bean.getStartDate().getTime()));
			pstmt.setDate(3, new java.sql.Date(bean.getEndDate().getTime()));
			pstmt.setInt(4, bean.getTransactionType());
			pstmt.setLong(5, bean.getId());
			pstmt.executeUpdate();
			conn.commit(); // End transaction
			pstmt.close();
		} catch (Exception e) {
			log.error("Database Exception..", e);
			try {
				conn.rollback();
			} catch (Exception ex) {
				throw new ApplicationException("Exception : update rollback exception " + ex.getMessage());
			}
			
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		log.debug("Model update End");
	}

	public List search(TradeBean bean) throws ApplicationException {
		return search(bean, 0, 0);
	}

	public List search(TradeBean bean, int pageNo, int PageSize) throws ApplicationException {
		log.debug("model search Started");
		StringBuffer sql = new StringBuffer("SELECT * FROM trade WHERE 1=1");

		if (bean != null) {
			if (bean.getId() > 0) {
				sql.append(" AND id = " + bean.getId());
			}
			if (bean.getUserId() != null && bean.getUserId().length() > 0) {
				sql.append(" AND user_id like '" + bean.getUserId() + "%'");
			}
			if (bean.getEndDate() != null && bean.getEndDate().getTime() > 0) {
				Date d = new Date(bean.getEndDate().getTime());
				sql.append(" AND END_DATE = '" + d + "'");
			}
			if (bean.getStartDate() != null && bean.getStartDate().getTime() > 0) {
				Date d = new Date(bean.getStartDate().getTime());
				sql.append(" AND start_date = '" + d + "'");
			}
			
			if (bean.getTransactionType() > 0) {
				sql.append(" AND transaction_type like '" + bean.getTransactionType() + "%'");
			}
			
		}
		// if page size is greater than zero then apply pagination
		if (PageSize > 0) {
			// Calculate start record index
			pageNo = (pageNo - 1) * PageSize;
			sql.append(" Limit " + pageNo + "," + PageSize);

		}
		
		System.out.println(sql);
		ArrayList list = new ArrayList();

		Connection conn = null;
		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql.toString());

			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				bean = new TradeBean();

				bean.setId(rs.getLong(1));
				bean.setUserId(rs.getString(2));
				bean.setStartDate(rs.getDate(3));
				bean.setEndDate(rs.getDate(4));
				bean.setTransactionType(rs.getInt(5));
			list.add(bean);
			}
			rs.close();
		} catch (Exception e) {
			log.error("Database Exception..", e);
			throw new ApplicationException("Exception : Exception in search Trade");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		log.debug("model search End");
		return list;
	}

	public List list() throws ApplicationException {
		return list(0, 0);
	}

	public List list(int pageNo, int pageSize) throws ApplicationException {
		log.debug("Model list Started");
		ArrayList list = new ArrayList();
		StringBuffer sql = new StringBuffer("select * from trade");
		// if page size is greater than zero then apply pagination
		if (pageSize > 0) {
			// Calculate start record index
			pageNo = (pageNo - 1) * pageSize;
			sql.append(" limit " + pageNo + "," + pageSize);
		}

		Connection conn = null;
		TradeBean bean = null;

		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql.toString());
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				bean = new TradeBean();
				bean.setId(rs.getLong(1));
				bean.setUserId(rs.getString(2));
				bean.setStartDate(rs.getDate(3));
				bean.setEndDate(rs.getDate(4));
				bean.setTransactionType(rs.getInt(5));
			list.add(bean);
			}
			rs.close();
		} catch (Exception e) {
			log.error("Database Exception..", e);
			throw new ApplicationException("Exception : Exception in getting list of trade");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}

		log.debug("Model list End");
		return list;

	}
	

	
	
}
