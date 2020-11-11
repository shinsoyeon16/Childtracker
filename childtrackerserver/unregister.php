<?php
error_reporting(E_ALL);
ini_set('display_errors', 1);
include('dbconnect.php');
try{
	$device_serial_number = (isset($_GET['device_serial_number']) ? $_GET['device_serial_number'] : '');
	$stmt = $con->prepare('delete from registration where device_serial_number = :device_serial_number');
	$stmt->bindParam(':device_serial_number', $device_serial_number, PDO::PARAM_STR);

	$result = "";

	if($stmt->execute()) { // 기기 연결해제 완료.
		echo "succed";
		} else echo "failed";
	} catch(PDOException $e){
		echo "error!! ".$e->getMessage();
	}
?>