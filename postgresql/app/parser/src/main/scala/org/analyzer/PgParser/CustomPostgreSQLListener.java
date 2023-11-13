package org.analyzer.PgParser;

import org.antlr.v4.runtime.ParserRuleContext;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;
import java.util.HashMap;
import java.util.Collections;

class CustomPostgreSQLListener extends PostgreSQLBaseListener {
  // private final Set<String> ignoredrulesSet = Collections.unmodifiableSet(new HashSet<>(Arrays.asList(
  //   "unary_operator"
  // )));

  // private final Set<String> withValuerulesSet = Collections.unmodifiableSet(new HashSet<>(Arrays.asList(
  //   "ident_name",
  //   "base_table",
  //   "comp_operator",
  //   "alias",
  //   "type_specifier",
  //   "byteint_kw",
  //   "integer_type",
  //   "varbyte_type",
  //   "byte_type",
  //   "float_type",
  //   "date_type",
  //   "period_type",
  //   "time_type",
  //   "timestamp_type",
  //   "numeric_type",
  //   "number_type",
  //   "string_type",
  //   "interval_type",
  //   "create_function_stmt_prefix",
  //   "function_name",
  //   "column_spec",
  //   "non_reserved_keyword",
  //   "sample_expression_options",
  //   "null_test_operator",
  //   "like_not_like",
  //   "std_trig_funs",
  //   "calendar_fun_name_td_specific",
  //   "business_calendar_fun_td_specific",
  //   "calendar_alias",
  //   "dtm_odbc_transaction_action",
  //   "values_opt",
  //   "mload_task",
  //   "fastexport_spool_option",
  //   "threshold_unit",
  //   "dateform_style",
  //   "query_band_scope",
  //   "isolated_loading_for_scope",
  //   "logging",
  //   "before_journal",
  //   "after_journal",
  //   "checksum_level",
  //   "block_compression_type",
  //   "data_block_size_unit",
  //   "temporal_col_keyword",
  //   "nonsequenced_temporal_col_keyword",
  //   "opt_null",
  //   "fkey_check_option",
  //   "include_data",
  //   "nusi_order_by_type_opt",
  //   "case_n_spec_kws",
  //   "range_spec_kws",
  //   "temp_table_on_commit",
  //   "comment_object_kind",
  //   "query_logging_with_types",
  //   "query_logging_with_opt_kw",
  //   "param_kind",
  //   "sql_security_type",
  //   "function_prop_kws",
  //   "function_language_clause",
  //   "function_deterministic_clause",
  //   "function_called_on_null_clause",
  //   "for_sql_state_opt",
  //   "handler_type",
  //   "handle_condition_kws",
  //   "cursor_ret_opt",
  //   "cursor_type_opt",
  //   "fetch_direction_opt",
  //   "release_opt_kws",
  //   "locking_object_keyword_opt",
  //   "access_spec_kws",
  //   "non_reserved_function_kws",
  //   "period_fun_kws",
  //   "bitwise_operands",
  //   "percentile_fun",
  //   "window_null_treatment_opt"
  // )));

  private Map<String, Integer> featureMap = new HashMap<>();

  @Override
  public void enterEveryRule(ParserRuleContext ctx) {
    String[] split = ctx.getPayload().getClass().toString().split("\\$");
    String value = ctx.getPayload().getText();
    String parseNode = split[split.length - 1].replace("Context", "");
    // if (!ignoredrulesSet.contains(parseNode)) {
    //   if (withValuerulesSet.contains(parseNode)) {
    //     hashSB.append(parseNode).append("|").append(value).append(">");
    //   } else {
    //     hashSB.append(parseNode).append(">");
    //   }
    // }

    addToMap(parseNode);
  }

  public void addToMap(String feature) {
    Integer prev = featureMap.get(feature);
    if (prev == null) {
      featureMap.put(feature, 1);
    } else {
      featureMap.put(feature, prev + 1);
    }
  }

  public Map<String, Integer> getFeatureMap() {
    return featureMap;
  }
}



