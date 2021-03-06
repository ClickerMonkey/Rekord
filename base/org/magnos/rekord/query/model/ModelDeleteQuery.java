
package org.magnos.rekord.query.model;

import java.sql.SQLException;

import org.magnos.rekord.ListenerEvent;
import org.magnos.rekord.Logging;
import org.magnos.rekord.Model;
import org.magnos.rekord.Rekord;
import org.magnos.rekord.Table;
import org.magnos.rekord.Transaction;
import org.magnos.rekord.Value;
import org.magnos.rekord.query.DeleteQuery;
import org.magnos.rekord.query.QueryTemplate;


public class ModelDeleteQuery implements ModelQuery
{

	protected Table table;
	protected QueryTemplate<Model> queryTemplate;

	public ModelDeleteQuery( Table table )
	{
	    this.table = table;
	    this.queryTemplate = DeleteQuery.forTable( table );
	}

	public boolean execute( Model model ) throws SQLException
	{
		Table parentTable = table.getParentTable();
		Value<?>[] values = model.getValues();
		
		if (parentTable != null)
		{
			values = model.getValues( table.getGivenFields() );
		}
		
		Rekord.log( Logging.DELETES, "%s -> %s", queryTemplate.getQuery(), model.getKey() );

		table.notifyListeners( model, ListenerEvent.PRE_DELETE );
		
		for (Value<?> v : values)
		{
			v.preDelete( model );
		}
		
		boolean deleted = queryTemplate.create().bind( model ).executeUpdate() > 0;

		if (deleted)
		{
		    Transaction trans = Rekord.getTransaction();
		    trans.purge( table, model );
		    
			for (Value<?> v : values)
			{
				v.postDelete( model );
			}

			if (parentTable != null)
			{
				parentTable.getDelete().execute( model );
			}
			
			table.notifyListeners( model, ListenerEvent.POST_DELETE );
		}

		return deleted;
	}

}
