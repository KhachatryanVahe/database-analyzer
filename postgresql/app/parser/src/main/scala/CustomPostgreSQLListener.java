package parser;

import org.antlr.v4.runtime.ParserRuleContext;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;
import java.util.HashMap;
import java.util.Collections;

class CustomPostgreSQLListener extends PostgreSQLParserBaseListener {
  private Map<String, Integer> featureMap = new HashMap<>();

  @Override
  public void enterEveryRule(ParserRuleContext ctx) {
    String[] split = ctx.getPayload().getClass().toString().split("\\$");
    String value = ctx.getPayload().getText();
    String parseNode = split[split.length - 1].replace("Context", "").toLowerCase();
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



