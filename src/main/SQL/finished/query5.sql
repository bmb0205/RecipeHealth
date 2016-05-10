
-- QUESTION: What are the PMIDs and article titles that are associated with the phytochemicals found within 'Blueberries, raw' given they match exact MeSH Terms?

SELECT
	all_nutrient_definition.nutrient_desc,
	pmid_mesh.pmid,
	pubmed_info.title
FROM all_nutrient_definition
	JOIN all_food_data
		ON all_food_data.nutrient_no = all_nutrient_definition.nutrient_no
	JOIN all_food_description
		ON all_food_description.ndb_no = all_food_data.ndb_no
	JOIN pmid_mesh
		ON LOWER(pmid_mesh.meshterm) = LOWER(all_nutrient_definition.nutrient_desc)
	JOIN pubmed_info
		ON pmid_mesh.pmid = pubmed_info.pmid
WHERE all_food_description.long_desc LIKE 'Blueberries, raw'
			AND all_food_data.nutrient_val != ANY ('{0, 0.0, 0.00, 0.000}'::numeric[])
;