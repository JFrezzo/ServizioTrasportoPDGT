<?php


$password = $_GET['password'];
$username = $_GET['username'];

//eseguo la connessione al database sul server locale
//inserendo nome utente e password

$link = new mysqli('localhost', 'uberlikeapp', '', 'my_uberlikeapp');


$selezione = sprintf("SELECT  Gestione.Stato_servizio, Conducente.Username,
					 Servizio.Longitudine_arrivo, Servizio.Latitudine_arrivo,
                     Richiesta.DataOra, Gestione.Costo_servizio
FROM   Cliente, Richiesta, Gestione, Servizio, Mezzo, Conducente
WHERE  Cliente.Username = '%s'
AND    Cliente.`Password` = '%s'
AND    Conducente.Username = Mezzo.Conducente
AND    Mezzo.Targa            = Gestione.Targa
AND    Gestione.Id_servizio      = Servizio.Id
AND    Servizio.Id_richiesta     = Richiesta.Id
AND    Richiesta.Cliente      = Cliente.Username
AND	   Gestione.Stato_servizio BETWEEN '0' AND '1'",
mysqli_real_escape_string($link,$username),
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
