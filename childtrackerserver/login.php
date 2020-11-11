<?php
error_reporting(E_ALL);
ini_set('display_errors', 1);
include('dbconnect.php');
try{
	$id = (isset($_GET['id']) ? $_GET['id'] : '');
	$password = (isset($_GET['password']) ? $_GET['password'] : '');
	$stmt = $con->prepare("SELECT * FROM user WHERE id = :id");
	$stmt->bindParam(':id', $id, PDO::PARAM_STR);
	$stmt->execute();
	$result = $stmt->fetchAll(PDO::FETCH_NUM);
	if ($result!=null && $result[0][2]===$password) echo "succed%".date("Y-m-d");
	else echo "failed";
	} catch(PDOException $e){
		echo "error!! ".$e->getMessage();
	}
?>