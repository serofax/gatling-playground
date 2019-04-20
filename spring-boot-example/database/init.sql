-- POSTGRES DIALECT

CREATE TABLE PROJECT (
    ID SERIAL NOT NULL,
    NAME VARCHAR(100) NOT NULL,
    SHORT_DESCRIPTION VARCHAR(255) NOT NULL,
    DESCRIPTION TEXT NOT NULL,
    SALES_REPRESENTATIVE VARCHAR(255) NOT NULL,
    LOCATION  VARCHAR(255) NOT NULL,
    NUMBER_OF_PEOPLE  VARCHAR(255) NOT NULL,
    DURATION VARCHAR(255) NOT NULL
);


INSERT INTO PROJECT ("name", "short_description", "description", "sales_representative", "location", "number_of_people", "duration")
    VALUES ('Example name', 'Example short description', 'Example description', 'Example sales reprasentive', 'Example location', '3', 'Example duration');

