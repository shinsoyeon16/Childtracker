<?php
error_reporting(E_ALL);
ini_set('display_errors', 1);
include('dbconnect.php');
try{
	$stmt = $con->prepare("insert into test (b) values(:data)");
	$data = $_GET['data']." / ".date('Y-m-d H:i:s');
	$stmt->bindParam(':data', $data, PDO::PARAM_STR);
	$stmt->execute();
	//$result = $stmt->fetchAll(PDO::FETCH_NUM);
	echo $data;
} catch(PDOException $e){
		echo "error!! ".$e->getMessage();
}
//echo "<script>console.log('test result : ".$data."');</script>";
?>
