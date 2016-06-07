CREATE TABLE from_to_journal AS
SELECT a.journal_id AS from_journal, b.journal_id AS to_journal,
count(a.article_refered) AS references
FROM (SELECT journal_id, article_refered
FROM articlestest
LATERAL VIEW explode(references) reftable AS article_refered) a
JOIN articlestest b
ON a.article_refered = b.article_id
GROUP BY a.journal_id, b.journal_id