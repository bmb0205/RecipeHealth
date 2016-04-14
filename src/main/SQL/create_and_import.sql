--
-- SQL script to create tables in recipehealth postgresql database and populate them from delimited files specified
--


-- CREATE SR TABLES IF NOT EXIST, THEN COPY DATA FROM PIPE DELIMITED FILE INTO TABLE --

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
CONSTRAINT "fk_NDB_No" FOREIGN KEY (NDB_No) REFERENCES Sr_Food_Description (NDB_No)
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
CONSTRAINT "fk_NDB_No" FOREIGN KEY (NDB_No) REFERENCES Sr_Food_Description (NDB_No),
CONSTRAINT "fk_Nutrient_No" FOREIGN KEY (Nutrient_No) REFERENCES Sr_Nutrient_Definition (Nutrient_No)
);

\copy SR_Nutrient_Data FROM '/Users/bburciag/BiSD/KnowledgeBase/Sources/USDA/StandardReference/SR_NUT_DATA.txt.out' DELIMITER '|' CSV;


-- SELECT SR_Nutrient_Data.NDB_No, SR_Nutrient_Data.Nutrient_No, ndef.Nutrient_Des
-- FROM SR_Nutrient_Definition AS ndef
--   INNER JOIN SR_Nutrient_Data
--     ON SR_Nutrient_Data.Nutrient_No = ndef.Nutrient_No
-- WHERE SR_Nutrient_Data.NDB_No = 01001;
