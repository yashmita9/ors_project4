package com.rays.pro4.Model;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;

//import org.apache.log4j.Logger;

import com.rays.pro4.Bean.CustomerBean;
import com.rays.pro4.Exception.ApplicationException;
import com.rays.pro4.Exception.DatabaseException;
import com.rays.pro4.Exception.DuplicateRecordException;
import com.rays.pro4.Exception.RecordNotFoundException;
import com.rays.pro4.Util.DataUtility;
import com.rays.pro4.Util.EmailBuilder;
import com.rays.pro4.Util.EmailMessage;
import com.rays.pro4.Util.EmailUtility;
import com.rays.pro4.Util.JDBCDataSource;

/**
 * JDBC Implementation of CustomerModel.
 * 
 * @author Yashmita Rathore
 *
 */

public class CustomerModel {
	private static Logger log = Logger.getLogger(CustomerModel.class);

	/**
	 * Find next PK of Customer
	 *
	 * @throws DatabaseException
	 */

	public int nextPK() throws DatabaseException {

		log.debug("Model nextPK Started");

		String sql = "select max(id) from customer";
		Connection conn = null;
		int pk = 0;
		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				pk = rs.getInt(1);
			}
			rs.close();
		} catch (Exception e) {

			throw new DatabaseException("Exception : Exception in getting PK");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		log.debug("Model nextPK Started");
		return pk + 1;

	}

	/**
	 * Customer Add
	 *
	 * @param bean
	 * @throws DatabaseException
	 *
	 */

	/**
	 * @param bean
	 * @return
	 * @throws ApplicationException
	 * @throws DuplicateRecordException
	 */
	public long add(CustomerBean bean) throws ApplicationException, DuplicateRecordException {
		log.debug("Model add Started");

		String sql = "insert into customer values(?,?,?,?,?)";

		Connection conn = null;
		int pk = 0;

		try {
			conn = JDBCDataSource.getConnection();
			pk = nextPK();

			conn.setAutoCommit(false);
			PreparedStatement pstmt = conn.prepareStatement(sql);

			pstmt.setLong(1, pk);
			pstmt.setString(2, bean.getClientName());
			pstmt.setString(3, bean.getLocation());
			pstmt.setLong(4, bean.getContactNumber());
			pstmt.setString(5, bean.getImportance());

			int i = pstmt.executeUpdate();
			System.out.println(i);
			conn.commit();
			pstmt.close();

		} catch (Exception e) {
			log.error("Database Exception ...", e);
			try {
				e.printStackTrace();
				conn.rollback();

			} catch (Exception e2) {
				e2.printStackTrace();
				// application exception
				throw new ApplicationException("Exception : add rollback exceptionn" + e2.getMessage());
			}
		}

		finally {
			JDBCDataSource.closeConnection(conn);
		}
		log.debug("Model Add End");
		return pk;

	}

	/**
	 * Delete a Customer
	 *
	 * @param bean
	 * @throws DatabaseException
	 */
	public void delete(CustomerBean bean) throws ApplicationException {
		log.debug("Model delete start");
		String sql = "delete from customer where id=?";
		Connection conn = null;
		try {
			conn = JDBCDataSource.getConnection();
			conn.setAutoCommit(false);
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, bean.getId());
			pstmt.executeUpdate();
			conn.commit();
			pstmt.close();
		} catch (Exception e) {
			log.error("DataBase Exception", e);
			try {
				conn.rollback();
			} catch (Exception e2) {
				throw new ApplicationException("Exception: Delete rollback Exception" + e2.getMessage());
			}
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		log.debug("Model Delete End");
	}

	/**
	 * Find Customer by PK
	 *
	 * @param pk : get parameter
	 * @return bean
	 * @throws DatabaseException
	 */

	public CustomerBean findByPK(long pk) throws ApplicationException {
		log.debug("Model findBy PK start");
		String sql = "select * from customer where id=?";
		CustomerBean bean = null;
		Connection conn = null;
		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, pk);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				bean = new CustomerBean();
				bean.setId(rs.getLong(1));
				bean.setClientName(rs.getString(2));
				bean.setLocation(rs.getString(3));
				bean.setContactNumber(rs.getLong(4));
				bean.setImportance(rs.getString(5));

			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
			log.error("DataBase Exception ", e);
			throw new ApplicationException("Exception : Exception in getting Customer by pk");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		log.debug("Method Find By PK end");
		return bean;
	}

	/**
	 * Update a customer
	 *
	 * @param bean
	 * @throws DatabaseException
	 */

	public void update(CustomerBean bean) throws ApplicationException, DuplicateRecordException {
		log.debug("Model Update Start");
		String sql = "update customer set client_name=?, location=?, contact_number=?, importance=? where id=? ";
		Connection conn = null;

		try {
			conn = JDBCDataSource.getConnection();
			conn.setAutoCommit(false);
			PreparedStatement pstmt = conn.prepareStatement(sql);

			pstmt.setString(1, bean.getClientName());
			pstmt.setString(2, bean.getLocation());
			pstmt.setLong(3, bean.getContactNumber());
			pstmt.setString(4, bean.getImportance());
			pstmt.setLong(5, bean.getId());

			int i = pstmt.executeUpdate();
			System.out.println("update customer>> " + i);
			conn.commit();
			pstmt.close();
		} catch (Exception e) {
			e.printStackTrace();
			log.error("DataBase Exception", e);
			try {
				conn.rollback();
			} catch (Exception e2) {
				e2.printStackTrace();
				throw new ApplicationException("Exception : Update Rollback Exception " + e2.getMessage());
			}
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		log.debug("Model Update End ");
	}

	/**
	 * Search Customer
	 *
	 * @param bean : Search Parameters
	 * @throws DatabaseException
	 */

	public List search(CustomerBean bean) throws ApplicationException {
		return search(bean, 0, 0);
	}

	/**
	 * Search Customer with pagination
	 *
	 * @return list : List of Customers
	 * @param bean     : Search Parameters
	 * @param pageNo   : Current Page No.
	 * @param pageSize : Size of Page
	 *
	 * @throws DatabaseException
	 */

	public List search(CustomerBean bean, int pageNo, int pageSize) throws ApplicationException {
		log.debug("Model Search Start");
		StringBuffer sql = new StringBuffer("select * from customer where 1=1");
		if (bean != null) {

			if (bean.getClientName() != null && bean.getClientName().length() > 0) {
				sql.append(" AND CLIENT_NAME like '" + bean.getClientName() + "%'");
			}
			if (bean.getLocation() != null && bean.getLocation().length() > 0) {
				sql.append(" AND LOCATION like '" + bean.getLocation() + "%'");
			}
			if (bean.getContactNumber() > 0) {
				sql.append(" AND CONTACT_NUMBER = " + bean.getContactNumber());
			}
			if (bean.getImportance() != null && bean.getImportance().length() > 0) {
				sql.append(" AND IMPORTANCE like '" + bean.getImportance() + "%'");
			}
			
			System.out.println("CUSTOMER SEARCH ===== "+bean.getId());
			if (bean.getId() > 0) {
				sql.append(" AND ID = " + bean.getId());
			}

		}
		// if page size is greater than zero then apply pagination
		if (pageSize > 0) {
			// Calculate start record index
			pageNo = (pageNo - 1) * pageSize;

			sql.append(" Limit " + pageNo + ", " + pageSize);
			// sql.append(" limit " + pageNo + "," + pageSize);
		}
		System.out.println("sql query search >>= " + sql.toString());
		List list = new ArrayList();
		Connection conn = null;
		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql.toString());
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				bean = new CustomerBean();
				bean.setId(rs.getLong(1));
				bean.setClientName(rs.getString(2));
				bean.setLocation(rs.getString(3));
				bean.setContactNumber(rs.getLong(4));
				bean.setImportance(rs.getString(5));

				list.add(bean);

			}
			rs.close();
		} catch (Exception e) {
			log.error("Database Exception", e);
			throw new ApplicationException("Exception: Exception in Search Customer");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		log.debug("Model Search end");
		return list;

	}

	/**
	 * Get List of Customer
	 *
	 * @return list : List of Customer
	 * @throws DatabaseException
	 */

	public List list() throws ApplicationException {
		return list(0, 0);
	}

	/**
	 * Get List of Customer with pagination
	 *
	 * @return list : List of customers
	 * @param pageNo   : Current Page No.
	 * @param pageSize : Size of Page
	 * @throws DatabaseException
	 */

	public List list(int pageNo, int pageSize) throws ApplicationException {
		log.debug("Model list Started");
		ArrayList list = new ArrayList();
		StringBuffer sql = new StringBuffer("select * from customer");

		if (pageSize > 0) {
			pageNo = (pageNo - 1) * pageSize;
			sql.append(" limit " + pageNo + "," + pageSize);
		}

		Connection conn = null;

		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql.toString());
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				CustomerBean bean = new CustomerBean();
				bean.setId(rs.getLong(1));
				bean.setClientName(rs.getString(2));
				bean.setLocation(rs.getString(3));
				bean.setContactNumber(rs.getLong(4));
				bean.setImportance(rs.getString(5));

				list.add(bean);

			}
			rs.close();
		} catch (Exception e) {
			log.error("Database Exception...", e);
			throw new ApplicationException("Exception : Exception in getting list of customers");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		log.debug("Model list End");
		return list;
	}

}