<?php
error_reporting(E_ALL);
ini_set('display_errors', 1);
include('dbconnect.php');
try{
	$stmt = $con->prepare("SELECT * FROM traces where DATE(record_time)=(:date) and device_serial_number in (select device_serial_number from registration where id = (:id))");
	$id = (isset($_GET['id']) ? $_GET['id'] : '');
	$date = (isset($_GET['date']) ? $_GET['date'] : '');
	$stmt->bindParam(':id', $id, PDO::PARAM_STR);
	$stmt->bindParam(':date', $date, PDO::PARAM_STR);
	$stmt->execute();
	$arr = $stmt->fetchAll(PDO::FETCH_NUM);
	$stmt2 = $con->prepare('SELECT device_serial_number, device_name FROM registration WHERE id = :id');
	$stmt2->bindParam(':id', $id, PDO::PARAM_STR);
	$stmt2->execute();
	$devices = $stmt2->fetchAll(PDO::FETCH_NUM);

	$result="";
	if($devices == null){ 
		echo "no_device";
	} else if($arr!=null){
		foreach($devices as $values){
			$result = $result.$values[0]."&".$values[1]."#";
		}
		foreach($arr as $values2){
			$result = $result.$values2[0]."&".$values2[1]."&".$values2[2]."&".$values2[3]."%";
		}
		$result = substr($result, 0, -1);
		echo $result;
	} else echo "no_data";
} catch(PDOException $e){
	echo "error!! ".$e->getMessage();
}
?>
