/**
 * ///-----------SECURITY CLASSIFICATION: UNCLASSIFIED------------------------
 * /// Copyright 2016 - Lockheed Martin Corporation, All Rights Reserved /// ///
 * Notwithstanding any contractor copyright notice, the government has ///
 * Unlimited Rights in this work as defined by DFARS 252.227-7013 and ///
 * 252.227-7014. Use of this work other than as specifically authorized by ///
 * these DFARS Clauses may violate government rights in this work. /// /// DFARS
 * Clause reference: 252.227-7013 (a)(16) and 252.227-7014 (a)(16) /// Unlimited
 * Rights. The Government has the right to use, modify, /// reproduce, perform,
 * display, release or disclose this computer software /// in whole or in part,
 * in any manner, and for any purpose whatsoever, /// and to have or authorize
 * others to do so. /// /// Distribution Statement D. Distribution authorized to
 * the Department of /// Defense and U.S. DoD contractors only in support of US
 * DoD efforts. /// Other requests shall be referred to the ACINT Modernization
 * Program /// Management under the Director of the Office of Naval
 * Intelligence. ///
 * -------------------------------UNCLASSIFIED---------------------------------
 */

package com.incadencecorp.coalesce.synchronizer.api.common.mocks;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetProvider;

import org.geotools.data.Query;
import org.opengis.filter.expression.PropertyName;

import com.incadencecorp.coalesce.common.exceptions.CoalescePersistorException;
import com.incadencecorp.coalesce.framework.datamodel.CoalesceEntity;
import com.incadencecorp.coalesce.framework.persistance.CoalesceParameter;
import com.incadencecorp.coalesce.framework.persistance.MockPersister;
import com.incadencecorp.coalesce.search.api.ICoalesceSearchPersistor;
import com.incadencecorp.coalesce.search.factory.CoalescePropertyFactory;
import com.incadencecorp.coalesce.search.resultset.CoalesceResultSet;

/**
 * This mock implementation extends the Mock persistor and implements the search
 * interface.
 * 
 * @author n78554
 *
 */
public class MockSearchPersister extends MockPersister implements ICoalesceSearchPersistor {

    /**
     * Returns the key and title of every entities saved.
     */
    @Override
    public CachedRowSet search(Query query, CoalesceParameter... parameters) throws CoalescePersistorException
    {
        CachedRowSet results;

        List<String> columns = new ArrayList<String>();

        if (query != null && query.getProperties() != null)
        {
            for (PropertyName property : query.getProperties())
            {
                columns.add(property.getPropertyName());
            }
        }

        List<Object[]> rows = new ArrayList<Object[]>();

        int objectKeyPos = columns.indexOf(CoalescePropertyFactory.getEntityKey().getPropertyName());
        int titlePos = columns.indexOf(CoalescePropertyFactory.getEntityTitle().getPropertyName());

        for (CoalesceEntity entity : getEntity(keys.toArray(new String[keys.size()])))
        {
            Object[] data = new Object[columns.size()];

            if (objectKeyPos != -1)
            {
                data[objectKeyPos] = entity.getKey();
            }

            if (titlePos != -1)
            {
                data[titlePos] = entity.getTitle();
            }

            rows.add(data);
        }

        try
        {
            results = RowSetProvider.newFactory().createCachedRowSet();
            results.populate(new CoalesceResultSet(rows.iterator(), columns.toArray(new String[columns.size()])));
        }
        catch (SQLException e)
        {
            throw new CoalescePersistorException("Failed", e);
        }

        return results;
    }

}