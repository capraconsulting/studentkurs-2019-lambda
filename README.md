# Studentkurs 2019

I dette kurset skal vi sette opp en webapplikasjon med lagring til database med Amazon Web Services. Webapplikasjonen er en enkel sak for å opprette, endre, slette ogse på events. Den er skrevet i TypeScript med React.

## Oppsett av utviklingsmiljø lokalt

Sørg for at du har gjort det som behøves på forhånd. Se PDF som ble sendt ut før kurset. Hvis du ikke har fått den, ligger den [her]()
Presentasjonen som Capra holdt kan du finne [her]().

Når du har gjort det som må gjøres på forhånd, kan du fortsette med følgende:

1. Klone dette repoet, `git clone https://github.com/capraconsulting/studentkurs-2019`.
2. Klone [webapp](https://github.com/capraconsulting/studentkurs-2019-webapp) og følg instruksjonene der for å sette den opp

Når dette er gjort er du klar til å sette i gang.

## Amazon Web Services (AWS)

Amazon Web Services er en rekke tjenester for ulike formål. Man har tjenester for å kjøre enkle virtuelle servere, tjenester for å sende e-post, prosessere enorme datamengder, lagring i ulike typer databaser, lagring av filer og lagring av statisk innhold til nettsider, for å nevne noe.

For å kunnee bruke AWS trenger man en konto. Man får mye gratis, slik at man kan komme i gang for en rimelig penge. Dersom du mangler konto, kan du se på instruksjonene vi sendte ut på forhånd for å få hjelp.

Man har flere forskjellige måter å jobbe med AWS på. Man kan bruke nettleseren, man kan lage maler som definerer hvordan ting skal fungere, eller man kan bruke kommandolinjen.

I dette kurset skal vi først sette opp tjenester med nettleseren, i den såkalte konsollen, og så oppdatere innholdet ved hjelp av AWS sitt CLI (Command Line Interface).

## Vi setter opp AWS-tjenester for applikasjonen vår

En vanlig webapplikasjon har tre lag: En frontend, en backend og en database. Frontenden er det vi ser, brukegrensesnittet. Backend har logikk og håndterer henvendelser fra frontend. Databasen lagrer data. Denne enkle figuren viser hvordan det kan se ut:

![En enkel applikasjon med en backend, frontend og database.](images/simple-app-arch.png)

Når vi kjører dette, enten lokalt eller på en server, pleier man å kjøre tre ting: En webpplikasjon (frontend), en backendapplikasjon (for eksempel skrevet i Java) og en database (som Postgres eller MySQL). Da må man passe på at serveren, eller egen maskin, er oppdatert og tettet for alle sikkerhetshull.

Når vi jobber med AWS, kan vi i stedet bruke tjenester som er spesielt designet for det vi ønsker å gjøre, være seg å kjøre backendkode eller vise en webapplikasjon. Man kan kalle det å velge tjenester ut fra man ønsker å oppnå "Architecting for the Cloud". I dette kurset vil vi ha en arkitektur som ser slik ut:

![En enkel arkitektur med appen i AWS](images/AWS-app-arch.png)

| Tjeneste                    | Fortkortelse | Hva bruker vi den til?                                                                                                                                                                                                                                                                             |
| :-------------------------- | :----------- | :------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| Simple Storage Service      | S3           | S3 brukes til lagring av data som skal akksesseres rimelig ofte. Det betyr gjerne nettsider og bilder lagres her. For eksemepl bruker Imgur S3 til å lagre nettsider. S3 brukes også i stor grad til å lagre, og vise, statiske nettsider og webapplikasjoner.                                     |
| API Gateway                 | -            | API Gateway lar deg definere endepunkt, og la disse peke på andre tjenester i AWS. Det betyr at du for eksempel kan lage et endepunkt for å lagre bilder i API Gateway, og peke det rett til S3. Da trenger du ikke programmere noe for å oppnå lagring via et API.                                |
| Relational Database Serivce | RDS          | RDS er en tjeneste for relasjonsdatabase. Her kan du lage databaseinstanser av typer som MySQL og Postgres. Med tradisjonelle servere må man gjerne passe på databaseinstansen, fordi de kjører på en server. Med RDS forholder du deg kun til databaseinstansen, ikke til serveren den kjører på. |
| Lambda Functions            | -            | Lambda Functions lar deg kjøre kode, helt uten en tradisjonell server. ...                                                                                                                                                                                                                         |

Videre vil vi sette opp hver av disse tjenestene etter tur.

### Steg 1: Sette opp Simple Storage Service (S3)

I første steg skal vi, så enkelt og raskt som mulig, legge webapplikasjonen vår på det åpne nettet. Det vil si: Vi skal kunne nå webapplikasjonen fra en nettadresse. For å gjøre det må vi gjøre tre ting:

1. Opprette en _bucket_ i S3
2. Gjøre innholdet åpent tilgjengelig
3. Laste opp webapplikasjonen til bucketen

Vi kjører i gang!

#### Opprette en bucket i S3

1. Logg inn i [AWS Management Console](https://eu-west-1.console.aws.amazon.com/console)
2. Naviger til S3. Det enkleste er å søke i feltet under «Find services».
3. Du har nå kommet til S3, som viser deg en liste over de _buckets_ du har. Den er nok foreløpig tom. En bucket er som en slags mappe du kan gi visse rettigheter og egenskaper. For å opprette en slik, trykk «Create bucket»
4. Gi den et navn som er DNS-compliant, det vil si at det må kunne være et domenenavn. Navn på _buckets_ må være unikt. Det betyr at ikke alle som gjør kurset kan ha samme navn på sin bucket. Vi foreslår at du gir denne bucketen `<hva du vil kalle din Event-tjeneste>.no`, for eksempel `123events.no`. Trykk «Next».
5. I neste steg kan du gjøre flere valg for bøtta, som å tagge den (nyttig hvis man har mye forskjellig på en og samme AWS-konto), eller skru på versjonering av objektene. Vi trenger ikke noe av dette når vi bare skal lagre en nettside, så vi trykker «Next».
6. Dette steget kontrollerer hvilke kontoer som kan sette tilganger for denne bucketen. For at vi skal kunne laste opp objekter og sette disse helt public (det skal jo være en nettside oppi), må vi fjerne markeringen ved «Block new public ACLs and uploading public objects (Recommended)» og «Remove public access granted through public ACLs (Recommended)». Gjør dette, og trykk «Next».
7. Da kommer vi til siste steg. Se at alt ser okei ut, og trykk «Create bucket».

#### Gjør innholdet åpent tilgjengelig

Når bucketen er opprettet må vi gjøre den public. Det gjør vi ved å trykke på navnet på bucketen, slik at vi går inn på den. Deretter gjør vi følgende:

1. Velg «Properties» øverst
2. Trykk på «Static web hosting»
3. Kopier URL som står etter «Endpoint». Dette blir URL til webappen, så den må du lagre til senere.
4. Velg «Use this bucket to host a website»
5. Under «Index document» skriver du `index.html`, altså standard verdi man bruker for indeksdokument på en nettside.
6. Trykk «Save»

Nå er selve bucketen åpnet for å kunne nås utenfra. Nå må vi laste opp innhold, og sørge for at det også kan nås utenfra.

#### Publiser applikasjonen

Nå som du har en bucket med de ønskelige egenskaper og rettigheter, er vi _nesten_ klar for å laste opp webapplikasjonen. Først, verifiser at du har bygget den. Det gjør du ved å se at du har en `dist` mappe i mappen til webappen. Hvis du ikke har det, gå til webappen sin README og se hvordan du bygger applikasjonen.

Når du har en bygg er endelig er alt klart: Vi kan publisere appen! Det gjør vi enten ved å dra filene vi vil publisere over i bucketen ved hjelp av nettleseren, eller vi kan gjøre det ved hjelp av kommandolinjen.

**Med nettleseren:**

1. Logg inn i/åpne AWS Manamgenet Console, gå til S3 og åpne bucketen du ønsker å legge innholdet i.
2. Trykk «Upload», åpne en filutforsker, marker innholdet i `dist`-mappen i webappprosjektet og dra innholdet over til AWS-vinduet
3. Trykk «Next»
4. Under «Manage public permissions» velger du `Grant public read access to this object(s)`.
5. Trykk «Next»
6. Og «Next» nok en gang. Til slutt «Upload»

**Med kommandolinje:**

1. Åpne en ny terminal og gå til mappen du har klonet webapplikasjonen i
2. Åpne et shell med din AWS-konto koblet på, `aws-vault exec <profilnavn>`
3. Publiser innholdet i din bucket med tilgang slik at alle kan lese filene: `aws s3 cp dist/ s3://<navn på bucket> --acl public-read --recursive`.

Når opplasting er ferdig bør du kunne nå applikasjonen på URLen vi lagret i stad.

### Steg 3: Relational Database Serivce (RDS)

Vi oppretter en database i RDS vi kan gi til Lambda Functions

#### Opprett databaseinstans i AWS

1. Gå til RDS og velg «Create database»
2. I første steg velger vi type database. I dette kurset velger vi Postgres, så velg dette og trykk «Next»
3. Under Use case velger du «Dev/Test» og trykker «Next»
4. I dette steget velger vi en rekke detaljer om databasen vi skal sette opp. Det viktigste her er «DB instance class» sier noe om hvor stor last instansen vil kunne håndtere, og dermed også kostnandsnivået. Her kan du velge `db.t2.micro`
5. Under «Settings» setter vi navn på instansen, og brukernavn og passord for den. Instansnavnet kan for eksempel være `eventsapp`. Brukernavnet kan godt være det samme. Passordet bør være noe du finner på selv. Husk å notere deg navn, brukernavn og passord slik at du har det til senere. Trykk «Next»
6. I dette steget får vi en rekke valg for databasen. Vi lar det meste stå som standard. Vi skal først og fremst sette et navn på databasen som skal kjøre på databaseinstansen. Denne kan godt være det samme om instansen, `eventsapp`. Trykk «Create database»

#### Hent ut instillinger

Nå skal vi hente ut URL til databasen, slik ta vi kan la koden vår koble seg på den senere:

1. Trykk «Databases» til venstre
2. Finn den du nettopp lagde, og vent til status er blitt «Available». Når den er det, trykker du på den.
3. Lagre verdien som står under «Endpoint», slik at vi har den til senere.. Den vil være noe sånt som: `<ditt-database-navn>.cwkzdvvfjrvm.eu-west-1.rds.amazonaws.com`.

### Steg 4: Lambda Functions

Lambda er en tjeneste i AWS for å kjøre en mengde kode på forespørsel. Med andre ord kjøres koden når den trigges, for eksempel av API Gateway. Det betyr at vi kan ha en backend, eller en funksjon, for hvert eneste endepunkt. Det gjør videre at vi får en applikasjon hvor de ulike endepunktene har veldig liten kobling mellom hverandre, og kan oppdateres uavhengig av hverandre.

I dette steget skal vi opprette Lambda-funksjoner, ett for hvert endepunkt. Deretter skal vi laste koden vår opp, enten ved hjelp av CLI eller nettleseren.

#### Opprett Lambda-function

1. Åpne eller logg inn i [AWS Management Console](https://eu-west-1.console.aws.amazon.com/console)
2. Sjekk at du er i AWS-region Ireleand, også kjent som `eu-west-1`. Dette ser du øverst i venstre hjørnet. Hvis du ikke er det allerede, bytter du til `eu-vest-1` Ireland.
3. Gå til Lambda
4. Trykk «Create function»
5. Du sjekker at «Author from scratch» er valgt øverst. Du gir funksjonen et navn under «Name». Dette bør være noe unikt, som gjør at du kjenner igjen funksjonen. For eksemepl `myeventsapp-GET-events`. Under Runtime velger du Java 8. Under «Role» velger du «Choose an exisiting role», og så velger du `service-role/lambda_basic_execution` i dropdownen under.
6. Trykk «Create function»

#### Bygg Lambda-funksjonene

Lambda-funksjonene ligger i `lambda`-mappen i dette repositoriet, og er skrevet i Java. Lambda trenger at dette er en ferdigbygget `JAR`-fil, eller en `ZIP` av Java-filer.

#### Last opp koden til Lambda-function

Når vi har bygd en JAR-fil for hvert endepunkt, kan vi hver og en opp til en Lambda ved hjelp av konsoll eller kommandolinjen. Gjør det for hvert av endepunktene.

**Med nettleseren:**

1. Åpne eller logg inn i [AWS Management Console](https://eu-west-1.console.aws.amazon.com/console)
2. Gå til Lambda-tjenensten
3. Finn funksjonen du opprettet i forrige steg
4. Trykk «Upload» og last opp en

**Med kommandolinje:**

1. Benytt `aws-vault` til å åpne et shell knyttet til kontoen det skal kjøre på: `aws-vault exec <profilnavn>`
2. For å laste opp, kjør: `aws lambda update-function-code --function-name=<funksjonsnavn> --zip-file=fileb://<navn på jar> --region eu-west-1`

#### Sett instillingene til database som en miljøvariabler

1. Under «Environment variables» legger du til `PG_URL` med verdien du hentet ut i slutten av steg 3, altså URL til Postgres-databasen.
2. Legg også til `PG_USER` med verdi `postgres`, og `PG_PASSWORD` med passordet du satte på samme måte.
3. Trykk «Save» øverst til høyre, og så «Actions» etterfulgt av «Publish new version».

### Steg 4: API Gateway

API Gateway er en tjeneste i AWS for å lage APIer helt uten programmering, og så kunne koble disse mot andre tjenester i AWS. I vårt tilfelle skal vi lage endepunkter for å opprette, endre, slette og hente ut eventer fra databasen vår. For å gjøre dette må vi:

1. Designe endepunktene våre slik at vi vet hvordan de skal se ut
2. Lage disse i API Gateway
3. Knytte endepunktene til andre tjenester i AWS, slik at endepunktene faktisk gjør noe. I vårt tilfelle betyr det at vi skal koble de til Lmabda-funksjoner.

Først, steg 1: Hvordan skal APIet se ut? Vi trenger altså Create Read Update Delete (CRUD) for events:

| Metode   | Endepunkt        | Konsumerer                                                | Returnerer                         |
| :------- | :--------------- | :-------------------------------------------------------- | :--------------------------------- |
| `GET`    | `/events`        | n/a                                                       | En array med alle events           |
| `POST`   | `/events`        | Nytt event objekt                                         | En nyopprettet event               |
| `GET`    | `/events/{uuid}` | n/a                                                       | En enkelt event                    |
| `DELETE` | `/events/{uuid}` | n/a                                                       | `200 OK` hvis event ble slettet.   |
| `PUT`    | `/events/{uuid}` | Event-objekt med endringer som man ønsker at skal lagres. | En event oppdatert etter endringer |

#### Lage API i API Gateway

1. Logg inn i AWS-konsollen og naviger til API Gateway
2. Trykk «Create API»
3. Da skal du se det følgende skjermbildet
   ![Create API i API Gateway](images/create-api-gateway.png)
4. Under «API Name» gir du APIet et navn, for eksmepel `eventsapp`.De andre verdiene kan du beholde som de er.
5. Trykk «Create API»

#### Lage ressurser til en API i API Gateway

Du er nå inne i editoren for å lage API i API Gateway. Her har vi to konsepter: _Resources_, og _Methods_. En Resource er en ressurs, eller en entitet. I vårt tilfelle er et eksemepl på en slik en ett enkelt event.

1. Trykk på «Actions» og trykk «Create resource»
2. Du vil da få opp et skjermbilde for å opprette en ressurs. «Resource name» er navnet på ressursen, og er kun for at man skal skjønne hva det er for noe man ser ressursen i editoren for API Gateway. «Resource Path» er endepunktet. Fyll ut begge basert på tabellen som beskriver API ovenfor. Det første du skal opprette er da altså `events`.
3. Gjenta dette slik at du får en struktur som den beskrevet ovenfor, altså en ressurs `/events` og under der en ressurs `{uuid}`

![Actions-menu i API Gateway](images/api-gateway-create-resource.png)

Når du er ferdig bør det se noe sånt som dette ut:

![Resources definert i API Gateway](images/api-gateway-resources.png)

#### Legg til metoder og knytt til Lambda-funksjoner for ditt API i API Gateway

Når API er importer har vi fått alle endepunktene som dokumentet definerer. Vanligvis kunne man definert dette ved hjelp av API Gateway direkte, men vi har lagt det ved for å gjøre det litt raskere for dere.

#### Deploy API

howto deploy og verifisere at det virker

![Vindu for å deploye API i API Gateway](images/deploy-api.png)

#### Steg 5: Bring it all together

Vi sjekker at alt virker

1. bytte endepunkter i webapp
2. deploy på nytt
3. sjekk at det funker

### Veien videre

Ting vi kanskje kan lage ekstraoppgaver av:

-   Lek med webappen og legg til nye features
-   CloudFront for webappen
-   Legg til script i `package.json` for å deploye webappen, tilsvarende for java
-   Bruke developer services for å sette opp automatisk deploy?
-   IAM med roller for sikkerhet
