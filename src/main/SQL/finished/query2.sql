-- What are the titles and abstracts for all of the published articles involving 'Catechin'

SELECT 
	mesh.meshid, mesh.meshterm,
	pmid_mesh.pmid,
	pubmed_info.title, pubmed_info.abstract
FROM pmid_mesh
	INNER JOIN mesh
		ON LOWER(pmid_mesh.meshterm) = LOWER(mesh.meshterm)
	INNER JOIN pubmed_info
		ON pmid_mesh.pmid = pubmed_info.pmid
	WHERE LOWER(pmid_mesh.meshterm) LIKE 'catechin' 
;