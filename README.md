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
2. Åpne et shell med din AWS-konto koblet på, `aws-vault exec <din konto>`
3. Publiser innholdet i din bucket med tilgang slik at alle kan lese filene: `aws s3 cp dist/ s3://<navn på bucket> --acl public-read --recursive`.

Når opplasting er ferdig bør du kunne nå applikasjonen på URLen vi lagret i stad.

### Steg 2 API Gateway

Vi oppretter API Gateway med endepunkter basert på en Swagger doc.

### Steg 3: Relational Database Serivce (RDS)

Vi oppretter en database i RDS vi kan gi til Lambda Functions

### Steg 4: Lambda Functions

Oppretter lambda functions og legger inn kode via console, eller ved hjelp av CLI

**Last opp koden ved hjelp av konsollen:**

1. Åpne eller logg inn i [AWS Management Console](https://eu-west-1.console.aws.amazon.com/console)
2. Gå til Lambda
3. Trykk «Create function»
4. Du sjekker at «Author from scratch» er valgt øverst. Du gir funksjonen et navn under «Name». Dette bør være noe unikt, som gjør at du kjenner igjen funksjonen. For eksemepl `myeventsapp-GET-events`. Under Runtime velger du Java 8. Under «Role» velger du «Choose an exisiting role», og så velger du `service-role/lambda_basic_execution` i dropdownen under.
5. Trykk «Create function»

**Sett instillingene til database som en miljøvariabler:**

1. Under «Environment variables» legger du til `PG_URL` med verdien du hentet ut i slutten av steg 3, altså URL til Postgres-databasen.
2. Legg også til `PG_USER` med verdi `postgres`, og `PG_PASSWORD` med passordet du satte på samme måte.
3. Trykk «Save» øverst til høyre, og så «Actions» etterfulgt av «Publish new version».

### Advanced section

Ting vi kanskje kan lage ekstraoppgaver av:

-   IAM med roller for sikkerhet
-   Lek med webappen og legg til nye features
-   Legg til script i `package.json` for å deploye webappen
-   CloudFront for webappen
