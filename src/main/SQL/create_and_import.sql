--
-- SQL script to create tables in recipehealth postgresql database and populate them from delimited files specified
--

-- EXTENSIONS --
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";


-- CREATE SR TABLES IF NOT EXIST, THEN COPY DATA FROM PIPE DELIMITED FILE INTO TABLE --


-- STANDARD REFERENCE DATA --

-- -- SR_Food_Description --
-- -- DROP TABLE IF EXISTS SR_Food_Description CASCADE;
-- CREATE TABLE IF NOT EXISTS SR_Food_Description
-- (
--   NDB_No INT NOT NULL,
--   Food_Group_Cd INT NOT NULL,
--   Long_Description TEXT NOT NULL,
-- PRIMARY KEY (NDB_No)
-- );
-- \copy SR_Food_Description FROM '/home/bmb0205/BiSD/KnowledgeBase/Sources/USDA/StandardReference/SR_FOOD_DES.txt.out' DELIMITER '|' CSV;


-- ALL_FOOD_DES --
CREATE TABLE IF NOT EXISTS ALL_Food_Description
(
  NDB_No INT NOT NULL,
  FdGrp_Cd INT NOT NULL,
  Long_Desc VARCHAR NOT NULL,
  PRIMARY KEY (NDB_No)
);
\copy ALL_Food_Description FROM '/home/bmb0205/BiSD/KnowledgeBase/Sources/USDA/ALL_FOOD_DES.csv.out' DELIMITER '|' CSV;


-- ALL_NUTR_DEF.csv.out --
CREATE TABLE IF NOT EXISTS ALL_Nutrient_Definition
(
  Nutrient_No INT NOT NULL,
  Unit VARCHAR NOT NULL,
  Nutrient_Des VARCHAR NOT NULL,
  PRIMARY KEY (Nutrient_No)
);
\copy ALL_Nutrient_Definition FROM '/home/bmb0205/BiSD/KnowledgeBase/Sources/USDA/ALL_NUTR_DEF.csv.out' DELIMITER '|' CSV;


-- -- SR_Nutrient_Definition --
-- -- DROP TABLE IF EXISTS SR_Nutrient_Definition CASCADE;
-- CREATE TABLE IF NOT EXISTS SR_Nutrient_Definition
-- (
--   Nutrient_No INT NOT NULL,
--   Units VARCHAR NOT NULL,
--   Nutrient_Des VARCHAR NOT NULL,
-- PRIMARY KEY (Nutrient_No)
-- );
-- \copy SR_Nutrient_Definition FROM '/home/bmb0205/BiSD/KnowledgeBase/Sources/USDA/StandardReference/SR_NUTR_DEF.txt.out' DELIMITER '|' CSV;


-- SR_Weight --
-- DROP TABLE IF EXISTS SR_Weight CASCADE;
CREATE TABLE IF NOT EXISTS SR_Weight
(
  NDB_No INT NOT NULL,
  Seq INT NOT NULL,
  Amount VARCHAR NOT NULL,
  Measure_Des VARCHAR NOT NULL,
  Gram_Weight VARCHAR NOT NULL,
PRIMARY KEY (NDB_No, Seq),
CONSTRAINT fk_NDB_No FOREIGN KEY (NDB_No) REFERENCES All_Food_Description (NDB_No)
);
\copy SR_WEIGHT FROM '/home/bmb0205/BiSD/KnowledgeBase/Sources/USDA/StandardReference/SR_WEIGHT.txt.out' DELIMITER '|' CSV;


-- -- SR_Nutrient_Data --
-- -- DROP TABLE IF EXISTS SR_Nutrient_Data CASCADE;
-- CREATE TABLE IF NOT EXISTS SR_Nutrient_Data
-- (
--   NDB_No INT NOT NULL,
--   Nutrient_No INT NOT NULL,
--   Nutrient_Val VARCHAR NOT NULL,
--   CC VARCHAR,
-- PRIMARY KEY (NDB_No, Nutrient_No),
-- CONSTRAINT fk_NDB_No FOREIGN KEY (NDB_No) REFERENCES ALL_Food_Description (NDB_No),
-- CONSTRAINT fk_Nutrient_No FOREIGN KEY (Nutrient_No) REFERENCES SR_Nutrient_Definition (Nutrient_No)
-- );
-- \copy SR_Nutrient_Data FROM '/home/bmb0205/BiSD/KnowledgeBase/Sources/USDA/StandardReference/SR_NUT_DATA.txt.out' DELIMITER '|' CSV;


-- FLAVONOID DATA --

-- -- FL_NUTRIENT_DEFINITION --
-- -- DROP TABLE IF EXISTS FL_Nutrient_Definition CASCADE;
-- CREATE TABLE IF NOT EXISTS FL_Nutrient_Definition
-- (
--   Nutrient_No INT NOT NULL,
--   Description VARCHAR NOT NULL,
--   Flav_Class VARCHAR NOT NULL,
--   Units VARCHAR NOT NULL,
-- PRIMARY KEY (Nutrient_No)
-- );
-- \copy FL_Nutrient_Definition FROM '/home/bmb0205/BiSD/KnowledgeBase/Sources/USDA/Flavonoid/FL_NUTR_DEF.txt.out' DELIMITER '|' CSV;


-- FL_Fd_Group --
-- DROP TABLE IF EXISTS FL_Food_Group CASCADE;
CREATE TABLE IF NOT EXISTS FL_Food_Group
(
  FdGrp_Cd INT NOT NULL,
  FdGrp_Description VARCHAR NOT NULL,
PRIMARY KEY (FdGrp_Cd)
);
\copy FL_Food_Group FROM '/home/bmb0205/BiSD/KnowledgeBase/Sources/USDA/Flavonoid/FL_FD_GROUP.txt.out' DELIMITER '|' CSV;


-- FL_Data_Src --
-- DROP TABLE IF EXISTS FL_Data_Src CASCADE;
CREATE TABLE IF NOT EXISTS FL_Data_Src
(
  DataSrc_ID VARCHAR NOT NULL,
  Title VARCHAR,
  Year VARCHAR,
  Journal VARCHAR NOT NULL,
PRIMARY KEY (DataSrc_ID)
);
\copy FL_Data_Src FROM '/home/bmb0205/BiSD/KnowledgeBase/Sources/USDA/Flavonoid/FL_DATA_SRC.txt.out' DELIMITER '|' CSV;


-- -- FL_Food_Description --
-- -- DROP TABLE IF EXISTS FL_Food_Description CASCADE;
-- CREATE TABLE IF NOT EXISTS FL_Food_Description
-- (
--   NDB_No INT NOT NULL,
--   FdGrp_Cd INT NOT NULL,
--   Long_Description VARCHAR NOT NULL,
-- PRIMARY KEY (NDB_No),
-- CONSTRAINT fk_FdGrp_Cd FOREIGN KEY (FdGrp_Cd) REFERENCES FL_Food_Group (FdGrp_Cd)
-- );
-- \copy FL_Food_Description FROM '/home/bmb0205/BiSD/KnowledgeBase/Sources/USDA/Flavonoid/FL_FOOD_DES.txt.out' DELIMITER '|' CSV;


-- FL_FLAV_DATA --
-- DROP TABLE IF EXISTS FL_Flav_Data CASCADE;
-- CREATE TABLE IF NOT EXISTS FL_Flav_Data
-- (
--   NDB_No INT NOT NULL,
--   Nutrient_No INT NOT NULL,
--   Flav_Val VARCHAR NOT NULL,
--   CC VARCHAR,
-- PRIMARY KEY (NDB_No, Nutrient_No),
-- CONSTRAINT fk_NDB_No FOREIGN KEY (NDB_No) REFERENCES ALL_Food_Description (NDB_No),
-- CONSTRAINT fk_Nutrient_No FOREIGN KEY (Nutrient_No) REFERENCES FL_Nutrient_Definition (Nutrient_No)
-- );
-- \copy FL_Flav_Data FROM '/home/bmb0205/BiSD/KnowledgeBase/Sources/USDA/Flavonoid/FL_FLAV_DAT.txt.out' DELIMITER '|' CSV;


-- -- FL_FLAV_IND --
-- -- DROP TABLE IF EXISTS FL_Flav_Ind CASCADE;
CREATE TABLE IF NOT EXISTS FL_Flav_Ind
(
  NDB_No INT NOT NULL,
  DataSrc_ID VARCHAR NOT NULL,
  Food_No INT NOT NULL,
  Food_Indiv_Description VARCHAR NOT NULL,
  Compound_Name VARCHAR NOT NULL,
  Compound_Val VARCHAR,
  uuid UUID NOT NULL,
PRIMARY KEY(uuid),
CONSTRAINT fk_DataSrc_ID FOREIGN KEY (DataSrc_ID) REFERENCES FL_Data_Src (DataSrc_ID)
);
\copy FL_Flav_Ind FROM '/home/bmb0205/BiSD/KnowledgeBase/Sources/USDA/Flavonoid/FL_FLAV_IND.txt.out' DELIMITER '|' CSV;


-- FL_DATA_SRCLN --
-- DROP TABLE IF EXISTS FL_Data_Srcln CASCADE;
CREATE TABLE IF NOT EXISTS FL_Data_Srcln
(
  NDB_No INT NOT NULL,
  Nutrient_No INT NOT NULL,
  DataSrc_ID VARCHAR NOT NULL,
  fl_uuid UUID NOT NULL,
PRIMARY KEY (fl_uuid),
CONSTRAINT fk_DataSrc_ID FOREIGN KEY (DataSrc_ID) REFERENCES FL_Data_Src (DataSrc_ID)
);
\copy FL_Data_Srcln FROM '/home/bmb0205/BiSD/KnowledgeBase/Sources/USDA/Flavonoid/FL_DATSRCLN.txt.out' DELIMITER '|' CSV;


-- ISOFLAVONE DATA --

-- ISO_DATA_SRC --
CREATE TABLE IF NOT EXISTS ISO_Data_Src
(
  DataSrc_ID VARCHAR NOT NULL,
  Title VARCHAR,
  Year VARCHAR,
  Journal VARCHAR,
PRIMARY KEY (DataSrc_ID)
);
\copy ISO_Data_Src FROM '/home/bmb0205/BiSD/KnowledgeBase/Sources/USDA/Isoflavone/ISO_DATA_SRC.csv.out' DELIMITER '|' CSV;


-- -- ISO_NUTR_DEF.csv.out --
-- CREATE TABLE IF NOT EXISTS ISO_Nutrient_Definition
-- (
--   Nutrient_No INT NOT NULL,
--   Nutrient_Des VARCHAR NOT NULL,
--   Unit VARCHAR NOT NULL,
--   PRIMARY KEY (Nutrient_No)
-- );
-- \copy ISO_Nutrient_Definition FROM '/home/bmb0205/BiSD/KnowledgeBase/Sources/USDA/Isoflavone/ISO_NUTR_DEF.csv.out' DELIMITER '|' CSV;

-- -- ISO_ISFL_DAT --
-- CREATE TABLE IF NOT EXISTS ISO_Isfl_Dat
-- (
--   NDB_No INT NOT NULL,
--   Nutrient_No INT NOT NULL,
--   Isfl_Val VARCHAR NOT NULL,
--   CC VARCHAR,
--   PRIMARY KEY (NDB_No, Nutrient_No),
--   CONSTRAINT fk_Nutrient_No FOREIGN KEY (Nutrient_No) REFERENCES ISO_Nutrient_Definition (Nutrient_No)
-- );
-- \copy ISO_Isfl_Dat FROM '/home/bmb0205/BiSD/KnowledgeBase/Sources/USDA/Isoflavone/ISO_ISFL_DAT.csv.out' DELIMITER '|' CSV;


-- ISO_DATA_SRCLN --
CREATE TABLE IF NOT EXISTS ISO_Data_Srcln_temp
(
  NDB_No INT NOT NULL,
  Nutrient_No INT NOT NULL,
  DataSrc_ID VARCHAR NOT NULL,
  iso_uuid UUID NOT NULL,
PRIMARY KEY (iso_uuid)
-- CONSTRAINT fk_Nutr_No FOREIGN KEY (NDB_no, Nutrient_No) REFERENCES ISO_Isfl_Dat (NDB_No, Nutrient_No),
-- CONSTRAINT fk_NDB_No FOREIGN KEY (NDB_No) REFERENCES ALL_Food_Description (NDB_No),
-- CONSTRAINT fk_DataSrc_ID FOREIGN KEY (DataSrc_ID) REFERENCES ISO_Data_Src (DataSrc_ID)
);
\copy ISO_Data_Srcln_temp FROM '/home/bmb0205/BiSD/KnowledgeBase/Sources/USDA/Isoflavone/ISO_DATA_SRCLN.csv.out' DELIMITER '|' CSV;


CREATE TABLE IF NOT EXISTS ISO_Data_Srcln
(
  NDB_No INT NOT NULL,
  Nutrient_No INT NOT NULL,
  DataSrc_ID VARCHAR NOT NULL,
  iso_uuid UUID NOT NULL,
  PRIMARY KEY (iso_uuid),
  -- CONSTRAINT fk_Nutr_No FOREIGN KEY (NDB_no, Nutrient_No) REFERENCES ISO_Isfl_Dat (NDB_No, Nutrient_No),
  CONSTRAINT fk_NDB_No FOREIGN KEY (NDB_No) REFERENCES ALL_Food_Description (NDB_No),
  CONSTRAINT fk_DataSrc_ID FOREIGN KEY (DataSrc_ID) REFERENCES ISO_Data_Src (DataSrc_ID),
  CONSTRAINT fk_Nutrient_No FOREIGN KEY (Nutrient_No) REFERENCES ALL_Nutrient_Definition (Nutrient_No)
);

INSERT INTO ISO_Data_Srcln
    SELECT *
    FROM ISO_Data_Srcln_temp
      WHERE ISO_Data_Srcln_temp.NDB_No IN
        (
          SELECT NDB_No FROM ALL_FOOD_DESCRIPTION
        );
DROP TABLE IF EXISTS ISO_Data_Srcln_temp;


-- ALL_FOOD_DATA.csv.out --
CREATE TABLE IF NOT EXISTS ALL_Food_Data
(
  NDB_No INT NOT NULL,
  Nutrient_No INT NOT NULL,
  Nutrient_Val VARCHAR NOT NULL,
  CC VARCHAR,
  PRIMARY KEY (NDB_No, Nutrient_No),
  CONSTRAINT fk_NDB_No FOREIGN KEY (NDB_No) REFERENCES ALL_Food_Description (NDB_No),
  CONSTRAINT fk_Nutrient_No FOREIGN KEY (Nutrient_No) REFERENCES ALL_Nutrient_Definition (Nutrient_No)
--   CONSTRAINT fk_Nutrient_No_sr FOREIGN KEY (Nutrient_No) REFERENCES SR_Nutrient_Definition (Nutrient_No)
);
\copy ALL_Food_Data FROM '/home/bmb0205/BiSD/KnowledgeBase/Sources/USDA/ALL_FOOD_DATA.csv.out' DELIMITER '|' CSV;


-- -- ISO_FOOD_DES --
-- CREATE TABLE IF NOT EXISTS ISO_Food_Description
-- (
--   NDB_No INT NOT NULL,
--   FdGrp_Cd INT NOT NULL,
--   Long_Desc VARCHAR NOT NULL,
-- PRIMARY KEY (NDB_No),
-- CONSTRAINT fk_NDB_No FOREIGN KEY (NDB_No) REFERENCES FL_Food_Description (NDB_No),
-- CONSTRAINT fk_NDB_No FOREIGN KEY (NDB_No) REFERENCES SR_Food_Description (NDB_No)
-- );
-- \copy ISO_Food_Description FROM '/home/bmb0205/BiSD/KnowledgeBase/Sources/USDA/Isoflavone/ISO_FOOD_DES.csv.out' DELIMITER '|' CSV;


-- TEST QUERIES --

-- SELECT SR_Nutrient_Data.NDB_No, SR_Nutrient_Data.Nutrient_No, ndef.Nutrient_Des
-- FROM SR_Nutrient_Definition AS ndef
--   INNER JOIN SR_Nutrient_Data
--     ON SR_Nutrient_Data.Nutrient_No = ndef.Nutrient_No
-- WHERE SR_Nutrient_Data.NDB_No = 01001;
