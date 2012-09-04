<?xml version="1.0" encoding="UTF-8" ?>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page import="org.nihul5.other.CONST"%>

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<link rel="stylesheet" type="text/css" rel="stylesheet/index" href="/<%=CONST.WEBAPP_NAME %>/styles/style.css" />
<script type="text/javascript" src="http://maps.googleapis.com/maps/api/js?key=<%=CONST.GOOGLE_APIKEY%>&sensor=false&libraries=geometry"></script>
<script type="text/javascript" src="/<%=CONST.WEBAPP_NAME%>/scripts/jquery.js"></script>
<script type="text/javascript" src="/<%=CONST.WEBAPP_NAME%>/scripts/map.js"></script>


<script type="text/javascript" src="/<%=CONST.WEBAPP_NAME%>/scripts/geoxml3.js"></script>
<script type="text/javascript" src="/<%=CONST.WEBAPP_NAME%>/scripts/ProjectOverlay.js"></script>



<script type="text/javascript">

	var toremove= '<?xml version="1.0" encoding="UTF-8"?>';
	var lastParser = null;

	$(document).ready(function() {
		$('#xml_button').click(function() {
		    window.location.href = '/<%=CONST.WEBAPP_NAME %>/XMLExport';
		});
		
		$('#export_post_button').click(function() {
			window.location.href ='/<%=CONST.WEBAPP_NAME %>/KMLExport/<%=CONST.KML_EXPORT_POST%>';
		});
		
		$('#export_event_button').click(function() {
			window.location.href ='/<%=CONST.WEBAPP_NAME %>/KMLExport/<%=CONST.KML_EXPORT_EVENT%>';
		});
		
		$('#export_path_button').click(function() {
			window.location.href ='/<%=CONST.WEBAPP_NAME %>/KMLExport/<%=CONST.KML_EXPORT_PATH%>';
		});
		

		$('#render_kml').click(function(event) {
			var files = document.getElementById('files').files;
	    	if (!files.length) {
    	  		alert('Please select a file!');
      			return;
    		}
	
		    var file = files[0];
		    var reader = new FileReader();

		    // If we use onloadend, we need to check the readyState.
		    reader.onloadend = function(evt) {
		      	if (evt.target.readyState == FileReader.DONE) { // DONE == 2
		      		var kmlstr = evt.target.result.replace(toremove, '');
		      		if (lastParser != null) 
		      			lastParser.hideDocument();
		      		lastParser = new geoXML3.parser({map: map});
					lastParser.parseKmlString(kmlstr);
		      	}
	    	};

		    reader.readAsBinaryString(file);
		 });
	});

</script>


<title>Export Messages</title>
</head>
<body>
	<%@ include file="/jsp/status_bar.jsp" %>
	<%@ include file="/jsp/menu.jsp"%>
	<div id="container">
		<div id="map_canvas" class="left"></div>
		<div id="content" class="right" align="center">
			<div class="center_box_colorless">
				<button class="cool_button" id="xml_button" type="button">Export all messages to XML</button>
			</div>
			<div class="center_box_colorless">
				<button class="cool_button" id="export_post_button" type="button">Export Posts to KML</button>
				<button class="cool_button" id="export_event_button" type="button">Export Events to KML</button>
				<button class="cool_button" id="export_path_button" type="button">Export Path to KML</button>
			</div>
			
			<div class="center_box_colorless">
				<input type="file" id="files" name="file" />
				<input type="button" class="cool_button2" id="render_kml" value="Render KML on map"/>
			</div>
		</div>
	</div>

	<%@ include file="/jsp/footer.jsp" %>
</body>
</html>
