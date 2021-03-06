package org.magnos.rekord.field;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.magnos.rekord.Field;
import org.magnos.rekord.FieldLoad;
import org.magnos.rekord.Key;
import org.magnos.rekord.LoadProfile;
import org.magnos.rekord.Model;
import org.magnos.rekord.Rekord;
import org.magnos.rekord.Transaction;
import org.magnos.rekord.Value;
import org.magnos.rekord.query.InsertAction;
import org.magnos.rekord.query.SelectQuery;

public class ManyToOne<T extends Model> extends JoinField<T>
{

	public ManyToOne( String name, int flags )
	{
		super( name, flags );
	}
	
	@Override
	public boolean isSelectable()
	{
		return !is(LAZY);
	}

	@Override
	public Value<T> newValue(Model model)
	{
		return new ManyToOneValue<T>( this, model );
	}
	
	@Override
	public String toString()
	{
	    StringBuilder sb = beginToString();
	    return endToString( sb );
	}
	
	private static class ManyToOneValue<T extends Model> implements Value<T>
	{
		private final ManyToOne<T> field;
		private T value;
		private Key key;
		private Model model;
		private boolean changed = false;
		
		public ManyToOneValue(ManyToOne<T> field, Model model)
		{
			this.field = field;
			this.model = model;
		}
		
		private Key getKey()
		{
			if (key == null)
			{
				key = model.getTable().keyForFields( model, field.getJoinColumns() );
			}
			
			return key;
		}
		
		private void copyBackKey( T value )
		{
			ForeignField<Object>[] foreign = (ForeignField<Object>[])field.getJoinColumns();
			
			for (int i = 0; i < foreign.length; i++)
			{
				model.set( foreign[i], value.get( foreign[i].getForeignColumn() ) );	
			}
		}
		
		private void clearKey()
		{
			for (ForeignField<?> c : field.getJoinColumns())
			{
				model.set( c, null );
			}
		}
		
		@Override
		public T get( Model model )
		{
			if (field.is( LAZY ) && value == null && getKey().exists())
			{
				try
				{
					loadFromKey( FieldLoad.DEFAULT );	
				}
				catch (SQLException e)
				{
					e.printStackTrace();
				}
			}
			
			return value;
		}
		
		@Override
		public boolean hasValue()
		{
			return (value != null);
		}
		
		@Override
		public void set( Model model, T value )
		{
			this.value = value;
			this.changed = true;
			
			if (value != null)
			{
				copyBackKey( value );
			}
			else
			{
				clearKey();
			}
		}
		
		@Override
		public boolean hasChanged()
		{
			return changed;
		}
		
		@Override
		public void clearChanges()
		{
			changed = false;
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
		public void load( FieldLoad fieldLoad ) throws SQLException
		{
			
		}

		@Override
		public void fromSelect( ResultSet results, FieldLoad fieldLoad ) throws SQLException
		{
			
		}
		
		@Override
		public void postSelect(Model model, FieldLoad fieldLoad) throws SQLException
		{
			if (!field.is(LAZY) && getKey().exists())
			{
				loadFromKey( fieldLoad );
			}
		}
		
		@Override
		public void fromResultSet( ResultSet results ) throws SQLException
		{
			
		}
		
		@Override
		public int toPreparedStatement( PreparedStatement preparedStatement, int paramIndex ) throws SQLException
		{
			return paramIndex;
		}
		
		@Override
		public void preSave(Model model) throws SQLException
		{

		}
		
		@Override
		public void postSave(Model model) throws SQLException
		{
		    
		}

        @Override
        public void preDelete(Model model) throws SQLException
        {
            
        }

        @Override
        public void postDelete(Model model) throws SQLException
        {
            
        }

		@Override
		public void serialize(ObjectOutputStream out) throws IOException
		{
			out.writeObject( value );
		}

		@Override
		public void deserialize(ObjectInputStream in) throws IOException, ClassNotFoundException
		{
			value = (T)in.readObject();
			
			if (value != null)
			{
				copyBackKey( value );	
			}
		}
		
		@Override
		public Field<T> getField()
		{
			return field;
		}
		
		@Override
		public String toString()
		{
			return field.getName() + "=" + (value != null ? value.getKey() : "null");
		}
        
        private void loadFromKey( FieldLoad fieldLoad ) throws SQLException
        {
            Key key = getKey();
            Transaction trans = Rekord.getTransaction();
            value = trans.getCached( field.getJoinTable(), key );
            
            LoadProfile load = field.getJoinLoad();
            
            if (fieldLoad != null)
            {
                load = fieldLoad.getLoadProfile( field.getJoinLoad() );
            }
            
            if (value == null)
            {
                // TODO test
                
                SelectQuery<T> select = new SelectQuery<T>( field.getJoinTable() );
                select.select( load );
                select.whereKey( key );
                
                value = select.create().first();
            }
            else
            {
                value.load( load, false );
            }
        }
		
	}

}
