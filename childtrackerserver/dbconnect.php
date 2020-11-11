<?php
try{
	$con = new PDO('mysql:host=localhost;port=3307;dbname=childtracker;charset=utf8', 'soyeon', '1234');
}catch(PDOException $e){
	die("Failed to connect to the database: ".$e->getMessage());
}
$con->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
$con->setAttribute(PDO::ATTR_DEFAULT_FETCH_MODE, PDO::FETCH_ASSOC);

header('Content-Type: text/html; charset=utf-8');
?>