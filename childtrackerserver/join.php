<?php
error_reporting(E_ALL);
ini_set('display_errors', 1);
include('dbconnect.php');
try{
	// stmt1 : 아이디 중복 조회
	// stmt2 : 회원가입
	$id = (isset($_GET['id']) ? $_GET['id'] : '');
	$password = (isset($_GET['password']) ? $_GET['password'] : '');
	$phone_number = (isset($_GET['phone_number']) ? $_GET['phone_number'] : '');
	$name = (isset($_GET['name']) ? $_GET['name'] : '');
	
	$stmt1 = $con->prepare('SELECT user_number FROM user WHERE id = (:id)');
	$stmt2 = $con->prepare("INSERT INTO user (id, password, phone_number, name) VALUES (:id, :password, :phone_number, :name)");
	$stmt1->bindParam(':id', $id, PDO::PARAM_STR);
	$stmt2->bindParam(':id', $id, PDO::PARAM_STR);
	$stmt2->bindParam(':password', $password, PDO::PARAM_STR);
	$stmt2->bindParam(':phone_number', $phone_number, PDO::PARAM_STR);
	$stmt2->bindParam(':name', $name, PDO::PARAM_STR);

	//아이디 중복 검사
	$stmt1->execute();
	$user_number = $stmt1->fetchAll(PDO::FETCH_NUM);
	
	if($user_number==null){ // 아이디 중복이 아닌경우.
		$stmt2->execute(); //회원가입 진행.
		echo "succed";
	} else if($user_number!=null) echo "duplicate_id"; //아이디 중복인 경우.
	else echo "failed1"; // 가입 실패인 경우.
} catch(PDOException $e){
	 echo "failed2".$id.$password.$e->getMessage();
}
?>