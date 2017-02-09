/**
 * ///-----------SECURITY CLASSIFICATION: UNCLASSIFIED------------------------
 * ///--------------------------------------------------------------------------
 * ---' Copyright 2016 - InCadence Strategic Solutions Inc., All Rights Reserved
 * Notwithstanding any contractor copyright notice, the Government has Unlimited
 * Rights in this work as defined by DFARS 252.227-7013 and 252.227-7014. Use of
 * this work other than as specifically authorized by these DFARS Clauses may
 * violate Government rights in this work. DFARS Clause reference: 252.227-7013
 * (a)(16) and 252.227-7014 (a)(16) Unlimited Rights. The Government has the
 * right to use, modify, reproduce, perform, display, release or disclose this
 * computer software and to have or authorize others to do so. Distribution
 * Statement D. Distribution authorized to the Department of Defense and U.S.
 * DoD contractors only in support of U.S. DoD efforts.
 * -----------------------------------------------------------------------------
 * -------------------------------UNCLASSIFIED---------------------------------
 */

package com.incadencecorp.coalesce.framework.persistance;

import java.util.List;

import com.incadencecorp.coalesce.common.exceptions.CoalescePersistorException;

/**
 * @author n67152
 */
public interface ICoalesceIndexingPersistor {

    /**
     * @param fieldPath This is in the format: recordset.fieldname
     * @param indexName
     * @param concurrently
     * @throws CoalescePersistorException
     */
    public void createIndex(String fieldPath, String indexName, boolean concurrently) throws CoalescePersistorException;

    /**
     * @param indexName
     * @return
     * @throws CoalescePersistorException
     */
    public boolean indexExists(String indexName) throws CoalescePersistorException;

    /**
     * @param indexName
     * @throws CoalescePersistorException
     */
    public void deleteIndex(String indexName, boolean concurrently) throws CoalescePersistorException;

    /**
     * @param indexName
     * @throws CoalescePersistorException
     */
    public void reindex(String indexName, boolean concurrently) throws CoalescePersistorException;

    /**
     * @return A list of indexNames for each index in the current schema
     * @throws CoalescePersistorException
     */
    public List<String> listIndexes() throws CoalescePersistorException;
}
