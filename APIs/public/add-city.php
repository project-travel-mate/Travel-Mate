<?php
	require '../inc/connection.inc.php';
	require '../inc/function.inc.php';
	
	$color = ['danger', 'success'];
	$message = ['Could not add your city', 'Success'];
	$icon = ['ban', 'check'];

function getcoordinates($a){
	$address = urlencode($a);
	$link = "http://maps.google.com/maps/api/geocode/json?address=".$address."&sensor=false";
	$response = json_decode(curl_URL_call($link), true);
		
	if($response['status'] == "OK") {
		$lat = (float)$response['results'][0]['geometry']['location']['lat'];
		$lng = (float)$response['results'][0]['geometry']['location']['lng'];
		return $lat . "-" . $lng;
	} else {
		return null;
	}
}

	if(isset($_POST['submit'])){
		$city_name = trim(strtolower($_POST['city']));
		$city_desc = addslashes(trim($_POST['description']));
		
		$coordinates = explode('-', getcoordinates($city_name));
		
		$lat = $coordinates[0];
		$lng = $coordinates[1];
		
		$query = "INSERT INTO `cities` (`city_name`, `description`, `lat`, `lng`) VALUES ('$city_name','$city_desc','$lat','$lng')";
		if(mysqli_query($connection, $query)){
			$success = true;
		} else {
			$success = false;
			$message[0] =  mysqli_error($connection);
		}
	}

	
?>

<!DOCTYPE html>
<html>
  <head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">

<?php
	include 'inc/style.inc.php';
?>

  </head>
  <body class="hold-transition skin-blue sidebar-mini">
    <div class="wrapper">

<?php
	include 'inc/header-front.inc.php';
	include 'inc/left-panel-front.inc.php';
?>      

      <div class="content-wrapper">
        <section class="content-header">
          <h1>
            Add City
            <small>Control panel</small>
          </h1>
        </section>

        <section class="content">
	  <div class="row">
            <div class="col-xs-12">
<?php

	if(isset($success)){
		echo '<div class="alert alert-' . $color[$success] . ' alert-dismissable">
                    <button type="button" class="close" data-dismiss="alert" aria-hidden="true">×</button>
                    <h4><i class="icon fa fa-' . $icon[$success] . '"></i> Alert!</h4>' . $message[$success] . '</div>';
        }
?>
            
              <div class="box box-primary">
                <form role="form" method="POST">
                  <div class="box-body">
                    <div class="form-group">
                      <label>Name *</label>
                      <input type="text" class="form-control" name="city" placeholder="Enter the name of the city" required>
                    </div>
                    <div class="form-group">
                      <label>Description *</label>
                      <textarea required class="form-control" name="description" cols=3 placeholder="Enter some information about the city"></textarea>
                    </div>
                  </div>
                  <div class="box-footer">
                    <button name="submit" type="submit" class="btn btn-primary">Add City</button>
                  </div>
                </form>
              </div>
            </div>
          </div>
        </section>
      </div>
    </div>

<?php
	include 'inc/scripts.inc.php';
?>

  </body>
</html>