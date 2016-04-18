--
-- SQL script to create tables in recipehealth postgresql database and populate them from delimited files specified
--


-- CREATE SR TABLES IF NOT EXIST, THEN COPY DATA FROM PIPE DELIMITED FILE INTO TABLE --

-- STANDARD REFERENCE DATA --

-- SR_Food_Description --
DROP TABLE IF EXISTS SR_Food_Description CASCADE;
CREATE TABLE IF NOT EXISTS SR_Food_Description
(
  NDB_No INT NOT NULL,
  Food_Group_Cd INT NOT NULL,
  Long_Description TEXT NOT NULL,
PRIMARY KEY (NDB_No)
);

\copy SR_Food_Description FROM '/Users/bburciag/BiSD/KnowledgeBase/Sources/USDA/StandardReference/SR_FOOD_DES.txt.out' DELIMITER '|' CSV;

-- SR_Nutrient_Definition --
DROP TABLE IF EXISTS SR_Nutrient_Definition CASCADE;
CREATE TABLE IF NOT EXISTS SR_Nutrient_Definition
(
  Nutrient_No INT NOT NULL,
  Units VARCHAR NOT NULL,
  Nutrient_Des VARCHAR NOT NULL,
PRIMARY KEY (Nutrient_No)
);

\copy SR_Nutrient_Definition FROM '/Users/bburciag/BiSD/KnowledgeBase/Sources/USDA/StandardReference/SR_NUTR_DEF.txt.out' DELIMITER '|' CSV;

-- SR_Weight --
DROP TABLE IF EXISTS SR_Weight CASCADE;
CREATE TABLE IF NOT EXISTS SR_Weight
(
  NDB_No INT NOT NULL,
  Seq INT NOT NULL,
  Amount VARCHAR NOT NULL,
  Measure_Des VARCHAR NOT NULL,
  Gram_Weight VARCHAR NOT NULL,
PRIMARY KEY (NDB_No, Seq),
CONSTRAINT "fk_NDB_No" FOREIGN KEY (NDB_No) REFERENCES SR_Food_Description (NDB_No)
);

\copy SR_WEIGHT FROM '/Users/bburciag/BiSD/KnowledgeBase/Sources/USDA/StandardReference/SR_WEIGHT.txt.out' DELIMITER '|' CSV;

-- SR_Nutrient_Data --
DROP TABLE IF EXISTS SR_Nutrient_Data CASCADE;
CREATE TABLE IF NOT EXISTS SR_Nutrient_Data
(
  NDB_No INT NOT NULL,
  Nutrient_No INT NOT NULL,
  Nutrient_Val VARCHAR NOT NULL,
PRIMARY KEY (NDB_No, Nutrient_No),
CONSTRAINT "fk_NDB_No" FOREIGN KEY (NDB_No) REFERENCES SR_Food_Description (NDB_No),
CONSTRAINT "fk_Nutrient_No" FOREIGN KEY (Nutrient_No) REFERENCES SR_Nutrient_Definition (Nutrient_No)
);

\copy SR_Nutrient_Data FROM '/Users/bburciag/BiSD/KnowledgeBase/Sources/USDA/StandardReference/SR_NUT_DATA.txt.out' DELIMITER '|' CSV;


-- FLAVONOID DATA --

-- FL_Fd_Group --
DROP TABLE IF EXISTS FL_Fd_Group;
CREATE TABLE IF NOT EXISTS FL_Fd_Group
(
  FdGrp_Cd INT NOT NULL,
  FdGrp_Description VARCHAR NOT NULL,
CONSTRAINT "pk_FdGrp_Cd" PRIMARY KEY (FdGrp_Cd),
CONSTRAINT "fk_FdGrp_Cd" FOREIGN KEY (FdGrp_Cd) REFERENCES FL_Food_Description (FdGrp_Cd)
);

\copy FL_Fd_Group FROM '/home/bmb0205/BiSD/KnowledgeBase/Sources/USDA/Flavonoid/FL_FD_GROUP.txt.out' DELIMITER '|' CSV;

-- FL_Food_Description --
DROP TABLE IF EXISTS FL_Food_Description;
CREATE TABLE IF NOT EXISTS FL_Food_Description
(
  NDB_No INT NOT NULL,
  FdGrp_Cd INT NOT NULL,
  Long_Description VARCHAR NOT NULL,
  PRIMARY KEY (NDB_No),
  CONSTRAINT "fk_FdGrp_Cd" FOREIGN KEY (FdGrp_Cd) REFERENCES FL_Food_Description (FdGrp_Cd)
);

\copy FL_Fd_Group FROM '/home/bmb0205/BiSD/KnowledgeBase/Sources/USDA/Flavonoid/FL_FD_GROUP.txt.out' DELIMITER '|' CSV;

-- TEST QUERIES --

-- SELECT SR_Nutrient_Data.NDB_No, SR_Nutrient_Data.Nutrient_No, ndef.Nutrient_Des
-- FROM SR_Nutrient_Definition AS ndef
--   INNER JOIN SR_Nutrient_Data
--     ON SR_Nutrient_Data.Nutrient_No = ndef.Nutrient_No
-- WHERE SR_Nutrient_Data.NDB_No = 01001;
