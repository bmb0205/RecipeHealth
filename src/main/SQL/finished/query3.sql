
-- -- What genes and pubmed articles are associated with the phytochemical 'catechin' in relation to cardiovascular disease or inflammation?

SELECT
	pmid_mesh.meshterm,
	entrez_gene.gene_id, entrez_gene.gene_desc,
	pubmed_info.pmid, pubmed_info.title, pubmed_info.abstract
FROM pmid_mesh
	JOIN pubmed_info
		ON pmid_mesh.pmid = pubmed_info.pmid
	JOIN gene_to_pubmed
		ON pubmed_info.pmid = gene_to_pubmed.pmid
	JOIN entrez_gene
		ON gene_to_pubmed.gene_id = entrez_gene.gene_id

WHERE pmid_mesh.meshterm = 'Catechin'
;