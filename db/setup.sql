CREATE TABLE public."events" (
  id           TEXT PRIMARY KEY,
  data         JSONB
);

ALTER TABLE public."events"  OWNER TO eventsapp;
