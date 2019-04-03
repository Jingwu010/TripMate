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
   CONSTRAINT citystate UNIQUE (city_name, state_id_fk)
) ENGINE=InnoDB;

CREATE TABLE poi(
   poi_id INT NOT NULL AUTO_INCREMENT,
   city_id_fk INT,
   poi_name VARCHAR(100) NOT NULL,
   poi_wikiurl VARCHAR(200),
   poi_lat DECIMAL(11, 8),
   poi_long DECIMAL(11, 8),
   last_updated INT NOT NULL,
   PRIMARY KEY ( poi_id )
) ENGINE=InnoDB;