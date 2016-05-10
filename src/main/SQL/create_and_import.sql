--
-- SQL script to create tables in recipehealth postgresql database and populate them from delimited files specified
--

-- DROP SCHEMA public CASCADE;
-- CREATE SCHEMA public;


-- EXTENSIONS --
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";


-- CREATE TABLES AND COPY DELIMITED DATA TO POPULATE THEM --

-- ALL_Food_Description --
CREATE TABLE IF NOT EXISTS ALL_Food_Description
(
  NDB_No INT NOT NULL,
  FdGrp_Cd INT,
  Long_Desc TEXT NOT NULL,
  PRIMARY KEY (NDB_No)
);
\copy ALL_Food_Description FROM '/home/bmb0205/BiSD/KnowledgeBase/Sources/USDA/ALL_FOOD_DES.csv.out' DELIMITER '|' CSV;


-- ALL_Nutrient_Definition --
CREATE TABLE IF NOT EXISTS ALL_Nutrient_Definition
(
  Nutrient_No INT NOT NULL,
  Unit TEXT NOT NULL,
  Nutrient_Desc TEXT NOT NULL,
  PRIMARY KEY (Nutrient_No)
);
\copy ALL_Nutrient_Definition FROM '/home/bmb0205/BiSD/KnowledgeBase/Sources/USDA/ALL_NUTR_DEF.csv.out' DELIMITER '|' CSV;


-- SR_Weight --
CREATE TABLE IF NOT EXISTS SR_Weight
(
  NDB_No INT NOT NULL,
  Seq INT NOT NULL,
  Amount TEXT NOT NULL,
  Measure_Desc TEXT NOT NULL,
  Gram_Weight NUMERIC NOT NULL,
PRIMARY KEY (NDB_No, Seq),
CONSTRAINT fk_NDB_No FOREIGN KEY (NDB_No) REFERENCES All_Food_Description (NDB_No)
);
\copy SR_WEIGHT FROM '/home/bmb0205/BiSD/KnowledgeBase/Sources/USDA/StandardReference/SR_WEIGHT.txt.out' DELIMITER '|' CSV;


-- FL_Fd_Group --
CREATE TABLE IF NOT EXISTS FL_Food_Group_temp
(
  FdGrp_Cd INT NOT NULL,
  FdGrp_Description TEXT NOT NULL,
PRIMARY KEY (FdGrp_Cd)
);
\copy FL_Food_Group_temp FROM '/home/bmb0205/BiSD/KnowledgeBase/Sources/USDA/Flavonoid/FL_FD_GROUP.txt.out' DELIMITER '|' CSV;

CREATE TABLE IF NOT EXISTS FL_Food_Group
(
  FdGrp_Cd INT NOT NULL,
  FdGrp_Description TEXT NOT NULL,
PRIMARY KEY (FdGrp_Cd)
);

INSERT INTO FL_Food_Group
  SELECT FdGrp_Cd, FdGrp_Description
  FROM FL_Food_Group_temp
  WHERE FL_Food_Group_temp.FdGrp_Cd IN
        (
          SELECT FdGrp_Cd FROM ALL_Food_Description
        );
DROP TABLE IF EXISTS FL_Food_Group_temp;


-- FL_Data_Src --
CREATE TABLE IF NOT EXISTS FL_Data_Src
(
  DataSrc_ID TEXT NOT NULL,
  Title TEXT,
  Year NUMERIC,
  Journal TEXT NOT NULL,
PRIMARY KEY (DataSrc_ID)
);
\copy FL_Data_Src FROM '/home/bmb0205/BiSD/KnowledgeBase/Sources/USDA/Flavonoid/FL_DATA_SRC.txt.out' DELIMITER '|' CSV;



-- FL_Data_Srcln --
CREATE TABLE IF NOT EXISTS FL_Data_Srcln_temp
(
  NDB_No INT NOT NULL,
  Nutrient_No INT NOT NULL,
  DataSrc_ID TEXT NOT NULL,
  fl_uuid UUID NOT NULL,
PRIMARY KEY (fl_uuid)
--?? CONSTRAINT fk_DataSrc_ID FOREIGN KEY (DataSrc_ID) REFERENCES FL_Data_Src (DataSrc_ID)
);
\copy FL_Data_Srcln_temp FROM '/home/bmb0205/BiSD/KnowledgeBase/Sources/USDA/Flavonoid/FL_DATSRCLN.txt.out' DELIMITER '|' CSV;


CREATE TABLE IF NOT EXISTS FL_Data_Srcln
(
  NDB_No INT NOT NULL,
  Nutrient_No INT NOT NULL,
  DataSrc_ID TEXT NOT NULL,
  fl_uuid UUID NOT NULL,
PRIMARY KEY (fl_uuid),
CONSTRAINT fk_DataSrc_ID FOREIGN KEY (DataSrc_ID) REFERENCES FL_Data_Src (DataSrc_ID),
CONSTRAINT fk_Nutrient_No FOREIGN KEY (Nutrient_No) REFERENCES All_Nutrient_Definition (Nutrient_No),
CONSTRAINT fk_NDB_No FOREIGN KEY (NDB_No) REFERENCES All_Food_Description (NDB_No)
);

INSERT INTO FL_Data_Srcln
  SELECT NDB_No, Nutrient_No, DataSrc_ID, fl_uuid
  FROM FL_Data_Srcln_temp
  WHERE FL_Data_Srcln_temp.nutrient_no IN
        (
          SELECT nutrient_No FROM ALL_Nutrient_Definition
        );
DROP TABLE IF EXISTS FL_Data_Srcln_temp;


-- ISOFLAVONE DATA --

-- ISO_Data_Src --
CREATE TABLE IF NOT EXISTS ISO_Data_Src
(
  IsoDataSrc_ID TEXT NOT NULL,
  Title TEXT,
  Year NUMERIC,
  Journal TEXT NOT NULL,
PRIMARY KEY (IsoDataSrc_ID)
);
\copy ISO_Data_Src FROM '/home/bmb0205/BiSD/KnowledgeBase/Sources/USDA/Isoflavone/ISO_DATA_SRC.csv.out' DELIMITER '|' CSV;


-- ISO_Data_Srcln --
CREATE TABLE IF NOT EXISTS ISO_Data_Srcln_temp
(
  NDB_No INT NOT NULL,
  Nutrient_No INT NOT NULL,
  IsoDataSrc_ID TEXT NOT NULL,
  iso_uuid UUID NOT NULL,
PRIMARY KEY (iso_uuid)
);
\copy ISO_Data_Srcln_temp FROM '/home/bmb0205/BiSD/KnowledgeBase/Sources/USDA/Isoflavone/ISO_DATA_SRCLN.csv.out' DELIMITER '|' CSV;


CREATE TABLE IF NOT EXISTS ISO_Data_Srcln
(
  NDB_No INT NOT NULL,
  Nutrient_No INT NOT NULL,
  IsoDataSrc_ID TEXT NOT NULL,
  iso_uuid UUID NOT NULL,
PRIMARY KEY (iso_uuid),
CONSTRAINT fk_IsoDataSrc_ID FOREIGN KEY (IsoDataSrc_ID) REFERENCES ISO_Data_Src (IsoDataSrc_ID),
CONSTRAINT fk_NDB_No FOREIGN KEY (NDB_No) REFERENCES ALL_Food_Description (NDB_No),
CONSTRAINT fk_Nutrient_No FOREIGN KEY (Nutrient_No) REFERENCES ALL_Nutrient_Definition (Nutrient_No)
);

INSERT INTO ISO_Data_Srcln
    SELECT *
    FROM ISO_Data_Srcln_temp
      WHERE ISO_Data_Srcln_temp.NDB_No IN
        (
          SELECT NDB_No FROM ALL_Food_Description
        );
DROP TABLE IF EXISTS ISO_Data_Srcln_temp;


-- ALL_Food_Data --
CREATE TABLE IF NOT EXISTS ALL_Food_Data
(
  NDB_No INT NOT NULL,
  Nutrient_No INT NOT NULL,
  Nutrient_Val NUMERIC NOT NULL,
  CC TEXT,
PRIMARY KEY (NDB_No, Nutrient_No),
CONSTRAINT fk_NDB_No FOREIGN KEY (NDB_No) REFERENCES ALL_Food_Description (NDB_No),
CONSTRAINT fk_Nutrient_No FOREIGN KEY (Nutrient_No) REFERENCES ALL_Nutrient_Definition (Nutrient_No)
);
\copy ALL_Food_Data FROM '/home/bmb0205/BiSD/KnowledgeBase/Sources/USDA/ALL_FOOD_DATA.csv.out' DELIMITER '|' CSV;

-- pubmed_info --
CREATE TABLE IF NOT EXISTS pubmed_info
(
  pmid INT NOT NULL,
  Title TEXT NOT NULL,
  Abstract TEXT NOT NULL,
PRIMARY KEY(pmid)
);
\copy pubmed_info FROM '/home/bmb0205/BiSD/KnowledgeBase/Sources/PubmedAdvancedSearch/pubmed_info.out' DELIMITER '|' CSV;

-- pmid_mesh --
CREATE TABLE IF NOT EXISTS pmid_mesh_temp
(
  MeshTerm TEXT NOT NULL,
  pmid INT,
  pubmed_uuid uuid NOT NULL,
PRIMARY KEY(pubmed_uuid)
);
\copy pmid_mesh_temp FROM '/home/bmb0205/BiSD/KnowledgeBase/Sources/PubmedAdvancedSearch/pubmed_meshTerms.out' DELIMITER '|' CSV;

-- mesh --
CREATE TABLE IF NOT EXISTS mesh
(
  MeshID TEXT NOT NULL,
  MeshTerm TEXT NOT NULL,
  PRIMARY KEY (MeshTerm)
);
\copy mesh FROM '/home/bmb0205/BiSD/KnowledgeBase/Sources/csv_out/meshNodeOut.csv.out' DELIMITER '|' CSV;


CREATE TABLE IF NOT EXISTS pmid_mesh
(
  MeshTerm TEXT NOT NULL,
  pmid INT NOT NULL,
  pubmed_uuid uuid NOT NULL,
PRIMARY KEY(pubmed_uuid)
);

CREATE INDEX mesh_meshterm_lc
  ON mesh (LOWER(meshterm));

INSERT INTO pmid_mesh
  SELECT MeshTerm, pmid, pubmed_uuid
  FROM pmid_mesh_temp
  WHERE pmid_mesh_temp.meshterm IN
        (
          SELECT meshterm FROM mesh
        );

CREATE INDEX pmid_mesh_meshterm_lc
  ON pmid_mesh (LOWER(meshterm));

DROP TABLE IF EXISTS pmid_mesh_temp;

ALTER TABLE pmid_mesh ADD CONSTRAINT fk_MeshTerm FOREIGN KEY (MeshTerm) REFERENCES mesh (MeshTerm);
ALTER TABLE pmid_mesh ADD CONSTRAINT fk_PMID FOREIGN KEY (pmid) REFERENCES pubmed_info (pmid);

-- entrez gene --
CREATE TABLE IF NOT EXISTS entrez_gene
(
  gene_id TEXT NOT NULL,
  gene_desc TEXT NOT NULL,
  PRIMARY KEY (gene_id)
);
\copy entrez_gene FROM '/home/bmb0205/BiSD/KnowledgeBase/Sources/csv_out/condensed_All_Mammalia.gene_info.out' DELIMITER '|' CSV;

-- gene_to_pubmed --
CREATE TABLE IF NOT EXISTS gene_to_pubmed_temp
(
  tax_id INT NOT NULL,
  gene_id TEXT NOT NULL,
  pmid INT NOT NULL,
PRIMARY KEY (gene_id, pmid)
);

\copy gene_to_pubmed_temp FROM '/home/bmb0205/BiSD/KnowledgeBase/Sources/csv_out/geneToPubmed.csv' DELIMITER '|' CSV;


CREATE TABLE IF NOT EXISTS gene_to_pubmed
(
  tax_id INT NOT NULL,
  gene_id TEXT NOT NULL,
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
