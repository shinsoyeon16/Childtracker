<?php
error_reporting(E_ALL);
ini_set('display_errors', 1);
include('dbconnect.php');
try{
	$stmt = $con->prepare("insert into traces (device_serial_number, latitude, longitude) values(:device_serial_number, :latitude, :longitude)");
	$device_serial_number = $_GET['device_serial_number'];
	$latitude = $_GET['latitude'];
	$longitude = $_GET['longitude'];
	$stmt->bindParam(':device_serial_number', $device_serial_number, PDO::PARAM_STR);
	$stmt->bindParam(':latitude', $latitude, PDO::PARAM_STR);
	$stmt->bindParam(':longitude', $longitude, PDO::PARAM_STR);
	$stmt->execute();
	echo "succed";
	} catch(PDOException $e){
		echo "error!! ".$e->getMessage();
	}
?>
