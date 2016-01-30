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
            Dashboard
            <small>Control panel</small>
          </h1>
          <ol class="breadcrumb">
            <li><a href="#"><i class="fa fa-dashboard"></i> Home</a></li>
            <li class="active">Dashboard</li>
          </ol>
        </section>

        <!-- Main content -->
        <section class="content">
         
          <div class="row">
            <section class="col-lg-8 connectedSortable">
              <div class="box box-widget">
                <div class="box-header with-border">
                  <div class="user-block">
                    <span class="username">3rd Party APIs Used</span>
                  </div>
                  <div class="box-tools">
                    <button class="btn btn-box-tool" data-widget="collapse"><i class="fa fa-minus"></i></button>
                    <button class="btn btn-box-tool" data-widget="remove"><i class="fa fa-times"></i></button>
                  </div>
                </div>
                <div class="box-body">

                  <p>Far far away, behind the word mountains, far from the
                    countries Vokalia and Consonantia, there live the blind
                    texts. Separated they live in Bookmarksgrove right at</p>
                  <p>the coast of the Semantics, a large language ocean.
                    A small river named Duden flows by their place and supplies
                    it with the necessary regelialia. It is a paradisematic
                    country, in which roasted parts of sentences fly into
                    your mouth.</p>

                </div>
              </div>
            </section>
            
            <section class="col-lg-4 connectedSortable">
            <div class="col-lg-12 col-xs-24">
	      <div class="small-box bg-yellow">
                <div class="inner">
                  <h3>5</h3>
                  <p>User Registrations</p>
                </div>
                <div class="icon">
                  <i class="ion ion-person-add"></i>
                </div>
                <a href="users.php" class="small-box-footer">
                  More info <i class="fa fa-arrow-circle-right"></i>
                </a>
              </div>
            </div>
            <div class="col-lg-12 col-xs-24">
              <div class="small-box bg-aqua">
                <div class="inner">
                  <h3>11</h3>
                  <p>Cities</p>
                </div>
                <div class="icon">
                  <i class="ion ion-pie-graph"></i>
                </div>
                <a href="cities.php" class="small-box-footer">More info <i class="fa fa-arrow-circle-right"></i></a>
              </div>
            </div>
            </section>
          </div>

        </section>
      </div>
    </div>

<?php
	include 'inc/scripts.inc.php';
?>

  </body>
</html>