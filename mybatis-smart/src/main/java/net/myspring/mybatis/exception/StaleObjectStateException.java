package net.myspring.mybatis.exception;

import java.io.Serializable;

/**
 * Created by liuj on 2016-08-23.
 */
public class StaleObjectStateException extends RuntimeException {

    private final String entityName;
    private final Serializable identifier;

    /**
     * Constructs a StaleObjectStateException using the supplied information.
     *
     * @param entityName The name of the entity
     * @param identifier The identifier of the entity
     */
    public StaleObjectStateException(String entityName, Serializable identifier) {
        super( "Row was updated or deleted by another transaction (or unsaved-value mapping was incorrect)" );
        this.entityName = entityName;
        this.identifier = identifier;
    }

    public String getEntityName() {
        return entityName;
    }

    public Serializable getIdentifier() {
        return identifier;
    }

    public String getMessage() {
        return super.getMessage() + " : " + infoString( entityName, identifier );
    }

    /**
     * Generate an info message string relating to a particular entity,
     * based on the given entityName and id.
     *
     * @param entityName The defined entity name.
     * @param id The entity id value.
     * @return An info string, in the form [FooBar#1].
     */
    private String infoString(String entityName, Serializable id) {
        StringBuilder s = new StringBuilder();
        s.append( '[' );
        if( entityName == null ) {
            s.append( "<null entity name>" );
        }
        else {
            s.append( entityName );
        }
        s.append( '#' );

        if ( id == null ) {
            s.append( "<null>" );
        }
        else {
            s.append( id );
        }
        s.append( ']' );

        return s.toString();
    }
}
