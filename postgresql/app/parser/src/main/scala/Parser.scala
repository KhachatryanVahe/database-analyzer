package parser

import scala.collection.mutable.Map
import scala.jdk.CollectionConverters._

object PostgresParser {
  val features = scala.collection.immutable.Map(
    "alias_clause" -> "Alias",
    "cast_expression" -> "Cast",
    "constr_body" -> "Constraint",
    "groupby_clause" -> "GroupBy",
    "interval_field" -> "IntervalLiteral",
    "orderby_clause" -> "Sorting",
    "null_ordering" -> "NullOrdering",
    "exception_statement" -> "ExceptionStatement",
    "join_kw" -> "Join",
    "if_exists" -> "Exists",
    "replace_kw" -> "Replace",
    "case_statement" -> "CaseStatement",
    "schema_create" -> "Create",
    "current_date_kw" -> "CurrentDate",
    "current_time_kw" -> "CurrentTime",
    "current_timestamp_kw" -> "CurrentTimestamp",
    "like_type" -> "Like",
    "fetch_kw" -> "Fetch",
    "sign" -> "Sign",
    "intersect_kw" -> "Intersect",
    "union_kw" -> "Union",
    "except_kw" -> "Except",
    "select_stmt" -> "Select",
    "insert_stmt_for_psql" -> "Insert",
    "update_stmt_for_psql" -> "Update",
    "delete_stmt_for_psql" -> "Delete",
    "loop_statement" -> "Loop",
    "if_statement" -> "If",
    "assign_stmt" -> "VarAssign",
    "integer_type" -> "IntegerCol",
    "floating_point_type" -> "FloatingPointCol",
    "fixed_point_type" -> "FixedPointCol",
    "character_type" -> "CharacterCol",
    "datetime_type" -> "DatetimeCol",
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

  def getInfo(input: String): (Map[String, Integer], Map[String, Integer], String) = {
    var rules = Map[String, Integer]()
    var attributes = Map[String, Integer]()
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
      }
      if (queryType == null && queryTypes.keySet.exists(_ == ruleName)) {
        queryType = queryTypes(ruleName)
      }
    }

    return (attributes, rules, queryType)
  }
}
