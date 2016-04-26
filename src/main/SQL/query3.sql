SELECT mesh.meshid, mesh.meshterm, pmid_mesh.pmid, pubmed_info.pmid, pubmed_info.title
FROM pmid_mesh
	INNER JOIN mesh
		ON LOWER(pmid_mesh.meshterm) = LOWER(mesh.meshterm)
	INNER JOIN pubmed_info
		ON pmid_mesh.pmid = pubmed_info.pmid

	WHERE mesh.meshterm = 'Flavonoids';