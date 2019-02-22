CREATE TABLE public."events" (
  id           SERIAL PRIMARY KEY,
  data         JSONB
);

ALTER TABLE public."events" OWNER TO eventsapp;
