package com.rays.pro4.controller;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.rays.pro4.Bean.BaseBean;
import com.rays.pro4.Bean.TradeBean;
import com.rays.pro4.Exception.ApplicationException;
import com.rays.pro4.Exception.DuplicateRecordException;
import com.rays.pro4.Model.TradeModel;
import com.rays.pro4.Util.DataUtility;
import com.rays.pro4.Util.DataValidator;
import com.rays.pro4.Util.PropertyReader;
import com.rays.pro4.Util.ServletUtility;

@WebServlet(name = "/TradeCtl" , urlPatterns = "/ctl/TradeCtl")
public class TradeCtl extends BaseCtl{

	private static final long serialVersionUID = 1L;

	private static Logger log = Logger.getLogger(TradeCtl.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see in.co.rays.ors.controller.BaseCtl#validate(javax.servlet.http.
	 * HttpServletRequest)
	 */
	@Override
	protected boolean validate(HttpServletRequest request) {
		System.out.println("uctl Validate");
		log.debug("TradeCtl Method validate Started");

		boolean pass = true;

		if (DataValidator.isNull(request.getParameter("userId"))) {
			request.setAttribute("userId", PropertyReader.getValue("error.require", "user Id"));
			pass = false;
		} else if (!DataValidator.isName(request.getParameter("userId"))) {
			request.setAttribute("userId", "User Id Contain Alphabet Only  ");
			pass = false;
		} else if (DataValidator.isLength(request.getParameter("userId"),8)) {
			request.setAttribute("userId", "User Id Contain 8 charactesr  ");
			pass = false;
		}

		if (DataValidator.isNull(request.getParameter("endDate"))) {
			request.setAttribute("endDate", PropertyReader.getValue("error.require", "End Date"));
			pass = false;
		} else if (!DataValidator.isDate(request.getParameter("endDate"))) {
			request.setAttribute("endDate", PropertyReader.getValue("error.date", "End Date"));
			pass = false;
		}
		
		if (DataValidator.isNull(request.getParameter("startDate"))) {
			request.setAttribute("startDate", PropertyReader.getValue("error.require", "Start Date"));
			pass = false;
		} else if (!DataValidator.isDate(request.getParameter("startDate"))) {
			request.setAttribute("startDate", PropertyReader.getValue("error.date", "Start Date"));
			pass = false;
		}


		if (DataValidator.isNull(request.getParameter("transactionType"))) {
			request.setAttribute("transactionType", PropertyReader.getValue("error.require", "Transaction Type"));
			pass = false;
		} else if (!DataValidator.isInteger(request.getParameter("transactionType"))) {
			request.setAttribute("transactionType", "Transaction Type contains numerical vlaue only");
			pass = false;
		}

		log.debug("TradeCtl Method validate Ended");

		return pass;
	}

	@Override
	protected void preload(HttpServletRequest request) {

		Map<Integer, String> map = new HashMap<Integer, String>();

		map.put(1, "Buy");
		map.put(2, "Sell");
		map.put(3, "All");
		
		
		request.setAttribute("Trade", map);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see in.co.rays.ors.controller.BaseCtl#populateBean(javax.servlet.http.
	 * HttpServletRequest)
	 */

	/**
	 * This is Populate Bean
	 */
	protected BaseBean populateBean(HttpServletRequest request) {
		System.out.println(" uctl Base bean P bean");
		log.debug("TradeCtl Method populatebean Started");

		TradeBean bean = new TradeBean();

		bean.setId(DataUtility.getLong(request.getParameter("id")));

		bean.setUserId(DataUtility.getString(request.getParameter("userId")));

		bean.setStartDate(DataUtility.getDate(request.getParameter("startDate")));

		bean.setEndDate(DataUtility.getDate(request.getParameter("endDate")));

		bean.setTransactionType(DataUtility.getInt(request.getParameter("transactionType")));

		log.debug("TradeCtl Method populatebean Ended");

		return bean;

	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		log.debug("TradeCtl Method doGet Started");

		String op = DataUtility.getString(request.getParameter("operation"));
		// get model
		TradeModel model = new TradeModel();
		long id = DataUtility.getLong(request.getParameter("id"));
		System.out.println("Medication Edit Id >= " + id);
		if (id != 0 && id > 0) {
			System.out.println("in id > 0  condition");
			TradeBean bean;
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
		log.debug("TradeCtl Method doGet Ended");
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		System.out.println("uctl Do Post");

		log.debug("TradeCtl Method doPost Started");

		String op = DataUtility.getString(request.getParameter("operation"));
		long id = DataUtility.getLong(request.getParameter("id"));

		TradeModel model = new TradeModel();
		if (OP_SAVE.equalsIgnoreCase(op) || OP_UPDATE.equalsIgnoreCase(op)) {
			TradeBean bean = (TradeBean) populateBean(request);
			System.out.println(" U ctl DoPost 11111111");

			try {
				if (id > 0) {

					// System.out.println("hi i am in dopost update");

					model.update(bean);
					ServletUtility.setBean(bean, request);
					System.out.println(" U ctl DoPost 222222");
					ServletUtility.setSuccessMessage("Trade is successfully Updated", request);

				} else {
					System.out.println(" U ctl DoPost 33333");
					long pk = model.add(bean);

					bean = model.findByPK(pk);


					ServletUtility.setSuccessMessage("Trade is successfully Added", request);
					/*
					 * ServletUtility.forward(getView(), request, response);
					 */ }
				/*
				 * ServletUtility.setBean(bean, request);
				 * ServletUtility.setSuccessMessage("Stock is successfully saved", request);
				 */

			} catch (ApplicationException | DuplicateRecordException e) {
				log.error(e);
				ServletUtility.handleException(e, request, response);
				return;
			} 
		} else if (OP_DELETE.equalsIgnoreCase(op)) {
			System.out.println(" U ctl D p 5555555");

			TradeBean bean = (TradeBean) populateBean(request);
			try {
				model.delete(bean);
				System.out.println(" u ctl D Post  6666666");
				ServletUtility.redirect(ORSView.TRADE_CTL ,request, response);
				return;
			} catch (ApplicationException e) {
				log.error(e);
				ServletUtility.handleException(e, request, response);
				return;
			}

		} else if (OP_CANCEL.equalsIgnoreCase(op)) {
			System.out.println(" U  ctl Do post 77777");

			ServletUtility.redirect(ORSView.TRADE_LIST_CTL, request, response);
			return;
		}
		ServletUtility.forward(getView(), request, response);

		log.debug("TradeCtl Method doPostEnded");
	}

	
	@Override
	protected String getView() {
		// TODO Auto-generated method stub
		return ORSView.TRADE_VIEW;
	}

	
}
