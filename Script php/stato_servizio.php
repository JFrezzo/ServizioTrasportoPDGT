<?php


// MODIFICA LO STATO DEL SERVIZIO (In corso, rifiutato, concluso...)


$targa = $_GET['targa'];
$password = $_GET['password'];
$id = $_GET['id'];
$stato = $_GET['stato'];



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
    $inserimento = "UPDATE Gestione SET Stato_servizio = '".$stato."'
					WHERE Id= '".$id."'"; 
                    
 
    
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