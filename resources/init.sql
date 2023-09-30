DROP TABLE IF EXISTS WEATHER;
CREATE TABLE WEATHER
(
    localidad     VARCHAR(50),
    provincia     VARCHAR(50),
    tempMax DOUBLE,
    horaTempMax   TIMESTAMP,
    tempMin DOUBLE,
    horaTempMin   TIMESTAMP,
    precipitacion double,
    dia           TIMESTAMP
);