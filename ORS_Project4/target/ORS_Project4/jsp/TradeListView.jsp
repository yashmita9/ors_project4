
<%@page import="com.rays.pro4.Model.TradeModel"%>
<%@page import="com.rays.pro4.Bean.TradeBean"%>
<%@page import="java.util.Map"%>
<%@page import="com.rays.pro4.Util.HTMLUtility"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.List"%>
<%@page import="com.rays.pro4.Util.DataUtility"%>
<%@page import="com.rays.pro4.Util.ServletUtility"%>
<%@page import="com.rays.pro4.controller.TradeListCtl"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<html>
<head>
<link rel="icon" type="image/png"
	href="<%=ORSView.APP_CONTEXT%>/img/logo.png" sizes="16*16" />

<title>Trade List</title>
<link rel="stylesheet"
	href="//code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">


<script src="<%=ORSView.APP_CONTEXT%>/js/jquery.min.js"></script>
<script src="<%=ORSView.APP_CONTEXT%>/js/Checkbox11.js"></script>

<link rel="stylesheet" href="/resources/demos/style.css">
<script src="https://code.jquery.com/jquery-1.12.4.js"></script>
<script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
<script>
	$(function() {
		$("#udate").datepicker({
			changeMonth : true,
			changeYear : true,
			yearRange : '2020:2025',
		//  mindefaultDate : "01-01-1962"
		});
	});
</script>

</head>
<body>
	<jsp:useBean id="bean" class="com.rays.pro4.Bean.TradeBean"
		scope="request"></jsp:useBean>
	<%@include file="Header.jsp"%>


	<form action="<%=ORSView.TRADE_LIST_CTL%>" method="post">

		<center>

			<div align="center">
				<h1>Trade List</h1>
				<h3>
					<font color="red"><%=ServletUtility.getErrorMessage(request)%></font>
					<font color="limegreen"><%=ServletUtility.getSuccessMessage(request)%></font>
				</h3>

			</div>
			<%
				Map map = (Map) request.getAttribute("Trade");
			%>

			<%
		

				int next = DataUtility.getInt(request.getAttribute("nextlist").toString());
			%>


			<%
				int pageNo = ServletUtility.getPageNo(request);
				int pageSize = ServletUtility.getPageSize(request);
				int index = ((pageNo - 1) * pageSize) + 1;

				List list = ServletUtility.getList(request);

				Iterator<TradeBean> it = list.iterator();

				if (list.size() != 0) {
			%>
			<table width="100%" align="center">
				<tr>
					
					<td align="center"><label>User Id</font> : 
					</label> <input type="text" name="userId" placeholder="Enter User Id"
						value="<%=ServletUtility.getParameter("userId", request)%>"></td>

					<td align="center"><label>Transaction Type</font> :
					
					</label>
						<%
							String hlist = HTMLUtility.getList2("transactionType", DataUtility.getStringData(bean.getTransactionType()), map);
						%> <%=hlist%>
				</td>
				<td>
					<label>Start Date</font> :
					</label><input type="text" name="startDate" id="udate"
						readonly="readonly" size="25" placeholder="Enter Date"
						value="<%=ServletUtility.getParameter("startDate", request)%>">
					
					
<!-- 					<td align="center"><label></font> : -->
<!-- 					</label> <input type="text" name="dosage" placeholder="Enter dosage" -->
<%-- 						value="<%=ServletUtility.getParameter("dosage", request)%>"> --%>
					
					




						<input type="submit" name="operation"
						value="<%=TradeListCtl.OP_SEARCH%>"> &nbsp; <input
						type="submit" name="operation"
						value="<%=TradeListCtl.OP_RESET%>"></td>
				</tr>
				
			
			</table>
			<br>

			<table border="1" width="100%" align="center" cellpadding=6px
				cellspacing=".2">
				<tr style="background: skyblue">
					<th><input type="checkbox" id="select_all" name="select">Select
						All</th>

					<th>S.No.</th>
					<th>User Id</th>
					<th>Start Date</th>
					<th>End Date</th>
					<th>Transaction Type</th>
					<th>Edit</th>
				</tr>

				<%
					while (it.hasNext()) {
							bean = it.next();
				%>


				<tr align="center">
					<td><input type="checkbox" class="checkbox" name="ids"
						value="<%=bean.getId()%>"></td>
					<td><%=index++%></td>
					<td><%=bean.getUserId()%></td>
					
					<td><%=bean.getStartDate()%></td>
					<td><%=bean.getEndDate()%></td>
					<td><%= map.get(bean.getTransactionType())%></td>
					<td><a href="TradeCtl?id=<%=bean.getId()%>">Edit</a></td>
				</tr>
				<%
					}
				%>
			</table>

			<table width="100%">
				<tr>
					<th></th>
					<%
						if (pageNo == 1) {
					%>
					<td><input type="submit" name="operation" disabled="disabled"
						value="<%=TradeListCtl.OP_PREVIOUS%>"></td>
					<%
						} else {
					%>
					<td><input type="submit" name="operation"
						value="<%=TradeListCtl.OP_PREVIOUS%>"></td>
					<%
						}
					%>

					<td><input type="submit" name="operation"
						value="<%=TradeListCtl.OP_DELETE%>"></td>
					<td><input type="submit" name="operation"
						value="<%=TradeListCtl.OP_NEW%>"></td>
					<td align="right"><input type="submit" name="operation"
						value="<%=TradeListCtl.OP_NEXT%>"
						<%=(list.size() < pageSize || next == 0) ? "disabled" : ""%>></td>



				</tr>
			</table>
			<%
				}
				if (list.size() == 0) {
			%>
			<td align="center"><input type="submit" name="operation"
				value="<%=TradeListCtl.OP_BACK%>"></td>
			<%
				}
			%>

			<input type="hidden" name="pageNo" value="<%=pageNo%>"> <input
				type="hidden" name="pageSize" value="<%=pageSize%>">

			
		
	
	</form>
	</br>
	</br>
	</br>
	</br>
	</br>
	</br>
	</br>

	</center>

	<%@include file="Footer.jsp"%>
</body>
</html>
