CREATE TABLE journalinfo AS
SELECT journal_id, count(*) AS size, collect_set(article_id) AS articles
FROM articlestest
GROUP BY journal_id