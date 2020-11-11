<?php
error_reporting(E_ALL);
ini_set('display_errors', 1);
include('dbconnect.php');
try{
	$id = (isset($_GET['id']) ? $_GET['id'] : '');
	$stmt = $con->prepare('SELECT * FROM registration where id = :id');
	$stmt->bindParam(':id', $id, PDO::PARAM_STR);

	$result = "";
	$stmt->execute();
	$arr = $stmt->fetchAll(PDO::FETCH_NUM);
	if($arr!=null) {
	foreach($arr as $values){
		$result = $result.$values[0]."&".$values[1]."&".$values[2]."&".$values[3]."%";
	}
	$result = substr($result, 0, -1);
	echo $result;
	} else echo "no_device";
	} catch(PDOException $e){
		echo "error!! ".$e->getMessage();
	}
?>