
-- QUESTION: What nutrients, both macro and micro, are found in this specific food item (1 cup raw bluerries) and in what amounts?

SELECT
	round((all_food_data.nutrient_val / 100.0) * sr_weight.gram_weight, 3),
	all_nutrient_definition.unit, all_nutrient_definition.nutrient_desc,
	all_food_description.long_desc
FROM all_nutrient_definition
	JOIN all_food_data
		ON all_food_data.nutrient_no = all_nutrient_definition.nutrient_no
	JOIN all_food_description
		ON all_food_description.ndb_no = all_food_data.ndb_no
	JOIN sr_weight
		ON all_food_description.ndb_no = sr_weight.ndb_no
	WHERE all_food_description.long_desc LIKE 'Blueberries, raw'
		AND sr_weight.amount = '1'
		AND sr_weight.measure_des = 'cup'
		AND all_food_data.nutrient_val != ANY ('{0, 0.0, 0.00, 0.000}'::numeric[])
;