
package org.magnos.rekord.type;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.magnos.rekord.Type;


public class TypeDate implements Type<Date>
{

    public static final TypeDate INSTANCE = new TypeDate();

    public static final String DATE_FORMAT = "MM/dd/yyyy";

    public static final ThreadLocal<SimpleDateFormat> DATE_FORMATTER_LOCAL = new ThreadLocal<SimpleDateFormat>()
    {
        protected SimpleDateFormat initialValue()
        {
            return new SimpleDateFormat( DATE_FORMAT );
        }
    };

    @Override
    public String getPartialExpression( String in, int limit, String alias )
    {
        return in;
    }

    @Override
    public Date fromResultSet( ResultSet resultSet, String column, boolean nullable ) throws SQLException
    {
        return resultSet.getDate( column );
    }

    @Override
    public Date fromResultSet( ResultSet resultSet, int column, boolean nullable ) throws SQLException
    {
        return resultSet.getDate( column );
    }

    @Override
    public boolean isPartial( Date value, int limit )
    {
        return false;
    }

    @Override
    public void toPreparedStatement( PreparedStatement preparedStatement, Date value, int paramIndex ) throws SQLException
    {
        if (value == null)
        {
            preparedStatement.setNull( paramIndex, Types.DATE );
        }
        else
        {
            preparedStatement.setDate( paramIndex, new java.sql.Date( value.getTime() ) );
        }
    }

    @Override
    public String toString( Date value )
    {
        return (value == null ? null : DATE_FORMATTER_LOCAL.get().format( value ));
    }

    @Override
    public Date fromString( String x )
    {
        if (x == null)
        {
            return null;
        }

        try
        {
            return new Date( DATE_FORMATTER_LOCAL.get().parse( x ).getTime() );
        }
        catch (ParseException e)
        {
            throw new RuntimeException( e );
        }
    }

    @Override
    public String toQueryString( Date value )
    {
        return "'" + toString( value ) + "'";
    }

}
