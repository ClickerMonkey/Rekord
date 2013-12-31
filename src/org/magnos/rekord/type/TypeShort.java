
package org.magnos.rekord.type;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import org.magnos.rekord.Type;


public class TypeShort implements Type<Short>
{
    
    public static final TypeShort INSTANCE = new TypeShort();

    @Override
    public String getPartialExpression( String in, int limit )
    {
        return in;
    }

    @Override
    public Short fromResultSet( ResultSet resultSet, String column, boolean nullable ) throws SQLException
    {
        short value = resultSet.getShort( column );

        return (nullable && resultSet.wasNull() ? null : value);
    }

    @Override
    public Short fromResultSet( ResultSet resultSet, int column, boolean nullable ) throws SQLException
    {
        short value = resultSet.getShort( column );

        return (nullable && resultSet.wasNull() ? null : value);
    }

    @Override
    public boolean isPartial( Short value, int limit )
    {
        return false;
    }

    @Override
    public void toPreparedStatement( PreparedStatement preparedStatement, Short value, int paramIndex ) throws SQLException
    {
        if (value == null)
        {
            preparedStatement.setNull( paramIndex, Types.SMALLINT );
        }
        else
        {
            preparedStatement.setShort( paramIndex, value );
        }
    }

}