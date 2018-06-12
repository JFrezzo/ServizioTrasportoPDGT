

# ServizioTrasportoPDGT

## Consegna
Appello: Sessione Estiva A.A 2017/2018

Nome: Jacopo Frezzotti

## Descrizione Servizio

Servizio che consente, tramite un app android, all'utente di richiedere un servizio di trasporto al conducente più vicino in un raggio prestabilito di km (50). Una volta effettuata la richiesta, nell'app android per conducenti sarà possibile vedere la richiesta e decidere se accettarla o meno.

## API

Per consentire il corretto svolgimento della piattaforma sono presenti delle richieste GET e POST.

#### Richieste GET:
###### 1)Aggiornare la posizione del Conducente in servizio con un suo mezzo: http://uberlikeapp.altervista.org/coordinate.php

Parametri richiesti:

-targa = targa del mezzo che il conducente sta utilizzando

-password = password account conducente

-latitudine = latitudine 

-longitudine = longitudine

###### 2)Modificare la disponibilità di un conducente: http://uberlikeapp.altervista.org/in_servizio.php

Parametri richiesti:

-targa = targa del mezzo che il conducente sta utilizzando

-password = password account conducente

-stato = 1(il conducente diventa in servizio), (0 = il conducente non è più in servizio)

###### 3)Effettuare una nuova richiesta da parte del cliente: http://uberlikeapp.altervista.org/nuova_richiesta.php

Parametri richiesti:

-username = username cliente

-password = password cliente

-budget = budget Max

-lat_a = latitudine arrivo

-lat_p = latitudine partenza

-lon_a = longitudine arrivo

-lon_p = longitudine partenza

-passeggeri = numero di passeggeri

-tipo_p = tipo di pagamento

-kg =  peso oggetto


###### 4)Mostra le nuove richieste e quelle già accettate di un conducente: http://uberlikeapp.altervista.org/richieste_conducente.php

Parametri richiesti:

-targa = targa conducente

-password = password conducente


###### 5)Mostra le richieste di un cliente che non sono ancora concluse: http://uberlikeapp.altervista.org/richieste_non_concluse.php

Parametri richiesti:

-username = username cliente

-password = password cliente


###### 6)Modifica lo stato di una determinata richiesta: http://uberlikeapp.altervista.org/stato_servizio.php

Parametri richiesti:

-targa = targa conducente

-password = password conducente

-id = id richiesta

-stato =  0 = ne accettato ne rifiutato, 1 = accettato, 2 = rifiutato, 3 = concluso


#### Richieste POST:
###### 1)Login cliente: http://uberlikeapp.altervista.org/login.php

Parametri richiesti:

-username = username cliente

-password = password cliente

-tipo = 0(Cliente)

###### 2)Login conducente con attivazione disponibilità mezzo: http://uberlikeapp.altervista.org/login_conducente.php

Parametri richiesti:

-targa = targa conducente

-password = password conducente



## Client Android

#### Cliente

L'applicazione android lato cliente è composta da un activity di autenticazione, la quale consente il login nell'app per iniziare ad utilizzare il servizio. La sessione all'interno dell'applicazione è gestita mediante le SharedPreferences, in cui sono memorizzati l'username e la password del cliente.
All'interno dell'app è presente un menù di navigazione laterale nel quale sarà possibile effettuare una nuova richiesta(sia per passeggeri che per oggetti), visualizzare le richieste non ancora concluse e effettuare il logout dall'applicazione.
Per gestire questo tipo di menù sono stati utilizzati anche dei Fragment per rendere l'app dinamica e moderna.
Nel fragment per richiedere un servizio si dovrà scegliere se effettuare una richiesta normale o per trarsporto di oggetti, una volta scelto comparirà un activity dove sarà possibile compilare la richiesta ed inoltrarla ad il conducente più vicino mediante lettura GPS del dispositivo.
Nel fragment per le richieste non ancora conluse sarannò mostrate semplicemente tutte le richieste effettuate di cui il conducente ha accettato o ancora non risposto.


#### Conducente

L'applicazione android lato conducente ha un layout simile a quello dell'app per clienti.
Anche questa è composta da un activity per il login nel quale verrà specificata la targa del mezzo con cui si vuole entrare in servizio e la password dell'account del conducente in questione.
Una volta autenticati saranno disponibili 3 opzioni nel menù laterale.
Una è quella che consente di vedere le nuove richieste o le richieste già accettate. Se si seleziona una richiesta non ancora accettata si sarà reindirizzati in un activity che mostra la posizione del cliente e sarà possibile accettare o rifiutare la richiesta.
Un altra è quella che consente di aggiornare la posizione del conducente tramite appunto lettura del GPS del dispositivo.
Infine è presente un activity di logout nel quale sarà possibile effettuare il logout dall'applicazione che imposterà a non in servizio il mezzo corrente.

###### Nota:
Nell' Android Manifest dell'app per conducenti dovrà essere inserita nel campo value dell'api key per la mappa, una api key registrata in precedenza tramite la google api console.

