
-- This query will return the amount of all detected nutrients or this specified food item in the amount chosen,
-- along with any PMIDs published with full MeSH terms matching the nutrient description. (specificity still needs to be added e.g. matching 'Catechin-(+)' to 'Catechin'
-- In this example, I have chosen 'Blueberries, raw' as the desired food item

-- QUESTION: What are the PMIDs and article titles that are associated with the phytochemicals found within this food (given they match exact MeSH Terms)?

SELECT
	round((all_food_data.nutrient_val / 100.0) * sr_weight.gram_weight, 3),
	all_nutrient_definition.unit, all_nutrient_definition.nutrient_desc,
	all_food_description.long_desc,
	pmid_mesh.pmid,
	pubmed_info.title
FROM all_nutrient_definition
	JOIN all_food_data
		ON all_food_data.nutrient_no = all_nutrient_definition.nutrient_no
	JOIN all_food_description
		ON all_food_description.ndb_no = all_food_data.ndb_no
	JOIN sr_weight
		ON all_food_description.ndb_no = sr_weight.ndb_no
	JOIN pmid_mesh
		ON LOWER(pmid_mesh.meshterm) = LOWER(all_nutrient_definition.nutrient_desc)
	JOIN pubmed_info
		ON pmid_mesh.pmid = pubmed_info.pmid
	WHERE all_food_description.long_desc LIKE 'Blueberries, raw'
		AND sr_weight.amount = '1'
		AND sr_weight.measure_des = 'cup'
	AND all_food_data.nutrient_val != ANY ('{0, 0.0, 0.00, 0.000}'::numeric[])
;