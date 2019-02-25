CREATE TABLE events (
  id           TEXT PRIMARY KEY,
  data         JSONB
);

ALTER TABLE events OWNER TO eventsapp;

INSERT INTO events VALUES (
  '783dc091-975c-418c-8e78-2db3bbcef15f',
  '{"title": "Kurs med Capra","date": "2019-02-25T08:10:00.500Z","description": "Kurs for å sette opp en app med bruk av AWS","id": "783dc091-975c-418c-8e78-2db3bbcef15f"}'
);
