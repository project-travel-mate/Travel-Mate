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
            Add User
            <small>Control panel</small>
          </h1>
        </section>

        <section class="content">
	  <div class="row">
            <div class="col-xs-12">
              <div class="box box-primary">
                <form role="form">
                  <div class="box-body">
                    <div class="form-group">
                      <label>Name *</label>
                      <input type="text" class="form-control" placeholder="Enter the name of user">
                    </div>
                    <div class="form-group">
                      <label>Email ID *</label>
                      <input type="email" class="form-control" placeholder="Enter a valid and functional email ID of user">
                    </div>
                    <div class="form-group col-md-6">
                      <label>Password *</label>
                      <input type="password" class="form-control" placeholder="Password">
                    </div>
                    <div class="form-group col-md-6">
                      <label>Confirm Password *</label>
                      <input type="password" class="form-control" placeholder="Re Enter password">
                    </div>
                  </div>
                  <div class="box-footer">
                    <button type="submit" class="btn btn-primary">Submit</button>
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