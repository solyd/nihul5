<%@ page import="org.nihul5.other.CONST"%>


<form id="location_form" action="javascript:onLocationSearch()">
	<table align="center">
		<tr>
			<td colspan=2 align="center"><h3>Search by location</h3></td>
		</tr>
		<tr>
			<td colspan=2 align="center" height="10px"></td>
		</tr>
		<tr>
			<td>Latitude (degrees):</td>
			<td><input id="lat" type="text" name="<%=CONST.MSG_LATITUDE%>"
				value=""/></td>
		</tr>
		<tr>
			<td>Longitude (degrees):</td>
			<td><input id="lng" type="text" name="<%=CONST.MSG_LONGITUDE%>"
				value=""/></td>
		</tr>
		<tr>
			<td>Search radius (km):</td>
			<td><input id="radius" type="text" name="<%=CONST.RADIUS%>"
				value=""/></td>
		</tr>

		<tr>
			<td colspan=2 align="center" height="10px"></td>
		</tr>

		<tr>
			<td></td>
			<td><input type="submit" name="Submit"
				value="Search by location" /></td>
		</tr>
	</table>
</form>

<br />
<br />

<form id="keywords_form" action="javascript:onKeywordsSearch()">
	<table align="center">
		<tr>
			<td colspan=2 align="center"><h3>Search by keywords</h3></td>
		</tr>
		<tr>
			<td colspan=2 align="center" height="10px"></td>
		</tr>
		<tr>
			<td>Keywords:</td>
			<td><input id="lat" type="text" name="<%=CONST.KEYWORDS %>" value=""></td>
		</tr>
		<tr>
			<td><i>(seperated by spaces)</i></td>

		</tr>
		<tr>
			<td colspan=2 align="center" height="10px"></td>
		</tr>

		<tr>
			<td></td>
			<td><input type="submit" name="Submit" value="Search by keywords" /></td>
		</tr>
	</table>
</form>

