CREATE TABLE journal_references AS
SELECT a.from_journal, a.to_journal, a.references AS citations,
b.size AS journal_size, c.total_references
FROM from_to_journal a
JOIN journalinfo b
ON a.from_journal = b.journal_id
JOIN (SELECT from_journal, sum(references) AS total_references
FROM (SELECT from_journal, references
FROM from_to_journal) blabla
GROUP BY from_journal
) c
ON a.from_journal = c.from_journal