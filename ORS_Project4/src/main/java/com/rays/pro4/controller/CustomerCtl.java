
package com.rays.pro4.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.rays.pro4.Bean.BaseBean;
import com.rays.pro4.Bean.CustomerBean;
import com.rays.pro4.Exception.ApplicationException;
import com.rays.pro4.Exception.DuplicateRecordException;

import com.rays.pro4.Model.CustomerModel;
import com.rays.pro4.Util.DataUtility;
import com.rays.pro4.Util.DataValidator;
import com.rays.pro4.Util.PropertyReader;
import com.rays.pro4.Util.ServletUtility;

//TODO: Auto-generated Javadoc
/**
 * The Class CustomerCtl.
 * 
 * @author Yashmita Rathore
 * 
 */
@WebServlet(name = "CustomerCtl", urlPatterns = { "/ctl/CustomerCtl" })
public class CustomerCtl extends BaseCtl {

	private static final long serialVersionUID = 1L;

	/** The log. */
	private static Logger log = Logger.getLogger(CustomerCtl.class);

	@Override
	protected void preload(HttpServletRequest request) {
		Map<Integer, String> map = new HashMap<Integer, String>();

		map.put(1, "High");
		map.put(2, "Medium");
		map.put(3, "Low");
		
		
		request.setAttribute("custom", map);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see in.co.rays.ors.controller.BaseCtl#validate(javax.servlet.http.
	 * HttpServletRequest)
	 */
	@Override
	protected boolean validate(HttpServletRequest request) {
		System.out.println("uctl Validate");
		log.debug("CustomerCtl Method validate Started");

		boolean pass = true;

		if (DataValidator.isNull(request.getParameter("clientName"))) {
			request.setAttribute("clientName", PropertyReader.getValue("error.require", "Client Name"));
			pass = false;
		} else if (!DataValidator.isName(request.getParameter("clientName"))) {
			request.setAttribute("clientName", "Client Name contains alphabet only");
			pass = false;
		}

		if (DataValidator.isNull(request.getParameter("location"))) {
			request.setAttribute("location", PropertyReader.getValue("error.require", "Location"));
			pass = false;
		} else if (!DataValidator.isName(request.getParameter("location"))) {
			request.setAttribute("location", " Location contains alphabet only");
			pass = false;
		}
		
		if (DataValidator.isNull(request.getParameter("importance"))) {
			request.setAttribute("importance", PropertyReader.getValue("error.require", "Importance"));
			pass = false;
		}
	

		if (DataValidator.isNull(request.getParameter("contactNumber"))) {
			request.setAttribute("contactNumber", PropertyReader.getValue("error.require", "Contact Number"));
			pass = false;
		} else if (!DataValidator.isMobileNo(request.getParameter("contactNumber"))) {
			request.setAttribute("contactNumber", "Contact Number must be 10 Digit and No. Series start with 6-9");
			pass = false;
		}


		log.debug("CustomerCtl Method validate Ended");

		return pass;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see in.co.rays.ors.controller.BaseCtl#populateBean(javax.servlet.http.
	 * HttpServletRequest)
	 */

	
	
	/**
	 *  This is Populate Bean
	 */
	protected BaseBean populateBean(HttpServletRequest request) {
		System.out.println(" uctl Base bean P bean");
		log.debug("CustomerCtl Method populatebean Started");

		CustomerBean bean = new CustomerBean();

		bean.setId(DataUtility.getLong(request.getParameter("id"))); 
		
		System.out.println("POPULATE BEAN CUSTOMER ===== "+bean.getId());

		bean.setClientName(DataUtility.getString(request.getParameter("clientName")));

		bean.setLocation(DataUtility.getString(request.getParameter("location"))); 
		
		bean.setContactNumber(DataUtility.getLong(request.getParameter("contactNumber")));

		bean.setImportance(DataUtility.getString(request.getParameter("importance")));

	

		log.debug("CustomerCtl Method populatebean Ended");

		return bean;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest,
	 * javax.servlet.http.HttpServletResponse)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		log.debug("CustomerCtl Method doGet Started");

		String op = DataUtility.getString(request.getParameter("operation"));
		// get model
		CustomerModel model = new CustomerModel();
		long id = DataUtility.getLong(request.getParameter("id"));
		System.out.println("Customer Edit Id >= " + id);
		if (id != 0 && id > 0) {
			System.out.println("in id > 0  condition");
			CustomerBean bean;
			try {
				bean = model.findByPK(id);
				System.out.println(bean);
				ServletUtility.setBean(bean, request);
			} catch (ApplicationException e) {
				log.error(e);
				ServletUtility.handleException(e, request, response);
				return;
			}
		}

		ServletUtility.forward(getView(), request, response);
		log.debug("CustomerCtl Method doGet Ended");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest,
	 * javax.servlet.http.HttpServletResponse)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		System.out.println("uctl Do Post");

		log.debug("CustomerCtl Method doPost Started");

		String op = DataUtility.getString(request.getParameter("operation"));
		long id = DataUtility.getLong(request.getParameter("id"));


		CustomerModel model = new CustomerModel();
		if (OP_SAVE.equalsIgnoreCase(op) || OP_UPDATE.equalsIgnoreCase(op)) {
			CustomerBean bean = (CustomerBean) populateBean(request);
			System.out.println(" U ctl DoPost 11111111");

			try {
				if (id > 0) {

					// System.out.println("hi i am in dopost update");
					System.out.println("contact number ==="+bean.getContactNumber());
					model.update(bean);
					ServletUtility.setBean(bean, request);
					System.out.println(" U ctl DoPost 222222");
					ServletUtility.setSuccessMessage("Customer is successfully Updated", request);

				} else {
					System.out.println(" U ctl DoPost 33333");
					long pk = model.add(bean);
					

					ServletUtility.setSuccessMessage("Customer is successfully Added", request);
					/*
					 * ServletUtility.forward(getView(), request, response);
					 */				}
				/*
				 * ServletUtility.setBean(bean, request);
				 * ServletUtility.setSuccessMessage("Customer is successfully saved", request);
				 */

			} catch (ApplicationException e) {
				log.error(e);
				ServletUtility.handleException(e, request, response);
				return;
			} catch (DuplicateRecordException e) {
				System.out.println(" U ctl D post 4444444");
				ServletUtility.setBean(bean, request);
				ServletUtility.setErrorMessage("Login id already exists", request);
			}
		} else if (OP_DELETE.equalsIgnoreCase(op)) {
			System.out.println(" U ctl D p 5555555");

			CustomerBean bean = (CustomerBean) populateBean(request);
			try {
				model.delete(bean);
				System.out.println(" u ctl D Post  6666666");
				ServletUtility.redirect(ORSView.CUSTOMER_CTL, request, response);
				return;
			} catch (ApplicationException e) {
				log.error(e);
				ServletUtility.handleException(e, request, response);
				return;
			}

		} else if (OP_CANCEL.equalsIgnoreCase(op)) {
			System.out.println(" U  ctl Do post 77777");

			ServletUtility.redirect(ORSView.CUSTOMER_LIST_CTL, request, response);
			return;
		}
		ServletUtility.forward(getView(), request, response);

		log.debug("CustomerCtl Method doPostEnded");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see in.co.rays.ors.controller.BaseCtl#getView()
	 */
	@Override
	protected String getView() {
		return ORSView.CUSTOMER_VIEW;
	}

}
