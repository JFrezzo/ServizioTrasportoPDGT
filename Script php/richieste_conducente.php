<?php

// MOSTRA LE NUOVE RICHIESTE PER IL CONDUCENTE INSIEME A QUELLE CHE HA GIA' ACCETTATO

$password = $_GET['password'];
$targa = $_GET['targa'];

//eseguo la connessione al database sul server locale
//inserendo nome utente e password

$link = new mysqli('localhost', 'uberlikeapp', '', 'my_uberlikeapp');


$selezione = sprintf("SELECT  Gestione.Stato_servizio,
					 Servizio.Longitudine_arrivo, Servizio.Latitudine_arrivo, 
                     Servizio.Latitudine_partenza, Servizio.Longitudine_partenza,
                     Servizio.Data, Servizio.Ora, Gestione.Costo_servizio, Gestione.Id
                    FROM   Cliente, Richiesta, Gestione, Servizio, Mezzo, Conducente
                    WHERE  Mezzo.Targa = '%s'
                    AND    Conducente.Password = '%s'
                    AND    Conducente.Username = Mezzo.Conducente
                    AND    Mezzo.Targa            = Gestione.Targa
                    AND    Gestione.Id_servizio      = Servizio.Id
                    AND    Servizio.Id_richiesta     = Richiesta.Id
                    AND    Richiesta.Cliente      = Cliente.Username
                    AND	   Gestione.Stato_servizio BETWEEN '0' AND '1'",
                    mysqli_real_escape_string($link,$targa),
                    mysqli_real_escape_string($link,$password));



$result = mysqli_query($link ,$selezione);


if($result->num_rows) 
{
	while($row = mysqli_fetch_array($result))
	{
	
	$output[] = $row ;

	}



print(json_encode($output));



}
else
{
     echo "Accesso rifiutato";
}




?>
