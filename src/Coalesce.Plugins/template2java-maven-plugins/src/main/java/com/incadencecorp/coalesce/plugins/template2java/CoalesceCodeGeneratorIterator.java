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

package com.incadencecorp.coalesce.plugins.template2java;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import com.incadencecorp.coalesce.common.exceptions.CoalesceException;
import com.incadencecorp.coalesce.framework.datamodel.CoalesceEntity;
import com.incadencecorp.coalesce.framework.datamodel.CoalesceEntityTemplate;
import com.incadencecorp.coalesce.framework.datamodel.CoalesceRecordset;
import com.incadencecorp.coalesce.framework.iterators.CoalesceIterator;
import com.incadencecorp.coalesce.mapper.impl.FieldMapperImpl;

/**
 * TODO Nested Sections not supported
 * 
 * @author Derek Clemenzi
 *
 */
public class CoalesceCodeGeneratorIterator extends CoalesceIterator<List<CoalesceRecordset>> {

    private VelocityEngine ve;
    private Path directory;
    private Path entityfolder;
    private String packagename;

    public CoalesceCodeGeneratorIterator(Path directory)
    {
        // Configure Engine to read templates from resources
        Properties p = new Properties();
        p.setProperty("resource.loader", "class");
        p.setProperty("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");

        ve = new VelocityEngine();
        ve.init(p);

        this.directory = directory;
    }

    public void generateCode(CoalesceEntityTemplate template) throws CoalesceException
    {
        generateCode(template.createNewEntity());
    }

    public void generateCode(CoalesceEntity entity) throws CoalesceException
    {
        packagename = entity.getClassName().substring(0, entity.getClassName().lastIndexOf("."));
        entityfolder = directory.resolve(Paths.get("generated", packagename.split("[.]")));

        List<CoalesceRecordset> recordsets = new ArrayList<CoalesceRecordset>();

        processAllElements(entity, recordsets);

        ClassNameNormalizer normalizer = new ClassNameNormalizer();

        String normalizedName = normalizer.normalize(entity.getName());

        if (NumberUtils.isNumber(normalizedName.substring(0, 1)))
        {
            throw new CoalesceException("Recordset names must start with a character");
        }

        // Set Parameters
        VelocityContext context = new VelocityContext();
        context.put("packagename_root", packagename);
        context.put("entity", entity);
        context.put("normalizer", normalizer);
        context.put("recordsets", recordsets);

        context.put("packagename_sub", "api.entity");
        context.put("classname", "I" + normalizedName + "Entity");
        createFile(ve.getTemplate("entity-api.vm"), context);

        context.put("packagename_sub", "impl.entity.coalesce");
        context.put("classname", normalizedName + "CoalesceEntity");
        createFile(ve.getTemplate("entity-coalesce.vm"), context);

        context.put("packagename_sub", "impl.entity.pojo");
        context.put("classname", normalizedName + "PojoEntity");
        createFile(ve.getTemplate("entity-pojo.vm"), context);
    }

    @Override
    protected boolean visitCoalesceRecordset(CoalesceRecordset recordset, List<CoalesceRecordset> params)
            throws CoalesceException
    {
        params.add(recordset);

        ClassNameNormalizer normalizer = new ClassNameNormalizer();

        String normalizedName = normalizer.normalize(recordset.getName());

        if (NumberUtils.isNumber(normalizedName.substring(0, 1)))
        {
            throw new CoalesceException("Recordset names must start with a character");
        }

        // Set Parameters
        VelocityContext context = new VelocityContext();
        context.put("packagename_root", packagename);
        context.put("name", normalizedName);
        context.put("recordset", recordset);
        context.put("normalizer", normalizer);
        context.put("fieldmapper", new FieldMapperImpl());
        context.put("typemapper", new ReturnTypeMapper());

        context.put("packagename_sub", "api.records");
        context.put("classname", "I" + normalizedName + "Record");
        createFile(ve.getTemplate("record-api.vm"), context);

        context.put("packagename_sub", "impl.records.coalesce");
        context.put("classname", normalizedName + "CoalesceRecord");
        createFile(ve.getTemplate("record-coalesce.vm"), context);

        context.put("packagename_sub", "impl.records.pojo");
        context.put("classname", normalizedName + "PojoRecord");
        createFile(ve.getTemplate("record-pojo.vm"), context);

        return false;
    }

    private void createFile(Template t, VelocityContext context) throws CoalesceException
    {
        String filename = context.get("classname").toString() + ".java";
        String packagename = context.get("packagename_root").toString() + "." + context.get("packagename_sub").toString();

        Path file = directory.resolve(Paths.get("generated", packagename.split("[.]"))).resolve(filename);

        try (StringWriter writer = new StringWriter())
        {
            t.merge(context, writer);
            FileUtils.writeStringToFile(new File(file.toUri()), writer.toString());
        }
        catch (IOException e)
        {
            throw new CoalesceException(e);
        }
    }

}