<?php
error_reporting(E_ALL);
ini_set('display_errors', 1);
include('dbconnect.php');
try{
	//stmt1 : 중복등록된 기기인지 확인
	//stmt2 : 기기번호가 유효한 번호인지 확인
	//stmt3 : 기기 등록정보를 db에 저장
	$id = (isset($_GET['id']) ? $_GET['id'] : '');
	$device_serial_number = (isset($_GET['device_serial_number']) ? $_GET['device_serial_number'] : '');
	$device_name = (isset($_GET['device_name']) ? $_GET['device_name'] : '');
	$stmt1 = $con->prepare('select * from registration where device_serial_number = :device_serial_number');
	$stmt2 = $con->prepare('select * from device where device_serial_number = :device_serial_number');
	$stmt3 = $con->prepare('INSERT INTO registration (id, device_serial_number, device_name) VALUES (:id, :device_serial_number, :device_name)');
	$stmt1->bindParam(':device_serial_number', $device_serial_number, PDO::PARAM_STR);
	$stmt2->bindParam(':device_serial_number', $device_serial_number, PDO::PARAM_STR);
	$stmt3->bindParam(':id', $id, PDO::PARAM_STR);
	$stmt3->bindParam(':device_name', $device_name, PDO::PARAM_STR);
	$stmt3->bindParam(':device_serial_number', $device_serial_number, PDO::PARAM_STR);

	$stmt1->execute();
	$result = $stmt1->fetchAll(PDO::FETCH_NUM);
	if($result != null) echo "duplicate_serial";
	else {
		$stmt2->execute();
		$result2 = $stmt2->fetchAll(PDO::FETCH_NUM);
		if($result2 != null) {
			if($stmt3->execute()) echo "succed";
		} else echo "failed";
	}
	} catch(PDOException $e){
		echo "error!! ".$e->getMessage();
	}
?>