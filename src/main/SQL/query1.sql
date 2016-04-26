CREATE VIEW testview AS SELECT all_food_data.ndb_no, all_food_data.nutrient_no, all_nutrient_definition.nutrient_des, all_food_description.long_desc, all_food_data.nutrient_val
FROM all_nutrient_definition, all_food_data, all_food_description
	WHERE all_food_data.ndb_no = 16014 
	AND all_food_data.ndb_no = all_food_description.ndb_no
	AND all_food_data.nutrient_no = all_nutrient_definition.nutrient_no;

SELECT * FROM testview;


