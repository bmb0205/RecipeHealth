

-- Since the year 2000, what studies were performed to analyze specific levels of the phytochemical Cyanidin?
--  What were their titles, journals, year published, and what foods were analyzed specifically?

SELECT
	all_food_description.long_desc,
	all_nutrient_definition.nutrient_desc,
	fl_data_src.year, fl_data_src.title, fl_data_src.journal
FROM all_food_description
	JOIN fl_data_srcln
		ON all_food_description.ndb_no = fl_data_srcln.ndb_no
	JOIN fl_data_src
		ON fl_data_src.datasrc_id = fl_data_srcln.datasrc_id
	JOIN all_nutrient_definition
		ON fl_data_srcln.nutrient_no = all_nutrient_definition.nutrient_no
WHERE fl_data_src.year > 2000 AND all_nutrient_definition.nutrient_desc = 'Cyanidin';


