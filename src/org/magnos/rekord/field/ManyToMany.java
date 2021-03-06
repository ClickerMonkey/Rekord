package org.magnos.rekord.field;

import org.magnos.rekord.FieldLoad;
import org.magnos.rekord.Model;
import org.magnos.rekord.Table;
import org.magnos.rekord.Value;
import org.magnos.rekord.query.InsertAction;
import org.magnos.rekord.query.expr.ColumnResolver;

public class ManyToMany<T extends Model> extends AbstractField<T>
{

	protected boolean manageRelationship;
	protected Table relationshipTable;
	protected Table relatedTable;
	
	public ManyToMany( String name, int flags )
	{
		super( name, flags );
	}

	@Override
	public boolean isSelectable()
	{
		return false;
	}
	
	@Override
	public String getSelectExpression(ColumnResolver resolver, FieldLoad fieldLoad)
	{
		return null;
	}
	
    @Override
    public InsertAction getInsertAction()
    {
        return InsertAction.NONE;
    }

    @Override
    public boolean isUpdatable()
    {
        return false;
    }

    @Override
    public String getSaveExpression()
    {
        return null;
    }
    
	@Override
	public Value<T> newValue( Model model )
	{
		return null;
	}

}
