package parser

class CustomPostgreSQLListener extends PostgreSQLBaseListener {
  override def enterSelectStatement(ctx: PostgreSQLParser.SelectStatementContext): Unit = {
    println("Enter Select Statement = " + ctx.getText)
  }

  override def exitSelectStatement(ctx: PostgreSQLParser.SelectStatementContext): Unit = {
    println("Exit Select Statement = " + ctx.getText)
  }

  override def enterSelectList(ctx: PostgreSQLParser.SelectListContext): Unit = {
    println("Enter Select List = " + ctx.getText)
  }

  override def exitSelectList(ctx: PostgreSQLParser.SelectListContext): Unit = {
    println("Exit Select List = " + ctx.getText)
  }

  override def enterFromClause(ctx: PostgreSQLParser.FromClauseContext): Unit = {
    println("Enter From Clause = " + ctx.getText)
  }

  override def exitFromClause(ctx: PostgreSQLParser.FromClauseContext): Unit = {
    println("Exit From Clause = " + ctx.getText)
  }

  override def enterTableList(ctx: PostgreSQLParser.TableListContext): Unit = {
    println("Enter Table List = " + ctx.getText)
  }

  override def exitTableList(ctx: PostgreSQLParser.TableListContext): Unit = {
    println("Exit Table List = " + ctx.getText)
  }

  override def enterTableReference(ctx: PostgreSQLParser.TableReferenceContext): Unit = {
    println("Enter Table Reference: " + ctx.IDENTIFIER.getText)
  }

  override def exitTableReference(ctx: PostgreSQLParser.TableReferenceContext): Unit = {
    println("Exit Table Reference = " + ctx.getText)
  }

  override def enterAlias(ctx: PostgreSQLParser.AliasContext): Unit = {
    println("Enter Alias: " + ctx.IDENTIFIER.getText)
  }

  override def exitAlias(ctx: PostgreSQLParser.AliasContext): Unit = {
    println("Exit Alias = " + ctx.getText)
  }

  override def enterWhereClause(ctx: PostgreSQLParser.WhereClauseContext): Unit = {
    println("Enter Where Clause  = " + ctx.getText)
  }

  override def exitWhereClause(ctx: PostgreSQLParser.WhereClauseContext): Unit = {
    println("Exit Where Clause = " + ctx.getText)
  }

  override def enterCondition(ctx: PostgreSQLParser.ConditionContext): Unit = {
    println("Enter Condition = " + ctx.getText)
  }

  override def exitCondition(ctx: PostgreSQLParser.ConditionContext): Unit = {
    println("Exit Condition = " + ctx.getText)
  }

  override def enterLogicalExpression(ctx: PostgreSQLParser.LogicalExpressionContext): Unit = {
    println("Enter Logical Expression = " + ctx.getText)
  }

  override def exitLogicalExpression(ctx: PostgreSQLParser.LogicalExpressionContext): Unit = {
    println("Exit Logical Expression = " + ctx.getText)
  }

  override def enterExpression(ctx: PostgreSQLParser.ExpressionContext): Unit = {
    println("Enter Expression: " + ctx.getText)
  }

  override def exitExpression(ctx: PostgreSQLParser.ExpressionContext): Unit = {
    println("Exit Expression = " + ctx.getText)
  }

  override def enterRelationalOperator(ctx: PostgreSQLParser.RelationalOperatorContext): Unit = {
    println("Enter Relational Operator: " + ctx.getText)
  }

  override def exitRelationalOperator(ctx: PostgreSQLParser.RelationalOperatorContext): Unit = {
    println("Exit Relational Operator = " + ctx.getText)
  }
}


