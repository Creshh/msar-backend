package de.tuchemnitz.tomkr.metaapp.service.graphql;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import de.tuchemnitz.tomkr.metaapp.model.MetaFile;

@Service
@Scope("singleton")
public class MetaFileCollection{
    private static Logger LOG = LoggerFactory.getLogger(MetaFileCollection.class);

    public List<MetaFile> metaFiles;

    public MetaFileCollection() {
        LOG.debug("MetaFileCollection constructor");
        metaFiles = new ArrayList<>();
        metaFiles.add(new MetaFile("name1", "location1"));
        metaFiles.add(new MetaFile("name2", "location2"));
    }

    public MetaFileCollection getMetaFileCollection(){
        return this;
    }
}