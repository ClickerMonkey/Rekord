
package org.magnos.rekord.type;

import java.sql.Clob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.magnos.rekord.Type;


public class TypeClob implements Type<Clob>
{

    public static final TypeClob INSTANCE = new TypeClob();
    
    @Override
    public String getPartialExpression( String in, int limit, String alias )
    {
        return in;
    }

    @Override
    public Clob fromResultSet( ResultSet resultSet, String column, boolean nullable ) throws SQLException
    {
        return resultSet.getClob( column );
    }

    @Override
    public Clob fromResultSet( ResultSet resultSet, int column, boolean nullable ) throws SQLException
    {
        return resultSet.getClob( column );
    }

    @Override
    public boolean isPartial( Clob value, int limit )
    {
        return false;
    }

    @Override
    public void toPreparedStatement( PreparedStatement preparedStatement, Clob value, int paramIndex ) throws SQLException
    {
        preparedStatement.setClob( paramIndex, value );
    }

    @Override
    public String toString( Clob value )
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public Clob fromString( String x )
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public String toQueryString( Clob value )
    {
        return null;
    }

}
