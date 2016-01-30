<?php
	require '../inc/connection.inc.php';
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
            Current Cities
            <small>Control panel</small>
          </h1>
        </section>

        <section class="content">
	  <div class="row">
            <div class="col-xs-12">
              <div class="box">
                <div class="box-body table-responsive no-padding">
                  <table class="table table-hover table-bordered">
                    <tbody><tr>
                      <th>#</th>
                      <th>Name</th>
                      <th>Description</th>
                      <th>Map Link</th>
                    </tr>
<?php
	$query = "SELECT * FROM `cities` ORDER BY `city_name` ASC";
	$i = 1;
	$query_run = mysqli_query($connection, $query);
	while($query_row = mysqli_fetch_assoc($query_run)){
		echo '<tr>
                      <td>' . $i++ . '</td>
                      <td>' . ucwords(trim($query_row['city_name'])) . '</td>
                      <td>' . trim($query_row['description']) . '</td>
                      <td><a href="http://maps.google.com/?q=' . (float)$query_row['lat'] . ',' . (float)$query_row['lng'] . '"><span class="label label-primary">View on Map</span></a></td>
                    </tr>';
	}
?>                    
                  </tbody></table>
                </div>
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