
-- Question 1: What fruits and vegetables are in the database and in what amounts, measures and weights are records kept?

SELECT
	all_food_description.long_desc,
	sr_weight.amount, sr_weight.measure_desc, sr_weight.gram_weight
FROM all_food_description
	JOIN fl_food_group
		ON all_food_description.fdgrp_cd = fl_food_group.fdgrp_cd
	JOIN sr_weight
		ON all_food_description.ndb_no = sr_weight.ndb_no
WHERE fl_food_group.fdgrp_description = 'Fruits and Fruit Juices'
	OR fl_food_group.fdgrp_description = 'Vegetables and Vegetable Products';