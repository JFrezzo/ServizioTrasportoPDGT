<?php

// LOGIN PER CONDUCENTE CON ATTIVAZIONE DISPONIBILITA'


$link = new mysqli('localhost', 'uberlikeapp', '', 'my_uberlikeapp');



$password = $_POST['password'];
$targa = $_POST['targa'];




if (!$link) {die('Impossibile connettersi: ' . mysql_error());}



 $sql =  sprintf("SELECT Conducente.Username, Mezzo.Targa 
FROM Conducente, Mezzo
WHERE Targa = '%s' 
AND Password = '%s'
AND Mezzo.Conducente = Conducente.Username",
mysqli_real_escape_string($link,$targa),
mysqli_real_escape_string($link,$password)) ;



$result = mysqli_query($link ,$sql);


if($result->num_rows) 
{

   // effettuo una richiesta
    $inserimento = "UPDATE In_Servizio SET Disponibile = '1'
					WHERE Targa= '".$targa."'"; 
                    
 
    
	// Esecuzione della query e controllo degli eventuali errori
	if (!$link->query($inserimento)) 
    {
    	die($link->error);
	}
    
    
    
    echo "true";
}
else
{
	echo "false";
}



?>
