
package org.magnos.rekord.query.expr;

import org.magnos.rekord.Field;
import org.magnos.rekord.Model;
import org.magnos.rekord.field.Column;
import org.magnos.rekord.field.ForeignColumn;
import org.magnos.rekord.query.Operator;
import org.magnos.rekord.query.condition.Condition;
import org.magnos.rekord.query.condition.GroupCondition;
import org.magnos.rekord.query.condition.InCondition;
import org.magnos.rekord.query.condition.OperatorCondition;


public class ModelExpression<R, M extends Model> extends Expression<R, M>
{

	public final Field<M> field;
	public final ForeignColumn<?>[] joinColumns;

	public ModelExpression( R returning, GroupExpression<R> group, String prepend, Field<M> field, ForeignColumn<?>[] joinColumns )
	{
		super( returning, group, prepend );

		this.field = field;
		this.joinColumns = joinColumns;
	}

	@Override
	protected Condition newOperationCondition( Operator op, M value )
	{
		if (joinColumns.length == 1)
		{
			return newOperatorCondition( op, 0, value );
		}

		Condition[] conditions = new Condition[joinColumns.length];

		for (int i = 0; i < joinColumns.length; i++)
		{
			conditions[i] = newOperatorCondition( op, i, value );
		}

		return new GroupCondition( " AND ", conditions );
	}

	protected <T> Condition newOperatorCondition( Operator op, int column, M value )
	{
		return new OperatorCondition<T>( (Column<T>)joinColumns[column], op, (T)value.get( joinColumns[column] ) );
	}

	@Override
	protected String getExpressionString()
	{
		if (joinColumns.length == 1)
		{
			return joinColumns[0].getQuotedName();
		}

		throw new UnsupportedOperationException();
	}

	@Override
	public R between( M min, M max )
	{
		throw new UnsupportedOperationException();
	}

	protected R in( boolean not, M... values )
	{
		if (joinColumns.length == 1)
		{
			Object[] actualValues = new Object[values.length];

			for (int i = 0; i < values.length; i++)
			{
				actualValues[i] = values[i].get( joinColumns[0] );
			}

			return addAndGet( new InCondition<Object>( (Column<Object>)joinColumns[0], not, actualValues ) );
		}

		throw new UnsupportedOperationException();
	}

	@Override
	public R in( M... values )
	{
		return in( false, values );
	}

	@Override
	public R notIn( M... values )
	{
		return in( true, values );
	}

	@Override
	public R nil()
	{
		return returning;
	}

}
