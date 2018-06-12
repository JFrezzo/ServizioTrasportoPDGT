<?php
// EFFETTUARE UNA NUOVA RICHIESTA DA PARTE DEL CLIENTE


date_default_timezone_set('Europe/Rome');

$data = date("Y-m-d");
$ora =  date("H:i:s");


$username = $_GET['username'];  // username cliente
$password = $_GET['password'];	// password cliente
$budget = $_GET['budget'];		// budget Max
$lat_a = $_GET['lat_a'];		// latitudine arrivo
$lat_p = $_GET['lat_p'];		// latitudine partenza
$lon_a = $_GET['lon_a'];		// longitudine arrivo
$lon_p = $_GET['lon_p'];		// longitudine partenza
$passeggeri = $_GET['passeggeri'];	// numero di passeggeri
$tipo_p = $_GET['tipo_p'];			// tipo di pagamento
$kg = $_GET['kg'];					// peso oggetto


if(($kg == 0 && $passeggeri == 0) || ($kg != 0 && $passeggeri != 0))
	die("Errore: I dati per la richiesta non sono corretti");






//eseguo la connessione al database sul server locale
//inserendo nome utente e password

$link = new mysqli('localhost', 'uberlikeapp', '', 'my_uberlikeapp');



// login

 $sql =  sprintf("SELECT Username, Nome 
FROM Cliente 
WHERE Username = '%s' 
AND Password = '%s';",
mysqli_real_escape_string($link,$username),
mysqli_real_escape_string($link,$password)) ;



$result = mysqli_query($link ,$sql);






if($result->num_rows) 
{
    
    $dataora = "".$data." ".$ora."";
      
      // effettuo una richiesta
    $inserimento = "INSERT INTO Richiesta(Cliente, DataOra) 
					VALUES ('".$username."','".$dataora."')"; 
                    
 
    
	// Esecuzione della query e controllo degli eventuali errori
	if (!$link->query($inserimento)) 
    {
    	die($link->error);
	}
    
    // prelevo l'id della richiesta appena effettuata
    
    $sql =  sprintf("SELECT Id 
  	FROM Richiesta 
  	WHERE Cliente = '".$username."' 
  	AND DataOra = '".$dataora."';");

	$result = mysqli_query($link ,$sql);
	$row = mysqli_fetch_assoc($result);
    
    $id = $row['Id'];  // id_richiesta
    
    
    $pi = "3.141592653";
    
    $km = (6372.795477598*ACOS(
						(SIN($pi * $lat_p / 180)*SIN($pi * $lat_a / 180)) +
						(COS($pi * $lat_p / 180)*COS($pi * $lat_a / 180) * COS(ABS(($pi * $lon_p / 180) - ($pi * $lon_a / 180))) )
					     ) );
    
    // inserisco i dati del servizio richiesto
    
    
    $inserimento_servizio = "INSERT INTO Servizio(Id_richiesta, Budget_max, Ora, Data, longitudine_partenza, latitudine_partenza, N_passeggeri, Km, Tipo_pagamento,  Longitudine_arrivo, Latitudine_arrivo, Peso_Kg)"; 
	$inserimento_servizio.=	"VALUES ('".$id."','".$budget."','".$ora."','".$data."','".$lon_p."','".$lat_p."','".$passeggeri."','".$km."','".$tipo_p."','".$lon_a."','".$lat_a."','".$kg."')"; 
                    
                    
	// Esecuzione della query e controllo degli eventuali errori
	if (!$link->query($inserimento_servizio)) 
    {
    	die($link->error);
	}
    
    
    
    // prelevo l'id del servizio appena richiesto
    
    $sql =  sprintf("SELECT Id 
  	FROM Servizio 
  	WHERE Id_richiesta = '".$id."' 
  	AND Data = '".$data."'
    AND ora = '".$ora."';");

	$result = mysqli_query($link ,$sql);
    
	$row = mysqli_fetch_assoc($result);
    
    $id_servizio = "".$row['Id']."";  // id_servizio
    
    
     // Individuo il conducente più vicino
    

	$pi = "3.141592653";
    
    $sql = sprintf("SELECT Mezzo.Targa, (6372.795477598*ACOS(
						(SIN(".$pi." * ".$lat_p." / 180)*SIN(".$pi." * latitudine / 180)) +
						(COS(".$pi." * ".$lat_p." / 180)*COS(".$pi." * latitudine / 180) * COS(ABS((".$pi." * ".$lon_p." / 180) - (".$pi." * longitudine / 180))) )
					      )
        ) AS distanza_km,
    Conducente.Username, ".$km." * Mezzo.Costo_per_km as Costo_servizio
	FROM In_Servizio, Mezzo, Conducente 
	WHERE Disponibile = 1
    AND ".$km." * Mezzo.Costo_per_km <= ".$budget." 
    AND ".$passeggeri." <= Mezzo.Passeggeri_max
    AND Mezzo.Conducente = Conducente.Username
	AND In_Servizio.Targa = Mezzo.Targa 
	HAVING distanza_km < 50
    ORDER BY distanza_km
    LIMIT 1
 	");
    
    
    
     

	$result = mysqli_query($link ,$sql);
    
    
    
    
    if($result->num_rows) 
	{
    
		$row = mysqli_fetch_assoc($result);
    
    	$targa_mezzo = "".$row['Targa']."";  // Mezzo.Targa
        $costo_servizio = "".$row['Costo_servizio']."";
        
        $inserimento_gestione = "INSERT INTO Gestione(Id_servizio, Targa, Costo_servizio, Stato_servizio) 
					VALUES ('".$id_servizio."','".$targa_mezzo."','".$costo_servizio."','0')"; 
                    
                    
        // Esecuzione della query e controllo degli eventuali errori
        if (!$link->query($inserimento_gestione)) 
        {
            die($link->error);
        }
    
    
    	echo "La richiesta è stata inoltrata al conducente più vicino";
    
    }
    else
    {
    	echo "Non ci sono conducenti disponibili nel raggio di 50 Km";
    }



}
else
{
     echo "Accesso rifiutato";
}




?>
