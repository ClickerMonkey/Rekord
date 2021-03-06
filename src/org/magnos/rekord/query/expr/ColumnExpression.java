package org.magnos.rekord.query.expr;

import org.magnos.rekord.field.Column;
import org.magnos.rekord.query.Operator;
import org.magnos.rekord.query.condition.BetweenCondition;
import org.magnos.rekord.query.condition.Condition;
import org.magnos.rekord.query.condition.ConditionResolver;
import org.magnos.rekord.query.condition.InCondition;
import org.magnos.rekord.query.condition.OperatorCondition;

public class ColumnExpression<R, T> extends Expression<R, T>
{
	
	public final Column<T> column;
	public final ColumnResolver columnResolver;
	
	public ColumnExpression(ConditionResolver<R> resolver, Column<T> column, ColumnResolver columnResolver)
	{
	    super( resolver );
	    
		this.column = column;
		this.columnResolver = columnResolver;
	}

	@Override
	protected Condition newOperationCondition(Operator op, T value)
	{
	    return new OperatorCondition<T>( columnResolver, column, op, value );
	}

	@Override
	protected String getExpressionString()
	{
	    return columnResolver.resolve( column );
	}
	
	protected R newBindOperation( Operator op )
	{
	    return resolver.resolve( OperatorCondition.forColumnBind( columnResolver, column, op ) );
	}

    public R eq()
    {
        return newBindOperation( Operator.EQ );
    }

    public R neq()
    {
        return newBindOperation( Operator.LT );
    }

    public R lt()
    {
        return newBindOperation( Operator.LT );
    }

    public R gt()
    {
        return newBindOperation( Operator.GT );
    }

    public R lte()
    {
        return newBindOperation( Operator.LTEQ );
    }

    public R gte()
    {
        return newBindOperation( Operator.GTEQ );
    }
	
	@Override
	public R between( T min, T max )
	{
		return resolver.resolve( new BetweenCondition<T>( columnResolver, column, min, max ) );
	}

	@Override
	public R in( T ... values )
	{
		return resolver.resolve( new InCondition<T>( columnResolver, column, false, values ) );
	}

	@Override
	public R notIn( T ... values )
	{
		return resolver.resolve( new InCondition<T>( columnResolver, column, true, values ) );
	}

	@Override
	public R nil()
	{
		return resolver.resolve( null );
	}
	
}
