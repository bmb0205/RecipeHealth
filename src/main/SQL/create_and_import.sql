--
-- SQL script to create tables in recipehealth postgresql database and populate them from delimited files specified
--

-- SR DATA
DROP TABLE SR_Food_Description;
DROP TABLE SR_Weight;
DROP TABLE SR_Nutrient_Definition;
DROP TABLE SR_Nutrient_Data;

CREATE TABLE IF NOT EXISTS SR_Food_Description
(
NDB_No INT NOT NULL,
Food_Group_Cd INT NOT NULL,
Long_Description TEXT NOT NULL,
PRIMARY KEY (NDB_No)
);

\copy SR_Food_Description FROM '/Users/bburciag/BiSD/KnowledgeBase/Sources/USDA/StandardReference/SR_FOOD_DES.txt.out' DELIMITER '|' CSV;

CREATE TABLE IF NOT EXISTS SR_Nutrient_Data
(
NDB_No INT NOT NULL,
Nutrient_No INT NOT NULL,
Nutrient_Val VARCHAR NOT NULL,
PRIMARY KEY (NDB_No, Nutrient_No)
);

\copy SR_Nutrient_Data FROM '/Users/bburciag/BiSD/KnowledgeBase/Sources/USDA/StandardReference/SR_NUT_DATA.txt.out' DELIMITER '|' CSV;

CREATE TABLE IF NOT EXISTS SR_Nutrient_Definition
(
Nutrient_No INT NOT NULL,
Units VARCHAR NOT NULL,
Nutrient_Des VARCHAR NOT NULL,
PRIMARY KEY (Nutrient_No)
);

\copy SR_Nutrient_Definition FROM '/Users/bburciag/BiSD/KnowledgeBase/Sources/USDA/StandardReference/SR_NUTR_DEF.txt.out' DELIMITER '|' CSV;

CREATE TABLE IF NOT EXISTS SR_Weight
(
NDB_No INT NOT NULL,
Seq INT NOT NULL,
Amount VARCHAR NOT NULL,
Measure_Des VARCHAR NOT NULL,
Gram_Weight VARCHAR NOT NULL,
PRIMARY KEY (NDB_No, Seq)
);

\copy SR_WEIGHT FROM '/Users/bburciag/BiSD/KnowledgeBase/Sources/USDA/StandardReference/SR_WEIGHT.txt.out' DELIMITER '|' CSV;