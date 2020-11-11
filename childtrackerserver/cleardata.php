<?php
error_reporting(E_ALL);
ini_set('display_errors', 1);
include('dbconnect.php');
try{
	$device_serial_number = (isset($_GET['device_serial_number']) ? $_GET['device_serial_number'] : '');
	$stmt = $con->prepare('delete from traces where device_serial_number = :device_serial_number');
	$stmt->bindParam(':device_serial_number', $device_serial_number, PDO::PARAM_STR);

	if($stmt->execute()) { // 데이터 초기화 완료.
		echo "succed";
		} else echo "failed";
	} catch(PDOException $e){
		echo "error!! ".$e->getMessage();
	}
?>