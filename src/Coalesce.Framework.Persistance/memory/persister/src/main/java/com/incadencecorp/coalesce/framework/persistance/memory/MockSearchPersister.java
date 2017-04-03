/*-----------------------------------------------------------------------------'
 Copyright 2017 - InCadence Strategic Solutions Inc., All Rights Reserved

 Notwithstanding any contractor copyright notice, the Government has Unlimited
 Rights in this work as defined by DFARS 252.227-7013 and 252.227-7014.  Use
 of this work other than as specifically authorized by these DFARS Clauses may
 violate Government rights in this work.

 DFARS Clause reference: 252.227-7013 (a)(16) and 252.227-7014 (a)(16)
 Unlimited Rights. The Government has the right to use, modify, reproduce,
 perform, display, release or disclose this computer software and to have or
 authorize others to do so.

 Distribution Statement D. Distribution authorized to the Department of
 Defense and U.S. DoD contractors only in support of U.S. DoD efforts.
 -----------------------------------------------------------------------------*/

package com.incadencecorp.coalesce.framework.persistance.memory;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetProvider;

import org.geotools.data.Query;
import org.opengis.filter.expression.PropertyName;

import com.incadencecorp.coalesce.common.exceptions.CoalescePersistorException;
import com.incadencecorp.coalesce.framework.datamodel.CoalesceEntity;
import com.incadencecorp.coalesce.search.api.ICoalesceSearchPersistor;
import com.incadencecorp.coalesce.search.api.SearchResults;
import com.incadencecorp.coalesce.search.factory.CoalescePropertyFactory;
import com.incadencecorp.coalesce.search.resultset.CoalesceResultSet;

public class MockSearchPersister extends MockPersister implements ICoalesceSearchPersistor {

    @Override
    public SearchResults search(Query query) throws CoalescePersistorException
    {
        CachedRowSet rowset;

        List<String> columns = new ArrayList<String>();

        columns.add(CoalescePropertyFactory.getColumnName(CoalescePropertyFactory.getEntityKey()));
        columns.add(CoalescePropertyFactory.getColumnName(CoalescePropertyFactory.getName()));
        columns.add(CoalescePropertyFactory.getColumnName(CoalescePropertyFactory.getSource()));
        columns.add(CoalescePropertyFactory.getColumnName(CoalescePropertyFactory.getEntityType()));
        columns.add(CoalescePropertyFactory.getColumnName(CoalescePropertyFactory.getEntityTitle()));

        if (query != null && query.getProperties() != null)
        {
            for (PropertyName property : query.getProperties())
            {
                columns.add(property.getPropertyName());
            }
        }

        List<Object[]> rows = new ArrayList<Object[]>();

        for (CoalesceEntity entity : getEntity(keys.toArray(new String[keys.size()])))
        {
            Object[] data = new Object[columns.size()];

            data[0] = entity.getKey();
            data[1] = entity.getName();
            data[2] = entity.getSource();
            data[4] = entity.getTitle();

            rows.add(data);
        }

        try
        {
            rowset = RowSetProvider.newFactory().createCachedRowSet();
            rowset.populate(new CoalesceResultSet(rows.iterator(), columns.toArray(new String[columns.size()])));
        }
        catch (SQLException e)
        {
            throw new CoalescePersistorException("Failed", e);
        }

        SearchResults results = new SearchResults();
        results.setResults(rowset);

        return results;
    }
}