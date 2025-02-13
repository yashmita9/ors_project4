<%@page import="com.rays.pro4.controller.TradeCtl"%>
<%@page import="com.rays.pro4.Util.HTMLUtility"%>
<%@page import="com.rays.pro4.Util.DataUtility"%>
<%@page import="java.util.Map"%>
<%@page import="com.rays.pro4.Util.ServletUtility"%>
<%@page import="com.rays.pro4.controller.ORSView"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<link rel="icon" type="image/png"
	href="<%=ORSView.APP_CONTEXT%>/img/logo.png" sizes="16*16" />
<title>Trade Page</title>

<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet"
	href="//code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">
<link rel="stylesheet" href="/resources/demos/style.css">
<script src="https://code.jquery.com/jquery-1.12.4.js"></script>
<script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
<script src="<%=ORSView.APP_CONTEXT%>/js/Utilities.js"></script>
<script>
	$(function() {
		$("#udatee").datepicker({
			changeMonth : true,
			changeYear : true,
			yearRange : '2024:2026',
		});
	});
	$(function() {
		$("#udate").datepicker({
			changeMonth : true,
			changeYear : true,
			yearRange : '2024:2026',
		});
	});
</script>

</head>


<body>
	<jsp:useBean id="bean" class="com.rays.pro4.Bean.TradeBean"
		scope="request"></jsp:useBean>
	<%@ include file="Header.jsp"%>
	<div align="center">

		<form action="<%=ORSView.TRADE_CTL%>" method="post">




			<h1>

				<%
					if (bean != null && bean.getId() > 0) {
				%>
				<tr>
					<th><font size="5px"> Update Trade </font></th>
				</tr>
				<%
					} else {
				%>
				<tr>
					<th><font size="5px"> Add Trade </font></th>
				</tr>
				<%
					}
				%>
			</h1>

			<h3>
				<font color="red"> <%=ServletUtility.getErrorMessage(request)%></font>
				<font color="limegreen"> <%=ServletUtility.getSuccessMessage(request)%></font>
			</h3>

			<%
				Map map = (Map) request.getAttribute("Trade");
			%>

			<table>
				<tr>
					<input type="hidden" name="id" value="<%=bean.getId()%>">
					<th align="left">User Id<span style="color: red">*</span>:
					</th>
					<td><input type="text" name="userId"
						placeholder="Enter Your User Id" size="26"
						value="<%=(DataUtility.getStringData(bean.getUserId()))%>"></td>

					<td style="position: fixed"><font color="red"><%=ServletUtility.getErrorMessage("userId", request)%></font></td>

				</tr>

				<th style="padding: 3px"></th>
				</tr>

				<tr>
					<th align="left">Start Date <span style="color: red">*</span>
						:
					</th>
					<td><input type="text" name="startDate"
						placeholder="Enter Start Date" size="26" readonly="readonly"
						id="udatee"
						value="<%=DataUtility.getDateString(bean.getStartDate())%>"></td>
					<td style="position: fixed;"><font color="red"> <%=ServletUtility.getErrorMessage("startDate", request)%></font></td>
				</tr>

				<tr>
					<th style="padding: 3px"></th>
				</tr>
				<tr>
					<th align="left">End Date <span style="color: red">*</span> :
					</th>
					<td><input type="text" name="endDate"
						placeholder="Enter End Date" size="26" readonly="readonly"
						id="udate"
						value="<%=DataUtility.getDateString(bean.getEndDate())%>"></td>
					<td style="position: fixed;"><font color="red"> <%=ServletUtility.getErrorMessage("endDate", request)%></font></td>
				</tr>

				<tr>
					<th style="padding: 3px"></th>
				</tr>
				<tr>
					<th align="left">Transaction Type<span style="color: red">*</span>
						:
					</th>
					<td>
						<%
							String hlist = HTMLUtility.getList2("transactionType", DataUtility.getStringData(bean.getTransactionType()), map);
						%> <%=hlist%>
					</td>
					<td style="position: fixed"><font color="red"> <%=ServletUtility.getErrorMessage("transactionType", request)%></font></td>
				</tr>



				<tr>
					<th style="padding: 3px"></th>
				</tr>



				<tr>
					<th></th>
					<%
						if (bean.getId() > 0) {
					%>
					<td colspan="2">&nbsp; &emsp; <input type="submit"
						name="operation" value="<%=TradeCtl.OP_UPDATE%>"> &nbsp;
						&nbsp; <input type="submit" name="operation"
						value="<%=TradeCtl.OP_CANCEL%>"></td>

					<%
						} else {
					%>

					<td colspan="2">&nbsp; &emsp; <input type="submit"
						name="operation" value="<%=TradeCtl.OP_SAVE%>"> &nbsp;
						&nbsp; <input type="submit" name="operation"
						value="<%=TradeCtl.OP_RESET%>"></td>

					<%
						}
					%>
				</tr>
			</table>
		</form>
	</div>

	<%@ include file="Footer.jsp"%>

</body>
</html>