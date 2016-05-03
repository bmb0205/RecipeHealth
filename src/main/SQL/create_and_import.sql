--
-- SQL script to create tables in recipehealth postgresql database and populate them from delimited files specified
--

DROP SCHEMA public CASCADE;
CREATE SCHEMA public;


-- EXTENSIONS --
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";


-- CREATE TABLES AND COPY DELIMITED DATA TO POPULATE THEM --

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
  Nutrient_Desc VARCHAR NOT NULL,
  PRIMARY KEY (Nutrient_No)
);
\copy ALL_Nutrient_Definition FROM '/home/bmb0205/BiSD/KnowledgeBase/Sources/USDA/ALL_NUTR_DEF.csv.out' DELIMITER '|' CSV;


-- SR_Weight --
-- DROP TABLE IF EXISTS SR_Weight CASCADE;
CREATE TABLE IF NOT EXISTS SR_Weight
(
  NDB_No INT NOT NULL,
  Seq INT NOT NULL,
  Amount VARCHAR NOT NULL,
  Measure_Des VARCHAR NOT NULL,
  Gram_Weight NUMERIC NOT NULL,
PRIMARY KEY (NDB_No, Seq),
CONSTRAINT fk_NDB_No FOREIGN KEY (NDB_No) REFERENCES All_Food_Description (NDB_No)
);
\copy SR_WEIGHT FROM '/home/bmb0205/BiSD/KnowledgeBase/Sources/USDA/StandardReference/SR_WEIGHT.txt.out' DELIMITER '|' CSV;


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
  Year NUMERIC,
  Journal VARCHAR NOT NULL,
PRIMARY KEY (DataSrc_ID)
);
\copy FL_Data_Src FROM '/home/bmb0205/BiSD/KnowledgeBase/Sources/USDA/Flavonoid/FL_DATA_SRC.txt.out' DELIMITER '|' CSV;


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
  Year NUMERIC,
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
  Nutrient_Val NUMERIC NOT NULL,
  CC VARCHAR,
PRIMARY KEY (NDB_No, Nutrient_No),
CONSTRAINT fk_NDB_No FOREIGN KEY (NDB_No) REFERENCES ALL_Food_Description (NDB_No),
CONSTRAINT fk_Nutrient_No FOREIGN KEY (Nutrient_No) REFERENCES ALL_Nutrient_Definition (Nutrient_No)
--   CONSTRAINT fk_Nutrient_No_sr FOREIGN KEY (Nutrient_No) REFERENCES SR_Nutrient_Definition (Nutrient_No)
);
\copy ALL_Food_Data FROM '/home/bmb0205/BiSD/KnowledgeBase/Sources/USDA/ALL_FOOD_DATA.csv.out' DELIMITER '|' CSV;



CREATE TABLE IF NOT EXISTS pubmed_info
(
  pmid INT NOT NULL,
  Title VARCHAR NOT NULL,
  Abstract VARCHAR NOT NULL,
PRIMARY KEY(pmid)
);
\copy pubmed_info FROM '/home/bmb0205/BiSD/KnowledgeBase/Sources/PubmedAdvancedSearch/pubmed_info.out' DELIMITER '|' CSV;

CREATE TABLE IF NOT EXISTS pmid_mesh_temp
(
  MeshTerm VARCHAR NOT NULL,
  PMID INT,
  pubmed_uuid uuid NOT NULL,
PRIMARY KEY(pubmed_uuid)
);
\copy pmid_mesh_temp FROM '/home/bmb0205/BiSD/KnowledgeBase/Sources/PubmedAdvancedSearch/pubmed_meshTerms.out' DELIMITER '|' CSV;

CREATE TABLE IF NOT EXISTS pmid_mesh
(
  MeshTerm VARCHAR NOT NULL,
  PMID INT,
  pubmed_uuid uuid NOT NULL,
PRIMARY KEY(pubmed_uuid),
CONSTRAINT fk_PMID FOREIGN KEY (PMID) REFERENCES pubmed_info (PMID)
);

CREATE TABLE IF NOT EXISTS mesh
(
  MeshID VARCHAR NOT NULL,
  MeshTerm VARCHAR NOT NULL,
  PRIMARY KEY (MeshID)
);
\copy mesh FROM '/home/bmb0205/BiSD/KnowledgeBase/Sources/csv_out/meshNodeOut.csv.out' DELIMITER '|' CSV;

CREATE INDEX mesh_meshterm_lc
  ON mesh (LOWER(meshterm));

INSERT INTO pmid_mesh
  SELECT MeshTerm, PMID, pubmed_uuid
  FROM pmid_mesh_temp
  WHERE pmid_mesh_temp.meshterm IN
        (
          SELECT meshterm FROM mesh
        );

CREATE INDEX pmid_mesh_meshterm_lc
  ON pmid_mesh (LOWER(meshterm));

DROP TABLE IF EXISTS pmid_mesh_temp;


CREATE TABLE IF NOT EXISTS entrez_gene
(
  gene_id VARCHAR NOT NULL,
  gene_desc VARCHAR NOT NULL,
  PRIMARY KEY (gene_id)
);
\copy entrez_gene FROM '/home/bmb0205/BiSD/KnowledgeBase/Sources/csv_out/condensed_All_Mammalia.gene_info.out' DELIMITER '|' CSV;


CREATE TABLE IF NOT EXISTS gene_to_pubmed_temp
(
  tax_id INT NOT NULL,
  gene_id VARCHAR NOT NULL,
  pmid INT NOT NULL,
PRIMARY KEY (gene_id, pmid)
);

\copy gene_to_pubmed_temp FROM '/home/bmb0205/BiSD/KnowledgeBase/Sources/csv_out/geneToPubmed.csv' DELIMITER '|' CSV;

CREATE TABLE IF NOT EXISTS gene_to_pubmed
(
  tax_id INT NOT NULL,
  gene_id VARCHAR NOT NULL,
  pmid INT NOT NULL,
PRIMARY KEY (gene_id, pmid),
CONSTRAINT fk_gene_id FOREIGN KEY (gene_id) REFERENCES entrez_gene (gene_id),
CONSTRAINT fk_pubmed_id FOREIGN KEY (pmid) REFERENCES pubmed_info (pmid)
);

INSERT INTO gene_to_pubmed
  SELECT tax_id, gene_id, pmid
  FROM gene_to_pubmed_temp
  WHERE gene_to_pubmed_temp.pmid IN
        (
          SELECT pmid FROM pubmed_info
        );
DROP TABLE IF EXISTS gene_to_pubmed_temp;



-- ALTER TABLE mesh ADD CONSTRAINT fk_MeshTerm FOREIGN KEY (MeshTerm) REFERENCES pmid_mesh (MeshTerm)


