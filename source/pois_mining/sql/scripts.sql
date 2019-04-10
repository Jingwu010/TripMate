CREATE USER 'tripmate'@'localhost'
  IDENTIFIED BY 'tripmate';
GRANT ALL PRIVILEGES ON tripmate.* TO 'tripmate'@'localhost';


CREATE USER 'tripmate'@'localhost' IDENTIFIED WITH mysql_native_password BY 'tripmate';

CREATE TABLE state(
   state_id INT NOT NULL AUTO_INCREMENT,
   state_name VARCHAR(100) NOT NULL UNIQUE,
   PRIMARY KEY ( state_id )
) ENGINE=InnoDB;

CREATE TABLE city(
   city_id INT NOT NULL AUTO_INCREMENT,
   state_id_fk INT,
   city_name VARCHAR(100) NOT NULL,
   PRIMARY KEY ( city_id ),
   -- CONSTRAINT FK_citystate FOREIGN KEY ( state_id_fk ) REFERENCES state( state_id ),
   CONSTRAINT citystate UNIQUE (city_name, state_id_fk)
) ENGINE=InnoDB;

CREATE TABLE poi(
   poi_id INT NOT NULL AUTO_INCREMENT,
   city_id_fk INT,
   poi_name VARCHAR(100) NOT NULL,
   poi_wikiurl VARCHAR(200),
   poi_lat DECIMAL(11, 8),
   poi_long DECIMAL(11, 8),
   poi_last_updated INT NOT NULL,
   PRIMARY KEY ( poi_id )
   -- CONSTRAINT FK_citypoi FOREIGN KEY ( city_id_fk ) REFERENCES city( city_id )
) ENGINE=InnoDB;

ALTER TABLE poi
ADD COLUMN poi_formatted_address VARCHAR(100) AFTER poi_name;

ALTER TABLE poi
ADD COLUMN poi_thumbnail VARCHAR(200) AFTER poi_wikiurl;

ALTER TABLE poi
CHANGE COLUMN last_updated poi_last_updated INT;

SET FOREIGN_KEY_CHECKS = 0;
DROP TABLE state;
DROP TABLE city;
DROP TABLE poi;
SET FOREIGN_KEY_CHECKS = 1;

INSERT INTO poi (poi_name, city_id_fk, poi_wikiurl, last_updated) 
    SELECT (poi_name, city_id_fk, poi_wikiurl, last_updated) 
    FROM (SELECT 'University of Alabama at Birmingham' as poi_name, 
            '' as city_id_fk, 
            'https://en.wikipedia.org//wiki/University_of_Alabama_at_Birmingham' as poi_wikiurl, 
            '2019-04-02 20:35:15' as last_updated) t 
    WHERE NOT EXISTS (SELECT 1 FROM poi p WHERE p.poi_name = t.poi_name AND p.city_id_fk = t.city_id_fk);

INSERT INTO poi (poi_name, city_id_fk, poi_wikiurl, last_updated) SELECT 'United States Capitol', '0', 'https://en.wikipedia.org//wiki/United_States_Capitol', '1554264453' WHERE NOT EXISTS (SELECT 1 FROM poi WHERE poi_name = 'United States Capitol' AND city_id_fk = '0');


SELECT *
INTO OUTFILE '/usr/local/mysql-8.0.15-macos10.14-x86_64/data/poi.csv'
FIELDS TERMINATED BY ','
ENCLOSED BY '"'
ESCAPED BY '\\'
LINES TERMINATED BY '\n'
FROM poi


SELECT *
INTO OUTFILE '/usr/local/mysql-8.0.15-macos10.14-x86_64/data/city.csv'
FIELDS TERMINATED BY ','
ENCLOSED BY '"'
ESCAPED BY '\\'
LINES TERMINATED BY '\n'
FROM city;

SELECT *
INTO OUTFILE '/usr/local/mysql-8.0.15-macos10.14-x86_64/data/state.csv'
FIELDS TERMINATED BY ','
ENCLOSED BY '"'
ESCAPED BY '\\'
LINES TERMINATED BY '\n'
FROM state;