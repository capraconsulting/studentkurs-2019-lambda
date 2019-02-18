# Studentkurs 2019

I dette kurset skal vi sette opp en webapplikasjon med lagring til database med Amazon Web Services. Webapplikasjonen er en enkel sak for å opprette, endre, slette ogse på events. Den er skrevet i TypeScript med React.

## Oppsett av utviklingsmiljø lokalt

Sørg for at du har gjort det som behøves på forhånd. Se PDF som ble sendt ut før kurset. Hvis du ikke har fått den, ligger den [her]()

Så kan du klone repoer og sette opp kode:

1. Klone dette repoet
2. Klone [webapp](https://github.com/capraconsulting/studentkurs-2019-webapp) og følg instruksjonene der for å sette den opp

## Amazon Web Services (AWS)

Amazon Web Services er en rekke tjenester for ulike formål. Man har tjenester for å kjøre enkle virtuelle servere, tjenester for å sende e-post, prosessere enorme datamengder, lagring i ulike typer databaser, lagring av filer og lagring av statisk innhold til nettsider, for å nevne noe.

For å kunnee bruke AWS trenger man en konto. Man får mye gratis, slik at man kan komme i gang for en rimelig penge. Dersom du mangler konto, kan du se på instruksjonene vi sendte ut på forhånd for å få hjelp.

Man har flere forskjellige måter å jobbe med AWS på. Man kan bruke nettleseren, man kan lage maler som definerer hvordan ting skal fungere, eller man kan bruke kommandolinjen.

I dette kurset skal vi først sette opp tjenester med nettleseren, i den såkalte konsollen, og så oppdatere innholdet ved hjelp av AWS sitt CLI (Command Line Interface).

## Vi setter opp AWS-tjenester for applikasjonen vår

Skisse som viser hvordan tingene henger sammen
Tabell som forklarer rollen til de fire tjenestene

### Steg 1: Sette opp Simple Storage Service (S3)

Vi lager en bucket og laster opp webappen til den

### Steg 2 API Gateway

Vi oppretter API Gateway med endepunkter basert på en Swagger doc.

### Steg3: Relational Database Serivce (RDS)

Vi oppretter en database i RDS vi kan gi til Lambda Functions

### Steg 4: Lambda Functions

Oppretter lambda functions og legger inn kode via console/CLI

### Advanced section

-   Lek med webappen og legg til nye features
-   Legg til script i `package.json` for å deploye webappen
-   CloudFront for webappen
