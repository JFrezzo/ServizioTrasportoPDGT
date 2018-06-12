<?php


// MODIFICA LO STATO DEL SERVIZIO (In corso, rifiutato, concluso...)


$targa = $_GET['targa'];
$password = $_GET['password'];
$latitudine = $_GET['latitudine'];
$longitudine = $_GET['longitudine'];




//eseguo la connessione al database sul server locale
//inserendo nome utente e password

$link = new mysqli('localhost', 'uberlikeapp', '', 'my_uberlikeapp');


$selezione = sprintf("SELECT Username
FROM   Conducente, Mezzo
WHERE  Mezzo.Targa = '%s'
AND    Conducente.Password = '%s'
AND	   Mezzo.Conducente = Conducente.Username",
mysqli_real_escape_string($link,$targa),
mysqli_real_escape_string($link,$password));



$result = mysqli_query($link ,$selezione);


if($result->num_rows) 
{



   // effettuo una richiesta
    $inserimento = "UPDATE In_Servizio SET Latitudine = '".$latitudine."', Longitudine = '".$longitudine."'
					WHERE Targa= '".$targa."'"; 
                    
 
    
	// Esecuzione della query e controllo degli eventuali errori
	if (!$link->query($inserimento)) 
    {
    	die($link->error);
	}
	
    echo "Modifica della posizione avvenuta con successo";
}
else
{
     echo "Accesso rifiutato";
}




?>