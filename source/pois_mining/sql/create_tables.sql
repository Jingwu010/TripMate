CREATE TABLE state(
   id INT NOT NULL AUTO_INCREMENT,
   name VARCHAR(100) NOT NULL UNIQUE,
   PRIMARY KEY ( id )
) ENGINE=InnoDB;

CREATE TABLE city(
   id INT NOT NULL AUTO_INCREMENT,
   state_id_fk INT,
   name VARCHAR(100) NOT NULL,
   PRIMARY KEY ( id ),
   CONSTRAINT citystate UNIQUE (name, state_id_fk)
) ENGINE=InnoDB;

CREATE TABLE poi(
   id INT NOT NULL AUTO_INCREMENT,
   city_id_fk INT,
   name VARCHAR(100) NOT NULL,
   formatted_address VARCHAR(100),
   wiki_url VARCHAR(200),
   thumbnail_url VARCHAR(200),
   lat DECIMAL(11, 8),
   lng DECIMAL(11, 8),
   last_updated INT NOT NULL,
   PRIMARY KEY ( id )
) ENGINE=InnoDB;