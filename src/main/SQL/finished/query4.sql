
-- What studies were performed to analyze the specific phytochemical Cyanidin?
-- What were their titles, journals and year published and what foods were analyzed specifically?

SELECT
	all_nutrient_definition.nutrient_desc,
	all_food_description.long_desc,
	fl_data_src.year, fl_data_src.title, fl_data_src.journal
FROM all_food_description
	JOIN fl_data_srcln
		ON fl_data_srcln.ndb_no = all_food_description.ndb_no
	JOIN fl_data_src
		ON fl_data_srcln.datasrc_id = fl_data_src.datasrc_id
	JOIN all_nutrient_definition
		ON fl_data_srcln.nutrient_no = all_nutrient_definition.nutrient_no
	WHERE all_nutrient_definition.nutrient_desc = 'Cyanidin'
;