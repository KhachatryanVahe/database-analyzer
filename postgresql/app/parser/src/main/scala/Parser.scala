package parser

import scala.collection.mutable.Map
import scala.jdk.CollectionConverters._

object PostgresParser {
  val features = scala.collection.immutable.Map(
    "alias_clause" -> "nAlias",
    "cast_expression" -> "nCast",
    "constr_body" -> "nConstraint",
    "groupby_clause" -> "nGroupBy",
    "interval_field" -> "nIntervalLiteral",
    "orderby_clause" -> "nSorting",
    "null_ordering" -> "nNullOrdering",
    "exception_statement" -> "nExceptionStatement",
    "join_kw" -> "nJoin",
    "if_exists" -> "nExists",
    "replace_kw" -> "nReplace",
    "case_statement" -> "nCaseStatement",
    "schema_create" -> "nCreate",
    "current_date_kw" -> "nCurrentDate",
    "current_time_kw" -> "nCurrentTime",
    "current_timestamp_kw" -> "nCurrentTimestamp",
    "like_type" -> "nLike",
    "fetch_kw" -> "nFetch",
    "sign" -> "nSign",
    "intersect_kw" -> "nIntersect",
    "union_kw" -> "nUnion",
    "except_kw" -> "nExcept",
    "select_stmt" -> "nSelect",
    "insert_stmt_for_psql" -> "nInsert",
    "update_stmt_for_psql" -> "nUpdate",
    "delete_stmt_for_psql" -> "nDelete",
    "loop_statement" -> "nLoop",
    "if_statement" -> "nIf",
    "assign_stmt" -> "nVarAssign",
    "integer_type" -> "nIntegerCol",
    "floating_point_type" -> "nFloatingPointCol",
    "fixed_point_type" -> "nFixedPointCol",
    "character_type" -> "nCharacterCol",
    "datetime_type" -> "nDatetimeCol",
  )

  val featureWeights = scala.collection.immutable.Map(
    "nAlias" -> 2,
    "nCast" -> 3,
    "nConstraint" -> 4,
    "nGroupBy" -> 8,
    "nIntervalLiteral" -> 5,
    "nSorting" -> 8,
    "nNullOrdering" -> 2,
    "nExceptionStatement" -> 5,
    "nJoin" -> 8,
    "nExists" -> 4,
    "nReplace" -> 5,
    "nCaseStatement" -> 6,
    "nCreate" -> 6,
    "nCurrentDate" -> 1,
    "nCurrentTime" -> 1,
    "nCurrentTimestamp" -> 1,
    "nLike" -> 2,
    "nFetch" -> 2,
    "nSign" -> 1,
    "nIntersect" -> 7,
    "nUnion" -> 7,
    "nExcept" -> 7,
    "nSelect" -> 5,
    "nInsert" -> 5,
    "nUpdate" -> 5,
    "nDelete" -> 5,
    "nLoop" -> 10,
    "nIf" -> 4,
    "nVarAssign" -> 4,
    "nIntegerCol" -> 1,
    "nFloatingPointCol" -> 1,
    "nFixedPointCol" -> 1,
    "nCharacterCol" -> 1,
    "nDatetimeCol" -> 1,
  )

  val queryTypes = scala.collection.immutable.Map(
    "select_stmt" -> "SELECT",
    "insert_stmt_for_psql" -> "INSERT",
    "merge_stmt_for_psql" -> "MERGE",
    "update_stmt_for_psql" -> "UPDATE",
    "delete_stmt_for_psql" -> "DELETE",
    "alter_aggregate_statement" -> "ALTER_AGGREGATE",
    "alter_collation_statement" -> "ALTER_COLLATION",
    "alter_conversion_statement" -> "ALTER_CONVERSION",
    "alter_default_privileges_statement" -> "ALTER_DEFAULT_PRIVILEGES",
    "alter_database_statement" -> "ALTER_DATABASE",
    "alter_domain_statement" -> "ALTER_DOMAIN",
    "alter_event_trigger_statement" -> "ALTER_EVENT_TRIGGER",
    "alter_extension_statement" -> "ALTER_EXTENSION",
    "alter_foreign_data_wrapper" -> "ALTER_FOREIGN_DA",
    "alter_fts_statement" -> "ALTER_FTS",
    "alter_function_statement" -> "ALTER_FUNCTION",
    "alter_group_statement" -> "ALTER_GROUP",
    "alter_index_statement" -> "ALTER_INDEX",
    "alter_language_statement" -> "ALTER_LANGUAGE",
    "alter_materialized_view_statement" -> "ALTER_MATERIALIZED_VIEW",
    "alter_operator_class_statement" -> "ALTER_OPERATOR_CLASS",
    "alter_operator_family_statement" -> "ALTER_OPERATOR_FAMILY",
    "alter_operator_statement" -> "ALTER_OPERATOR",
    "alter_owner_statement" -> "ALTER_OWNER",
    "alter_policy_statement" -> "ALTER_POLICY",
    "alter_publication_statement" -> "ALTER_PUBLICATION",
    "alter_rule_statement" -> "ALTER_RULE",
    "alter_schema_statement" -> "ALTER_SCHEMA",
    "alter_sequence_statement" -> "ALTER_SEQUENCE",
    "alter_server_statement" -> "ALTER_SERVER",
    "alter_statistics_statement" -> "ALTER_STATISTICS",
    "alter_subscription_statement" -> "ALTER_SUBSCRIPTION",
    "alter_table_statement" -> "ALTER_TABLE",
    "alter_tablespace_statement" -> "ALTER_TABLESPACE",
    "alter_trigger_statement" -> "ALTER_TRIGGER",
    "alter_type_statement" -> "ALTER_TYPE",
    "alter_user_mapping_statement" -> "ALTER_USER_MAPPING",
    "alter_user_or_role_statement" -> "ALTER_USER_OR_ROLE",
    "alter_view_statement" -> "ALTER_VIEW",
    "copy_statement" -> "COPY",
    "create_database_statement" -> "CREATE_DATABASE",
    "create_extension_statement" -> "CREATE_EXTENSION",
    "create_foreign_table_statement" -> "CREATE_FOREIGN_TABLE",
    "create_function_statement" -> "CREATE_FUNCTION",
    "create_index_statement" -> "CREATE_INDEX",
    "create_operator_statement" -> "CREATE_OPERATOR",
    "create_schema_statement" -> "CREATE_SCHEMA",
    "create_sequence_statement" -> "CREATE_SEQUENCE",
    "create_server_statement" -> "CREATE_SERVER",
    "create_table_as_statement" -> "CREATE_TABLE",
    "create_table_statement" -> "CREATE_TABLE",
    "create_tablespace_statement" -> "CREATE_TABLESPACE",
    "create_trigger_statement" -> "CREATE_TRIGGER",
    "create_type_statement" -> "CREATE_TYPE",
    "create_user_mapping_statement" -> "CREATE_USER_MAPPING",
    "create_user_or_role_statement" -> "CREATE_USER_OR_ROLE",
    "create_view_statement" -> "CREATE_VIEW",
    "drop_cast_statement" -> "DROP_CAST",
    "drop_constraint" -> "DROP_CONSTRAINT",
    "drop_database_statement" -> "DROP_DATABASE",
    "drop_function_statement" -> "DROP_FUNCTION",
    "drop_operator_class_statement" -> "DROP_OPERATOR_CLASS",
    "drop_operator_family_statement" -> "DROP_OPERATOR_FAMILY",
    "drop_operator_statement" -> "DROP_OPERATOR",
    "drop_owned_statement" -> "DROP_OWNED",
    "drop_policy_statement" -> "DROP_POLICY",
    "drop_rule_statement" -> "DROP_RULE",
    "drop_statement" -> "DROP",
    "drop_trigger_statement" -> "DROP_TRIGGER",
    "drop_user_mapping_statement" -> "DROP_USER_MAPPING",
    "truncate_stmt" -> "TRUNCATE_TABLE",
    "transaction_statement" -> "TRANSACTION",
    "set_statement" -> "SET",
    "schema_import" -> "IMPORT_SCHEMA",
    "declare_statement" -> "DECLARE",
    "execute_statement" -> "EXECUTE",
    "explain_statement" -> "EXPLAIN",
    "reindex_stmt" -> "REINDEX",
    "show_statement" -> "SHOW"
  )

  def parse(query: String): Map[String, Integer] = {
    var processedQuery = query.trim()
    if (processedQuery.takeRight(1) != ";") {
      processedQuery += ";"
    }
    val listener = PgParser.parse(processedQuery)
    return (listener.getFeatureMap().asScala)
  }

  def getInfo(input: String): (Map[String, Integer], Map[String, Integer], Int, String, String) = {
    var rules = Map[String, Integer]()
    var attributes = Map[String, Integer]()
    var score = 0
    var queryType: String = null
    var error: String = null
    try {
      val (r) = parse(input)
      rules = r
    } catch {
      case e: Throwable => {
        error = e.getMessage()
      }
    }

    for ((ruleName, ruleCount) <- rules) {
      if (features.keySet.exists(_ == ruleName)) {
        val featureName = features(ruleName)
        if (attributes.keySet.exists(_ == featureName)) {
          attributes(featureName) = attributes(featureName) + ruleCount
        } else {
          attributes(featureName) = ruleCount
        }
        score += featureWeights(featureName) * ruleCount
      }
      if (queryType == null && queryTypes.keySet.exists(_ == ruleName)) {
        queryType = queryTypes(ruleName)
      }
    }

    return (attributes, rules, score, queryType, error)
  }
}
