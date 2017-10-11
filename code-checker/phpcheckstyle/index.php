<!DOCTYPE html>
<html>
	<head>
		<title>PHPCheckstyle Web Interface</title>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	</head>

	<body>

		<h1><img src="./html/images/Logo_phpcheckstyle.png"/>&nbsp;PHPCheckstyle</h1>

		<form name="myform" action="runFromWeb.php" method="POST">

			<table>

				<tr>
					<td>File(s) to analyse <span style="color:red">*</span></td>
					<td><input type="text" id="sourceDir" name="sourceDir" value="./test"></input></td>
				</tr>

				<tr>
					<td>Destination directory</td>
					<td><input type="text" id="resultDir" name ="resultDir" value="./checkstyle_result"></input></td>
				</tr>

				<tr>
					<td>Configuration file</td>
					<td><input type="text" id="configFile" name ="configFile" value="default.cfg.xml"></input></td>
				</tr>

				<tr>
					<td>Exclude Files or Directories <span style="color:red">*</span></td>
					<td><input type="text" id="excludeFile" name ="excludeFile" value=""></input></td>
				</tr>

				<tr>
					<td>Language</td>
					<td>
						<input type="select" id="lang" name ="lang">
							<option value="en-us">English</option>
							<option value="fr-fr">Français</option>
						</input>
					</td>
				</tr>

			</table>

			<br/>
			<br/>

			<input type="submit" value="Run"></input>

			<br/>
			<br/>

		</form>

		<i><span style="color:red">*</span> Multiple filenames or directory names can be added, comma separated.</i>

	</body>

</html>
