package processing

object Constants {

    val createTableDefinitionGetterFunction =
"""
CREATE OR REPLACE FUNCTION public.get_table_definition (
    p_schema_name character varying,
    p_table_name character varying
)
    RETURNS SETOF TEXT
    AS $BODY$
BEGIN
    RETURN query
    WITH table_rec AS (
        SELECT
            c.relname, n.nspname, c.oid
        FROM
            pg_catalog.pg_class c
            LEFT JOIN pg_catalog.pg_namespace n ON n.oid = c.relnamespace
        WHERE
            relkind = 'r'
            AND n.nspname = p_schema_name
            AND c.relname LIKE p_table_name
        ORDER BY
            c.relname
    ),
    col_rec AS (
        SELECT
            a.attname AS colname,
            pg_catalog.format_type(a.atttypid, a.atttypmod) AS coltype,
            a.attrelid AS oid,
            ' DEFAULT ' || (
                SELECT
                    pg_catalog.pg_get_expr(d.adbin, d.adrelid)
                FROM
                    pg_catalog.pg_attrdef d
                WHERE
                    d.adrelid = a.attrelid
                    AND d.adnum = a.attnum
                    AND a.atthasdef) AS column_default_value,
            CASE WHEN a.attnotnull = TRUE THEN
                'NOT NULL'
            ELSE
                'NULL'
            END AS column_not_null,
            a.attnum AS attnum
        FROM
            pg_catalog.pg_attribute a
        WHERE
            a.attnum > 0
            AND NOT a.attisdropped
        ORDER BY
            a.attnum
    ),
    con_rec AS (
        SELECT
            conrelid::regclass::text AS relname,
            n.nspname,
            conname,
            pg_get_constraintdef(c.oid) AS condef,
            contype,
            conrelid AS oid
        FROM
            pg_constraint c
            JOIN pg_namespace n ON n.oid = c.connamespace
    ),
    glue AS (
        SELECT
            format( E'-- %1$I.%2$I definition\n\n-- Drop table\n\n-- DROP TABLE IF EXISTS %1$I.%2$I\n\nCREATE TABLE %1$I.%2$I (\n', table_rec.nspname, table_rec.relname) AS top,
            format( E'\n);\n\n\n-- adempiere.wmv_ghgaudit foreign keys\n\n', table_rec.nspname, table_rec.relname) AS bottom,
            oid
        FROM
            table_rec
    ),
    cols AS (
        SELECT
            string_agg(format('    %I %s%s %s', colname, coltype, column_default_value, column_not_null), E',\n') AS lines,
            oid
        FROM
            col_rec
        GROUP BY
            oid
    ),
    constrnt AS (
        SELECT
            string_agg(format('    CONSTRAINT %s %s', con_rec.conname, con_rec.condef), E',\n') AS lines,
            oid
        FROM
            con_rec
        WHERE
            contype <> 'f'
        GROUP BY
            oid
    ),
    frnkey AS (
        SELECT
            string_agg(format('ALTER TABLE %I.%I ADD CONSTRAINT %s %s', nspname, relname, conname, condef), E';\n') AS lines,
            oid
        FROM
            con_rec
        WHERE
            contype = 'f'
        GROUP BY
            oid
    )
    SELECT
        concat(glue.top, cols.lines, E',\n', constrnt.lines, glue.bottom, frnkey.lines, ';')
    FROM
        glue
        JOIN cols ON cols.oid = glue.oid
        LEFT JOIN constrnt ON constrnt.oid = glue.oid
        LEFT JOIN frnkey ON frnkey.oid = glue.oid;
END;
$BODY$
LANGUAGE plpgsql
"""

    val getObjectsListQuery = """
SELECT
    n.nspname AS schema_name,
    c.relname AS object_name,
    CASE c.relkind
        WHEN 'r' THEN 'TABLE'
        WHEN 'm' THEN 'MATERIALIZED_VIEW'
        WHEN 'i' THEN 'INDEX'
        WHEN 'S' THEN 'SEQUENCE'
        WHEN 'v' THEN 'VIEW'
        WHEN 'c' THEN 'TYPE'
        else c.relkind::TEXT
    end AS object_type,
    CASE c.relkind
        WHEN 'r' THEN (SELECT get_table_definition(n.nspname::TEXT, c.relname::TEXT))
        WHEN 'i' THEN (SELECT indexdef FROM pg_indexes WHERE indexname = c.relname and schemaname = n.nspname)
        WHEN 'v' THEN (SELECT pg_get_viewdef(c.relname::regclass))
        ELSE NULL
    END AS object_ddl
FROM pg_class c
JOIN pg_namespace n
ON n.oid = c.relnamespace
WHERE n.nspname NOT IN ('information_schema', 'pg_catalog')
    and n.nspname NOT LIKE 'pg_toast%'
UNION ALL
SELECT
	routine_schema AS schema_name,
	routine_name AS object_name,
	routine_type AS object_type,
	(SELECT pg_get_functiondef(routine_name::regproc)) AS object_ddl
FROM information_schema.routines
WHERE routine_type = 'FUNCTION' AND specific_schema NOT IN ('pg_catalog', 'information_schema')
UNION ALL
SELECT
	n.nspname AS schema_name,
	p.proname AS procedure_name,
	'PROCEDURE' AS object_type,
	(SELECT pg_get_functiondef(p.oid::regproc)) AS object_ddl
FROM pg_namespace n
JOIN pg_proc p
ON p.pronamespace = n.oid
WHERE p.prokind = 'procedure' AND n.nspname NOT IN ('pg_catalog', 'information_schema')
UNION ALL
SELECT
	trigger_schema AS schema_name,
	trigger_name AS object_name,
	'TRIGGER' AS object_type,
	pg_get_triggerdef(trigger_name::regproc)
FROM information_schema.triggers
WHERE trigger_schema NOT IN ('pg_catalog', 'information_schema')
"""

    val dropTableDefinitionGetterFunction = """
DROP FUNCTION get_table_definition(
    p_schema_name character varying,
    p_table_name character varying
)
"""
}
