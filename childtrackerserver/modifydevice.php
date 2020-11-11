<?php
error_reporting(E_ALL);
ini_set('display_errors', 1);
include('dbconnect.php');
try{
	$device_serial_number = (isset($_GET['device_serial_number']) ? $_GET['device_serial_number'] : '');
	$device_name = (isset($_GET['device_name']) ? $_GET['device_name'] : '');
	$stmt = $con->prepare('update registration set device_name = :device_name where device_serial_number = :device_serial_number');
	$stmt->bindParam(':device_serial_number', $device_serial_number, PDO::PARAM_STR);
	$stmt->bindParam(':device_name', $device_name, PDO::PARAM_STR);

	if($stmt->execute()) { // 기기별칭 수정완료.
		echo "succed";
	}else echo "failed";
	} catch(PDOException $e){
		echo "error!! ".$e->getMessage();
	}
?>