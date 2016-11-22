<%@page import="wa3.model.Documenti"%>
<%@page import="java.util.List"%>
<%@page import="wa3.dao.DocumentiDao"%>
<%@page import="wa3.web.WebFactory"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%!public String pretty(Object value) {
		if (value == null)
			return "";
		return value.toString();
	}%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script type="text/javascript" src="js/jquery-2.1.3.min.js"></script>
<title>Documenti</title>
<script src="js/swal/sweetalert.min.js"></script>
<link rel="stylesheet" type="text/css" href="js/swal/sweetalert.css">

<script type="text/javascript">
	$(document)
			.ready(
					function() {
						$(".btn_save").click(
								function(eventData) {
									var documenti = $(
											"#doc_" + eventData.target.id)
											.val();
									salvaDocumenti(eventData.target.id,
											documenti);
								});

						$(".titolo").css("background-color", "green");

						$(".btnElimina")
								.click(
										function(eventData) {

											swal(
													{
														title : "Sei sicuro?",
														text : "You will not be able to recover this imaginary file!",
														type : "warning",
														showCancelButton : true,
														confirmButtonColor : "#DD6B55",
														confirmButtonText : "Yes, delete it!",
														cancelButtonText : "No, cancel plx!",
														closeOnConfirm : true,
														closeOnCancel : false
													},
													function(isConfirm) {
														if (isConfirm) {
															deleteRiga(eventData);
														} else {
															swal(
																	"Cancelled",
																	"Your imaginary file is safe :)",
																	"error");
														}
													});
										});

					});

	function getServerUrl() {
		var x = getBaseUrl() + "/service?";
		return x;
	}

	function deleteRiga(eventData) {
		var url = getServerUrl() + "cmd=eliminariga&id=" + eventData.target.id;
		console.log(url);
		$.ajax({
			url : url,
			async : true
		}).done(function(result) {
			console.log("result=" + result);
			if (result.result == "Ok") {
				$("#tr_" + eventData.target.id).remove();
				swal("Deleted!", "La riga è stata eliminata.", "success");
				console.log("eliminato:" + eventData.target.id);
			}

		});

	}

	function salvaDocumenti(id, documenti) {
		var url = getServerUrl() + "cmd=salvaDocumenti&id=" + id
				+ "&documenti=" + documenti;
		console.log(url);
		$.ajax({
			url : url,
			async : true
		}).done(function(result) {
			console.log("result=" + result);
			if (result.result == "Ok") {
				$("#tr_" + eventData.target.id).remove();
				swal("Salvato!", "Il campo è stato salvato", "success");

			}

		});

	}

	function getBaseUrl() {
		/*
		 * http://www.refulz.com:8082/index.php#tab2?foo=789
		 *
		 * Property Result ------------------------------------------ host
		 * www.refulz.com:8082 hostname www.refulz.com port 8082 protocol http
		 * pathname index.php href http://www.refulz.com:8082/index.php#tab2 hash
		 * #tab2 search ?foo=789
		 */
		var loc = $(location);
		var res = loc.attr('pathname').split("/");
		var x = loc.attr('protocol') + "//" + loc.attr('host');
		if (res[1] != "") {
			x = x + "/" + res[1];
		}
		console.log(x);
		return x;
	}
</script>
<style type="text/css">
.btnElimina {
	cursor: pointer;
	border: 1px solid gray;
	background-color: red;
	border-radius: 6px;
	padding: 4px;
}

.datiDocumenti, .datiRiga, .valueCell {
	border: 1px solid gray;
	padding: 2px;
}
</style>
</head>
<body>
	<%
		DocumentiDao documentiDao = WebFactory.getDocumentiDao();
		Documenti currentDir = (Documenti) session.getAttribute("currentDir");
		if (currentDir == null) {
			currentDir = documentiDao.getById("root");
			session.setAttribute("currentDir", currentDir);
		}
	%>
	<h1>Files di <%=currentDir.getNome() %></h1>
	<table>
		<thead>
			<tr>
				<th class="titolo"><%="Nome"%></th>
				<th><%="Mime"%></th>
			</tr>
		</thead>
		<tbody>
			<%
				List<Documenti> lista = WebFactory.getFigli(currentDir.getId());
				for (Documenti documenti : lista) {
			%>
			<tr class="datiDocumenti">
				<td class="valueCell"><input type="text"
					id="doc_<%=documenti.getId()%>"
					value="<%=pretty(documenti.getNome())%>"> <input
					type="button" id="<%=documenti.getId()%>" class="btn_save"
					value="salva">
				<td class="valueCell"><%=pretty(documenti.getMime())%></td>
			</tr>

			<%
				}
			%>
		</tbody>
	</table>
</body>
</html>