<?php

// MODIFICARE LA DISPONIBILITA' DI UN CONDUCENTE

$password = $_GET['password'];
$targa = $_GET['targa'];
$stato = $_GET['stato'];  // 1 = si 0 = no



//eseguo la connessione al database sul server locale
//inserendo nome utente e password

$link = new mysqli('localhost', 'uberlikeapp', '', 'my_uberlikeapp');


$selezione = sprintf("SELECT Conducente.Username
FROM   Conducente, Mezzo
WHERE  Mezzo.Targa = '%s'
AND    Conducente.Password = '%s'
AND		Mezzo.Conducente = Conducente.Username",
mysqli_real_escape_string($link,$targa),
mysqli_real_escape_string($link,$password));



$result = mysqli_query($link ,$selezione);


if($result->num_rows) 
{



   // effettuo una richiesta
    $inserimento = "UPDATE In_Servizio SET Disponibile = '".$stato."'
					WHERE Targa='".$targa."'"; 
                    
 
    
	// Esecuzione della query e controllo degli eventuali errori
	if (!$link->query($inserimento)) 
    {
    	die($link->error);
	}
	
    echo "Modifica dello stato avvenuto con successo";
}
else
{
     echo "Accesso rifiutato";
}




?>