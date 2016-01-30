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
            Current Users
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
                      <th>User ID</th>
                      <th>Name</th>
                      <th>Email ID</th>
                      <th>User Status</th>                      
                    </tr>
<?php
	$query = "SELECT `id`,`name`,`email`,`active` FROM `users` ORDER BY `id` ASC";
	$i = 1;
	$query_run = mysqli_query($connection, $query);
	while($query_row = mysqli_fetch_assoc($query_run)){
		echo '<tr>
                      <td>' . $i++ . '</td>
                      <td>USR' . $query_row['id'] . '</td>
                      <td>' . ucwords(trim($query_row['name'])) . '</td>
                      <td>' . trim($query_row['email']) . '</td>';
                if((bool)$query_row['active']){
                	echo '<td><span class="label label-success">Active</span></td>';
                } else {
                	echo '<td><span class="label label-danger">Inactive</span></td>';
                }
                echo  '</tr>';
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