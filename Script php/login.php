<?php

// LOGIN 


$link = new mysqli('localhost', 'uberlikeapp', '', 'my_uberlikeapp');


$username = $_POST['username'];
$password = $_POST['password'];
$tipo = $_POST['tipo'];


if($tipo == 0)
	$utente = "Cliente";
else if($tipo ==  1)
	$utente = "Conducente";
else
	die('Errore');




if (!$link) {die('Impossibile connettersi: ' . mysql_error());}



 $sql =  sprintf("SELECT Username, Nome 
FROM ".$utente."
WHERE Username = '%s' 
AND Password = '%s';",
mysqli_real_escape_string($link,$username),
mysqli_real_escape_string($link,$password)) ;



$result = mysqli_query($link ,$sql);


if($result->num_rows) 
{
    echo "true";
}
else
{
	echo "false";
}



?>
