<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="en">
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>Expert Search</title>

<link rel="stylesheet" href="/search/resources/jquery-ui.css">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.1/jquery.min.js"></script>

<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>

<link rel="stylesheet" href="/search/resources/style.css">
<link rel="stylesheet" href="/search/resources/ui.theme.css">

<script src="https://code.jquery.com/jquery-1.12.4.js"></script>
<script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>

<script>
	var count = 0;
	var templatekey;
	var root = "/cxf/data/";

	$(document).ready(function() {
		populateTemplates();

	});

	function reset() {
		$("#parameters > tbody").empty();
		$("#results").empty();
		count = 0;
	}

	function createRow(idx) {
		var table = $("#parameters");

		table.append('<tr id="row' + idx + '"><td>' + createRemoveControl(idx)
				+ '</td><td>' + createRecordsetControl(idx) + '</td><td>'
				+ createFieldControl(idx) + '</td><td>'
				+ createOperationControl(idx) + '</td><td>'
				+ createValueControl(idx) + '</td><td>'
				+ createMatchCcontrol(idx) + '</td></tr>');

		$("#remove" + idx).click(function() {
			$("#row" + idx).remove();
		})

	}

	function loadOptions() {

		$.ajax({
			url : root + "options/new",
			crossDomain : true,
			success : function(data) {

				reset();

				data.forEach(function(option) {

					createRow(count);

					$.ajax({
						type : "GET",
						url : root + "templates/" + templatekey + "/recordsets",
						async : false,
						success : function(rsData) {
							var rsControl = populateRecordsetControl(count,
									rsData, option.recordset);

							$.ajax({
								type : "GET",
								url : root + "templates/" + templatekey + "/recordsets/"
										+ rsControl.val() + "/fields",
								async : false,
								success : function(fieldData) {

									populateFieldControl(count, fieldData,
											option.field);

									populateOperationControl(count,
											option.comparer);

									populateValueControl(count, option.value);

									if (option.matchCase) {
										$("#match" + count).prop('checked',
												true);
									}

								}
							});
						}
					});

					count++;

				});

				updateStatus("Loaded");
			},

			error : function(jqXHR, status) {
				// error handler
				console.log(jqXHR);
				updateStatus('FAILED' + status.code);
			}
		});

	}

	function getOptions() {
		var options = [];

		var idx = 0;

		for (ii = 0; ii <= count; ii++) {

			if ($("#row" + ii).length) {

				var option = new Object();
				option.recordset = $("#recordset" + ii).find('option:selected')
						.text();
				option.field = $("#field" + ii).find('option:selected').text();
				option.comparer = $("#operation" + ii).find('option:selected')
						.text();
				option.value = $("#value" + ii).val();
				option.matchCase = $("#match" + ii).prop('checked');

				options.push(option);
			}
		}

		return options;
	}

	function updateStatus(msg) {
		$("#status").text(msg);
	}

	function updateTemplateName() {
		$("#templateName").text($("#templates").find('option:selected').text());
	}

	function createRemoveControl(idx) {
		return '<button id="remove' + idx + '" class="ui-button ui-widget ui-corner-all ui-button-icon-only" title="Remove"><span class="ui-icon ui-icon-minusthick"></span>Remove</button>';
	}

	function createRecordsetControl(idx) {
		return '<select id="recordset' + idx + '" class="form-control"></select>';
	}

	function createFieldControl(idx) {
		return '<select id="field' + idx + '" class="form-control" disabled></select>';
	}

	function createOperationControl(idx) {
		return '<select id="operation' + idx + '" class="form-control" disabled></select>';
	}

	function createValueControl(idx) {
		return '<input id="value' + idx + '" class="form-control" disabled></input>';
	}

	function createMatchCcontrol(idx) {
		return '<input id="match' + idx + '" type="checkbox" class="form-control" style="margin: 0" title="Match Case"/>';
	}

	function createOptions(control, data, selected) {

		data.forEach(function(item) {

			if (selected != null && selected == item.name) {
				control.append('<option value="' + item.key + '" selected>'
						+ item.name + '</option>');
			} else {
				control.append('<option value="' + item.key + '">' + item.name
						+ '</option>');
			}

		});

	}

	function populateTemplates() {

		var control = $("#templates");
		control.empty();

		$.ajax({
			url : root + "templates",
		}).then(
				function(data) {

					data.forEach(function(item) {
						control.append('<option value="' + item.key + '">'
								+ item.name + '</option>');
					});

				});
	}

	function populateRecordsetControl(idx, data, selected) {

		// 		alert("Populating Recordset: " + idx);

		var control = $("#recordset" + idx);
		control.empty();
		control.append('<option disabled selected> -- select -- </option>');
		control.change(function(event) {
			$.ajax(
					{
						url : root + "templates/" + templatekey + "/recordsets/" + control.val() + "/fields"
					}).then(function(data) {
				populateFieldControl(idx, data);
			});
		});

		createOptions(control, data, selected);

		return control;
	}

	function populateFieldControl(idx, data, selected) {

		// 		alert("Populating Fields: " + idx);

		var control = $("#field" + idx);
		control.empty();
		control.removeAttr("disabled");
		control.append('<option disabled selected> -- select -- </option>');
		control.change(function(event) {
			populateOperationControl(idx);
			populateValueControl(idx);
		});

		createOptions(control, data, selected);

		return;
	}

	function populateOperationControl(idx, selected) {

		// 		alert("Populating Operation")

		var control = $("#operation" + idx);
		control.empty();
		control.removeAttr("disabled");
		control.append('<option value="1">=</option>');
		control.append('<option value="2">!=</option>');

		// 			$.each(data.d, function (key, value) {
		// 			    control.append(
		// 			        $("<option></option>")
		// 			          .attr("value", value.ReferentTypeID)
		// 			          .text(value.Description)
		// 			    );
		// 			});

		return;
	}

	function populateValueControl(idx, selected) {
		var valueControl = $("#value" + idx);
		valueControl.removeAttr("disabled");
		valueControl.val(selected);
		return;
	}

	function populateResults(data) {

		$("#results").empty();

		var htmlrow;

		htmlrow += "<th>Entity Key</th>";
		htmlrow += "<th>Title</th>";
		htmlrow += "<th>Name</th>";
		htmlrow += "<th>Source</th>";
		htmlrow += "<th>Type</th>";

		$.each(data.result[0].result.hits[0].values, function(key, value) {
			htmlrow += "<th>" + key + "</th>";
		});

		$("#results").append('<tr>' + htmlrow + '</tr>');

		$.each(data.result[0].result.hits, function(key, hit) {

			htmlrow = "<td>" + hit.entityKey + "</td>";
			htmlrow += "<td>" + hit.title + "</td>";
			htmlrow += "<td>" + hit.name + "</td>";
			htmlrow += "<td>" + hit.source + "</td>";
			htmlrow += "<td>" + hit.type + "</td>";

			$.each(hit.values, function(key, value) {
				htmlrow += "<td>" + value + "</td>";
			});

			$("#results").append('<tr>' + htmlrow + '</tr>');

		});
	}

	$(function() {

		var dialog_template, dialog_save, dialog_load;

		dialog_template = $("#dialog-template-form").dialog({
			autoOpen : true,
			height : 250,
			width : 350,
			modal : true,
			buttons : {
				"OK" : function() {
					reset();

					updateTemplateName();

					createRow(count);
					
					templatekey = $("#templates").val();
					
					$.ajax({
						url : root + "templates/" + templatekey + "/recordsets"
					}).then(function(data) {
						populateRecordsetControl(count, data);
					});

					dialog_template.dialog("close");
				},
				Cancel : function() {
					dialog_template.dialog("close");
				}
			},
			close : function() {
				dialog_template.find("form")[0].reset();
			}
		});

		dialog_save = $("#dialog-save-form").dialog({
			autoOpen : false,
			height : 250,
			width : 350,
			modal : true,
			buttons : {
				"Save" : function() {
					dialog_save.dialog("close");

					$.ajax({
						type : "POST",
						url : root + "options/new",
						data : JSON.stringify(getOptions()),// now data come in this function
						contentType : "application/json; charset=utf-8",
						crossDomain : true,
						success : function(data, status, jqXHR) {
							updateStatus("Saved");
						},

						error : function(jqXHR, status) {
							// error handler
							console.log(jqXHR);
							updateStatus('FAILED' + status.code);
						}
					});
				},
				Cancel : function() {
					dialog_save.dialog("close");
				}
			},
			close : function() {
				dialog_save.find("form")[0].reset();
			}
		});

		dialog_load = $("#dialog-load-form").dialog({
			autoOpen : false,
			height : 250,
			width : 350,
			modal : true,
			buttons : {
				"Load" : function() {
					loadOptions();
					dialog_load.dialog("close");
				},
				Cancel : function() {
					dialog_load.dialog("close");
				}
			},
			close : function() {
				dialog_load.find("form")[0].reset();
			}
		});

		$("#template-selection").on("click", function() {
			closeMenu();
			dialog_template.dialog("open");
		});

		$("#save-form").on("click", function() {
			closeMenu();
			dialog_save.dialog("open");
		});

		$("#load-form").on("click", function() {
			closeMenu();
			dialog_load.dialog("open");
		});

		function closeMenu() {
			$('#myNavbar').addClass('collapsed');
			$('.navbar-collapse').removeClass('in').css('height', '0');
		}

		$(".widget input[type=submit], .widget a, .widget button").button();
		$("#submit").click(function(event) {

			$.ajax({
				type : "POST",
				url : root + "search",
				data : JSON.stringify(getOptions()),
				contentType : "application/json; charset=utf-8",
				crossDomain : true,
				success : function(data, status, jqXHR) {
					populateResults(data);
				},

				error : function(jqXHR, status) {
					// error handler
					console.log(jqXHR);
					updateStatus('FAILED' + status.code);
				}
			});

		});
		$("#reset").click(function(event) {
			closeMenu();
			reset();
			createRow(count);
		});
		$("#add").click(function(event) {
			count++;

			createRow(count);

			$.ajax({
				url : root + "templates/" + templatekey + "/recordsets"
			}).then(function(data) {
				populateRecordsetControl(count, data);
			});
		});

	});
</script>

</head>
<body>

    <nav class="navbar navbar-default">
    <div class="container">
        <div class="navbar-header">
            <button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#myNavbar">
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
            <img class="navbar-icon" src="/search/resources/images/InCadence_Logo_Small_200_x_53.png" /> <a id="templateName" class="navbar-brand" href="#"></a>
        </div>
        <div class="collapse navbar-collapse" id="myNavbar">
            <ul class="nav navbar-nav navbar-right">
                <li><a id="template-selection" href="#">Select</a></li>
                <li><a id="load-form" href="#">Load</a></li>
                <li><a id="save-form" href="#">Save</a></li>
                <li><a id="reset" href="#">Reset</a></li>
            </ul>
        </div>
    </div>
    </nav>

    <div class="container-fluid text-center templatecontainer">

        <table id="parameters" class="table">
            <thead>
                <tr>
                    <th width="5%"></th>
                    <th width="25">Recordset</th>
                    <th width="25%">Field</th>
                    <th width="10%">Comparer</th>
                    <th width="30%">value</th>
                    <th width="5%">case</th>
                </tr>
            </thead>
            <tbody>
            </tbody>
            <tfooter>
            <td width="5%"></td>
            <td width="25"></td>
            <td width="25%"></td>
            <td width="10%"></td>
            <td width="30%"></td>
            <td width="5%"></td>
            </tfooter>
        </table>

    </div>
    <div class="container-fluid text-left templatecontainer">
        <table id="results" class="table">
            <thead>
            </thead>
            <tbody>
            </tbody>
        </table>
    </div>

    <div class="container-fluid navbar-fixed-bottom">
        <div class="row-fluid">

            <div class="text-right">
                <span id="status" class="text-left">status</span>
                <button id="add" class="ui-button ui-widget ui-corner-all ui-button-icon-only" title="Add">
                    <span class="ui-icon ui-icon-plusthick"></span>
                    Add
                </button>
                <input id="submit" class="ui-button ui-widget ui-corner-all" type="submit" value="search">
            </div>
        </div>

        <!-- Footer -->
        <footer class="container-fluid bg-4 text-center">
        <p>
            Powered By <a href="#">Coalesce</a>
        </p>
        </footer>
    </div>

    <!-- Select Template Form -->
    <div id="dialog-template-form" title="Select Template">
        <form>
            <fieldset>
                <select id="templates" class="form-control"></select>
                <!-- Allow form submission with keyboard without duplicating the dialog button -->
                <input type="submit" tabindex="-1" style="position: absolute; top: -1000px">
            </fieldset>
        </form>
    </div>

    <div id="dialog-load-form" title="Load Filter Options">
        <p>Place Holder</p>
        <form>
            <fieldset>
                <!-- Allow form submission with keyboard without duplicating the dialog button -->
                <input type="submit" tabindex="-1" style="position: absolute; top: -1000px">
            </fieldset>
        </form>
    </div>

    <div id="dialog-save-form" title="Save Filter Options">
        <p>Place Holder</p>
        <form>
            <fieldset>
                <!-- Allow form submission with keyboard without duplicating the dialog button -->
                <input type="submit" tabindex="-1" style="position: absolute; top: -1000px">
            </fieldset>
        </form>
    </div>

</body>
</html>